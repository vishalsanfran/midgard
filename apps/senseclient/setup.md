# Development Environment Setup

This guide will help you set up your development environment for the Sense Client React Native application.

## Prerequisites

### 1. Node.js and npm
- Install Node.js (LTS version recommended)
- npm comes bundled with Node.js
- Verify installation:
  ```bash
  node --version
  npm --version
  ```

### 2. Android Studio and Android SDK
1. Download and install [Android Studio](https://developer.android.com/studio)
2. During installation, make sure to select:
   - Android SDK
   - Android SDK Platform
   - Android Virtual Device
   - Performance (Intel ® HAXM) [for Intel chips]
   - Android Emulator

3. After installation:
   - Open Android Studio
   - Go to "More Actions" or "Configure"
   - Select "SDK Manager"
   - In the SDK Platforms tab, select:
     - Android 13.0 (API Level 33)
     - Android 12.0 (API Level 31)
   - In the SDK Tools tab, ensure you have:
     - Android SDK Build-Tools
     - Android SDK Command-line Tools
     - Android Emulator
     - Android SDK Platform-Tools

4. Set up environment variables:
   ```bash
   # Add to your shell profile (~/.zshrc, ~/.bash_profile, etc.)
   export ANDROID_HOME=$HOME/Library/Android/sdk
   export PATH=$PATH:$ANDROID_HOME/emulator
   export PATH=$PATH:$ANDROID_HOME/platform-tools
   ```

### 3. Java Development Kit (JDK)
- Install JDK 17 or later
- Verify installation:
  ```bash
  java -version
  ```

### 4. Watchman (for macOS)
```bash
brew install watchman
```

## Project Setup

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd midgard/apps/senseclient
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start Metro bundler:
   ```bash
   npm start
   ```

4. In a new terminal, run the Android app:
   ```bash
   npm run android
   ```

## Troubleshooting

### Common Issues

1. **Android SDK not found**
   - Verify ANDROID_HOME environment variable
   - Check if Android Studio is properly installed
   - Ensure SDK platforms and tools are installed

2. **Java version issues**
   - Make sure you have JDK 17 or later installed
   - Verify JAVA_HOME environment variable

3. **Emulator issues**
   - Create a new virtual device in Android Studio
   - Ensure HAXM is installed (for Intel chips)
   - Check if virtualization is enabled in BIOS

4. **Build failures**
   - Clean the project:
     ```bash
     cd android
     ./gradlew clean
     cd ..
     ```
   - Rebuild:
     ```bash
     npm run android
     ```

### Environment Variables

Add these to your shell profile:
```bash
# Android
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/platform-tools

# Java
export JAVA_HOME=$(/usr/libexec/java_home)
export PATH=$PATH:$JAVA_HOME/bin
```

## Development Workflow

1. Start Metro bundler:
   ```bash
   npm start
   ```

2. In a new terminal, run the app:
   ```bash
   npm run android
   ```

3. For development:
   - Press 'R' to reload
   - Press 'D' to open developer menu
   - Press 'M' to toggle menu (in emulator)

## Testing

Run tests:
```bash
npm test
```

## Building for Release

1. Generate a signed APK:
   ```bash
   cd android
   ./gradlew assembleRelease
   ```

2. The APK will be available at:
   ```
   android/app/build/outputs/apk/release/app-release.apk
   ```

## Additional Resources

- [React Native Documentation](https://reactnative.dev/docs/getting-started)
- [Android Studio Documentation](https://developer.android.com/studio)
- [Node.js Documentation](https://nodejs.org/en/docs/)
- [Java Documentation](https://docs.oracle.com/en/java/) 