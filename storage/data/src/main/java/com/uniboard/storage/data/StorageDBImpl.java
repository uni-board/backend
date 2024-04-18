package com.uniboard.storage.data;

import com.uniboard.storage.domain.StorageDB;

import com.mongodb.client.*;
import org.bson.Document;

public class StorageDBImpl implements StorageDB {
    public final String nameDatabase = "CreatedBoards";
    public final String nameCollection = "Storage";
    MongoClient mongoClient;
    private static boolean hasCollection(final MongoDatabase db, final String collectionName){
        assert db != null;
        assert collectionName != null && !collectionName.isEmpty();
        try (final MongoCursor<String> cursor = db.listCollectionNames().iterator())
        {
            while (cursor.hasNext())
                if (cursor.next().equals(collectionName))
                    return true;
        }
        return false;
    }

    public StorageDBImpl(MongoClient client) {
        mongoClient = client;
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);

        if (!hasCollection(database, nameCollection)){
            database.createCollection(nameCollection);
        }
    }


    public String add(){
        MongoCollection collection = mongoClient.getDatabase(nameDatabase).getCollection(nameCollection);
        return collection.insertOne(new Document()).getInsertedId().asObjectId().getValue().toString();
    }

    public void delete(String idBoard) {
        MongoDatabase database = mongoClient.getDatabase("CreatedBoards");
        var todoCollection = database.getCollection(idBoard);
        todoCollection.drop();
    }
    public boolean fileExists(String id) {
        MongoDatabase database = mongoClient.getDatabase("CreatedBoards");
        return hasCollection(database, nameCollection);
    }

}