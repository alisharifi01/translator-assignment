package com.assetcontrol.translator.biz;

import com.assetcontrol.translator.biz.exception.IdNotFoundException;
import com.assetcontrol.translator.biz.exception.InvalidFileNameException;
import com.assetcontrol.translator.biz.exception.InvalidLineFormatException;
import com.assetcontrol.translator.model.ColumnDictionaryConfigHolder;
import com.assetcontrol.translator.model.FileColumnsHolder;
import com.assetcontrol.translator.model.RowDictionaryConfigHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Responsible for translating first row(column row) and the other rows
 */
@Component
public class DataLineTranslator {

    private static final int ID_INDEX = 0;
    private RowDictionaryConfigHolder rowDictionaryConfigHolder;
    private ColumnDictionaryConfigHolder columnDictionaryConfigHolder;
    private FileColumnsHolder fileColumnsHolder;


    @Autowired
    public DataLineTranslator(RowDictionaryConfigHolder rowDictionaryConfigHolder, ColumnDictionaryConfigHolder columnDictionaryConfigHolder, FileColumnsHolder fileColumnsHolder) {
        this.rowDictionaryConfigHolder = rowDictionaryConfigHolder;
        this.columnDictionaryConfigHolder = columnDictionaryConfigHolder;
        this.fileColumnsHolder = fileColumnsHolder;
    }

    /*
      translate one row according the row-mapping model and file columns model
     */
    public String translateRowLine(String line, String fileName)
            throws IdNotFoundException, InvalidLineFormatException, InvalidFileNameException {

        StringBuffer translatedLine = new StringBuffer();
        String[] splittedLine = line.split("\\t");
        List<String> columns = fileColumnsHolder.getColumnsByFileName().get(fileName);
        if(columns == null){
            throw new InvalidFileNameException();
        }
        if(splittedLine.length != columns.size()) {
            throw new InvalidLineFormatException();
        }

        String translatedID = rowDictionaryConfigHolder.getRowDictionary().get(splittedLine[ID_INDEX]);
        if (translatedID == null) {
            throw new IdNotFoundException();
        }

        for (int i = 0; i < splittedLine.length; i++) {
            if (columnDictionaryConfigHolder.getColumnDictionary().containsKey(columns.get(i))) {
                if (i == ID_INDEX) {
                    translatedLine.append(translatedID).append("\t");
                } else {
                    translatedLine.append(splittedLine[i]).append("\t");
                }
            }
        }
        return translatedLine.substring(0,translatedLine.length() - 1);
    }


    /*
      translate first line including columns according the column-mapping model and file columns
     */
    public String translateColumnLine(String line, String fileName) throws InvalidLineFormatException, InvalidFileNameException {
        StringBuffer translatedLine = new StringBuffer();
        String[] splittedLine = line.split("\\t");
        List<String> columns = fileColumnsHolder.getColumnsByFileName().get(fileName);
        if(columns == null){
            throw new InvalidFileNameException();
        }
        if(columns.size() != splittedLine.length) {
            throw new InvalidLineFormatException();
        }
        for (int i = 0; i < splittedLine.length; i++) {
            String translatedColumn = columnDictionaryConfigHolder.getColumnDictionary().get(columns.get(i));
            if (translatedColumn != null) {
                translatedLine.append(translatedColumn);
                translatedLine.append("\t");
            }
        }
        if(translatedLine.toString().equals("")){
            throw new InvalidLineFormatException();
        }
        return translatedLine.substring(0,translatedLine.length() - 1);
    }
}
