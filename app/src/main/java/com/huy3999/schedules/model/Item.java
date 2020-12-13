package com.huy3999.schedules.model;

import com.huy3999.dragboardview.model.DragItem;

public class Item implements DragItem {
    private String itemId;
    private String itemName;

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    private String info;

    public Item(String itemId, String itemName, String info) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.info = info;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getInfo() {
        return info;
    }
    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    @Override
    public int getColumnIndex() {
        return 0;
    }

    @Override
    public int getItemIndex() {
        return 0;
    }

    @Override
    public void setColumnIndex(int columnIndexInHorizontalRecycleView) {

    }

    @Override
    public void setItemIndex(int itemIndexInVerticalRecycleView) {

    }
}


