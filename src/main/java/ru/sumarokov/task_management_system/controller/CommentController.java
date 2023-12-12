package ru.sumarokov.task_management_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
@Tag(name = "comment", description = "The comment API")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService,
                             UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @Operation(summary = "Get task comments", tags = "comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments received"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentDto>> getTaskComments(@RequestParam(value = "taskId") Long taskId,
                                                            @RequestParam(value = "page") Integer page,
                                                            @RequestParam(value = "size") Integer size) {
        List<CommentDto> commentsDto = commentService.getTaskComments(taskId, PageRequest.of(page, size))
                .stream()
                .map(CommentDto::toDto)
                .toList();
        return ResponseEntity.ok().body(commentsDto);
    }

    @Operation(summary = "Get comment", tags = "comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment received"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping(path = "/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDto> getComment(@PathVariable Long commentId) {
        CommentDto commentDto = CommentDto.toDto(commentService.getComment(commentId));
        return ResponseEntity.ok().body(commentDto);
    }

    @Operation(summary = "Create comment", tags = "comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comments created"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDto> createComment(Principal principal,
                                                    @RequestBody @Valid CommentDto commentDto) {
        User user = userService.getUser(principal);
        Comment comment = commentDto.toEntity();
        comment.setAuthor(user);
        Comment commentCreated = commentService.createComment(comment);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(String.format("/comment/%d", commentCreated.getId()))
                .toUriString());
        return ResponseEntity.created(uri).body(CommentDto.toDto(commentCreated));
    }

    @Operation(summary = "Update comment", tags = "comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Comments updated"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDto> updateComment(Principal principal,
                                                    @RequestBody @Valid CommentDto commentDto) {
        User user = userService.getUser(principal);
        Comment commentUpdated = commentService.updateComment(commentDto.toEntity(), user.getId());
        return ResponseEntity.accepted().body(CommentDto.toDto(commentUpdated));
    }

    @Operation(summary = "Delete comment", tags = "comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comments deleted"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(Principal principal,
                                              @PathVariable Long commentId) {
        User user = userService.getUser(principal);
        commentService.deleteComment(commentId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
