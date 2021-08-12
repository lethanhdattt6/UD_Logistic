package com.example.udlogistic;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.udlogistic.database.FireBaseManage;
import com.example.udlogistic.model.KhachHang;
import com.example.udlogistic.model.PhongBan;

import java.util.UUID;


public class frDialog_ThemPhongBan extends DialogFragment {
    FireBaseManage fireBaseManage;
    PhongBan phongBan;
    public  interface OnInputSelected{
        void setInputUpdate(PhongBan phongBan);
        void setInput(PhongBan phongBan);

    }
    public  OnInputSelected onInputSelected;
    View view;
    static final String TAG = "frDialog_ThemPhongBan";
    EditText edtTenPhongBan;
    Button btnClose,btnSave;

    public frDialog_ThemPhongBan(PhongBan phongBan) {
        // Required empty public constructor
      this. phongBan = phongBan;
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
        view =  inflater.inflate(R.layout.fragment_fr_dialog__them_phong_ban, container, false);
        edtTenPhongBan = view.findViewById(R.id.edtTenPhongBan);
        btnClose = view.findViewById(R.id.btnClose);
        btnSave = view.findViewById(R.id.btnSave);
        //Setevent
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        if (phongBan!= null)
        {
            edtTenPhongBan.setText(phongBan.getTenPhong());
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Check())
                {
                    if (phongBan==null)
                    {
                        String ma = UUID.randomUUID().toString();;
                        String ten = edtTenPhongBan.getText().toString();;
                        onInputSelected.setInput(new PhongBan(ma,ten));
                        Toast.makeText(view.getContext(),"Đã thêm Phòng Ban thành công" , Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();
                    }else
                    {
                        String ma = phongBan.getMaPhong();
                        String ten = edtTenPhongBan.getText().toString();
                        onInputSelected.setInputUpdate(new PhongBan(ma,ten));
                        Toast.makeText(view.getContext(),"Đã sửa thông tin phòng ban thành công" , Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();
                    }
                }
            }
        });
        return view;
    }

    private boolean Check() {
        boolean res = true;
        if(edtTenPhongBan.getText().toString().isEmpty())
        {
            res = false;
            edtTenPhongBan.setError("Vui lòng nhập tên phòng ban");
        }
        return  res;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onInputSelected = (OnInputSelected)getTargetFragment();
    }
}