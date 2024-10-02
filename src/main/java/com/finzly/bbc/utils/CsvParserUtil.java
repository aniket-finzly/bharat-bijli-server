package com.finzly.bbc.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CsvParserUtil {

    public static <T> List<T> parseCsvFile (MultipartFile file, Function<CSVRecord, T> mappingFunction) throws IOException {
        List<T> records = new ArrayList<> ();

        try (CSVParser csvParser = new CSVParser (new InputStreamReader (file.getInputStream ()), CSVFormat.DEFAULT.withFirstRecordAsHeader ())) {
            for (CSVRecord record : csvParser) {
                T mappedRecord = mappingFunction.apply (record);
                records.add (mappedRecord);
            }
        }

        return records;
    }
}
