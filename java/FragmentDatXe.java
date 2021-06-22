package com.example.doan2;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDatXe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDatXe extends Fragment {
    //TODO: truyền giá trị khKey(mã/key của khách hàng đã đăng nhập) qua hàm initData()
    private final double INFINITE=1e9;
    private Context context;
    private FragmentMap fragmentMap;
    private ImageView btnSetStartingPoint, btnSetDestinationPoint;
    private EditText etCurrentPos, etDestPos;
    private TextView tvKilometer, tvPrice;
    private Button btnPlaceOrder;
    private MapHelper mapHelper;
    private String khKey;
    private KhachHang kh;
    private KhachHangDBHelper khHelper;
    private DoiTacDBHelper dtHelper;
    private ChuyenDiDBHelper cdHelper;
    private DatabaseStatus<KhachHang> khStatus;
    private DatabaseStatus<DoiTac> dtStatus;
    private DoiTac doiTac;
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
        initData();
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
        btnPlaceOrder=view.findViewById(R.id.DatXe_btnPlaceOrder);

        mapHelper=fragmentMap.getMapHelper();
        mapHelper.setKilometersDisplayingControl(tvKilometer);
//        fragmentMap.enableKmDisplaying();
        mapHelper.setPricesDisplayingControl(tvPrice);
//        fragmentMap.enablePriceDisplaying();
        View.OnClickListener listener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=v.getId();
                switch(id) {
                    case R.id.DatXe_btnSetStartingPoint:
                        if(!etCurrentPos.getText().toString().isEmpty())
                        {
                            MapHelper.OnPostGettingAddressJob onPostGettingAddressJob =new MapHelper.OnPostGettingAddressJob() {
                                @Override
                                public void updateCurrentLocation(Place place) {
                                    etCurrentPos.setText(place.getAddress());
                                    mapHelper.setStartMarker();
                                    setKHPlace(place);
                                }
                            };
                            getLocationFromAddress(onPostGettingAddressJob,etCurrentPos);
                            return;
                        }
                        if(mapHelper.hasPlacedMarker()) {
                            etCurrentPos.setText(mapHelper.getCurrentPlace().getAddress());
                            mapHelper.setStartMarker();
                        }
                        setKHPlace(mapHelper.getCheckpoints()[0]);
                        break;
                    case R.id.DatXe_btnSetDestinationPoint:
                        if(!etDestPos.getText().toString().isEmpty())
                        {
                            MapHelper.OnPostGettingAddressJob onPostGettingAddressJob =new MapHelper.OnPostGettingAddressJob() {
                                @Override
                                public void updateCurrentLocation(Place place) {
                                    etDestPos.setText(place.getAddress());
                                    mapHelper.setDestinationMarker();
                                }
                            };
                            getLocationFromAddress(onPostGettingAddressJob,etDestPos);
                            return;
                        }
                        if(mapHelper.hasPlacedMarker()){
                            etDestPos.setText(mapHelper.getCurrentPlace().getAddress());
                            mapHelper.setDestinationMarker();
                        }
                        break;
                }
            }
        };
        btnSetStartingPoint.setOnClickListener(listener);
        btnSetDestinationPoint.setOnClickListener(listener);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double soDu, tongTien;
                String temp=tvPrice.getText().toString();
                tongTien=Double.valueOf(temp.substring(0,temp.length()-4));
                soDu=Double.valueOf(kh.getSoTien());
                if(soDu>=tongTien)
                {
                    //dat chuyen di
                    Toast.makeText(getContext(),getString(R.string.order_success),Toast.LENGTH_LONG).show();
                    kh.setSoTien(String.valueOf(soDu-tongTien));
                    khHelper.update(khStatus, khKey,kh);
                    String dtKey=getNearestDoiTac();
                    if(dtKey.isEmpty())
                    {
                        Toast.makeText(getContext(),getString(R.string.no_rider),Toast.LENGTH_LONG).show();
                        return;
                    }
                    updateDTStatus(dtKey,doiTac);
                    createChuyenDi(dtKey);
                    return;
                }
                Toast.makeText(getContext(), getString(R.string.not_enough_balance),Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private void getLocationFromAddress(final MapHelper.OnPostGettingAddressJob onPostGettingAddressJob, EditText et)
    {
        String searchString=et.getText().toString();
        mapHelper.getLocationFromAddress(onPostGettingAddressJob,searchString);
    }

    private void initData()
    {
        khKey ="-Mch5htwZf1jXrhQUzJ0";
        khHelper=new KhachHangDBHelper();
        //get data from KhachHang
        khStatus =new DatabaseStatus<KhachHang>() {
            @Override
            public void doWhenInserted() {

            }

            @Override
            public void doWhenRead(ArrayList<KhachHang> data, ArrayList<String> keys) {
                for(int i=0;i<keys.size();i++)
                    if(keys.get(i).equals(khKey))
                    {
                        kh=data.get(i);
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
        khHelper.read(khStatus);
        //get data from DoiTac
        dtHelper = new DoiTacDBHelper();
        dtStatus=new DatabaseStatus<DoiTac>() {
            @Override
            public void doWhenInserted() {

            }

            @Override
            public void doWhenRead(ArrayList<DoiTac> data, ArrayList<String> keys) {

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
    }
    private double getEuclerDistance(Place p1, Place p2)
    {
        return Math.sqrt(Math.pow(p1.getLatitude()-p2.getLatitude(),2)+Math.pow(p1.getLongitude()-p2.getLongitude(),2));
    }

    private String getNearestDoiTac()
    {
        ArrayList<DoiTac> doiTacs=dtHelper.getDoiTacs();
        ArrayList<String> dtKeys=dtHelper.getKeys();
        String key="";
        double min=INFINITE;
        doiTac=null;
        for(int i=0;i<dtKeys.size();i++)
        {
            Place pKH, pDT;
            try {
                pKH = new Place(Double.valueOf(kh.getViDoHienTai()), Double.valueOf(kh.getKinhDoHienTai()), "");
                pDT = new Place(Double.valueOf(doiTacs.get(i).getViDoHienTai()), Double.valueOf(doiTacs.get(i).getKinhDoHienTai()), "");
            }catch(NumberFormatException e)
            {
                continue;
            }
            double distance=getEuclerDistance(pKH,pDT);
            if(distance<min && doiTacs.get(i).getDangBan().equals(DoiTac.KHONG_BAN))
            {
                key=dtKeys.get(i);
                doiTac=doiTacs.get(i);
                min=distance;
            }
        }
        return key;
    }

    private void setKHPlace(Place place)
    {
        kh.setViDoHienTai(String.valueOf(place.getLatitude()));
        kh.setKinhDoHienTai(String.valueOf(place.getLongitude()));
        khHelper.update(new DatabaseStatus<KhachHang>() {
            @Override
            public void doWhenInserted() {

            }

            @Override
            public void doWhenRead(ArrayList<KhachHang> data, ArrayList<String> keys) {

            }

            @Override
            public void doWhenUpdated() {

            }

            @Override
            public void doWhenDeleted() {

            }
        },khKey,kh);
    }

    private void createChuyenDi(String dtKey)
    {
        ChuyenDi chuyenDi=new ChuyenDi();
        chuyenDi.setDiemBatDau(etCurrentPos.getText().toString());
        chuyenDi.setDiemDen(etDestPos.getText().toString());
        chuyenDi.setViDoDiemBatDau(kh.getViDoHienTai());
        chuyenDi.setKinhDoDiemBatDau(kh.getKinhDoHienTai());
        chuyenDi.setViDoDiemDen(String.valueOf(mapHelper.getCheckpoints()[1].getLatitude()));
        chuyenDi.setKinhDoDiemDen(String.valueOf(mapHelper.getCheckpoints()[1].getLongitude()));
        chuyenDi.setThoiGianBatDau(ChuyenDi.NOT_APPLICABLE);
        chuyenDi.setSoKm(tvKilometer.getText().toString());
        String giaTien=tvPrice.getText().toString();
        giaTien=giaTien.substring(0,giaTien.length()-4);
        chuyenDi.setGiaTien(giaTien);
        chuyenDi.setMaKhachHang(khKey);
        chuyenDi.setMaDoiTac(dtKey);
        chuyenDi.setTrangThai(ChuyenDi.CHUA_HOAN_THANH);
        chuyenDi.setThoiGianKetThuc(ChuyenDi.NOT_APPLICABLE);
        cdHelper.insert(new DatabaseStatus<ChuyenDi>() {
            @Override
            public void doWhenInserted() {

            }

            @Override
            public void doWhenRead(ArrayList<ChuyenDi> data, ArrayList<String> keys) {

            }

            @Override
            public void doWhenUpdated() {

            }

            @Override
            public void doWhenDeleted() {

            }
        },chuyenDi);
    }
    private void updateDTStatus(String dtKey, DoiTac doiTac)
    {
        doiTac.setDangBan(DoiTac.BAN);
        dtHelper.update(dtStatus, dtKey,doiTac);
    }
}