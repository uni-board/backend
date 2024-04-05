package com.uniboard.board.data

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.uniboard.board.domain.AllBoardsRepository
import java.util.*

class AllBoardsRepositoryImpl(private val mongoClient: MongoClient) : AllBoardsRepository {
    private val nameDatabase = "CreatedBoards"

    private val freeId: UUID
        get() = UUID.randomUUID()


    override fun add(): String {
        val database = mongoClient.getDatabase(nameDatabase)
        val uuid = freeId
        database.createCollection(uuid.toString())
        return uuid.toString()
    }

    override fun delete(idBoard: String) {
        val database = mongoClient.getDatabase("CreatedBoards")
        val todoCollection: MongoCollection<*> = database.getCollection(idBoard)
        todoCollection.drop()
    }

    override fun exists(idBoard: String): Boolean {
        val database = mongoClient.getDatabase("CreatedBoards")
        return hasCollection(database, idBoard)
    }

    companion object {
        private fun hasCollection(db: MongoDatabase, collectionName: String?): Boolean {
            assert(collectionName != null && !collectionName.isEmpty())
            db.listCollectionNames().iterator().use { cursor ->
                while (cursor.hasNext()) if (cursor.next() == collectionName) return true
            }
            return false
        }
    }
}