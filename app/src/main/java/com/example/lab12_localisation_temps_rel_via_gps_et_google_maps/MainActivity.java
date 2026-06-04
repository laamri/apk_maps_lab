package com.example.lab12_localisation_temps_rel_via_gps_et_google_maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_LOC = 100;
    private TextView tvLat, tvLon;
    private RequestQueue requestQueue;
    private LocationManager locationManager;
    
    // 10.0.2.2 est l'adresse IP de votre PC vue par l'émulateur Android
    private final String insertUrl = "http://10.0.2.2/localisation/createPosition.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLat = findViewById(R.id.tvLat);
        tvLon = findViewById(R.id.tvLon);
        Button btnMap = findViewById(R.id.btnMap);

        requestQueue = Volley.newRequestQueue(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        btnMap.setOnClickListener(v -> startActivity(new Intent(this, MapsActivity.class)));

        askLocationPermissionAndStart();
    }

    private void askLocationPermissionAndStart() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_LOC);
        } else {
            startGpsUpdates();
        }
    }

    @SuppressLint("MissingPermission")
    private void startGpsUpdates() {
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                tvLat.setText("Lat: " + location.getLatitude());
                tvLon.setText("Lon: " + location.getLongitude());
                addPosition(location.getLatitude(), location.getLongitude());
            }
            @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override public void onProviderEnabled(@NonNull String provider) {}
            @Override public void onProviderDisabled(@NonNull String provider) {}
        };

        // On demande des mises à jour toutes les 10 secondes pour ne pas surcharger
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, listener);
        Toast.makeText(this, "Application prête. Changez la position GPS !", Toast.LENGTH_LONG).show();
    }

    private void addPosition(final double lat, final double lon) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                insertUrl,
                response -> {
                    // Si ça marche, on le voit dans les logs (Logcat)
                    System.out.println("DEBUG: Position envoyée avec succès !");
                },
                error -> {
                    // Si ça rate, on affiche l'erreur
                    Toast.makeText(MainActivity.this, "Erreur réseau (XAMPP): " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                params.put("latitude", String.valueOf(lat));
                params.put("longitude", String.valueOf(lon));
                params.put("date", sdf.format(new Date()));
                params.put("imei", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                return params;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOC && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startGpsUpdates();
        }
    }
}
