package com.reactnativehypertracksdk.common

/**
 * Check HyperTrack SDK docs for LocationError
 */
internal sealed class LocationError
internal object NotRunning : LocationError()
internal object Starting : LocationError()
internal data class Errors(val errors: Set<HyperTrackError>) : LocationError()
