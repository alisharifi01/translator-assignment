package com.assetcontrol.translator.biz;

public interface DataFileReaderListener {

    void lineReceived(String line,String fileName,boolean isFirstLine);
    void produceDone(String fileName);
}
