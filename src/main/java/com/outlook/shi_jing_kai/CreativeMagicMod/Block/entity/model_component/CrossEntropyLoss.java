package com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity.model_component;

public class CrossEntropyLoss {
    // Calculates the cross-entropy loss for a single prediction
    public double computeLoss(double[] predictions, int targetLabel) {
        double epsilon = 1e-9;  // Small constant for numerical stability
        return -Math.log(predictions[targetLabel] + epsilon);
    }

    // Calculates gradients with respect to the output of softmax layer
    public double[] computeGradient(double[] predictions, int targetLabel) {
        double[] gradient = predictions.clone();
        gradient[targetLabel] -= 1.0;  // dL/dy_i = y_i - target
        return gradient;
    }
}
