package com.example.udlogistic;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.udlogistic.R;
import com.example.udlogistic.database.FireBaseManage;
import com.example.udlogistic.model.NhanVien;
import com.example.udlogistic.model.PhongBan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;


public class frDialog_ThemNhanVien extends DialogFragment {
    FireBaseManage fireBaseManage;
    NhanVien nhanVien;
    ArrayList<PhongBan> phongBans = new ArrayList<>();
    public  interface OnInputSelected{
        void setInputUpdate(NhanVien nhanVien);
        void setInput(NhanVien nhanVien);

    }
    public  OnInputSelected onInputSelected;
    View view;
    static final String TAG = "frDialog_ThemNhanVien";
    Button btnClose,btnSave;
    EditText edtTenNhanVien,edtDiaChi,edtSoDienThoai,edtChucVu;
    Spinner spPhongBan;
    public frDialog_ThemNhanVien(NhanVien nhanVien) {
        // Required empty public constructor
      this. nhanVien = nhanVien;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment frDialog_ThemKhachHang.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fireBaseManage = new FireBaseManage();
        view =  inflater.inflate(R.layout.fragment_fr_dialog__them_nhan_vien, container, false);
        edtTenNhanVien = view.findViewById(R.id.edtTenNhanVien);
        edtDiaChi = view.findViewById(R.id.edtDiaChi);
        edtSoDienThoai = view.findViewById(R.id.edtSoDienThoai);
        edtChucVu = view.findViewById(R.id.edtChucVu);
        spPhongBan = view.findViewById(R.id.spPhongBan);
        btnClose = view.findViewById(R.id.btnClose);
        btnSave = view.findViewById(R.id.btnSave);
        loadDataPhongBan();
        //Setevent
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        if (nhanVien!= null)
        {
            edtTenNhanVien.setText(nhanVien.getHoTen());
            edtChucVu.setText(nhanVien.getChucVu());
            edtDiaChi.setText(nhanVien.getDiaChi());
            edtSoDienThoai.setText(nhanVien.getSoDienThoai());
            checkedSpinnerPhongban(nhanVien.getBoPhan());
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Check())
                {
                    if (nhanVien==null)
                    {
                        String ma = UUID.randomUUID().toString();
                        String ten = edtTenNhanVien.getText().toString();
                        String diachi = edtDiaChi.getText().toString();
                        String sdt = edtSoDienThoai.getText().toString();
                        String chucvu = edtChucVu.getText().toString();
                        String bophan = spPhongBan.getSelectedItem().toString();
                        onInputSelected.setInput(new NhanVien(ma,ten,diachi,sdt,bophan,chucvu));
                        Toast.makeText(view.getContext(),"Đã thêm  nhân viên thành công" , Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();
                    }else
                    {
                        String ma = nhanVien.getMaNhanVien();
                        String ten = edtTenNhanVien.getText().toString();
                        String diachi = edtDiaChi.getText().toString();
                        String sdt = edtSoDienThoai.getText().toString();
                        String chucvu = edtChucVu.getText().toString();
                        String bophan = spPhongBan.getSelectedItem().toString();
                        onInputSelected.setInputUpdate(new NhanVien(ma,ten,diachi,sdt,bophan,chucvu));
                        Toast.makeText(view.getContext(),"Đã sửa nhân viên thành công" , Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();
                    }
                }
            }
        });
        return view;
    }
    private void loadDataPhongBan( ) {
        fireBaseManage.childPhongBan.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                phongBans.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    PhongBan phongBan = postSnapshot.getValue(PhongBan.class);
                        phongBans.add(phongBan);
                    }
                ArrayList<String>spinnerArray = new ArrayList<>();
                phongBans.forEach(phongBan -> {
                    spinnerArray.add(phongBan.getTenPhong());
                });
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                        (getContext(), android.R.layout.simple_spinner_item,
                                spinnerArray); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                spPhongBan.setAdapter(spinnerArrayAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void checkedSpinnerPhongban(String s ) {
        fireBaseManage.childPhongBan.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<PhongBan>phongBans = new ArrayList<>();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    PhongBan phongBan = postSnapshot.getValue(PhongBan.class);
                    phongBans.add(phongBan);
                }
                ArrayList<String>spinnerArray = new ArrayList<>();
                phongBans.forEach(o -> {
                if (o.getTenPhong().equals(s))
                {
                    spPhongBan.setSelection(phongBans.indexOf(o));
                }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private boolean Check() {
        boolean res = true;
        if(edtTenNhanVien.getText().toString().isEmpty())
        {
            res = false;
            edtTenNhanVien.setError("Vui lòng nhập tên nhân viên");
        }
        if(edtDiaChi.getText().toString().isEmpty())
        {
            res = false;
            edtDiaChi.setError("Vui lòng nhập địa chỉ");
        }
        if(edtSoDienThoai.getText().toString().isEmpty())
        {
            res = false;
            edtSoDienThoai.setError("Vui lòng nhập số điên thoại");
        }
        if(edtChucVu.getText().toString().isEmpty())
        {
            res = false;
            edtChucVu.setError("Vui lòng nhập chức vụ");
        }
        return  res;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onInputSelected = (OnInputSelected)getTargetFragment();
    }
}