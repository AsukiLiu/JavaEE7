package org.asuki.dao.producers;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

@ApplicationScoped
public class MongoProducer {

    private DB db;

    @PostConstruct
    public void postConstruct() {
        try {
            this.db = new MongoClient("localhost", 27017).getDB("jee7test");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private void connect(String host, int port, String dbname, String username,
            String password) throws UnknownHostException {

        MongoClientOptions mongoClientOptions = MongoClientOptions.builder()
                .connectionsPerHost(20).build();
        MongoClient mongoClient = new MongoClient(
                new ServerAddress(host, port), mongoClientOptions);
        mongoClient.setWriteConcern(WriteConcern.SAFE);
        DB db = mongoClient.getDB(dbname);

        if (db.authenticate(username, password.toCharArray())) {
            this.db = db;
        } else {
            throw new RuntimeException("Fail to authenticate with MongoDB");
        }
    }

    @Produces
    public DB db() {
        return this.db;
    }

}
