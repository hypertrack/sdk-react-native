package com.reactnativehypertracksdk.common

/**
 * The data that represents geotag to create. Now it contains only payload data but
 * can have other params like expectedLocation in future
 *
 * @param data Geotag payload
 */
internal data class Geotag(val data: Map<String, Any?>)
