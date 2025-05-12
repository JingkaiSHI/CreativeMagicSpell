package com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity;

import ai.onnxruntime.*;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class RuneClassifier {
    private static final String MODEL_PATH = "assets/creativemagicmod/models/rune_classifier.onnx";
    private static OrtEnvironment environment;
    private static OrtSession session;

    public static void initialize() {
        try {
            // Extract model to a temp location we can access with file paths
            Path tempDir = FMLPaths.GAMEDIR.get().resolve("config/creativemagicmod/models");
            tempDir.toFile().mkdirs();
            Path modelPath = tempDir.resolve("rune_classifier.onnx");

            // Copy from classpath resources to the temp location if it doesn't exist
            if (!modelPath.toFile().exists()) {
                try (InputStream is = RuneClassifier.class.getClassLoader().getResourceAsStream(MODEL_PATH)) {
                    if (is == null) {
                        throw new RuntimeException("Could not find model in resources: " + MODEL_PATH);
                    }
                    Files.copy(is, modelPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }

            // Initialize ONNX Runtime with the extracted model file
            environment = OrtEnvironment.getEnvironment();
            session = environment.createSession(modelPath.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() throws OrtException {
        if (session != null) {
            session.close();
        }
        if (environment != null) {
            environment.close();
        }
    }

    public static int classifyRune(float[][][][] inputData) {
        try {
            if (environment == null || session == null) {
                initialize();
            }

            OnnxTensor inputTensor = OnnxTensor.createTensor(environment, inputData);
            OrtSession.Result result = session.run(
                    java.util.Collections.singletonMap("input", inputTensor)
            );

            float[][] output = (float[][]) result.get(0).getValue();
            inputTensor.close();

            // Find the index with the highest probability
            int bestClass = 0;
            float bestScore = output[0][0];
            for (int i = 1; i < output[0].length; i++) {
                if (output[0][i] > bestScore) {
                    bestScore = output[0][i];
                    bestClass = i;
                }
            }
            return bestClass;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Your existing loadImageAsTensor method can remain
    // But add a method to convert canvas state to tensor
    public static float[][][][] canvasToTensor(int[][] canvasState) {
        float[][][][] tensor = new float[1][1][16][16];
        for (int y = 0; y < canvasState.length; y++) {
            for (int x = 0; x < canvasState[y].length; x++) {
                tensor[0][0][y][x] = canvasState[y][x] == 1 ? 1.0f : 0.0f;
            }
        }
        return tensor;
    }
}
