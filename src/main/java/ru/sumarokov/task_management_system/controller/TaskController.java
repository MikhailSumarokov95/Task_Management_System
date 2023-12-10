package ru.sumarokov.task_management_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.sumarokov.task_management_system.dto.TaskDto;
import ru.sumarokov.task_management_system.dto.UserDto;
import ru.sumarokov.task_management_system.entity.Task;
import ru.sumarokov.task_management_system.entity.User;
import ru.sumarokov.task_management_system.helper.Status;
import ru.sumarokov.task_management_system.service.TaskService;
import ru.sumarokov.task_management_system.service.UserService;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController(value = "/task")
@RequestMapping("/task")
@Tag(name = "task", description = "The task API")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService,
                          UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasksDto = taskService.getAllTasks()
                .stream()
                .map(TaskDto::toDto)
                .toList();
        return ResponseEntity.ok().body(tasksDto);
    }

    @GetMapping(path = "?authorId={authorId}&executorId={executorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getAuthorTasks(@RequestParam(required = false, value = "authorId") Long authorId,
                                                        @RequestParam(required = false, value = "executorId") Long executorId) {
        List<TaskDto> tasksDto = taskService.getTasks(authorId, executorId)
                .stream()
                .map(TaskDto::toDto)
                .toList();
        return ResponseEntity.ok().body(tasksDto);
    }

    @GetMapping(path = "/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> getTask(@PathVariable Long taskId) {
        TaskDto taskDto = TaskDto.toDto(taskService.getTask(taskId));
        return (ResponseEntity.ok().body(taskDto));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> createTask(Principal principal,
                                              @RequestBody @Valid TaskDto taskDto) {
        User user = userService.getUser(principal);
        Task taskCreated = taskService.saveTask(taskDto.toEntity(), user);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(String.format("/task/%d", taskCreated.getId()))
                .toUriString());
        return ResponseEntity.created(uri).body(TaskDto.toDto(taskCreated));
    }

    @Operation(summary = "Update task", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TaskDto.class)))
            }),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> updateTask(Principal principal,
                                              @RequestBody @Valid TaskDto taskDto) {
        User user = userService.getUser(principal);
        Task taskUpdated = taskService.saveTask(taskDto.toEntity(), user);
        return ResponseEntity.accepted().body(TaskDto.toDto(taskUpdated));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(Principal principal,
                                           @PathVariable Long taskId) {
        User user = userService.getUser(principal);
        taskService.deleteTask(taskId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{taskId}/set-executor")
    public ResponseEntity<TaskDto> setExecutor(Principal principal,
                                               @PathVariable Long taskId,
                                               @RequestBody @Valid UserDto executor) {
        User user = userService.getUser(principal);
        Task taskUpdated = taskService.setTaskExecutor(taskId, user.getId(), executor.getId());
        return ResponseEntity.accepted().body(TaskDto.toDto(taskUpdated));
    }

    @PutMapping("/{taskId}/set-status")
    public ResponseEntity<TaskDto> setStatus(Principal principal,
                                             @PathVariable Long taskId,
                                             @RequestBody @Valid Status status) {
        User user = userService.getUser(principal);
        Task taskUpdated = taskService.setTaskStatus(taskId, status, user.getId());
        return ResponseEntity.accepted().body(TaskDto.toDto(taskUpdated));
    }
}
