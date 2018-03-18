package de.iubh.fernstudium.iwmb.iubhtodoapp.db.services;

import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.User;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.UserEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.exceptions.UserNotFoundException;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.PasswordUtil;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.EntityDataStore;

/**
 * Created by ivanj on 10.03.2018.
 */

public class UserDBService {

    private ReactiveEntityStore<Persistable> dataStore;

    public UserDBService(EntityDataStore<Persistable> dataStore) {
        this.dataStore = ReactiveSupport.toReactiveStore(dataStore);
    }

    public UserDBService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    public void checkCredentials(String userName, String pw){

    }

    public User getUser(String userName) throws UserNotFoundException{
        UserEntity userEntity = dataStore.toBlocking().findByKey(UserEntity.class, userName);
        if(userEntity == null){
            throw new UserNotFoundException("User not found");
        }
        return userEntity;
    }

    public boolean createUser(String userName, String pw){
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);

        userEntity.setEncryptedPw(PasswordUtil.hashPw(pw));
        UserEntity createdUserEntity = dataStore.toBlocking().insert(userEntity);
        if(createdUserEntity != null){
            return true;
        }
        return false;
    }

    public void changePw(String oldPw, String newPw){

    }

}
