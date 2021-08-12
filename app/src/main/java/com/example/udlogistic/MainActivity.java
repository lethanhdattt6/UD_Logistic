package com.example.udlogistic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.developer.kalert.KAlertDialog;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.view.GravityCompat.*;

public class MainActivity extends AppCompatActivity {
    //Global Declaration
    SNavigationDrawer sNavigationDrawer;
    Class fragmentClass;
    public static Fragment fragment;
    KAlertDialog pDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        initControl();
    }

    private void initControl() {
        //Inside onCreate()
        sNavigationDrawer = findViewById(R.id.navigationDrawer);
        //Creating a list of menu Items

        List<MenuItem> menuItems = new ArrayList<>();

        //Use the MenuItem given by this library and not the default one.
        //First parameter is the title of the menu item and then the second parameter is the image which will be the background of the menu item.

        menuItems.add(new MenuItem("Quản lý khách hàng", R.drawable.news_bg));
        menuItems.add(new MenuItem("Quản lý nhân viên", R.drawable.feed_bg));
        menuItems.add(new MenuItem("Quản lý phòng ban", R.drawable.message_bg));

        //then add them to navigation drawer
        sNavigationDrawer.setMenuItemList(menuItems);
        fragmentClass = fr_QuanLyKhachHang.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
                }
            }, 10);

        }


        //Listener to handle the menu item click. It returns the position of the menu item clicked. Based on that you can switch between the fragments.

        sNavigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (position) {
                            case 0: {

                                fragmentClass = fr_QuanLyKhachHang.class;
                                break;
                            }
                            case 1: {
                                fragmentClass = fr_QuanLyNhanVien.class;
                                break;
                            }
                            case 2: {
                                fragmentClass = fr_QuanLyPhongBan.class;
                                break;
                            }

                        }
                        sNavigationDrawer.closeDrawer();
                    }
                }, 100);
                //Listener for drawer events such as opening and closing.
                sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {
                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerOpening() {

                    }

                    @Override
                    public void onDrawerClosing() {

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (fragment != null) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
                                }
                            }, 800);

                        }
                    }

                    @Override
                    public void onDrawerClosed() {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        System.out.println("State " + newState);
                    }
                });
            }
        });

    }

}