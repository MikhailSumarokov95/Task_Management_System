package ru.sumarokov.task_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasks(Long authorId, Long executorId) {
        return taskRepository.findByExecutorIdAndExecutorId(authorId, executorId);
    }

    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(Task.class, taskId));
    }

    public Task saveTask(Task task, User user) {
        if (task.getId() != null) {
            checkWhetherTaskCanBeChanged(task.getId(), user.getId());
        } else {
            task.setAuthor(user);
        }
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
            throw new AccessDeniedException("Назначать исполнителя может только автор задачи");
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
            throw new AccessDeniedException("Только исполнитель задачи может менять её статус");
        }
        task.setStatus(status);
        return taskRepository.save(task);
    }

    private void checkWhetherTaskCanBeChanged(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(Task.class, taskId));
        if (!task.getAuthor().getId().equals(userId)) {
            throw new AccessDeniedException("Нельзя редактировать/удалять чужие задачи");
        }
    }
}
