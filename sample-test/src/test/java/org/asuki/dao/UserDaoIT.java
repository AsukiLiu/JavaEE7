package org.asuki.dao;

import static java.lang.System.out;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import org.asuki.common.Resources;
import org.asuki.common.util.Cryptos;
import org.asuki.model.cdi.VetoExtension;
import org.asuki.model.entity.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

//NOTE  VM arguments: -Djava.util.logging.manager=org.jboss.logmanager.LogManager
@RunWith(Arquillian.class)
public class UserDaoIT {

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
    private UserDao userDao;

    @Test
    public void testEncryptAndDecryptPassword() {

        User user = new User();
        user.setName("andy");
        user.setPassword("1234");
        user.setCardNumber("A1234");

        userDao.createUser(user);

        User createdUser = userDao.findUserById(1);
        out.println(createdUser);
        assertThat(createdUser.getPassword(), is("1234"));
        assertThat(createdUser.getCardNumber(), is("A1234"));

        createdUser.setPassword("abcd");
        createdUser.setCardNumber("B5678");
        userDao.updateUser(createdUser);

        User updatedUser = userDao.findUserById(1);
        out.println(updatedUser);
        assertThat(updatedUser.getPassword(), is("abcd"));
        assertThat(updatedUser.getCardNumber(), is("B5678"));
    }

}
