package ru.sumarokov.premium_calculation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.sumarokov.premium_calculation.config.AbstractApplicationTest;
import ru.sumarokov.task_management_system.entity.Comment;
import ru.sumarokov.task_management_system.entity.Task;
import ru.sumarokov.task_management_system.entity.User;
import ru.sumarokov.task_management_system.helper.Priority;
import ru.sumarokov.task_management_system.helper.Status;
import ru.sumarokov.task_management_system.repository.CommentRepository;
import ru.sumarokov.task_management_system.repository.TaskRepository;
import ru.sumarokov.task_management_system.repository.UserRepository;
import ru.sumarokov.task_management_system.service.CommentService;

public class CommentServiceTests extends AbstractApplicationTest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void getTaskComments() {
        User userOne = userRepository.save(new User(null, "oneUser@mail.ru", "123"));
        User userTwo = userRepository.save(new User(null, "twoUser@mail.ru", "123"));
        Task taskOne = taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        Task taskTwo = taskRepository.save(new Task(null, "TwoTitle", "TwoDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        commentRepository.save(new Comment(null, "one", userOne, taskOne));
        commentRepository.save(new Comment(null, "two", userOne, taskOne));
        commentRepository.save(new Comment(null, "three", userOne, taskOne));
        commentRepository.save(new Comment(null, "four", userOne, taskOne));
        commentRepository.save(new Comment(null, "five", userTwo, taskOne));
        commentRepository.save(new Comment(null, "six", userTwo, taskTwo));
        commentRepository.save(new Comment(null, "seven", userTwo, taskTwo));
        commentRepository.save(new Comment(null, "eight", userTwo, taskTwo));
        commentRepository.save(new Comment(null, "nine", userTwo, taskTwo));

        Assert.assertEquals(commentService.getTaskComments(taskOne.getId(), PageRequest.of(0, 10)).size(), 5);
        Assert.assertEquals(commentService.getTaskComments(taskTwo.getId(), PageRequest.of(0, 10)).size(), 4);
        Assert.assertEquals(commentService.getTaskComments(taskOne.getId(), PageRequest.of(1, 2)).size(), 2);
    }

    @Test
    public void getComment() {
        User userOne = userRepository.save(new User(null, "oneUser@mail.ru", "123"));
        Task taskOne = taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        Comment commentExpected = commentRepository.save(new Comment(null, "one", userOne, taskOne));
        commentRepository.save(new Comment(null, "two", userOne, taskOne));
        commentRepository.save(new Comment(null, "three", userOne, taskOne));
        commentRepository.save(new Comment(null, "four", userOne, taskOne));

        Comment commentActual = commentService.getComment(commentExpected.getId());

        Assert.assertEquals(commentExpected.getAuthor().getId(), commentActual.getAuthor().getId());
        Assert.assertEquals(commentExpected.getContent(), commentActual.getContent());
        Assert.assertEquals(commentExpected.getTask().getId(), commentActual.getTask().getId());
    }

    @Test
    public void updateComment() {
        User userOne = userRepository.save(new User(null, "oneUser@mail.ru", "123"));
        User userTwo = userRepository.save(new User(null, "twoUser@mail.ru", "123"));
        Task taskOne = taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        Task taskTwo = taskRepository.save(new Task(null, "TwoTitle", "TwoDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        Comment comment = commentRepository.save(new Comment(null, "one", userOne, taskOne));

        Comment commentExpected = commentService.updateComment(new Comment(comment.getId(), "Two", userTwo, taskTwo), userOne.getId());
        Comment commentActual = commentRepository.findById(commentExpected.getId()).orElseThrow();

        Assert.assertEquals(commentExpected.getAuthor().getId(), commentActual.getAuthor().getId());
        Assert.assertEquals(commentExpected.getContent(), commentActual.getContent());
        Assert.assertEquals(commentExpected.getTask().getId(), commentActual.getTask().getId());
    }

    @Test
    public void deleteComment() {
        User userOne = userRepository.save(new User(null, "oneUser@mail.ru", "123"));
        Task taskOne = taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        Comment comment = commentRepository.save(new Comment(null, "one", userOne, taskOne));

        commentService.deleteComment(comment.getId(), userOne.getId());

        Assert.expectThrows(Exception.class, () -> commentRepository.findById(comment.getId()).orElseThrow());
    }
}
