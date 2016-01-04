package com.study.john.mymaptest;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    double latitude, longitude;
    // 위치정보 확인 및 관리를 위한 객체
    LocationManager l_manager;
    Location location;

    //위도와 경도를 저장하는 포지션객체 LatLng
    static LatLng MY_POSITION = null;
    Marker marker = null;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        l_manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        location =
                l_manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //NETWORK_PROVIDER가 안되면 GPS_PROVIDER로 실행해본다.

        //Location객체를 통해 위도와 경도를 얻어온다.
        latitude = location.getLatitude();//위도
        longitude = location.getLongitude();//경도

        MY_POSITION = new LatLng(latitude, longitude);

        map =
                ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        marker = map.addMarker(new MarkerOptions().position(MY_POSITION));

        //아래의 getAddress메서드를 통해 현재 위도와 경도를 주소로 변경한 후 마커에 출력
        marker.setTitle(getAddress(latitude, longitude));

        //1.위도, 경도 정보
        //2.축척1 ~ 21(클수록 확대범위가 커짐.)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(MY_POSITION, 18.5f));

    }//onCreate()

    //위도와 경도 기반으로 주소를 리턴하는 메서드
    public String getAddress(double lat, double lng) {
        String address = null;

        // 위치정보를 활용하기 위한 구글 API 객체
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // 주소 목록을 담기 위한 List
        List<Address> list = null;

        try {
            // 주소 목록을 가져온다. --> 위도,경도,조회 갯수
            list = geocoder.getFromLocation(lat, lng, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (list == null) {
            Toast.makeText(getApplicationContext(),
            "주소데이터 얻기 실패", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (list.size() > 0) {
            // getFromLocation() 메서드에서 결과를 하나만 요청했기 때문에,
            // 반복처리는 필요 없다.
            Address addr = list.get(0);
            address = addr.getCountryName() + " "
                    + addr.getAdminArea() + " "
                    + addr.getLocality() + " "
                    + addr.getThoroughfare() + " "
                    + addr.getFeatureName();
        }
        return address;
    }
}
