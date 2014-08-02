package org.asuki.dao;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.asuki.model.entity.Post;
import org.slf4j.Logger;

public class PostDao extends BaseDao<Post, Long> {

    @Inject
    private Logger log;

    @Override
    protected Class<Post> getEntityClass() {
        return Post.class;
    }

    public List<Post> findAllPosts() {
        return findByQuery("SELECT p FROM Post p ORDER BY p.createdDate DESC");
    }

    public long countCommentsById(Long id) {
        // Same: JOIN p.comments c
        return countByQuery(
                id,
                "SELECT COUNT(c) FROM Post p JOIN TREAT(p.comments AS Comment) c WHERE p.id=:id");
    }

    @Transactional
    public void update(List<Long> ids) {
        int number = bulkUpdate(ids);
        log.info("Updated number: " + number);
    }

    @Transactional
    public void delete(List<Long> ids) {
        int number = bulkDelete(ids);
        log.info("Deleted number: " + number);
    }

}
