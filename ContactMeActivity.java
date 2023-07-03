package com.example.mysportfriends_school_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactMeActivity extends AppCompatActivity {
    private Button contactViaPhone;
    private Customer customer;
    private Button contactViaEmail;
    private EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);
        init();
    }

    public void init() {
        Intent intent = getIntent();
        customer = (Customer) intent.getSerializableExtra("customer");
        contactViaEmail = findViewById(R.id.contactViaEmailBtn);
        contactViaPhone = findViewById(R.id.contactViaPhoneBtn);
        editTextMessage = findViewById(R.id.editTextMessage);

        // Contact using phone sms
        contactViaPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "0587364632";
                String message = ": הודעה מפרויקט צרו קשר איתי - אפליקציית ספורט" + editTextMessage.getText().toString();

                if (editTextMessage.getText().toString().isEmpty()) {
                    Toast.makeText(ContactMeActivity.this, "  ההודעה ריקה, אנא מלא את הודעת הSMS", Toast.LENGTH_SHORT).show();
                } else {
                    showSMSConfirmationDialog(phoneNumber, message);
                }
            }
        });
    }
    // Confirm email dialog
    public void showEmailConfirmationDialog(String recipientEmail, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("שליחת אימייל")
                .setMessage("האם אתה בטוח שברצונך לשלוח אימייל זה?\n\n" + message)
                .setPositiveButton("שלח", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendEmail(recipientEmail, message);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    // Send email
    public void sendEmail(String recipientEmail, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + recipientEmail));
        intent.putExtra(Intent.EXTRA_SUBJECT, "צור קשר איתי - אפליקציית ספורט");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showNoEmailClientDialog();
        }
    }

    // Show alert dialog for no email client
    private void showNoEmailClientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("אין מזיני אימייל")
                .setMessage("לא נמצא מזין אימייל במכשיר זה. האם ברצונך להשתמש באימייל הברירת מחדל?")
                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendEmailUsingDefaultClient();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    // Send email using default client
    private void sendEmailUsingDefaultClient() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{customer.getEmail()});
        intent.putExtra(Intent.EXTRA_SUBJECT, "צור קשר איתי - אפליקציית ספורט");
        intent.putExtra(Intent.EXTRA_TEXT, editTextMessage.getText().toString());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "לא נמצא מזיני אימייל", Toast.LENGTH_SHORT).show();
        }
    }

    // Show SMS confirmation dialog
    public void showSMSConfirmationDialog(String phoneNumber, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("שליחת הודעת SMS")
                .setMessage("האם אתה בטוח שברצונך לשלוח הודעת SMS זו?\n\n" + message)
                .setPositiveButton("שלח", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendSMS(phoneNumber, message);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    // Send SMS
    public void sendSMS(String phoneNumber, String message) {
        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "הודעת SMS נשלחה", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "לא אושרה הרשאת SMS", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
