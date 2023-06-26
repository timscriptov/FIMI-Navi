package com.fimi.app.x8s.tools;

import android.annotation.SuppressLint;

import java.util.LinkedList;


public class LinkedListQueue<T> {
    private final int LINK_SIZE = 50;
    private LinkedList<T> linkedList = new LinkedList<>();

    public void add(T obj) {
        if (size() >= this.LINK_SIZE) {
            this.linkedList.removeFirst();
        }
        this.linkedList.addLast(obj);
    }

    @SuppressLint({"NewApi"})
    public T getFront() {
        return this.linkedList.peekFirst();
    }

    public void removeFront() {
        this.linkedList.removeFirst();
    }

    public int size() {
        return this.linkedList.size();
    }

    public void removeAll() {
        this.linkedList.clear();
    }

    public LinkedList<T> getLinkedList() {
        return this.linkedList;
    }

    public void setLinkedList(LinkedList<T> linkedList) {
        this.linkedList = linkedList;
    }
}
