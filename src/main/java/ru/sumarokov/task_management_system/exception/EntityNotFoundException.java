package ru.sumarokov.task_management_system.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> classEntity, Long id) {
        super(String.format("Объект класса %s c id = %d не найден", classEntity.getName(), id));
    }

    public EntityNotFoundException(Class<?> classEntity, String name) {
        super(String.format("Объект класса %s c именем = %d не найден", classEntity.getName(), name));
    }

    public EntityNotFoundException(Class<?> classEntity) {
        super(String.format("Объект класса %s не найден", classEntity.getName()));
    }
}
