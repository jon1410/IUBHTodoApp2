package de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.os.Parcelable;

import java.sql.Timestamp;

import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.TodoStatus;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;
import io.requery.Table;

/**
 * Created by ivanj on 03.03.2018.
 */

@Table(name = "Todo")
@Entity
public interface Todo extends Persistable, Observable, Parcelable {

    @Key
    @Generated
    int getId();

    @Bindable
    String getTitle();

    @Bindable
    String getDescription();

    @Bindable
    TodoStatus getStatus();

    @Bindable
    Timestamp getDueDate();

    @Bindable
    Boolean getFavoriteFlag();

    @ManyToOne
    User getUser();

}
