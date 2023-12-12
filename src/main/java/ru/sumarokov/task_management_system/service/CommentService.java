package ru.sumarokov.task_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    public List<Comment> getTaskComments(Long taskId, PageRequest page) {
        return commentRepository.findByTaskId(taskId, page);
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, commentId));
    }
    public Comment createComment(Comment comment) {
        if (comment.getId() != null) {
            throw new IllegalArgumentException("The new comment id must be null");
        }
        return commentRepository.save(comment);
    }

    public Comment updateComment(Comment comment, User user) {
        if (comment.getId() == null) {
            throw new IllegalArgumentException("An existing comment's id must not be null");
        }
        checkWhetherCommentCanBeChanged(comment.getId(), user.getId());
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
            throw new AccessDeniedException("You cannot edit/delete other people's comments");
        }
    }
}
