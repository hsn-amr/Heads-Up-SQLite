package com.example.headsupprep

import com.google.gson.annotations.SerializedName

class Celebrity {

    @SerializedName("pk")
    var pk: Int? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("taboo1")
    var taboo1: String? = null

    @SerializedName("taboo2")
    var taboo2: String? = null

    @SerializedName("taboo3")
    var taboo3: String? = null

}