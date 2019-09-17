import React, {Component} from 'react';
import {StyleSheet, Text, Button, View} from 'react-native';
import {HyperTrack, CriticalErrors} from 'hypertrack-sdk-react-native';

const PUBLISHABLE_KEY = "paste_your_key_here";
export default class App extends Component<{}> {

    state = {
        deviceId: "",
        isTracking: true,
        trackingState: "Started",
    };

    _initializeHyperTrack = async () => {
        HyperTrack.enableDebugLogging(true);
        this.hyperTrack = await HyperTrack.createInstance(PUBLISHABLE_KEY);
        this.hyperTrack.setDeviceName("RNElvis");
        this.hyperTrack.setMetadata({key1: "vaLue", key2: "8"});
        this.hyperTrack.registerTrackingListeners(this,
            (error) => {
                if (error.code === CriticalErrors.INVALID_PUBLISHABLE_KEY
                    || error.code === CriticalErrors.AUTHORIZATION_FAILED) {
                    console.log("Initialization failed")
                } else {
                    console.log("Tracking failed")
                }
                this.setState({
                    trackingState: "Stopped with error: " + ((error.code + " - " + error.message) || "unknown"),
                    isTracking: false
                })
            },
            () => this.setState({trackingState: "Started", isTracking: true}),
            () => this.setState({trackingState: "Stopped", isTracking: false}));
        const deviceId = await this.hyperTrack.getDeviceID();
        console.log(deviceId);
        this.setState({deviceId: deviceId});
    };

    _onPressTrackingButton  = async () => {
        const isTracking = await this.hyperTrack.isTracking();

        if (isTracking) this.hyperTrack.stopTracking();
        else this.hyperTrack.startTracking();
    };

    componentWillMount() {
        this._initializeHyperTrack();
    }

    componentWillUnmount() {
        this.hyperTrack.unregisterTrackingListeners(this);
    }

    render() {
        const {
            deviceId,
            isTracking,
            trackingState
        } = this.state;

        return (
            <View style={styles.container}>
                <Text style={styles.text}>{deviceId}</Text>
                <Button
                    style={styles.button}
                    title={isTracking ? "Stop tracking" : "Start tracking"}
                    onPress={this._onPressTrackingButton.bind(this)}
                />
                <Text style={styles.textTitle}>{"Tracking state"}</Text>
                <Text style={styles.text}>{trackingState}</Text>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    button: {
        fontSize: 20,
        margin: 10,
    },
    textTitle: {
        fontSize: 16,
        fontWeight: 'bold',
        marginTop: 15
    },
    text: {
        fontSize: 14,
        margin: 10
    },
});
