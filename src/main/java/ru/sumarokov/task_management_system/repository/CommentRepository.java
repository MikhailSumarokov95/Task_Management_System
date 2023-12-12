package ru.sumarokov.task_management_system.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sumarokov.task_management_system.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT * " +
            "FROM comment " +
            "WHERE :taskId = task_id " +
            "LIMIT :pageSize OFFSET :offset",
            nativeQuery = true)
    List<Comment> findByTaskId(Long taskId,
                               Integer pageSize,
                               Integer offset);
}

