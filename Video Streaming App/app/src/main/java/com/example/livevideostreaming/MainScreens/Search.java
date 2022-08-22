package com.example.livevideostreaming.MainScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.livevideostreaming.Adapters.MainRecyclerViewAdapter;
import com.example.livevideostreaming.Adapters.SearchRecyclerViewAdapter;
import com.example.livevideostreaming.Models.AllCategory;
import com.example.livevideostreaming.R;
import com.example.livevideostreaming.Retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Search extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    MenuItem menuItem;
    RecyclerView recyclerView;
    SearchRecyclerViewAdapter searchRecyclerViewAdapter;
    List<AllCategory> all_CategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle("Search");

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        recyclerView = findViewById(R.id.mainSearchRecyclerView);

        all_CategoryList = new ArrayList<>();


        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected())
        {
            AlertDialog.Builder alertDialogue = new AlertDialog.Builder(Search.this);
            alertDialogue.setTitle("No Internet connection");
            alertDialogue.setMessage("Please turn on your internet");
            alertDialogue.setCancelable(false);

            alertDialogue.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    recreate();
                }
            });
            alertDialogue.create().show();


        } else
        {
            // To fetch data from API
            getAllMoviesData();
        }



        Menu menu = bottomNavigationView.getMenu();
        menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.search_item:
                         if(item.getItemId() != R.id.search_item)
                         {
                             Intent intent = new Intent(Search.this,Search.class);
                             startActivity(intent);
                             finish();
                         }
                        break;
                    case R.id.setting_item:
                            Intent intent1 = new Intent(Search.this,Settings.class);
                            startActivity(intent1);
                            break;
                    case R.id.home_item:

                        Intent intent2 = new Intent(Search.this,MainScreen.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent2);
                        finish();
                        break;


                }

                return true;
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        menuItem.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.seacrhView);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRecyclerViewAdapter.getFilter().filter(newText);
                return true;
            }
        });


        return true;
    }

    private void setRecyclerView(List<AllCategory> allCategoryList)
    {
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(Search.this,allCategoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(Search.this));
        recyclerView.setAdapter(searchRecyclerViewAdapter);
    }

    private void getAllMoviesData() {

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(RetrofitClient.getRetrofitClient().getAllCategoriesMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<AllCategory>>() {
                    @Override
                    public void onNext(@NonNull List<AllCategory> allCategoryList) {
                        all_CategoryList = allCategoryList;
                        setRecyclerView(allCategoryList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));

    }



}