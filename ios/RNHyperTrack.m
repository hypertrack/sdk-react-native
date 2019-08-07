#import "RNHyperTrack.h"
#import <React/RCTLog.h>
#import <React/RCTEventDispatcher.h>

NSString * const HyperTrackErrorEvent     = @"hypertrack.error";
NSString * const HyperTrackTrackingEvent  = @"hypertrack.tracking.event";

@implementation RNHyperTrack
  
  
RCT_EXPORT_MODULE();
  
- (dispatch_queue_t)methodQueue {
  return dispatch_get_main_queue();
}
  
- (NSArray<NSString *> *)supportedEvents {
  return @[HyperTrackErrorEvent, HyperTrackTrackingEvent];
}
  
#pragma mark - HyperTrack SDK
  
RCT_EXPORT_METHOD(initialize :(NSString *)publishableKey startsTracking :(BOOL)startsTracking) {
  RCTLogInfo(@"Initializing HyperTrack with publishableKey: %@", publishableKey);
  __weak __typeof(self) weakSelf = self;
  dispatch_async(dispatch_get_main_queue(), ^{
    [weakSelf addObservers];
    [HTSDK initializeWithPublishableKey:publishableKey delegate:weakSelf startsTracking:startsTracking requestsPermissions:startsTracking];
  });
}

RCT_EXPORT_METHOD(startTracking) {
  [HTSDK startTrackingAndRequestPermissions:YES];
}

RCT_EXPORT_METHOD(stopTracking) {
  [HTSDK stopTracking];
}

RCT_EXPORT_METHOD(isTracking :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  resolve([NSNumber numberWithBool:[HTSDK isTracking]]);
}

RCT_EXPORT_METHOD(getDeviceID: (RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  NSString * deviceId = [HTSDK deviceID];
  resolve(deviceId);
}

RCT_EXPORT_METHOD(setDevice :(NSString *)name :(NSDictionary<NSString*, id>*)metadata :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  [HTSDK setDeviceWithName:name metadata:metadata completionHandler:^(HTSDKDeviceNameError *error) {
    if (error) {
      NSError * nsError = [NSError errorWithDomain:@"HTSDKDeviceNameError"
                                              code:error.type
                                          userInfo:@{@"description" : error.errorMessage}];
      reject(@"Error", error.errorMessage, nsError);
    } else {
      resolve(nil);
    }
  }];
}
  
RCT_EXPORT_METHOD(setTripMarker:(NSDictionary<NSString*, id>*)metadata :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  [HTSDK setTripMarker:metadata
     completionHandler:^(HTSDKCustomEventError * _Nullable error) {
                     if (error) {
                       NSError * nsError = [NSError errorWithDomain:@"HTSDKCustomEventError"
                                                               code:error.type
                                                           userInfo:@{@"description" : error.errorMessage}];
                       reject(@"Error", error.errorMessage, nsError);
                     } else {
                       resolve(nil);
                     }
                   }];
}
  
#pragma mark - HTSDKDelegate
  
- (void)hyperTrack:(Class)hyperTrack didEncounterCriticalError:(HTSDKCriticalError *)criticalError {
  [self sendEventWithName: HyperTrackErrorEvent body: @{@"error": criticalError.errorMessage}];
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
  
}
  
- (void)sendTrackingStateToRNWith:(NSNotification*)notification {
  [self sendEventWithName: HyperTrackTrackingEvent body: @{@"isTracking": @([HTSDK isTracking])}];
}
  
@end
