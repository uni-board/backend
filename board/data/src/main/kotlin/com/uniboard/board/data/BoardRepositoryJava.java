package com.uniboard.board.data;
import com.uniboard.board.domain.BoardRepository;
import com.uniboard.board.domain.BoardObject;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoardRepositoryJava implements BoardRepository {
    private String nameDatabase = "CreatedBoards";
    private MongoClient mongoClient;

    public BoardRepositoryJava(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
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


    public List<BoardObject> all(String boardId){
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        MongoCollection<Document> collection = database.getCollection(boardId);
        List<Document> documents = collection.find().into(new ArrayList<>());
        return documents.stream().filter(x->x.get("id")!=null).map(this::fromDocumentToBoardObject).collect(Collectors.toList());
    }


    public void add(String boardId, BoardObject element) {
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        MongoCollection<Document> collection = database.getCollection(boardId);
        Document doc = new Document("id", element.getId()).append("state", element.getState());
        collection.insertOne(doc);
    }


    public String get(String boardId, String id){
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        MongoCollection<Document> collection = database.getCollection(boardId);
        return (String) collection.find(Filters.eq("id", id)).first().get("state");
    }


    public void delete(String boardId, String id){
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        MongoCollection<Document> collection = database.getCollection(boardId);
        collection.deleteOne(Filters.eq("id", id));
    }

    public void set(String boardId, BoardObject element) {
        MongoDatabase database = mongoClient.getDatabase(nameDatabase);
        MongoCollection<Document> collection = database.getCollection(boardId);
        delete(boardId, element.getId());
        add(boardId, element);
    }
}