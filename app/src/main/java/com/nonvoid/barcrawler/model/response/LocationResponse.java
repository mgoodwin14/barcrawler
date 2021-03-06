package com.nonvoid.barcrawler.model.response;

import com.google.gson.annotations.SerializedName;
import com.nonvoid.barcrawler.model.BreweryLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/5/2017.
 */

public class LocationResponse {
    @SerializedName("currentPage")
    int currentPage;
    @SerializedName("numberOfPages")
    int numberOfPages;
    @SerializedName("totalResults")
    int results;
    @SerializedName("data")
    List<BreweryLocation> locations;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public int getResults() {
        return results;
    }

    public List<BreweryLocation> getLocations() {
        if(locations == null){
            locations = new ArrayList<>();
        }
        return locations;
    }

    public void setLocations(List<BreweryLocation> locations) {
        this.locations = locations;
    }
}
