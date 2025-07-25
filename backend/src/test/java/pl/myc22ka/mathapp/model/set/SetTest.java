package pl.myc22ka.mathapp.model.set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.myc22ka.mathapp.utils.managers.files.CsvRecordParser;
import pl.myc22ka.mathapp.utils.managers.files.FileManager;
import pl.myc22ka.mathapp.utils.managers.files.parsers.SetParser;
import pl.myc22ka.mathapp.utils.managers.files.records.SetRecord;

import java.io.File;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SetTest {
    private static List<SetRecord> records;

    @BeforeAll
    static void before() throws Exception {
        File file = new File("src/main/resources/static/test/sets.csv");
        CsvRecordParser<SetRecord> parser = new SetParser();
        FileManager<SetRecord> manager = new FileManager<>(parser);

        records = manager.read(file);
    }

    @Test
    void fileTestUnion() {
        runSetTest("Union", ISet::union, SetRecord::union);
    }

    @Test
    void fileTestDifference() {
        runSetTest("Difference", ISet::difference, SetRecord::difference);
    }

    @Test
    void fileTestIntersection() {
        runSetTest("Intersection", ISet::intersection, SetRecord::intersection);
    }

    private void runSetTest(String operationName,
                            BiFunction<ISet, ISet, ISet> actualOperation,
                            Function<SetRecord, ISet> expectedFunction) {
        for (int i = 0; i < records.size(); i++) {
            SetRecord entry = records.get(i);
            ISet A = entry.A();
            ISet B = entry.B();
            ISet expected = expectedFunction.apply(entry);

            long start = System.nanoTime();
            ISet actual = actualOperation.apply(A, B);
            long end = System.nanoTime();

            long durationInMicros = (end - start) / 1_000;

            System.out.printf("%s of A = %s and B = %s took %d µs%n", operationName, A, B, durationInMicros);

            assertEquals(
                    expected.toString(),
                    actual.toString(),
                    "Failed for " + operationName + " tests in row " + (i + 2) +
                            ":\nA = " + A +
                            "\nB = " + B
            );
        }
    }
}