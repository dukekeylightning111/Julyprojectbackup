package com.example.mysportfriends_school_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class LogInFragment extends Fragment {

    private EditText user_nameEditText;
    private EditText passwordEditText;
    private Button sign_inButton;
    private AppDatabase app_db;
    private String message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        app_db = AppDatabase.getDatabase(getActivity());

        user_nameEditText = view.findViewById(R.id.user_name);
        passwordEditText = view.findViewById(R.id.password);
        sign_inButton = view.findViewById(R.id.log_in);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateFields();
            }

            private void validateFields() {
                String username = user_nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (username.length() == 0 && password.length() != 0) {
                    user_nameEditText.setError("שם משתמש חסר, אנא מלאו חלק זה.");
                }
                if (username.length() < 3 && username.length() != 0) {
                    user_nameEditText.setError("שם משתמש צריך להכיל 3 תווים או יותר.");
                } else {
                    user_nameEditText.setError(null);
                }
                if (password.length() == 0) {
                    passwordEditText.setError("ססמא חסרה, אנא מלאו חלק זה.");
                }
                if (password.length() < 3 && password.length() != 0) {
                    passwordEditText.setError("סיסמא צריכה להכיל 3 תווים או יותר.");
                } else {
                    passwordEditText.setError(null);
                }
            }
        };

        user_nameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);

        sign_inButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_nameEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty()) {
                    message = "";
                    if (user_nameEditText.getText().toString().isEmpty()) {
                        message = "שם משתמש חסר\n";
                    }
                    if (passwordEditText.getText().toString().isEmpty()) {
                        message += "ססמא חסרה\n";
                    }
                    new AlertDialog.Builder(getActivity())
                            .setTitle(message.isEmpty() ? "אין נתונים" : "פרטים חסרים")
                            .setMessage(message.isEmpty() ? "נא למלא שם משתמש וססמא" : message)
                            .setPositiveButton("אישור", null)
                            .show();
                } else {
                    String userName = user_nameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean isCustomerExists = (app_db.customerDAO().checkCustomerExists(userName, password) > 0);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isCustomerExists) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Customer customer = app_db.customerDAO().getCustomerByNameAndPassword(userName, password);
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                                        intent.putExtra("customer", customer);
                                                        startActivity(intent);
                                                        getActivity().finish();
                                                    }
                                                });
                                            }
                                        }).start();
                                    } else {
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("פרטים שגויים")
                                                .setMessage("שם משתמש או ססמא לא נכונים")
                                                .setPositiveButton("אישור", null)
                                                .show();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        return view;
    }
}