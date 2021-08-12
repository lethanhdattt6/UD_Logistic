package com.example.udlogistic.database;

import android.util.Log;
import android.widget.Toast;

import com.example.udlogistic.model.KhachHang;
import com.example.udlogistic.model.NhanVien;
import com.example.udlogistic.model.PhongBan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseManage {
    public FirebaseDatabase database;
    public DatabaseReference childKhachHang;
    public DatabaseReference childPhongBan;
    public DatabaseReference childNhanVien;
    public FireBaseManage() {
        database = FirebaseDatabase.getInstance("https://ud-logistic-default-rtdb.asia-southeast1.firebasedatabase.app/");
        childKhachHang = database.getReference("KhachHang");
        childPhongBan = database.getReference("PhongBan");
        childNhanVien = database.getReference("NhanVien");
    }
    public DatabaseReference getRootReference()
    {
        return database.getReference();
    }
    public void writeNhanVien(NhanVien nhanVien) {
        childNhanVien.child(nhanVien.getMaNhanVien()).setValue(nhanVien);
    }
    public void deleteNhanVien(NhanVien nhanVien) {
        childNhanVien.child(nhanVien.getMaNhanVien()).removeValue();
    }
    public void writeKhachHang(KhachHang khachHang)
    {
        childKhachHang.child(khachHang.getMaKhachHang()).setValue(khachHang);
    }
    public void deleteKhachHang(KhachHang khachHang)
    {
        childKhachHang.child(khachHang.getMaKhachHang()).removeValue();
    }
    public void delePhongBan(PhongBan phongBan)
    {
        childPhongBan.child(phongBan.getMaPhong()).removeValue();
    }
    public void writePhongBan(PhongBan phongBan)
    {
        childPhongBan.child(phongBan.getMaPhong()).setValue(phongBan);
    }

}
