package pl.myc22ka.mathapp.exercise.exercise.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.exercise.service.ExerciseService;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping("/create/{templateId}")
    public ResponseEntity<Exercise> createExercise(
            @PathVariable Long templateId,
            @RequestBody List<String> values
    ) {
        Exercise exercise = exerciseService.createExercise(templateId, values);
        return ResponseEntity.ok(exercise);
    }

    @PostMapping("/generate/{templateId}")
    public ResponseEntity<Exercise> generateExercise(
            @PathVariable Long templateId
    ) {
        Exercise exercise = exerciseService.generateExercise(templateId);
        return ResponseEntity.ok(exercise);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getExercise(@PathVariable Long id) {
        return ResponseEntity.of(exerciseService.findById(id));
    }
}
