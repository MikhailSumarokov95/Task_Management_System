package ru.sumarokov.task_management_system.dto;

import ru.sumarokov.task_management_system.entity.Task;
import ru.sumarokov.task_management_system.entity.User;
import ru.sumarokov.task_management_system.helper.Priority;
import ru.sumarokov.task_management_system.helper.Status;

public class TaskDto {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private User author;
    private User executor;

    public TaskDto(Long id,
                   String title,
                   String description,
                   Status status,
                   Priority priority,
                   User author,
                   User executor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.author = author;
        this.executor = executor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public Task toEntity() {
        return new Task(id, title, description, status, priority, author, executor);
    }

    public static TaskDto toDto(Task task) {
        return new TaskDto(task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getAuthor(),
                task.getExecutor());
    }
}
