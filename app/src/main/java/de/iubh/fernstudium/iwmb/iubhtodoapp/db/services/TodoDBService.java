package de.iubh.fernstudium.iwmb.iubhtodoapp.db.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.TodoEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.User;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.UserEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.TodoStatus;
import io.requery.Persistable;
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

    public Result<TodoEntity> getTodosForUser(String userId){
        UserEntity user = dataStore.toBlocking().findByKey(UserEntity.class, userId);
        return this.getTodosForUser(user);
    }

    public List<Todo> getTodosAsListForUser(String userId){
        Result<TodoEntity> todoEntityResult = this.getTodosForUser(userId);
        return new ArrayList<Todo>(todoEntityResult.toList());
    }

}
