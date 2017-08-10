package com.nonvoid.barcrawler.datalayer.client

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.model.Brewery
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Maybe

/**
 * Created by Matt on 8/8/2017.
 */
class FireBaseClient(private val user: FirebaseUser) {
    private val reference :DatabaseReference = FirebaseDatabase.getInstance().reference

    fun setBreweryAsFavorite(brewery: Brewery, favorite: Boolean) {
        getBreweryFavoriteReference(brewery)
            .child(user.uid)
            .setValue(favorite)
            .addOnCompleteListener { result ->
                if(result.isSuccessful){
                    Log.d("MPG", "set favorite to $favorite")
                }else {
                    Log.d("MPG", "failed to set favorite to $favorite")
                }
            }
    }

    fun getNumberOfFavoritesForBrewery(brewery: Brewery): Maybe<Int> {
        return RxFirebaseDatabase.observeSingleValueEvent(getBreweryFavoriteReference(brewery)
                .orderByValue()
                .equalTo(true), {snapShot -> snapShot.childrenCount.toInt()}
        )
    }

    fun isBeerLiked(beer: Beer): Maybe<Boolean> {
        return RxFirebaseDatabase.observeSingleValueEvent(getBeerRatingReference(beer)
                .child(user.uid)
                .orderByValue(), {snapShot -> snapShot.value == 1}
        )
    }

    fun likeBeer(beer: Beer){
        rateBeer(beer, 1)
    }

    fun dislikeBeer(beer: Beer){
        rateBeer(beer, 0)
    }

    private fun rateBeer(beer: Beer, rating: Int){
        getBeerRatingReference(beer)
                .child(user.uid)
                .setValue(rating)
                .addOnCompleteListener { result ->
                    if(result.isSuccessful){
                        Log.d("MPG", "set rating to $rating")
                    }else {
                        Log.d("MPG", "failed to set rating to $rating")
                    }
                }

    }

    private fun getBreweryFavoriteReference(brewery: Brewery): DatabaseReference{
        return reference.child(BREWERY)
                .child(brewery.id)
                .child(FAVORITE)
    }

    private fun getBeerRatingReference(beer: Beer): DatabaseReference{
        return reference.child(BEER)
                .child(beer.id)
                .child(RATING)

    }

    companion object {
        const val BREWERY = "brewery"
        const val FAVORITE = "favorite"
        const val BEER = "beer"
        const val RATING = "rating"
    }
}