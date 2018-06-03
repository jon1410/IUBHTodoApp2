package de.iubh.fernstudium.iwmb.iubhtodoapp.robolectric.test.utils;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.BuildConfig;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.TodoEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.User;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.UserEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.TodoStatus;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.TodoSorter;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 26)
public class SorterTest {

    List<Todo> todoList;

    @Before
    public void init() throws InterruptedException {
        todoList = new ArrayList<>();
        User u = createUser();
        for (int i=0; i<=5; i++){
            TodoEntity t = new TodoEntity();
            t.setUser(u);
            t.setStatus(TodoStatus.OPEN);
            t.setTitle("title" +i);
            t.setDueDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
            t.setDescription("desc" +i);
            if(i==3){
                t.setFavoriteFlag(true);
                t.setStatus(TodoStatus.IN_PROGRESS);
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
        Assert.assertTrue(sorted.get(0).getFavoriteFlag());
        Assert.assertFalse(sorted.get(1).getFavoriteFlag());
        System.out.println(sorted);
    }

    @Test
    public void testSortByDueDateFlag(){
        Assert.assertTrue(todoList.size() == 6);
        List<Todo> sorted = TodoSorter.sortTodosByDueDate(todoList);
        Assert.assertTrue(sorted.size() == 6);
        System.out.println(sorted);
    }

    @Test
    public void testSortByStatusInProgress(){
        Assert.assertTrue(todoList.size() == 6);
        List<Todo> sorted = TodoSorter.sortTodosByStatusInProgress(todoList);
        Assert.assertTrue(sorted.size() == 6);
        Assert.assertTrue(sorted.get(5).getStatus() == TodoStatus.OPEN);
        Assert.assertTrue(sorted.get(0).getStatus() == TodoStatus.IN_PROGRESS);
        System.out.println(sorted);
    }

    @Test
    public void testSortByStatusOpen(){
        Assert.assertTrue(todoList.size() == 6);
        List<Todo> sorted = TodoSorter.sortTodosByStatusOpen(todoList);
        Assert.assertTrue(sorted.size() == 6);
        Assert.assertTrue(sorted.get(0).getStatus() == TodoStatus.OPEN);
        Assert.assertTrue(sorted.get(5).getStatus() == TodoStatus.IN_PROGRESS);
        System.out.println(sorted);
    }
}
