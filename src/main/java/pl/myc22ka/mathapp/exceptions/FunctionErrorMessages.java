package pl.myc22ka.mathapp.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FunctionErrorMessages {
    NULL_VALUE_NOT_ALLOWED("Value cannot be null"),
    NULL_VALUE_UPDATE_ERROR("Value is null, cannot update expression"),
    ILLOGICAL_MATH_OPERATION("This mathematical operation is not logically valid"),
    NO_SOLUTIONS("No solutions found"),
    ALL_SOLUTIONS("All solutions found");

    private final String message;

    @Override
    public String toString() {
        return message;
    }
}
