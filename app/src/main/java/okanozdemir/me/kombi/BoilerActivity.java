package okanozdemir.me.kombi;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BoilerActivity extends AppCompatActivity {

    private TextView statusText;
    private String accessToken = "";
    private String deviceToken = "";
    public ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button onBtn = findViewById(R.id.onBtn);
        Button offBtn = findViewById(R.id.offBtn);
        Button refreshBtn = findViewById(R.id.refreshBtn);
        statusText = findViewById(R.id.textView);

        onBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOffBoiler(getBaseContext(),"on");

            }
        });
        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOffBoiler(getBaseContext(),"off");
            }
        });
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStatusBoiler(getBaseContext());
            }
        });
        getStatusBoiler(getBaseContext());
    }

    private void onOffBoiler(Context context,final String arg){

        String urlSetRelay ="https://api.particle.io/v1/devices/" + deviceToken + "/setRelay";

        progress = ProgressDialog.show(BoilerActivity.this,"",
                "İşlem Yapılıyor...", true);

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, urlSetRelay, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String value = json.getString("return_value");
                    if(value.equals("1"))
                    {
                        statusText.setText("Kombi Açık");
                    }
                    else if(value.equals("0"))
                    {
                        statusText.setText("Kombi Kapalı");
                    }
                    else{
                        statusText.setText("Kombiye Erişilemiyor!");
                    }
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("access_token", accessToken);
                params.put("args", arg);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

    private void getStatusBoiler(Context context){

        String urlStatusRelay = "https://api.particle.io/v1/devices/" + deviceToken + "/getRelay";

        progress = ProgressDialog.show(BoilerActivity.this,"",
                "İşlem Yapılıyor...", true);

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, urlStatusRelay, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String value = json.getString("return_value");
                    if(value.equals("1"))
                    {
                        statusText.setText("Kombi Açık");
                    }
                    else if(value.equals("0"))
                    {
                        statusText.setText("Kombi Kapalı");
                    }
                    else{
                        statusText.setText("Kombiye Erişilemiyor!");
                    }
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("access_token", accessToken);
                params.put("args", "get");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }
}