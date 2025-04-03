import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import HomeScreen from '../screens/HomeScreen';
import CameraScreen from '../screens/CameraScreen';
import VoiceScreen from '../screens/VoiceScreen';

export type RootStackParamList = {
  Home: undefined;
  Camera: undefined;
  Voice: undefined;
};

const Stack = createNativeStackNavigator<RootStackParamList>();

const AppNavigator = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Home">
        <Stack.Screen 
          name="Home" 
          component={HomeScreen} 
          options={{ title: 'Sense Client' }}
        />
        <Stack.Screen 
          name="Camera" 
          component={CameraScreen} 
          options={{ title: 'Camera' }}
        />
        <Stack.Screen 
          name="Voice" 
          component={VoiceScreen} 
          options={{ title: 'Voice' }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default AppNavigator; 