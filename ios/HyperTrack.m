#import "HyperTrack.h"
#import <React/RCTLog.h>
#import <React/RCTEventDispatcher.h>

@interface HyperTrack ()

@property(nonatomic) HTSDK *hyperTrack;

@end


@implementation HyperTrack
{
    bool hasListeners;
}
/**
 * RN Critical error type.
 */
typedef NS_ENUM(NSUInteger, RNCriticalErrorType) {
    permissionDenied = 3,
    authorizationError = 2,
    invalidPublishableKey = 1,
    generalError = 0
};

RCT_EXPORT_MODULE();

- (dispatch_queue_t)methodQueue {
    return dispatch_get_main_queue();
}

- (NSArray<NSString *> *)supportedEvents {
    return @[@"onTrackingErrorHyperTrack",
             @"onTrackingStateChanged",
    ];
}

- (void)startObserving {
    hasListeners = YES;
}

- (void)stopObserving {
    hasListeners = NO;
}

#pragma mark - HyperTrack SDK

RCT_EXPORT_METHOD(initialize:(NSString *)publishableKey startsTracking:(BOOL)startsTracking automaticallyRequestPermissions:(BOOL)automaticallyRequestPermissions :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTLogInfo(@"Initializing HyperTrack with publishableKey: %@", publishableKey);
    __weak __typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        [weakSelf addObservers];
        HTResult *result = [HTSDK makeSDKWithPublishableKey:publishableKey
                            automaticallyRequestPermissions:automaticallyRequestPermissions];
        if (result.hyperTrack != nil) {
            RCTLogInfo(@"Successfully set publishableKey and created SDK instance");
            self.hyperTrack = result.hyperTrack;
            if (startsTracking) {
                [self.hyperTrack start];
            }
            resolve(nil);
        } else {
            switch ([result.error code]) {

                case HTFatalErrorDevelopmentPublishableKeyIsEmpty:
                {
                    NSError *error = [NSError errorWithDomain: result.error.domain
                                                         code: invalidPublishableKey
                                                     userInfo: result.error.userInfo];
                    reject([NSString stringWithFormat:@"%d", (int)error.code], error.localizedDescription, error);
                }
                    break;

                case HTFatalErrorDevelopmentMissingLocationUpdatesBackgroundModeCapability:
                {
                    NSError *error = [NSError errorWithDomain: result.error.domain
                                                         code: generalError
                                                     userInfo: result.error.userInfo];
                    reject([NSString stringWithFormat:@"%d", (int)error.code], error.localizedDescription, error);
                }
                    break;

                case HTFatalErrorDevelopmentRunningOnSimulatorUnsupported:
                {
                    NSError *error = [NSError errorWithDomain: result.error.domain
                                                         code: generalError
                                                     userInfo: result.error.userInfo];
                    reject([NSString stringWithFormat:@"%d", (int)error.code], error.localizedDescription, error);
                }
                    break;

                case HTFatalErrorProductionLocationServicesUnavalible:
                {
                    NSError *error = [NSError errorWithDomain: result.error.domain
                                                         code: permissionDenied
                                                     userInfo: result.error.userInfo];
                    reject([NSString stringWithFormat:@"%d", (int)error.code], error.localizedDescription, error);
                }
                    break;

                case HTFatalErrorProductionMotionActivityServicesUnavalible:
                {
                    NSError *error = [NSError errorWithDomain: result.error.domain
                                                         code: permissionDenied
                                                     userInfo: result.error.userInfo];
                    reject([NSString stringWithFormat:@"%d", (int)error.code], error.localizedDescription, error);
                }
                    break;

                case HTFatalErrorProductionMotionActivityPermissionsDenied:
                {
                    NSError *error = [NSError errorWithDomain: result.error.domain
                                                         code: permissionDenied
                                                     userInfo: result.error.userInfo];
                    reject([NSString stringWithFormat:@"%d", (int)error.code], error.localizedDescription, error);
                }
                    break;

                default:
                {
                    NSError *error = [NSError errorWithDomain: result.error.domain
                                                         code: generalError
                                                     userInfo: result.error.userInfo];
                    reject([NSString stringWithFormat:@"%d", (int)error.code], error.localizedDescription, error);
                }
                    break;
            }
        }
    });
}

RCT_EXPORT_METHOD(isAvailable:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    if ( [self.hyperTrack availability] == HTAvailabilityAvailable) {
        resolve(@YES);
    } else {
        resolve(@NO);
    }
}

RCT_EXPORT_METHOD(setAvailability:(BOOL)availability:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    [self.hyperTrack setAvailability: availability ? HTAvailabilityAvailable : HTAvailabilityUnavailable];
    if ( [self.hyperTrack availability] == HTAvailabilityAvailable) {
        resolve(@YES);
    } else {
        resolve(@NO);
    }
}

RCT_EXPORT_METHOD(startTracking) {
    [self.hyperTrack start];
}

RCT_EXPORT_METHOD(stopTracking) {
    [self.hyperTrack stop];
}

RCT_EXPORT_METHOD(syncDeviceSettings) {
    [self.hyperTrack syncDeviceSettings];
}

