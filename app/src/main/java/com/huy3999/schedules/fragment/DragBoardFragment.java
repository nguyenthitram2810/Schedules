package com.huy3999.schedules.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;

import com.huy3999.dragboardview.DragBoardView;
import com.huy3999.dragboardview.model.DragColumn;
import com.huy3999.dragboardview.model.DragItem;
import com.huy3999.schedules.R;
import com.huy3999.schedules.adapter.ColumnAdapter;
import com.huy3999.schedules.apiservice.BaseApiService;
import com.huy3999.schedules.apiservice.UtilsApi;
import com.huy3999.schedules.model.Entry;
import com.huy3999.schedules.model.Item;
import com.huy3999.schedules.model.Project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DragBoardFragment extends Fragment {
    private BaseApiService mApiService;
    private ColumnAdapter mAdapter;
    DragBoardView dragBoardView;
    private Project project;
    private List<DragColumn> mData = new ArrayList<>();
    private static final String ARG_DATA = "data";
    private static final String ARG_PROJECT = "project";
    List<DragItem> todoList;
    List<DragItem> doingList;
    List<DragItem> doneList;
    public DragBoardFragment() {
        // Required empty public constructor
    }

    public static DragBoardFragment newInstance(Project project) {
        DragBoardFragment fragment = new DragBoardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROJECT, (Serializable) project);
        //args.putSerializable(ARG_DATA, (Serializable) data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            project = (Project) getArguments().getSerializable(ARG_PROJECT);
            //mData = (List<DragColumn>) getArguments().getSerializable(ARG_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_drag_board, container, false);
        dragBoardView = view.findViewById(R.id.drag_board);
        mAdapter = new ColumnAdapter(getContext());
        mApiService = UtilsApi.getAPIService();
        todoList = new ArrayList<>();
        Log.d("project","id: "+project.id);
        //getData("Todo");
//        for (int j = 0; j < 5; j++) {
//                todoList.add(new Item(""+j,"name "+j,"sdf","Todo","fsd",null));
//            }
        doingList = new ArrayList<>();
        doneList = new ArrayList<>();
        getData("Done");

        return view;
    }
    public void getData(String state) {
        List<DragItem> items = new ArrayList<>();
        mApiService.getTaskByState(project.id,state)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Item>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Item> itemList) {
                        for (Item item : itemList) {
                            items.add(item);
                            Log.d("item",""+item.name);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        doneList = items;
                        mData.add(new Entry("0","Todo",todoList));
                        mData.add(new Entry("1","Doing",doingList));
                        mData.add(new Entry("2","Done",doneList));
                        Log.d("data",doneList.get(0).toString());
                        mAdapter.setData(mData);
                        dragBoardView.setHorizontalAdapter(mAdapter);
                    }
                });
    }
}