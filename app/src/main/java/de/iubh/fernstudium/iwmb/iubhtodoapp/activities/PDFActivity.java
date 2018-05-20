package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;

public class PDFActivity extends AppCompatActivity {

    public static final String FRAGMENT_PDF_TAG = "pdf_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_activity);
        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putString(Constants.FILENAME_KEY, getIntent().getStringExtra(Constants.FILENAME_KEY));
            Fragment pdfFragment = new PDFFragment();
            pdfFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.idPdfContainer, pdfFragment,
                            FRAGMENT_PDF_TAG)
                    .commit();
        }
    }
}
