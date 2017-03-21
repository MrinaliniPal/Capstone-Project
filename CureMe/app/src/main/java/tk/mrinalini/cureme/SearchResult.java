package tk.mrinalini.cureme;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class SearchResult extends AppCompatActivity {

    static String search;
    private final CharSequence Titles[] = {"Disease Information", "Home Remedy", "Emergency Contact"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            search = extras.getString("search");
            setTitle(search);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        int numboftabs = 3;
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager(), Titles, numboftabs);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

}
