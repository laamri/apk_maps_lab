package com.example.mapapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class GoogleMapActivity extends AppCompatActivity {

    private MapView map;
    private RequestQueue requestQueue;
    private String showUrl = "http://10.0.2.2/map_project/getPosition.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // CONFIGURATION IMPORTANTE POUR OSMDROID
        // 1. Charger la config
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        // 2. Définir un User-Agent (Indispensable pour charger les tuiles)
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_google_map);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        map.getController().setZoom(15.0);
        // Centre par défaut : Casablanca (ou une position connue)
        GeoPoint startPoint = new GeoPoint(33.5731, -7.5898);
        map.getController().setCenter(startPoint);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        loadPositions();
    }

    private void loadPositions() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                showUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                JSONArray positions = response.getJSONArray("positions");
                                
                                Drawable markerDrawable = ContextCompat.getDrawable(GoogleMapActivity.this, R.drawable.marker);
                                Bitmap markerBitmap = null;
                                if (markerDrawable != null) {
                                    markerBitmap = drawableToBitmap(markerDrawable);
                                    markerBitmap = Bitmap.createScaledBitmap(markerBitmap, 80, 80, false);
                                }

                                for (int i = 0; i < positions.length(); i++) {
                                    JSONObject position = positions.getJSONObject(i);
                                    double lat = position.getDouble("latitude");
                                    double lng = position.getDouble("longitude");

                                    Marker marker = new Marker(map);
                                    marker.setPosition(new GeoPoint(lat, lng));
                                    marker.setTitle("Position " + (i + 1));
                                    
                                    if (markerBitmap != null) {
                                        marker.setIcon(new BitmapDrawable(getResources(), markerBitmap));
                                    }
                                    
                                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                    map.getOverlays().add(marker);
                                }
                                map.invalidate();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Si le serveur local n'est pas lancé, on affiche quand même la map vide de marqueurs
                        Toast.makeText(GoogleMapActivity.this, "Note: Serveur PHP injoignable (10.0.2.2)", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        
        requestQueue.add(jsonObjectRequest);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }
}
