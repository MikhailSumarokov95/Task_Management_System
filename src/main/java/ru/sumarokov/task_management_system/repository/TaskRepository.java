package ru.sumarokov.task_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sumarokov.task_management_system.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT * " +
            "FROM task " +
            "WHERE (:authorId is null or author_id = :authorId) AND " +
            "(:executorId is null or executor_id = :executorId) " +
            "LIMIT :pageSize OFFSET :offset",
            nativeQuery = true)
    List<Task> findByAuthorIdAndExecutorId(Long authorId,
                                           Long executorId,
                                           Integer pageSize,
                                           Integer offset);
}
