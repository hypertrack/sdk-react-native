#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTLog.h>
#import <React/RCTEventDispatcher.h>

@interface RCT_EXTERN_MODULE(HyperTrackSdk,RCTEventEmitter)

RCT_EXTERN_METHOD(increment: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(decrement: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_REMAP_METHOD(multiply,
                 multiplyWithA:(nonnull NSNumber*)a withB:(nonnull NSNumber*)b
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  NSNumber *result = @([a floatValue] * [b floatValue]);

  resolve(result);
}

@end

