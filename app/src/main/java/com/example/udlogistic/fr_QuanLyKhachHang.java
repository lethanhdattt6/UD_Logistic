package com.example.udlogistic;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.example.udlogistic.adapter.Adapter_LvDanhSachKhachHang;
import com.example.udlogistic.database.FireBaseManage;
import com.example.udlogistic.model.KhachHang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class fr_QuanLyKhachHang extends Fragment implements frDialog_ThemKhachHang.OnInputSelected {

    FireBaseManage fireBaseManage;
    static final String TAG = "fr_QuanLyKhachHang";
    SearchView searchView;
    ListView lvDanhSachKhachHang;
    View view;
    TextView txtHoTen;
    Adapter_LvDanhSachKhachHang adapter_lvDanhSachKhachHang;
    ArrayList<KhachHang>khachHangs = new ArrayList<KhachHang>();
    Button btnThemKhachHang;
    public fr_QuanLyKhachHang() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setControl() {
        lvDanhSachKhachHang = view.findViewById(R.id.lvDanhSachKhachHang);
        btnThemKhachHang = view.findViewById(R.id.btnThemKhachHang);
        txtHoTen = view.findViewById(R.id.txtHoTen);
        searchView = view.findViewById(R.id.searchView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fr__quan_ly_khach_hang, container, false);
        fireBaseManage = new FireBaseManage();
        setControl();
        loadData();
        setEvent();
        return view;
    }

    private void loadData() {

       fireBaseManage.childKhachHang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                khachHangs.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    KhachHang khachHang = postSnapshot.getValue(KhachHang.class);
                    khachHangs.add(khachHang);
                }
                adapter_lvDanhSachKhachHang.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setEvent() {
        adapter_lvDanhSachKhachHang = new Adapter_LvDanhSachKhachHang(view.getContext(),R.layout.lvkhachhang_item,khachHangs);
           adapter_lvDanhSachKhachHang.setFragmentManage(getFragmentManager());
           adapter_lvDanhSachKhachHang.setfr_QuanLyKhachHang(fr_QuanLyKhachHang.this);

        lvDanhSachKhachHang.setAdapter(adapter_lvDanhSachKhachHang);
        btnThemKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frDialog_ThemKhachHang frDialog_themKhachHang = new frDialog_ThemKhachHang(null);
                if (getFragmentManager() != null) {
                    frDialog_themKhachHang.setTargetFragment(fr_QuanLyKhachHang.this,1);
                    frDialog_themKhachHang.show(getFragmentManager(),"frDialog_ThemKhachHang");
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter_lvDanhSachKhachHang.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter_lvDanhSachKhachHang.getFilter().filter(newText);
                return true;
            }
        });
    }


    @Override
    public void setInput(KhachHang khachHang) {
        fireBaseManage.writeKhachHang(khachHang);
    }

    @Override
    public void setInputUpdate(KhachHang khachHang) { ;
                fireBaseManage.writeKhachHang(khachHang);
        }
    }



