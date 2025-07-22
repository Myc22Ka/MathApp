package pl.myc22ka.mathapp.model.set.utils;

public class Depth {
    int braces = 0, parens = 0, brackets = 0;

    public void update(char c) {
        switch (c) {
            case '{' -> braces++;
            case '}' -> braces--;
            case '(' -> parens++;
            case ')' -> parens--;
            case '[' -> brackets++;
            case ']' -> brackets--;
        }
    }

    public boolean isTop() {
        return braces == 0 && parens == 0 && brackets == 0;
    }
}
