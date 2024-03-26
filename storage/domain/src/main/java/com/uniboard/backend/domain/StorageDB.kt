package com.uniboard.backend.domain;

public interface StorageDB {
    long add(long boardId);

    boolean fileExists(long boardId, long id);
    void delete(long id);
}
