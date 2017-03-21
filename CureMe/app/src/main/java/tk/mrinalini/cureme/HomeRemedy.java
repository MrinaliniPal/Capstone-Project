package tk.mrinalini.cureme;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class HomeRemedy extends Fragment {
    private TextView remedy;
    private String disease;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_remedy, container, false);

        remedy = (TextView) view.findViewById(R.id.remedy);
        disease = SearchResult.search;
        home_remedy_fetch();

        return view;
    }

    private void home_remedy_fetch() {

        if (Utils.isNetworkAvailable(getContext())) {
            String home_remedy_url = getString(R.string.home_remedy_url);

            JSONObject params = new JSONObject();
            try {
                params.put("disease", disease);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, home_remedy_url, params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray detail = response.getJSONArray("hremedy");

                        if (detail.length() > 0) {
                            for (int i = 0; i < detail.length(); i++) {
                                JSONObject singledetail = detail.getJSONObject(i);
                                remedy.setText(singledetail.getString("remedy"));
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
