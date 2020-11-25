package com.huy3999.schedules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.item_title.setText(((Item) item).getItemName());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_title;

        public ViewHolder(View itemView) {
            super(itemView);
            item_title = itemView.findViewById(R.id.item_title);
        }
    }
}
