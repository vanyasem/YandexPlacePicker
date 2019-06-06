package ru.semkin.yandexplacepicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.mapkit.GeoObject;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.BoundingBox;
import com.yandex.mapkit.geometry.Geometry;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.SearchType;
import com.yandex.mapkit.search.Session;
import com.yandex.runtime.Error;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import ru.semkin.yandexplacepicker.PlaceParcelable;
import ru.semkin.yandexplacepicker.R;
import ru.semkin.yandexplacepicker.YandexPlacePicker;

public class SearchActivity extends AppCompatActivity implements Session.SearchListener, PlaceConfirmDialogFragment.OnPlaceConfirmedListener {

    public static final String EXTRA_PLACE = "extra_place";

    private final static String DIALOG_CONFIRM_PLACE_TAG = "dialog_place_confirm";
    private final BoundingBox BOUNDING_BOX =
            new BoundingBox(new Point(-85, -179), new Point(85, 179));
    private final SearchOptions SEARCH_OPTIONS = new SearchOptions().setSearchTypes(
            SearchType.GEO.value | SearchType.BIZ.value);

    private SearchManager mSearchManager;

    private Toolbar mToolbar;
    private ContentLoadingProgressBar mSpinner;
    private RecyclerView mRecyclerSuggest;
    private PlacePickerAdapter mPlaceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(YandexPlacePicker.yandexMapsKey);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initializes the map
        MapKitFactory.initialize(this);
        SearchFactory.initialize(this);

        mToolbar = findViewById(R.id.toolbar);
        mSpinner = findViewById(R.id.pbLoading);
        mRecyclerSuggest = findViewById(R.id.rvSuggestResults);

        // Configure the toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize the recycler view
        mRecyclerSuggest.setLayoutManager(new LinearLayoutManager(this));

        // Construct SearchManager
        mSearchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.ONLINE);
    }

    @Override
    protected void onStop() {
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onSearchResponse(@NonNull Response response) {
        if(mPlaceAdapter == null) {
            mPlaceAdapter = new PlacePickerAdapter(response.getCollection().getChildren(), this::showConfirmPlacePopup);
        } else {
            mPlaceAdapter.swapData(response.getCollection().getChildren());
        }
        mRecyclerSuggest.setAdapter(mPlaceAdapter);
        mSpinner.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSearchError(@NonNull Error error) {
        String errorMessage = getString(R.string.unknown_error_message);
        if (error instanceof RemoteError) {
            errorMessage = getString(R.string.remote_error_message);
        } else if (error instanceof NetworkError) {
            errorMessage = getString(R.string.network_error_message);
        }
        Log.e(YandexPickerActivity.class.getCanonicalName(), errorMessage);

        mSpinner.setVisibility(View.INVISIBLE);
        Toast.makeText(this, R.string.picker_load_search_results_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                if(!newText.trim().equals("")) {
                    requestSuggest(newText.trim());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setQueryHint(getString(android.R.string.search_go));
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        searchView.requestFocus();
        return true;
    }

    private void requestSuggest(String query) {
        mSpinner.setVisibility(View.VISIBLE);
        Geometry geometry = Geometry.fromBoundingBox(BOUNDING_BOX);
        mSearchManager.submit(query, geometry, SEARCH_OPTIONS, this);
    }

    private void showConfirmPlacePopup(GeoObject place) {
        PlaceConfirmDialogFragment fragment = PlaceConfirmDialogFragment.newInstance(place, this);
        fragment.show(getSupportFragmentManager(), DIALOG_CONFIRM_PLACE_TAG);
    }

    public static PlaceParcelable getPlace(Intent intent) {
        return intent.getParcelableExtra(EXTRA_PLACE);
    }

    @Override
    public void onPlaceConfirmed(PlaceParcelable place) {
        Intent data = new Intent();
        data.putExtra(YandexPlacePicker.EXTRA_PLACE, place);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}
