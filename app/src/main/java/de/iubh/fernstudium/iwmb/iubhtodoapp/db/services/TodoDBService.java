package de.iubh.fernstudium.iwmb.iubhtodoapp.db.services;

import android.text.TextUtils;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.TodoEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.User;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.UserEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.TodoStatus;
import io.requery.Persistable;
import io.requery.query.OrderingExpression;
import io.requery.query.Result;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.EntityDataStore;

/**
 * Created by ivanj on 11.03.2018.
 */

public class TodoDBService {

    private ReactiveEntityStore<Persistable> dataStore;

    public TodoDBService(EntityDataStore<Persistable> dataStore) {
        this.dataStore = ReactiveSupport.toReactiveStore(dataStore);
    }

    public TodoDBService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    public Todo createTodo(String description, String title, Calendar dueDate, User assignee, boolean isFavourite){
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setDescription(description);
        todoEntity.setTitle(title);
        todoEntity.setDueDate(new Timestamp(dueDate.getTime().getTime()));
        todoEntity.setStatus(TodoStatus.OPEN);
        todoEntity.setFavoriteFlag(isFavourite);
        todoEntity.setUser(assignee);

        return dataStore.toBlocking().insert(todoEntity);
    }

    public Todo createTodo(String description, String title, Calendar dueDate, String assignee){
        UserEntity user = dataStore.toBlocking().findByKey(UserEntity.class, assignee);
        return createTodo(description, title, dueDate, user, false);
    }

    public Todo createTodo(String description, String title, Calendar dueDate, String assignee, boolean isFavourite){
        UserEntity user = dataStore.toBlocking().findByKey(UserEntity.class, assignee);
        return createTodo(description, title, dueDate, user, isFavourite);
    }

    public boolean deleteTodo(int id){
        TodoEntity todoEntity = (TodoEntity) getTodo(id);
        if(todoEntity == null){
            return false;
        }
        dataStore.toBlocking().delete(todoEntity);
        return true;
    }

    public Todo getTodo(int id){
        TodoEntity todoEntity = dataStore.toBlocking().findByKey(TodoEntity.class, id);
        return todoEntity;
    }

    public boolean markTodoAsFavorite(Todo todo){
        TodoEntity todoEntity = (TodoEntity) getTodo(todo.getId());
        if(todoEntity == null){
            return false;
        }
        todoEntity.setFavoriteFlag(true);
        dataStore.toBlocking().update(todoEntity);
        return true;
    }

    public Result<TodoEntity> getTodosForUser(User user){
        return dataStore.toBlocking().select(TodoEntity.class).where(TodoEntity.USER.eq(user)).get();
    }

    public Result<TodoEntity> getTodosForUserAndDate(User user){
        DateTime jodaDate = DateTime.now().withTimeAtStartOfDay();
        Timestamp from = new Timestamp(jodaDate.toDate().getTime());
        Timestamp to = new Timestamp(jodaDate.plusDays(1).minusSeconds(1).toDate().getTime());
        return dataStore.toBlocking().select(TodoEntity.class).where(TodoEntity.USER.eq(user)).and(TodoEntity.DUE_DATE.between(from, to)).get();
    }

    public Result<TodoEntity> getTodosForUser(String userId){
        return this.getTodosForUser(getUser(userId));
    }

    public List<Todo> getTodosAsListForUser(String userId){
        Result<TodoEntity> todoEntityResult = this.getTodosForUser(userId);
        return new ArrayList<Todo>(todoEntityResult.toList());
    }

    public List<Todo> getTodosAsListForUserAndCurrentDate(String userId){
        Result<TodoEntity> todoEntityResult = this.getTodosForUserAndDate(getUser(userId));
        return new ArrayList<Todo>(todoEntityResult.toList());
    }

    private User getUser(String userId){
        return dataStore.toBlocking().findByKey(UserEntity.class, userId);
    }

}
