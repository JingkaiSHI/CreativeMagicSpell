package com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity;


import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.KFoldIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.util.ModelSerializer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModelHandler {

    public MultiLayerNetwork createModel(int numClass) {
        NeuralNetConfiguration.ListBuilder listBuilder = new NeuralNetConfiguration.Builder()
                .seed(12345)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Adam(0.001))
                .l2(0.001)
                .list();

        // Correctly configure the input shape here
        listBuilder.layer(0, new ConvolutionLayer.Builder(3, 3)
                .nIn(1)  // Number of channels
                .stride(1, 1)
                .nOut(64)  // Number of filters
                .weightInit(WeightInit.XAVIER)
                .activation(Activation.RELU)
                .build());

        listBuilder.layer(1, new ConvolutionLayer.Builder(3, 3)
                .stride(1, 1)
                .nOut(64)
                .activation(Activation.RELU)
                .build());

        listBuilder.layer(2, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX, new int[]{2, 2})
                .build());

        listBuilder.layer(3, new ConvolutionLayer.Builder(3, 3)
                .stride(1, 1)
                .nOut(128)
                .activation(Activation.RELU)
                .build());

        listBuilder.layer(4, new ConvolutionLayer.Builder(3, 3)
                .stride(1, 1)
                .nOut(128)
                .activation(Activation.RELU)
                .build());

        listBuilder.layer(5, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX, new int[]{2, 2})
                .build());

        listBuilder.layer(6, new DenseLayer.Builder().activation(Activation.RELU)
                .nOut(128).build());

        listBuilder.layer(7, new OutputLayer.Builder(LossFunctions.LossFunction.MCXENT)
                .nOut(numClass)  // Dynamically set based on actual number of classes
                .activation(Activation.SOFTMAX)
                .build());

        // Ensure the input shape is correctly interpreted
        MultiLayerNetwork model = new MultiLayerNetwork(listBuilder.setInputType(InputType.convolutional(16, 16, 1)).build());
        model.init();

        return model;
    }


    public List<Double> crossValidateModel(DataSet dataSet, int numFolds, int numClass) {
        List<Double> foldAccuracies = new ArrayList<>();
        KFoldIterator kFoldIterator = new KFoldIterator(numFolds, dataSet);

        while (kFoldIterator.hasNext()) {
            DataSet fold = kFoldIterator.next();
            MultiLayerNetwork model = createModel(numClass);
            model.fit(fold);

            // Evaluate dataset on the fold
            Evaluation eval = new Evaluation(numClass);
            eval.eval(fold.getLabels(), model.output(fold.getFeatures()));
            double accuracy = eval.accuracy();
            foldAccuracies.add(accuracy);
        }

        return foldAccuracies;
    }

    public void saveModel(MultiLayerNetwork model, String modelPath) throws Exception {
        File locationToSave = new File(modelPath);
        ModelSerializer.writeModel(model, locationToSave, true);
    }

    public static void main(String[] args) throws Exception {
        File datasetDir = new File("C:\\Users\\shi_j\\Desktop\\CreativeMagicMod\\src\\main\\java\\com\\outlook\\shi_jing_kai\\CreativeMagicMod\\Block\\entity\\dataset");
        DataPreprocessor dataPreprocessor = new DataPreprocessor();
        DataSet dataSet = dataPreprocessor.loadDataAsDataSet(datasetDir);

        // Determine the number of classes
        int numClasses = dataSet.getLabels().columns();

        ModelHandler modelHandler = new ModelHandler();

        List<Double> accuracies = modelHandler.crossValidateModel(dataSet, 8, numClasses);
        System.out.println("Cross-validation accuracies: " + accuracies);
        System.out.println("Mean accuracy: " + accuracies.stream().mapToDouble(a -> a).average().orElse(0.0));

        MultiLayerNetwork model = modelHandler.createModel(numClasses);
        model.fit(dataSet);
        modelHandler.saveModel(model, "rune_eyes.zip");
    }

    public void visualizeDataSet(DataSet dataSet){
        List list = dataSet.asList();
        for(int i = 0; i < list.size(); i++){
            // handel each input

        }
    }
}

