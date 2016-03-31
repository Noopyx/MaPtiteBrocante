
package com.noopyx.map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements View.OnClickListener, DatePicker.OnDateChangedListener {

    static private String login;
    static private String password;
    private Map<String,Brocante> listMarker = new HashMap<>();
    private List<Brocante> listBro = new ArrayList<>();
    private List<Brocante> listEnvoi = new ArrayList<>();
    DisplayMetrics metrics = new DisplayMetrics();
    private TextView viewTap = null;
    private String textTap = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceuil);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        Button signUp = (Button) findViewById(R.id.signUp);
        Button login = (Button) findViewById(R.id.LogIn);

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        ViewGroup.LayoutParams params = signUp.getLayoutParams();
        params.width = metrics.widthPixels / 2;
        signUp.setLayoutParams(params);

        params = login.getLayoutParams();
        params.width = metrics.widthPixels / 2;
        login.setLayoutParams(params);


    }

    public void retourAcceuil(View view) {
        setContentView(R.layout.acceuil);
        Button signUp = (Button) findViewById(R.id.signUp);
        Button login = (Button) findViewById(R.id.LogIn);

        ViewGroup.LayoutParams params = signUp.getLayoutParams();
        params.width = metrics.widthPixels / 2;
        signUp.setLayoutParams(params);

        params = login.getLayoutParams();
        params.width = metrics.widthPixels / 2;
        login.setLayoutParams(params);
    }

    public void goToSignIn(View view) {
        setContentView(R.layout.login);
        Button inscription = (Button) findViewById(R.id.inscription);
        Button retour = (Button) findViewById(R.id.retour);

        ViewGroup.LayoutParams params = inscription.getLayoutParams();
        params.width = metrics.widthPixels / 2;
        inscription.setLayoutParams(params);

        params = retour.getLayoutParams();
        params.width = metrics.widthPixels / 2;
        retour.setLayoutParams(params);
    }

    public void goToInscription(View view) {
        setContentView(R.layout.inscription);
        Button connect = (Button) findViewById(R.id.inscript);
        Button retour = (Button) findViewById(R.id.retour);

        ViewGroup.LayoutParams params = connect.getLayoutParams();
        params.width = metrics.widthPixels / 2;
        connect.setLayoutParams(params);

        params = retour.getLayoutParams();
        params.width = metrics.widthPixels / 2;
        retour.setLayoutParams(params);

        /*TextView text =(TextView) findViewById(R.id.textView);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Avenir.ttf");
        text.setTypeface(typeface);*/
    }


    public void validation(View view) {
        EditText login = (EditText) findViewById(R.id.login);
        EditText pwd = (EditText) findViewById(R.id.pwd);

        this.login = login.getText().toString();
        password = pwd.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://51.254.167.72:80/v1/login",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String json) {
                        connection();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                }

                if (error instanceof TimeoutError) {
                    Log.e("Volley", "TimeoutError");
                }else if(error instanceof NoConnectionError){
                    Log.e("Volley", "NoConnectionError");
                } else if (error instanceof AuthFailureError) {
                    Log.e("Volley", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("Volley", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("Volley", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("Volley", "ParseError");
                }                TextView output = (TextView) findViewById(R.id.output);
                output.setVisibility(View.VISIBLE);
                output.setText("Erreur de login ou de mot de passe");
            }
        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> mHeaders = new HashMap<String, String>();
                String creds = String.format(LoginActivity.login + ":" + LoginActivity.password);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                mHeaders.put("Authorization", auth);
                return mHeaders;
            }
        };
        queue.add(stringRequest);


    }


    public void setMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        int i = 0;
        intent.putExtra("length",listEnvoi.size());
        for(Brocante bro : listEnvoi)
            intent.putExtra("param" + (i++), bro.getArrayString());
        startActivity(intent);
    }

    public void connection () {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://51.254.167.72:80/v1/brocante",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String json) {
                        try {
                            recupBro(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                }

                if (error instanceof TimeoutError) {
                    Log.e("Volley", "TimeoutError");
                }else if(error instanceof NoConnectionError){
                    Log.e("Volley", "NoConnectionError");
                } else if (error instanceof AuthFailureError) {
                    Log.e("Volley", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("Volley", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("Volley", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("Volley", "ParseError");
                }                TextView output = (TextView) findViewById(R.id.output);
                output.setVisibility(View.VISIBLE);
                output.setText("Erreur de login ou de mot de passe");
            }
        });
        queue.add(stringRequest);
    }

    public void recupBro(String json) throws JSONException {;
        JSONArray raay = new JSONArray(json);

        for(int i=0; i < raay.length(); i++) {
            JSONObject obj = raay.getJSONObject(i);
            String[] dSplit = obj.optString("date").split("&#x2f;");
            if(dSplit.length > 2) {
                GregorianCalendar date = new GregorianCalendar();
                if(obj.optString("heure_debut").length() > 3)
                    date.set(Integer.parseInt(dSplit[2]), Integer.parseInt(dSplit[1]), Integer.parseInt(dSplit[0]),Integer.parseInt(obj.optString("heure_debut").substring(0, 2)),Integer.parseInt(obj.optString("heure_debut").substring(3)));
                if (obj.optString("heure_debut").length() > 0)
                    if(obj.optString("heure_debut").length() >1)
                        date.set(Integer.parseInt(dSplit[2]), Integer.parseInt(dSplit[1]), Integer.parseInt(dSplit[0]), Integer.parseInt(obj.optString("heure_debut").substring(0, 2)), 0);
                    else if(obj.optString("heure_debut").length() == 1)
                        date.set(Integer.parseInt(dSplit[2]), Integer.parseInt(dSplit[1]), Integer.parseInt(dSplit[0]), Integer.parseInt(obj.optString("heure_debut").substring(0, 1)), 0);

                Brocante bro = new Brocante(obj.optString("libelle"), date, obj.optString("ville"));
                bro.setHeureFin(obj.optString("heure_fin"));
                listBro.add(bro);
                listMarker.put(obj.optString("libelle"), bro);
            }
        }

        setContentView(R.layout.acceuilprofil);

        Button map = (Button) findViewById(R.id.carte);
        Button profil = (Button) findViewById(R.id.profil);
        ViewGroup.LayoutParams params = map.getLayoutParams();
        params.width = metrics.widthPixels / 2;
        map.setLayoutParams(params);

        params = profil.getLayoutParams();
        params.width = metrics.widthPixels / 2;
        profil.setLayoutParams(params);

        DatePicker date = (DatePicker) findViewById(R.id.calendrier);
        date.init(date.getYear(),date.getMonth(),date.getDayOfMonth(),this);
        LinearLayout liste = (LinearLayout) findViewById(R.id.listeBro);
        for (Brocante bro : listBro) {

            if(bro.getGregorian().get(Calendar.DATE) == date.getDayOfMonth() && bro.getGregorian().get(Calendar.MONTH) == date.getMonth() && bro.getGregorian().get(Calendar.YEAR) == date.getYear()) {
                TextView text = new TextView(this);
                text.setText(bro.getLibelle());
                text.setTextSize(getResources().getDimension(R.dimen.size_text_listBrocante));
                text.setClickable(true);
                text.setOnClickListener(this);
                text.setGravity(Gravity.CENTER_HORIZONTAL);
                liste.addView(text, 1);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(viewTap != null) {
            viewTap.setText(textTap);
        }
        TextView text = (TextView) view;
        viewTap = text;
        textTap = text.getText().toString();
        text.setText(listMarker.get(text.getText()).getLibelle()+"\n"+listMarker.get(text.getText()).getDate());
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
        LinearLayout liste = (LinearLayout) findViewById(R.id.listeBro);
        DatePicker date = (DatePicker) findViewById(R.id.calendrier);
        listEnvoi.clear();
        liste.removeAllViewsInLayout();
        TextView msg = new TextView(this);
        msg.setText("Brocante(s) disponibles :");
        //msg.setTextSize(R.dimen.text_list);
        msg.setTextColor(Color.rgb(188, 44, 44));
        liste.addView(msg, 0);

        for (Brocante bro : listBro) {
            if(bro.getGregorian().get(Calendar.DATE) == date.getDayOfMonth() && bro.getGregorian().get(Calendar.MONTH) == date.getMonth()+1 && bro.getGregorian().get(Calendar.YEAR) == date.getYear()) {
                TextView text = new TextView(this);
                text.setText(bro.getLibelle());
                text.setTextSize(getResources().getDimension(R.dimen.size_text_listBrocante));
                text.setClickable(true);
                text.setOnClickListener(this);
                text.setGravity(Gravity.CENTER_HORIZONTAL);
                liste.addView(text, 1);
                listEnvoi.add(bro);
            }
        }
    }

    public void inscription (View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        EditText login = (EditText) findViewById(R.id.newlogin);
        EditText pwd = (EditText) findViewById(R.id.mdp);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody = new JSONObject("{\"name\":\""+login.getText().toString()+"\", \"password\":\""+pwd.getText().toString()+"\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://51.254.167.72/v1/userdb", jsonBody,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject json) {
                        connection();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                }

                if (error instanceof TimeoutError) {
                    Log.e("Volley", "TimeoutError");
                }else if(error instanceof NoConnectionError){
                    Log.e("Volley", "NoConnectionError");
                } else if (error instanceof AuthFailureError) {
                    Log.e("Volley", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("Volley", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("Volley", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("Volley", "ParseError");
                }
                TextView output = (TextView) findViewById(R.id.output);
                output.setVisibility(View.VISIBLE);
                output.setText("Login déjà utilisé");
            }
        });

        queue.add(stringRequest);
    }
}