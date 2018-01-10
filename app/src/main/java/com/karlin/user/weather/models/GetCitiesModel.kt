package com.karlin.user.weather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 *
 * Created by User on 09.01.2018.
 */

class GetCitiesModel {

    @SerializedName("predictions") @Expose
    var predictions: List<Prediction>? = null

    @SerializedName("status") @Expose var status: String? = null

}

class MainTextMatchedSubstring {

    @SerializedName("length")
    @Expose
    var length: Int? = null
    @SerializedName("offset")
    @Expose
    var offset: Int? = null

}

class MatchedSubstring {

    @SerializedName("length")
    @Expose
    var length: Int? = null
    @SerializedName("offset")
    @Expose
    var offset: Int? = null

}

class Prediction {

    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("matched_substrings")
    @Expose
    var matchedSubstrings: List<MatchedSubstring>? = null
    @SerializedName("place_id")
    @Expose
    var placeId: String? = null
    @SerializedName("reference")
    @Expose
    var reference: String? = null
    @SerializedName("structured_formatting")
    @Expose
    var structuredFormatting: StructuredFormatting? = null
    @SerializedName("terms")
    @Expose
    var terms: List<Term>? = null
    @SerializedName("types")
    @Expose
    var types: List<String>? = null

}

class StructuredFormatting {

    @SerializedName("main_text")
    @Expose
    var mainText: String? = null
    @SerializedName("main_text_matched_substrings")
    @Expose
    var mainTextMatchedSubstrings: List<MainTextMatchedSubstring>? = null
    @SerializedName("secondary_text")
    @Expose
    var secondaryText: String? = null

}

class Term {

    @SerializedName("offset")
    @Expose
    var offset: Int? = null
    @SerializedName("value")
    @Expose
    var value: String? = null

}
