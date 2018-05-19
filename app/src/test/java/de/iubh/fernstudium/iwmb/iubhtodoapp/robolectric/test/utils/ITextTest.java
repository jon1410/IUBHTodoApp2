package de.iubh.fernstudium.iwmb.iubhtodoapp.robolectric.test.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Calendar;

import de.iubh.fernstudium.iwmb.iubhtodoapp.BuildConfig;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.TodoEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.User;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.UserEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.TodoStatus;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.ITextUtil;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 26)
public class ITextTest {

    @Test
    public void testPdfDocumentCanBeCreated() throws FileNotFoundException {
        ITextUtil iTextUtil = new ITextUtil();
        TodoEntity todo = new TodoEntity();
        todo.setTitle("The Title");
        todo.setDescription("A very long Description, that need to be shown in the PDF Document. Ipsum Lorem blablablablablablablablablab testtestets testtestets testtestets testtestets testtestets testtestets testtestets testtestetstesttestets testtestets testtestets");
        todo.setStatus(TodoStatus.IN_PROGRESS);
        todo.setUser(createUser());
        todo.setDueDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        //OutputStream outputStream = new FileOutputStream(new File("C:/Users/ivanj/Desktop/IUBH/IWMB/IWMB02/PDFs"));
        assertTrue(iTextUtil.createPdfFromTodo(todo, "C:/Users/ivanj/Desktop/IUBH/IWMB/IWMB02/PDFs/"));
    }

    private User createUser() {
        UserEntity ue = new UserEntity();
        ue.setUserName("username");
        ue.setEncryptedPw("ow");
        return ue;
    }
}