RCT_EXPORT_METHOD(subscribeOnEvents) {

}

RCT_EXPORT_METHOD(enableDebugLogging:(BOOL)isEnabled) {
    [HTSDK setIsLoggingEnabled:isEnabled];
}

RCT_EXPORT_METHOD(enableMockLocation:(BOOL)isAllowed) {
    [self.hyperTrack setMockLocationsAllowed:isAllowed];
}

RCT_EXPORT_METHOD(isTracking:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    resolve([NSNumber numberWithBool:[self.hyperTrack isTracking]]);
}

RCT_EXPORT_METHOD(getLocation:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    HTLocationResult *locationResult = self.hyperTrack.location;

    switch (locationResult.error) {
        case HTLocationErrorNoError:
            resolve(@{ @"location":  @{ @"latitude": [NSNumber numberWithDouble:locationResult.location.latitude], @"longitude": [NSNumber numberWithDouble:locationResult.location.longitude] } });
            break;
        case HTLocationErrorLocationPermissionsNotDetermined:
            resolve(@{ @"error":  @{ @"code": @"location_permissions_not_determined" } });
            break;
        case HTLocationErrorLocationPermissionsCantBeAskedInBackground:
            resolve(@{ @"error":  @{ @"code": @"location_permissions_cant_be_asked_in_background" } });
            break;
        case HTLocationErrorLocationPermissionsInsufficientForBackground:
            resolve(@{ @"error":  @{ @"code": @"location_permissions_insufficient_for_background" } });
            break;
        case HTLocationErrorLocationPermissionsRestricted:
            resolve(@{ @"error":  @{ @"code": @"location_permissions_restricted" } });
            break;
        case HTLocationErrorLocationPermissionsReducedAccuracy:
            resolve(@{ @"error":  @{ @"code": @"location_permissions_reduced_accuracy" } });
            break;
        case HTLocationErrorLocationPermissionsDenied:
            resolve(@{ @"error":  @{ @"code": @"location_permissions_denied" } });
            break;
        case HTLocationErrorLocationServicesDisabled:
            resolve(@{ @"error":  @{ @"code": @"location_services_disabled" } });
            break;
        case HTLocationErrorMotionActivityPermissionsNotDetermined:
            resolve(@{ @"error":  @{ @"code": @"motion_activity_permissions_not_determined" } });
            break;
        case HTLocationErrorMotionActivityPermissionsCantBeAskedInBackground:
            resolve(@{ @"error":  @{ @"code": @"motion_activity_permissions_cant_be_asked_in_background" } });
            break;
        case HTLocationErrorMotionActivityPermissionsDenied:
            resolve(@{ @"error":  @{ @"code": @"motion_activity_permissions_denied" } });
            break;
        case HTLocationErrorMotionActivityServicesDisabled:
            resolve(@{ @"error":  @{ @"code": @"motion_activity_services_disabled" } });
            break;
        case HTLocationErrorNotRunning:
            resolve(@{ @"error":  @{ @"code": @"not_running" } });
            break;
        case HTLocationErrorGpsSignalLost:
            resolve(@{ @"error":  @{ @"code": @"gps_signal_lost" } });
            break;
        case HTLocationErrorStarting:
            resolve(@{ @"error":  @{ @"code": @"starting" } });
            break;
        case HTLocationErrorLocationMocked:
            resolve(@{ @"error":  @{ @"code": @"location_mocked" } });
            break;
    }
}

RCT_EXPORT_METHOD(getDeviceID:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    resolve(self.hyperTrack.deviceID);
}

RCT_EXPORT_METHOD(setDeviceName:(NSString *)deviceName :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    self.hyperTrack.deviceName = deviceName;
    resolve(nil);
}

RCT_EXPORT_METHOD(setMetadata:(NSDictionary<NSString*, id>*)metadata :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    HTMetadata *hyperTrackMetadata = [[HTMetadata alloc] initWithDictionary:metadata];
    if (metadata != nil) {
        [self.hyperTrack setDeviceMetadata:hyperTrackMetadata];
        resolve(nil);
    } else {
        NSError *error = [NSError errorWithDomain:@"HyperTrackMetadataError" code:-1 userInfo:@{ @"description": @"Metadata is not a valid JSON" }];
        reject([NSString stringWithFormat:@"%d", (int)error.code], error.localizedDescription, error);
    }
}

