package com.huy3999.schedules;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.huy3999.dragboardview.DragBoardView;
import com.huy3999.dragboardview.model.DragColumn;
import com.huy3999.dragboardview.model.DragItem;
import com.huy3999.dragboardview.utils.AttrAboutPhone;
import com.huy3999.schedules.adapter.ColumnAdapter;
import com.huy3999.schedules.model.Entry;
import com.huy3999.schedules.model.Item;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ColumnAdapter mAdapter;
    DragBoardView dragBoardView;
    private List<DragColumn> mData = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dragBoardView = findViewById(R.id.layout_main);
        mAdapter = new ColumnAdapter(this);
        mAdapter.setData(mData);
        dragBoardView.setHorizontalAdapter(mAdapter);

        getDataAndRefreshView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        AttrAboutPhone.saveAttr(this);
        AttrAboutPhone.initScreen(this);
        super.onWindowFocusChanged(hasFocus);
    }

    private void getDataAndRefreshView() {
        for (int i = 0; i < 3; i++) {
            List<DragItem> itemList = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                itemList.add(new Item("entry " + i + " item id " + j, "item name " + j, "info " + j));
            }
            //mData.add(new Entry("entry id " + i, "name " + i, itemList));
            mData.add(new Entry("entry 0","Todo",itemList));
            mData.add(new Entry("entry 1","Doing",itemList));
            mData.add(new Entry("entry 2","Done",itemList));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}