package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;


public class DialogIntroducirTexto extends android.support.v4.app.DialogFragment {

    private EditText etTexto;

    private DialogInsertarTextoInterface mListener;

    public static DialogIntroducirTexto newInstance(String titulo, String mensaje) {
        DialogIntroducirTexto f = new DialogIntroducirTexto();
        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        args.putString("mensaje", mensaje);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        String titulo = getArguments().getString("titulo");
        String mensaje = getArguments().getString("mensaje");
        etTexto = new EditText(getActivity());
        etTexto.setInputType(InputType.TYPE_CLASS_TEXT);
        etTexto.selectAll();
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setView(etTexto)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String valor = etTexto.getText().toString();
                        if (mListener != null) {
                            mListener.onPossitiveDialogInsertarTexto(valor);
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null) {
                            mListener.onNegativeDialogInsertarTexto();
                        }
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public void setOnDialogInsertarTextoListener(DialogIntroducirTexto.DialogInsertarTextoInterface mListener) {
        this.mListener = mListener;
    }

    public interface DialogInsertarTextoInterface {
        void onPossitiveDialogInsertarTexto(String valor);

        void onNegativeDialogInsertarTexto();
    }
}
