package com.nonvoid.barcrawler.datalayer.api;

import com.nonvoid.barcrawler.model.BreweryLocation;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


/**
 * Created by Matt on 5/5/2017.
 * This interface is used by retro
 */

public interface BreweryAPI {
    Observable<ArrayList<BreweryLocation>> getLocationsInCity(String city);
}
