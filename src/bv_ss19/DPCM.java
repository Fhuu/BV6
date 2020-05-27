package bv_ss19;

import java.awt.image.Raster;

public class DPCM {

    public enum FilterType {
        A ("A (Horizontal)"),
        B ("B (Vertical)"),
        C ("C (Diagonal)"),
        ABC ("A+B-C"),
        adaptiv ("adaptiv");


        private final String name;
        FilterType(String s) {
            name = s;
        }
        public String toString() {
            return this.name;
        }

    }

    private int[] errors;

    private float MSE = 0;

    public void setLength (int length) {
        errors = new int[length];
    }

    public float getMSE() {
        return MSE;
    }

    public void horizontal (RasterImage originalImage, RasterImage predictionImage, float quantizationValue, RasterImage reconstructedImage) {
        for (int x = 0; x < originalImage.width; x++) {
            for(int y = 0; y < originalImage.height; y++) {
                int position = y * originalImage.width + x;
                int positionA = position - 1;
                int colorA = 128;
                if((position >= 0 && positionA >= 0) && (position < originalImage.argb.length && positionA < originalImage.argb.length)) {
                    colorA = originalImage.argb[positionA] & 0xff;
                }
                int colorX = originalImage.argb[position] & 0xff;
                int e = colorX - colorA;
                e = (int) (Math.round(e / quantizationValue));
                e = (int) Math.round(e * quantizationValue);
                predictionImage(position, predictionImage, e);
                reconstructImage(position, reconstructedImage, positionA, e);
                MSE = MSE + e * e;

//                MSE = MSE + ((reconstructedImage.argb[position] & 0xff - originalImage.argb[position] & 0xff) * (reconstructedImage.argb[position] & 0xff - originalImage.argb[position] & 0xff));
            }
        }
        MSE = MSE/originalImage.argb.length;
    }

    public void vertical (RasterImage originalImage, RasterImage predictionImage, float quantizationValue, RasterImage reconstructedImage) {
        for (int x = 0; x < originalImage.width; x++) {
            for(int y = 0; y < originalImage.height; y++) {
                int position = y * originalImage.width + x;
                int positionB = position - originalImage.width;
                int colorB = 128;
                if((position >= 0 && positionB >= 0) && (position < originalImage.argb.length && positionB < originalImage.argb.length)) {
                    colorB = originalImage.argb[positionB] & 0xff;
                }
                int colorX = originalImage.argb[position] & 0xff;
                int e = colorX - colorB;
                e = (int) (Math.round(e / quantizationValue) * quantizationValue);
                MSE = MSE + (e * e);
                predictionImage(position, predictionImage, e);
                reconstructImage(position, reconstructedImage, positionB, e);
            }
        }
        MSE = MSE/originalImage.argb.length;
    }

    public void diagonal (RasterImage originalImage, RasterImage predictionImage, float quantizationValue, RasterImage reconstructedImage) {
        for (int x = 0; x < originalImage.width; x++) {
            for(int y = 0; y < originalImage.height; y++) {
                int position = y * originalImage.width + x;
                int positionC = position - originalImage.width - 1;
                int colorC = 128;
                if((position >= 0 && positionC >= 0) && (position < originalImage.argb.length && positionC < originalImage.argb.length)) {
                    colorC = originalImage.argb[positionC] & 0xff;
                }
                int colorX = originalImage.argb[position] & 0xff;
                int e = colorX - colorC;
                e = (int) (Math.round(e / quantizationValue) * quantizationValue);
                MSE = MSE + (e * e);
                predictionImage(position, predictionImage, e);
                reconstructImage(position, reconstructedImage, positionC, e);
            }
        }
        MSE = MSE/originalImage.argb.length;
    }

