package de.iubh.fernstudium.iwmb.iubhtodoapp.robolectric.test.utils;

import android.text.format.DateFormat;

import org.apache.commons.text.TextStringBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import de.iubh.fernstudium.iwmb.iubhtodoapp.BuildConfig;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.TodoEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.User;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.UserEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.TodoStatus;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.EmailUtil;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 26)
public class EmailUtilTest {

    long today = Calendar.getInstance().getTime().getTime();

    @Test
    public void testCreateEmailPlainTextContent(){
        String expected = createExpectedString();
        TodoEntity todoEntity = createTodo();
        Assert.assertEquals(expected, EmailUtil.createEmailPlainTextContent(todoEntity));
    }

    private String createExpectedString() {
        TextStringBuilder sb = new TextStringBuilder();
        sb.append("Titel des Todo: The Title");
        sb.appendNewLine();
        sb.append("Beschreibung des Todo:");
        sb.appendNewLine();
        sb.append("A very long Description, that need to be shown in the the Email. Ipsum Lorem blablablablablablablablablab testtestets testtestets testtestets testtestets testtestets testtestets testtestets testtestetstesttestets testtestets testtestets");
        sb.appendNewLine();
        sb.appendNewLine();
        String dueDate = DateFormat.format(Constants.DATE_FORMAT, new Date(today)).toString();
        sb.append("Erledigen bis: ");
        sb.append(dueDate);
        sb.appendNewLine();

        return sb.toString();
    }

    private TodoEntity createTodo() {
        TodoEntity todo = new TodoEntity();
        todo.setTitle("The Title");
        todo.setDescription("A very long Description, that need to be shown in the the Email. Ipsum Lorem blablablablablablablablablab testtestets testtestets testtestets testtestets testtestets testtestets testtestets testtestetstesttestets testtestets testtestets");
        todo.setStatus(TodoStatus.IN_PROGRESS);
        todo.setUser(createUser());
        todo.setDueDate(new Timestamp(today));
        return todo;
    }

    private User createUser() {
        UserEntity ue = new UserEntity();
        ue.setUserName("username");
        ue.setEncryptedPw("ow");
        return ue;
    }
}
