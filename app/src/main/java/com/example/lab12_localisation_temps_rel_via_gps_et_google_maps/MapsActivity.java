package com.example.lab12_localisation_temps_rel_via_gps_et_google_maps;

import android.os.Bundle;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RequestQueue requestQueue;
    // Utilisation de 10.0.2.2 pour l'émulateur
    private final String showUrl = "http://10.0.2.2/localisation/showPositions.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }

    private void setUpMap() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                showUrl,
                null,
                response -> {
                    try {
                        JSONArray positions = response.getJSONArray("positions");
                        for (int i = 0; i < positions.length(); i++) {
                            JSONObject position = positions.getJSONObject(i);
                            LatLng location = new LatLng(position.getDouble("latitude"), position.getDouble("longitude"));
                            mMap.addMarker(new MarkerOptions().position(location).title("Position " + i));
                            if (i == positions.length() - 1) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Erreur de chargement des points", Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(jsonObjectRequest);
    }
}
