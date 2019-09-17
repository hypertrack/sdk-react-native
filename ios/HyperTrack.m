#import "HyperTrack.h"
#import <React/RCTLog.h>
#import <React/RCTEventDispatcher.h>

@implementation HyperTrack

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
             @"onTrackingStopHyperTrack",
             @"onTrackingStartHyperTrack"];
}

#pragma mark - HyperTrack SDK

RCT_EXPORT_METHOD(initialize :(NSString *)publishableKey startsTracking :(BOOL)startsTracking :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTLogInfo(@"Initializing HyperTrack with publishableKey: %@", publishableKey);
    __weak __typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        [weakSelf addObservers];
        HTSDK.publishableKey = publishableKey;
      
        if (startsTracking) {
          [HTSDK startTracking];
        }
        resolve(nil);
    });
}

RCT_EXPORT_METHOD(startTracking) {
    [HTSDK startTracking];
}

RCT_EXPORT_METHOD(stopTracking) {
    [HTSDK stopTracking];
}

RCT_EXPORT_METHOD(subscribeOnEvents) {
  
}

RCT_EXPORT_METHOD(enableDebugLogging:(BOOL)isEnable) {
  
}

RCT_EXPORT_METHOD(isTracking:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    resolve([NSNumber numberWithBool:[HTSDK isTracking]]);
}

RCT_EXPORT_METHOD(getDeviceID:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    NSString * deviceId = [HTSDK deviceID];
    resolve(deviceId);
}

RCT_EXPORT_METHOD(setDeviceName:(NSString *)deviceName :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  HTSDK.deviceName = deviceName;
  resolve(nil);
}

RCT_EXPORT_METHOD(setMetadata:(NSDictionary<NSString*, id>*)metadata :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  [HTSDKMetadata makeMetadata:metadata
                      success:^(HTSDKMetadata * _Nonnull metadata) {
                        HTSDK.metadata = metadata;
                        resolve(nil);
                      } failure:^(HTSDKMetadataError * _Nonnull error) {
                        NSError * nsError = [NSError errorWithDomain:@"HyperTrackMetadataError"
                                                                code:-1
                                                            userInfo:@{@"description": error.errorMessage}];
                        reject([NSString stringWithFormat:@"%d", (int)nsError.code], nsError.localizedDescription, nsError);
                      }];
}

RCT_EXPORT_METHOD(setTripMarker:(NSDictionary<NSString*, id>*)metadata :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  [HTSDKMetadata makeMetadata:metadata
                      success:^(HTSDKMetadata * _Nonnull metadata) {
                        [HTSDK setTripMarker: metadata];
                        resolve(nil);
                      } failure:^(HTSDKMetadataError * _Nonnull error) {
                        NSError * nsError = [NSError errorWithDomain:@"HyperTrackMetadataError"
                                                                code:-1
                                                            userInfo:@{@"description": error.errorMessage}];
                        reject([NSString stringWithFormat:@"%d", (int)nsError.code], nsError.localizedDescription, nsError);
                      }];
}

#pragma mark - NSNotificationCenter

- (void)addObservers {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(sendTrackingStateToRNWith:)
                                                 name:HyperTrackStartedTrackingNotification
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(sendTrackingStateToRNWith:)
                                                 name:HyperTrackStoppedTrackingNotification
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(sendCriticalErrorToRNWith:)
                                                 name:HyperTrackDidEncounterCriticalErrorNotification
                                               object:nil];
}

- (void)sendTrackingStateToRNWith:(NSNotification*)notification {
  NSString *eventName = @"";
  if ([notification.name isEqualToString: @"HyperTrackStartedTracking"]) {
    eventName = @"onTrackingStartHyperTrack";
  } else if ([notification.name isEqualToString: @"HyperTrackStoppedTracking"]) {
    eventName = @"onTrackingStopHyperTrack";
  } else {
    return;
  }
  [self sendEventWithName: eventName body: @{@"isTracking": @([HTSDK isTracking])}];
}

- (void)sendCriticalErrorToRNWith:(NSNotification*)notification {
  [self sendEventWithName: @"onTrackingErrorHyperTrack" body: @{@"code": [self convertNativeErrorTypeToRN:[notification HTSDKError].type],
                                                                @"message": [notification HTSDKError].errorMessage}];
}

- (NSNumber*)convertNativeErrorTypeToRN:(HTSDKCriticalErrorType)nativeType {
  switch (nativeType) {
    case criticalErrorPermissionDenied:
      return [NSNumber numberWithInteger:permissionDenied];
    case criticalErrorAuthorizationError:
      return [NSNumber numberWithInteger:authorizationError];
    case criticalErrorInvalidPublishableKey:
      return [NSNumber numberWithInteger:invalidPublishableKey];
    case criticalErrorGeneralError:
      return [NSNumber numberWithInteger:generalError];
  }
}

@end
