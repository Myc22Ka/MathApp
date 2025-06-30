package pl.myc22ka.mathapp.utils.managers.files.parsers;

import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.Set;
import pl.myc22ka.mathapp.utils.managers.files.CsvRecordParser;
import pl.myc22ka.mathapp.utils.managers.files.records.SetRecord;

import java.util.Arrays;

public class SetParser implements CsvRecordParser<SetRecord> {
    @Override
    public SetRecord parse(String[] record) {
        if (record.length < SetRecord.class.getRecordComponents().length) {
            throw new IllegalArgumentException("Invalid record: " + Arrays.toString(record));
        }

        ISet A = Set.of(record[0]);
        ISet B = Set.of(record[1]);

        ISet union = Set.of(record[2]);
        ISet intersection = Set.of(record[3]);
        ISet difference = Set.of(record[4]);

        return new SetRecord(A, B, union, intersection, difference);
    }
}
