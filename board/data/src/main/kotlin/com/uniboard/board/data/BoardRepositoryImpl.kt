package com.uniboard.board.data

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.uniboard.board.domain.BoardObject
import com.uniboard.board.domain.BoardRepository
import org.bson.Document
import java.util.stream.Collectors

class BoardRepositoryImpl(private val mongoClient: MongoClient) : BoardRepository {
    private val nameDatabase = "CreatedBoards"

    private fun fromDocumentToBoardObject(document: Document): BoardObject {
        return BoardObject((document["id"] as String?)!!, (document["state"] as String?)!!)
    }


    override fun all(boardId: String): List<BoardObject> {
        val database = mongoClient.getDatabase(nameDatabase)
        val collection = database.getCollection(boardId)
        val documents: List<Document> = collection.find().into(ArrayList())
        return documents.stream().map { document: Document -> this.fromDocumentToBoardObject(document) }
            .collect(Collectors.toList())
    }


    override fun add(boardId: String, element: BoardObject) {
        val database = mongoClient.getDatabase(nameDatabase)
        val collection = database.getCollection(boardId)
        val doc = Document("id", element.id).append("state", element.state)
        collection.insertOne(doc)
    }


    override fun get(boardId: String, id: String): String {
        val database = mongoClient.getDatabase(nameDatabase)
        val collection = database.getCollection(boardId)
        return collection.find(Filters.eq("id", id)).first()!!["state"] as String
    }


    override fun delete(boardId: String, id: String) {
        val database = mongoClient.getDatabase(nameDatabase)
        val collection = database.getCollection(boardId)
        collection.deleteOne(Filters.eq("id", id))
    }

    override fun set(boardId: String, element: BoardObject) {
        val database = mongoClient.getDatabase(nameDatabase)
        val collection = database.getCollection(boardId)
        delete(boardId, element.id)
        add(boardId, element)
    }

    companion object {
        private fun hasCollection(db: MongoDatabase, collectionName: String?): Boolean {
            checkNotNull(db)
            assert(collectionName != null && !collectionName.isEmpty())
            db.listCollectionNames().iterator().use { cursor ->
                while (cursor.hasNext()) if (cursor.next() == collectionName) return true
            }
            return false
        }
    }
}
