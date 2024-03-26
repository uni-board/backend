package com.uniboard.storage.data;

import com.uniboard.storage.domain.StorageDB;
import com.uniboard.storage.domain.StorageRepository;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class StorageRepositoryImpl implements StorageRepository {

    private final StorageDB db;
    private final String PATH;

    public StorageRepositoryImpl(StorageDB db) {
        PATH = new File(".").getAbsolutePath() + "/storage";
        this.db = db;
    }

    @Override
    public long put(@NotNull InputStream stream) {
        long id = db.add();
        try {
            Files.copy(stream, Path.of(PATH + "/" + id));
        } catch (IOException e) {
            // TODO: Add logging
        }
        return id;
    }

    @NotNull
    @Override
    public InputStream get(long id) {
        boolean exists = db.fileExists(id);
        if (exists) {
            try {
                return new FileInputStream(PATH + "/" + id);
            } catch (FileNotFoundException e) {
                // TODO: Add logging
            }
        }
        return InputStream.nullInputStream();
    }

    @Override
    public boolean fileExists(long id) {
        return db.fileExists(id);
    }
}
