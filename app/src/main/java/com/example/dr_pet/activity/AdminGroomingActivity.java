package com.example.dr_pet.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.R;
import com.example.dr_pet.adapter.AdminGroomingAdapter;
import com.example.dr_pet.adapter.GroomingAdapter;
import com.example.dr_pet.model.Grooming;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminGroomingActivity extends AppCompatActivity {

    RecyclerView groomingListRe;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_grooming);
        groomingListRe = findViewById(R.id.groomingListRe);
        groomingListRe.setLayoutManager(new LinearLayoutManager(this));

        //load grooming list
        loadPendingGroomings();
    }

    private void loadPendingGroomings() {
        List<com.example.dr_pet.model.Grooming> pendingList = new ArrayList<>();

        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Account");

        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String phone = userSnapshot.child("phoneNumber").getValue(String.class);
                    DataSnapshot groomingSnapshot = userSnapshot.child("service").child("grooming");
                    for (DataSnapshot groomingIdSnap : groomingSnapshot.getChildren()) {
                        String status = groomingIdSnap.child("status").getValue(String.class);
                        if ("pending".equalsIgnoreCase(status)) {
                            com.example.dr_pet.model.Grooming grooming = groomingIdSnap.getValue(Grooming.class);
                            grooming.setPhone(phone);
                            pendingList.add(grooming);
                        }
                    }
                }

                AdminGroomingAdapter adapter = new AdminGroomingAdapter(pendingList, new AdminGroomingAdapter.OnItemActionListener() {
                    @Override
                    public void onDeny(Grooming grooming) {
                        deleteGrooming(grooming);
                    }

                    @Override
                    public void onAccept(Grooming grooming) {
                        completeGrooming(grooming);
                    }

                    @Override
                    public void onCall(Grooming grooming) {
                        String phoneNumber = grooming.getPhone();
                        if (phoneNumber != null && !phoneNumber.isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + phoneNumber));
                            startActivity(intent);
                        }
                    }
                });

                groomingListRe.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }

    private void completeGrooming(Grooming grooming) {
        String targetId = grooming.getId();

        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Account");

        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot groomingList = userSnapshot.child("service").child("grooming");
                    for (DataSnapshot groomingItem : groomingList.getChildren()) {
                        if (groomingItem.getKey().equals(targetId)) {
                            groomingItem.getRef().child("status").setValue("complete");
                            Log.d("Update", "Đã cập nhật grooming ID " + targetId + " thành 'complete'");
                            break;
                        }
                    }
                }
                loadPendingGroomings();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Lỗi khi cập nhật grooming: " + error.getMessage());
            }
        });
    }


    private void deleteGrooming(Grooming grooming) {
        String targetId = grooming.getId();

        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Account");

        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot groomingList = userSnapshot.child("service").child("grooming");
                    for (DataSnapshot groomingItem : groomingList.getChildren()) {
                        if (groomingItem.getKey().equals(targetId)) {
                            groomingItem.getRef().child("status").setValue("Deny"); // Xoá ở đây
                            Log.d("Delete", "Xoá grooming id = " + targetId);
                            break;
                        }
                    }
                }
                loadPendingGroomings();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Lỗi xoá grooming: " + error.getMessage());
            }
        });
    }

}