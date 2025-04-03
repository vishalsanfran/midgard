import React, { useEffect, useState } from 'react';
import { View, StyleSheet, Alert } from 'react-native';
import { Camera, useCameraDevice } from 'react-native-vision-camera';
import { Button, Text } from 'react-native-paper';

const CameraScreen = () => {
  const [hasPermission, setHasPermission] = useState(false);
  const device = useCameraDevice('back');

  useEffect(() => {
    checkPermission();
  }, []);

  const checkPermission = async () => {
    const cameraPermission = await Camera.requestCameraPermission();
    setHasPermission(cameraPermission === 'granted');
  };

  if (!hasPermission) {
    return (
      <View style={styles.container}>
        <Text>No access to camera</Text>
        <Button onPress={checkPermission}>Grant Permission</Button>
      </View>
    );
  }

  if (!device) {
    return (
      <View style={styles.container}>
        <Text>No camera device found</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Camera
        style={StyleSheet.absoluteFill}
        device={device}
        isActive={true}
      />
      <View style={styles.controls}>
        <Button
          mode="contained"
          onPress={() => Alert.alert('Coming soon', 'Text recognition feature will be implemented soon')}
        >
          Capture & Recognize Text
        </Button>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  controls: {
    position: 'absolute',
    bottom: 20,
    left: 0,
    right: 0,
    padding: 20,
    alignItems: 'center',
  },
});

export default CameraScreen; 