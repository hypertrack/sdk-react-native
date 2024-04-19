package com.reactnativehypertracksdk.common

import android.location.Location
import com.hypertrack.sdk.android.HyperTrack

/**
 * The data that represents geotag to create
 *
 * @param data Geotag payload
 */
internal data class GeotagData(
    val data: Map<String, Any?>,
    val expectedLocation: Location?,
    val orderHandle: String?,
    val orderStatus: HyperTrack.OrderStatus?,
)
