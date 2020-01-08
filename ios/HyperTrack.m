#import "HyperTrack.h"
#import <React/RCTLog.h>
#import <React/RCTEventDispatcher.h>

@interface HyperTrack ()

@property(nonatomic) HTSDK *hyperTrack;

@end


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
        HTResult *result = [HTSDK makeSDKWithPublishableKey:publishableKey];
        if (result.hyperTrack != nil) {
          RCTLogInfo(@"Successfully set publishableKey and created SDK instance");
          self.hyperTrack = result.hyperTrack;
          if (startsTracking) {
            [self.hyperTrack start];
          }
        } else if (result.error != nil) {
          [self sendError:result.error];
        }
        resolve(nil);
    });
}

RCT_EXPORT_METHOD(startTracking) {
    [self.hyperTrack start];
}

RCT_EXPORT_METHOD(stopTracking) {
    [self.hyperTrack stop];
}

RCT_EXPORT_METHOD(subscribeOnEvents) {
  
}

RCT_EXPORT_METHOD(enableDebugLogging:(BOOL)isEnable) {
  
}

RCT_EXPORT_METHOD(isTracking:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    resolve([NSNumber numberWithBool:[self.hyperTrack isRunning]]);
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

RCT_EXPORT_METHOD(setTripMarker:(NSDictionary<NSString*, id>*)metadata :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  HTMetadata *hyperTrackMetadata = [[HTMetadata alloc] initWithDictionary:metadata];
  if (metadata != nil) {
    [self.hyperTrack addTripMarker:hyperTrackMetadata];
    resolve(nil);
  } else {
    NSError *error = [NSError errorWithDomain:@"HyperTrackMetadataError" code:-1 userInfo:@{ @"description": @"Trip marker is not a valid JSON" }];
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
    eventName = @"onTrackingStartHyperTrack";
  } else if ([notification.name isEqualToString: @"HyperTrackStoppedTracking"]) {
    eventName = @"onTrackingStopHyperTrack";
  } else {
    return;
  }
  [self sendEventWithName: eventName body: @{@"isTracking": @([self.hyperTrack isRunning])}];
}

- (void)sendCriticalErrorForNotification:(NSNotification *)notification {
  [self sendError:[notification hyperTrackTrackingError]];
}

- (void)sendError:(NSError *)error {
  if (error != nil) {
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
