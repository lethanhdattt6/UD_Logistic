package com.example.udlogistic;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.example.udlogistic.adapter.Adapter_LvDanhSachNhanVien;
import com.example.udlogistic.database.FireBaseManage;
import com.example.udlogistic.model.KhachHang;
import com.example.udlogistic.model.NhanVien;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class fr_QuanLyNhanVien extends Fragment implements frDialog_ThemNhanVien.OnInputSelected {

    FireBaseManage fireBaseManage;

    static final String TAG = "fr_QuanLyNhanVien";
    SearchView searchView;
    ListView lvDanhSachNhanVien;
    View view;
    TextView txtHoTen;
    Adapter_LvDanhSachNhanVien adapter_lvDanhSachNhanVien;
    ArrayList<NhanVien> nhanViens = new ArrayList<NhanVien>();
    Button btnThemNhanVien;
    public fr_QuanLyNhanVien() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setControl() {
        lvDanhSachNhanVien = view.findViewById(R.id.lvDanhSachNhanVien);
        btnThemNhanVien = view.findViewById(R.id.btnThemNhanVien);
        txtHoTen = view.findViewById(R.id.txtHoTen);
        searchView = view.findViewById(R.id.searchView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fr__quan_ly_nhan_vien, container, false);
        fireBaseManage = new FireBaseManage();
        setControl();
        loadData();
        setEvent();
        return view;
    }

    private void loadData() {

        fireBaseManage.childNhanVien.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nhanViens.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    NhanVien nhanVien = postSnapshot.getValue(NhanVien.class);
                    nhanViens.add(nhanVien);
                }

                adapter_lvDanhSachNhanVien.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setEvent() {
        adapter_lvDanhSachNhanVien = new Adapter_LvDanhSachNhanVien(view.getContext(),R.layout.lvnhanvien_item,nhanViens);
        adapter_lvDanhSachNhanVien.setFragmentManage(getFragmentManager());
        adapter_lvDanhSachNhanVien.setfr_QuanLyNhanVien(fr_QuanLyNhanVien.this);

        lvDanhSachNhanVien.setAdapter(adapter_lvDanhSachNhanVien);
        btnThemNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frDialog_ThemNhanVien frDialog_themNhanVien = new frDialog_ThemNhanVien(null);
                if (getFragmentManager() != null) {
                    frDialog_themNhanVien.setTargetFragment(fr_QuanLyNhanVien.this,1);
                    frDialog_themNhanVien.show(getFragmentManager(),"frDialog_ThemNhanVien");
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter_lvDanhSachNhanVien.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter_lvDanhSachNhanVien.getFilter().filter(newText);
                return true;
            }
        });
    }


    @Override
    public void setInput(NhanVien nhanVien) {
        fireBaseManage.writeNhanVien(nhanVien);
    }

    @Override
    public void setInputUpdate(NhanVien nhanVien) { ;
                fireBaseManage.writeNhanVien(nhanVien);
        }
    }
