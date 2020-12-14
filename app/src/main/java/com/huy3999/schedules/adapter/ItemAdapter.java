package com.huy3999.schedules.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.huy3999.dragboardview.adapter.VerticalAdapter;
import com.huy3999.dragboardview.helper.DragHelper;
import com.huy3999.dragboardview.model.DragItem;
import com.huy3999.schedules.R;
import com.huy3999.schedules.model.Item;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends VerticalAdapter<ItemAdapter.ViewHolder> {

    public ItemAdapter(Context context, DragHelper dragHelper) {
        super(context, dragHelper);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_item, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(Context context, final ViewHolder holder, @NonNull DragItem item, final int position) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dragItem(holder);
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(item,position);
            }
        });
        holder.item_title.setText(((Item) item).name);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_title;

        public ViewHolder(View itemView) {
            super(itemView);
            item_title = itemView.findViewById(R.id.item_title);
        }
    }
    private void openDialog(DragItem item,final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_item, null);
        //View view = getLayoutInflater().inflate(R.layout.test, null);
        EditText edtItemName = view.findViewById(R.id.edtItemName);
        EditText edtItemDes = view.findViewById(R.id.edtItemDes);
        final AlertDialog alertDialog = builder.create();
        edtItemName.setText(((Item) item).name);
        edtItemDes.setText(((Item) item).description);
        builder.setMessage("Edit item")
                .setView(view)
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!edtItemName.getText().toString().trim().equals("") && !edtItemDes.getText().toString().trim().equals("")){
                            //((Item) item).name = edtItemName.getText().toString().trim();
                            //((Item) item).setInfo(edtItemDes.getText().toString().trim());
                            //notifyDataSetChanged();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
        builder.show();
    }
}
