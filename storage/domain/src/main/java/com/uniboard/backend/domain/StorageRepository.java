package com.uniboard.backend.domain;

import java.io.InputStream;

public interface StorageRepository {
    long put(long boardId, InputStream stream);

    InputStream get(long boardId, long id);

}
