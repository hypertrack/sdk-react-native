
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNPluginAndroidLocationServicesGoogleSpec.h"

@interface PluginAndroidLocationServicesGoogle : NSObject <NativePluginAndroidLocationServicesGoogleSpec>
#else
#import <React/RCTBridgeModule.h>

@interface PluginAndroidLocationServicesGoogle : NSObject <RCTBridgeModule>
#endif

@end
