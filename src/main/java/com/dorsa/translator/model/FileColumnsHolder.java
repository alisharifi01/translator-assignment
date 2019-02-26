package com.dorsa.translator.model;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FileColumnsHolder {

    private Map<String , List<String>> columnsByFileName = new ConcurrentHashMap<>();

    public Map<String , List<String>> getColumnsByFileName() {
        return columnsByFileName;
    }
}
