package com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity.model_component;


public class PoolingLayer {
    private final int poolSize;

    public PoolingLayer(int poolSize) {
        this.poolSize = poolSize;
    }

    // Forward pass for max pooling
    public double[][][] forward(double[][][] input) {
        int numFilters = input.length;
        int outputSize = input[0].length / poolSize;
        double[][][] output = new double[numFilters][outputSize][outputSize];

        for (int f = 0; f < numFilters; f++) {
            for (int i = 0; i < outputSize; i++) {
                for (int j = 0; j < outputSize; j++) {
                    double maxVal = Double.NEGATIVE_INFINITY;
                    for (int pi = 0; pi < poolSize; pi++) {
                        for (int pj = 0; pj < poolSize; pj++) {
                            int x = i * poolSize + pi;
                            int y = j * poolSize + pj;
                            maxVal = Math.max(maxVal, input[f][x][y]);
                        }
                    }
                    output[f][i][j] = maxVal;
                }
            }
        }
        return output;
    }

    public double[][][] backward(double[][][] input, double[][][] gradientOutput) {
        int depth = input.length;
        int inputSize = input[0].length;
        double[][][] gradientInput = new double[depth][inputSize][inputSize];

        for (int d = 0; d < depth; d++) {
            for (int i = 0; i < gradientOutput[0].length; i++) {
                for (int j = 0; j < gradientOutput[0][0].length; j++) {
                    double maxVal = Double.NEGATIVE_INFINITY;
                    int maxI = -1, maxJ = -1;

                    // Find max value and its index within the poolSize x poolSize window
                    for (int pi = 0; pi < poolSize; pi++) {
                        for (int pj = 0; pj < poolSize; pj++) {
                            int x = i * poolSize + pi;
                            int y = j * poolSize + pj;
                            if (x < inputSize && y < inputSize) {  // Boundary check
                                if (input[d][x][y] > maxVal) {
                                    maxVal = input[d][x][y];
                                    maxI = x;
                                    maxJ = y;
                                }
                            }
                        }
                    }

                    // Ensure maxI and maxJ are valid before assigning gradient
                    if (maxI >= 0 && maxJ >= 0 && maxI < inputSize && maxJ < inputSize) {
                        gradientInput[d][maxI][maxJ] = gradientOutput[d][i][j];
                    }
                }
            }
        }
        return gradientInput;
    }
}
