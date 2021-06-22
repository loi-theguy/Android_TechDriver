package com.example.doan2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentKhoiHanh#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentKhoiHanh extends Fragment {
    //TODO: truyền giá trị cho cdKey(mã/key của chuyến đi được tạo trong activity trước) qua hàm init()
    private FragmentMap fragmentMap;
    private EditText etCurrentPos, etDestinationPos;
    private ImageView btnSetStartPoint, btnSetDestinationPoint;
    private Button btnConfirm;
    private String cdKey;
    private ChuyenDi cd;
    private Place[] checkpoints=new Place[2];
    private ChuyenDiDBHelper cdHelper;
    private DatabaseStatus<ChuyenDi> cdStatus;
    private MapHelper mapHelper;
    private boolean hasLoaded=false;
    private String dtKey;
    private DoiTacDBHelper dtHelper;
    private DatabaseStatus<DoiTac> dtStatus;
    private DoiTac dt;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentKhoiHanh() {
        // Required empty public constructor
        fragmentMap =new FragmentMap();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentKhoiHanh.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentKhoiHanh newInstance(String param1, String param2) {
        FragmentKhoiHanh fragment = new FragmentKhoiHanh();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_khoi_hanh, container, false);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.KhoiHanh_mapPlacer,(Fragment) fragmentMap)
                .commit();
        etCurrentPos=view.findViewById(R.id.KhoiHanh_etCurrentPos);
        etDestinationPos = view.findViewById(R.id.KhoiHanh_etDestPos);
        btnSetStartPoint=view.findViewById(R.id.KhoiHanh_btnSetStartPoint);
        btnSetDestinationPoint=view.findViewById(R.id.KhoiHanh_btnSetDestinationPoint);
        btnConfirm=view.findViewById(R.id.KhoiHanh_btnXacNhan);
        mapHelper=fragmentMap.getMapHelper();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.KhoiHanh_btnSetStartPoint:
                        if(!etCurrentPos.getText().toString().isEmpty())
                        {
                            MapHelper.OnPostGettingAddressJob onPostGettingAddressJob =new MapHelper.OnPostGettingAddressJob() {
                                @Override
                                public void updateCurrentLocation(Place place) {
                                    etCurrentPos.setText(place.getAddress());
                                    mapHelper.setStartMarker();
                                }
                            };
                            getLocationFromAddress(onPostGettingAddressJob,etCurrentPos);
                            return;
                        }
                        if(mapHelper.hasPlacedMarker()) {
                            etCurrentPos.setText(mapHelper.getCurrentPlace().getAddress());
                            mapHelper.setStartMarker();
                        }
                        break;
                    case R.id.KhoiHanh_btnSetDestinationPoint:
                        if(!etDestinationPos.getText().toString().isEmpty())
                        {
                            MapHelper.OnPostGettingAddressJob onPostGettingAddressJob =new MapHelper.OnPostGettingAddressJob() {
                                @Override
                                public void updateCurrentLocation(Place place) {
                                    etDestinationPos.setText(place.getAddress());
                                    mapHelper.setDestinationMarker();
                                }
                            };
                            getLocationFromAddress(onPostGettingAddressJob,etDestinationPos);
                            return;
                        }
                        if(mapHelper.hasPlacedMarker()){
                            etDestinationPos.setText(mapHelper.getCurrentPlace().getAddress());
                            mapHelper.setDestinationMarker();
                        }
                        break;
                }
            }
        };

        btnSetStartPoint.setOnClickListener(listener);
        btnSetDestinationPoint.setOnClickListener(listener);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd.setThoiGianKetThuc(getCurrentDateTime());
                cd.setTrangThai(ChuyenDi.HOAN_THANH);
                cdHelper.update(cdStatus,cdKey,cd);
                updateDoiTacStatus();
                Toast.makeText(getContext(), getString(R.string.ride_completed),Toast.LENGTH_LONG).show();
            }
        });
        return  view;
    }
    private void init()
    {
        cdKey="-McnW4k3g97pFb6REnhe";
        cdStatus=new DatabaseStatus<ChuyenDi>() {
            @Override
            public void doWhenInserted() {

            }

            @Override
            public void doWhenRead(ArrayList<ChuyenDi> data, ArrayList<String> keys) {
                if (hasLoaded) return;
                for (int i=0;i<keys.size();i++)
                    if(keys.get(i).equals(cdKey))
                    {
                        cd=data.get(i);
                        checkpoints[0]= new Place(Double.valueOf(cd.getViDoDiemBatDau()),Double.valueOf(cd.getKinhDoDiemBatDau()),cd.getDiemBatDau());
                        checkpoints[1]= new Place(Double.valueOf(cd.getViDoDiemDen()),Double.valueOf(cd.getKinhDoDiemDen()),cd.getDiemDen());
                        mapHelper.setCurrentPlace(checkpoints[1]);
                        etDestinationPos.setText(checkpoints[1].getAddress());
                        mapHelper.setDestinationMarker();
                        mapHelper.setCurrentPlace(checkpoints[0]);
                        etCurrentPos.setText(checkpoints[0].getAddress());
                        mapHelper.setStartMarker();
                        dtKey=cd.getMaDoiTac();
                        dtHelper.read(dtStatus);
                        hasLoaded=true;
                        return;
                    }
            }

            @Override
            public void doWhenUpdated() {

            }

            @Override
            public void doWhenDeleted() {

            }
        };
        cdHelper=new ChuyenDiDBHelper();
        cdHelper.read(cdStatus);
        dtStatus=new DatabaseStatus<DoiTac>() {
            @Override
            public void doWhenInserted() {

            }

            @Override
            public void doWhenRead(ArrayList<DoiTac> data, ArrayList<String> keys) {
                for (int i = 0; i < keys.size(); i++)
                    if (keys.get(i).equals(dtKey))
                    {
                        dt=data.get(i);
                        return;
                    }
            }

            @Override
            public void doWhenUpdated() {

            }

            @Override
            public void doWhenDeleted() {

            }
        };
        dtHelper=new DoiTacDBHelper();

    }
    private void getLocationFromAddress(final MapHelper.OnPostGettingAddressJob onPostGettingAddressJob, EditText et)
    {
        String searchString=et.getText().toString();
        mapHelper.getLocationFromAddress(onPostGettingAddressJob,searchString);
    }
    private String getCurrentDateTime()
    {
        SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date=new Date();
        return formatter.format(date);
    }
    private void updateDoiTacStatus()
    {
        dt.setDangBan(DoiTac.KHONG_BAN);
        dtHelper.update(dtStatus,dtKey,dt);
    }
}