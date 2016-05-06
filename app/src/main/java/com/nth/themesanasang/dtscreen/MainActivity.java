package com.nth.themesanasang.dtscreen;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spannable;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private Session session;

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    CoordinatorLayout rootLayout;

    private NavigationView navigationView;
    private  String user_id;
    private  String username;
    private  String nameFull;

    private int mCurrentSelectedPosition;

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
        if (myIntent.hasExtra("user_id")){
            user_id = myIntent.getStringExtra("user_id");
            username = myIntent.getStringExtra("username");
            nameFull = myIntent.getStringExtra("name");
        }

        if (savedInstanceState == null) {
            navigationView.getMenu().getItem(0).setChecked(true);
            PatientFragment fragment = PatientFragment.newInstance(username);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
        }else{
            mCurrentSelectedPosition = savedInstanceState.getInt("home");
            navigationView.getMenu().getItem(mCurrentSelectedPosition).setChecked(true);
        }

        if(user_id == null){
            logoutUser();
        }

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
        //return true;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                //Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                searchView.clearFocus();

                SearchResultFragment fragment = SearchResultFragment.newInstance(username, query);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

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
        /*if (id == R.id.action_settings) {
            return true;
        }*/

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

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch(menuItem.getItemId()) {
            case R.id.btnListPatient:
                mCurrentSelectedPosition = 0;
                PatientFragment Patient = PatientFragment.newInstance(username);
                transaction.replace(R.id.flContent, Patient);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.btnScreen:
                mCurrentSelectedPosition = 1;
                ScreenFragment Screen = ScreenFragment.newInstance(username);
                transaction.replace(R.id.flContent, Screen);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            /*case R.id.btnUploadData:
                fragmentClass = UploadDataFragment.class;
                break;*/
            case R.id.btnProfile:
                mCurrentSelectedPosition = 1;
                ProfileFragment Profile = ProfileFragment.newInstance(username);
                transaction.replace(R.id.flContent, Profile);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            default:
                mCurrentSelectedPosition = 0;
                PatientFragment DefaultFragment = PatientFragment.newInstance(username);
                transaction.replace(R.id.flContent, DefaultFragment);
                transaction.addToBackStack(null);
                transaction.commit();
        }


        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.d("Msg = ", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("ออกจากระบบ");
            //dialog.setIcon(R.drawable.ic_launcher);
            dialog.setCancelable(true);
            dialog.setMessage("คุณต้องการออกจากระบบ?");
            dialog.setPositiveButton("ใช่", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            dialog.setNegativeButton("ไม่", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                    navigationView.getMenu().getItem(0).setChecked(true);
                    PatientFragment fragment = PatientFragment.newInstance(username);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
                }
            });

            dialog.show();
        }
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("home", mCurrentSelectedPosition);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt("home", 0);
        Menu menu = navigationView.getMenu();
        menu.getItem(mCurrentSelectedPosition).setChecked(true);
    }


    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
