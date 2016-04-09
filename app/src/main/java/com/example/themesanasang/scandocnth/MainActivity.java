package com.example.themesanasang.scandocnth;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spannable;

import butterknife.Bind;
import butterknife.ButterKnife;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Session session;

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    CoordinatorLayout rootLayout;

    private NavigationView navigationView;
    private  String userid;
    private  String username;
    private  String nameFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        session = new Session(MainActivity.this);

        if (!session.getLoggedIn()) {
            logoutUser();
        }

        initToolbar();
        initInstances();

        navigationView = (NavigationView) findViewById(R.id.navigation);
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        setupDrawerContent(navigationView);

        Intent myIntent = getIntent();
        if (myIntent.hasExtra("userid")){
            userid = myIntent.getStringExtra("userid");
            username = myIntent.getStringExtra("username");
            nameFull = myIntent.getStringExtra("name");
        }

        PatientFragment fragment = PatientFragment.newInstance(username);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/NotoSans-Regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initInstances() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.themesanasang, R.string.themesanasang);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {

        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.btnListPatient:
                fragment = PatientFragment.newInstance(username);
                break;
            /*case R.id.btnScreen:
                fragmentClass = ScreenFragment.class;
                break;
            case R.id.btnUploadData:
                fragmentClass = UploadDataFragment.class;
                break;
            case R.id.btnProfile:
                fragmentClass = ProfileFragment.class;
                break;*/
            case R.id.btnLogout:
                logoutUser();
            default:
                fragment = PatientFragment.newInstance(username);
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }




    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
