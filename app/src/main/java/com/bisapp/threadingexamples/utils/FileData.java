package com.bisapp.threadingexamples.utils;

import java.io.File;

public class FileData {
    private File name;
    private String size;

    public FileData(File name, String size) {
        this.name = name;
        this.size = size;
    }

    public File getName() {
        return name;
    }

    public void setName(File name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