RCT_EXPORT_METHOD(addGeotag:(NSDictionary<NSString*, id>*)metadata :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    HTMetadata *hyperTrackMetadata = [[HTMetadata alloc] initWithDictionary:metadata];
    if (metadata != nil) {
        [self.hyperTrack addGeotag:hyperTrackMetadata];
        HTLocationResult *locationResult = self.hyperTrack.location;
        switch (locationResult.error) {
            case HTLocationErrorNoError:
                resolve(@{ @"location":  @{ @"latitude": [NSNumber numberWithDouble:locationResult.location.latitude], @"longitude": [NSNumber numberWithDouble:locationResult.location.longitude] } });
                break;
            case HTLocationErrorLocationPermissionsNotDetermined:
                resolve(@{ @"error":  @{ @"code": @"location_permissions_not_determined" } });
                break;
            case HTLocationErrorLocationPermissionsCantBeAskedInBackground:
                resolve(@{ @"error":  @{ @"code": @"location_permissions_cant_be_asked_in_background" } });
                break;
            case HTLocationErrorLocationPermissionsInsufficientForBackground:
                resolve(@{ @"error":  @{ @"code": @"location_permissions_insufficient_for_background" } });
                break;
            case HTLocationErrorLocationPermissionsRestricted:
                resolve(@{ @"error":  @{ @"code": @"location_permissions_restricted" } });
                break;
            case HTLocationErrorLocationPermissionsReducedAccuracy:
                resolve(@{ @"error":  @{ @"code": @"location_permissions_reduced_accuracy" } });
                break;
            case HTLocationErrorLocationPermissionsDenied:
                resolve(@{ @"error":  @{ @"code": @"location_permissions_denied" } });
                break;
            case HTLocationErrorLocationServicesDisabled:
                resolve(@{ @"error":  @{ @"code": @"location_services_disabled" } });
                break;
            case HTLocationErrorMotionActivityPermissionsNotDetermined:
                resolve(@{ @"error":  @{ @"code": @"motion_activity_permissions_not_determined" } });
                break;
            case HTLocationErrorMotionActivityPermissionsCantBeAskedInBackground:
                resolve(@{ @"error":  @{ @"code": @"motion_activity_permissions_cant_be_asked_in_background" } });
                break;
            case HTLocationErrorMotionActivityPermissionsDenied:
                resolve(@{ @"error":  @{ @"code": @"motion_activity_permissions_denied" } });
                break;
            case HTLocationErrorMotionActivityServicesDisabled:
                resolve(@{ @"error":  @{ @"code": @"motion_activity_services_disabled" } });
                break;
            case HTLocationErrorNotRunning:
                resolve(@{ @"error":  @{ @"code": @"not_running" } });
                break;
            case HTLocationErrorGpsSignalLost:
                resolve(@{ @"error":  @{ @"code": @"gps_signal_lost" } });
                break;
            case HTLocationErrorStarting:
                resolve(@{ @"error":  @{ @"code": @"starting" } });
                break;
            case HTLocationErrorLocationMocked:
                resolve(@{ @"error":  @{ @"code": @"location_mocked" } });
                break;
        }
    } else {
        NSError *error = [NSError errorWithDomain:@"HyperTrackMetadataError" code:-1 userInfo:@{ @"description": @"Geotag is not a valid JSON" }];
        reject([NSString stringWithFormat:@"%d", (int)error.code], error.localizedDescription, error);
    }
}


#pragma mark - NSNotificationCenter

- (void)addObservers {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(sendTrackingStateToRNWith:)
                                                 name:HTSDK.startedTrackingNotification
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(sendTrackingStateToRNWith:)
                                                 name:HTSDK.stoppedTrackingNotification
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(sendCriticalErrorForNotification:)
                                                 name:HTSDK.didEncounterRestorableErrorNotification
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(sendCriticalErrorForNotification:)
                                                 name:HTSDK.didEncounterUnrestorableErrorNotification
                                               object:nil];
}

- (void)sendTrackingStateToRNWith:(NSNotification*)notification {
    NSString *eventName = @"";
    if ([notification.name isEqualToString: @"HyperTrackStartedTracking"]) {
        eventName = @"onTrackingStateChanged";
    } else if ([notification.name isEqualToString: @"HyperTrackStoppedTracking"]) {
        eventName = @"onTrackingStateChanged";
    } else {
        return;
    }
    if (hasListeners) {
        [self sendEventWithName: eventName body: @([self.hyperTrack isTracking])];
    }
}

- (void)sendCriticalErrorForNotification:(NSNotification *)notification {
    [self sendError:[notification hyperTrackTrackingError]];
}

- (void)sendError:(NSError *)error {
    if (error != nil && hasListeners) {
        [self sendEventWithName: @"onTrackingErrorHyperTrack"
                           body: @{ @"code": [self convertErrorCodeToRN:[error code]], @"message": error.localizedDescription }];
    }
}

- (NSNumber *)convertErrorCodeToRN:(NSInteger)errorCode {
    switch (errorCode) {
        case HTRestorableErrorLocationPermissionsDenied:
        case HTRestorableErrorLocationServicesDisabled:
        case HTRestorableErrorMotionActivityServicesDisabled:
        case HTUnrestorableErrorMotionActivityPermissionsDenied:
        case HTFatalErrorProductionMotionActivityPermissionsDenied:
            return [NSNumber numberWithInteger:permissionDenied];
            break;
        case HTRestorableErrorTrialEnded:
        case HTRestorableErrorPaymentDefault:
            return [NSNumber numberWithInteger:authorizationError];
            break;
        case HTUnrestorableErrorInvalidPublishableKey:
        case HTFatalErrorDevelopmentPublishableKeyIsEmpty:
            return [NSNumber numberWithInteger:invalidPublishableKey];
            break;
        default:
            return [NSNumber numberWithInteger:generalError];
            break;
    }
}

@end
