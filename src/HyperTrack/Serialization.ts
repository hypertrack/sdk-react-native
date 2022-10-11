import type { Location } from "./Location"
import type { LocationError } from "./LocationError"

function deserializeTracking(isTracking: Map<String, Object>): boolean {
    if(isTracking.get("type")! == "isTracking") {
        return isTracking.get("value") as boolean
    } else {
        throw Error("invalid isTracking response")
    }
}

function deserializeAvailability(isAvailable: Map<String, Object>): boolean {
    if(isAvailable.get("type")! == "isAvailable") {
        return isAvailable.get("value") as boolean
    } else {
        throw Error("invalid isAvailable response")
    }
}

function deserializeLocationResponse(response: Map<String, Object>): (LocationError | Location) {
    throw Error("not implemented")
}