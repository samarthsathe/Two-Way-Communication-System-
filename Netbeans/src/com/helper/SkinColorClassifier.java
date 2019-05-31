/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helper;

import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.io.File;
import javax.imageio.ImageIO;

import swinghelper.image.operations.ImageCropper;
import swinghelper.image.operations.ResizeImage;

/**
 *
 * @author Administrator
 */
public class SkinColorClassifier {

    public int totalSkinArea = 0;

    public boolean[][] skinColor(BufferedImage inputImage) {
        long starttime = System.currentTimeMillis();
        int w = inputImage.getWidth();
        int h = inputImage.getHeight(null);
        boolean[][] matrix = new boolean[h][w];
        totalSkinArea = 0;
        for (int row = 0; row < h; row++) {
            int rowCount = 0;
            for (int col = 0; col < w; col++) {
                int pixel = inputImage.getRGB(col, row);
                boolean skinColor = false;
                float red = (pixel >> 16) & 0xff;
                float green = (pixel >> 8) & 0xff;
                float blue = (pixel) & 0xff;
                if (isSkinColor(red, green, blue)) {
                    matrix[row][col] = true;
                    totalSkinArea++;
                    inputImage.setRGB(col, row, 0);
                } else {
                    matrix[row][col] = false;
                }
            }
        }
        long endtime = System.currentTimeMillis();
        System.out.println("Time Required Technique 1 " + ((endtime - starttime) / 1000));
        return matrix;
    }

    public BufferedImage applySkinColor(BufferedImage inputImage) {
        long starttime = System.currentTimeMillis();
        int w = inputImage.getWidth();
        int h = inputImage.getHeight(null);
        boolean[][] matrix = new boolean[h][w];
        totalSkinArea = 0;
        int start = -1, end = -1;;

        int[] matrixHorizontalCount = new int[h];
        for (int row = 0; row < h; row++) {
            int rowCount = 0;
            for (int col = 0; col < w; col++) {
                int pixel = inputImage.getRGB(col, row);
                boolean skinColor = false;
                float red = (pixel >> 16) & 0xff;
                float green = (pixel >> 8) & 0xff;
                float blue = (pixel) & 0xff;
                if (!isSkinColor(red, green, blue)) {
                    inputImage.setRGB(col, row, 0);
                   
                } else {
                    matrixHorizontalCount[row]++;
                     totalSkinArea++;
                }
            }
            if (start == -1) {
                if (matrixHorizontalCount[row] > 100) {
                    start = row;
                }
            } else {
                if (end == -1 && start != -1 && matrixHorizontalCount[row] < 100) {
                    end = row;
                }
            }
//            System.out.println(" " + row + " " + matrixHorizontalCount[row]);

        }
//        try {
////            System.out.println("Start " + start + " " + end);
////            inputImage = ImageCropper.cropMyImage(inputImage, w, end - start, 0, start);
////            ImageIO.write(inputImage, "jpg", new File("o.jpg"));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        long endtime = System.currentTimeMillis();
        System.out.println("Time Required Technique 1 " + ((endtime - starttime) / 1000));
        return inputImage;
    }

    public boolean isSkinColor(float red, float green, float blue) {
        boolean rgb_classifier = red > 95 && green > 40 && green < 100
                && blue > 20
                && Math.max(Math.max(red, green), blue) - Math.min(Math.min(red, green), blue) > 15
                && Math.abs(red - green) > 15
                && red > green
                && red > blue;


        double n[] = _to_normalized(red, green, blue);
        double nr = n[0], ng = n[1], nb = n[2];

        boolean norm_rgb_classifier = (nr / ng) > 1.185f && ((red * blue) / ((red + green + blue) * (red + green + blue))) > 0.107f && ((red * green) / ((red + green + blue) * (red + green + blue))) > 0.112f;

        double[] hsv = _to_hsv(red, green, blue);
        double h1 = hsv[0], s1 = hsv[1], v1 = hsv[2];
        boolean hsv_classifier = h1 > 0 && h1 < 35 && s1 > 0.23 && s1 < 0.68;

        double[] ycbcr = _to_ycbcr(red, green, blue);

        double y1 = ycbcr[0], cb = ycbcr[1], cr = ycbcr[2];

        boolean ycbcr_classifier = (97.5 <= cb && cb <= 142.5) && (134 <= cr && cr <= 176);

        if (rgb_classifier || norm_rgb_classifier || hsv_classifier || ycbcr_classifier) {
            return true;
        } else {
            return false;
        }
    }

    public double[] _to_normalized(float r, float g, float b) {
        if (r == 0) {
            r = 0.0001f;
        }
        if (g == 0) {
            g = 0.0001f;
        }
        if (b == 0) {
            b = 0.0001f;
        }
        double _sum = r + g + b;
        return new double[]{r / _sum, g / _sum, b / _sum};
    }

    public double[] _to_ycbcr(float r, float g, float b) {
        double y = 0.299 * r + 0.587 * g + 0.114 * b;
        double cb = 128 - 0.168736 * r - 0.331364 * g + 0.5 * b;
        double cr = 128 + 0.5 * r - 0.418688 * g - 0.081312 * b;
        return new double[]{y, cb, cr};
    }

    public double[] _to_hsv(float r, float g, float b) {
        double h = 0;
        double _sum = (r + g + b);
        double _max = Math.max(Math.max(r, g), b);
        double _min = Math.min(Math.min(r, g), b);
        double diff = _max - _min;
        if (_sum == 0) {
            _sum = 0.0001;
        }
        if (_max == r) {
            if (diff == 0) {
                h = Double.MAX_VALUE;
            } else {
                h = (g - b) / diff;
            }
        } else if (_max == g) {
            h = 2 + ((g - r) / diff);
        } else {
            h = 4 + ((r - g) / diff);

        }

        h *= 60;
        if (h < 0) {
            h += 360;
        }
        //[h, 1.0 - (3.0 * (_min / _sum)), (1.0 / 3.0) * _max]
        return new double[]{h, 1.0 - (3.0 * (_min / _sum)), (1.0 / 3.0) * _max};
    }

    public double[] _to_normalized_hsv(float h, float s, float v) {
        if (h == 0) {
            h = 0.0001f;
        }
        if (s == 0) {
            s = 0.0001f;
        }
        if (v == 0) {
            v = 0.0001f;
        }
        double _sum = h + s + v;
        return new double[]{h / 360, s / 100, v / 100};
    }
}
