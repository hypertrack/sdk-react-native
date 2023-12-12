import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-plugin_android_push_service_firebase' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// @ts-ignore
const PluginAndroidPushServiceFirebase = NativeModules.PluginAndroidPushServiceFirebase
  ? NativeModules.PluginAndroidPushServiceFirebase
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );
