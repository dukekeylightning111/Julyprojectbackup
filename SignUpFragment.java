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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class SignUpFragment extends Fragment {

    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText confirmPasswordEditText;
    private Button signUpBtn;
    private AppDatabase app_db;
    private String message = "";
    private String temp = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        app_db = AppDatabase.getDatabase(getActivity());

        userNameEditText = view.findViewById(R.id.user_name);
        passwordEditText = view.findViewById(R.id.password);
        signUpBtn = view.findViewById(R.id.signUP);
        emailEditText = view.findViewById(R.id.email);
        confirmPasswordEditText = view.findViewById(R.id.password_confirm);
        setupErrorValidation();
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSignUp();
            }
        });
        return view;
    }

    private void setupErrorValidation() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateUsername();
                validatePassword();
                validateConfirmPassword();
            }
        };
        userNameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        confirmPasswordEditText.addTextChangedListener(textWatcher);
    }

    private void validateUsername() {
        String username = userNameEditText.getText().toString();
        if (username.isEmpty()) {
            userNameEditText.setError("שם משתמש חסר, אנא מלאו חלק זה.");
        } else if (username.length() < 3) {
            userNameEditText.setError("שם משתמש צריך להכיל 3 תווים או יותר.");
        } else {
            userNameEditText.setError(null);
        }
    }

    private void validatePassword() {
        String password = passwordEditText.getText().toString();
        if (password.isEmpty()) {
            passwordEditText.setError("ססמא חסרה, אנא מלאו חלק זה.");
        } else if (password.length() < 3) {
            passwordEditText.setError("ססמא צריכה להכיל 3 תווים או יותר.");
        } else {
            passwordEditText.setError(null);
        }
    }

    private void validateConfirmPassword() {
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        if (password.isEmpty()) {
            passwordEditText.setError("ססמא חסרה, אנא מלאו חלק זה.");
        } else if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("אישור ססמא חסרה, אנא מלאו חלק זה.");
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("ססמא ואישור ססמא אינם מתאימים.");
        } else {
            confirmPasswordEditText.setError(null);
        }
    }

    private void performSignUp() {
        String userName = userNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        temp = "";

        if (userName.isEmpty()) {
            temp += "שם משתמש חסר\n";
        } else if (userName.length() <= 2) {
            temp += "שם משתמש קצר מדי, יש להזין 3 תווים או יותר\n";
        }

        if (password.isEmpty()) {
            temp += "ססמא חסרה\n";
        } else if (password.length() < 4) {
            temp += "ססמא קצרה מידי, יש להזין 4 תווים או יותר\n";
        }

        if (email.isEmpty()) {
            temp += "דואר אלקטרוני חסר\n";
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            temp += "דואר אלקטרוני אינו תקין\n";
        }

        if (confirmPassword.isEmpty()) {
            temp += "אישור ססמא חסרה\n";
        }

        if (!confirmPassword.equals(password)) {
            temp += "ססמא ואישור ססמא אינם תואמים\n";
        }

        if (!temp.isEmpty()) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(temp)
                    .setPositiveButton("אישור", null)
                    .show();
        }

        temp = "";
        if (password.length() >= 4 && password.equals(confirmPassword) && userName.length() > 2 && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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
                                                if (customer.email.equals(email)) {
                                                    new AlertDialog.Builder(getActivity())
                                                            .setMessage("דואר אלקטרוני זה כבר רשום")
                                                            .setPositiveButton("אישור", null)
                                                            .show();
                                                } else {
                                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                                    intent.putExtra("customer", customer);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                }).start();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("\nאלו הפרטים שמילאתם\n האם אתם בטוחים ליצירת משתמש?\n " + " שם משתמש: " + userName + "\n" + " דואר אלקטרוני: " + email)
                                        .setCancelable(false)
                                        .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Customer customer = new Customer(userName, password, email);
                                                Intent newIntent = new Intent(getActivity(), MainActivity.class);
                                                newIntent.putExtra("customer", customer);
                                                Toast.makeText(getActivity(), "שלום, ברוכים הבאים " + customer.getName(), Toast.LENGTH_LONG).show();
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        app_db.customerDAO().insert(customer);
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                startActivity(newIntent);
                                                            }
                                                        });
                                                    }
                                                }).start();
                                            }
                                        })
                                        .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                    });
                }
            }).start();
        }
    }
}
