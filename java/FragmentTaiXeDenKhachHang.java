package com.example.doan2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTaiXeDenKhachHang#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTaiXeDenKhachHang extends Fragment {
    //TODO: Truyền giá trị cho dtKey(mã/ keyđối tác) và cdKey(mã/key chuyến đi) qua hàm init()
    //Note: Mở activity khởi hành qua OnClickListener của btnGo

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
    private MapHelper mapHelper;
    private DoiTac dt;
    private String dtKey;
    private DoiTacDBHelper dtHelper;
    private Button btnGo;
    private DatabaseStatus<DoiTac> dtStatus;
    private ChuyenDiDBHelper cdHelper;
    private ChuyenDi cd;
    private String cdKey;//Lay ma chuyen di tu activity truoc de truyen vao day
    private DatabaseStatus<ChuyenDi> cdStatus;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
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
        btnGo=view.findViewById(R.id.KhoiHanh_btnGo);
        mapHelper= fragmentMap.getMapHelper();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.TXKH_btnSetStartPoint:
                        if(!etCurrentPos.getText().toString().isEmpty())
                        {
                            MapHelper.OnPostGettingAddressJob onPostGettingAddressJob =new MapHelper.OnPostGettingAddressJob() {
                                @Override
                                public void updateCurrentLocation(Place place) {
                                    etCurrentPos.setText(place.getAddress());
                                    mapHelper.setStartMarker();
                                    setDTPlace(place);
                                }
                            };
                            getLocationFromAddress(onPostGettingAddressJob,etCurrentPos);
                            return;
                        }
                        if(mapHelper.hasPlacedMarker()) {
                            etCurrentPos.setText(mapHelper.getCurrentPlace().getAddress());
                            mapHelper.setStartMarker();
                        }
                        setDTPlace(mapHelper.getCheckpoints()[0]);
                        break;
                    case R.id.TXKH_btnSetDestinationPoint:
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
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd.setThoiGianBatDau(getCurrentDateTime());
                cdHelper.update(cdStatus,cdKey,cd);
                //TODO: Gọi Activity khởi hành ở chỗ này

            }
        });
        return view;
    }
    private void init()
    {
        dtKey="-Mcd50fHM85Zg10vEfc9";
        cdKey="-McnW4k3g97pFb6REnhe";
        dtHelper=new DoiTacDBHelper();
        dtStatus=new DatabaseStatus<DoiTac>() {
            @Override
            public void doWhenInserted() {

            }

            @Override
            public void doWhenRead(ArrayList<DoiTac> data, ArrayList<String> keys) {
                for(int i=0;i<keys.size();i++)
                    if(keys.get(i).equals(dtKey))
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
        dtHelper.read(dtStatus);
        cdHelper=new ChuyenDiDBHelper();
        cdStatus=new DatabaseStatus<ChuyenDi>() {
            @Override
            public void doWhenInserted() {

            }

            @Override
            public void doWhenRead(ArrayList<ChuyenDi> data, ArrayList<String> keys) {
                for(int i=0;i<keys.size();i++)
                    if(keys.get(i).equals(cdKey))
                    {
                        cd=data.get(i);
                        Place destination= new Place(Double.valueOf(cd.getViDoDiemBatDau()),Double.valueOf(cd.getKinhDoDiemBatDau()),cd.getDiemBatDau());
                        mapHelper.setCurrentPlace(destination);
                        mapHelper.setDestinationMarker();
                        mapHelper.moveCameraToCurrentMarker();
                        etDestinationPos.setText(cd.getDiemBatDau());
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
        cdHelper.read(cdStatus);
    }
    private void getLocationFromAddress(final MapHelper.OnPostGettingAddressJob onPostGettingAddressJob, EditText et)
    {
        String searchString=et.getText().toString();
        mapHelper.getLocationFromAddress(onPostGettingAddressJob,searchString);
    }
    private void setDTPlace(Place place)
    {
        dt.setViDoHienTai(String.valueOf(place.getLatitude()));
        dt.setKinhDoHienTai(String.valueOf(place.getLongitude()));
        dtHelper.update(dtStatus, dtKey,dt);
    }
    private String getCurrentDateTime()
    {
        SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date=new Date();
        return formatter.format(date);
    }
}