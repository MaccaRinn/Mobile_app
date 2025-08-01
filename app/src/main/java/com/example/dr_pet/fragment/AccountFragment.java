package com.example.dr_pet.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dr_pet.model.Account;
import com.example.dr_pet.R;
import com.example.dr_pet.activity.HomeActivity;
import com.example.dr_pet.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.example.dr_pet.model.AuthManager;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //declare firebase and gg
    FirebaseAuth auth;



    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // compulsory to use firebase or gg authentication
        auth = FirebaseAuth.getInstance();




        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Button btnLogout = view.findViewById(R.id.btn_logout);
        Button btnSave = view.findViewById(R.id.btn_save);
        EditText edtFName = view.findViewById(R.id.edtFName);
        EditText edtLName = view.findViewById(R.id.edtLName);
        EditText edtAdr = view.findViewById(R.id.edtAdr);
        EditText edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        TextView txtEmail = view.findViewById(R.id.txtEmail);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null){
            FirebaseDatabase.getInstance().getReference("Account")
                    .child(uid)
                    .get()
                    .addOnSuccessListener(dataSnapshot -> {
                        if(dataSnapshot.exists()){
                            Account account = dataSnapshot.getValue(Account.class);
                            if (account != null){
                                txtEmail.setText(auth.getCurrentUser().getEmail());
                                edtAdr.setText(account.getAddress());
                                edtFName.setText(account.getFirstName());
                                edtLName.setText(account.getLastName());
                            }
                            else{
                                Toast.makeText(getActivity(), "Invalid Data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Error when get value" + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                AuthManager.setLoggedIn(getActivity(), false);
                startActivity(new Intent(getActivity(), HomeActivity.class));
                Toast.makeText(getActivity(), "Log out", Toast.LENGTH_SHORT).show();
            }
        });


        //update to firebase
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account currAccount = new Account();
                String firstname = edtFName.getText().toString();
                String lastname = edtLName.getText().toString();
                String address = edtAdr.getText().toString();
                String phoneNumber = edtPhoneNumber.getText().toString();
                String uid = FirebaseAuth.getInstance().getUid();


                Map<String, Object> updateFields = new HashMap<>();
                if (!firstname.isEmpty()) {
                    updateFields.put("firstName", firstname);
                }
                if (!lastname.isEmpty()) {
                    updateFields.put("lastName", lastname);
                }
                if (!address.isEmpty()) {
                    updateFields.put("address", address);
                }
                if (!phoneNumber.isEmpty()) {
                    updateFields.put("phoneNumber", phoneNumber);
                }

                FirebaseDatabase.getInstance().getReference("Account")
                        .child(uid)
                        .updateChildren(updateFields)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(getActivity(), "Account Detail has been updated", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error when saving: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            }
        });
        return view;
    }





}