package com.reactnativehypertracksdk.common

import android.location.Location

/**
 * The data that represents geotag to create
 *
 * @param data Geotag payload
 */
internal data class GeotagData(
    val data: Map<String, Any?>,
    val expectedLocation: Location?
)
