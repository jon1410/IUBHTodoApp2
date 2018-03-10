package de.iubh.fernstudium.iwmb.iubhtodoapp.db.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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

@RunWith(AndroidJUnit4.class)
public class UserAndroidTest {

    private EntityDataStore<Persistable> dataStore;

    @Before
    public void initialize(){
        dataStore = DBUtil.createReactiveEntityStore(InstrumentationRegistry.getTargetContext());
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
        UserEntity result = dataStore.insert(userEntity);
        assertNotNull(result);
        System.out.println(result.toString());

        Scalar<Integer> r = dataStore.count(User.class).get();
        assertNotNull(r);

        assertEquals(new Integer(1), r.value());
    }

    private UserEntity createUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("User1");
        userEntity.setEncryptedPw("pwEncrypted");
        return userEntity;
    }

}
