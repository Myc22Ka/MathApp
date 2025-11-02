package pl.myc22ka.mathapp.topic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.myc22ka.mathapp.topic.dto.TopicDTO;
import pl.myc22ka.mathapp.topic.service.TopicService;

import java.util.List;

/**
 * REST controller for managing topics.
 * <p>
 * Provides endpoints to view available topics used in math templates.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 17.10.2025
 */
@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
@Tag(name = "Topics", description = "Endpoints for managing topics")
public class TopicController {

    private final TopicService topicService;

    /**
     * Retrieves all available topics.
     *
     * @return a list of {@link TopicDTO} objects
     */
    @Operation(summary = "Get all topics", description = "Retrieve all topics")
    @GetMapping
    public ResponseEntity<List<TopicDTO>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }
}