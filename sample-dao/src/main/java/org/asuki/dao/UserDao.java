package org.asuki.dao;

import static javax.ejb.TransactionAttributeType.SUPPORTS;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asuki.model.entity.User;

@LocalBean
@Stateless
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    @TransactionAttribute(SUPPORTS)
    public User findUserById(Integer id) {
        return em.find(User.class, id);
    }

    public void createUser(User user) {
        em.persist(user);
    }

    public User updateUser(User user) {
        User merged = em.merge(user);

        // NOTE: because changes won't be persisted
        merged.setPassword(user.getPassword());

        return merged;
    }

}
