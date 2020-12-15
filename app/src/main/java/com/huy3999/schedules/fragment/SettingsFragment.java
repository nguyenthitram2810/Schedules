package com.huy3999.schedules.fragment;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huy3999.schedules.MainActivity;
import com.huy3999.schedules.R;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

public class SettingsFragment extends Fragment {
    private static View root;
    private LinearLayout btnAccount, btnColorTheme, btnInfo, btnSupport;
    private String colorChoosed;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_settings_fragment, container, false);
        btnAccount = root.findViewById(R.id.account_settings);
        btnColorTheme = root.findViewById(R.id.color_theme_settings);
        btnInfo = root.findViewById(R.id.infomation_settings);
        btnSupport = root.findViewById(R.id.support_settings);

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(container.getId(), new AccountFragment()).commit();
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog al = settingsDialog("Infomation","Schedule Application - version3 - Tr.CongDat,Ng.ThiTram,Ph.ThanhHuy,Ng.TienVan");
                al.show();
            }
        });

        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog al = settingsDialog("Support","If you need some helps, please call: 1900 6088 (Chi ong vang)");
                al.show();
            }
        });
        return root;
    }

    private AlertDialog settingsDialog(String title, String s) {
        //Dialog
        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
        //Thiết lập tiêu đề
        b.setTitle(title);
        b.setMessage(s);
        // Nút Ok
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        return b.create();
    }


    public void onChooseColor(View view) {
        openColorPicker();
    }

    public void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(getActivity());
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#ffffff");
        colors.add("#3C8D2F");
        colors.add("#20724f");
        colors.add("#6a3ab2");
        colors.add("#323299");
        colors.add("#800080");
        colors.add("#b79716");
        colors.add("#966d37");
        colors.add("#b77231");
        colors.add("#808000");

        colorPicker.setColors(colors)
                .setColumns(5)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        colorChoosed = colors.get(position);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
    }
}