package com.example.doan2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTaiXeDenKhachHang#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTaiXeDenKhachHang extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentMap fragmentMap;
    private EditText etCurrentPos, etDestinationPos;
    private ImageView btnSetStartPoint, btnSetDestinationPoint;

    public FragmentTaiXeDenKhachHang() {
        // Required empty public constructor
        fragmentMap =new FragmentMap();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTaiXeDenKhachHang.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTaiXeDenKhachHang newInstance(String param1, String param2) {
        FragmentTaiXeDenKhachHang fragment = new FragmentTaiXeDenKhachHang();
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
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.TXKH_mapPlacer,(Fragment) fragmentMap)
                .commit();
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tai_xe_den_khach_hang, container, false);

        etCurrentPos=view.findViewById(R.id.TXKH_etCurrentPos);
        etDestinationPos = view.findViewById(R.id.TXKH_etDestPos);
        btnSetStartPoint=view.findViewById(R.id.TXKH_btnSetStartPoint);
        btnSetDestinationPoint=view.findViewById(R.id.TXKH_btnSetDestinationPoint);
        MapHelper mapHelper= fragmentMap.getMapHelper();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.TXKH_btnSetStartPoint:
                        if (mapHelper.hasPlacedMarker()) {
                            etCurrentPos.setText(String.valueOf(mapHelper.getCurrentLatitude()) + "," + String.valueOf(mapHelper.getCurrentLongitude()));
                            mapHelper.setStartMarker();
                        }
                        break;
                    case R.id.TXKH_btnSetDestinationPoint:
                        if (mapHelper.hasPlacedMarker()) {
                            etDestinationPos.setText(String.valueOf(mapHelper.getCurrentLatitude()) + "," + String.valueOf(mapHelper.getCurrentLongitude()));
                            mapHelper.setDestinationMarker();
                        }
                        break;
                }
            }
        };
        btnSetStartPoint.setOnClickListener(listener);
        btnSetDestinationPoint.setOnClickListener(listener);
        return view;
    }
}