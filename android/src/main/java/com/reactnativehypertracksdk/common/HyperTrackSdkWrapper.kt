package com.reactnativehypertracksdk.common

import com.hypertrack.sdk.*
import com.hypertrack.sdk.android.HyperTrack
import com.hypertrack.sdk.android.HyperTrack.metadata
import com.hypertrack.sdk.android.Json
import com.hypertrack.sdk.android.Result
import com.reactnativehypertracksdk.common.Serialization.deserializeIsAvailable
import com.reactnativehypertracksdk.common.Serialization.deserializeName
import com.reactnativehypertracksdk.common.Serialization.deserializeGeotagData
import com.reactnativehypertracksdk.common.Serialization.deserializeIsTracking
import com.reactnativehypertracksdk.common.Serialization.deserializeMetadata
import com.reactnativehypertracksdk.common.Serialization.serializeDeviceId
import com.reactnativehypertracksdk.common.Serialization.serializeErrors
import com.reactnativehypertracksdk.common.Serialization.serializeIsAvailable
import com.reactnativehypertracksdk.common.Serialization.serializeIsTracking
import com.reactnativehypertracksdk.common.Serialization.serializeLocationErrorFailure
import com.reactnativehypertracksdk.common.Serialization.serializeLocationResult
import com.reactnativehypertracksdk.common.Serialization.serializeLocationSuccess
import com.reactnativehypertracksdk.common.Serialization.serializeLocationWithDeviationSuccess
import com.reactnativehypertracksdk.common.Serialization.serializeMetadata
import com.reactnativehypertracksdk.common.Serialization.serializeName

typealias Serialized = Map<String, Any?>

/**
 * This class stores SDK instance, calls HyperTrack SDK methods and serializes responses.
 * It receives serialized params.
 */
internal object HyperTrackSdkWrapper {

    fun addGeotag(args: Serialized): WrapperResult<Serialized> {
        return deserializeGeotagData(args)
            .flatMapSuccess { geotag ->
                // TODO: return proper error if JSON is wrong
                val geotagMetadata = Json.fromMap(geotag.data)!!
                val expectedLocation = geotag
                    .expectedLocation
                    ?.let {
                        HyperTrack.Location(
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                    }
                if (expectedLocation != null) {
                    HyperTrack
                        .addGeotag(geotagMetadata, expectedLocation)
                        .let {
                            when (it) {
                                is Result.Failure -> {
                                    serializeLocationErrorFailure(it.failure)
                                }

                                is Result.Success -> {
                                    serializeLocationWithDeviationSuccess(it.success)
                                }
                            }
                        }
                } else {
                    HyperTrack
                        .addGeotag(geotagMetadata)
                        .let { serializeLocationResult(it) }
                }.let {
                    Success(it)
                }
            }
    }

    fun getDeviceId(): WrapperResult<Serialized> {
        return Success(serializeDeviceId(HyperTrack.deviceID))
    }

    fun getErrors(): WrapperResult<List<Serialized>> {
        return Success(serializeErrors(HyperTrack.errors))
    }

    fun getIsAvailable(): WrapperResult<Serialized> {
        return Success(
            serializeIsAvailable(HyperTrack.isAvailable)
        )
    }

    fun getIsTracking(): WrapperResult<Serialized> {
        return Success(
            serializeIsTracking(HyperTrack.isTracking)
        )
    }

    fun getLocation(): WrapperResult<Serialized> {
        return HyperTrack
            .location
            .let {
                when (it) {
                    is Result.Failure -> serializeLocationErrorFailure(it.failure)
                    is Result.Success -> serializeLocationSuccess(it.success)
                }
            }
            .let { Success(it) }
    }

    fun getMetadata(): WrapperResult<Serialized> {
        return Success(
            serializeMetadata(HyperTrack.metadata.toMap())
        )
    }

    fun getName(): WrapperResult<Serialized> {
        return Success(
            serializeName(HyperTrack.name)
        )
    }

    fun setIsAvailable(args: Serialized): WrapperResult<Unit> {
        return deserializeIsAvailable(args)
            .mapSuccess { isAvailable ->
                HyperTrack.isAvailable = isAvailable
            }
    }

    fun setIsTracking(args: Serialized): WrapperResult<Unit> {
        return deserializeIsTracking(args)
            .mapSuccess { isTracking ->
                HyperTrack.isTracking = isTracking
            }
    }

    fun setMetadata(args: Serialized): WrapperResult<Unit> {
        return deserializeMetadata(args)
            .flatMapSuccess { metadata ->
                WrapperResult.tryAsResult {
                    // TODO: return proper error if JSON is wrong
                    HyperTrack.metadata = Json.fromMap(metadata)!!
                }
            }
    }

    fun setName(args: Serialized): WrapperResult<Unit> {
        return deserializeName(args)
            .mapSuccess { name ->
                HyperTrack.name = name
            }
    }

}
