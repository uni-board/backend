package com.uniboard.board.data;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.uniboard.board.domain.AllBoardsRepository;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.UUID;


public class AllBoardsRepositoryJava implements AllBoardsRepository{
        private String nameDatabase = "CreatedBoards";
        private MongoClient mongoClient;
        private Document query = new Document("isSettings", true);

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

        public AllBoardsRepositoryJava(MongoClient client) {
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
        public String settings(String id){
            MongoDatabase database = mongoClient.getDatabase("CreatedBoards");
            MongoCollection todoCollection = database.getCollection(id);
            Document h = (Document) todoCollection.find(new Document("isSettings", true)).first();
            return (String) h.get("settings");
        }

        public void edit(String id, String settings){
            MongoDatabase database = mongoClient.getDatabase("CreatedBoards");
            MongoCollection todoCollection = database.getCollection(id);
            Bson updates = Updates.set("settings", settings);
            UpdateOptions options = new UpdateOptions().upsert(true);
            todoCollection.updateOne(query, updates, options);
            System.out.println(id);
        }

}