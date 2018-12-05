package com.longnh.mobile.mininow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.longnh.mobile.mininow.model.Customer;
import com.longnh.mobile.mininow.service.CustomerService;
import com.longnh.mobile.mininow.service.VolleyCallback;
import com.longnh.mobile.mininow.ultils.UserSession;

import androidx.appcompat.app.AppCompatActivity;

public class LoginCustomerActivity extends AppCompatActivity {

    public ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_customer);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        addControls();
        addEvents();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        setUser(currentUser);
    }

    private void addControls() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
    }

    private void addEvents() {
        login.setOnClickListener(v -> {
            String emailVal = email.getText().toString();
            String passwordVal = password.getText().toString();
            if (TextUtils.isEmpty(emailVal) || TextUtils.isEmpty(passwordVal)) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            } else {
                showProgressDialog();
                mAuth.signInWithEmailAndPassword(emailVal, passwordVal)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                setUser(user);
                            } else {
                                Toast.makeText(this, "Đăng nhập thất bại. Vui lòng kiểm tra thông tin",
                                        Toast.LENGTH_SHORT).show();
                            }
                            hideProgressDialog();
                        });
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Đang xử lý ...");
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    private void setUser(FirebaseUser user) {
        if (user != null) {
            CustomerService.getCustomerInfoByUID(this, user.getUid(), data -> {
                Customer customer = (Customer) data;
                UserSession session = new UserSession(getApplicationContext(), UserSession.UserSessionType.CUSTOMER);
                session.createCustomerSession(customer);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            });

        }
    }
}
