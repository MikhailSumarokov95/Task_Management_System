package ru.sumarokov.task_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.sumarokov.task_management_system.dto.TaskDto;
import ru.sumarokov.task_management_system.entity.Task;
import ru.sumarokov.task_management_system.entity.User;
import ru.sumarokov.task_management_system.helper.Status;
import ru.sumarokov.task_management_system.service.TaskService;
import ru.sumarokov.task_management_system.service.UserService;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/task")
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

    @GetMapping(path = "/author/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getAuthorTasks(@PathVariable Long authorId) {
        List<TaskDto> tasksDto = taskService.getAuthorTasks(authorId)
                .stream()
                .map(TaskDto::toDto)
                .toList();
        return ResponseEntity.ok().body(tasksDto);
    }

    @GetMapping(path = "/executor/{executorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getExecutorTasks(@PathVariable Long executorId) {
        List<TaskDto> tasksDto = taskService.getExecutorTasks(executorId)
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
                                              @RequestBody TaskDto taskDto) {
        User user = userService.getUser(principal);
        Task taskCreated = taskService.saveTask(taskDto.toEntity(), user);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(String.format("/task/%d", taskCreated.getId()))
                .toUriString());
        return ResponseEntity.created(uri).body(TaskDto.toDto(taskCreated));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> updateTask(Principal principal,
                                              @RequestBody TaskDto taskDto) {
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

    @PutMapping("/task/{taskId}/set-executor")
    public ResponseEntity<TaskDto> setExecutor(Principal principal,
                                               @PathVariable Long taskId,
                                               @RequestBody User executor) {
        User user = userService.getUser(principal);
        Task taskUpdated = taskService.setTaskExecutor(taskId, user.getId(), executor.getId());
        return ResponseEntity.accepted().body(TaskDto.toDto(taskUpdated));
    }

    @PutMapping("/task/{taskId}/set-status")
    public ResponseEntity<TaskDto> setStatus(Principal principal,
                                             @PathVariable Long taskId,
                                             @RequestBody Status status) {
        User user = userService.getUser(principal);
        Task taskUpdated = taskService.setTaskStatus(taskId, status, user.getId());
        return ResponseEntity.accepted().body(TaskDto.toDto(taskUpdated));
    }
}
