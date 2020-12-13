//package com.huy3999.schedules.fragment;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.huy3999.schedules.R;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//
//public class AddItemDialog extends DialogFragment {
//    NoticeDialogListener listener;
//    EditText edtItemName, edtItemDes;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.dialog_add_item, container);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        edtItemName = view.findViewById(R.id.edtItemName);
//        edtItemDes = view.findViewById(R.id.edtItemDes);
//        Button btnClose = (Button) view.findViewById(R.id.btn_close);
//        Button btnUpdate = (Button) view.findViewById(R.id.btn_update);
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Update clicked!", Toast.LENGTH_SHORT).show();
//                if (!edtItemName.getText().toString().trim().equals("") && !edtItemDes.getText().toString().trim().equals("")) {
//                    String itemName = edtItemName.getText().toString().trim();
//                    String itemDes = edtItemDes.getText().toString().trim();
//                    listener.onDialogPositiveClick(itemName, itemDes);
//                }
//            }
//        });
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getDialog().dismiss();
//            }
//        });
//    }
//
//
//    //    @NonNull
////    @Override
////    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
////        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////        View view = getLayoutInflater().inflate(R.layout.dialog_add_item, null);
////        //View view = getLayoutInflater().inflate(R.layout.test, null);
//////        edtItemName = view.findViewById(R.id.edtItemName);
//////        edtItemDes = view.findViewById(R.id.edtItemDes);
////        builder.setMessage("Add item")
////                .setView(view)
////                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
//////                        if(!edtItemName.getText().toString().trim().equals("") && !edtItemDes.getText().toString().trim().equals("")){
//////                            String itemName = edtItemName.getText().toString().trim();
//////                            String itemDes = edtItemDes.getText().toString().trim();
//////                            listener.onDialogPositiveClick(itemName,itemDes);
//////                        }
////
////                    }
////                })
////                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        //listener.onDialogNegativeClick();
////                    }
////                });
////        return builder.create();
//////        AlertDialog dialog = builder.create();
//////        dialog.show();
//////        return dialog;
////    }
//    public interface NoticeDialogListener {
//        public void onDialogPositiveClick(String itemName, String itemDes);
//
//        public void onDialogNegativeClick();
//    }
//
//    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            listener = (NoticeDialogListener) context;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(" must implement NoticeDialogListener");
//        }
//    }
//}