package com.example.mysportfriends_school_project;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDelete extends ItemTouchHelper.Callback {

    private SportingActivityAdapter adapter;
    private AppDatabase appDatabase;
    private Context context;

    public SwipeToDelete(SportingActivityAdapter adapter, AppDatabase appDatabase, Context context) {
        this.adapter = adapter;
        this.appDatabase = appDatabase;
        this.context = context;
    }

    public SwipeToDelete(SportingActivityAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        showConfirmationDialog(position);
    }

    public void showConfirmationDialog(final int position) {
        SportingActivityClass sportingActivity = adapter.getSportingActivityAtPosition(position);

        if (!sportingActivity.isDeletable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("אינכם יכולים למחוק פעולה זו.")
                    .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            adapter.notifyDataSetChanged();
                        }
                    });
            builder.create().show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("אתם בטוחים לגבי מחיקת פריט זה?")
                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                appDatabase.SportingActivityClassDao().delete(sportingActivity);
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.deleteItem(position);
                                    }
                                });
                            }
                        }).start();
                    }
                })
                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                appDatabase.SportingActivityClassDao().delete(sportingActivity);

                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyItemRemoved(position);
                                    }
                                });
                            }
                        }).start();
                    }
                });
        builder.create().show();
    }}
