package tk.mrinalini.cureme;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText symptomText;
    private ListView possible_disease_list;
    private ArrayList<String> diseaseName;
    private TextView noDiseaseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        noDiseaseText = (TextView) findViewById(R.id.noDiseaseText);
        symptomText = (EditText) findViewById(R.id.symptom);
        possible_disease_list = (ListView) findViewById(R.id.possible_disease_listview);
        diseaseName = new ArrayList<>();

        possible_disease_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, SearchResult.class);
                intent.putExtra("search", diseaseName.get(i));
                startActivity(intent);
            }
        });
    }

    public void onClickSearch(View view) {

        final String symptom = symptomText.getText().toString().toLowerCase();

        if (Utils.isNetworkAvailable(this)) {

            JSONObject params = new JSONObject();
            try {
                params.put("symptom", symptom);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final String search_url = getString(R.string.search_url);

            noDiseaseText.setVisibility(View.GONE);
            possible_disease_list.setVisibility(View.VISIBLE);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, search_url, params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    diseaseName.clear();
                    try {
                        JSONArray detail = response.getJSONArray("diseases");

                        if (detail.length() > 0) {
                            noDiseaseText.setVisibility(View.GONE);
                            possible_disease_list.setVisibility(View.VISIBLE);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put(DBContract.CureMe_DB_Entry.SEARCH, symptom);
                            getContentResolver().insert(DBContentProvider.CONTENT_URI, contentValues);

                            for (int i = 0; i < detail.length(); i++) {
                                JSONObject singledetail = detail.getJSONObject(i);
                                diseaseName.add(singledetail.getString("disease"));
                            }

                            ArrayAdapter dataAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, diseaseName);
                            possible_disease_list.setAdapter(dataAdapter);
                        } else {
                            noDiseaseText.setVisibility(View.VISIBLE);
                            possible_disease_list.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(this, R.string.no_internet_text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.uf) {
            startActivity(new Intent(MainActivity.this, UpcomingFeatures.class));
            return true;
        } else if (id == R.id.aboutDev) {
            startActivity(new Intent(MainActivity.this, AboutDev.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
