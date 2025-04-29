package com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity;


import com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity.model_component.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelHandler {


    // current directory: /Users/herobrine/IdeaProjects/CreativeMagicSpell
    // write to this directory for the model
    public static void main(String[] args) throws Exception {
        String dataDir = "src/main/java/com/outlook/shi_jing_kai/CreativeMagicMod/Block/entity/rune_data";
        DataLoader loader = new DataLoader();

        // Define a deeper CNN architecture with more layers and filters
        ConvolutionLayer convLayer1 = new ConvolutionLayer(3, 8);  // 8 filters in the first layer
        PoolingLayer poolLayer1 = new PoolingLayer(2);  // 2x2 pooling after the first convolution layer
        ConvolutionLayer convLayer2 = new ConvolutionLayer(3, 16);  // 16 filters in the second layer
        PoolingLayer poolLayer2 = new PoolingLayer(2);  // Another pooling layer

        // Fully connected layer with input size adjusted to match pooled output dimensions
        FullyConnectedLayer fcLayer = new FullyConnectedLayer(64, 16);  // Final output size of 16 classes
        CrossEntropyLoss lossFunction = new CrossEntropyLoss();

        int epochs = 30;
        int kFolds = 5;

        try {
            // Load and shuffle data
            List<List<boolean[][]>> data = loader.loadAllClasses(dataDir);
            List<Pair<boolean[][], Integer>> dataPairs = new ArrayList<>();
            for (int label = 0; label < data.size(); label++) {
                for (boolean[][] sample : data.get(label)) {
                    dataPairs.add(new Pair<>(sample, label));  // Pair each sample with its label
                }
            }

            // Shuffle the list of pairs
            Collections.shuffle(dataPairs);

            // Separate back into samples and labels
            List<boolean[][]> allSamples = new ArrayList<>();
            List<Integer> allLabels = new ArrayList<>();
            for (Pair<boolean[][], Integer> pair : dataPairs) {
                allSamples.add(pair.key());
                allLabels.add(pair.value());
            }

            // Split into k folds for cross-validation
            int foldSize = allSamples.size() / kFolds;
            double totalAccuracy = 0.0;

            for (int fold = 0; fold < kFolds; fold++) {
                System.out.printf("Fold %d/%d%n", fold + 1, kFolds);

                // Prepare training and validation data for the fold
                List<boolean[][]> trainSamples = new ArrayList<>();
                List<Integer> trainLabels = new ArrayList<>();
                List<boolean[][]> valSamples = new ArrayList<>();
                List<Integer> valLabels = new ArrayList<>();

                for (int i = 0; i < allSamples.size(); i++) {
                    if (i >= fold * foldSize && i < (fold + 1) * foldSize) {
                        valSamples.add(allSamples.get(i));
                        valLabels.add(allLabels.get(i));
                    } else {
                        trainSamples.add(allSamples.get(i));
                        trainLabels.add(allLabels.get(i));
                    }
                }

                // Training phase for this fold
                for (int epoch = 1; epoch <= epochs; epoch++) {
                    double totalLoss = 0.0;

                    for (int i = 0; i < trainSamples.size(); i++) {
                        boolean[][] sampleImage = trainSamples.get(i);
                        int label = trainLabels.get(i);

                        // Convert binary boolean array to double for compatibility
                        double[][] doubleImage = booleanToDouble(sampleImage);

                        // Forward pass through layers
                        double[][][] convOutput1 = convLayer1.forward(doubleImage);
                        double[][][] pooledOutput1 = poolLayer1.forward(convOutput1);
                        double[][][] convOutput2 = convLayer2.forward(pooledOutput1);
                        double[][][] pooledOutput2 = poolLayer2.forward(convOutput2);
                        double[] flattenedInput = flatten(pooledOutput2);
                        double[] fcOutput = fcLayer.forward(flattenedInput);
                        double[] probabilities = fcLayer.softmax(fcOutput);

                        // Calculate loss
                        double loss = lossFunction.computeLoss(probabilities, label);
                        totalLoss += loss;

                        // Backward pass
                        double[] lossGradient = lossFunction.computeGradient(probabilities, label);
                        double[] fcGradient = fcLayer.backward(flattenedInput, lossGradient);  // Gradient from the fully connected layer

                        // Propagate gradients backward through the pooling and convolution layers
                        double[][][] pooledGradient2 = poolLayer2.backward(convOutput2, reshapeTo3D(fcGradient, 16, 2, 2));  // Convert fcGradient to match pooled output size
                        double[][][] convGradient2 = convLayer2.backward(pooledOutput1, pooledGradient2);
                        double[][][] pooledGradient1 = poolLayer1.backward(convOutput1, convGradient2);
                        convLayer1.backward(doubleImage, pooledGradient1);

                        // Update weights
                        fcLayer.updateWeights();
                        convLayer2.updateWeights();
                        convLayer1.updateWeights();
                    }
                    System.out.printf("Epoch %d - Fold %d - Average Loss: %.4f%n", epoch, fold + 1, totalLoss / trainSamples.size());
                }

                // Validation phase for this fold
                int correctPredictions = 0;
                for (int i = 0; i < valSamples.size(); i++) {
                    boolean[][] sampleImage = valSamples.get(i);
                    int label = valLabels.get(i);

                    // Convert binary boolean array to double for compatibility
                    double[][] doubleImage = booleanToDouble(sampleImage);

                    // Forward pass only
                    double[][][] convOutput1 = convLayer1.forward(doubleImage);
                    double[][][] pooledOutput1 = poolLayer1.forward(convOutput1);
                    double[][][] convOutput2 = convLayer2.forward(pooledOutput1);
                    double[][][] pooledOutput2 = poolLayer2.forward(convOutput2);
                    double[] flattenedInput = flatten(pooledOutput2);
                    double[] fcOutput = fcLayer.forward(flattenedInput);
                    double[] probabilities = fcLayer.softmax(fcOutput);

                    // Get the predicted class
                    int predictedLabel = getPredictedLabel(probabilities);

                    if (predictedLabel == label) {
                        correctPredictions++;
                    }
                }

                // Calculate and log accuracy for this fold
                double accuracy = (double) correctPredictions / valSamples.size() * 100;
                System.out.printf("Fold %d - Validation Accuracy: %.2f%%%n", fold + 1, accuracy);
                totalAccuracy += accuracy;
            }

            // Average accuracy across all folds
            System.out.printf("5-Fold Cross-Validation Accuracy: %.2f%%%n", totalAccuracy / kFolds);

        } catch (IOException e) {
            System.out.println("Error reading data!");
        }

        // save and override the existing test model
        saveModel(convLayer1, convLayer2, fcLayer, "src/main/java/com/outlook/shi_jing_kai/CreativeMagicMod/Block/entity/model_test/rune_classifier_001.json");
    }

    // Helper method to convert boolean[][] to double[][] for convolution compatibility
    public static double[][] booleanToDouble(boolean[][] input) {
        int height = input.length;
        int width = input[0].length;
        double[][] output = new double[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                output[i][j] = input[i][j] ? 1.0 : 0.0;
            }
        }
        return output;
    }

    // Helper method to flatten the pooled output
    public static double[] flatten(double[][][] featureMaps) {
        int depth = featureMaps.length;
        int height = featureMaps[0].length;
        int width = featureMaps[0][0].length;
        double[] flatArray = new double[depth * height * width];

        int index = 0;
        for (int d = 0; d < depth; d++) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    flatArray[index++] = featureMaps[d][i][j];
                }
            }
        }
        return flatArray;
    }

    // Helper to reshape a 1D gradient array back to 3D for compatibility
    public static double[][][] reshapeTo3D(double[] input, int depth, int height, int width) {
        double[][][] output = new double[depth][height][width];
        int index = 0;
        for (int d = 0; d < depth; d++) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    output[d][i][j] = input[index++];
                }
            }
        }
        return output;
    }

    // Helper method to find the predicted class
    public static int getPredictedLabel(double[] probabilities) {
        int predictedLabel = 0;
        double maxProb = probabilities[0];
        for (int i = 1; i < probabilities.length; i++) {
            if (probabilities[i] > maxProb) {
                maxProb = probabilities[i];
                predictedLabel = i;
            }
        }
        return predictedLabel;
    }

    public static void saveModel(ConvolutionLayer convLayer1, ConvolutionLayer convLayer2, FullyConnectedLayer fcLayer, String outputFilePath) {
        try {
            StringBuilder json = new StringBuilder("{\n");

            // Save conv1 filters
            json.append("  \"conv1\": ").append(arrayToJson(convLayer1.getFilters())).append(",\n");

            // Save conv2 filters
            json.append("  \"conv2\": ").append(arrayToJson(convLayer2.getFilters())).append(",\n");

            // Save fully connected layer weights and biases
            json.append("  \"fcWeights\": ").append(arrayToJson(fcLayer.getWeights())).append(",\n");
            json.append("  \"fcBiases\": ").append(arrayToJson(fcLayer.getBiases())).append("\n");

            json.append("}");

            // Write to file
            Files.write(Paths.get(outputFilePath), json.toString().getBytes());
            System.out.println("Model saved to " + outputFilePath);
        } catch (IOException e) {
            System.out.println("Error saving model: " + e.getMessage());
        }
    }

    // Helper to convert arrays to JSON-like string
    private static String arrayToJson(double[][][] array) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(arrayToJson(array[i]));
            if (i < array.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private static String arrayToJson(double[][] array) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(arrayToJson(array[i]));
            if (i < array.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private static String arrayToJson(double[] array) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

}

record Pair<K, V>(K key, V value) {
}


