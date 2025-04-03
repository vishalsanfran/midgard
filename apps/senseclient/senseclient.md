# Sense Client Documentation

## Overview

Sense Client is a React Native mobile application designed for multi-modal data processing. It provides a user-friendly interface for interacting with various data processing capabilities, including text extraction from images, voice processing, and computer vision features.

## Features

### Current Features
- Camera access for image capture
- Basic voice recording interface
- Material Design UI components
- TypeScript support for type safety
- Navigation between different processing modes

### Planned Features
- Text extraction from images using ML Kit
- Voice-to-text conversion
- Object detection and recognition
- Integration with backend sentiment analysis
- Real-time processing feedback

## Project Structure

```
senseclient/
├── src/
│   ├── assets/           # Static assets (images, fonts)
│   ├── components/       # Reusable UI components
│   ├── hooks/            # Custom React hooks
│   ├── navigation/       # Navigation configuration
│   ├── screens/          # Screen components
│   ├── services/         # API and external service integrations
│   ├── types/            # TypeScript type definitions
│   └── utils/            # Utility functions
├── App.tsx               # Root component
├── babel.config.js       # Babel configuration
├── tsconfig.json         # TypeScript configuration
└── package.json          # Project dependencies
```

## Dependencies

### Core Dependencies
- React Native
- React Navigation
- React Native Paper (UI components)
- React Native Vision Camera
- React Native Voice

### Development Dependencies
- TypeScript
- Metro React Native Babel Preset
- React Native Reanimated

## Setup and Installation

### Prerequisites
- Node.js (v14 or later)
- npm or yarn
- Android Studio (for Android development)
- Android SDK
- Java Development Kit (JDK)

### Installation Steps

1. Clone the repository:
```bash
git clone <repository-url>
cd apps/senseclient
```

2. Install dependencies:
```bash
npm install
```

3. Start the Metro bundler:
```bash
npm start
```

4. Run on Android:
```bash
npm run android
```

## Development

### Adding New Features

1. Create new screen components in `src/screens/`
2. Add navigation routes in `src/navigation/AppNavigator.tsx`
3. Implement business logic in appropriate service files
4. Add necessary type definitions in `src/types/`

### Code Style

- Follow TypeScript best practices
- Use functional components with hooks
- Implement proper error handling
- Add appropriate comments and documentation
- Follow the project's folder structure

## Testing

To run tests:
```bash
npm test
```

## Building for Production

### Android
```bash
cd android
./gradlew assembleRelease
```

## Troubleshooting

### Common Issues

1. **Metro Bundler Issues**
   - Clear Metro cache: `npm start -- --reset-cache`
   - Check port availability

2. **Android Build Issues**
   - Ensure Android SDK is properly configured
   - Check Gradle version compatibility
   - Verify JDK installation

3. **TypeScript Errors**
   - Check type definitions
   - Verify tsconfig.json settings
   - Update @types packages if needed

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

[Add your license here] 