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
import com.longnh.mobile.mininow.model.Customer;
import com.longnh.mobile.mininow.service.CustomerService;
import com.longnh.mobile.mininow.ultils.UserSession;

import org.json.JSONException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ChangeInformationActivity extends AppCompatActivity {

    Uri selectedUriImage;
    private EditText fullName, address, phone;
    private Customer current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);
        UserSession session = new UserSession(getApplicationContext(), UserSession.UserSessionType.CUSTOMER);
        current = session.getCustomerDetails();
        innitView();
    }

    public void changeImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 200);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == RESULT_OK) {
            selectedUriImage = data.getData();
            ImageView avat = findViewById(R.id.avatar);

            FirebaseStorage storageRef = FirebaseStorage.getInstance();
            StorageReference ref = storageRef.getReference().child("CU-" + current.getId() + selectedUriImage.getPathSegments());
            UploadTask uploadTask = ref.putFile(selectedUriImage);
            uploadTask.addOnFailureListener(exception -> Toast.makeText(ChangeInformationActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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

        fullName.setText(current.getName());
        address.setText(current.getAddress());
        phone.setText(current.getPhone());
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
            customer.setId(current.getId());
            customer.setName(fullName.getText().toString());
            customer.setAddress(address.getText().toString());
            customer.setPhone(phone.getText().toString());
            customer.setImgURL("");
            CustomerService.updateCustomer(getApplicationContext(), customer, data -> {
                Customer customer1 = (Customer) data;
                current = customer1;
                Toast.makeText(ChangeInformationActivity.this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();

            });
        }
    }

}
