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
import com.example.udlogistic.adapter.Adapter_LvDanhSachPhongBan;
import com.example.udlogistic.database.FireBaseManage;
import com.example.udlogistic.model.KhachHang;
import com.example.udlogistic.model.PhongBan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class fr_QuanLyPhongBan extends Fragment implements frDialog_ThemPhongBan.OnInputSelected {
    KAlertDialog pDialog ;
    FireBaseManage fireBaseManage;
    static final String TAG = "fr_QuanLyPhongBan";
    SearchView searchView;
    ListView lvDanhSachKhachHang;
    View view;
    TextView txtHoTen;
    Adapter_LvDanhSachPhongBan adapter_lvDanhSachPhongBan;
    ArrayList<PhongBan> phongBans = new ArrayList<PhongBan>();
    Button btnThemPhongBan;
    public fr_QuanLyPhongBan() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setControl() {
        lvDanhSachKhachHang = view.findViewById(R.id.lvDanhSachPhongBan);
        btnThemPhongBan = view.findViewById(R.id.btnThemPhongBan);
        txtHoTen = view.findViewById(R.id.txtHoTen);
        searchView = view.findViewById(R.id.searchView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fr__quan_ly_phong_ban, container, false);
        fireBaseManage = new FireBaseManage();
        setControl();
        loadData();
        setEvent();
        return view;
    }

    private void loadData() {
        pDialog  = new KAlertDialog(view.getContext(), KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        fireBaseManage.childPhongBan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                phongBans.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    PhongBan phongBan = postSnapshot.getValue(PhongBan.class);
                    phongBans.add(phongBan);
                }
                adapter_lvDanhSachPhongBan.notifyDataSetChanged();
                pDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setEvent() {
        adapter_lvDanhSachPhongBan = new Adapter_LvDanhSachPhongBan(view.getContext(),R.layout.lvkhachhang_item,phongBans);
        adapter_lvDanhSachPhongBan.setFragmentManage(getFragmentManager());
        adapter_lvDanhSachPhongBan.setfr_QuanLyKhachHang(fr_QuanLyPhongBan.this);

        lvDanhSachKhachHang.setAdapter(adapter_lvDanhSachPhongBan);
        btnThemPhongBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frDialog_ThemPhongBan frDialogThemPhongBan = new frDialog_ThemPhongBan(null);
                if (getFragmentManager() != null) {
                    frDialogThemPhongBan.setTargetFragment(fr_QuanLyPhongBan.this,1);
                    frDialogThemPhongBan.show(getFragmentManager(),"frDialog_ThemPhongBan");
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter_lvDanhSachPhongBan.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter_lvDanhSachPhongBan.getFilter().filter(newText);
                return true;
            }
        });
    }


    @Override
    public void setInput(PhongBan phongBan) {
        fireBaseManage.writePhongBan(phongBan);
    }

    @Override
    public void setInputUpdate(PhongBan phongBan) { ;
        fireBaseManage.writePhongBan(phongBan);
    }

}