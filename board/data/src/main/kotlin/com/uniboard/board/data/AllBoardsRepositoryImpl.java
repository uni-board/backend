package com.uniboard.board.data;

import com.uniboard.board.domain.AllBoardsRepository;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.UUID;


public class AllBoardsRepositoryImpl implements AllBoardsRepository {
    private String nameDatabase = "CreatedBoards";
    private MongoClient mongoClient;

    private static boolean hasCollection(final MongoDatabase db, final String collectionName) {
        assert db != null;
        assert collectionName != null && !collectionName.isEmpty();
        try (final MongoCursor<String> cursor = db.listCollectionNames().iterator()) {
            while (cursor.hasNext())
                if (cursor.next().equals(collectionName))
                    return true;
        }
        return false;
    }

    public AllBoardsRepositoryImpl(MongoClient client) {
        mongoClient = client;
    }

    private UUID getFreeId() {
        return UUID.randomUUID();
    }


    public String add() {
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        UUID uuid = getFreeId();
        database.createCollection(uuid.toString());
        return uuid.toString();
    }

    public void delete(String idBoard) {
        MongoDatabase database = mongoClient.getDatabase("CreatedBoards");
        MongoCollection todoCollection = database.getCollection(idBoard);
        todoCollection.drop();
    }

    public boolean exists(String idBoard) {
        MongoDatabase database = mongoClient.getDatabase("CreatedBoards");
        return hasCollection(database, idBoard);
    }
}