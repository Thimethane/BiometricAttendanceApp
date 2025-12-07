# 📱 BiometricAttendanceApp

A modern Kotlin-based Android application that enables touch-free employee attendance using smartphone biometric authentication and GPS validation—ideal for post-COVID workplace requirements.

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple.svg)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-8.0%2B-green.svg)](https://developer.android.com)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.0-blue.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📌 Table of Contents
- [Project Overview](#-project-overview)
- [Key Features](#-key-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Installation & Setup](#-installation--setup)
- [Configuration](#-configuration)
- [Usage Guide](#-usage-guide)
- [Security Features](#-security-features)
- [Testing](#-testing)
- [Known Limitations](#-known-limitations)
- [Future Enhancements](#-future-enhancements)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)

---

## 📌 Project Overview

Organizations returning to physical offices after COVID-19 need safe, contactless attendance systems. **BiometricAttendanceApp** provides a secure, touch-free solution that combines:

- 🔐 **Device biometric authentication** (fingerprint/face unlock via Android BiometricPrompt)
- 📍 **GPS-based office location validation** (geofencing)
- 💾 **Secure local storage** using SQLite via Room
- 🚫 **Duplicate entry prevention** (no double check-ins/check-outs)

### Problem Statement
Traditional attendance systems require:
- Physical contact (fingerprint scanners, punch cards)
- Shared devices that pose hygiene risks
- Manual verification processes

### Solution
A personal smartphone-based system where employees use their own devices with built-in security features, eliminating shared touchpoints while maintaining accuracy and accountability.

---

## 🚀 Key Features

### 👤 User Management
- **Sign-Up**
    - Full name validation
    - Email validation (regex pattern)
    - Password requirements (min 8 chars, 1 uppercase, 1 number)
    - Secure password hashing using SHA-256 with salt
    - Employee ID auto-generation

- **Sign-In**
    - Email + password authentication
    - Session management with auto-logout
    - Remember me functionality
    - Password recovery flow

### 🏠 Home Dashboard
- Real-time attendance status indicator
- Quick action buttons:
    - ✅ Check-In
    - ✅ Check-Out
    - 📊 View Attendance History
- Today's work duration calculator
- Last check-in/check-out timestamp display

### 🔐 Biometric Authentication
- **Android BiometricPrompt API** integration
- **Two-Phase Flow**:
    1. **First-time users**: Register biometric → save flag in database
    2. **Returning users**: Authenticate with device biometric
- **Fallback Options**:
    - PIN/Pattern if biometric fails
    - Maximum retry limit (3 attempts)
- **Supported Biometrics**:
    - Fingerprint
    - Face Recognition
    - Iris Scan (device-dependent)

### 📍 GPS Location Validation
- **FusedLocationProviderClient** for accurate location
- **Geofencing Parameters**:
    - Office coordinates: Configurable (lat, lng)
    - Allowed radius: 200 meters (configurable)
    - Accuracy threshold: < 50 meters
- **Validation Rules**:
    - Blocks attendance outside office boundaries
    - Handles GPS accuracy issues gracefully
    - Indoor location fallback using network provider
- **Battery Optimization**:
    - Location fetched only during check-in/check-out
    - No background tracking

### 📅 Attendance History
- **RecyclerView/LazyColumn** display with:
    - Date and time (formatted: DD MMM YYYY, HH:MM AM/PM)
    - Check-in and check-out timestamps
    - Work duration calculation
    - Status indicators (Present/Partial/Absent)
- **Filtering Options**:
    - Date range picker
    - Monthly view
    - Weekly view
- **Duplicate Prevention**:
    - One check-in per day
    - One check-out per day
    - Smart detection of incomplete entries

### 📊 Additional Features
- **Statistics Dashboard**:
    - Total working days this month
    - Average daily work hours
    - On-time arrival percentage
- **Notifications**:
    - Check-in reminder at work start time
    - Check-out reminder at work end time
- **Export Functionality**:
    - Export attendance as CSV
    - Share via email/messaging apps

---

## 🧱 Tech Stack

| Category | Technology | Version | Purpose |
|---------|------------|---------|---------|
| **Language** | Kotlin | 1.9.0 | Primary development language |
| **UI Framework** | Jetpack Compose | 1.5.0 | Modern declarative UI |
| **Architecture** | MVVM | - | Separation of concerns |
| **Database** | Room (SQLite) | 2.5.2 | Local data persistence |
| **Biometric** | BiometricPrompt API | androidx.biometric:1.2.0 | Device authentication |
| **Location** | Fused Location Provider | play-services-location:21.0.1 | GPS services |
| **Async** | Kotlin Coroutines | 1.7.3 | Asynchronous operations |
| **State Management** | StateFlow/ViewModel | - | Reactive UI updates |
| **Dependency Injection** | Hilt | 2.48 | Dependency management |
| **Navigation** | Navigation Compose | 2.7.0 | Screen navigation |
| **Security** | Android Keystore | - | Secure key storage |
| **Testing** | JUnit, Mockito, Compose Test | - | Unit & UI testing |

### Dependencies (build.gradle.kts)
```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.timothee.biometricattendance"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.timothee.biometricattendance"
        minSdk = 24  // Android 7.0 (Nougat)
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")

    // Jetpack Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // ViewModel Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    ksp("androidx.room:room-compiler:2.6.0")

    // Biometric Authentication (Compatible with API 24+)
    implementation("androidx.biometric:biometric:1.2.0-alpha05")

    // Location Services (Compatible with API 24+)
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // Security Crypto (Compatible with API 23+)
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

---

## 🏗️ Architecture

### MVVM Pattern (Model-View-ViewModel)

```
┌─────────────────────────────────────────────────────────┐
│                         View Layer                      │
│              (Composable Screens & UI)                  │
└───────────────────┬─────────────────────────────────────┘
                    │ User Actions
                    ▼
┌─────────────────────────────────────────────────────────┐
│                    ViewModel Layer                      │
│         (Business Logic & State Management)             │
│                                                         │
│  • AuthViewModel                                        │
│  • AttendanceViewModel                                  │
│  • LocationViewModel                                    │
│  • BiometricViewModel                                   │
└───────────────────┬─────────────────────────────────────┘
                    │ Data Operations
                    ▼
┌─────────────────────────────────────────────────────────┐
│                   Repository Layer                      │
│              (Data Source Abstraction)                  │
│                                                         │
│  • UserRepository                                       │
│  • AttendanceRepository                                 │
└───────────────────┬─────────────────────────────────────┘
                    │
        ┌───────────┴───────────┐
        ▼                       ▼
┌───────────────┐       ┌───────────────┐
│  Room Database│       │  LocationAPI  │
│  (SQLite)     │       │  BiometricAPI │
└───────────────┘       └───────────────┘
```

### Data Flow
1. **UI** emits user actions to ViewModel
2. **ViewModel** processes business logic and calls Repository
3. **Repository** fetches/stores data from Database or APIs
4. **StateFlow** emits updates back to UI
5. **UI** recomposes based on new state

---

## 📂 Project Structure

```
BiometricAttendanceApp/
├── gradle/
├── .gitignore
├── build.gradle.kts
├── settings.gradle.kts
├── README.md
└── LICENSE


app/src/main/java/com/timothee/biometricattendance/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   ├── UserDao.kt
│   │   │   └── AttendanceDao.kt
│   │   ├── entities/
│   │   │   ├── User.kt
│   │   │   └── Attendance.kt
│   │   └── database/
│   │       └── AppDatabase.kt
│   └── repository/
│       ├── UserRepository.kt
│       └── AttendanceRepository.kt
└── utils/
    ├── Constants.kt
    ├── ValidationUtils.kt
    ├── SecurityUtils.kt
    ├── DateTimeUtils.kt
    ├── LocationUtils.kt
    ├── SessionManager.kt
    ├── BiometricUtils.kt
    └── PermissionUtils.kt
```

---

## 🔧 Installation & Setup

### Prerequisites
- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: 17 or higher
- **Minimum Android SDK**: 26 (Android 8.0 Oreo)
- **Target Android SDK**: 34 (Android 14)
- **Device Requirements**:
    - Biometric hardware (fingerprint sensor or face unlock)
    - GPS/Location services
    - Minimum 50MB storage

### Step 1: Clone Repository
```bash
git clone https://github.com/Thimethane/BiometricAttendanceApp.git
cd BiometricAttendanceApp
```

### Step 2: Open in Android Studio
1. Launch Android Studio
2. Select **File → Open**
3. Navigate to cloned directory
4. Click **OK** and wait for Gradle sync

### Step 3: Configure Environment
Create `local.properties` in root directory:
```properties
sdk.dir=/path/to/Android/sdk
```

### Step 4: Build Project
```bash
./gradlew clean build
```

### Step 5: Run on Device/Emulator
- Connect physical device with USB debugging enabled
- Or start Android Emulator (with biometric enrollment)
- Click **Run** (▶️) in Android Studio

---

## ⚙️ Configuration

### Office Location Setup
Edit `utils/Constants.kt`:
```kotlin
object Constants {
    // Office GPS Coordinates
    const val OFFICE_LATITUDE = 37.7749  // San Francisco example
    const val OFFICE_LONGITUDE = -122.4194
    const val OFFICE_RADIUS_METERS = 200.0  // Allowed check-in radius
    
    // Work Hours
    const val WORK_START_HOUR = 9  // 9:00 AM
    const val WORK_END_HOUR = 17   // 5:00 PM
    
    // Attendance Rules
    const val MAX_BIOMETRIC_RETRIES = 3
    const val SESSION_TIMEOUT_MINUTES = 30
}
```

### Permissions (AndroidManifest.xml)
```xml
<!-- Already included in manifest -->
<uses-permission android:name="android.permission.USE_BIOMETRIC" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET" />
```

### Biometric Configuration
System handles automatically, but ensure device has:
- Enrolled fingerprint/face in device settings
- Screen lock enabled (PIN/Pattern/Password)

---

## 📖 Usage Guide

### First-Time Setup

#### 1. Sign Up
- Open app → Tap **"Sign Up"**
- Enter:
    - Full Name
    - Email Address
    - Password (min 8 chars, 1 uppercase, 1 number)
- Tap **"Create Account"**

#### 2. Enable Biometric
- After sign-up, app prompts: **"Register Biometric?"**
- Tap **"Yes"** → Follow device biometric prompt
- Success message: **"Biometric Registered!"**

#### 3. Grant Location Permission
- App requests: **"Allow location access?"**
- Tap **"While using the app"**

### Daily Usage

#### Check-In
1. Open app at office location
2. Tap **"Check In"** button
3. Authenticate with fingerprint/face
4. GPS validates location
5. Success: **"Checked in at 9:05 AM"**

#### Check-Out
1. Tap **"Check Out"** button
2. Authenticate with biometric
3. GPS validates location
4. Success: **"Checked out at 5:30 PM. Duration: 8h 25m"**

#### View History
1. Tap **"Attendance History"**
2. Scroll through records
3. Filter by date range (optional)
4. Tap record to see details

---

## 🔒 Security Features

### Password Security
- **Hashing Algorithm**: SHA-256 with random salt
- **Salt Generation**: 16-byte cryptographically secure random
- **Storage**: Hashed password + salt stored in Room database
- **Validation**: Constant-time comparison to prevent timing attacks

### Biometric Security
- **Android Keystore**: Biometric authentication tied to secure hardware
- **No Storage**: No biometric data stored in app (handled by OS)
- **Fallback**: Device PIN/Pattern if biometric fails
- **Lockout**: 5 failed attempts trigger 30-second lockout

### Database Security
- **SQLite Encryption**: Using SQLCipher (optional, can be enabled)
- **File Protection**: Database stored in app-private directory
- **No Cloud Sync**: All data stored locally only

### Location Privacy
- **No Tracking**: Location fetched only during check-in/check-out
- **No Storage**: GPS coordinates not stored permanently
- **Local Validation**: Distance calculation done on-device

### Session Management
- **Auto-Logout**: 30 minutes of inactivity
- **Secure Token**: Session token stored in EncryptedSharedPreferences
- **Manual Logout**: Clears all session data

---

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

**Coverage Areas**:
- ViewModel logic (AuthViewModel, AttendanceViewModel)
- Repository operations
- Validation utilities (email, password)
- Date/time calculations

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

**Coverage Areas**:
- Database operations (Room DAO)
- UI interactions (Compose tests)
- Navigation flows
- Biometric prompt integration

### Manual Testing Checklist
- [ ] Sign-up with valid/invalid data
- [ ] Sign-in with correct/incorrect credentials
- [ ] Biometric enrollment on first use
- [ ] Check-in inside office location
- [ ] Check-in outside office location (should fail)
- [ ] Duplicate check-in prevention
- [ ] Check-out after check-in
- [ ] View attendance history
- [ ] Filter attendance by date
- [ ] Export attendance data
- [ ] Handle GPS disabled scenario
- [ ] Handle biometric not enrolled scenario
- [ ] Test session timeout
- [ ] Test app on different Android versions

---

## ⚠️ Known Limitations

### Technical Limitations
1. **Local Storage Only**: No cloud sync or backup
2. **Single Office**: Supports one office location (configurable)
3. **GPS Dependency**: Requires outdoor GPS signal (indoor accuracy varies)
4. **No Offline Editing**: Cannot modify past attendance
5. **Device-Specific**: Biometric tied to one device only

### Usage Constraints
- **One Check-In Per Day**: Cannot check in multiple times
- **Sequential Flow**: Must check-in before check-out
- **Location Accuracy**: 200m radius may allow nearby check-ins
- **Battery Impact**: GPS usage drains battery moderately
- **Android 8.0+**: Not compatible with older Android versions

### Security Considerations
- **Physical Access**: Anyone with device access after unlock can use app
- **Spoofing Risk**: GPS can be spoofed with root access
- **No MFA**: Relies solely on biometric + password

---

## 🚀 Future Enhancements

### High Priority
- [ ] **Admin Web Dashboard**: HR portal to view all employee attendance
- [ ] **Cloud Sync**: Firebase/AWS backend for data backup
- [ ] **Multi-Office Support**: Select office from dropdown
- [ ] **Offline Mode**: Queue attendance when no internet, sync later
- [ ] **Photo Capture**: Optional selfie during check-in for verification

### Medium Priority
- [ ] **Leave Management**: Request and track leaves within app
- [ ] **Shift Scheduling**: Support for different work shifts
- [ ] **Overtime Tracking**: Calculate and display overtime hours
- [ ] **Reports & Analytics**: Monthly/yearly attendance reports
- [ ] **QR Code Check-In**: Alternative to GPS for indoor locations

### Low Priority
- [ ] **Dark Mode**: Complete dark theme support
- [ ] **Multi-Language**: Localization for different languages
- [ ] **Wear OS Support**: Check-in from smartwatch
- [ ] **NFC Check-In**: Tap phone at office entrance
- [ ] **Gamification**: Rewards for consistent attendance

---

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

### How to Contribute
1. **Fork** the repository
2. Create a **feature branch** (`git checkout -b feature/AmazingFeature`)
3. **Commit** changes (`git commit -m 'Add AmazingFeature'`)
4. **Push** to branch (`git push origin feature/AmazingFeature`)
5. Open a **Pull Request**

### Code Standards
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable/function names
- Add comments for complex logic
- Write unit tests for new features
- Update README for major changes

### Pull Request Checklist
- [ ] Code builds without errors
- [ ] All tests pass
- [ ] New tests added for new features
- [ ] README updated (if applicable)
- [ ] No merge conflicts

---

## 📄 License

This project is licensed under the **MIT License**.

```
MIT License

Copyright (c) 2024 [Your Name/Organization]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 📞 Contact

### Project Maintainer
- **Name**: Timothee RINGUYENEZA
- **Email**: thimethane@gmail.com
- **GitHub**: [@Thimethane](https://github.com/Thimethane)
- **LinkedIn**:[LinkedIn](https://www.linkedin.com/in/timotheeringuyeneza/)

### Report Issues
Found a bug or have a feature request?
- Open an [Issue](https://github.com/Thimethane/BiometricAttendanceApp/issues)
- Use appropriate labels (bug/enhancement/question)
- Provide detailed description with steps to reproduce

### Community
- **Discussions**: [GitHub Discussions](https://github.com/Thimethane/BiometricAttendanceApp/discussions)
- **Wiki**: [Project Wiki](https://github.com/Thimethane/BiometricAttendanceApp/wiki)

---

## 🙏 Acknowledgments

- **Android Team** for BiometricPrompt API
- **Google** for Jetpack Compose and Room
- **Kotlin Community** for excellent documentation
- **Contributors** who improve this project

---

## 📊 Project Status

![Development Status](https://img.shields.io/badge/status-in%20development-yellow)
![GitHub Issues](https://img.shields.io/github/issues/Thimethane/BiometricAttendanceApp)
![GitHub Pull Requests](https://img.shields.io/github/issues-pr/Thimethane/BiometricAttendanceApp)
![Last Commit](https://img.shields.io/github/last-commit/Thimethane/BiometricAttendanceApp)

**Current Version**: 1.0.0-alpha  
**Last Updated**: December 2024  
**Roadmap**: See [Projects Tab](https://github.com/Thimethane/BiometricAttendanceApp/projects)

---

<div align="center">

**⭐ If you find this project useful, please star it on GitHub! ⭐**

Made with ❤️ by Timothee RINGUENEZA

</div>