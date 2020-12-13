package com.huy3999.schedules.adapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huy3999.dragboardview.adapter.HorizontalAdapter;
import com.huy3999.dragboardview.model.DragColumn;
import com.huy3999.dragboardview.model.DragItem;
import com.huy3999.schedules.MainActivity;
import com.huy3999.schedules.R;
import com.huy3999.schedules.model.Entry;
import com.huy3999.schedules.model.Item;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ColumnAdapter extends HorizontalAdapter<ColumnAdapter.ViewHolder>  {
    public ColumnAdapter(Context context) {
        super(context);
    }

    @Override
    public boolean needFooter() {
        return true;
    }

    @Override
    public int getContentLayoutRes() {
        return R.layout.recyclerview_item_entry;
    }

    @Override
    public int getFooterLayoutRes() {
        return R.layout.recyclerview_footer_addlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(View parent, int viewType) {
        return new ViewHolder(parent, viewType);
    }

    @Override
    public void onBindContentViewHolder(final ViewHolder holder, DragColumn dragColumn, int position) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dragCol(holder);
                return true;
            }
        });

        final Entry entry = (Entry) dragColumn;
        holder.tv_title.setText(entry.getName());
        List<DragItem> itemList = entry.getItemList();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        holder.rv_item.setLayoutManager(layoutManager);
        ItemAdapter itemAdapter = new ItemAdapter(mContext, dragHelper);
        itemAdapter.setData(itemList);
        holder.rv_item.setAdapter(itemAdapter);
        holder.add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(itemList,itemAdapter,position);
            }
        });
    }

    @Override
    public void onBindFooterViewHolder(final ViewHolder holder, int position) {
        holder.add_subPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.add_subPlan.setVisibility(View.GONE);
                holder.edit_sub_plan.setVisibility(View.VISIBLE);
            }
        });
        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.add_subPlan.setVisibility(View.VISIBLE);
                holder.edit_sub_plan.setVisibility(View.GONE);
            }
        });
        holder.btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = holder.editText.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                    appendNewColumn(new Entry(
                            "entry id " + name,
                            "name : new entry",
                            new ArrayList<DragItem>()));
                }
            }
        });
    }


    public class ViewHolder extends HorizontalAdapter.ViewHolder {

        RelativeLayout col_content_container;
        ImageView title_icon;
        TextView tv_title;
        RecyclerView rv_item;
        RelativeLayout add_task;

        RelativeLayout add_subPlan;
        RelativeLayout edit_sub_plan;
        Button btn_cancel;
        Button btn_ok;
        EditText editText;

        public ViewHolder(View convertView, int itemType) {
            super(convertView, itemType);
        }

        @Override
        public RecyclerView getRecyclerView() {
            return rv_item;
        }

        @Override
        public void findViewForContent(View convertView) {
            col_content_container = convertView.findViewById(R.id.col_content_container);
            title_icon = convertView.findViewById(R.id.title_icon);
            tv_title = convertView.findViewById(R.id.tv_title);
            rv_item = convertView.findViewById(R.id.rv);
            add_task = convertView.findViewById(R.id.add);
        }

        @Override
        public void findViewForFooter(View convertView) {
            add_subPlan = convertView.findViewById(R.id.add_sub_plan);
            edit_sub_plan = convertView.findViewById(R.id.edit_sub_plan);
            btn_cancel = convertView.findViewById(R.id.add_cancel);
            btn_ok = convertView.findViewById(R.id.add_ok);
            editText = convertView.findViewById(R.id.add_et);
        }
    }
    private void openDialog(List<DragItem> itemList, ItemAdapter itemAdapter,final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_item, null);
        //View view = getLayoutInflater().inflate(R.layout.test, null);
        EditText edtItemName = view.findViewById(R.id.edtItemName);
        EditText edtItemDes = view.findViewById(R.id.edtItemDes);
        final AlertDialog alertDialog = builder.create();
        builder.setMessage("Add item")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!edtItemName.getText().toString().trim().equals("") && !edtItemDes.getText().toString().trim().equals("")){
                            String itemName = edtItemName.getText().toString().trim();
                            String itemDes = edtItemDes.getText().toString().trim();
//                            itemList.add(new Item(
//                                        "entry " + " item id ",
//                                        "item name : "+itemName,
//                                        "info : "+itemDes));
                            itemList.add(new Item(
                                    "entry " + " item id ",
                                    itemName,
                                    itemDes));
                                itemAdapter.notifyItemInserted(itemAdapter.getItemCount() - 1);

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


