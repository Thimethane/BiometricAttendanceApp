# 📱 BiometricAttendanceApp

A modern Kotlin-based Android application that enables touch-free employee attendance using smartphone biometric authentication and GPS validation—ideal for post-COVID workplace requirements.

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple.svg)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-7.0%2B-green.svg)](https://developer.android.com)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.0-blue.svg)](https://developer.android.com/jetpack/compose)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24-orange.svg)](https://developer.android.com)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📌 Table of Contents
- [Project Overview](#-project-overview)
- [Assignment Requirements](#-assignment-requirements)
- [Key Features](#-key-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Installation & Setup](#-installation--setup)
- [Configuration](#-configuration)
- [Usage Guide](#-usage-guide)
- [Assignment Checklist](#-assignment-checklist)
- [Testing Scenarios](#-testing-scenarios)
- [Screenshots](#-screenshots)
- [Security Features](#-security-features)
- [Known Limitations](#-known-limitations)
- [Future Enhancements](#-future-enhancements)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)

---

## 📌 Project Overview

### Problem Statement
Organizations returning to physical offices after COVID-19 need safe, contactless attendance systems that eliminate shared touchpoints while maintaining accuracy and accountability.

### Solution
**BiometricAttendanceApp** provides a secure, touch-free attendance solution using employees' personal smartphones with:

- 🔐 **Device biometric authentication** (fingerprint/face unlock via Android BiometricPrompt)
- 📍 **GPS-based office location validation** (geofencing with 200m radius)
- 💾 **Secure local storage** using SQLite via Room Database
- 🚫 **Duplicate entry prevention** (no double check-ins/check-outs per day)
- 🔒 **Password security** using SHA-256 hashing with salt

### Target Users
- Organizations with on-site employees
- Companies implementing post-COVID safety measures
- Businesses requiring accurate attendance tracking
- Teams needing contactless check-in/check-out systems

---

## 📋 Assignment Requirements

This project fulfills the following assignment requirements:

### **Part 1: Core Features**

#### ✅ **User Registration & Authentication**
- [x] Sign-up screen with name and email fields
- [x] Email validation (regex pattern matching)
- [x] Separate password setup screen
- [x] Password validation (min 8 chars, uppercase, number)
- [x] Secure password storage (SHA-256 + salt)
- [x] Sign-in screen with email/password authentication
- [x] Credential verification against database

#### ✅ **Home Screen**
- [x] Three main buttons:
    - Check-In button
    - Check-Out button
    - View Attendance History button
- [x] User session management
- [x] Logout functionality

#### ✅ **Biometric Authentication Flow**

**Case 1: Biometric Not Registered**
- [x] Alert prompt to register biometric
- [x] Biometric registration process
- [x] Success message on registration
- [x] Save registration status in database

**Case 2: Biometric Already Registered**
- [x] Request biometric authentication
- [x] Match biometric with device enrollment
- [x] GPS location validation on success
- [x] Check-in/out only if within office premises
- [x] Save date and time for successful attendance
- [x] Show error if location doesn't match

**Case 3: Duplicate Prevention**
- [x] Prevent multiple check-ins per day
- [x] Prevent multiple check-outs per day
- [x] Display appropriate error messages

#### ✅ **Attendance History**
- [x] View past attendance records
- [x] Display date and time of check-ins/check-outs
- [x] Show attendance status (Present/Partial)
- [x] Calculate work duration

#### ✅ **Testing & Deployment**
- [x] APK generation for testing
- [x] Device/emulator testing
- [x] Screenshots of all screens
- [x] Screenshots of all functionalities
- [x] Screenshots of all test cases

---

## 🚀 Key Features

### 👤 **User Management**
- **Sign-Up Flow**:
    - Full name validation (min 2 characters)
    - Email validation (RFC 5322 compliant regex)
    - Password requirements enforced:
        - Minimum 8 characters
        - At least 1 uppercase letter
        - At least 1 lowercase letter
        - At least 1 number
    - Secure password hashing using SHA-256 with random salt
    - Automatic employee ID generation

- **Sign-In Flow**:
    - Email + password authentication
    - Session management with SharedPreferences
    - Persistent login state
    - Secure logout with session clearing

### 🏠 **Home Dashboard**
- Real-time attendance status indicator
- Quick action buttons with Material Design 3
- Today's work duration calculator
- Last check-in/check-out timestamp display
- User profile information
- Logout option

### 🔐 **Biometric Authentication**
- **Android BiometricPrompt API** integration (supports API 24+)
- **Two-Phase Registration Flow**:
    1. **First-time users**: Prompt to register → save flag in database
    2. **Returning users**: Authenticate with device biometric
- **Fallback Options**:
    - Device PIN/Pattern if biometric fails
    - Maximum retry limit (3 attempts)
    - Clear error messages for each failure type
- **Supported Biometrics**:
    - Fingerprint scanner
    - Face Recognition (device-dependent)
    - Iris Scan (device-dependent)
- **Security Features**:
    - No biometric data stored in app
    - Hardware-backed authentication
    - Uses Android Keystore

### 📍 **GPS Location Validation**
- **FusedLocationProviderClient** for accurate location
- **Geofencing Parameters**:
    - Office coordinates: Configurable (Kigali, Rwanda by default)
    - Allowed radius: 200 meters (configurable)
    - Accuracy threshold: < 50 meters
- **Validation Rules**:
    - Blocks attendance outside office boundaries
    - Handles GPS accuracy issues gracefully
    - Indoor location fallback using network provider
    - Distance calculation using Haversine formula
- **Battery Optimization**:
    - Location fetched only during check-in/check-out
    - No background tracking
    - Automatic location service shutdown after use

### 📅 **Attendance History**
- **Display Features**:
    - RecyclerView/LazyColumn with efficient rendering
    - Date and time formatted: "DD MMM YYYY, HH:MM AM/PM"
    - Check-in and check-out timestamps
    - Automatic work duration calculation
    - Status indicators:
        - ✅ Present (both check-in and check-out)
        - ⏰ Partial (only check-in)
        - ❌ Absent (no records)
- **Filtering Options**:
    - Date range picker
    - Monthly view
    - Weekly view
    - Search by date
- **Duplicate Prevention Logic**:
    - One check-in per day per user
    - One check-out per day per user
    - Smart detection of incomplete entries
    - Database-level constraints

### 📊 **Additional Features**
- **Statistics Dashboard**:
    - Total working days this month
    - Average daily work hours
    - On-time arrival percentage
    - Monthly attendance rate
- **Notifications** (Future):
    - Check-in reminder at work start time
    - Check-out reminder at work end time
- **Export Functionality** (Future):
    - Export attendance as CSV
    - Share via email/messaging apps
    - PDF report generation

---

## 🧱 Tech Stack

| Category | Technology | Version | Purpose |
|---------|------------|---------|---------|
| **Language** | Kotlin | 1.9.0 | Primary development language |
| **UI Framework** | Jetpack Compose | 1.5.0 | Modern declarative UI |
| **Architecture** | MVVM | - | Separation of concerns |
| **Database** | Room (SQLite) | 2.6.0 | Local data persistence |
| **Biometric** | BiometricPrompt API | androidx.biometric:1.2.0 | Device authentication |
| **Location** | Fused Location Provider | play-services-location:21.0.1 | GPS services |
| **Async** | Kotlin Coroutines | 1.7.3 | Asynchronous operations |
| **State Management** | StateFlow/ViewModel | - | Reactive UI updates |
| **Navigation** | Navigation Compose | 2.7.0 | Screen navigation |
| **Security** | SHA-256 + Android Keystore | - | Password hashing & secure storage |
| **KSP** | Kotlin Symbol Processing | 1.9.0-1.0.13 | Annotation processing for Room |

### **Why These Technologies?**

**Jetpack Compose**: Modern, declarative UI framework that reduces boilerplate code and improves developer productivity.

**Room Database**: Type-safe database access layer with compile-time SQL verification and automatic SQLite management.

**BiometricPrompt**: Standardized API for biometric authentication across different devices and Android versions.

**Fused Location Provider**: Combines multiple location sources (GPS, WiFi, Cell) for best accuracy with minimal battery drain.

**Kotlin Coroutines**: Simplifies asynchronous programming with readable, maintainable code.

**MVVM Architecture**: Separates business logic from UI, making code testable and maintainable.

---

## 🏗️ Architecture

### **MVVM Pattern (Model-View-ViewModel)**

```
┌─────────────────────────────────────────────────────────┐
│                      VIEW LAYER                          │
│            (Composable Screens & UI)                     │
│  • SignUpScreen.kt                                       │
│  • SignInScreen.kt                                       │
│  • HomeScreen.kt                                         │
│  • AttendanceHistoryScreen.kt                           │
└───────────────────┬─────────────────────────────────────┘
                    │ User Actions (Events)
                    │ UI State Updates
                    ▼
┌─────────────────────────────────────────────────────────┐
│                   VIEWMODEL LAYER                        │
│         (Business Logic & State Management)              │
│  • AuthViewModel.kt                                      │
│  • HomeViewModel.kt                                      │
│  • AttendanceViewModel.kt                               │
│  • BiometricViewModel.kt                                │
│                                                          │
│  Responsibilities:                                       │
│  - Handle user input validation                          │
│  - Process business logic                                │
│  - Manage UI state with StateFlow                        │
│  - Call repository methods                               │
└───────────────────┬─────────────────────────────────────┘
                    │ Data Operations
                    │ Database Queries
                    ▼
┌─────────────────────────────────────────────────────────┐
│                  REPOSITORY LAYER                        │
│            (Data Source Abstraction)                     │
│  • UserRepository.kt                                     │
│  • AttendanceRepository.kt                              │
│                                                          │
│  Responsibilities:                                       │
│  - Abstract data source details                          │
│  - Provide clean API to ViewModels                       │
│  - Handle data caching                                   │
└───────────────────┬─────────────────────────────────────┘
                    │
        ┌───────────┴────────────┐
        ▼                        ▼
┌──────────────────┐    ┌──────────────────┐
│  ROOM DATABASE   │    │  DEVICE APIs     │
│  (Local Storage) │    │  - BiometricAPI  │
│  • User Table    │    │  - LocationAPI   │
│  • Attendance    │    │  - SessionMgr    │
└──────────────────┘    └──────────────────┘
```

### **Data Flow**

1. **User Interaction** → UI emits events to ViewModel
2. **ViewModel Processing** → Validates input, processes business logic
3. **Repository Call** → ViewModel requests data from Repository
4. **Data Source** → Repository fetches/stores data from Database or Device APIs
5. **State Update** → StateFlow emits new state to UI
6. **UI Recomposition** → Compose UI automatically updates based on new state

### **Key Architectural Benefits**

✅ **Separation of Concerns**: Each layer has a single responsibility
✅ **Testability**: ViewModels can be unit tested without UI
✅ **Maintainability**: Changes in one layer don't affect others
✅ **Scalability**: Easy to add new features without breaking existing code
✅ **Reactive**: UI automatically updates when data changes

---

## 📂 Project Structure

```
BiometricAttendanceApp/
│
├── app/
│   ├── build.gradle.kts                    # Module-level dependencies
│   │
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml         # App permissions & config
│       │   │
│       │   ├── java/com/timothee/biometricattendance/
│       │   │   │
│       │   │   ├── MainActivity.kt         # App entry point
│       │   │   │
│       │   │   ├── data/
│       │   │   │   ├── local/
│       │   │   │   │   ├── dao/
│       │   │   │   │   │   ├── UserDao.kt              # User database operations
│       │   │   │   │   │   └── AttendanceDao.kt        # Attendance DB operations
│       │   │   │   │   │
│       │   │   │   │   ├── entities/
│       │   │   │   │   │   ├── User.kt                 # User table schema
│       │   │   │   │   │   └── Attendance.kt           # Attendance table schema
│       │   │   │   │   │
│       │   │   │   │   └── database/
│       │   │   │   │       └── AppDatabase.kt          # Room database setup
│       │   │   │   │
│       │   │   │   └── repository/
│       │   │   │       ├── UserRepository.kt           # User data access layer
│       │   │   │       └── AttendanceRepository.kt     # Attendance data access
│       │   │   │
│       │   │   ├── ui/
│       │   │   │   ├── theme/
│       │   │   │   │   ├── Theme.kt                    # App theme & colors
│       │   │   │   │   └── Type.kt                     # Typography definitions
│       │   │   │   │
│       │   │   │   ├── auth/
│       │   │   │   │   ├── SignUpScreen.kt             # Registration screen
│       │   │   │   │   ├── SetPasswordScreen.kt        # Password setup
│       │   │   │   │   ├── SignInScreen.kt             # Login screen
│       │   │   │   │   └── AuthViewModel.kt            # Auth business logic
│       │   │   │   │
│       │   │   │   ├── home/
│       │   │   │   │   ├── HomeScreen.kt               # Dashboard screen
│       │   │   │   │   └── HomeViewModel.kt            # Home business logic
│       │   │   │   │
│       │   │   │   ├── attendance/
│       │   │   │   │   ├── AttendanceHistoryScreen.kt  # History display
│       │   │   │   │   └── AttendanceViewModel.kt      # Attendance logic
│       │   │   │   │
│       │   │   │   └── components/
│       │   │   │       ├── CustomButton.kt             # Reusable buttons
│       │   │   │       ├── CustomTextField.kt          # Input fields
│       │   │   │       └── LoadingDialog.kt            # Loading indicator
│       │   │   │
│       │   │   ├── navigation/
│       │   │   │   ├── Screen.kt                       # Route definitions
│       │   │   │   └── NavGraph.kt                     # Navigation graph
│       │   │   │
│       │   │   └── utils/
│       │   │       ├── Constants.kt                    # App constants
│       │   │       ├── ValidationUtils.kt              # Input validation
│       │   │       ├── SecurityUtils.kt                # Password hashing
│       │   │       ├── DateTimeUtils.kt                # Date/time formatting
│       │   │       ├── LocationUtils.kt                # GPS calculations
│       │   │       ├── SessionManager.kt               # User session
│       │   │       ├── BiometricUtils.kt               # Biometric checks
│       │   │       └── PermissionUtils.kt              # Runtime permissions
│       │   │
│       │   └── res/
│       │       ├── drawable/                           # App icons & images
│       │       ├── values/
│       │       │   ├── colors.xml                      # Color definitions
│       │       │   ├── strings.xml                     # String resources
│       │       │   └── themes.xml                      # App themes
│       │       └── xml/
│       │           ├── data_extraction_rules.xml
│       │           └── backup_rules.xml
│       │
│       └── test/
│           └── java/com/timothee/biometricattendance/
│               ├── viewmodel/
│               │   ├── AuthViewModelTest.kt            # Auth VM tests
│               │   └── HomeViewModelTest.kt            # Home VM tests
│               ├── repository/
│               │   └── AttendanceRepositoryTest.kt     # Repo tests
│               └── utils/
│                   ├── ValidationUtilsTest.kt          # Validation tests
│                   └── SecurityUtilsTest.kt            # Security tests
│
├── build.gradle.kts                        # Project-level config
├── settings.gradle.kts                     # Project settings
├── gradle.properties                       # Gradle properties
├── .gitignore                             # Git ignore rules
├── README.md                              # This file
└── LICENSE                                # MIT License

```

### **File Count Summary**
- **Configuration**: 2 files (build.gradle.kts, AndroidManifest.xml)
- **Data Layer**: 6 files (Entities, DAOs, Database, Repositories)
- **UI Layer**: 10+ files (Screens, ViewModels, Components, Theme)
- **Utils**: 8 files (Validation, Security, Location, Session, etc.)
- **Navigation**: 2 files (Screen routes, NavGraph)
- **Total**: 26+ core files

---

## 🔧 Installation & Setup

### **Prerequisites**
- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: 17 or higher
- **Minimum Android SDK**: 24 (Android 7.0 Nougat)
- **Target Android SDK**: 34 (Android 14)
- **Device Requirements**:
    - Biometric hardware (fingerprint sensor or face unlock)
    - GPS/Location services enabled
    - Minimum 50MB storage space
    - Internet connection (for initial setup)

### **Step 1: Clone or Download Repository**

**Option A: Clone with Git**
```bash
git clone https://github.com/Thimethane/BiometricAttendanceApp.git
cd BiometricAttendanceApp
```

**Option B: Download ZIP**
1. Download project ZIP file
2. Extract to desired location
3. Open folder in terminal/command prompt

### **Step 2: Open in Android Studio**
1. Launch **Android Studio**
2. Select **File → Open**
3. Navigate to `BiometricAttendanceApp` directory
4. Click **OK**
5. Wait for Gradle sync to complete (may take 2-5 minutes)

### **Step 3: Configure Gradle Files**

Ensure you have **both** Gradle files configured:

**Project-level** `build.gradle.kts`:
```kotlin
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}
```

**Module-level** `app/build.gradle.kts`:
- Already provided in project files
- Contains all dependencies

### **Step 4: Sync Project**
1. Click **"Sync Now"** in the banner at top
2. Or: **File → Sync Project with Gradle Files**
3. Wait for sync to complete
4. Resolve any dependency conflicts if prompted

### **Step 5: Configure Office Location**

Edit `utils/Constants.kt` with your office GPS coordinates:

```kotlin
object Constants {
    // Update these with your actual office location
    const val OFFICE_LATITUDE = -1.9441      // Your office latitude
    const val OFFICE_LONGITUDE = 30.0619     // Your office longitude
    const val OFFICE_RADIUS_METERS = 200.0   // Allowed check-in radius
}
```

**How to find your coordinates:**
1. Open Google Maps
2. Right-click your office location
3. Select first option (coordinates)
4. Copy latitude and longitude
5. Paste into Constants.kt

### **Step 6: Build Project**

**Via Android Studio:**
- **Build → Make Project** (Ctrl+F9 / Cmd+F9)
- Wait for build to complete
- Check "Build" tab for any errors

**Via Command Line:**
```bash
./gradlew clean build
```

### **Step 7: Run on Device/Emulator**

**Physical Device (Recommended):**
1. Enable **Developer Options** on your phone
2. Enable **USB Debugging**
3. Connect phone via USB
4. Click **Run** ▶️ in Android Studio
5. Select your device
6. Wait for app to install

**Emulator:**
1. **Tools → Device Manager**
2. Create new device or use existing
3. **Important**: Enroll biometric in emulator:
    - **Settings → Security → Fingerprint**
    - Follow enrollment process
4. Click **Run** ▶️
5. Select emulator
6. Wait for app to launch

### **Step 8: Grant Permissions**

On first launch, the app will request:
1. **Location Permission** → Select "While using the app"
2. **Biometric Permission** → Automatic if biometric enrolled

---

## ⚙️ Configuration

### **Office Location Setup**

The app needs your office GPS coordinates to validate check-ins. Update `utils/Constants.kt`:

```kotlin
object Constants {
    // Office GPS Coordinates
    const val OFFICE_LATITUDE = -1.9441       // Default: Kigali, Rwanda
    const val OFFICE_LONGITUDE = 30.0619
    const val OFFICE_RADIUS_METERS = 200.0    // 200 meters = ~650 feet
    
    // GPS Settings
    const val GPS_ACCURACY_THRESHOLD = 50.0   // Minimum accuracy in meters
    
    // Work Hours (24-hour format)
    const val WORK_START_HOUR = 9             // 9:00 AM
    const val WORK_END_HOUR = 17              // 5:00 PM
    
    // Security Settings
    const val MIN_PASSWORD_LENGTH = 8         // Minimum password length
    const val MAX_BIOMETRIC_RETRIES = 3       // Max failed attempts
    const val SESSION_TIMEOUT_MINUTES = 30    // Auto-logout time
}
```

### **Permissions (AndroidManifest.xml)**

Already configured in the provided manifest:

```xml
<!-- Biometric Permission -->
<uses-permission android:name="android.permission.USE_BIOMETRIC" />

<!-- Location Permissions -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- Optional -->
<uses-permission android:name="android.permission.INTERNET" />
```

### **Database Configuration**

Room database is automatically configured. To view/modify:

**Location**: `data/local/database/AppDatabase.kt`

```kotlin
@Database(
    entities = [User::class, Attendance::class],
    version = 1,  // Increment for schema changes
    exportSchema = false
)
```

**To clear database during development:**
```kotlin
Room.databaseBuilder(context, AppDatabase::class.java, "biometric_attendance_database")
    .fallbackToDestructiveMigration()  // Deletes & recreates on version change
    .build()
```

### **Theme Customization**

Edit `ui/theme/Theme.kt` to change app colors:

```kotlin
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),      // Change primary color
    secondary = Color(0xFF03DAC5),    // Change secondary color
    background = Color(0xFFFFFBFE),   // Change background
    // ... more colors
)
```

---

## 📖 Usage Guide

### **First-Time Setup**

#### **1. Sign Up**
1. Open app → Tap **"Sign Up"** button
2. Enter:
    - **Full Name**: Minimum 2 characters
    - **Email Address**: Must be valid format
3. Tap **"Continue"**
4. Enter **Password**:
    - At least 8 characters
    - At least 1 uppercase letter (A-Z)
    - At least 1 lowercase letter (a-z)
    - At least 1 number (0-9)
5. Tap **"Create Account"**
6. ✅ Success! Redirected to Sign In

#### **2. Sign In**
1. Enter **Email** used during sign-up
2. Enter **Password**
3. Tap **"Sign In"**
4. ✅ If credentials match, redirected to Home

#### **3. Enable Biometric (First Time)**
1. On Home screen, tap **"Check In"**
2. Alert appears: **"Register Biometric?"**
3. Tap **"Yes"**
4. Follow device biometric prompt:
    - Place finger on sensor, or
    - Look at camera for face unlock
5. ✅ Success message: **"Biometric Registered!"**
6. Status saved in database

#### **4. Grant Location Permission (First Time)**
1. App requests: **"Allow location access?"**
2. Tap **"While using the app"**
3. ✅ Permission granted

### **Daily Usage**

#### **Check-In Process**
1. Arrive at office (within 200m of configured location)
2. Open app (auto-navigates to Home if logged in)
3. Tap **"Check In"** button
4. **Biometric prompt** appears:
    - Place finger on sensor, or
    - Look at camera for face unlock
5. App validates:
    - ✅ Biometric matches device enrollment
    - ✅ GPS location within office radius
6. Success screen:
    - **"Checked in at 9:05 AM"**
    - Current location displayed
    - Distance from office shown

**Possible Errors:**
- ❌ **"Biometric not recognized"** → Retry or use device PIN
- ❌ **"You are not at office premises"** → Move closer (shows distance)
- ❌ **"You have already checked in today"** → Cannot check in twice

#### **Check-Out Process**
1. End of workday, tap **"Check Out"** button
2. **Biometric prompt** appears
3. Authenticate with fingerprint/face
4. App validates location
5. Success screen:
    - **"Checked out at 5:30 PM"**
    - **"Duration: 8h 25m"**
    - Total hours worked today

**Requirements:**
- ✅ Must have checked in today
- ✅ Must be within office radius
- ✅ Can only check out once per day

#### **View Attendance History**
1. On Home screen, tap **"View Attendance"**
2. See list of all records:
    - **Date**: DD MMM YYYY
    - **Check-In**: HH:MM AM/PM
    - **Check-Out**: HH:MM AM/PM
    - **Duration**: Xh Ym
    - **Status**: Present ✅ / Partial ⏰
3. Scroll to see past records
4. Tap any record for details
5. Tap **Back arrow** to return to Home

#### **Logout**
1. On Home screen, tap **"Logout"**
2. Confirmation dialog appears
3. Tap **"Yes"**
4. Redirected to Sign In screen
5. Session cleared from device

---

## ✅ Assignment Checklist

### **Core Functionality**
- [x] Sign-up screen with name & email fields
- [x] Email validation with proper error messages
- [x] Separate password setup screen
- [x] Password validation (8+ chars, uppercase, number)
- [x] Data stored in Room database after sign-up
- [x] Sign-in screen with email/password fields
- [x] Credential verification against database
- [x] Error message for incorrect credentials
- [x] Home screen with 3 buttons (Check-In, Check-Out, View Attendance)

### **Biometric Authentication**
- [x] **Case 1**: Alert to register biometric if not enrolled
- [x] **Case 1**: Biometric registration process
- [x] **Case 1**: Success message on registration
- [x] **Case 1**: Save registration status in database
- [x] **Case 2**: Request biometric for enrolled users
- [x] **Case 2**: Match biometric with device
- [x] **Case 2**: GPS location validation after biometric success
- [x] **Case 2**: Check-in/out only if within office premises
- [x] **Case 2**: Save date & time for successful attendance
- [x] **Case 2**: Error message if location doesn't match
- [x] **Case 2**: Error message if biometric doesn't match
- [x] **Case 3**: Prevent double check-in on same day
- [x] **Case 3**: Prevent double check-out on same day
- [x] **Case 3**: Proper error messages for duplicates

### **Attendance History**
- [x] View button displays attendance list
- [x] Shows all past attendance records
- [x] Displays date and time for each record
- [x] Shows check-in time
- [x] Shows check-out time
- [x] Calculates and displays duration

### **Technical Requirements**
- [x] Built with Kotlin
- [x] Uses MVVM architecture
- [x] Room database for local storage
- [x] BiometricPrompt API integration
- [x] GPS/Location services integration
- [x] Proper error handling
- [x] Clean code with comments
- [x] Follows Android best practices

### **Testing & Deployment**
- [x] APK generated successfully
- [x] Tested on physical device/emulator
- [x] All screens function correctly
- [x] All edge cases handled
- [x] Screenshots captured for all screens
- [x] Screenshots for all functionalities
- [x] Screenshots for all test cases

---

## 🧪 Testing Scenarios

### **Test Case 1: Sign Up Flow**
**Steps:**
1. Launch app
2. Tap "Sign Up"
3. Enter invalid email (e.g., "test@")
4. Tap Continue

**Expected:** Error message "Please enter a valid email address"

**Actual:** ✅ Pass

---

**Steps:**
1. Enter valid email (e.g., "timothee@example.com")
2. Enter name "Tim"
3. Tap Continue
4. Enter weak password "abc123"
5. Tap Create Account

**Expected:** Error message "Password must contain at least one uppercase letter"

**Actual:** ✅ Pass

---

**Steps:**
1. Enter strong password "Test@123"
2. Tap Create Account

**Expected:** Account created, redirected to Sign In

**Actual:** ✅ Pass

---

### **Test Case 2: Sign In Flow**
**Steps:**
1. Enter wrong email
2. Tap Sign In

**Expected:** Error "Invalid email or password"

**Actual:** ✅ Pass

---

**Steps:**
1. Enter correct email but wrong password
2. Tap Sign In

**Expected:** Error "Invalid email or password"

**Actual:** ✅ Pass

---

**Steps:**
1. Enter correct email and password
2. Tap Sign In

**Expected:** Redirected to Home screen

**Actual:** ✅ Pass

---

### **Test Case 3: Biometric Registration**
**Steps:**
1. On Home, tap "Check In"
2. No biometric registered yet

**Expected:** Alert "Register Biometric?"

**Actual:** ✅ Pass

---

**Steps:**
1. Tap "Yes" on alert
2. Follow device biometric prompt
3. Scan fingerprint/face

**Expected:** Success message "Biometric Registered!"

**Actual:** ✅ Pass

---

### **Test Case 4: Check-In - Success**
**Preconditions:** User at office location (within 200m)

**Steps:**
1. Tap "Check In"
2. Authenticate with biometric

**Expected:**
- Success message "Checked in at [time]"
- Location validated
- Record saved in database

**Actual:** ✅ Pass

---

### **Test Case 5: Check-In - Outside Office**
**Preconditions:** User NOT at office (>200m away)

**Steps:**
1. Tap "Check In"
2. Authenticate with biometric

**Expected:** Error "You are not at office premises. Distance: [X]m from office"

**Actual:** ✅ Pass

---

### **Test Case 6: Check-In - Duplicate Prevention**
**Preconditions:** Already checked in today

**Steps:**
1. Tap "Check In" again
2. Authenticate with biometric

**Expected:** Error "You have already checked in today"

**Actual:** ✅ Pass

---

### **Test Case 7: Check-Out - Success**
**Preconditions:** Checked in earlier today, at office

**Steps:**
1. Tap "Check Out"
2. Authenticate with biometric

**Expected:**
- Success message "Checked out at [time]"
- Duration calculated: "Duration: Xh Ym"
- Database updated

**Actual:** ✅ Pass

---

### **Test Case 8: Check-Out - Not Checked In**
**Preconditions:** No check-in today

**Steps:**
1. Tap "Check Out"

**Expected:** Error "You must check in before checking out"

**Actual:** ✅ Pass

---

### **Test Case 9: View Attendance History**
**Steps:**
1. Tap "View Attendance"
2. Scroll through list

**Expected:**
- Shows all past records
- Displays date, check-in, check-out, duration
- Records ordered by date (newest first)

**Actual:** ✅ Pass

---

### **Test Case 10: Biometric Authentication Failure**
**Steps:**
1. Tap "Check In"
2. Use wrong finger/face
3. Biometric fails

**Expected:** Error from device "Biometric not recognized. Try again"

**Actual:** ✅ Pass

---

### **Test Case 11: GPS Disabled**
**Steps:**
1. Disable GPS in device settings
2. Open app
3. Tap "Check In"

**Expected:** Prompt "Please enable location services"

**Actual:** ✅ Pass

---

### **Test Case 12: Session Persistence**
**Steps:**
1. Sign in successfully
2. Close app completely
3. Reopen app

**Expected:** User still logged in, Home screen shown

**Actual:** ✅ Pass

---

### **Test Case 13: Logout**
**Steps:**
1. On Home, tap "Logout"
2. Confirm logout

**Expected:**
- Redirected to Sign In
- Session cleared
- Must sign in again to access Home

**Actual:** ✅ Pass

---

## 📸 Screenshots

### **Authentication Flow**
- **Sign Up Screen**: Name and email input fields with validation
- **Set Password Screen**: Password requirements and validation
- **Sign In Screen**: Login form with error handling
- **Biometric Registration Alert**: Prompt to register biometric

### **Home Screen**
- **Dashboard**: Check-In, Check-Out, View Attendance buttons
- **User Profile**: Name and email displayed
- **Last Activity**: Most recent check-in/out shown

### **Check-In/Out Flow**
- **Biometric Prompt**: Device fingerprint/face scanner
- **Success Screen**: Confirmation with time and location
- **Error Screen**: Location validation failure
- **Duplicate Error**: Already checked in/out message

### **Attendance History**
- **List View**: All attendance records
- **Detail View**: Individual record with full information
- **Empty State**: No records yet message

### **Error States**
- **Invalid Email**: Validation error message
- **Weak Password**: Password requirements not met
- **Wrong Credentials**: Login failure
- **Outside Office**: GPS location error
- **No GPS Signal**: Location services disabled

---

## 🔒 Security Features

### **Password Security**
- **Hashing Algorithm**: SHA-256 with 256-bit output
- **Salt Generation**: 16-byte cryptographically secure random salt per user
- **Storage**: Only hash + salt stored, never plain text
- **Verification**: Constant-time comparison to prevent timing attacks

**Implementation:**
```kotlin
// Password is never stored in plain text
val salt = SecurityUtils.generateSalt()
val hash = SecurityUtils.hashPassword(password, salt)
// Store: hash + salt in database
```

### **Biometric Security**
- **Hardware-backed**: Uses Android Keystore for key management
- **No Storage**: Biometric templates never stored in app
- **Device-level**: Relies on device's secure biometric hardware
- **Fallback**: Device PIN/Pattern as backup authentication
- **Lockout**: 5 failed attempts → 30-second lockout

### **Database Security**
- **App-private Directory**: Database stored in protected app folder
- **No External Access**: Only app can access database files
- **Optional Encryption**: Can enable SQLCipher for full DB encryption
- **Automatic Backups**: Disabled for sensitive data

### **Location Privacy**
- **No Tracking**: Location only fetched during check-in/out
- **No Storage**: GPS coordinates not permanently stored
- **Local Processing**: Distance calculation done on-device
- **Permission Control**: User controls when location is accessed

### **Session Management**
- **Auto-Logout**: 30 minutes of inactivity
- **Secure Storage**: Session tokens in EncryptedSharedPreferences
- **Manual Logout**: Clears all session data immediately
- **No Cloud Sync**: All data remains on device

---

## ⚠️ Known Limitations

### **Technical Limitations**
1. **Local Storage Only**: No cloud sync or remote backup
2. **Single Office Location**: Supports one office per install (configurable)
3. **GPS Dependency**: Requires outdoor GPS signal (indoor accuracy varies)
4. **No Offline Editing**: Cannot modify past attendance records
5. **Device-Specific**: Biometric tied to one device only
6. **Android Only**: Not available for iOS

### **Usage Constraints**
- **One Check-In Per Day**: Cannot check in multiple times
- **Sequential Flow**: Must check in before checking out
- **Location Radius**: 200m radius may allow nearby check-ins
- **Battery Impact**: GPS usage drains battery moderately
- **Android 7.0+**: Not compatible with older Android versions

### **Security Considerations**
- **Physical Access**: Anyone with device access after unlock can use app
- **GPS Spoofing**: GPS can be spoofed with root access
- **No MFA**: Relies solely on biometric + password
- **Local Data**: If device is lost, attendance data goes with it

---

## 🚀 Future Enhancements

### **High Priority**
- [ ] **Admin Web Dashboard**: HR portal to view all employee attendance
- [ ] **Cloud Sync**: Firebase/AWS backend for data backup and multi-device
- [ ] **Multi-Office Support**: Select office from dropdown before check-in
- [ ] **Offline Mode**: Queue attendance when no internet, sync later
- [ ] **Photo Capture**: Optional selfie during check-in for extra verification
- [ ] **Push Notifications**: Reminders for check-in/out

### **Medium Priority**
- [ ] **Leave Management**: Request and track leaves within app
- [ ] **Shift Scheduling**: Support for different work shifts
- [ ] **Overtime Tracking**: Calculate and display overtime hours
- [ ] **Reports & Analytics**: Monthly/yearly attendance reports with charts
- [ ] **QR Code Check-In**: Alternative to GPS for indoor locations
- [ ] **Manager Approval**: Workflow for attendance corrections

### **Low Priority**
- [ ] **Dark Mode**: Complete dark theme support (partially implemented)
- [ ] **Multi-Language**: Localization for French, Swahili, etc.
- [ ] **Wear OS Support**: Check-in from smartwatch
- [ ] **NFC Check-In**: Tap phone at office entrance
- [ ] **Gamification**: Rewards for consistent attendance
- [ ] **Face Recognition**: Additional biometric option

---



## 🤝 Contributing

We welcome contributions to improve BiometricAttendanceApp!

### **How to Contribute**

1. **Fork** the repository
2. Create a **feature branch**:
   ```bash
   git checkout -b feature/AmazingFeature
   ```
3. **Commit** your changes:
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```
4. **Push** to your branch:
   ```bash
   git push origin feature/AmazingFeature
   ```
5. Open a **Pull Request**

### **Code Standards**
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add comments for complex logic
- Write unit tests for new features
- Update README for major changes
- Ensure code builds without warnings

### **Pull Request Checklist**
- [ ] Code builds without errors
- [ ] All tests pass
- [ ] New tests added for new features
- [ ] README updated (if applicable)
- [ ] No merge conflicts
- [ ] Follows project code style
- [ ] Screenshots added for UI changes

---

## 📄 License

This project is licensed under the **MIT License**.

```
MIT License

Copyright (c) 2024 Timothee

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

### **Project Maintainer**
- **Name**: Timothee RINGUYENEZA
- **Email**: timotheeringuyeneza@gmail.com
- **GitHub**: [@Thimethane](https://github.com/Thimethane)
- **LinkedIn**: [Timothee's Profile](https://www.linkedin.com/in/timotheeringuyeneza/)

### **Report Issues**
Found a bug or have a feature request?
- Open an [Issue](https://github.com/Thimethane/BiometricAttendanceApp/issues)
- Use appropriate labels (bug/enhancement/question)
- Provide detailed description with steps to reproduce

### **Get Help**
- **Stack Overflow**: Tag with `biometric-attendance` and `android`
- **GitHub Discussions**: [Project Discussions](https://github.com/Thimethane/BiometricAttendanceApp/discussions)
- **Documentation**: Check this README first

---

## 🙏 Acknowledgments

- **Android Team** for BiometricPrompt API and excellent documentation
- **Google** for Jetpack Compose, Room, and comprehensive Android libraries
- **Kotlin Team** for the modern, expressive programming language
- **Material Design** for beautiful UI components and guidelines
- **Stack Overflow Community** for countless solutions and best practices
- **Open Source Contributors** who inspire and educate

---

## 📊 Project Statistics

![Development Status](https://img.shields.io/badge/status-active-success.svg)
![Code Size](https://img.shields.io/github/languages/code-size/Thimethane/BiometricAttendanceApp)
![GitHub Issues](https://img.shields.io/github/issues/Thimethane/BiometricAttendanceApp)
![GitHub Pull Requests](https://img.shields.io/github/issues-pr/Thimethane/BiometricAttendanceApp)
![Last Commit](https://img.shields.io/github/last-commit/Thimethane/BiometricAttendanceApp)

**Current Version**: 1.0.0  
**Last Updated**: December 2024  
**Total Files**: 26+ core files  
**Lines of Code**: ~3,500+

---

**Deliverables**:
- ✅ Complete Android Studio project
- ✅ Source code with proper package structure
- ✅ README.md with comprehensive documentation
- ✅ APK file for testing
- ✅ Screenshots of all screens and test cases
- ✅ Video demonstration (optional)

---

<div align="center">

**⭐ If you find this project useful, please star it on GitHub! ⭐**

**Made with ❤️ by Timothee**

[Report Bug](https://github.com/Thimethane/BiometricAttendanceApp/issues) ·
[Request Feature](https://github.com/Thimethane/BiometricAttendanceApp/issues) ·
[Documentation](https://github.com/Thimethane/BiometricAttendanceApp/wiki)

</div>

---

**End**
