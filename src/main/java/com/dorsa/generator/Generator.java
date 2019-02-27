package com.dorsa.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Generator {

    public static void main(String[] args) throws IOException {
        int counter = 0;
        for(int i = 1 ; i < 2 ; i++){
            File file = new File("/home/alisharifi/adminuser/data/input/input" + i + ".txt");
            file.delete();
            FileWriter fw = new FileWriter("/home/alisharifi/Desktop/data/input/input" + i + ".txt", true);
            String firstLine = "COL1\tCOL2\tCOL3\tCOL4\tCOL5\tCOL6\n";
            fw.write(firstLine);
            fw.flush();
            int rows = 1 * 100000;
            for (int j = counter ; j < counter + rows; j++) {
                StringBuffer sb = new StringBuffer("ID");
                sb.append(j);
                for (int k = 1; k <= 5; k++) {
                    sb.append("\tVAL").append(j).append(k);
                }
                fw.write(sb.toString()+ "\n");
                fw.flush();
            }
            counter += rows;
        }
    }
}