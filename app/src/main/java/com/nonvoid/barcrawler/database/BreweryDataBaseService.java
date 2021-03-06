package com.nonvoid.barcrawler.database;

import com.nonvoid.barcrawler.model.response.BeerResponse;
import com.nonvoid.barcrawler.model.response.BreweryResponse;
import com.nonvoid.barcrawler.model.response.LocationResponse;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BreweryDataBaseService {

    String KEY = "key=98d5ee318c335e35af66cc5f952fd412";
    String FORMAT = "&format=json";
    String NOT_PLANNING = "&inPlanning=n";
    String NOT_CLOSED = "&isClosed=n";
    String VERIFIED_STATUS = "&status=verified";

    @GET("locations?"+KEY+FORMAT+NOT_PLANNING+NOT_CLOSED+"&isPrimary=y&openToPublic=y")
    Observable<LocationResponse> searchCityForBreweries(@Query("locality") String city);

    @GET("search?"+KEY+FORMAT+VERIFIED_STATUS+"&type=brewery&withLocations=y")
    Observable<BreweryResponse> searchForBrewery(@Query("q") String query);

    @GET("search?"+KEY+FORMAT+"&type=beer&withBreweries=y")
    Observable<BeerResponse> searchForBeer(@Query("q") String query);

    @GET("brewery/{id}?"+KEY+FORMAT)
    Observable<BreweryResponse> getBreweryById(@Path("id") String breweryId);

    @GET("breweries?"+KEY+FORMAT)
    Observable<BreweryResponse> getBreweriesById(@Query("ids") String ids);

    @GET("brewery/{id}/beers?"+KEY+FORMAT)
    Observable<BeerResponse> getBeersForBrewery(@Path("id") String breweryId);

    @GET("beer/{id}?"+KEY+FORMAT)
    Observable<BeerResponse> getBeerById(@Path("id") String beerId);

    @GET("beers/?"+KEY+FORMAT)
    Observable<BeerResponse> getBeersById(@Query("ids") String ids);
}
