import { AppRegistry } from 'react-native';
import App from './src/App.ios';
import { name as appName } from './app.json';

AppRegistry.registerComponent(appName, () => App);
