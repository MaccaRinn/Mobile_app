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
import com.example.dr_pet.adapter.AdminBoardingAdapter;
import com.example.dr_pet.adapter.AdminGroomingAdapter;
import com.example.dr_pet.model.Boarding;
import com.example.dr_pet.model.Grooming;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminBoardingActivity extends AppCompatActivity {

    private RecyclerView boardingListRe;

    private List<Boarding> boardingList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_boarding);
        boardingListRe = findViewById(R.id.boardingListRe);
        boardingListRe.setLayoutManager(new LinearLayoutManager(this));
        loadPendingBoarding();



    }


    private void loadPendingBoarding() {
        List<Boarding> pendingList = new ArrayList<>();

        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Account");

        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String phone = userSnapshot.child("phoneNumber").getValue(String.class);
                    DataSnapshot boardingSnapshot = userSnapshot.child("service").child("boarding");
                    for (DataSnapshot boardingIdSnap : boardingSnapshot.getChildren()) {
                        String status = boardingIdSnap.child("status").getValue(String.class);
                        if ("pending".equalsIgnoreCase(status)) {
                            Boarding boarding = boardingIdSnap.getValue(Boarding.class);
                            boarding.setPhone(phone);
                            pendingList.add(boarding);
                        }
                    }
                }

                AdminBoardingAdapter adapter = new AdminBoardingAdapter(pendingList, new AdminBoardingAdapter.OnItemActionListener() {
                    @Override
                    public void onDenyB(Boarding boarding) {
                        deleteBoarding(boarding);
                    }

                    @Override
                    public void onAcceptB(Boarding boarding) {
                        completeBoarding(boarding);
                    }

                    @Override
                    public void onCallB(Boarding boarding) {
                        String phoneNumber = boarding.getPhone();
                        if (phoneNumber != null && !phoneNumber.isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + phoneNumber));
                            startActivity(intent);
                        }
                    }
                });

                boardingListRe.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }

    private void deleteBoarding(Boarding boarding) {
        String targetId = boarding.getId();

        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Account");

        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot boardingList = userSnapshot.child("service").child("boarding");
                    for (DataSnapshot boardingItem : boardingList.getChildren()) {
                        if (boardingItem.getKey().equals(targetId)) {
                            boardingItem.getRef().child("status").setValue(""); // Xoá ở đây
                            Log.d("Delete", "Xoá boarding id = " + targetId);
                            break;
                        }
                    }
                }
                loadPendingBoarding();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Lỗi xoá boarding: " + error.getMessage());
            }
        });
    }


    private void completeBoarding(Boarding boarding) {
        String targetId = boarding.getId();

        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Account");

        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot boardingList = userSnapshot.child("service").child("boarding");
                    for (DataSnapshot boardingItem : boardingList.getChildren()) {
                        if (boardingItem.getKey().equals(targetId)) {
                            boardingItem.getRef().child("status").setValue("complete");
                            Log.d("Update", "Đã cập nhật boarding ID " + targetId + " thành 'complete'");
                            break;
                        }
                    }
                }
                loadPendingBoarding();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Lỗi khi cập nhật boarding: " + error.getMessage());
            }
        });
    }


}