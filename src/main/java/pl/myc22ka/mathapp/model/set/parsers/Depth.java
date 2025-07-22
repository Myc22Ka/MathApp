package pl.myc22ka.mathapp.model.set.parsers;

public class Depth {
    int braces = 0, parens = 0, brackets = 0;

    void update(char c) {
        switch (c) {
            case '{' -> braces++;
            case '}' -> braces--;
            case '(' -> parens++;
            case ')' -> parens--;
            case '[' -> brackets++;
            case ']' -> brackets--;
        }
    }

    boolean isTop() {
        return braces == 0 && parens == 0 && brackets == 0;
    }
}
