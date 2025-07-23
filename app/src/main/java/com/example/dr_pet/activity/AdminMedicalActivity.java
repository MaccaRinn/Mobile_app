package com.example.dr_pet.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.R;
import com.example.dr_pet.adapter.AdminMedicalAdapter;
import com.example.dr_pet.model.Medical;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminMedicalActivity extends AppCompatActivity {

    RecyclerView medicalListRe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_medical);
        medicalListRe = findViewById(R.id.medicalListRe);
        medicalListRe.setLayoutManager(new LinearLayoutManager(this));

        loadPendingMedicals();



    }


    private void loadPendingMedicals() {
        List<com.example.dr_pet.model.Medical> pendingList = new ArrayList<>();

        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Account");

        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String phone = userSnapshot.child("phoneNumber").getValue(String.class);
                    DataSnapshot medicalSnapshot = userSnapshot.child("service").child("medical");  // Changed to medical
                    for (DataSnapshot medicalIdSnap : medicalSnapshot.getChildren()) {
                        String status = medicalIdSnap.child("status").getValue(String.class);
                        if ("pending".equalsIgnoreCase(status)) {
                            com.example.dr_pet.model.Medical medical = medicalIdSnap.getValue(Medical.class);
                            medical.setPhone(phone);
                            pendingList.add(medical);
                        }
                    }
                }

                AdminMedicalAdapter adapter = new AdminMedicalAdapter(pendingList, new AdminMedicalAdapter.OnItemActionListener() {
                    @Override
                    public void onDenyM(Medical medical) {
                        deleteMedical(medical);
                    }

                    @Override
                    public void onAcceptM(Medical medical) {
                        completeMedical(medical);
                    }

                    @Override
                    public void onCallM(Medical medical) {
                        String phoneNumber = medical.getPhone();
                        if (phoneNumber != null && !phoneNumber.isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + phoneNumber));
                            startActivity(intent);
                        }
                    }
                });

                medicalListRe.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
            }
        });



    }


    private void completeMedical(Medical medical) {
        String targetId = medical.getAppointmentId();

        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Account");

        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot medicalList = userSnapshot.child("service").child("medical");  // Changed to medical
                    for (DataSnapshot medicalItem : medicalList.getChildren()) {
                        if (medicalItem.getKey().equals(targetId)) {
                            medicalItem.getRef().child("status").setValue("complete");
                            Log.d("Update", "Đã cập nhật medical ID " + targetId + " thành 'complete'");
                            break;
                        }
                    }
                }
                loadPendingMedicals();  // Load pending medicals after update
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Lỗi khi cập nhật medical: " + error.getMessage());
            }
        });
    }


    private void deleteMedical(Medical medical) {
        String targetId = medical.getAppointmentId();

        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Account");

        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot medicalList = userSnapshot.child("service").child("medical");  // Changed to medical
                    for (DataSnapshot medicalItem : medicalList.getChildren()) {
                        if (medicalItem.getKey().equals(targetId)) {
                            medicalItem.getRef().child("status").setValue("Deny"); // Deny instead of deletion
                            Log.d("Delete", "Xoá medical id = " + targetId);
                            break;
                        }
                    }
                }
                loadPendingMedicals();  // Load pending medicals after update
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Lỗi xoá medical: " + error.getMessage());
            }
        });
    }




}