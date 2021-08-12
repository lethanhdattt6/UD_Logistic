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
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.udlogistic.R;
import com.example.udlogistic.database.FireBaseManage;
import com.example.udlogistic.frDialog_ThemKhachHang;
import com.example.udlogistic.frDialog_ThemNhanVien;
import com.example.udlogistic.fr_QuanLyKhachHang;
import com.example.udlogistic.fr_QuanLyNhanVien;
import com.example.udlogistic.model.KhachHang;
import com.example.udlogistic.model.NhanVien;

import java.util.ArrayList;

public class Adapter_LvDanhSachNhanVien extends ArrayAdapter implements Filterable {
    Context context; int resource;
    ArrayList<NhanVien>nhanViens = new ArrayList<>();
    ArrayList<NhanVien>source = new ArrayList<>();
    FragmentManager fragmentManager;
    fr_QuanLyNhanVien fr_quanLyNhanVien;
    FireBaseManage fireBaseManage = new FireBaseManage();
    LinearLayout linearLayout;
    public Adapter_LvDanhSachNhanVien(@NonNull Context context, int resource, @NonNull ArrayList<NhanVien>nhanViens) {
        super(context, resource, nhanViens);
        this.context = context;
        this.resource = resource;
        this.nhanViens = nhanViens;
        this.source = nhanViens;
    }
    public  void setFragmentManage( FragmentManager fragmentManager)
    {
        this.fragmentManager = fragmentManager;
    }
    public  void setfr_QuanLyNhanVien(fr_QuanLyNhanVien fr_quanLyNhanVien)
    {
        this.fr_quanLyNhanVien = fr_quanLyNhanVien;
    }
    @Override
    public int getCount() {
        return nhanViens.size();
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
                    nhanViens = source;
                } else {
                    ArrayList<NhanVien> list = new ArrayList<>();
                    for (NhanVien nhanVien : source) {
                        if (nhanVien.getHoTen().toLowerCase().contains(strSearch.toLowerCase())||
                                nhanVien.getSoDienThoai().toLowerCase().contains(strSearch.toLowerCase())||
                                nhanVien.getDiaChi().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(nhanVien);
                        }
                    }
                    nhanViens = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = nhanViens;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                nhanViens = (ArrayList<NhanVien>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.lvnhanvien_item,null);
        //Nạp layout
        TextView txtSTT = view.findViewById(R.id.txtSTT);
        TextView txtHoTen = view.findViewById(R.id.txtHoTen);
        TextView txtBoPhan = view.findViewById(R.id.txtBoPhan);
        TextView txtChucVu = view.findViewById(R.id.txtChucVu);
        Button btnSua = view.findViewById(R.id.btnSua);

        NhanVien nhanVien = nhanViens.get(position);
        //Nạp dữ liệu
        if (nhanVien == null) {
            return null;
        }
        txtSTT.setText((position+1)+"");
        txtHoTen.setText((nhanVien.getHoTen()));
        txtBoPhan.setText(nhanVien.getBoPhan());
        txtChucVu.setText(nhanVien.getChucVu());
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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frDialog_ThemNhanVien frDialog_themNhanVien = new frDialog_ThemNhanVien(nhanViens.get(position));
                frDialog_themNhanVien.setTargetFragment(fr_quanLyNhanVien,1);
                frDialog_themNhanVien.show(fragmentManager,"frDialog_ThemNhanVien");
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
                frDialog_ThemNhanVien frDialog_themNhanVien = new frDialog_ThemNhanVien(nhanViens.get(position));

                    frDialog_themNhanVien.setTargetFragment(fr_quanLyNhanVien,1);
                    frDialog_themNhanVien.show(fragmentManager,"frDialog_ThemNhanVien");
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
                        fireBaseManage.deleteNhanVien(nhanViens.get(position));
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
