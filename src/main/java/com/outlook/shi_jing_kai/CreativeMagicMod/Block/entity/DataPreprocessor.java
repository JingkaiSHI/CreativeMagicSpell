package com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity;

import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.Writable;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.transform.ImageTransform;
import org.datavec.image.transform.FlipImageTransform;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.dataset.DataSet;
import org.datavec.image.recordreader.ImageRecordReader;

import java.io.File;
import java.util.Random;

public class DataPreprocessor {

    private static final int IMG_HEIGHT = 128;
    private static final int IMG_WIDTH = 128;
    private static final int CHANNELS = 1;
    private static final int BATCH_SIZE = 32;
    private static final Random rng = new Random(12345);

    public RecordReaderDataSetIterator loadData(File imageDir) throws Exception {
        // Load images using NativeImageLoader
        FileSplit fileSplit = new FileSplit(imageDir, NativeImageLoader.ALLOWED_FORMATS, rng);
        ImageTransform transform = new FlipImageTransform(rng);

        ImageRecordReader recordReader = new ImageRecordReader(IMG_HEIGHT, IMG_WIDTH, CHANNELS);

        recordReader.initialize(fileSplit, transform);

        // Normalize the dataset
        RecordReaderDataSetIterator dataIter = new RecordReaderDataSetIterator(recordReader, BATCH_SIZE);
        ImagePreProcessingScaler preProcessor = new ImagePreProcessingScaler(0, 1);
        dataIter.setPreProcessor(preProcessor);

        return dataIter;
    }
}

