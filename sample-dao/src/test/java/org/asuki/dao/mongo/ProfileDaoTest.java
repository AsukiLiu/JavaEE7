package org.asuki.dao.mongo;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.github.fakemongo.junit.FongoRule;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class ProfileDaoTest {

    @Rule
    public FongoRule fongoRule = new FongoRule();

    @Mock
    private Logger log;

    @InjectMocks
    private ProfileDao profileDao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        DB db = fongoRule.getDB("jee7test");
        DBCollection profiles = db.getCollection("profiles");
        profileDao.setProfiles(profiles);
    }

    @Test
    public void testCrud() throws Exception {
        final String USER_NAME = "tester";
        final String DETAIL = "Male";

        assertThat(profileDao.getProfiles().count(), is(0L));

        profileDao.create(USER_NAME, DETAIL);
        boolean isExisted = profileDao.isExisted(USER_NAME, DETAIL);
        assertThat(isExisted, is(true));
        assertThat(profileDao.getProfiles().count(), is(1L));

        assertThat(profileDao.find(USER_NAME, DETAIL).toString(),
                endsWith("\"username\" : \"tester\" , \"detail\" : \"Male\"}"));

        profileDao.updateByPush(USER_NAME, "30");
        assertThat(
                profileDao.find(USER_NAME, DETAIL).toString(),
                endsWith("\"username\" : \"tester\" , \"detail\" : \"Male\" , \"other\" : [ \"30\"]}"));

        profileDao.updateByPull(USER_NAME, "30");
        assertThat(
                profileDao.find(USER_NAME, DETAIL).toString(),
                endsWith("\"username\" : \"tester\" , \"detail\" : \"Male\" , \"other\" : [ ]}"));

        profileDao.remove(USER_NAME);
        assertThat(profileDao.getProfiles().count(), is(0L));
    }
}
