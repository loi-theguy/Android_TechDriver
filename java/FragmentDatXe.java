package com.example.doan2;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDatXe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDatXe extends Fragment {

    private Context context;
    private FragmentMap fragmentMap;
    private ImageView btnSetStartingPoint, btnSetDestinationPoint;
    private EditText etCurrentPos, etDestPos;
    private TextView tvKilometer, tvPrice;
//    SharedPreferences preferences= getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentDatXe() {
        // Required empty public constructor
        fragmentMap =new FragmentMap();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDatXe.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDatXe newInstance(String param1, String param2) {
        FragmentDatXe fragment = new FragmentDatXe();
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
        View view=inflater.inflate(R.layout.fragment_dat_xe, container, false);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.DatXe_mapPlacer,(Fragment) fragmentMap)
                .commit();
        context=view.getContext();
        btnSetDestinationPoint = view.findViewById(R.id.DatXe_btnSetDestinationPoint);
        btnSetStartingPoint = view.findViewById(R.id.DatXe_btnSetStartingPoint);
        etCurrentPos = view.findViewById(R.id.DatXe_etCurrentPos);
        etDestPos = view.findViewById(R.id.DatXe_etDestPos);
        tvKilometer = view.findViewById(R.id.DatXe_tvKilometer);
        tvPrice = view.findViewById(R.id.DatXe_tvPrice);
        MapHelper mapHelper=fragmentMap.getMapHelper();
        try{
            mapHelper.setKilometersDisplayingControl(tvKilometer);
        } catch(Exception e)
        {
            e.printStackTrace();
        }
//        fragmentMap.enableKmDisplaying();
        mapHelper.setPricesDisplayingControl(tvPrice);
//        fragmentMap.enablePriceDisplaying();
        View.OnClickListener listener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=v.getId();
                switch(id) {
                    case R.id.DatXe_btnSetStartingPoint:
                        if(mapHelper.hasPlacedMarker()) {
                            etCurrentPos.setText(String.valueOf(mapHelper.getCurrentLatitude()) + "," + String.valueOf(mapHelper.getCurrentLongitude()));
                            mapHelper.setStartMarker();
                        }
                        break;
                    case R.id.DatXe_btnSetDestinationPoint:
                        if(mapHelper.hasPlacedMarker()){
                            etDestPos.setText(String.valueOf(mapHelper.getCurrentLatitude()) + "," + String.valueOf(mapHelper.getCurrentLongitude()));
                            mapHelper.setDestinationMarker();
                        }
                        break;
                }
            }
        };
        btnSetStartingPoint.setOnClickListener(listener);
        btnSetDestinationPoint.setOnClickListener(listener);
        return view;
    }
}