package com.uniboard.board.data

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.uniboard.board.domain.AllBoardsRepository
import org.bson.Document
import java.util.*

class AllBoardsRepositoryImpl(private val mongoClient: MongoClient) : AllBoardsRepository {
    private val nameDatabase = "CreatedBoards"
    private val query = Document("isSettings", true)

    private fun hasCollection(db: MongoDatabase, collectionName: String?): Boolean {
        assert(!collectionName.isNullOrEmpty())
        db.listCollectionNames().iterator().use { cursor ->
            while (cursor.hasNext()) if (cursor.next() == collectionName) return true
        }
        return false
    }

    private fun getFreeId(): UUID {
        return UUID.randomUUID()
    }


    override fun add(): String {
        val database: MongoDatabase = mongoClient.getDatabase(nameDatabase)
        val uuid = getFreeId()
        database.createCollection(uuid.toString())
        return uuid.toString()
    }

    override fun delete(id: String) {
        val database: MongoDatabase = mongoClient.getDatabase("CreatedBoards")
        val todoCollection: MongoCollection<*> = database.getCollection(id)
        todoCollection.drop()
    }

    override fun exists(id: String): Boolean {
        val database: MongoDatabase = mongoClient.getDatabase("CreatedBoards")
        return hasCollection(database, id)
    }

    override fun settings(id: String): String {
        val database: MongoDatabase = mongoClient.getDatabase("CreatedBoards")
        val todoCollection: MongoCollection<*> = database.getCollection(id)
        val h = todoCollection.find(Document("isSettings", true)).first() as Document
        return h["settings"].toString()
    }

    override fun edit(id: String, settings: String) {
        val database: MongoDatabase = mongoClient.getDatabase("CreatedBoards")
        val todoCollection: MongoCollection<*> = database.getCollection(id)
        val updates = Updates.set("settings", settings)
        val options = UpdateOptions().upsert(true)
        todoCollection.updateOne(query, updates, options)
    }
}