
package com.noopyx.map;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;//hello you

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {
    private GoogleMap mMap;
    private Marker ici;
    private double longitude = 0, latitude = 0;
    private boolean affichMarker = true;
    public Map<String,String[]> listMarker = new HashMap<>();
    private Map<String,Marker> listMark = new HashMap<>();
    Polyline line;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);


        Intent intent = getIntent();

        int cpt=0, length = intent.getIntExtra("length",0);
        while(cpt < length) {
            listMarker.put(intent.getStringArrayExtra("param" + cpt)[2], intent.getStringArrayExtra("param"+cpt));
            cpt++;
        }


        context = MapsActivity.this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            if(affichMarker) {
                LatLng loc = new LatLng(latitude, longitude);
                ici = mMap.addMarker(new MarkerOptions().position(loc).title("Vous êtes ici !"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 12.0f));;
                affichMarker = false;
            }
            else {
                ici.setPosition(new LatLng(latitude, longitude));
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            System.out.println("YOLO Change status");
        }

        @Override
        public void onProviderEnabled(String s) {
            System.out.println("YOLO provider enabled : " + s);
        }

        @Override
        public void onProviderDisabled(String s) {
            System.out.println("YOLO provider disabled : " + s);
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 200, 1, locationListener);

        //addBrocante(50.650070, 3.074831, "Chez Corwin !", "Brocante de proximité");

        for(String libelle : listMarker.keySet()) {
            new getLatLngAdress(listMarker.get(libelle)[0],listMarker.get(libelle)[1],listMarker.get(libelle)[2]).execute();
        }


        /*addBrocante(50.652070, 3.075931, "yolo", "Brocante de proximité");
        addBrocante(50.654070, 3.076031, "yolo2", "Brocante de proximité");
        addBrocante(50.656070, 3.077131, "yolo3", "Brocante de proximité");
        addBrocante(50.658070, 3.078231, "yolo4", "Brocante de proximité");*/


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Collection<String> collection = listMark.keySet();
        for(String marker : collection) {
            if(Math.abs(listMark.get(marker).getPosition().latitude - latLng.latitude) < 0.004 && Math.abs(listMark.get(marker).getPosition().longitude - latLng.longitude) < 0.004 && !marker.equals("Vous êtes ici !")) {
                String url = getURL(this.latitude, this.longitude, listMark.get(marker).getPosition().latitude, listMark.get(marker).getPosition().longitude);
                new connectAsyncTask(url).execute();
                break;
            }
        }
    }


    public String getURL(double sourcelat, double sourcelog, double destlat,
                          double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }

    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;
        String url;

        connectAsyncTask(String urlPass) {
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if (result != null) {
                drawPath(result);
            }
        }
    }

    public class JSONParser {

        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        // constructor
        public JSONParser() {
        }

        public String getJSONFromUrl(String url) {

            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                json = sb.toString();
                is.close();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return json;

        }
    }

    public void drawPath(String result) {
        if (line != null) {
            mMap.clear();
        }
        try {
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude, dest.longitude))
                        .width(5).color(Color.BLUE).geodesic(true));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public void addBrocante (double latitude, double longitude, String libelle, String avis) {
        listMark.put(libelle, mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(libelle)
                .snippet(avis)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));

    }

    private class getLatLngAdress extends AsyncTask<Void, Void, String> {
        String adress;
        String libelle;
        String avis;
        double lat, lng;

        public getLatLngAdress(String adress, String libelle, String avis) {
            this.adress = adress;
            this.libelle = libelle;
            this.avis = avis;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONObject jsonObject = getLocationInfo(adress);

            try {
                lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");
                lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("YOLO",lat+" "+lng);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            addBrocante(lat, lng, libelle, avis);
        }
    }
    public static JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            address = address.replaceAll(" ","%20");

            HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address="+address+"&sensor=false");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }
}
