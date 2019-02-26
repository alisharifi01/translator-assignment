package com.dorsa.translator.model;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ColumnDictionaryConfigHolder {

    private Map<String ,String> columnDictionary = new ConcurrentHashMap<>();

    public Map<String, String> getColumnDictionary() {
        return columnDictionary;
    }
}
