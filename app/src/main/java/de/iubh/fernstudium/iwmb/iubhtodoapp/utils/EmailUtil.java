package de.iubh.fernstudium.iwmb.iubhtodoapp.utils;

import android.text.format.DateFormat;

import org.apache.commons.text.TextStringBuilder;

import java.util.Date;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.TodoApplication;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;

public class EmailUtil {

    public static String createEmailPlainTextContent(Todo todo){

        TextStringBuilder sb = new TextStringBuilder();
        sb.append(getTextFromResource(R.string.pdf_todo_Titel));
        sb.append(" ");
        sb.append(todo.getTitle());
        sb.appendNewLine();
        sb.append(getTextFromResource(R.string.pdf_todo_Description_head));
        sb.appendNewLine();
        sb.append(todo.getDescription());
        sb.appendNewLine();
        sb.appendNewLine();
        String dueDate = DateFormat.format(Constants.DATE_FORMAT, new Date(todo.getDueDate().getTime())).toString();
        sb.append(getTextFromResource(R.string.pdf_todo_dueDate_head));
        sb.append(" ");
        sb.append(dueDate);
        sb.appendNewLine();

        if(todo.getContactId() > 0){
            String contanctName = ContactUtils.getContactName(todo.getContactId());
            if(contanctName != null){
                sb.append(getTextFromResource(R.string.pdf_linked_contact_head));
                sb.append(contanctName);
                sb.appendNewLine();
            }
        }

        return sb.toString();
    }

    private static String getTextFromResource(int key) {
        return TodoApplication.resources.getString(key);
    }
}
