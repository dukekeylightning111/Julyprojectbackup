package com.example.mysportfriends_school_project;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapView;
    private Button confirmButton;
    private EditText placeEditText;

    private Geocoder geocoder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        confirmButton = view.findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    LatLng selectedLocation = googleMap.getCameraPosition().target;
                    googleMap.addMarker(new MarkerOptions().position(selectedLocation));

                    Bundle result = new Bundle();
                    String address = getAddressFromLocation(selectedLocation);

                    if (address == null) {
                        address = getLocationString(selectedLocation);
                    } else {
                        address = address + "";
                    }

                    if (selectedLocation.latitude == 0 && selectedLocation.longitude == 0) {
                        String place = placeEditText.getText().toString();
                        if (!place.isEmpty()) {
                            result.putString("selected_place", place);
                            getParentFragmentManager().setFragmentResult("map_result", result);
                            requireActivity().onBackPressed();
                        }
                    } else {
                        result.putParcelable("selected_location", selectedLocation);
                        result.putString("selected_address", address);
                        getParentFragmentManager().setFragmentResult("map_result", result);
                        requireActivity().onBackPressed();
                    }
                }
            }
        });

        geocoder = new Geocoder(requireContext(), Locale.getDefault());

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng israelLocation = new LatLng(31.0461, 34.8516);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(israelLocation, 8));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        if (googleMap != null) {
            LatLng location = new LatLng(0, 0);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void setFragmentResultListener(String resultKey, DesignSportActivity designSportActivity, FragmentResultListener fragmentResultListener) {
        getParentFragmentManager().setFragmentResultListener(resultKey, this, fragmentResultListener);
    }

    public String getAddressFromLocation(LatLng location) {
        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getLocationString(LatLng location) {
        return "Latitude: " + location.latitude + ", Longitude: " + location.longitude;
    }

}