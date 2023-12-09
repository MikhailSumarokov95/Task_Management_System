package ru.sumarokov.task_management_system.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.sumarokov.task_management_system.dto.CommentDto;
import ru.sumarokov.task_management_system.entity.Comment;
import ru.sumarokov.task_management_system.entity.User;
import ru.sumarokov.task_management_system.service.CommentService;
import ru.sumarokov.task_management_system.service.UserService;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService,
                             UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping(path = "?taskId={taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentDto>> getTaskComments(@PathVariable Long taskId) {
        List<CommentDto> commentsDto = commentService.getTaskComments(taskId)
                .stream()
                .map(CommentDto::toDto)
                .toList();
        return ResponseEntity.ok().body(commentsDto);
    }

    @GetMapping(path = "/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDto> getComment(@PathVariable Long commentId) {
        CommentDto commentDto = CommentDto.toDto(commentService.getComment(commentId));
        return ResponseEntity.ok().body(commentDto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDto> createComment(Principal principal,
                                              @RequestBody @Valid CommentDto commentDto) {
        User user = userService.getUser(principal);
        Comment commentCreated = commentService.saveComment(commentDto.toEntity(), user);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(String.format("/comment/%d", commentCreated.getId()))
                .toUriString());
        return ResponseEntity.created(uri).body(CommentDto.toDto(commentCreated));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDto> updateComment(Principal principal,
                                                    @RequestBody @Valid CommentDto commentDto) {
        User user = userService.getUser(principal);
        Comment commentUpdated = commentService.saveComment(commentDto.toEntity(), user);
        return ResponseEntity.accepted().body(CommentDto.toDto(commentUpdated));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(Principal principal,
                                              @PathVariable Long commentId) {
        User user = userService.getUser(principal);
        commentService.deleteComment(commentId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
