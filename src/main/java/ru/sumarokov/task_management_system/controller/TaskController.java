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

@RestController()
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

    @Operation(summary = "Get all tasks", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks received"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasksDto = taskService.getAllTasks()
                .stream()
                .map(TaskDto::toDto)
                .toList();
        return ResponseEntity.ok().body(tasksDto);
    }

    @Operation(summary = "Receive a task with a filter by author or performer using pagination (if the wound filter parameters are null, then they will not be used for filtering)", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks received"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping(path = "/getTaskWithFilter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getAuthorAndExecutorTasks(@RequestParam(required = false, value = "authorId") Long authorId,
                                                                   @RequestParam(required = false, value = "executorId") Long executorId,
                                                                   @RequestParam(value = "page") Integer page,
                                                                   @RequestParam(value = "size") Integer size) {
        List<TaskDto> tasksDto = taskService.getTasks(authorId, executorId, PageRequest.of(page, size))
                .stream()
                .map(TaskDto::toDto)
                .toList();
        return ResponseEntity.ok().body(tasksDto);
    }

    @Operation(summary = "Get task", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task received"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping(path = "/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> getTask(@PathVariable Long taskId) {
        TaskDto taskDto = TaskDto.toDto(taskService.getTask(taskId));
        return (ResponseEntity.ok().body(taskDto));
    }

    @Operation(summary = "Create task", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
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
            @ApiResponse(responseCode = "202", description = "Task updated"),
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

    @Operation(summary = "Delete task", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(Principal principal,
                                           @PathVariable Long taskId) {
        User user = userService.getUser(principal);
        taskService.deleteTask(taskId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Set executor", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Executor appointed"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PutMapping("/{taskId}/set-executor")
    public ResponseEntity<TaskDto> setExecutor(Principal principal,
                                               @PathVariable Long taskId,
                                               @RequestBody @Valid UserDto executor) {
        User user = userService.getUser(principal);
        Task taskUpdated = taskService.setTaskExecutor(taskId, user.getId(), executor.getId());
        return ResponseEntity.accepted().body(TaskDto.toDto(taskUpdated));
    }

    @Operation(summary = "Set status", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Status changed"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PutMapping("/{taskId}/set-status")
    public ResponseEntity<TaskDto> setStatus(Principal principal,
                                             @PathVariable Long taskId,
                                             @RequestBody @Valid Status status) {
        User user = userService.getUser(principal);
        Task taskUpdated = taskService.setTaskStatus(taskId, status, user.getId());
        return ResponseEntity.accepted().body(TaskDto.toDto(taskUpdated));
    }
}
