package tk.mrinalini.cureme;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpcomingFeatures extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_features);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt = (TextView) findViewById(R.id.uf_txt);

        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        txt.setText(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new FetchData(this);
    }

    private static class FetchData extends AsyncTaskLoader<String> {

        FetchData(Context context) {
            super(context);
        }

        @Override
        public String loadInBackground() {
            String load_url = getContext().getResources().getString(R.string.uf_url);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonString = null;
            String line;
            try {
                URL url = new URL(load_url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) return null;

                reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = reader.readLine()) != null) buffer.append(line);

                if (buffer.length() == 0) return null;
                jsonString = buffer.toString();

            } catch (IOException e) {

                return null;
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jsonString;
        }

        @Override
        public void deliverResult(String data) {
            super.deliverResult(data);
        }
    }
}
