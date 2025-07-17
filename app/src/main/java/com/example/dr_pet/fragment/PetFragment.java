package com.example.dr_pet.fragment;

import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.dr_pet.R;
import com.example.dr_pet.adapter.PetAdapter;

import com.example.dr_pet.model.Pet;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView petRecylceView;
    private PetAdapter petAdapter;

    private List<Pet> petList;


    public PetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PetFragment newInstance(String param1, String param2) {
        PetFragment fragment = new PetFragment();
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

        View view  = inflater.inflate(R.layout.fragment_pet, container, false);
        petRecylceView = view.findViewById(R.id.petProfileRe);
        petRecylceView.setLayoutManager(new GridLayoutManager(getContext(),2));

        petList = new ArrayList<>();

//        petList.add(newPet);

        petAdapter = new PetAdapter(petList, getContext());
        petRecylceView.setAdapter(petAdapter);

        return view;
    }


}