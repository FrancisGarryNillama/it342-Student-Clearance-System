package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.Comment;
import edu.cit.studentclearancesystem.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;

    @GetMapping("/task/{taskId}")
    public List<Comment> getCommentsForTask(@PathVariable Long taskId) {
        return commentRepository.findByTaskTaskIdOrderByCreatedAtAsc(taskId);
    }
}
