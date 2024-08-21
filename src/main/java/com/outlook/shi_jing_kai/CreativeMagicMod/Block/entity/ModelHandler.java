package com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity;


import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.dataset.DataSet;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.KFoldIterator;
import java.util.ArrayList;
import java.util.List;
import org.deeplearning4j.util.ModelSerializer;
import java.io.File;

public class ModelHandler {

    public MultiLayerNetwork createModel() {
        NeuralNetConfiguration.ListBuilder listBuilder = new NeuralNetConfiguration.Builder()
                .seed(12345)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Adam(0.001))
                .l2(0.001)
                .list();

        listBuilder.layer(0, new ConvolutionLayer.Builder(3, 3)
                .nIn(1)
                .stride(1, 1)
                .nOut(64)
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

        listBuilder.layer(7, new OutputLayer.Builder(LossFunctions.LossFunction.SPARSE_MCXENT)
                .nOut(16)
                .activation(Activation.SOFTMAX)
                .build());

        MultiLayerNetwork model = new MultiLayerNetwork(listBuilder.build());
        model.init();

        return model;
    }


    public List<Double> crossValidateModel(DataSetIterator dataIter, int numFolds) {
        List<Double> foldAccuracies = new ArrayList<>();
        KFoldIterator kFoldIterator = new KFoldIterator(numFolds, (DataSet) dataIter);

        while(kFoldIterator.hasNext()) {
            DataSet fold = kFoldIterator.next();
            MultiLayerNetwork model = createModel();
            model.fit(fold);

            double accuracy = model.evaluate((DataSetIterator) fold).accuracy();
            foldAccuracies.add(accuracy);
        }

        return foldAccuracies;
    }

    public void saveModel(MultiLayerNetwork model, String modelPath) throws Exception {
        File locationToSave = new File(modelPath);
        ModelSerializer.writeModel(model, locationToSave, true);
    }

    public static void main(String[] args) throws Exception {
        File datasetDir = new File("C:\\Users\\shi_j\\Desktop\\CreativeMagicMod\\src\\main\\java\\com\\outlook\\shi_jing_kai\\CreativeMagicMod\\Block\\entity\\model");
        DataPreprocessor dataPreprocessor = new DataPreprocessor();
        DataSetIterator dataIter = dataPreprocessor.loadData(datasetDir);

        ModelHandler modelHandler = new ModelHandler();

        List<Double> accuracies = modelHandler.crossValidateModel(dataIter, 8);
        System.out.println("Cross-validation accuracies: " + accuracies);
        System.out.println("Mean accuracy: " + accuracies.stream().mapToDouble(a -> a).average().orElse(0.0));

        MultiLayerNetwork model = modelHandler.createModel();
        model.fit(dataIter);
        modelHandler.saveModel(model, "rune_eyes.zip");
    }


}
