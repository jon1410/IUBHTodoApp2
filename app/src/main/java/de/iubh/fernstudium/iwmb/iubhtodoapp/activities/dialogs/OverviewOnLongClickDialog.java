package de.iubh.fernstudium.iwmb.iubhtodoapp.activities.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;

public class OverviewOnLongClickDialog extends DialogFragment {

    public interface OverviewOnLongClickDialogListener {
        void onShowTodoDetails(DialogFragment dialog);

        void onDeleteTodo(DialogFragment dialog);
    }

    OverviewOnLongClickDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OverviewOnLongClickDialog.OverviewOnLongClickDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OverviewOnLongClickDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(R.string.on_long_click_dialog_title)
                .setItems(R.array.onLongClickDialogItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                listener.onShowTodoDetails(OverviewOnLongClickDialog.this);
                                break;
                            case 1:
                                listener.onDeleteTodo(OverviewOnLongClickDialog.this);
                                break;
                            default:
                                dismiss();
                        }
                    }
                })
                .setNeutralButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return dialogBuilder.create();
    }
}
