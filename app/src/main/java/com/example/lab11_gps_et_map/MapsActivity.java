package com.example.lab11_gps_et_map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lab11_gps_et_map.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Marker currentMarker;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 1) LocationManager permet d'écouter la localisation
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 2) Marker initial (exemple)
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        Toast.makeText(getApplicationContext(), "Map Ready", Toast.LENGTH_SHORT).show();

        // 3) Vérifier permission runtime
        startLocationUpdates(locationManager);
    }

    private void startLocationUpdates(LocationManager locationManager) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            
            // 8) Si pas de permission : la demander
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
            return;
        }

        // 4) Demander des mises à jour de position via NETWORK_PROVIDER
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000,   // minTime = 1 seconde
                0,      // minDistance = 0 mètres pour les tests
                new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        updateMarker(location);
                    }

                    @Override
                    public void onProviderDisabled(@NonNull String provider) {
                        // Si le provider est désactivé, proposer d'activer GPS
                        buildAlertMessageNoGps();
                    }
                }
        );
    }

    private void updateMarker(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // 5) Afficher un toast (debug)
        Toast.makeText(getApplicationContext(), latitude + " " + longitude, Toast.LENGTH_SHORT).show();

        LatLng position = new LatLng(latitude, longitude);

        // Partie 6 : Version “propre” (1 seul marker qui bouge)
        if (currentMarker == null) {
            currentMarker = mMap.addMarker(new MarkerOptions().position(position).title("Ma Position"));
        } else {
            currentMarker.setPosition(position);
        }

        // 7) Zoomer et centrer sur cette position (recommandé avec animation)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15.0f));
    }

    // Étape 8 — Ajouter buildAlertMessageNoGps()
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    // Étape 9-bis — Ajouter onRequestPermissionsResult
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission accordée", Toast.LENGTH_SHORT).show();
                // Relancer la logique de localisation
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                startLocationUpdates(locationManager);
            } else {
                Toast.makeText(this, "Permission refusée", Toast.LENGTH_LONG).show();
            }
        }
    }
}