package com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity.model_component;

import java.util.Random;

public class ConvolutionLayer {
    private final int filterSize;
    private final int numFilters;
    private double[][][] filters;
    private double[][][] filterGradients;

    private double learningRate;
    private int t = 0;
    private double[][][] m;
    private double[][][] v;

    // Constructor to initialize the filters
    public ConvolutionLayer(int filterSize, int numFilters) {
        this.filterSize = filterSize;
        this.numFilters = numFilters;
        this.filters = new double[numFilters][filterSize][filterSize];
        this.filterGradients = new double[numFilters][filterSize][filterSize];
        this.learningRate = 0.0001;
        this.m = new double[numFilters][filterSize][filterSize];
        this.v = new double[numFilters][filterSize][filterSize];
        initializeFilters();
    }

    public double[][][] getFilters() {
        return this.filters;
    }


    // Construction from pre-learned parameter
    public ConvolutionLayer(double[][][] filters){
        this.filters = filters;
        this.numFilters = filters.length;
        // assume that the filter is always a square
        this.filterSize = filters[0].length;
    }

    // Initialize filters with random values
    private void initializeFilters() {
        Random rand = new Random();
        for (int f = 0; f < numFilters; f++) {
            for (int i = 0; i < filterSize; i++) {
                for (int j = 0; j < filterSize; j++) {
                    filters[f][i][j] = rand.nextDouble() * 2 - 1;  // Values between -1 and 1
                }
            }
        }
    }

    public void setFilters(double[][][] filters){
        this.filters = filters;
    }

    // Forward pass for the first convolutional layer (input is double[][])
    public double[][][] forward(double[][] input) {
        int outputSize = input.length - filterSize + 1;
        double[][][] output = new double[numFilters][outputSize][outputSize];

        for (int f = 0; f < numFilters; f++) {
            for (int i = 0; i < outputSize; i++) {
                for (int j = 0; j < outputSize; j++) {
                    double sum = handleSum(f, input, i, j);
                    output[f][i][j] = relu(sum);  // Apply ReLU activation
                }
            }
        }
        return output;
    }

    // Forward pass for subsequent convolutional layers (input is double[][][])
    public double[][][] forward(double[][][] input) {
        int outputSize = input[0].length - filterSize + 1;
        double[][][] output = new double[numFilters][outputSize][outputSize];

        for (int f = 0; f < numFilters; f++) {
            for (double[][] doubles : input) {  // Loop over input feature maps
                for (int i = 0; i < outputSize; i++) {
                    for (int j = 0; j < outputSize; j++) {
                        double sum = handleSum(f, doubles, i, j);
                        output[f][i][j] += sum;  // Accumulate results across input feature maps
                    }
                }
            }

            // Apply ReLU activation to the entire output feature map for each filter
            output[f] = applyReLU(output[f]);
        }
        return output;
    }

    private double handleSum(int f, double[][] doubles, int i, int j) {
        double sum = 0.0;
        for (int fi = 0; fi < filterSize; fi++) {
            for (int fj = 0; fj < filterSize; fj++) {
                sum += doubles[i + fi][j + fj] * filters[f][fi][fj];
            }
        }
        return sum;
    }

    // ReLU activation function
    private double relu(double x) {
        return Math.max(0, x);
    }

    // Apply ReLU activation to a 2D feature map
    private double[][] applyReLU(double[][] featureMap) {
        int height = featureMap.length;
        int width = featureMap[0].length;
        double[][] activatedMap = new double[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                activatedMap[i][j] = relu(featureMap[i][j]);
            }
        }
        return activatedMap;
    }


    // Backward pass for the convolutional layer
    public double[][][] backward(double[][][] input, double[][][] gradientOutput) {
        int depth = input.length;
        int inputSize = input[0].length;
        double[][][] gradientInput = new double[depth][inputSize][inputSize];

        // set filter gradients
        for (int f = 0; f < numFilters; f++) {
            for (int i = 0; i < filterSize; i++) {
                for (int j = 0; j < filterSize; j++) {
                    filterGradients[f][i][j] = 0;  // Reset filter gradients
                }
            }
        }

        // Gradient for filters and input
        for (int f = 0; f < numFilters; f++) {
            for (int d = 0; d < depth; d++) {
                for (int i = 0; i < gradientOutput[0].length; i++) {
                    for (int j = 0; j < gradientOutput[0][0].length; j++) {
                        for (int fi = 0; fi < filterSize; fi++) {
                            for (int fj = 0; fj < filterSize; fj++) {
                                int x = i + fi;
                                int y = j + fj;
                                // Update gradient for filters
                                filters[f][fi][fj] -= learningRate * gradientOutput[f][i][j] * input[d][x][y];
                                // Propagate gradient to input
                                gradientInput[d][x][y] += gradientOutput[f][i][j] * filters[f][fi][fj];
                            }
                        }
                    }
                }
            }
        }
        return gradientInput;
    }

    public void updateWeights() {
        t++;
        for (int f = 0; f < numFilters; f++) {
            for (int i = 0; i < filterSize; i++) {
                for (int j = 0; j < filterSize; j++) {
                    double beta1 = 0.9;
                    m[f][i][j] = beta1 * m[f][i][j] + (1 - beta1) * filterGradients[f][i][j];
                    double beta2 = 0.999;
                    v[f][i][j] = beta2 * v[f][i][j] + (1 - beta2) * Math.pow(filterGradients[f][i][j], 2);

                    // Bias correction
                    double mHat = m[f][i][j] / (1 - Math.pow(beta1, t));
                    double vHat = v[f][i][j] / (1 - Math.pow(beta2, t));
                    double epsilon = 1e-8;
                    filters[f][i][j] -= learningRate * mHat / (Math.sqrt(vHat) + epsilon);  // Update filters
                    filterGradients[f][i][j] = 0;  // Reset gradient after update
                }
            }
        }
    }

    // Overloaded backward method for double[][] input
    public double[][] backward(double[][] input, double[][][] gradientOutput) {
        int outputSize = input.length - filterSize + 1;
        double[][] gradientInput = new double[input.length][input[0].length];

        // set filter gradients
        for (int f = 0; f < numFilters; f++) {
            for (int i = 0; i < filterSize; i++) {
                for (int j = 0; j < filterSize; j++) {
                    filterGradients[f][i][j] = 0;  // Reset filter gradients
                }
            }
        }

        for (int f = 0; f < numFilters; f++) {
            for (int i = 0; i < outputSize; i++) {
                for (int j = 0; j < outputSize; j++) {
                    double gradient = gradientOutput[f][i][j];
                    for (int fi = 0; fi < filterSize; fi++) {
                        for (int fj = 0; fj < filterSize; fj++) {
                            int x = i + fi;
                            int y = j + fj;
                            gradientInput[x][y] += filters[f][fi][fj] * gradient;
                            filterGradients[f][fi][fj] += input[x][y] * gradient;  // Update filter gradients
                        }
                    }
                }
            }
        }
        return gradientInput;
    }
}
