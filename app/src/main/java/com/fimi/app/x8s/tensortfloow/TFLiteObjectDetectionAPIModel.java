package com.fimi.app.x8s.tensortfloow;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Trace;

import androidx.annotation.NonNull;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class TFLiteObjectDetectionAPIModel implements Classifier {
    private static final float IMAGE_MEAN = 128.0f;
    private static final float IMAGE_STD = 128.0f;
    private static final int NUM_DETECTIONS = 2;
    private static final int NUM_THREADS = 6;
    private final Vector<String> labels = new Vector<>();
    private ByteBuffer imgData;
    private int inputSize;
    private int[] intValues;
    private boolean isModelQuantized;
    private float[] numDetections;
    private float[][] outputClasses;
    private float[][][] outputLocations;
    private float[][] outputScores;
    private Interpreter tfLite;

    private TFLiteObjectDetectionAPIModel() {
    }

    private static MappedByteBuffer loadModelFile(@NonNull AssetManager assets, String modelFilename) throws IOException {
        AssetFileDescriptor fileDescriptor = assets.openFd(modelFilename);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    @NonNull
    public static Classifier create(@NonNull AssetManager assetManager, String modelFilename, @NonNull String labelFilename, int inputSize, boolean isQuantized) throws IOException {
        int numBytesPerChannel;
        TFLiteObjectDetectionAPIModel d = new TFLiteObjectDetectionAPIModel();
        String actualFilename = labelFilename.split("file:///android_asset/")[1];
        InputStream labelsInput = assetManager.open(actualFilename);
        BufferedReader br = new BufferedReader(new InputStreamReader(labelsInput));
        while (true) {
            String line = br.readLine();
            if (line == null) {
                break;
            }
            d.labels.add(line);
        }
        br.close();
        d.inputSize = inputSize;
        try {
            d.tfLite = new Interpreter(loadModelFile(assetManager, modelFilename));
            d.isModelQuantized = isQuantized;
            if (isQuantized) {
                numBytesPerChannel = 1;
            } else {
                numBytesPerChannel = 4;
            }
            d.imgData = ByteBuffer.allocateDirect(d.inputSize * d.inputSize * 3 * numBytesPerChannel);
            d.imgData.order(ByteOrder.nativeOrder());
            d.intValues = new int[d.inputSize * d.inputSize];
            d.tfLite.setNumThreads(6);
            d.outputLocations = (float[][][]) Array.newInstance(Float.TYPE, 1, 2, 4);
            d.outputClasses = (float[][]) Array.newInstance(Float.TYPE, 1, 2);
            d.outputScores = (float[][]) Array.newInstance(Float.TYPE, 1, 2);
            d.numDetections = new float[1];
            return d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Classifier.Recognition> recognizeImage(@NonNull Bitmap bitmap) {
        Trace.beginSection("recognizeImage");
        Trace.beginSection("preprocessBitmap");
        bitmap.getPixels(this.intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        this.imgData.rewind();
        for (int i = 0; i < this.inputSize; i++) {
            for (int j = 0; j < this.inputSize; j++) {
                int pixelValue = this.intValues[(this.inputSize * i) + j];
                if (this.isModelQuantized) {
                    this.imgData.put((byte) ((pixelValue >> 16) & 255));
                    this.imgData.put((byte) ((pixelValue >> 8) & 255));
                    this.imgData.put((byte) (pixelValue & 255));
                } else {
                    this.imgData.putFloat((((pixelValue >> 16) & 255) - 128.0f) / 128.0f);
                    this.imgData.putFloat((((pixelValue >> 8) & 255) - 128.0f) / 128.0f);
                    this.imgData.putFloat(((pixelValue & 255) - 128.0f) / 128.0f);
                }
            }
        }
        Trace.endSection();
        Trace.beginSection("feed");
        this.outputLocations = (float[][][]) Array.newInstance(Float.TYPE, 1, 2, 4);
        this.outputClasses = (float[][]) Array.newInstance(Float.TYPE, 1, 2);
        this.outputScores = (float[][]) Array.newInstance(Float.TYPE, 1, 2);
        this.numDetections = new float[1];
        Object[] inputArray = {this.imgData};
        Map<Integer, Object> outputMap = new HashMap<>();
        outputMap.put(0, this.outputLocations);
        outputMap.put(1, this.outputClasses);
        outputMap.put(2, this.outputScores);
        outputMap.put(3, this.numDetections);
        Trace.endSection();
        Trace.beginSection("run");
        this.tfLite.runForMultipleInputsOutputs(inputArray, outputMap);
        Trace.endSection();
        ArrayList<Classifier.Recognition> recognitions = new ArrayList<>(2);
        for (int i2 = 0; i2 < 2; i2++) {
            RectF detection = new RectF(this.outputLocations[0][i2][1] * this.inputSize, this.outputLocations[0][i2][0] * this.inputSize, this.outputLocations[0][i2][3] * this.inputSize, this.outputLocations[0][i2][2] * this.inputSize);
            int nClass = (int) this.outputClasses[0][i2];
            if (nClass >= 0 && nClass <= 90) {
                recognitions.add(new Classifier.Recognition("" + i2, this.labels.get(nClass + 1), this.outputScores[0][i2], detection));
            }
        }
        Trace.endSection();
        return recognitions;
    }

    @Override
    public void enableStatLogging(boolean logStats) {
    }

    @Override
    public String getStatString() {
        return "";
    }

    @Override
    public void close() {
    }
}
