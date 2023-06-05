package com.fimi.kernel.animutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class LineIterator implements Iterator<String> {
    private final BufferedReader bufferedReader;
    private String cachedLine;
    private boolean finished = false;

    public LineIterator(Reader reader) throws IllegalArgumentException {
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        }
        if (reader instanceof BufferedReader) {
            this.bufferedReader = (BufferedReader) reader;
        } else {
            this.bufferedReader = new BufferedReader(reader);
        }
    }

    public static void closeQuietly(LineIterator iterator) {
        if (iterator != null) {
            iterator.close();
        }
    }

    @Override
    public boolean hasNext() {
        boolean z = true;
        if (this.cachedLine != null) {
            return true;
        }
        if (this.finished) {
            return false;
        }
        while (true) {
            try {
                String line = this.bufferedReader.readLine();
                if (line == null) {
                    this.finished = true;
                    z = false;
                    break;
                } else if (isValidLine(line)) {
                    this.cachedLine = line;
                    break;
                }
            } catch (IOException ioe) {
                close();
                throw new IllegalStateException(ioe);
            }
        }
        return z;
    }

    protected boolean isValidLine(String line) {
        return true;
    }

    @Override
    public String next() {
        return nextLine();
    }

    public String nextLine() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more lines");
        }
        String currentLine = this.cachedLine;
        this.cachedLine = null;
        return currentLine;
    }

    public void close() {
        this.finished = true;
        IOUtils.closeQuietly(this.bufferedReader);
        this.cachedLine = null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove unsupported on LineIterator");
    }
}
