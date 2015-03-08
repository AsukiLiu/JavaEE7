package org.asuki.dao;

import static com.google.common.collect.Collections2.filter;
import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.IOException;

import org.asuki.common.Resources;
import org.asuki.common.util.Cryptos;
import org.asuki.dao.PostDao;
import org.asuki.model.cdi.VetoExtension;
import org.asuki.model.entity.Comment;
import org.asuki.model.entity.Post;
import org.asuki.model.entity.CommentNegative;
import org.asuki.model.entity.CommentPositive;
import org.asuki.model.enums.Mode;
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

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import javax.persistence.PersistenceUnitUtil;

//NOTE  VM arguments: -Djava.util.logging.manager=org.jboss.logmanager.LogManager
@RunWith(Arquillian.class)
public class PostDaoIT {

    private static final Logger LOG = Logger.getLogger(PostDaoIT.class
            .getName());

    @Deployment
    public static WebArchive createDeployment() throws IOException {
        // @formatter:off
        final WebArchive war = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addPackages(true, 
                        "org.asuki.model",
                        "org.asuki.dao")
                .addClasses(
                        Cryptos.class,
                        Resources.class)
                .addAsServiceProvider(Extension.class, VetoExtension.class)
                .addAsWebInfResource("META-INF/jboss-deployment-structure.xml",
                        "jboss-deployment-structure.xml")
                .addAsWebInfResource("META-INF/persistence.xml",
                        "classes/META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE,
                        ArchivePaths.create("beans.xml"));
        // @formatter:on

        LOG.info(war.toString(Formatters.VERBOSE));

        return war;
    }

    @Inject
    private PostDao postDao;

    @Inject
    private Instance<Post> post;

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
        final List<Long> ids = asList(1L, 2L);

        List<Post> posts = postDao.findAllPosts();
        assertThat(filter(posts, post -> !post.isApproved()).size(), is(2));

        postDao.update(ids);
        posts = postDao.findAllPosts();
        assertThat(filter(posts, post -> post.isApproved()).size(), is(2));

        postDao.delete(ids);
        posts = postDao.findAllPosts();
        assertThat(posts.size(), is(0));
    }

    @Test
    @InSequence(2)
    public void testEntityGraph() {
        final Long id = 3L;

        Post post = postDao.findById(id);
        List<Comment> comments = asList(new Comment("content1").withPost(post),
                new Comment("content2").withPost(post));
        post.setComments(comments);
        postDao.edit(post);

        PersistenceUnitUtil util = postDao.getPersistenceUnitUtil();

        post = postDao.findById(id);

        assertThat(util.isLoaded(post, "title"), is(true));
        assertThat(util.isLoaded(post, "body"), is(true));
        assertThat(util.isLoaded(post, "comments"), is(false));

        post = postDao.findByIdWithGraph(id);

        assertThat(util.isLoaded(post, "title"), is(true));
        assertThat(util.isLoaded(post, "body"), is(true));
        assertThat(util.isLoaded(post, "comments"), is(true));
        out.println(post.toString());
    }

    @Test
    @InSequence(3)
    public void testInheritance() {
        final Long id = 5L;

        Post post = postDao.findByIdWithGraph(id);

        CommentPositive commentP = new CommentPositive("Positive");
        commentP.setPost(post);
        CommentNegative commentN = new CommentNegative("Negative");
        commentN.setPost(post);

        post.getComments().add(commentP);
        post.getComments().add(commentN);

        postDao.edit(post);

        assertThat(postDao.countCommentsById(id), is(2L));
        assertThat(postDao.countCommentsById_2(id), is(2L));
    }

    @Test
    @InSequence(4)
    public void testConverter() {
        final Long id = 7L;

        Post post = postDao.findById(id);
        post.setMode(Mode.READ_WRITE);
        postDao.edit(post);

        post = postDao.findById(id);

        assertThat(post.getMode(), is(Mode.READ_WRITE));
    }

    @Test
    @InSequence(5)
    public void testVetoExtension() {
        assertThat(post.isUnsatisfied(), is(true));
        assertThat(post.isAmbiguous(), is(false));
    }

}
