package com.nonvoid.barcrawler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.adapter.BreweryListAdapter;
import com.nonvoid.barcrawler.model.Brewery;
import com.nonvoid.barcrawler.model.BreweryLocation;
import com.nonvoid.barcrawler.util.IntentTags;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Matt on 5/11/2017.
 */

public class BreweryMapFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.brewery_mapview)
    MapView mapView;

//    private BreweryLocation location;
    private List<BreweryLocation> locationList;
    private List<Marker> markerList = new ArrayList<>();

    public static BreweryMapFragment newInstance(BreweryLocation location){
        ArrayList<BreweryLocation> list = new ArrayList<>();
        list.add(location);
        return newInstance(list);
    }

    public static BreweryMapFragment newInstance(ArrayList<BreweryLocation> breweryLocations) {
        BreweryMapFragment fragment = new BreweryMapFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(IntentTags.BREWERY_ITEM, breweryLocations);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationList = getArguments().getParcelableArrayList(IntentTags.BREWERY_ITEM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brewery_map_fragment, container, false);
        ButterKnife.bind(this, view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(!locationList.isEmpty()){
            if(locationList.size()==1){
                //show details of one location
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(locationList.get(0).getLatLng());
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_location));

                Marker marker = googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));
                markerList.add(marker);
            }else {
                //put all location marker on the map
                LatLngBounds.Builder builder = LatLngBounds.builder();
                for(BreweryLocation location : locationList){
                    builder.include(location.getLatLng());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(location.getLatLng());
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_location));
                    markerOptions.title(location.getName());
                    Marker marker = googleMap.addMarker(markerOptions);
                    markerList.add(marker);
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 10));
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mapView!=null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}
