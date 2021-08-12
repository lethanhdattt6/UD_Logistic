package com.example.udlogistic.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.udlogistic.R;
import com.example.udlogistic.database.FireBaseManage;
import com.example.udlogistic.frDialog_ThemKhachHang;
import com.example.udlogistic.frDialog_ThemPhongBan;
import com.example.udlogistic.fr_QuanLyKhachHang;
import com.example.udlogistic.fr_QuanLyPhongBan;
import com.example.udlogistic.model.KhachHang;
import com.example.udlogistic.model.PhongBan;

import java.util.ArrayList;

public class Adapter_LvDanhSachPhongBan  extends ArrayAdapter implements Filterable {
    Context context; int resource;
    ArrayList<PhongBan> phongBans = new ArrayList<>();
    ArrayList<PhongBan>source = new ArrayList<>();
    FragmentManager fragmentManager;
    fr_QuanLyPhongBan fr_quanLyPhongBan;
    FireBaseManage fireBaseManage = new FireBaseManage();
    public Adapter_LvDanhSachPhongBan(@NonNull Context context, int resource, @NonNull ArrayList<PhongBan> phongBans) {
        super(context, resource, phongBans);
        this.context = context;
        this.resource = resource;
        this.phongBans = phongBans;
        this.source = phongBans;
    }
    public  void setFragmentManage( FragmentManager fragmentManager)
    {
        this.fragmentManager = fragmentManager;
    }
    public  void setfr_QuanLyKhachHang( fr_QuanLyPhongBan fr_quanLyPhongBan)
    {
        this.fr_quanLyPhongBan = fr_quanLyPhongBan;
    }
    @Override
    public int getCount() {
        return phongBans.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    phongBans = source;
                } else {
                    ArrayList<PhongBan> list = new ArrayList<>();
                    for (PhongBan phongBan : source) {
                        if (phongBan.getTenPhong().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(phongBan);
                        }
                    }
                    phongBans = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = phongBans;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                phongBans = (ArrayList<PhongBan>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.lvphongban_item,null);
        //Nạp layout
        TextView txtSTT = view.findViewById(R.id.txtSTT);
        TextView txtHoTen = view.findViewById(R.id.txtTenPhong);
        Button btnSua = view.findViewById(R.id.btnSua);

        PhongBan phongBan = phongBans.get(position);
        //Nạp dữ liệu
        if (phongBan == null) {
            return null;
        }
        txtSTT.setText((position+1)+"");
        txtHoTen.setText((phongBan.getTenPhong()));
        //Sự kiện
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Khởi tạo popup menu
                PopupMenu popupMenu = new PopupMenu(v.getContext(), btnSua);
                popupMenu.inflate(R.menu.lv_edit_button);
                popupMenu.show();
                //Set Sự kiện khi nhấn vào 1 item trong pop up menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return menuItemClicked(item,position);
                    }
                });
            }
        });
        return view;

    }
    private boolean menuItemClicked(MenuItem item, int position) {
        FragmentActivity activity = (FragmentActivity)(context);
        FragmentManager fm = activity.getSupportFragmentManager();
        android.app.FragmentManager fm2 = activity.getFragmentManager();

        switch (item.getItemId()) {
            case R.id.menuItem_Edit:
                frDialog_ThemPhongBan frDialog_themPhongBan = new frDialog_ThemPhongBan(phongBans.get(position));

                frDialog_themPhongBan.setTargetFragment(fr_quanLyPhongBan,1);
                frDialog_themPhongBan.show(fragmentManager,"frDialog_ThemPhongBan");
                break;
            case R.id.menuItem_Delete:
                //Tạo message box ? Bạn có muốn xóa
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");
                //set Text và sự kiện cho nút accept
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        fireBaseManage.delePhongBan(phongBans.get(position));
                        dialog.dismiss();
                    }
                });
                //set Text và sự kiện cho nút reject
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                //Hiển thị dialog
                alert.show();
                break;
        }
        return true;
    }
}
