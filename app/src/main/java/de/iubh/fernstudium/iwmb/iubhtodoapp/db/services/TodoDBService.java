package de.iubh.fernstudium.iwmb.iubhtodoapp.db.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.TodoEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.User;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.TodoStatus;
import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

/**
 * Created by ivanj on 11.03.2018.
 */

public class TodoDBService {

    private EntityDataStore<Persistable> dataStore;

    public TodoDBService(EntityDataStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    public Todo createTodo(String description, String title, Calendar dueDate, User assignee){
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setDescription(description);
        todoEntity.setTitle(title);
        todoEntity.setDueDate(new Timestamp(dueDate.getTime().getTime()));
        todoEntity.setStatus(TodoStatus.OPEN);
        todoEntity.setFavoriteFlag(false);
        todoEntity.setUser(assignee);

        return dataStore.insert(todoEntity);
    }

    public boolean deleteTodo(int id){
        TodoEntity todoEntity = (TodoEntity) getTodo(id);
        if(todoEntity == null){
            return false;
        }
        dataStore.delete(todoEntity);
        return true;
    }

    public Todo getTodo(int id){
        TodoEntity todoEntity = dataStore.findByKey(TodoEntity.class, id);
        return todoEntity;
    }

    public boolean markTodoAsFavorite(Todo todo){
        TodoEntity todoEntity = (TodoEntity) getTodo(todo.getId());
        if(todoEntity == null){
            return false;
        }
        todoEntity.setFavoriteFlag(true);
        dataStore.update(todoEntity);
        return true;
    }

    public List<Todo> getTodosForUser(User user){
        Result<TodoEntity> queryResult = dataStore.select(TodoEntity.class).where(TodoEntity.USER.eq(user)).get();
        return new ArrayList<Todo>(queryResult.toList());
    }

}
