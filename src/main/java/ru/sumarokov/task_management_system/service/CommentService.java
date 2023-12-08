package ru.sumarokov.task_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.sumarokov.task_management_system.entity.Comment;
import ru.sumarokov.task_management_system.entity.User;
import ru.sumarokov.task_management_system.exception.EntityNotFoundException;
import ru.sumarokov.task_management_system.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getTaskComments(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, commentId));
    }

    public Comment saveComment(Comment comment, User user) {
        if (comment.getId() != null) {
            checkWhetherCommentCanBeChanged(comment.getId(), user.getId());
        }
        else {
            comment.setAuthor(user);
        }
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, Long userId) {
        checkWhetherCommentCanBeChanged(commentId, userId);
        commentRepository.deleteById(commentId);
    }

    private void checkWhetherCommentCanBeChanged(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, commentId));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new AccessDeniedException("Нельзя редактировать/удалять чужие комментарии");
        }
    }
}
