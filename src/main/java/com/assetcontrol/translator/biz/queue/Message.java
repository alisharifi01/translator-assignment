package com.assetcontrol.translator.biz.queue;

public class Message {

    private String line;
    private String fileName;

    public Message(String line, String fileName) {
        this.line  = line;
        this.fileName  = fileName;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
