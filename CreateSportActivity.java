package com.example.mysportfriends_school_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class CreateSportActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    private NavigationView navigationView;

    private ImageView football_imgView;
    private ActionBarDrawerToggle toggle;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ImageView tennis_imgView;
    private ImageView basketball_imgView;
    private ImageView volleyball_imgView;
    private Customer selected_customer;
    private AppDatabase app_db;
    private CustomerDao customerDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sport);
        init();
        func_volleyball_clicked();
        func_basketball_clicked();
        func_football_clicked();
        func_tennis_clicked();
    }

    public void init(){


        drawer = findViewById(R.id.actionBarDrawerToggle);
        navigationView = findViewById(R.id.navigationView);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();
        Intent intent = getIntent();
        selected_customer = (Customer) intent.getSerializableExtra("customer");
        NavigationForAllActivities.SetNavigationForActivities(drawer,
                navigationView, CreateSportActivity.this, selected_customer
        );



        football_imgView = findViewById(R.id.football_imgView);
        basketball_imgView= findViewById(R.id.basketball_imgView);
        volleyball_imgView = findViewById(R.id.volleyball_imgVIew);
        tennis_imgView = findViewById(R.id.tennis_imgView);
        app_db = AppDatabase.getDatabase(this);
        customerDao = app_db.customerDAO();
        int expandedHeight = 500;

    }
    // categiry image clicked, start intent

    public void func_football_clicked(){
        football_imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateSportActivity.this);
                builder.setMessage("אשרו בחירת קטגורית כדורגל")
                        .setTitle("אשרו")
                        .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(CreateSportActivity.this, DesignSportActivity.class);
                                intent.putExtra("sport", "football");
                                intent.putExtra("customer", selected_customer);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(navigationView)) {
            drawer.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }
// categiry image clicked, start intent

    public void func_basketball_clicked(){
        basketball_imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateSportActivity.this);
                builder.setMessage("אשרו בחירת קטגורית כדורסל")
                        .setTitle("אשרו")
                        .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(CreateSportActivity.this, DesignSportActivity.class);
                                intent.putExtra("sport", "basketball");
                                intent.putExtra("customer", selected_customer);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


// categiry image clicked, start intent

    public void func_tennis_clicked(){
        tennis_imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateSportActivity.this);
                builder.setMessage("אשרו בחירת קטגורית טניס")
                        .setTitle("אשרו")
                        .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(CreateSportActivity.this, DesignSportActivity.class);
                                intent.putExtra("sport", "tennis");
                                intent.putExtra("customer", selected_customer);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }// categiry image clicked, start intent

    public void func_volleyball_clicked(){
        volleyball_imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateSportActivity.this);
                builder.setMessage("אשרו בחירת קטגורית כדורעף")
                        .setTitle("אשרו")
                        .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(CreateSportActivity.this, DesignSportActivity.class);
                                intent.putExtra("sport", "volleyball");
                                intent.putExtra("customer", selected_customer);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}