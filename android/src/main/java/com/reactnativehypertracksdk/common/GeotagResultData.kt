package com.reactnativehypertracksdk.common

import android.location.Location

internal sealed class GeotagResultData
internal data class GeotagSuccess(val location: Location) : GeotagResultData()
internal data class GeotagSuccessWithDeviation(
    val location: Location,
    val deviation: Float
) : GeotagResultData()
internal data class GeotagFailure(val failure: Throwable) : GeotagResultData()
