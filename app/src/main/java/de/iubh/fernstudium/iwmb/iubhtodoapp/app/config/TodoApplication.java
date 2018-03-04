package de.iubh.fernstudium.iwmb.iubhtodoapp.app.config;

import android.app.Application;
import android.os.StrictMode;

import de.iubh.fernstudium.iwmb.iubhtodoapp.BuildConfig;
import de.iubh.fernstudium.iwmb.iubhtodoapp.entities.Models;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.DBUtil;
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;

/**
 * Created by ivanj on 03.03.2018.
 */

public class TodoApplication extends Application {

    private ReactiveEntityStore<Persistable> dataStore;

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.enableDefaults();
    }

    ReactiveEntityStore<Persistable> getDataStore() {
        if (dataStore == null) {
            dataStore = DBUtil.createReactiveEntityStore(this);
        }
        return dataStore;
    }

}
