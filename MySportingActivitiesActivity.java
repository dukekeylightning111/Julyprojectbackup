package com.example.mysportfriends_school_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class MySportingActivitiesActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ArrayList<SportingActivityClass> sportingActivityClasses;
    private Context context;
    private EditText search;
    private TextInputLayout searchLayout;
    private Customer currentCustomer;
    private AppDatabase app_db;
    private List<SportingActivityClass> allSportingActivities;

    private CustomerDao customerDao;
    private FloatingActionButton myFAB;
    private SportingActivityAdapter adapter;
    private ActionBarDrawerToggle toggle;
    private LiveData<SportingActivityClass> currentLiveData;
    private LiveData<List<SportingActivityClass>> sportingActivityLiveData;
    private TextView noActivities;
    private MediatorLiveData<List<SportingActivityClass>> mediatorSportingActivityLiveData
            = new MediatorLiveData<>();

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView customer_name;
    private SportingActivityClassDao sportingActivityClassDao;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sporting_activities);
        init();

        func_view_sport_activities();
        func_create_sport_activity();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateSportingActivitiesList();
            }
        });
    }
    private void func_no_activities(){
        noActivities.setText("אין לכם פעולות ספורט");
    }
    private void editSportingActivity(final SportingActivityClass sportingActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Sporting Activity");

        TextInputLayout titleLayout = new TextInputLayout(this);
        titleLayout.setHint("כותרת");
        EditText editTitle = new EditText(this);
        titleLayout.addView(editTitle);
        TextInputLayout descriptionLayout = new TextInputLayout(this);
        descriptionLayout.setHint("תיאור");
        EditText editDescription = new EditText(this);
        descriptionLayout.addView(editDescription);
        TextInputLayout timeLayout = new TextInputLayout(this);
        timeLayout.setHint("זמן");
        EditText editTime = new EditText(this);
        timeLayout.addView(editTime);
        TextInputLayout dateLayout = new TextInputLayout(this);
        dateLayout.setHint("תאריך");
        EditText editDate = new EditText(this);
        dateLayout.addView(editDate);
        editTitle.setText(sportingActivity.getTitle());
        editDescription.setText(sportingActivity.getDescription());
        editTime.setText(sportingActivity.getTime());
        editDate.setText(sportingActivity.getDate());
        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.addView(titleLayout);
        dialogLayout.addView(descriptionLayout);
        dialogLayout.addView(timeLayout);
        dialogLayout.addView(dateLayout);

        builder.setView(dialogLayout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTitle = editTitle.getText().toString();
                String newDescription = editDescription.getText().toString();
                String newTime = editTime.getText().toString();
                String newDate = editDate.getText().toString();

                // Perform validation on the edited values
                if (TextUtils.isEmpty(newTitle)) {
                    titleLayout.setError("Title cannot be empty");
                    return;
                }

                // Update the sporting activity with the new values
                sportingActivity.setTitle(newTitle);
                sportingActivity.setDescription(newDescription);
                sportingActivity.setTime(newTime);
                sportingActivity.setDate(newDate);

                // Update the sporting activity in the database using a background thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sportingActivityClassDao.insert(sportingActivity);
                        currentCustomer.addSportingActivity(sportingActivity);
                    }
                }).start();

                // Update the adapter by triggering LiveData to refresh the list
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateSportingActivitiesList() {
        if (allSportingActivities != null && !allSportingActivities.isEmpty()) {
            if (search.getText().toString().isEmpty()) {
                adapter.setSportingActivities(allSportingActivities);
            } else {
                String sportTitle = search.getText().toString().toLowerCase();
                List<SportingActivityClass> filteredSportingActivities = new ArrayList<>();

                for (SportingActivityClass sportingActivity : allSportingActivities) {
                    if (sportingActivity.getTitle().toLowerCase().contains(sportTitle)) {
                        filteredSportingActivities.add(sportingActivity);
                    }
                }

                adapter.clearSportingActivities(); // Clear the existing activities
                adapter.setSportingActivities(filteredSportingActivities);
            }

            adapter.notifyDataSetChanged();
        }
    }

    public void init() {
        Intent intent = getIntent();
        currentCustomer = (Customer) intent.getSerializableExtra("customer");
        customer_name = findViewById(R.id.customer_name);
        noActivities = findViewById(R.id.noactivities);
        searchLayout = findViewById(R.id.searchitemlayout);
        search = searchLayout.getEditText();
        drawerLayout = findViewById(R.id.my_drawer_layout);
        app_db = AppDatabase.getDatabase(this);
        customerDao = app_db.customerDAO();
        sportingActivityClassDao = app_db.SportingActivityClassDao();
        myFAB = findViewById(R.id.myFAB);
        context = getApplicationContext();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.navigationView);
        context = MySportingActivitiesActivity.this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                allSportingActivities = customerDao.getAllActivitiesByCustomerID(currentCustomer.getId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateSportingActivitiesList();
                    }
                });
            }
        }).start();
        if(allSportingActivities.isEmpty()){
            func_no_activities();
        }
        NavigationForAllActivities.SetNavigationForActivities(drawerLayout, navigationView, this, currentCustomer);
        currentLiveData = sportingActivityClassDao.getSportingActivitiesByUserIdNoList(currentCustomer.getId());
        adapter = new SportingActivityAdapter(currentCustomer, sportingActivityClassDao, MySportingActivitiesActivity.this, MySportingActivitiesActivity.this);
        adapter.setSportingActivities(allSportingActivities);
        recyclerView = findViewById(R.id.sporting_activity_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MySportingActivitiesActivity.this));
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
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }

    public void func_create_sport_activity() {
        myFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createSportIntent = new Intent(context, CreateSportActivity.class);
                createSportIntent.putExtra("customer", currentCustomer);
                startActivity(createSportIntent);
            }
        });
    }

    public void func_view_sport_activities() {
        mediatorSportingActivityLiveData.observe(this, new Observer<List<SportingActivityClass>>() {
            @Override
            public void onChanged(List<SportingActivityClass> sportingActivities) {
                adapter.setSportingActivities(sportingActivities);
                adapter.notifyDataSetChanged();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<SportingActivityClass> sportingActivities = currentCustomer.getSportingActivityClasses();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new SportingActivityAdapter(currentCustomer, sportingActivityClassDao, MySportingActivitiesActivity.this, MySportingActivitiesActivity.this);
                        adapter.setSportingActivities(sportingActivities);

                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MySportingActivitiesActivity.this));

                        SwipeToDelete swipeToDelete = new SwipeToDelete(adapter, app_db, MySportingActivitiesActivity.this);
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDelete);
                        itemTouchHelper.attachToRecyclerView(recyclerView);

                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }
}