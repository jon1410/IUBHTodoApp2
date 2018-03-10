package de.iubh.fernstudium.iwmb.iubhtodoapp.db.services;

import org.mindrot.jbcrypt.BCrypt;

import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.User;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.UserEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.exceptions.UserNotFoundException;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.PasswordUtil;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.sql.EntityDataStore;

/**
 * Created by ivanj on 10.03.2018.
 */

public class UserDBService {

    private EntityDataStore<Persistable> dataStore;
    private User user;
    private boolean userCreated;

    public UserDBService(EntityDataStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    public void checkCredentials(String userName, String pw){

    }

    public User getUser(String userName) throws UserNotFoundException{
        UserEntity userEntity = dataStore.findByKey(UserEntity.class, userName);
        if(userEntity == null){
            throw new UserNotFoundException("User not found");
        }
        return userEntity;
    }

    public boolean createUser(String userName, String pw){
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);

        userEntity.setEncryptedPw(PasswordUtil.hashPw(pw));
        UserEntity createdUserEntity = dataStore.insert(userEntity);
        if(createdUserEntity != null){
            return true;
        }
        return false;
    }

    public void changePw(String oldPw, String newPw){

    }

}
