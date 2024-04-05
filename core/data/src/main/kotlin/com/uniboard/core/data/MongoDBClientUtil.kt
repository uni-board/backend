package com.uniboard.core.data

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients

object MongoDBClientUtil {
    fun create(url: String): MongoClient {
        return MongoClients.create(url)
    }
}
