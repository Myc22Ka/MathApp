package pl.myc22ka.mathapp.model.function;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.myc22ka.mathapp.utils.managers.files.CsvRecordParser;
import pl.myc22ka.mathapp.utils.managers.files.FileManager;
import pl.myc22ka.mathapp.utils.managers.files.parsers.FunctionParser;
import pl.myc22ka.mathapp.utils.managers.files.records.FunctionRecord;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionTest {
    private static List<FunctionRecord> records;

    @BeforeAll
    static void before() throws Exception {
        File file = new File("src/main/resources/static/data.csv");
        CsvRecordParser<FunctionRecord> parser = new FunctionParser();
        FileManager<FunctionRecord> manager = new FileManager<>(parser);

        records = manager.read(file);
    }

    @Test
    void getRealRoots() {

        records.forEach(entry -> {
            var function = entry.function();
            var expectedRoots = entry.roots();

            var result = function.getRealRoots();

            assertEquals(expectedRoots, result, "Failed for function: " + function +
                    ". Expected: " + expectedRoots +
                    ", but got: " + result);
        });
    }
}