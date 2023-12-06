package ru.sumarokov.task_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sumarokov.task_management_system.entity.Task;

public interface TaskRepository extends JpaRepository<Long, Task> {
}
