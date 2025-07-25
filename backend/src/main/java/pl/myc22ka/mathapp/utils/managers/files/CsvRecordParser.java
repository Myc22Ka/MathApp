package pl.myc22ka.mathapp.utils.managers.files;

public interface CsvRecordParser<T> {
    T parse(String[] record);
}
