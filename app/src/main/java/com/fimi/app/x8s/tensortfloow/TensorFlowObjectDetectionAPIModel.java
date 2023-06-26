package com.fimi.app.x8s.tensortfloow;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Trace;

import androidx.annotation.NonNull;

import org.tensorflow.Graph;
import org.tensorflow.Operation;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;


public class TensorFlowObjectDetectionAPIModel implements Classifier {
    private static final int MAX_RESULTS = 100;
    private final Vector<String> labels = new Vector<>();
    private byte[] byteValues;
    private TensorFlowInferenceInterface inferenceInterface;
    private String inputName;
    private int inputSize;
    private int[] intValues;
    private boolean logStats = false;
    private float[] outputClasses;
    private float[] outputLocations;
    private String[] outputNames;
    private float[] outputNumDetections;
    private float[] outputScores;

    private TensorFlowObjectDetectionAPIModel() {
    }

    @NonNull
    public static Classifier create(@NonNull AssetManager assetManager, String modelFilename, @NonNull String labelFilename, int inputSize) throws IOException {
        TensorFlowObjectDetectionAPIModel d = new TensorFlowObjectDetectionAPIModel();
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
        d.inferenceInterface = new TensorFlowInferenceInterface(assetManager, modelFilename);
        Graph g = d.inferenceInterface.graph();
        d.inputName = "image_tensor";
        Operation inputOp = g.operation(d.inputName);
        if (inputOp == null) {
            throw new RuntimeException("Failed to find input Node '" + d.inputName + "'");
        }
        d.inputSize = inputSize;
        Operation outputOp1 = g.operation("detection_scores");
        if (outputOp1 == null) {
            throw new RuntimeException("Failed to find output Node 'detection_scores'");
        }
        Operation outputOp2 = g.operation("detection_boxes");
        if (outputOp2 == null) {
            throw new RuntimeException("Failed to find output Node 'detection_boxes'");
        }
        Operation outputOp3 = g.operation("detection_classes");
        if (outputOp3 == null) {
            throw new RuntimeException("Failed to find output Node 'detection_classes'");
        }
        d.outputNames = new String[]{"detection_boxes", "detection_scores", "detection_classes", "num_detections"};
        d.intValues = new int[d.inputSize * d.inputSize];
        d.byteValues = new byte[d.inputSize * d.inputSize * 3];
        d.outputScores = new float[100];
        d.outputLocations = new float[400];
        d.outputClasses = new float[100];
        d.outputNumDetections = new float[1];
        return d;
    }

    @Override
    public List<Classifier.Recognition> recognizeImage(@NonNull Bitmap bitmap) {
        Trace.beginSection("recognizeImage");
        Trace.beginSection("preprocessBitmap");
        bitmap.getPixels(this.intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int i = 0; i < this.intValues.length; i++) {
            this.byteValues[(i * 3) + 2] = (byte) (this.intValues[i] & 255);
            this.byteValues[(i * 3) + 1] = (byte) ((this.intValues[i] >> 8) & 255);
            this.byteValues[(i * 3)] = (byte) ((this.intValues[i] >> 16) & 255);
        }
        Trace.endSection();
        Trace.beginSection("feed");
        this.inferenceInterface.feed(this.inputName, this.byteValues, 1, this.inputSize, this.inputSize, 3);
        Trace.endSection();
        Trace.beginSection("run");
        this.inferenceInterface.run(this.outputNames, this.logStats);
        Trace.endSection();
        Trace.beginSection("fetch");
        this.outputLocations = new float[400];
        this.outputScores = new float[100];
        this.outputClasses = new float[100];
        this.outputNumDetections = new float[1];
        this.inferenceInterface.fetch(this.outputNames[0], this.outputLocations);
        this.inferenceInterface.fetch(this.outputNames[1], this.outputScores);
        this.inferenceInterface.fetch(this.outputNames[2], this.outputClasses);
        this.inferenceInterface.fetch(this.outputNames[3], this.outputNumDetections);
        Trace.endSection();
        PriorityQueue<Classifier.Recognition> pq = new PriorityQueue<>(1, (lhs, rhs) -> Float.compare(rhs.getConfidence(), lhs.getConfidence()));
        for (int i2 = 0; i2 < this.outputScores.length; i2++) {
            RectF detection = new RectF(this.outputLocations[(i2 * 4) + 1] * this.inputSize, this.outputLocations[i2 * 4] * this.inputSize, this.outputLocations[(i2 * 4) + 3] * this.inputSize, this.outputLocations[(i2 * 4) + 2] * this.inputSize);
            pq.add(new Classifier.Recognition("" + ((int) this.outputClasses[i2]), this.labels.get((int) this.outputClasses[i2]), this.outputScores[i2], detection));
        }
        ArrayList<Classifier.Recognition> recognitions = new ArrayList<>();
        for (int i3 = 0; i3 < Math.min(pq.size(), 100); i3++) {
            recognitions.add(pq.poll());
        }
        Trace.endSection();
        return recognitions;
    }

    @Override
    public void enableStatLogging(boolean logStats) {
        this.logStats = logStats;
    }

    @Override
    public String getStatString() {
        return this.inferenceInterface.getStatString();
    }

    @Override
    public void close() {
        this.inferenceInterface.close();
    }
}
