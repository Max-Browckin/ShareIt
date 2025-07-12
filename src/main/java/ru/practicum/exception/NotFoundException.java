package ru.practicum.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException of(String entityName, Object id) {
        return new NotFoundException(entityName + " with ID " + id + " not found");
    }
}
