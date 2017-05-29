package com.nonvoid.barcrawler.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.activity.BreweryDetailsActivity;
import com.nonvoid.barcrawler.activity.BreweryListActivity;
import com.nonvoid.barcrawler.adapter.BreweryListAdapter;
import com.nonvoid.barcrawler.datalayer.api.BreweryAPI;
import com.nonvoid.barcrawler.datalayer.client.BreweryClient;
import com.nonvoid.barcrawler.model.BreweryLocation;
import com.nonvoid.barcrawler.util.IntentTags;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Matt on 5/11/2017.
 */

public class BreweryListFragment extends Fragment implements BreweryListAdapter.Callback {

    private static final String TAG = BreweryListActivity.class.getSimpleName();
    private static final String BREWERY_LOCTION_LIST_BUNDLE_KEY = "brewery_locations";

    @BindView(R.id.brewery_list_recyclerview)
    RecyclerView breweryListRecyclerView;
    @BindView(R.id.search_edit_text)
    EditText searchEditText;

    private BreweryAPI client = new BreweryClient();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private List<BreweryLocation> breweryLocations;


    public static BreweryListFragment newInstance(List<BreweryLocation> locations){
        BreweryListFragment fragment = new BreweryListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BREWERY_LOCTION_LIST_BUNDLE_KEY, (ArrayList<? extends Parcelable>) locations);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MPG onCreate");

        Bundle bundle = getArguments();

        if(bundle!= null) {
            ArrayList<BreweryLocation> list = getArguments().getParcelableArrayList(BREWERY_LOCTION_LIST_BUNDLE_KEY);
            if(list!=null){
                breweryLocations.addAll(list);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brewery_list_fragment, container, false);
        ButterKnife.bind(this, view);

        breweryListRecyclerView.setHasFixedSize(true);
        breweryListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("MPG", "Trying to search");

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                Disposable disposable =  client.getLocationsInCity(v.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                list -> {
                                    breweryLocations = list;
                                    breweryListRecyclerView.setAdapter(new BreweryListAdapter(breweryLocations, BreweryListFragment.this));
                                    Log.d("MPG", "successful search");
                        },
                                    throwable -> Log.d("MPG", "Failed to search")
                        );

                compositeDisposable.add(disposable);
                return true;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "MPG onResume");

//        breweryListRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onBrewerySelected(BreweryLocation location) {
        Intent intent = new Intent(getContext(), BreweryDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentTags.BREWERY_ITEM, location);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
