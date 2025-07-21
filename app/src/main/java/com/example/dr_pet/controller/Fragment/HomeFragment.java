package com.example.dr_pet.controller.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;
import android.widget.ImageButton;

import com.example.dr_pet.Model.Pet;
import com.example.dr_pet.R;
import com.example.dr_pet.controller.activity.BoardingActivity;
import com.example.dr_pet.controller.activity.Grooming;
import com.example.dr_pet.controller.activity.MedicalActivity;
import com.example.dr_pet.controller.adapter.PetAdapter;
import com.example.dr_pet.controller.activity.ShopActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView petReView;

    private PetAdapter petAdapter;

    private List<Pet> petList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        petReView = view.findViewById(R.id.petRecylceViewHome);
        petReView.setLayoutManager(new GridLayoutManager(getContext(),2));

        petList = new ArrayList<>();
        petAdapter = new PetAdapter(petList, getContext());
        petReView.setAdapter(petAdapter);


        ImageButton btnGrooming = view.findViewById(R.id.btn_grooming);
        btnGrooming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Grooming.class);
                startActivity(intent);
            }
        });

        ImageButton btnMedical = view.findViewById(R.id.btn_medical);
        btnMedical.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MedicalActivity.class);
            startActivity(intent);
        });

        ImageButton btnBoarding = view.findViewById(R.id.btn_boarding);
        btnBoarding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BoardingActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btnShop = view.findViewById(R.id.btn_service_shop);
        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopActivity.class);
                startActivity(intent);
            }
        });

        return view;
        //return inflater.inflate(R.layout.fragment_home, container, false);
    }
}