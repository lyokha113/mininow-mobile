package com.longnh.mobile.mininow;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.longnh.mobile.mininow.entity.Customer;
import com.longnh.mobile.mininow.model.CustomerService;
import com.longnh.mobile.mininow.model.VolleyCallback;
import com.longnh.mobile.mininow.ultils.ConstantManager;

import org.json.JSONException;

import androidx.annotation.NonNull;
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

            FirebaseStorage storageRef = FirebaseStorage.getInstance();
            StorageReference ref = storageRef.getReference().child("CU-" + ConstantManager.customer.getId() + selectedUriImage.getPathSegments());
            UploadTask uploadTask = ref.putFile(selectedUriImage);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(ChangeInformationActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    avat.setImageURI(selectedUriImage);
                }
            });
        }
    }

    private void innitView() {
        fullName = findViewById(R.id.fullName);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);

        fullName.setText(ConstantManager.customer.getName());
        address.setText(ConstantManager.customer.getAddress());
        phone.setText(ConstantManager.customer.getPhone());
    }

    private boolean checkConfirm() {
        if (fullName.getText().toString().trim().equals("")
                || address.getText().toString().trim().equals("")
                || phone.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void clickToUpdate(View view) throws JSONException {
        if (checkConfirm()) {
            Customer customer = new Customer();
            customer.setId(ConstantManager.customer.getId());
            customer.setName(fullName.getText().toString());
            customer.setAddress(address.getText().toString());
            customer.setPhone(phone.getText().toString());
            customer.setImgURL("");
            CustomerService.updateCustomer(getApplicationContext(), customer, data -> {
                Customer customer1 = (Customer) data;
                ConstantManager.customer = customer1;
                Toast.makeText(ChangeInformationActivity.this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();

            });
        }
    }

}
