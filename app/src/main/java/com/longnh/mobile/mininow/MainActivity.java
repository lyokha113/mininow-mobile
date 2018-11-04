package com.longnh.mobile.mininow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private MenuItem prevMenuItem;
    private BottomNavigationView bottomNavigation;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();

        setBottomNavigationConfig();
        setupViewPager(viewPager);
    }


    private void addControls() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewpager);
    }


    private void addEvents() {
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        setBottomNavigationSelectedItem(item);
                        switch (item.getItemId()) {
                            case R.id.nav_store:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.nav_order:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.nav_account:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return true;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    bottomNavigation.getMenu().getItem(0).setChecked(false);

                MenuItem item = bottomNavigation.getMenu().getItem(position);
                setBottomNavigationSelectedItem(item);
                item.setChecked(true);
                prevMenuItem = item;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setBottomNavigationConfig() {
        bottomNavigation.setItemIconTintList(null);
        bottomNavigation.getMenu().getItem(0).setIcon(R.drawable.nav_store_selected);
    }

    private void setBottomNavigationSelectedItem(@NonNull MenuItem item) {

        Menu menu = bottomNavigation.getMenu();
        menu.getItem(0).setIcon(R.drawable.nav_store);
        menu.getItem(1).setIcon(R.drawable.nav_order);
        menu.getItem(2).setIcon(R.drawable.nav_account);

        switch (item.getItemId()) {
            case R.id.nav_store:
                item.setIcon(R.drawable.nav_store_selected);
                break;
            case R.id.nav_order:
                item.setIcon(R.drawable.nav_order_selected);
                break;
            case R.id.nav_account:
                item.setIcon(R.drawable.nav_account_selected);
                break;
        }

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Fragment storeFragment = new StoreFragment();
        Fragment orderFragment = new OrderFragment();
        Fragment accountFragment = new AccountFragment();

        adapter.addFrag(storeFragment, "store");
        adapter.addFrag(orderFragment, "order");
        adapter.addFrag(accountFragment, "account");

        viewPager.setAdapter(adapter);
    }

}
