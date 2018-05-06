package de.iubh.fernstudium.iwmb.iubhtodoapp.activities.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;

/**
 * Created by ivanj on 10.03.2018.
 */

public class CreateNewUserDialog extends DialogFragment {

    public interface CreateNewUserDialogListener {
         void onDialogPositiveClick(DialogFragment dialog);
         void onDialogNegativeClick(DialogFragment dialog);
    }

    CreateNewUserDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CreateNewUserDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement CreateNewUserDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(getString(R.string.dialog_create_new_user))
                .setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogPositiveClick(CreateNewUserDialog.this);
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogNegativeClick(CreateNewUserDialog.this);
                        Toast.makeText(getContext(),getString(R.string.dialog_user_not_created), Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });

        return dialogBuilder.create();
    }

}
