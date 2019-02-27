package com.dorsa.translator.integration;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application.properties")
public class IntegrationTest {

    @Value("${file.path.dir.output.data}")
    private String OUTPUT_DIRECTORY;

    private static String INPUT_DIRECTORY;
    private static String ROW_MAPPING_FILE_PATH;
    private static String COLUMN_MAPPING_FILE_PATH;

    private static void parseTestConfigFile() {
        InputStream inputStream = IntegrationTest.class.getClassLoader()
                .getResourceAsStream("application.properties");
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();
            if (line.contains("file.path.dir.input.data")) {
                String[] split = line.split("=");
                INPUT_DIRECTORY = split[1];
            }
            if (line.contains("file.path.config.row.mapping")) {
                String[] split = line.split("=");
                ROW_MAPPING_FILE_PATH = split[1];
            }
            if (line.contains("file.path.config.column.mapping")) {
                String[] split = line.split("=");
                COLUMN_MAPPING_FILE_PATH = split[1];
            }
        }
        if (COLUMN_MAPPING_FILE_PATH == null ||
                ROW_MAPPING_FILE_PATH == null ||
                INPUT_DIRECTORY == null) {
            throw new RuntimeException("invalid config-test.properties file");
        }
    }

    @BeforeClass
    public static void setup() throws IOException {
        parseTestConfigFile();
        writeSampleInputFiles();
        writeSampleRowMappingConfig();
        writeSampleColumnMappingConfig();
    }

    private static void writeSampleColumnMappingConfig() throws IOException {
        String sampleColumnMapping = "COL1\tOURID\n" +
                "COL3\tOURCOL3\n" +
                "COL4\tOURCOL4";

        writeOnFile(COLUMN_MAPPING_FILE_PATH , sampleColumnMapping);
    }

    private static void writeSampleRowMappingConfig() throws IOException {
        String sampleRowMapping = "ID1\tOURID1\n" +
                "ID2\tOURID2\n" +
                "ID8\tOURID8\n" +
                "ID10\tOURID10";

        writeOnFile(ROW_MAPPING_FILE_PATH , sampleRowMapping);
    }

    private static void writeSampleInputFiles() throws IOException {
        String sampleInput1 = "COL1\tCOL2\tCOL3\tCOL4\tCOL5\tCOL6\n" +
                "ID0\tVAL01\tVAL02\tVAL03\tVAL04\tVAL05\n" +
                "ID1\tVAL11\tVAL12\tVAL13\tVAL14\tVAL15\n" +
                "ID2\tVAL21\tVAL22\tVAL23\tVAL24\tVAL25\n" +
                "ID3\tVAL31\tVAL32\tVAL33\tVAL34\tVAL35\n" +
                "ID4\tVAL41\tVAL42\tVAL43\tVAL44\tVAL45\n" +
                "ID5\tVAL51\tVAL52\tVAL53\tVAL54\tVAL55\n" +
                "ID6\tVAL61\tVAL62\tVAL63\tVAL64\tVAL65\n" +
                "ID7\tVAL71\tVAL72\tVAL73\tVAL74\tVAL75\n" +
                "ID8\tVAL81\tVAL82\tVAL83\tVAL84\tVAL85\n" +
                "ID9\tVAL91\tVAL92\tVAL93\tVAL94\tVAL95\n" +
                "ID10\tVAL101\tVAL102\tVAL103\tVAL104\tVAL105";

        writeOnFile(INPUT_DIRECTORY + "input1.txt" , sampleInput1);
    }

    private static void writeOnFile(String filePath , String lines) throws IOException {
        File file = new File(filePath);
        file.delete();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(lines);
        fileWriter.flush();
    }

    @Test
    public void afterApplicationDone() throws IOException {
        System.out.println("end");
        List<String> expectedOutput =
                Arrays.asList("OURID\tOURCOL3\tOURCOL4",
                        "OURID1\tVAL12\tVAL13",
                        "OURID2\tVAL22\tVAL23",
                        "OURID8\tVAL82\tVAL83",
                        "OURID10\tVAL102\tVAL103");

        List<String> outputLines = Files.readAllLines(Paths.get(OUTPUT_DIRECTORY + "input1.out"));
        assertEquals(expectedOutput, outputLines);
    }
}
