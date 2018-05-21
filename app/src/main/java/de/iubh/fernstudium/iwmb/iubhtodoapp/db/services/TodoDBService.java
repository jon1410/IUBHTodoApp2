package de.iubh.fernstudium.iwmb.iubhtodoapp.db.services;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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

    public Todo createTodo(String description, String title, Calendar dueDate, User assignee, boolean isFavourite, int contactId){
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setDescription(description);
        todoEntity.setTitle(title);
        todoEntity.setDueDate(new Timestamp(dueDate.getTime().getTime()));
        todoEntity.setStatus(TodoStatus.OPEN);
        todoEntity.setFavoriteFlag(isFavourite);
        todoEntity.setUser(assignee);
        todoEntity.setContactId(contactId);

        return dataStore.toBlocking().insert(todoEntity);
    }

    public Todo createTodo(String description, String title, Calendar dueDate, String assignee){
        UserEntity user = dataStore.toBlocking().findByKey(UserEntity.class, assignee);
        return createTodo(description, title, dueDate, user, false, 0);
    }

    public Todo createTodo(String description, String title, Calendar dueDate, String assignee, boolean isFavourite, int contactId){
        UserEntity user = dataStore.toBlocking().findByKey(UserEntity.class, assignee);
        return createTodo(description, title, dueDate, user, isFavourite, contactId);
    }

    public Todo updateFileName(int idToUpdate, String fileName){
        TodoEntity todoEntity = (TodoEntity) getTodo(idToUpdate);
        todoEntity.setFileName(fileName);
        return dataStore.toBlocking().update(todoEntity);
    }

    public Todo changeTodo(int idToUpdate, Todo todoValues){
        TodoEntity todoEntity = (TodoEntity) getTodo(idToUpdate);
        if(todoEntity == null){
            return null;
        }

        todoEntity.setDueDate(todoValues.getDueDate());
        todoEntity.setStatus(todoValues.getStatus());
        todoEntity.setDescription(todoValues.getDescription());
        todoEntity.setTitle(todoValues.getTitle());
        todoEntity.setFavoriteFlag(todoValues.getFavoriteFlag());
        todoEntity.setContactId(todoValues.getContactId());
        todoEntity.setFileName(todoValues.getFileName());

        return dataStore.toBlocking().update(todoEntity);
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
