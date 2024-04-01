package com.uniboard.board.data;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.uniboard.board.domain.BoardObject;
import com.uniboard.board.domain.BoardRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoardRepositoryImpl implements BoardRepository {
    private String nameDatabase = "CreatedBoards";
    private MongoClient mongoClient;

    public BoardRepositoryImpl(MongoClient client) {
        mongoClient = client;
    }

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


    private BoardObject fromDocumentToBoardObject(Document document){
        return new BoardObject((String) document.get("id"), (String) document.get("state"));
    }


    public List<BoardObject> all(long boardId){
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        MongoCollection<Document> collection = database.getCollection(Long.toString(boardId));
        List<Document> documents = collection.find().into(new ArrayList<>());
        return documents.stream().map(this::fromDocumentToBoardObject).collect(Collectors.toList());
    }


    public void add(long boardId, BoardObject element) {
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        MongoCollection<Document> collection = database.getCollection(Long.toString(boardId));
        Document doc = new Document("id", element.getId()).append("state", element.getState());
        collection.insertOne(doc);
    }


    public String get(long boardId, String id){
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        MongoCollection<Document> collection = database.getCollection(Long.toString(boardId));
        return (String) collection.find(Filters.eq("id", id)).first().get("state");
    }


    public void delete(long boardId, String id){
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        MongoCollection<Document> collection = database.getCollection(Long.toString(boardId));
        collection.deleteOne(Filters.eq("id", id));
    }

    public void set(long boardId, BoardObject element) {
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        MongoCollection<Document> collection = database.getCollection(Long.toString(boardId));
        delete(boardId, element.getId());
        add(boardId, element);
    }
}
