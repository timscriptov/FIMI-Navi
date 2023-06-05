package com.fimi.app.x8s.widget.videoview;

import androidx.annotation.NonNull;

import java.io.Serializable;



public class X8FmMediaInfo implements Serializable {
    private String duration;
    private String name;
    private String path;

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @NonNull
    public String toString() {
        return "X8FmMediaInfo{name='" + this.name + "'" + ", duration='" + this.duration + "'" + ", path='" + this.path + "'" + '}';
    }
}
