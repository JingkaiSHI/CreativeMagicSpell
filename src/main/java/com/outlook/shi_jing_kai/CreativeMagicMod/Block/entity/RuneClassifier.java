package com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity;

import ai.onnxruntime.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class RuneClassifier {
    // the main is kept as a 'how' to use the model
    public static void main(String[] args) throws Exception {
        float[][][][] inputData = loadImageAsTensor("src/main/java/com/outlook/shi_jing_kai/CreativeMagicMod/Block/entity/class_3_03.png");
        try (
                OrtEnvironment environment = OrtEnvironment.getEnvironment();
                OrtSession session = environment.createSession("src/main/java/com/outlook/shi_jing_kai/CreativeMagicMod/Block/entity/model_official/rune_classifier.onnx")){

            OnnxTensor inputTensor = OnnxTensor.createTensor(environment, inputData);
            OrtSession.Result result = session.run(
                    java.util.Collections.singletonMap("input", inputTensor)
            );

            float[][] output = (float[][]) result.get(0).getValue();
            // output is [batchSize, 16] since your final layer = 16 classes
            System.out.println(java.util.Arrays.toString(output[0]));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static float[][][][] loadImageAsTensor(String imagePath) throws Exception {
        // Read the image (16×16)
        BufferedImage image = ImageIO.read(new File(imagePath));

        // Prepare a 4D array: [batch=1, channels=1, height=16, width=16]
        float[][][][] inputData = new float[1][1][16][16];

        // Loop over each pixel
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                int rgb = image.getRGB(x, y);

                // Extract alpha and color channels
                int alpha = (rgb >> 24) & 0xFF;
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // Map "white" => 1.0, "transparent" => 0.0, else 0.0
                if (alpha == 0) {
                    // fully transparent
                    inputData[0][0][y][x] = 0.0f;
                } else {
                    // check if it's white
                    boolean isWhite = (r == 255 && g == 255 && b == 255);
                    inputData[0][0][y][x] = isWhite ? 1.0f : 0.0f;
                }
            }
        }

        return inputData;
    }
}

