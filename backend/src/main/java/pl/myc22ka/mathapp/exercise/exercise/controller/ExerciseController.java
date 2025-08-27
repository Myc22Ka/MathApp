package pl.myc22ka.mathapp.exercise.exercise.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.exercise.exercise.dto.ExerciseDTO;
import pl.myc22ka.mathapp.exercise.exercise.service.ExerciseService;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    /**
     * Tworzy nowe ćwiczenie na podstawie wybranego szablonu oraz podanych wartości.
     *
     * @param templateId identyfikator szablonu ćwiczenia
     * @param values lista wartości wstawianych do szablonu
     * @return utworzone ćwiczenie jako DTO
     */
    @Operation(
            summary = "Utwórz ćwiczenie",
            description = "Tworzy nowe ćwiczenie na podstawie szablonu oraz listy wartości."
    )
    @ApiResponse(responseCode = "200", description = "Ćwiczenie zostało utworzone")
    @PostMapping("/create/{templateId}")
    public ResponseEntity<ExerciseDTO> createExercise(
            @PathVariable Long templateId,
            @RequestBody List<String> values
    ) {
        return ResponseEntity.ok(exerciseService.createExercise(templateId, values));
    }

    /**
     * Generuje nowe ćwiczenie automatycznie na podstawie wybranego szablonu.
     *
     * @param templateId identyfikator szablonu ćwiczenia
     * @return wygenerowane ćwiczenie jako DTO
     */
    @Operation(
            summary = "Wygeneruj ćwiczenie",
            description = "Automatycznie generuje nowe ćwiczenie na podstawie szablonu."
    )
    @ApiResponse(responseCode = "200", description = "Ćwiczenie zostało wygenerowane")
    @PostMapping("/generate/{templateId}")
    public ResponseEntity<ExerciseDTO> generateExercise(
            @PathVariable Long templateId
    ) {
        return ResponseEntity.ok(exerciseService.generateExercise(templateId));
    }

    /**
     * Pobiera ćwiczenie po identyfikatorze.
     *
     * @param id identyfikator ćwiczenia
     * @return ćwiczenie jako DTO lub 404 jeśli nie istnieje
     */
    @Operation(
            summary = "Pobierz ćwiczenie",
            description = "Zwraca szczegóły ćwiczenia o podanym identyfikatorze."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zwrócono ćwiczenie"),
            @ApiResponse(responseCode = "404", description = "Ćwiczenie nie istnieje")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> getExercise(@PathVariable Long id) {
        return exerciseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

