package ru.sumarokov.task_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sumarokov.task_management_system.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByExecutorIdAndExecutorId(Long authorId, Long executorId);
}
