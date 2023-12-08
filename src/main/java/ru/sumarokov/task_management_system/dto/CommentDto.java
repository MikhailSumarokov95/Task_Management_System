package ru.sumarokov.task_management_system.dto;

import ru.sumarokov.task_management_system.entity.Comment;
import ru.sumarokov.task_management_system.entity.Task;
import ru.sumarokov.task_management_system.entity.User;

public class CommentDto {

    private Long id;
    private String content;
    private User author;
    private Task task;

    public CommentDto(Long id, String content, User author, Task task) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Comment toEntity() {
        return new Comment(id, content, author, task);
    }

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getContent(),
                comment.getAuthor(),
                comment.getTask());
    }
}
