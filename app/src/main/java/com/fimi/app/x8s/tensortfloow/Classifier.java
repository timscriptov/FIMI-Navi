package com.fimi.app.x8s.tensortfloow;

import android.graphics.Bitmap;
import android.graphics.RectF;

import java.util.List;


public interface Classifier {
    void close();

    void enableStatLogging(boolean z);

    String getStatString();

    List<Recognition> recognizeImage(Bitmap bitmap);


    class Recognition {
        private final Float confidence;
        private final String id;
        private final String title;
        private RectF location;

        public Recognition(String id, String title, Float confidence, RectF location) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
            this.location = location;
        }

        public String getId() {
            return this.id;
        }

        public String getTitle() {
            return this.title;
        }

        public Float getConfidence() {
            return this.confidence;
        }

        public RectF getLocation() {
            return new RectF(this.location);
        }

        public void setLocation(RectF location) {
            this.location = location;
        }

        public String toString() {
            String resultString = this.id != null ? "[" + this.id + "] " : "";
            if (this.title != null) {
                resultString = resultString + this.title + " ";
            }
            if (this.confidence != null) {
                resultString = resultString + String.format("(%.1f%%) ", Float.valueOf(this.confidence.floatValue() * 100.0f));
            }
            if (this.location != null) {
                resultString = resultString + this.location + " ";
            }
            return resultString.trim();
        }
    }
}
