package ru.sumarokov.task_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sumarokov.task_management_system.entity.Comment;

public interface CommentRepository extends JpaRepository<Long, Comment> {
}

