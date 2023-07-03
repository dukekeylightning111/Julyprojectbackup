package com.example.mysportfriends_school_project;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SportingActivityAdapter extends RecyclerView.Adapter<SportingActivityAdapter.ViewHolder> {
    private List<SportingActivityClass> sportingActivities = new ArrayList<>();
    private Customer customer;
    private SportingActivityClassDao sportingActivityClassDao;
    private int customerId;
    private Context context;
    private DoubleClickListener doubleClickListener;

    public void deleteItem(int position) {
        sportingActivities.remove(position);
        notifyItemRemoved(position);
    }

    public SportingActivityAdapter(Customer customer, SportingActivityClassDao sportingActivityClassDao, Activity activity, Context context) {
        this.customer = customer;
        this.customerId = customer.getId();
        this.sportingActivityClassDao = sportingActivityClassDao;
        this.context = context;
        loadSportingActivities();
        doubleClickListener = new DoubleClickListener(new DoubleClickListener.OnDoubleClickListener() {
            @Override
            public void onDoubleClick(View view) {
                int position = (int) view.getTag();
                SportingActivityClass sportingActivity = getSportingActivityAtPosition(position);
                String message = customer.getName().toString() + " הזמין אותכם! \n" + " "
                        + "פעולת ספורט:\n "  + sportingActivity.getTitle()
                        + "\nזמן: " + sportingActivity.getTime()
                        + "\nקטגוריה" + sportingActivity.getCategory()
                        + "\nמיקום: " + sportingActivity.getLocation()
                        + "\nתיאור: " + sportingActivity.getDescription();

                showConfirmationDialog(message);
            }
        });
    }

    private void showConfirmationDialog(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("אשרו בחירת פעולה לפני השיתוף")
                .setMessage("אתם מאשרים שיתוף לפעולה זו? (ההודעה תראה כך:)" + message)
                .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showShareDialog(message);
                    }
                })
                .setNegativeButton("ביטול", null)
                .show();
    }

    private void showShareDialog(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("שתפו פעולה")
                .setMessage("איך תרצו לשתף פעולה זו?")
                .setPositiveButton("הודעת SMS?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendSMS(message);
                    }
                })
                .show();
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("הודעה");
        builder.setMessage(message);
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void sendSMS(String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"));
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
    public void clearSportingActivities() {
        sportingActivities.clear();
        notifyDataSetChanged();
    }

    private void sendEmail(String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "אתם הוזמנתם לפעולת הפורט שלי! הנה פרטי פעולת הספורט");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    private void sendWhatsApp(String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "אין גישה לאפליצית WhatsApp במכשיר זה", Toast.LENGTH_SHORT).show();
        }
    }

    public SportingActivityAdapter(List<SportingActivityClass> sportingActivities, Customer customer) {
        this.customer = customer;
        this.sportingActivities = sportingActivities;
    }

    public void setSportingActivities(List<SportingActivityClass> sportingActivities) {
        this.sportingActivities.clear();

        if (sportingActivities != null) {
            this.sportingActivities.addAll(sportingActivities);
        }

        notifyDataSetChanged();
    }

    public SportingActivityClass getSportingActivityAtPosition(int position) {
        if (position >= 0 && position < sportingActivities.size()) {
            return sportingActivities.get(position);
        } else {
            return null;
        }
    }

    public void removeSportingActivity(int position) {
        sportingActivities.remove(position);
        notifyItemRemoved(position);
    }



    public void deleteSportingActivity(SportingActivityClass sportingActivityClass, SportingActivityClassDao sportingActivityClassDao) {
        sportingActivityClassDao.delete(sportingActivityClass);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sporting_acitivity_item, parent, false);
        view.setOnClickListener(doubleClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(sportingActivities.get(position));
    }

    @Override
    public int getItemCount() {
        return sportingActivities.size();
    }

    private void loadSportingActivities() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<SportingActivityClass> sportingActivities = sportingActivityClassDao.getSportingActivitiesByUserId(customerId);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        setSportingActivities(sportingActivities);
                    }
                });
            }
        }).start();
    }
    public void setNotification(Context context, SportingActivityClass sportingActivity) {
        String activityTimeString = sportingActivity.getTime();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date activityTime = format.parse(activityTimeString);

            Calendar notificationTime = Calendar.getInstance();
            notificationTime.setTime(activityTime);
            notificationTime.add(Calendar.MINUTE, -15); // 15 minutes before
            setAlarm(context, notificationTime, sportingActivity);

            notificationTime.add(Calendar.MINUTE, 5); // 10 minutes before
            setAlarm(context, notificationTime, sportingActivity);

            notificationTime.add(Calendar.MINUTE, 5); // 5 minutes before
            setAlarm(context, notificationTime, sportingActivity);

            notificationTime.add(Calendar.MINUTE, 2); // 3 minutes before
            setAlarm(context, notificationTime, sportingActivity);

            notificationTime.add(Calendar.MINUTE, 1); // 1 minute before
            setAlarm(context, notificationTime, sportingActivity);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setAlarm(Context context, Calendar notificationTime, SportingActivityClass sportingActivity) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, NotificationBroadCast.class);
        intent.putExtra("sportingActivity", sportingActivity);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime.getTimeInMillis(), pendingIntent);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private Button shareButton;
        private TextView timeTextView;
        private TextView locationTextView;
        private TextView adminCustomerTextView;
        private TextView activityDescTextView;
        private ImageView imageViewCategory;
        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.sport_activity_name);
            timeTextView = itemView.findViewById(R.id.sport_activity_time);
            locationTextView = itemView.findViewById(R.id.sport_activity_location);
            activityDescTextView = itemView.findViewById(R.id.act_desc);
            imageViewCategory = itemView.findViewById(R.id.imageViewCategory);
            context = itemView.getContext();
        }

        public void bind(@NonNull SportingActivityClass sportingActivity) {
            if (sportingActivity != null) {
                String name = sportingActivity.getTitle();
                String time = sportingActivity.getTime();
                String location = sportingActivity.getLocation();
                locationTextView.setText("מיקום הפעולה: " + location);
                String description = sportingActivity.getDescription();
                Customer customer = sportingActivity.getCustomer();

                nameTextView.setText(name);
                timeTextView.setText(sportingActivity.getDate() + " " + time);
                activityDescTextView.setText(description);
                imageViewCategory.setScaleType(ImageView.ScaleType.CENTER_CROP);

                String category = sportingActivity.getCategory();
                if (category.equals("football")) {
                    imageViewCategory.setImageResource(R.drawable.people_football_image);
                } else if (category.equals("basketball")) {
                    imageViewCategory.setImageResource(R.drawable.basketbal_in_activity);
                } else if (category.equals("tennis")) {
                    imageViewCategory.setImageResource(R.drawable.tennis_in_activity_wallpaper);
                } else if (category.equals("volleyball")) {
                    imageViewCategory.setImageResource(R.drawable.people_volleyval_image);
                }

                itemView.setTag(getAdapterPosition());
            } else{
                nameTextView.setText("לא יצרתם פעולות ספורט");
            }
        }
    }
}

