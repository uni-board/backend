package com.uniboard.storage.data;

import com.uniboard.storage.domain.StorageDB;
import com.uniboard.storage.domain.StorageRepository;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class StorageRepositoryImpl implements StorageRepository {

    private final StorageDB db;
    private final String PATH = null;

    public StorageRepositoryImpl(StorageDB db) {
        this.db = db;
    }

    @Override
    public long put(long boardId, @NotNull InputStream stream) {
        long id = db.add(boardId);
        try {
            Files.copy(stream, Path.of(PATH + "/" + boardId + "/" + id));
        } catch (IOException e) {
            // TODO: Add logging
        }
        return id;
    }

    @NotNull
    @Override
    public InputStream get(long boardId, long id) {
        boolean exists = db.fileExists(boardId, id);
        if (exists) {
            try {
                return new FileInputStream(PATH + "/" + boardId + "/" + id);
            } catch (FileNotFoundException e) {
                // TODO: Add logging
            }
        }
        return InputStream.nullInputStream();
    }
}
