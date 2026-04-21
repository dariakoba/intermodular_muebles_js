package com.example.peliculas.interfaces;

import java.time.LocalDateTime;

public interface SoftDeletable {

    LocalDateTime getDeletedAt();

    void setDeletedAt(LocalDateTime deletedAt);

    default boolean isDeleted() {
        return getDeletedAt() != null;
    }

    default void markDeleted() {
        setDeletedAt(LocalDateTime.now());
    }
}