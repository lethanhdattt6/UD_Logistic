package com.example.udlogistic;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.udlogistic.database.FireBaseManage;
import com.example.udlogistic.model.KhachHang;

import java.util.UUID;


public class frDialog_ThemKhachHang extends DialogFragment {
    FireBaseManage fireBaseManage;
    KhachHang khachHang;
    public  interface OnInputSelected{
        void setInputUpdate(KhachHang khachHang);
        void setInput(KhachHang khachHang);

    }
    public  OnInputSelected onInputSelected;
    View view;
    static final String TAG = "frDialog_ThemKhachHang";
    EditText edtTenKhachHang,edtDiaChi,edtSoDienThoai;
    Button btnClose,btnSave;

    public frDialog_ThemKhachHang(KhachHang khachHang) {
        // Required empty public constructor
      this. khachHang = khachHang;
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
        view =  inflater.inflate(R.layout.fragment_fr_dialog__them_khach_hang, container, false);
        edtTenKhachHang = view.findViewById(R.id.edtTenKhachHang);
        edtDiaChi = view.findViewById(R.id.edtDiaChi);
        edtSoDienThoai = view.findViewById(R.id.edtSoDienThoai);
        btnClose = view.findViewById(R.id.btnClose);
        btnSave = view.findViewById(R.id.btnSave);
        //Setevent
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        if (khachHang!= null)
        {
            edtTenKhachHang.setText(khachHang.getHoTen());
            edtDiaChi.setText(khachHang.getDiaChi());
            edtSoDienThoai.setText(khachHang.getSoDienThoai());
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Check())
                {
                    if (khachHang==null)
                    {
                        String ma = UUID.randomUUID().toString();;
                        String ten = edtTenKhachHang.getText().toString();
                        String diachi = edtDiaChi.getText().toString();
                        String sdt = edtSoDienThoai.getText().toString();
                        onInputSelected.setInput(new KhachHang(ma,ten,diachi,sdt));
                        Toast.makeText(view.getContext(),"Đã thêm khách hàng thành công" , Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();

                    }else
                    {
                        String ma = khachHang.getMaKhachHang();
                        String ten = edtTenKhachHang.getText().toString();
                        String diachi = edtDiaChi.getText().toString();
                        String sdt = edtSoDienThoai.getText().toString();
                        onInputSelected.setInputUpdate(new KhachHang(ma,ten,diachi,sdt));
                        Toast.makeText(view.getContext(),"Đã sửa thông tin khách hàng thành công" , Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();
                    }
                }
            }
        });
        return view;
    }

    private boolean Check() {
        boolean res = true;
        if(edtTenKhachHang.getText().toString().isEmpty())
        {
            res = false;
            edtTenKhachHang.setError("Vui lòng nhập tên khách hàng");
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
        return  res;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onInputSelected = (OnInputSelected)getTargetFragment();
    }
}