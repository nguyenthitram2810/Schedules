package com.huy3999.schedules.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.huy3999.schedules.adapter.ProjectAdapter;
import com.huy3999.schedules.dragboardview.DragBoardView;
import com.huy3999.schedules.dragboardview.model.DragColumn;
import com.huy3999.schedules.dragboardview.model.DragItem;
import com.huy3999.schedules.R;
import com.huy3999.schedules.adapter.ColumnAdapter;
import com.huy3999.schedules.apiservice.BaseApiService;
import com.huy3999.schedules.apiservice.UtilsApi;
import com.huy3999.schedules.model.Entry;
import com.huy3999.schedules.model.Item;
import com.huy3999.schedules.model.Project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragBoardFragment extends Fragment {
    private static final String TODO = "Todo";
    private static final String DOING = "Doing";
    private static final String DONE = "Done";
    private BaseApiService mApiService;
    private ColumnAdapter mAdapter;
    DragBoardView dragBoardView;
    private Project project;
    private List<DragColumn> mData = new ArrayList<>();
    private static final String ARG_PROJECT = "project";
    List<DragItem> todoList;
    List<DragItem> doingList;
    List<DragItem> doneList;
    private String email;
    private FirebaseAuth auth;
    public DragBoardFragment() {
        // Required empty public constructor
    }

    public static DragBoardFragment newInstance(Project project) {
        DragBoardFragment fragment = new DragBoardFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROJECT,project);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            project = (Project) getArguments().getParcelable(ARG_PROJECT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_drag_board, container, false);
        dragBoardView = view.findViewById(R.id.drag_board);
        mApiService = UtilsApi.getAPIService();
        auth = FirebaseAuth.getInstance();
        email = auth.getCurrentUser().getEmail();
        mAdapter = new ColumnAdapter(getContext(),mApiService,project);
        todoList = new ArrayList<>();
        getData(TODO);
        doingList = new ArrayList<>();
        doneList = new ArrayList<>();
//        mData.add(new Entry("0","Todo",todoList));
//        mData.add(new Entry("1","Doing",doingList));
//        mData.add(new Entry("2","Done",doneList));
        mAdapter.setData(mData);
        getActivity().setTitle(project.name);
        dragBoardView.setHorizontalAdapter(mAdapter);
        //getAllTaskWithRealtimeUpdates();
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
                        if(state==TODO){
                            todoList = items;
                            mData.add(new Entry("0","Todo",todoList));
                            mAdapter.notifyDataSetChanged();
                            getData(DOING);
                        }
                        if(state == DOING){
                            doingList = items;
                            mData.add(new Entry("2","Doing",doingList));
                            mAdapter.notifyDataSetChanged();
                            getData(DONE);
                        }
                        if(state == DONE){
                            doneList = items;
                            mData.add(new Entry("3","Done",doneList));
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                });
    }
//    public void getAllTaskWithRealtimeUpdates() {
//        FirebaseFirestore.getInstance()
//                .collection("tasks")
//                .whereArrayContains("member", email)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if(error != null ) {
//                            Log.d("REALTIME", "Listen failed");
//                        }
//                        if(value != null) {
//                            for (QueryDocumentSnapshot doc : value) {
//                                Log.d("REALTIME", doc.getId());
//                                Item item = new Item(doc.getId() , doc.get("name").toString(),
//                                        doc.get("description").toString(),doc.get("state").toString(),
//                                        doc.get("project_id").toString(),
//                                        (ArrayList<String>) doc.get("member"));
//
//                                if (item.state == TODO){
//                                    mData.remove(0);
//                                    todoList.add(item);
//                                    Log.d("item","todo : "+todoList.get(0));
//                                    mData.add(new Entry("0","Todo",todoList));
//                                    mAdapter.notifyDataSetChanged();
//                                }else if(item.state == DOING){
//                                    mData.remove(1);
//                                    doingList.add(item);
//                                    Log.d("item","doing : "+doingList.get(0));
//                                    mData.add(new Entry("1","Doing",doingList));
//                                    mAdapter.notifyDataSetChanged();
//                                }else if(item.state == DONE){
//                                    mData.remove(2);
//                                    doneList.add(item);
//                                    Log.d("item","done : "+doneList.get(0));
//                                    mData.add(new Entry("2","Done",doneList));
//                                    mAdapter.notifyDataSetChanged();
//                                }
//                            }
////                            mAdapter = new ColumnAdapter(getContext(),mApiService,project);
////                            mAdapter.setData(mData);
////                            dragBoardView.setHorizontalAdapter(mAdapter);
//                        }
//                    }
//                });
//    }
}