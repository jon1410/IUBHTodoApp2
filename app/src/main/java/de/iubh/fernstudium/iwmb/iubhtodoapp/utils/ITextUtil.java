package de.iubh.fernstudium.iwmb.iubhtodoapp.utils;

import android.text.format.DateFormat;
import android.text.format.DateUtils;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.TodoApplication;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;

public class ITextUtil {

    private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font titleFontSmall = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);

    public boolean createPdfFromTodo(Todo todo, String path){
        String absPathWithFileName = path + constructFileName(todo);
        try {
            return this.createPdfFromTodo(todo, new FileOutputStream(new File(absPathWithFileName), false));
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public boolean createPdfFromTodo(Todo todo, OutputStream outputStream){
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            addContent(document, todo);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void addContent(Document document, Todo todo) throws DocumentException {
        Paragraph preface  = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(getTextFromResource(R.string.pdf_head_Titel), titleFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(getTextFromResource(R.string.pdf_todo_Titel) + todo.getTitle(), titleFontSmall));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(getTextFromResource(R.string.pdf_todo_Description_head), titleFontSmall));
        preface.add(new Paragraph(todo.getDescription(), smallBold));
        addEmptyLine(preface, 1);
        String dueDate = DateFormat.format(Constants.DATE_FORMAT, new Date(todo.getDueDate().getTime())).toString();
        preface.add(new Paragraph(getTextFromResource(R.string.pdf_todo_dueDate_head) + dueDate, titleFontSmall));
        addEmptyLine(preface, 1);
        if(todo.getContactId() > 0){
            String contanctName = ContactUtils.getContactName(todo.getContactId());
            if(contanctName != null){
                preface.add(new Paragraph(getTextFromResource(R.string.pdf_todo_dueDate_head) + dueDate, titleFontSmall));
            }
        }
        document.add(preface);
    }

    private String getTextFromResource(int key) {
        return TodoApplication.resources.getString(key);
    }

    private  void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public static String constructFileName(Todo todo) {
        return todo.getId() + "_Todo_Export_" + todo.getTitle() + ".pdf";
    }
}
