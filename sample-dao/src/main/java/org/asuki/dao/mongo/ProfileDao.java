package org.asuki.dao.mongo;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@ApplicationScoped
public class ProfileDao {

    private static final String PROFILES_COLLECTION = "profiles";

    @Inject
    private Logger log;

    @Inject
    private DB db;

    @Getter
    @Setter
    private DBCollection profiles;

    @PostConstruct
    public void postConstruct() {
        profiles = db.getCollection(PROFILES_COLLECTION);
    }

    public void create(String username, String detail) {
        profiles.save(new BasicDBObject().append("username", username).append(
                "detail", detail));
    }

    public DBObject find(String username, String detail) {
        BasicDBObject query = new BasicDBObject("username", username).append(
                "detail", detail);
        log.info("Query: {}", query.toString());

        return profiles.findOne(query);
    }

    public boolean isExisted(String username, String detail) {
        DBObject exists = find(username, detail);
        return exists != null;
    }

    public void updateByPush(String username, String other) {
        profiles.update(new BasicDBObject("username", username),
                new BasicDBObject("$push", new BasicDBObject("other", other)));
    }

    public void updateByPull(String username, String other) {
        BasicDBObject findQuery = new BasicDBObject("username", username);
        BasicDBObject updateQuery = new BasicDBObject();
        updateQuery.put("$pull", new BasicDBObject("other", other));

        profiles.update(findQuery, updateQuery);
        // or
        profiles.update(new BasicDBObject("username", username),
                new BasicDBObject("$pull", new BasicDBObject("other", other)));
    }

    public void remove(String username) {
        profiles.remove(new BasicDBObject("username", username));
    }

}
