package org.asuki.dao;

import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

public abstract class BaseDao<E, K> {

    @PersistenceContext
    private EntityManager em;

    protected abstract Class<E> getEntityClass();

    public E findById(K k) {
        return em.find(getEntityClass(), k);
    }

    // TODO refactor
    public E findByIdWithGraph(K key) {
        // #Approach one
        // EntityGraph<?> postEntityGraph=em.getEntityGraph("post");

        // #Approach two
        EntityGraph<?> postEntityGraph = em.createEntityGraph(getEntityClass());
        postEntityGraph.addAttributeNodes("title");
        postEntityGraph.addSubgraph("comments").addAttributeNodes("content");

        return em
                .createQuery("SELECT p FROM Post p WHERE p.id=:id",
                        getEntityClass())
                .setHint("javax.persistence.loadgraph", postEntityGraph)
                .setParameter("id", key).getResultList().get(0);
    }

    public List<E> findByQuery(String query) {
        return em.createQuery(query, getEntityClass()).getResultList();
    }

    public long countByQuery(K key, String query) {
        return (Long) em.createQuery(query).setParameter("id", key)
                .getSingleResult();
    }

    public long countByNamedQuery(K key, String queryKey) {
        return (Long) em.createNamedQuery(queryKey).setParameter("id", key)
                .getSingleResult();
    }

    @Transactional
    public void create(E e) {
        em.persist(e);
        em.flush();
    }

    @Transactional
    public void edit(E e) {
        em.merge(e);
        em.flush();
    }

    protected int bulkUpdate(List<K> keys) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<E> update = cb.createCriteriaUpdate(getEntityClass());
        Root<E> root = update.from(getEntityClass());
        // TODO refactor
        update.set(root.get("approved"), true).where(root.get("id").in(keys));

        return em.createQuery(update).executeUpdate();
    }

    protected int bulkDelete(List<K> keys) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<E> delete = cb.createCriteriaDelete(getEntityClass());
        Root<E> root = delete.from(getEntityClass());
        // TODO refactor
        delete.where(root.get("id").in(keys));

        return em.createQuery(delete).executeUpdate();
    }

    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return em.getEntityManagerFactory().getPersistenceUnitUtil();
    }
}
