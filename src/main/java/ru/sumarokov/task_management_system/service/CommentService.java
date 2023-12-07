package ru.sumarokov.task_management_system.service;

import org.springframework.stereotype.Service;
import ru.sumarokov.task_management_system.entity.Comment;
import ru.sumarokov.task_management_system.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getTaskComments(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }
}
