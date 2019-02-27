package com.assetcontrol.translator.model;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RowDictionaryConfigHolder {

    private Map<String ,String> rowDictionary = new ConcurrentHashMap<>();

    public Map<String, String> getRowDictionary() {
        return rowDictionary;
    }
}
