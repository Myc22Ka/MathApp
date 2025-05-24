package pl.myc22ka.mathapp.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FunctionErrorMessages {
    NULL_VALUE_NOT_ALLOWED("Value cannot be null"),
    NULL_VALUE_UPDATE_ERROR("Value is null, cannot update expression");

    private final String message;

    @Override
    public String toString() {
        return message;
    }
}
