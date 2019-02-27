package com.dorsa.translator.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;

@Component
public class FileUtil {

    @Value("${file.path.dir.input.data}")
    private String DATA_INPUT_DIRECTORY;

    public File[] getInputFiles() {
        File folder = new File(DATA_INPUT_DIRECTORY);
        return folder.listFiles();
    }

}

