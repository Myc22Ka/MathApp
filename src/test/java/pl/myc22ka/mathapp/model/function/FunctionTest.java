package pl.myc22ka.mathapp.model.function;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.myc22ka.mathapp.utils.managers.files.CsvRecordParser;
import pl.myc22ka.mathapp.utils.managers.files.FileManager;
import pl.myc22ka.mathapp.utils.managers.files.parsers.FunctionParser;
import pl.myc22ka.mathapp.utils.managers.files.parsers.FunctionRootsRecord;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionTest {
    private static List<FunctionRootsRecord> records;

    @BeforeAll
    static void before() throws Exception {
        File file = new File("src/main/resources/static/data.csv");
        CsvRecordParser<FunctionRootsRecord> parser = new FunctionParser();
        FileManager<FunctionRootsRecord> manager = new FileManager<>(parser);

        records = manager.read(file);
    }

    @Test
    void getRealRoots() {

        records.forEach(entry -> {
            var function = new Function(entry.key());
            var expectedRoots = entry.values();

            var result = function.getRealRoots();

            assertEquals(expectedRoots, result, "Failed for function: " + function +
                    ". Expected: " + expectedRoots +
                    ", but got: " + result);
        });
    }
}