package com.nonvoid.barcrawler.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.dagger.MyApp;
import com.nonvoid.barcrawler.fragment.BeerListFragment;
import com.nonvoid.barcrawler.fragment.BreweryMapFragment;
import com.nonvoid.barcrawler.model.BreweryLocation;
import com.nonvoid.barcrawler.util.IntentTags;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Matt on 5/13/2017.
 */

public class BreweryDetailsActivity extends BaseActivity {

    @BindView(R.id.brewery_details_name_textview)
    TextView breweryNameTextView;

    @BindView(R.id.brewery_details_description_textview)
    TextView breweryDescriptionTextView;
    private BreweryLocation location;

    @Inject
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brewery_details_activity);
        ((MyApp) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {

            location = bundle.getParcelable(IntentTags.BREWERY_ITEM);

            if (location != null) {
                breweryNameTextView.setText(location.getName());
                breweryDescriptionTextView.setText(location.getDescription());
                breweryDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());
                BreweryMapFragment fragment = BreweryMapFragment.newInstance(location);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.brewery_map_fragment_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.brewery_details_menu, menu);
        if(sharedPref.getBoolean(location.getBreweryId(), false)) {
            menu.getItem(0).setIcon(R.drawable.ic_favorite_checked);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.favorite_button:
                toggleFavorite(location);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.beer_list_button)
    public void onShowBeerList(){
        //TODO show beer list fragment
        BeerListFragment fragment = BeerListFragment.newInstance(location);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.brewery_map_fragment_frame, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void toggleFavorite(BreweryLocation location) {

        Boolean fav = sharedPref.getBoolean(location.getBreweryId(), false);
        sharedPref.edit()
                .putBoolean(location.getBreweryId(), !fav)
                .apply();

        invalidateOptionsMenu();
    }
}