    public void abc (RasterImage originalImage, RasterImage predictionImage, float quantizationValue, RasterImage reconstructedImage) {
        for (int x = 0; x < originalImage.width; x++) {
            for(int y = 0; y < originalImage.height; y++) {
                int position = y * originalImage.width + x;
                int positionA = position - 1;
                int positionB = position - originalImage.width;
                int positionC = position - originalImage.width - 1;
                int colorA = 128;
                int colorB = 128;
                int colorC = 128;
                if((position >= 0 && positionA >= 0) && (position < originalImage.argb.length && positionA < originalImage.argb.length)) {
                    colorA = originalImage.argb[positionA] & 0xff;

                }
                if((position >= 0 && positionB >= 0) && (position < originalImage.argb.length && positionB < originalImage.argb.length)) {
                    colorB = originalImage.argb[positionB] & 0xff;

                }
                if((position >= 0 && positionC >= 0) && (position < originalImage.argb.length && positionC < originalImage.argb.length)) {
                    colorC = originalImage.argb[positionC] & 0xff;

                }
                int colorX = originalImage.argb[position] & 0xff;
                int e = colorX - (colorA + colorB - colorC);
                e = (int) (Math.round(e / quantizationValue) * quantizationValue);
                MSE = MSE + (e * e);
                predictionImage(position, predictionImage, e);
//                reconstructImage(position, reconstructedImage, colorA + colorB - colorC, e);
            }
        }
        MSE = MSE/originalImage.argb.length;
    }

    public void adaptiv (RasterImage originalImage, RasterImage predictionImage, float quantizationValue, RasterImage reconstructedImage) {
        for (int x = 0; x < originalImage.width; x++) {
            for(int y = 0; y < originalImage.height; y++) {
                int position = y * originalImage.width + x;
                int positionA = position - 1;
                int positionB = position - originalImage.width;
                int positionC = position - originalImage.width - 1;
                int colorA = 128;
                int colorB = 128;
                int colorC = 128;
                if((position >= 0 && positionA >= 0) && (position < originalImage.argb.length && positionA < originalImage.argb.length)) {
                    colorA = originalImage.argb[positionA] & 0xff;

                }
                if((position >= 0 && positionB >= 0) && (position < originalImage.argb.length && positionB < originalImage.argb.length)) {
                    colorB = originalImage.argb[positionB] & 0xff;

                }
                if((position >= 0 && positionC >= 0) && (position < originalImage.argb.length && positionC < originalImage.argb.length)) {
                    colorC = originalImage.argb[positionC] & 0xff;

                }
                int colorX = originalImage.argb[position] & 0xff;
                int e;
                if(Math.abs(colorA - colorC) < Math.abs(colorB - colorC)) {
                    e = colorX - colorB;
                    e = (int) (Math.round(e / quantizationValue) * quantizationValue);
                    MSE = MSE + (e * e);
                    predictionImage(position, predictionImage, e);
                    reconstructImage(position, reconstructedImage, positionB, e);
                } else {
                    e = colorX - colorA;
                    reconstructImage(position, reconstructedImage, positionA, e);
                    e = (int) (Math.round(e / quantizationValue) * quantizationValue);
                    MSE = MSE + (e * e);
                    predictionImage(position, predictionImage, e);
                }
            }
        }
        MSE = MSE/originalImage.argb.length;
    }

    public void predictionImage(int index, RasterImage predictionImage, int e) {
        int color = validateColor(e + 128);
        predictionImage.argb[index] = 0xff << 24 | color << 16 | color << 8 | color;
    }

    public void reconstructImage(int index, RasterImage reconstructedImage, int position, int e) {
        int colorInput = 128;
        if(position >= 0 && position < reconstructedImage.argb.length) {
            colorInput = reconstructedImage.argb[position];
        }
        int colorOutput = ((colorInput + e) & 0xff);
        reconstructedImage.argb[index] = 0xff << 24 | colorOutput << 16 | colorOutput << 8 | colorOutput;
    }

    public double entropy (RasterImage image) {
        double entropy = 0;
        int[] histogram = new int[256];
        for(int index = 0; index < image.argb.length; index++) {
            histogram[image.argb[index] & 0xff]++;
        }

        for(int index = 0; index < histogram.length; index++) {
            if (histogram[index] != 0) {
                double pj = image.argb.length/histogram[index];
                entropy = entropy + -1 * ((1 / pj) * (((-1) * Math.log(pj)) / Math.log(2)));
            }
        }
        return entropy;
    }

    private int validateColor(int color) {
        if(color >= 255) {
            color = 255;
        }
        if(color <= 0)
            color = 0;
        return color;
    }

}
