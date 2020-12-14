package com.huy3999.schedules.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huy3999.dragboardview.DragBoardView;
import com.huy3999.dragboardview.model.DragColumn;
import com.huy3999.schedules.R;
import com.huy3999.schedules.adapter.ColumnAdapter;
import com.huy3999.schedules.model.Project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DragBoardFragment extends Fragment {

    private ColumnAdapter mAdapter;
    DragBoardView dragBoardView;
    private Project project;
    private List<DragColumn> mData = new ArrayList<>();
    private static final String ARG_DATA = "data";
    private static final String ARG_PROJECT = "project";

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
        mAdapter.setData(mData);
        dragBoardView.setHorizontalAdapter(mAdapter);
        return view;
    }
}