package pl.myc22ka.mathapp.utils.managers.files.parsers;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.function.FunctionFactory;
import pl.myc22ka.mathapp.model.function.FunctionType;
import pl.myc22ka.mathapp.utils.managers.files.CsvRecordParser;
import pl.myc22ka.mathapp.utils.managers.files.records.FunctionRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FunctionParser implements CsvRecordParser<FunctionRecord> {
    private static final ExprEvaluator evaluator = new ExprEvaluator();

    @Override
    public FunctionRecord parse(String[] record) {
        if(record.length < FunctionRecord.class.getRecordComponents().length) {
            throw new IllegalArgumentException("Invalid record: " + Arrays.toString(record));
        }

        var function = FunctionFactory.create(record[0]);
        var type = FunctionType.parse(record[1]);
        var roots = parseList(record[2]);
        var derivative = evaluator.parse(record[3]);

        var range = record[4];
        var domain = record[5];

        var integral = evaluator.parse(record[6]);

        return new FunctionRecord(function, type, roots, derivative, range, domain, integral);
    }

    private List<IExpr> parseList(String input) {
        // Expect input like "[-1, 0, 1]"
        input = input.replaceAll("[\\[\\]]", "").trim(); // Remove brackets
        List<IExpr> result = new ArrayList<>();

        if (input.isEmpty()) return result;

        for (String s : input.split(",")) {
            result.add(evaluator.parse(s.trim()));
        }

        result.sort(Comparator.comparingDouble(evaluator::evalf));

        return result;
    }
}
