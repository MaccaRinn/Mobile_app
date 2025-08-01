package com.example.dr_pet.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;
import android.widget.ImageButton;

import com.example.dr_pet.activity.MedicalActivity;
import com.example.dr_pet.model.Pet;
import com.example.dr_pet.R;
import com.example.dr_pet.activity.BoardingActivity;
import com.example.dr_pet.activity.GroomingActivity;
import com.example.dr_pet.adapter.PetAdapter;
import com.example.dr_pet.activity.ShopActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView petReView;

    private PetAdapter petAdapter;

    private List<Pet> petList;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                Intent intent = new Intent(getActivity(), GroomingActivity.class);
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