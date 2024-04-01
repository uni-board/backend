package com.uniboard.core.data;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDBClientUtil {
    private final static String url = "mongodb://localhost:27017";

    public static MongoClient create(){
        return MongoClients.create(url);
    }
}
