package pl.myc22ka.mathapp.utils.managers.files;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class FileManager<T> {

    private final CsvRecordParser<T> parser;

    public List<T> read(File file) throws Exception {
        List<T> results = new ArrayList<>();

        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(file))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(';')
                        .build())
                .withSkipLines(1)
                .build()) {

            StreamSupport.stream(csvReader.spliterator(), false)
                    .filter(record -> record.length > 0 &&
                            !record[0].trim().startsWith("#") &&
                            Arrays.stream(record).anyMatch(s -> !s.trim().isEmpty()))
                    .forEach(record -> {
                        T parsed = parser.parse(record);
                        results.add(parsed);
                    });
        }

        return results;
    }
}
