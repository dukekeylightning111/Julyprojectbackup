package com.example.mysportfriends_school_project;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DesignSportActivity extends AppCompatActivity implements LocationListener, Serializable {
    private String category;
    private TextView info_sport_category;
    private String selectedLocation = "";
    private FragmentResultListener fragmentResultListener;
    private Button time_button;
    private String formattedDate = "";
    private Button create_sport_activity;
    private String time = "";
    private String date = "";
    private String temp = "";
    private String message = "";
    private String title = "";
    private int PERMISSION_REQUEST_CODE = 1;
    private EditText editText_title;
    private Calendar calendar;
    private Button getDate;
    private String description = "";
    private EditText editText_sport_act_desc;
    private SportingActivityClass sportingActivityClass;
    private CustomerDao customerDao;
    private AppDatabase app_db;
    private Button btn_to_see_sport_activities;
    private int hour;
    private int minute;
    private Button get_location;
    private ActionBarDrawerToggle toggle;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private SportingActivityClassDao sportingActivityClassDao;
    private Customer currentCustomer;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_sport);
        Intent intent = getIntent();
        init();
        getDate();
        func_get_location();
        func_createSportActivity();
        func_expand_cv();
        func_view_sport_activities();
        getSupportFragmentManager().setFragmentResultListener("map_result", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals("map_result")) {
                    String selectedAddress = result.getString("selected_address");

                    get_location.setText("מיקום:" + selectedAddress);
                    selectedLocation = selectedAddress;
                }
            }
        });
    }

    public void setFragmentResultListener(String resultKey, FragmentResultListener fragmentResultListener) {
        getSupportFragmentManager().setFragmentResultListener(resultKey, this, fragmentResultListener);
    }

    public void showTimePickerDialog(View v) {
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(this, timeSetListener, currentHour, currentMinute, DateFormat.is24HourFormat(this));
        dialog.show();
    }

    public TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar selectedTime = Calendar.getInstance();
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedTime.set(Calendar.MINUTE, minute);

            Calendar currentTime = Calendar.getInstance();

            if (selectedTime.before(currentTime)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DesignSportActivity.this);
                builder.setTitle("זמן פעולה אינו תקין");
                builder.setMessage("בחרתם זמן שכבר עבר.");
                builder.setPositiveButton("אישור", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                time_button.setText("זמן פעולה: " + time + " לחצו עבור לשנות");
            }
        }
    };


    private boolean checkPermissions() {
        int coarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int internetPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int networkStatePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE);

        return coarseLocationPermission == PackageManager.PERMISSION_GRANTED &&
                fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                internetPermission == PackageManager.PERMISSION_GRANTED &&
                networkStatePermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
        }, PERMISSION_REQUEST_CODE);
    }

    public void func_expand_cv() {
        getDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderCustomised = new AlertDialog.Builder(DesignSportActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                Calendar currentDate = Calendar.getInstance();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DesignSportActivity.this, new
                        DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, selectedYear);
                                calendar.set(Calendar.MONTH, selectedMonth);
                                calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                                if (calendar.before(currentDate)) {
                                    AlertDialog alertDialog = builderCustomised.setTitle("תאריך אינו תקין")
                                            .setMessage("אנא בחרו תאריך תקין (תאריך היום או אחרי)")
                                            .setPositiveButton("אישור", null)
                                            .create();
                                    alertDialog.show();
                                    return;
                                }

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String format_date = dateFormat.format(calendar.getTime());
                                date = format_date;
                                getDate.setText("תאריך פעולה:" + date + " לחצו עבור לשנות");
                                formattedDate = date;
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }
    // set the details for activity
    public void func_createSportActivity() {
        create_sport_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String errorMessages = "";

                title = editText_title.getText().toString();
                description = editText_sport_act_desc.getText().toString();

                if (title.isEmpty()) {
                    errorMessages += "כותרת חסרה, יש למלא חלק זה\n";
                }
                if (category.isEmpty()) {
                    errorMessages += "קטגוריה חסרה, יש למלא חלק זה\n";
                }
                if (time.isEmpty()) {
                    errorMessages += "זמן פעולה חסר, יש למלא חלק זה\n";
                }
                if (description.isEmpty()) {
                    errorMessages += "תיאור פעולה חסר, יש למלא חלק זה\n";
                }
                if (formattedDate.isEmpty()) {
                    errorMessages += "תאריך פעולה חסר, יש למלא חלק זה\n";
                }
                if (selectedLocation.toString().isEmpty()) {
                    errorMessages += "מיקום פעולה חסר, יש למלא חלק זה\n";
                }

                if (!errorMessages.isEmpty()) {
                    new AlertDialog.Builder(DesignSportActivity.this)
                            .setTitle("פרטים חסרים")
                            .setMessage(errorMessages)
                            .setPositiveButton("אישור", null)
                            .show();
                } else {
                    new AlertDialog.Builder(DesignSportActivity.this)
                            .setTitle("אישור יצירת פעילות ספורט")
                            .setMessage("האם אתה בטוח שברצונך ליצור פעילות ספורט?\n\nפרטי הפעילות שהוזנו:\nכותרת: " + title + "\nקטגוריה: " + category + "\nתיאור: " + description + "\nזמן: " + time + "\nתאריך: " + formattedDate + "\n מיקום" + selectedLocation)
                            .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    sportingActivityClass = new SportingActivityClass(title, category, currentCustomer, time, description, selectedLocation, formattedDate);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            currentCustomer.addSportingActivity(sportingActivityClass);
                                            sportingActivityClassDao.insert(sportingActivityClass);
                                            scheduleNotification(DesignSportActivity.this, sportingActivityClass);
                                        }
                                    }).start();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new AlertDialog.Builder(DesignSportActivity.this)
                                                    .setMessage("פעולה נוצרה בהצלחה")
                                                    .setPositiveButton("אישור", null)
                                                    .show();
                                            editText_title.setText("");
                                            get_location.setText("קבעו מיקום");
                                            editText_sport_act_desc.setText("");
                                            time_button.setText("קבעו זמן לפעולה");
                                            getDate.setText("קבעו תאריך לפעולה");
                                            scheduleNotification(DesignSportActivity.this, sportingActivityClass);

                                        }
                                    });
                                }

                            })
                            .setNegativeButton("לא", null)
                            .show();
                }
            }
        });
    }
    private Time parseTime(String timeString) {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date date = timeFormat.parse(timeString);
            return new Time(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void scheduleNotification(Context context, SportingActivityClass sportingActivity) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, NotificationBroadCast.class);
        intent.putExtra("sportingActivity", sportingActivity);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date date = dateFormat.parse(sportingActivity.getDate());
            Time time = parseTime(sportingActivity.getTime());

            if (date != null && time != null) {
                calendar.setTime(date);
                calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
                calendar.set(Calendar.MINUTE, time.getMinutes());
                calendar.set(Calendar.SECOND, 1);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    // checking if variables are valid
    public boolean check_variables() {
        String desc = editText_sport_act_desc.getText().toString();
        String ttl = editText_title.getText().toString();

        if (date == null || time == null || time.isEmpty() || desc.isEmpty() || ttl.isEmpty()) {
            return false;
        } else {
            description = desc;
            title = ttl;
            return true;
        }
    }
    // pop up date dialog
    public void getDate() {
        time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (date != null) {
                    showTimePickerDialog(view);
                }
            }
        });
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                time_button.setText("זמן פעולה: " + time + " לחצו עבור לשנות");
                String selectedDateTime = "תאריך הפעולה: " + formattedDate + " " + time;
            }
        };
    }
    // start intent and view sport activities
    public void func_view_sport_activities() {
        btn_to_see_sport_activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(DesignSportActivity.this, MySportingActivitiesActivity.class);
                I.putExtra("customer", currentCustomer);
                startActivity(I);
            }
        });
    }

    // initialzie
    public void init() {
        drawerLayout = findViewById(R.id.myFragmentContainerView);
        getDate = findViewById(R.id.getDate);
        create_sport_activity = findViewById(R.id.button_add_sport);
        category = getIntent().getStringExtra("sport");
        setTitle(category);
        editText_title = findViewById(R.id.sport_act_title);
        editText_sport_act_desc = findViewById(R.id.EditTxt_act_desc);
        app_db = AppDatabase.getDatabase(this);
        customerDao = app_db.customerDAO();
        sportingActivityClassDao = app_db.SportingActivityClassDao();
        btn_to_see_sport_activities = findViewById(R.id.btn_to_see_sport_activities);
        Intent intent = getIntent();
        currentCustomer = (Customer) intent.getSerializableExtra("customer");
        info_sport_category = findViewById(R.id.txtView_sport_act_info);
        info_sport_category.setText("פעולת ה" + category + " של " + currentCustomer.getName());
        get_location = findViewById(R.id.get_location);
        calendar = Calendar.getInstance();
        time_button = findViewById(R.id.button_confirm_date);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.design_navigation_view);
        context = DesignSportActivity.this;
        NavigationForAllActivities.SetNavigationForActivities(drawerLayout,
                navigationView, this, currentCustomer);
        title = editText_title.getText().toString();
        description = editText_sport_act_desc.getText().toString();

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
    public void func_get_location() {
        get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DesignSportActivity.this);
                builder.setTitle("בחירת מיקום");
                builder.setMessage("אנא בחר את אופציית המיקום:");

                TextInputLayout inputLayout = new TextInputLayout(DesignSportActivity.this);
                inputLayout.setHint("הכנס מיקום");

                EditText inputEditText = new EditText(DesignSportActivity.this);
                inputLayout.addView(inputEditText);
                LinearLayout dialogLayout = new LinearLayout(DesignSportActivity.this);
                dialogLayout.setOrientation(LinearLayout.VERTICAL);
                dialogLayout.addView(inputLayout);
                builder.setView(dialogLayout);
                builder.setPositiveButton("הכנסת מיקום", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String enteredLocation = inputEditText.getText().toString().trim();
                        if (!enteredLocation.isEmpty()) {
                            selectedLocation = enteredLocation;
                            get_location.setText(selectedLocation + "זה המיקום שבחרתם :");
                        }
                    }
                });

                builder.setNeutralButton("שימוש במיקום נוכחי", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        {
                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            LocationListener locationListener = new LocationListener() {
                                boolean isLocationFound = false;

                                @Override
                                public void onLocationChanged(Location location) {
                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();
                                    Geocoder geocoder = new Geocoder(DesignSportActivity.this, Locale.getDefault());
                                    try {
                                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                        if (addresses.size() > 0) {
                                            Address address = addresses.get(0);
                                            String city = address.getLocality();
                                            String street = address.getThoroughfare();
                                            String fullAddress = address.getAddressLine(0);
                                            selectedLocation = fullAddress;
                                            get_location.setText(selectedLocation + "זה המיקום שבחרתם :");
                                            isLocationFound = true;

                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {}

                                @Override
                                public void onProviderEnabled(String provider) {}

                                @Override
                                public void onProviderDisabled(String provider) {}
                            };

                            try {
                                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
                            } catch (SecurityException e) {
                                e.printStackTrace();
                                Toast.makeText(DesignSportActivity.this, "שגיאה בעת גישה למיקום", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                });


                builder.setNegativeButton("בחירת מיקום מהמפה", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openMapFragment();
                        fragmentResultListener = new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                                selectedLocation = result.getString("selected_address");
                                get_location.setText(selectedLocation + "זה המיקום שבחרתם :");
                            }
                        };
                    }
                });

                builder.show();
            }
        });
    }



    public void openMapFragment() {
        MapsFragment mapFragment = new MapsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.myFragmentContainerView, mapFragment, "map_fragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}