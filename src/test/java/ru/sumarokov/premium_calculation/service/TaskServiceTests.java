package ru.sumarokov.premium_calculation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.sumarokov.premium_calculation.config.AbstractApplicationTest;
import ru.sumarokov.task_management_system.entity.Task;
import ru.sumarokov.task_management_system.entity.User;
import ru.sumarokov.task_management_system.helper.Priority;
import ru.sumarokov.task_management_system.helper.Status;
import ru.sumarokov.task_management_system.repository.TaskRepository;
import ru.sumarokov.task_management_system.repository.UserRepository;
import ru.sumarokov.task_management_system.service.TaskService;

public class TaskServiceTests extends AbstractApplicationTest {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void getTasks() {
        User userOne = userRepository.save(new User(null, "oneUser@mail.ru", "123"));
        User userTwo = userRepository.save(new User(null, "twoUser@mail.ru", "123"));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userTwo));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userTwo, userTwo));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userTwo, userTwo));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userTwo, userOne));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userTwo, userOne));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userTwo, userOne));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userTwo, userOne));

        Assert.assertEquals(taskService.getTasks(userOne.getId(), null, PageRequest.of(0, 20)).size(), 5);
        Assert.assertEquals(taskService.getTasks(userTwo.getId(), null, PageRequest.of(0, 20)).size(), 6);
        Assert.assertEquals(taskService.getTasks(userTwo.getId(), userOne.getId(), PageRequest.of(0, 20)).size(), 4);
        Assert.assertEquals(taskService.getTasks(null, userOne.getId(), PageRequest.of(0, 20)).size(), 8);
        Assert.assertEquals(taskService.getTasks(userOne.getId(), userOne.getId(), PageRequest.of(0, 20)).size(), 4);
        Assert.assertEquals(taskService.getTasks(userOne.getId(), userTwo.getId(), PageRequest.of(0, 20)).size(), 1);
        Assert.assertEquals(taskService.getTasks(userOne.getId(), userOne.getId(), PageRequest.of(1, 2)).size(), 2);
    }

    @Test
    public void getTask() {
        User userOne = userRepository.save(new User(null, "oneUser@mail.ru", "123"));
        User userTwo = userRepository.save(new User(null, "twoUser@mail.ru", "123"));
        Task taskExpected = taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userTwo));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userTwo, userTwo));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userTwo, userTwo));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userTwo, userOne));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userTwo, userOne));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userTwo, userOne));
        taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userTwo, userOne));

        Task task = taskService.getTask(taskExpected.getId());

        Assert.assertEquals(taskExpected.getExecutor().getId(), task.getExecutor().getId());
        Assert.assertEquals(taskExpected.getAuthor().getId(), task.getAuthor().getId());
        Assert.assertEquals(taskExpected.getPriority(), task.getPriority());
        Assert.assertEquals(taskExpected.getDescription(), task.getDescription());
        Assert.assertEquals(taskExpected.getTitle(), task.getTitle());
        Assert.assertEquals(taskExpected.getStatus(), task.getStatus());
    }

    @Test
    public void createTask() {
        User userOne = userRepository.save(new User(null, "oneUser@mail.ru", "123"));

        Task taskExpected = taskService.createTask(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));
        Task taskActual = taskRepository.findById(taskExpected.getId()).orElseThrow();

        Assert.assertEquals(taskExpected.getStatus(), taskActual.getStatus());
        Assert.assertEquals(taskExpected.getExecutor().getId(), taskActual.getExecutor().getId());
        Assert.assertEquals(taskExpected.getAuthor().getId(), taskActual.getAuthor().getId());
        Assert.assertEquals(taskExpected.getTitle(), taskActual.getTitle());
        Assert.assertEquals(taskExpected.getPriority(), taskActual.getPriority());
        Assert.assertEquals(taskExpected.getDescription(), taskActual.getDescription());
    }

    @Test
    public void updateTask() {
        User userOne = userRepository.save(new User(null, "oneUser@mail.ru", "123"));
        Task task = taskService.createTask(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));

        Task taskExpected = taskService.updateTask(new Task(task.getId(), "one", "one", Status.IN_PROGRESS, Priority.LOW, userOne, userOne), userOne.getId());
        Task taskActual = taskRepository.findById(taskExpected.getId()).orElseThrow();

        Assert.assertEquals(taskExpected.getStatus(), taskActual.getStatus());
        Assert.assertEquals(taskExpected.getExecutor().getId(), taskActual.getExecutor().getId());
        Assert.assertEquals(taskExpected.getAuthor().getId(), taskActual.getAuthor().getId());
        Assert.assertEquals(taskExpected.getTitle(), taskActual.getTitle());
        Assert.assertEquals(taskExpected.getPriority(), taskActual.getPriority());
        Assert.assertEquals(taskExpected.getDescription(), taskActual.getDescription());

    }

    @Test
    public void deleteTask() {
        User userOne = userRepository.save(new User(null, "oneUser@mail.ru", "123"));
        Task task = taskService.createTask(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));

        taskService.deleteTask(task.getId(), userOne.getId());
        Assert.expectThrows(Exception.class,
                () -> taskRepository.findById(task.getId()).orElseThrow());
    }

    @Test
    public void setTaskExecutor() {
        User userOne = userRepository.save(new User(null, "oneUser@mail.ru", "123"));
        User userTwo = userRepository.save(new User(null, "twoUser@mail.ru", "123"));
        Task task = taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));

        taskService.setTaskExecutor(task.getId(), userOne.getId(), userTwo.getId());
        Task taskActual = taskRepository.findById(task.getId()).orElseThrow();

        Assert.assertEquals(userTwo.getId(), taskActual.getExecutor().getId());
    }

    @Test
    public void setTaskStatus() {
        User userOne = userRepository.save(new User(null, "oneUser@mail.ru", "123"));
        Task task = taskRepository.save(new Task(null, "oneTitle", "oneDescriptions", Status.COMPLETED, Priority.HIGH, userOne, userOne));

        taskService.setTaskStatus(task.getId(), Status.PENDING, userOne.getId());
        Task taskActual = taskRepository.findById(task.getId()).orElseThrow();

        Assert.assertEquals(Status.PENDING, taskActual.getStatus());
    }
}
