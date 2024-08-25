package com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity;

import org.nd4j.linalg.api.ndarray.INDArray;

import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataPreprocessor {

    private static final int IMG_SIZE = 128;
    private static final int BLOCK_SIZE = 16;
    private static final int CHANNELS = 1;
    private static final Random rng = new Random(12345);
    private static final Logger log = LoggerFactory.getLogger(DataPreprocessor.class);

    public DataSet loadDataAsDataSet(File rootDir) throws Exception {
        // Ensure rootDir is a directory
        if (!rootDir.isDirectory()) {
            log.error("The provided root directory is not a directory: " + rootDir.getAbsolutePath());
            return null;
        }

        // Get subdirectories (each representing a class)
        File[] subDirs = rootDir.listFiles(File::isDirectory);
        if (subDirs == null || subDirs.length == 0) {
            log.error("No subdirectories found in the root directory: " + rootDir.getAbsolutePath());
            return null;
        }

        int numClasses = subDirs.length;
        log.info("Number of classes detected: " + numClasses);

        List<DataSet> dataSetList = new ArrayList<>();

        for (int labelIndex = 0; labelIndex < numClasses; labelIndex++) {
            File classDir = subDirs[labelIndex];

            // Get all PNG images in this subdirectory
            File[] imageFiles = classDir.listFiles((dir, name) -> name.endsWith(".png"));
            if (imageFiles == null || imageFiles.length == 0) {
                log.warn("No image files found in directory: " + classDir.getAbsolutePath());
                continue; // Skip this class if no images found
            }

            // Process each image file
            for (File imageFile : imageFiles) {
                log.info("Processing image file: " + imageFile.getName());

                // Load the image
                BufferedImage bufferedImage = ImageIO.read(imageFile);

                if (bufferedImage.getWidth() != IMG_SIZE || bufferedImage.getHeight() != IMG_SIZE) {
                    log.error("Image " + imageFile.getName() + " does not have the expected dimensions of 128x128.");
                    continue;
                }

                // Process each 16x16 block in the image
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        // Extract the 16x16 block and reshape it to [1, 1, 16, 16]
                        INDArray block = Nd4j.zeros(1, 1, BLOCK_SIZE, BLOCK_SIZE);

                        for (int x = 0; x < BLOCK_SIZE; x++) {
                            for (int y = 0; y < BLOCK_SIZE; y++) {
                                int pixel = bufferedImage.getRGB(j * BLOCK_SIZE + y, i * BLOCK_SIZE + x);

                                // Extract the color component and apply the binary rule
                                if ((pixel & 0xFFFFFF) == 0xFFFFFF) {  // If the pixel is pure white
                                    block.putScalar(0, 0, x, y, 1);
                                }
                            }
                        }

                        INDArray label = Nd4j.create(1, numClasses).putScalar(labelIndex, 1);
                        DataSet dataSet = new DataSet(block, label);
                        dataSetList.add(dataSet);
                    }
                }
            }
        }

        // Check if the list is empty before merging
        if (dataSetList.isEmpty()) {
            log.error("No valid DataSet found. Unable to merge an empty dataset.");
            throw new IllegalArgumentException("Unable to merge an empty dataset");
        }

        // Merge all datasets into a single DataSet
        DataSet fullDataSet = DataSet.merge(dataSetList);

        return fullDataSet;
    }
}








