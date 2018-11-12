package com.longnh.mobile.mininow;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeInformationActivity extends AppCompatActivity {
    Uri selectedUriImage;
    private EditText fullName, address, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);
        innitView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public void changeAvat(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 200);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == RESULT_OK) {
            selectedUriImage = data.getData();
            ImageView avat = findViewById(R.id.avatar);
            avat.setImageURI(selectedUriImage);

        }
    }

    private void innitView() {
        fullName = findViewById(R.id.fullName);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
    }

    private boolean checkConfirm() {
        if (fullName.getText().toString().trim().equals("")
                || address.getText().toString().trim().equals("")
                || phone.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Không được để trống hoặc khoảng trống !!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void clickToUpdate(View view) {
        if (checkConfirm()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);

        } else
            Toast.makeText(this, "Nhập lại !!!", Toast.LENGTH_SHORT).show();
    }

}
