package de.iubh.fernstudium.iwmb.iubhtodoapp.db.test;


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
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.DBUtil;
import io.reactivex.Single;
import io.requery.Persistable;
import io.requery.query.Scalar;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveScalar;
import io.requery.sql.EntityDataStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Created by ivanj on 04.03.2018.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 26)
public class UserRobolectricTest {

    private EntityDataStore<Persistable> dataStore;

    @Before
    public void initialize(){
        dataStore = DBUtil.createReactiveEntityStore(RuntimeEnvironment.application);
    }

    @After
    public void tearDown(){
        if(dataStore != null){
            dataStore.close();
        }
    }

    @Test
    public void testInsert(){
        UserEntity userEntity = createUserEntity();
        UserEntity createdEntity = dataStore.insert(userEntity);
        assertNotNull(createdEntity);
        System.out.println(createdEntity.toString());

        Scalar<Integer> count = dataStore.count(User.class).get();
        assertNotNull(count);
        assertEquals(new Integer(1), count.value());
    }

    private UserEntity createUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("User1");
        userEntity.setEncryptedPw("pwEncrypted");
        return userEntity;
    }

}
