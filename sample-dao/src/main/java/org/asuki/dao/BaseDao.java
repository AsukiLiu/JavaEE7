package org.asuki.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    public List<E> findAll(String query) {
        return em.createQuery(query, getEntityClass()).getResultList();
    }

    @Transactional
    public void create(E e) {
        em.persist(e);
    }

    @Transactional
    public void edit(E e) {
        em.merge(e);
    }

    protected int bulkUpdate(List<K> keys) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<E> update = cb.createCriteriaUpdate(getEntityClass());
        Root<E> root = update.from(getEntityClass());
        // TODO
        update.set(root.get("approved"), true).where(root.get("id").in(keys));

        return em.createQuery(update).executeUpdate();
    }

    protected int bulkDelete(List<K> keys) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<E> delete = cb.createCriteriaDelete(getEntityClass());
        Root<E> root = delete.from(getEntityClass());
        // TODO
        delete.where(root.get("id").in(keys));

        return em.createQuery(delete).executeUpdate();
    }
}
