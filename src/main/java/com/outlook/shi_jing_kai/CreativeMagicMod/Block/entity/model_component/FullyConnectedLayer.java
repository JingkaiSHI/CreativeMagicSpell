package com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity.model_component;

import java.util.Random;

public class FullyConnectedLayer {
    private double[][] weights;
    private double[] biases;
    private final double[][] weightGradients;
    private final double[] biasGradients;
    private final int inputSize;
    private final int outputSize;
    private final double[][] mWeights;
    private final double[][] vWeights;
    private final double[] mBiases;
    private final double[] vBiases;
    private int t = 0;

    // Constructor to initialize weights and biases
    public FullyConnectedLayer(int inputSize, int outputSize) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.weights = new double[outputSize][inputSize];
        this.biases = new double[outputSize];
        this.weightGradients = new double[outputSize][inputSize];
        this.biasGradients = new double[outputSize];
        this.mWeights = new double[outputSize][inputSize];
        this.vWeights = new double[outputSize][inputSize];
        this.mBiases = new double[outputSize];
        this.vBiases = new double[outputSize];
        initializeWeights();
    }

    public double[][] getWeights() {
        return this.weights;
    }

    public double[] getBiases() {
        return this.biases;
    }

    public void setWeights(double[][] weights){
        this.weights = weights;
    }

    public void setBiases(double[] biases){
        this.biases = biases;
    }

    // Xavier initialization
    private void initializeWeights() {
        Random rand = new Random();
        double scale = Math.sqrt(2.0 / (inputSize + outputSize));
        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                weights[i][j] = rand.nextGaussian() * scale;
            }
            biases[i] = 0;  // Initialize biases to zero
        }
    }


    // Forward pass through the fully connected layer with ReLU activation
    public double[] forward(double[] input) {
        double[] output = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
            double sum = biases[i];
            for (int j = 0; j < inputSize; j++) {
                sum += input[j] * weights[i][j];
            }
            output[i] = relu(sum);  // Apply ReLU activation
        }
        return output;
    }

    // ReLU activation function
    private double relu(double x) {
        return Math.max(0, x);
    }

    public double[] backward(double[] input, double[] gradientOutput){
        double[] gradientInput = new double[inputSize];

        // Calculate gradients with respect to weights, biases, and input
        for(int i = 0; i < outputSize; i++){
            for(int j = 0; j < inputSize; j++){
                weightGradients[i][j] += gradientOutput[i] * input[j];
            }
            biasGradients[i] += gradientOutput[i];
        }

        // Calculate gradient with respect to input
        for(int j = 0; j < inputSize; j++){
            for(int i = 0; i < outputSize; i++){
                gradientInput[j] += weights[i][j] * gradientOutput[i];
            }
        }

        return gradientInput;
    }

    // Update weights and biases using gradients
    public void updateWeights(){
        t++;
        for(int i = 0; i < outputSize; i++){
            double learningRate = 0.0001;
            double beta1 = 0.9;
            double epsilon = 1e-8;
            double beta2 = 0.999;
            for(int j = 0; j < inputSize; j++){
                // Calculate m_t and v_t for weights
                mWeights[i][j] = beta1 * mWeights[i][j] + (1 - beta1) * weightGradients[i][j];
                vWeights[i][j] = beta2 * vWeights[i][j] + (1 - beta2) * Math.pow(weightGradients[i][j], 2);

                // Bias correction
                double mHat = mWeights[i][j] / (1 - Math.pow(beta1, t));
                double vHat = vWeights[i][j] / (1 - Math.pow(beta2, t));

                // Update weights
                weights[i][j] -= learningRate * mHat / (Math.sqrt(vHat) + epsilon);

                // Reset gradient after update
                weightGradients[i][j] = 0;
            }
            mBiases[i] = beta1 * mBiases[i] + (1 - beta1) * biasGradients[i];
            vBiases[i] = beta2 * vBiases[i] + (1 - beta2) * Math.pow(biasGradients[i], 2);

            double mHat = mBiases[i] / (1 - Math.pow(beta1, t));
            double vHat = vBiases[i] / (1 - Math.pow(beta2, t));

            biases[i] -= learningRate * mHat / (Math.sqrt(vHat) + epsilon);

            // Reset bias gradients after update
            biasGradients[i] = 0;
        }
    }

    // Softmax activation function for final output layer
    public double[] softmax(double[] input) {
        double max = Double.NEGATIVE_INFINITY;
        for (double val : input) {
            max = Math.max(max, val);
        }

        double sum = 0.0;
        double[] expValues = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            expValues[i] = Math.exp(input[i] - max);  // Stability improvement
            sum += expValues[i];
        }

        for (int i = 0; i < expValues.length; i++) {
            expValues[i] /= sum;
        }
        return expValues;
    }
}
