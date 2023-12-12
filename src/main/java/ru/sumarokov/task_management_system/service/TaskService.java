package ru.sumarokov.task_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.sumarokov.task_management_system.entity.Task;
import ru.sumarokov.task_management_system.entity.User;
import ru.sumarokov.task_management_system.exception.EntityNotFoundException;
import ru.sumarokov.task_management_system.helper.Status;
import ru.sumarokov.task_management_system.repository.TaskRepository;
import ru.sumarokov.task_management_system.repository.UserRepository;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getTasks(Long authorId, Long executorId, PageRequest pageRequest) {
        return taskRepository.findByAuthorIdAndExecutorId(authorId,
                executorId,
                pageRequest.getPageSize(),
                pageRequest.getPageSize() * pageRequest.getPageNumber());
    }

    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(Task.class, taskId));
    }

    public Task createTask(Task task) {
        if (task.getId() != null) {
            throw new IllegalArgumentException("The new task id must be null");
        }
        return taskRepository.save(task);
    }

    public Task updateTask(Task task, Long userId) {
        if (task.getId() == null) {
            throw new IllegalArgumentException("An existing task's id must not be null");
        }
        checkWhetherTaskCanBeChanged(task.getId(), userId);
        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId, Long userId) {
        checkWhetherTaskCanBeChanged(taskId, userId);
        taskRepository.deleteById(taskId);
    }

    public Task setTaskExecutor(Long taskId, Long userId, Long executorId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(Task.class, taskId));
        if (!task.getAuthor().getId().equals(userId)) {
            throw new AccessDeniedException("Only the task author can assign an executor");
        }
        User executor = userRepository.findById(executorId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, taskId));
        task.setExecutor(executor);
        return taskRepository.save(task);
    }

    public Task setTaskStatus(Long taskId, Status status, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(Task.class, taskId));
        if (!task.getExecutor().getId().equals(userId)) {
            throw new AccessDeniedException("Only the performer of a task can change its status");
        }
        task.setStatus(status);
        return taskRepository.save(task);
    }

    private void checkWhetherTaskCanBeChanged(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(Task.class, taskId));
        if (!task.getAuthor().getId().equals(userId)) {
            throw new AccessDeniedException("You cannot edit/delete other people's tasks");
        }
    }
}
