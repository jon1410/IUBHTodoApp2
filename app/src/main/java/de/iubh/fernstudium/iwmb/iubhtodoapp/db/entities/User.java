package de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.os.Parcelable;

import java.util.List;

import io.requery.Entity;
import io.requery.Key;
import io.requery.OneToMany;
import io.requery.Persistable;
import io.requery.Table;

/**
 * Created by ivanj on 03.03.2018.
 */

@Table(name = "User")
@Entity
public interface User extends Persistable, Observable, Parcelable {

    @Key
    @Bindable
    String getUserName();

    @Bindable
    String getEncryptedPw();

    @OneToMany
    List<Todo> getTodos();
}
