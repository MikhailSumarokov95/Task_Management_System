package ru.sumarokov.task_management_system.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.sumarokov.task_management_system.entity.Comment;
import ru.sumarokov.task_management_system.entity.Task;
import ru.sumarokov.task_management_system.entity.User;

public class CommentDto {

    private Long id;
    @NotEmpty(message = "Поле \"Описание\" должно быть заполнено")
    @Size(min = 0, max = 1024, message = "Поле \"Содержание\" должно быть длиной от 0 до 1024 символов")
    private String content;
    @NotNull(message = "Поле \"Автор\" должно быть заполнено")
    private UserDto authorDto;
    @NotNull(message = "Поле \"Задача\" должно быть заполнено")
    private TaskDto taskDto;

    public CommentDto(Long id, String content, UserDto authorDto, TaskDto taskDto) {
        this.id = id;
        this.content = content;
        this.authorDto = authorDto;
        this.taskDto = taskDto;
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

    public UserDto getAuthor() {
        return authorDto;
    }

    public void setAuthor(UserDto author) {
        this.authorDto = authorDto;
    }

    public TaskDto getTask() {
        return taskDto;
    }

    public void setTask(TaskDto taskDto) {
        this.taskDto = taskDto;
    }

    public Comment toEntity() {
        return new Comment(id, content, authorDto.toEntity(), taskDto.toEntity());
    }

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getContent(),
                UserDto.toDto(comment.getAuthor()),
                TaskDto.toDto(comment.getTask()));
    }
}
