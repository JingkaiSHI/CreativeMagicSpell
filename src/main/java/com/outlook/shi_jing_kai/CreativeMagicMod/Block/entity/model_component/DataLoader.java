package com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity.model_component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    private static final int IMAGE_SIZE = 16;

    public List<List<boolean[][]>> loadAllClasses(String basePath) throws IOException {
        List<List<boolean[][]>> allClassesData = new ArrayList<>();

        // Iterate over each class folder from class_0 to class_15
        for (int classIndex = 0; classIndex < 16; classIndex++) {
            String classPath = String.format("%s/class_%d", basePath, classIndex);
            List<boolean[][]> classData = loadClassData(classPath, classIndex);
            allClassesData.add(classData);
        }
        return allClassesData;
    }

    private List<boolean[][]> loadClassData(String classPath, int classIndex) throws IOException {
        List<boolean[][]> classData = new ArrayList<>();

        // Iterate over each image in the class folder
        for (int sampleIndex = 1; sampleIndex <= 64; sampleIndex++) {
            String filePath = String.format("%s/class_%d_%02d.png", classPath, classIndex, sampleIndex);
            BufferedImage img = ImageIO.read(new File(filePath));
            classData.add(convertToBinaryArray(img));
        }
        return classData;
    }

    public boolean[][] convertToBinaryArray(BufferedImage img) {
        boolean[][] binaryArray = new boolean[IMAGE_SIZE][IMAGE_SIZE];

        for (int y = 0; y < IMAGE_SIZE; y++) {
            for (int x = 0; x < IMAGE_SIZE; x++) {
                int pixel = img.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                // Set true for white pixels, false for transparent or any non-white color
                binaryArray[y][x] = (alpha != 0) && (red == 255 && green == 255 && blue == 255);
            }
        }
        return binaryArray;
    }

    public static void saveBinaryImage(boolean[][] binaryArray, String outputFilePath) {
        int width = binaryArray[0].length;
        int height = binaryArray.length;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Set white for true, black for false
                Color color = binaryArray[y][x] ? Color.WHITE : Color.BLACK;
                img.setRGB(x, y, color.getRGB());
            }
        }

        try {
            ImageIO.write(img, "png", new File(outputFilePath));
            System.out.println("Saved binary image to " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Function to augment a single binary image
    public List<boolean[][]> augmentData(boolean[][] image) {
        List<boolean[][]> augmentedImages = new ArrayList<>();

        // Original image
        augmentedImages.add(image);

        // Rotate 90, 180, 270 degrees
        augmentedImages.add(rotateImage(image, 90));
        augmentedImages.add(rotateImage(image, 180));
        augmentedImages.add(rotateImage(image, 270));

        // Flip horizontally and vertically
        augmentedImages.add(flipImage(image, true));  // Horizontal flip
        augmentedImages.add(flipImage(image, false)); // Vertical flip

        // Translate by 1 pixel in each direction
        augmentedImages.add(translateImage(image, 1, 0));  // Shift right
        augmentedImages.add(translateImage(image, -1, 0)); // Shift left
        augmentedImages.add(translateImage(image, 0, 1));  // Shift down
        augmentedImages.add(translateImage(image, 0, -1)); // Shift up

        return augmentedImages;
    }

    // Rotate images in 3 possible angles
    private boolean[][] rotateImage(boolean[][] image, int degrees) {
        int size = image.length;
        boolean[][] rotated = new boolean[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                switch (degrees) {
                    case 90 -> rotated[j][size - 1 - i] = image[i][j];
                    case 180 -> rotated[size - 1 - i][size - 1 - j] = image[i][j];
                    case 270 -> rotated[size - 1 - j][i] = image[i][j];
                    default -> throw new IllegalArgumentException("Rotation must be 90, 180, or 270 degrees.");
                }
            }
        }
        return rotated;
    }

    // Flip image horizontally or vertically
    private boolean[][] flipImage(boolean[][] image, boolean horizontal) {
        int size = image.length;
        boolean[][] flipped = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (horizontal) {
                    flipped[i][j] = image[i][size - j - 1];
                } else {
                    flipped[i][j] = image[size - i - 1][j];
                }
            }
        }
        return flipped;
    }

    // Translate image by dx and dy pixels
    private boolean[][] translateImage(boolean[][] image, int dx, int dy) {
        int size = image.length;
        boolean[][] translated = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int x = i + dx;
                int y = j + dy;
                if (x >= 0 && x < size && y >= 0 && y < size) {
                    translated[i][j] = image[x][y];
                }
            }
        }
        return translated;
    }

    // Test augmentation methods by visualizing augmented images
    public static void main(String[] args) throws IOException {
        String dataDir = "src/main/java/org/example/data/rune_data";
        DataLoader loader = new DataLoader();
        boolean[][] sampleImage = loader.loadAllClasses(dataDir).get(0).get(0);

        // Augment and save images
        saveBinaryImage(sampleImage, "output/original.png");

        // Flip horizontally and vertically
        boolean[][] flippedHorizontal = loader.flipImage(sampleImage, true);
        saveBinaryImage(flippedHorizontal, "output/flipped_horizontal.png");

        boolean[][] flippedVertical = loader.flipImage(sampleImage, false);
        saveBinaryImage(flippedVertical, "output/flipped_vertical.png");

        // Rotate
        boolean[][] rotated90 = loader.rotateImage(sampleImage, 90);
        saveBinaryImage(rotated90, "output/rotated_90.png");

        boolean[][] rotated180 = loader.rotateImage(sampleImage, 180);
        saveBinaryImage(rotated180, "output/rotated_180.png");

        boolean[][] rotated270 = loader.rotateImage(sampleImage, 270);
        saveBinaryImage(rotated270, "output/rotated_270.png");

        // Translate
        boolean[][] translated = loader.translateImage(sampleImage, 2, 2);
        saveBinaryImage(translated, "output/translated_2_2.png");

    }
}
