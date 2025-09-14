package pl.myc22ka.mathapp.step.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StepType {
    N1("Przygotowanie do ćwiczenia"),
    N2("Rozgrzewka"),
    N3("Właściwe ćwiczenie"),
    N4("Odpoczynek");

    private final String description;
}