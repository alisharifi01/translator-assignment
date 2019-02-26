package com.dorsa.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigGenerator {


    public static void main(String[] args) throws IOException {
        File file = new File("/home/alisharifi/Desktop/data/conf/row-mapping.conf");
        file.delete();
        FileWriter fw = new FileWriter("/home/alisharifi/Desktop/data/conf/row-mapping.conf",false);
        int rows = 20 * 10000000;
        for(int i = 1 ; i < rows; i+=50) {
            String line2 = "ID" + i +
                    "\tOURID" + i;
                    fw.write(line2 + "\n");

        }
        fw.flush();
    }
}