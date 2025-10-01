package pl.myc22ka.mathapp.ai.prompt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.myc22ka.mathapp.ai.prompt.dto.TopicDTO;
import pl.myc22ka.mathapp.ai.prompt.service.TopicService;

import java.util.List;

/**
 * REST controller for Topic management.
 */
@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
@Tag(name = "Topics", description = "Endpoints for managing topics")
public class TopicController {

    private final TopicService topicService;

    /**
     * Get all topics without filtering.
     *
     * @return list of TopicDTO
     */
    @Operation(summary = "Get all topics", description = "Retrieve all topics without filters")
    @GetMapping
    public ResponseEntity<List<TopicDTO>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }
}