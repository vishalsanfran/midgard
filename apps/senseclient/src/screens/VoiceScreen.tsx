import React, { useState } from 'react';
import { View, StyleSheet, Alert } from 'react-native';
import { Button, Text } from 'react-native-paper';

const VoiceScreen = () => {
  const [isListening, setIsListening] = useState(false);
  const [transcript, setTranscript] = useState('');

  const startListening = async () => {
    try {
      setIsListening(true);
      setTranscript('');
      // Voice recognition implementation will be added here
      Alert.alert('Coming soon', 'Voice recognition feature will be implemented soon');
    } catch (error) {
      Alert.alert('Error', 'Failed to start voice recognition');
    } finally {
      setIsListening(false);
    }
  };

  const stopListening = () => {
    setIsListening(false);
  };

  return (
    <View style={styles.container}>
      <Text variant="headlineSmall" style={styles.title}>
        Voice Recognition
      </Text>
      
      <View style={styles.content}>
        <Text style={styles.transcript}>
          {transcript || 'Press the button to start voice recognition'}
        </Text>
        
        <Button
          mode="contained"
          onPress={isListening ? stopListening : startListening}
          style={styles.button}
        >
          {isListening ? 'Stop Listening' : 'Start Listening'}
        </Button>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  title: {
    marginBottom: 20,
    textAlign: 'center',
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  transcript: {
    marginBottom: 30,
    textAlign: 'center',
    fontSize: 16,
  },
  button: {
    width: '100%',
    maxWidth: 300,
  },
});

export default VoiceScreen; 