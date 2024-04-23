package com.uniboard.board.data;
import com.uniboard.board.domain.AllBoardsRepository;

import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.UUID;


public class AllBoardsRepositoryJava implements AllBoardsRepository {
    public String nameDatabase = "CreatedBoards";
    MongoClient mongoClient;
    private Document query = new Document("isSettings", true);

    private static boolean hasCollection(final MongoDatabase db, final String collectionName)
    {
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
    public AllBoardsRepositoryJava(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
    

    private UUID getFreeId(){
        return UUID.randomUUID();
    }


    public String add(){
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        String uuid = getFreeId().toString();
        database.createCollection(uuid);
        edit(uuid, "");
        return uuid;

    }

    public void delete(String idBoard) {
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        MongoCollection todoCollection = database.getCollection(idBoard);
        todoCollection.drop();
    }
    public boolean exists(String idBoard) {
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        return hasCollection(database, idBoard);

    }
    public String settings(String id){
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        MongoCollection todoCollection = database.getCollection(id);
        Document h = (Document) todoCollection.find(new Document("isSettings", true)).first();
        return (String) h.get("settings");
    }

    public void edit(String id, String settings){
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        MongoCollection todoCollection = database.getCollection(id);
        Bson updates = Updates.set("settings", settings);
        UpdateOptions options = new UpdateOptions().upsert(true);
        todoCollection.updateOne(query, updates, options);
    }
}
