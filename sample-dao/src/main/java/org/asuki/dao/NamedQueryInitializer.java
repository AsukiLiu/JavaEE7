package org.asuki.dao;

import static org.asuki.common.Constants.Sqls.COUNT_COMMENTS_BY_ID;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Singleton
@Startup
public class NamedQueryInitializer {

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        Query query = em
                .createQuery("SELECT COUNT(c) FROM Post p JOIN p.comments c WHERE p.id=:id");
        em.getEntityManagerFactory().addNamedQuery(COUNT_COMMENTS_BY_ID, query);
    }

}
