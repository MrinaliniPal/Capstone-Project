package tk.mrinalini.cureme;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Disease_Information extends android.support.v4.app.Fragment {
    private String disease;
    private TextView info;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.disease_info, container, false);

        info = (TextView) view.findViewById(R.id.info);
        disease = SearchResult.search;
        disease_info_fetch();
        return view;
    }

    private void disease_info_fetch() {
        if (Utils.isNetworkAvailable(getContext())) {
            String disease_info_url = getString(R.string.disease_info_url);

            JSONObject params = new JSONObject();
            try {
                params.put("disease", disease);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, disease_info_url, params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray detail = response.getJSONArray("information");

                        if (detail.length() > 0) {
                            for (int i = 0; i < detail.length(); i++) {
                                JSONObject singledetail = detail.getJSONObject(i);
                                info.setText(singledetail.getString("info"));
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(getContext(), R.string.no_internet_text, Toast.LENGTH_SHORT).show();
        }
    }
}
