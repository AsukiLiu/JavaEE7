package org.asuki.dao;

import static com.google.common.collect.Collections2.filter;
import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.IOException;

import org.asuki.common.Resources;
import org.asuki.dao.PostDao;
import org.asuki.model.converter.UuidToBytesConverter;
import org.asuki.model.entity.Comment;
import org.asuki.model.entity.Post;
import org.asuki.model.listener.BaseEntityListener;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.PersistenceUnitUtil;

//NOTE  VM arguments: -Djava.util.logging.manager=org.jboss.logmanager.LogManager
@RunWith(Arquillian.class)
public class PostDaoIT {

    private static final Logger LOG = Logger.getLogger(PostDaoIT.class
            .getName());

    @Deployment
    public static WebArchive createDeployment() throws IOException {
        final WebArchive war = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addPackages(true, "org.asuki.model.entity", "org.asuki.dao")
                .addClasses(BaseEntityListener.class,
                        UuidToBytesConverter.class, BaseEntityListener.class,
                        Resources.class)
                .addAsWebInfResource("META-INF/jboss-deployment-structure.xml",
                        "jboss-deployment-structure.xml")
                .addAsWebInfResource("META-INF/persistence.xml",
                        "classes/META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE,
                        ArchivePaths.create("beans.xml"));

        LOG.info(war.toString(Formatters.VERBOSE));

        return war;
    }

    @Inject
    private PostDao postDao;

    @Before
    public void setUp() {
        postDao.create(new Post().withTitle("title1").withBody("body1")
                .withUuid(randomUUID()));
        postDao.create(new Post().withTitle("title2").withBody("body2")
                .withUuid(randomUUID()));
    }

    @Test
    @InSequence(1)
    public void testBulkCrud() {
        List<Post> posts = postDao.findAllPosts();
        assertThat(filter(posts, post -> !post.isApproved()).size(), is(2));

        postDao.update(asList(1L, 2L));
        posts = postDao.findAllPosts();
        assertThat(filter(posts, post -> post.isApproved()).size(), is(2));

        postDao.delete(asList(1L, 2L));
        posts = postDao.findAllPosts();
        assertThat(posts.size(), is(0));
    }

    @Test
    @InSequence(2)
    public void testEntityGraph() {
        Post post = postDao.findById(3L);
        List<Comment> comments = asList(new Comment("content1").withPost(post),
                new Comment("content2").withPost(post));
        post.setComments(comments);
        postDao.edit(post);

        PersistenceUnitUtil util = postDao.getPersistenceUnitUtil();

        post = postDao.findById(3L);

        assertThat(util.isLoaded(post, "title"), is(true));
        assertThat(util.isLoaded(post, "body"), is(true));
        assertThat(util.isLoaded(post, "comments"), is(false));

        post = postDao.findByIdWithGraph(3L);

        assertThat(util.isLoaded(post, "title"), is(true));
        assertThat(util.isLoaded(post, "body"), is(true));
        assertThat(util.isLoaded(post, "comments"), is(true));
        out.println(post.toString());
    }

}
