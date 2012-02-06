package org.jboss.as.quickstarts.tasksJsf.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.tasksJsf.DefaultDeployment;
import org.jboss.as.quickstarts.tasksJsf.Resources;
import org.jboss.as.quickstarts.tasksJsf.beans.Repository;
import org.jboss.as.quickstarts.tasksJsf.beans.RepositoryBean;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lukas Fryc
 */
@RunWith(Arquillian.class)
public class UserDaoTest {

    @Deployment
    public static WebArchive deployment() throws IllegalArgumentException, FileNotFoundException {
        return new DefaultDeployment()
                .withPersistence()
                .withImportedData()
                .getArchive()
                .addClasses(User.class, Task.class, UserDao.class, UserDaoImpl.class, Resources.class, Repository.class,
                        RepositoryBean.class);
    }

    @Inject
    UserDao userDao;

    @Inject
    Repository repository;

    @Test
    public void userDao_should_create_user_so_it_could_be_retrieved_from_userDao_by_username() {
        // given
        User created = new User("username1");

        // when
        userDao.createUser(created);
        User retrieved = userDao.getForUsername("username1");

        // then
        assertTrue(repository.isManaging(created));
        assertTrue(repository.isManaging(retrieved));
        assertEquals(created, retrieved);
    }

    @Test
    public void user_should_be_retrievable_from_userDao_by_username() {
        // given
        String username = "jdoe";

        // when
        User retrieved = userDao.getForUsername(username);

        // then
        assertEquals(username, retrieved.getUsername());
    }

    @Test
    public void userDao_should_return_null_when_searching_for_non_existent_user() {
        // given
        String nonExistent = "nonExistent";

        // when
        User retrieved = userDao.getForUsername(nonExistent);

        // then
        assertNull(retrieved);
    }
}
