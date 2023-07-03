package com.example.mysportfriends_school_project;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class NavigationForAllActivities {
    public static void SetNavigationForActivities(DrawerLayout drawerLayout,
                                                  NavigationView navigationView,
                                                  Activity activity, Customer currentCustomer) {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Intent intent = null;

                if (itemId == R.id.home_page && !(activity instanceof MainActivity)) {
                    intent = new Intent(activity, MainActivity.class);
                } else if (itemId == R.id.create_sport_activity && !(activity instanceof CreateSportActivity)) {
                    intent = new Intent(activity, CreateSportActivity.class);
                } else if (itemId == R.id.projectInfo && !(activity instanceof ProjectInfoActivity)) {
                    intent = new Intent(activity, ProjectInfoActivity.class);
                } else if (itemId == R.id.view_sport_activity && !(activity instanceof MySportingActivitiesActivity)) {
                    intent = new Intent(activity, MySportingActivitiesActivity.class);
                } else if (itemId == R.id.exit_app) {
                    showExitDialog(activity);
                } else if (itemId == R.id.logout) {
                    showLogoutDialog(activity);
                }

                if (intent != null) {
                    intent.putExtra("customer", currentCustomer);
                    activity.startActivity(intent);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private static void showExitDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("אשרו יציאה");
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton("ביטול", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void showLogoutDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("אשרו התנתקות?");
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(activity, WelcomeUserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.finish();
            }
        }).setNegativeButton("ביטול", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}