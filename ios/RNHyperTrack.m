#import "RNHyperTrack.h"
#import <React/RCTLog.h>
#import <React/RCTEventDispatcher.h>
@import HyperTrack;

@implementation RNHyperTrack


RCT_EXPORT_MODULE();

/**
 HyperTrackEvent methods
 */

- (dispatch_queue_t)methodQueue {
	return dispatch_get_main_queue();
}

- (NSArray<NSString *> *)supportedEvents {
	return @[@"location.changed"];
}

/**
 Setup methods
 */

RCT_EXPORT_METHOD(initialize :(NSString *)token) {
    RCTLogInfo(@"Initializing HyperTrack with token: %@", token);
    [HTSDK initializeWithPublishableKey:token delegate:nil startsTracking:YES requestsPermissions:YES];
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

RCT_EXPORT_METHOD(getDeviceId: (RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	NSString * deviceId = [HTSDK deviceID];
	resolve(@[deviceId]);
}

RCT_EXPORT_METHOD(setDeviceWithName :(NSString *)name :(NSDictionary<NSString*, id>*)metadata :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  [HTSDK setDeviceWithName:name metadata:metadata completionHandler:^(HTSDKDeviceNameError *error) {
    if (error) {
      NSDictionary * userInfo = @{@"description" : error.errorMessage};
      NSError * nsError = [NSError errorWithDomain:@"HTSDKDeviceNameError"
                                              code:error.type
                                          userInfo:userInfo];
      reject(@"Error", @"", nsError);
    } else {
      resolve(nil);
    }
  }];
}

RCT_EXPORT_METHOD(sendCustomEventWithMetadata :(NSDictionary<NSString*, id>*)metadata :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  [HTSDK sendCustomEventWithMetadata:metadata
                   completionHandler:^(HTSDKCustomEventError * _Nullable error) {
                     if (error) {
                       NSDictionary * userInfo = @{@"description" : error.errorMessage};
                       NSError * nsError = [NSError errorWithDomain:@"HTSDKCustomEventError"
                                                               code:error.type
                                                           userInfo:userInfo];
                       reject(@"Error", @"", nsError);
                     } else {
                       resolve(nil);
                     }
                   }];
}

@end

