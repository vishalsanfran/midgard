import React from 'react';
import { View, StyleSheet } from 'react-native';
import { Button, Text } from 'react-native-paper';
import { useNavigation } from '@react-navigation/native';
import type { NativeStackNavigationProp } from '@react-navigation/native-stack';
import type { RootStackParamList } from '../navigation/AppNavigator';

type HomeScreenNavigationProp = NativeStackNavigationProp<RootStackParamList, 'Home'>;

const HomeScreen = () => {
  const navigation = useNavigation<HomeScreenNavigationProp>();

  return (
    <View style={styles.container}>
      <Text variant="headlineMedium" style={styles.title}>
        Sense Client
      </Text>
      <Text variant="bodyLarge" style={styles.subtitle}>
        Choose a feature to explore
      </Text>
      
      <View style={styles.buttonContainer}>
        <Button
          mode="contained"
          onPress={() => navigation.navigate('Camera')}
          style={styles.button}
        >
          Camera & Text Recognition
        </Button>
        
        <Button
          mode="contained"
          onPress={() => navigation.navigate('Voice')}
          style={styles.button}
        >
          Voice Processing
        </Button>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    marginBottom: 10,
    textAlign: 'center',
  },
  subtitle: {
    marginBottom: 30,
    textAlign: 'center',
  },
  buttonContainer: {
    width: '100%',
    maxWidth: 300,
  },
  button: {
    marginVertical: 10,
  },
});

export default HomeScreen; 