package de.iubh.fernstudium.iwmb.iubhtodoapp.utils.unit.test;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.TodoEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.User;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.UserEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.TodoStatus;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.TodoSorter;

public class SorterTest {

    List<Todo> todoList;

    @Before
    public void init() throws InterruptedException {
        todoList = new ArrayList<>();
        User u = createUser();
        for (int i=0; i<=5; i++){
            TodoEntity t = new TodoEntity();
            t.setUser(u);
            //t.setStatus(TodoStatus.OPEN);
            t.setTitle("title" +i);
            t.setDueDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
            t.setDescription("desc" +i);
            if(i==3){
                t.setFavoriteFlag(true);
            }else{
                t.setFavoriteFlag(false);
            }
            todoList.add(t);
            Thread.sleep(100);
        }
    }

    private User createUser() {
        UserEntity u = new UserEntity();
        u.setEncryptedPw("test");
        u.setUserName("name");
        return u;
    }

    @Test
    public void testSortByFavoriteFlag(){
        Assert.assertTrue(todoList.size() == 6);
        List<Todo> sorted = TodoSorter.sortTodosByFavoriteFlag(todoList);
        Assert.assertTrue(sorted.size() == 6);
        System.out.println(sorted);
    }

    @Test
    public void testSortByDueDateFlag(){
        Assert.assertTrue(todoList.size() == 6);
        List<Todo> sorted = TodoSorter.sortTodosByDueDate(todoList);
        Assert.assertTrue(sorted.size() == 6);
        System.out.println(sorted);
    }
}
