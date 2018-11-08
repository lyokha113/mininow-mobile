package com.longnh.mobile.mininow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.longnh.mobile.mininow.R;

import androidx.appcompat.app.AppCompatActivity;

public class DMKActivity extends AppCompatActivity {
    private EditText passNow,passNew,passNewConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmk);
        innitView();

    }
    private void innitView(){
        passNow = findViewById(R.id.passNow);
        passNew = findViewById(R.id.passNew);
        passNewConfirm = findViewById(R.id.passNewConfirm);
    }
    private boolean checkConfirm(){
        if(passNow.getText().toString().trim().equals("")
                ||passNew.getText().toString().trim().equals("")
                ||passNewConfirm.getText().toString().trim().equals("")){
            Toast.makeText(this, "Không được để trống hoặc khoảng trống !!!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!passNew.getText().equals(passNewConfirm.getText())){
            Toast.makeText(this, "Mật khẩu mới và mật khẩu xác nhận không trùng !!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void clickToUpdate(View view) {
        if (checkConfirm()){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);

        }else
            Toast.makeText(this, "Nhập lại !!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }
}
