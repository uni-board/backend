package com.uniboard.storage.data;

import com.uniboard.core.domain.BuildConfig;
import com.uniboard.storage.domain.StorageDB;
import com.uniboard.storage.domain.StorageRepository;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class StorageRepositoryImpl implements StorageRepository {

    private final StorageDB db;
    private final String PATH;

    public StorageRepositoryImpl(StorageDB db, BuildConfig config) {
        var dir = new File(config.getWORKDIR(), "storage");
        dir.mkdirs();
        PATH = dir.getAbsolutePath();
        System.out.println(PATH);
        this.db = db;
    }

    @NotNull
    @Override
    public String put(@NotNull InputStream stream) {
        String id = db.add();
        try {
            Files.copy(stream, Path.of(PATH + "/" + id));
        } catch (IOException e) {
            // TODO: Add logging
        }
        return id;
    }

    @NotNull
    @Override
    public InputStream get(@NotNull String id) {
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
    public boolean fileExists(@NotNull String id) {
        return db.fileExists(id);
    }
}
