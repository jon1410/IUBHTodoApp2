package de.iubh.fernstudium.iwmb.iubhtodoapp.db.service.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import de.iubh.fernstudium.iwmb.iubhtodoapp.BuildConfig;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.User;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.UserEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.services.UserDBService;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.exceptions.UserNotFoundException;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.DBUtil;
import io.reactivex.Single;
import io.requery.Persistable;
import io.requery.query.Return;
import io.requery.query.Scalar;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveScalar;
import io.requery.sql.EntityDataStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by ivanj on 10.03.2018.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 26)
public class UserDBServiceTest {

    private EntityDataStore<Persistable> dataStore;
    private UserDBService userDBService;

    @Before
    public void initialize(){
        dataStore = DBUtil.createReactiveEntityStore(RuntimeEnvironment.application);
        userDBService = new UserDBService(dataStore);
    }

    @After
    public void tearDown(){
        if(dataStore != null){
            dataStore.close();
        }
    }

    @Test
    public void testInsert() throws UserNotFoundException {

        boolean userCreated = userDBService.createUser("testUser", "testPw");
        assertTrue(userCreated);

        Scalar<Integer> count = dataStore.count(User.class).get();
        assertNotNull(count);
        assertEquals(new Integer(1), count.value());

        User user = userDBService.getUser("testUser");
        assertNotNull(user);
        assertEquals("testUser", user.getUserName());
    }
}
