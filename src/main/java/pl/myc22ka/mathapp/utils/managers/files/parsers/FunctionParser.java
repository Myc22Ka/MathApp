package pl.myc22ka.mathapp.utils.managers.files.parsers;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.utils.managers.files.CsvRecordParser;

import java.util.ArrayList;
import java.util.List;

public class FunctionParser implements CsvRecordParser<FunctionRootsRecord> {
    private static final ExprEvaluator evaluator = new ExprEvaluator();

    @Override
    public FunctionRootsRecord parse(String[] record) {
        String key = record.length > 0 ? record[0] : "";
        List<IExpr> values = new ArrayList<>();
        for (int i = 1; i < record.length; i++) {
            try {
                values.add(evaluator.parse(record[i]));
            } catch (NumberFormatException e) {
                // Możesz tu dodać logowanie lub obsługę błędów
            }
        }
        return new FunctionRootsRecord(key, values);
    }
}
