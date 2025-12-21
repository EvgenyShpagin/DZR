# DZR Music App

## 📱 Overview

DZR is a modular Android application built with modern Android development practices. It features a clean architecture separating concerns into Feature, Library, and Core layers, utilizing Jetpack Compose for UI, Koin for Dependency Injection, and Clean Architecture principles.

## 🏗 Architecture

The project follows a strict modular architecture designed for scalability and separation of concerns.

### Module Types

1.  **Root Modules**
    *   `app`: Application entry point. Assembles features, initializes DI graph, and handles root navigation.
    *   `build-logic`: Contains custom Gradle convention plugins for consistent build configuration.

2.  **Core Modules** (`core:*`)
    *   Fundamental components used throughout the app.
    *   Examples: `core:network`, `core:design-system`, `core:auth`, `core:common`.
    *   *Rule:* Can only depend on other `core` modules.

3.  **Library Modules** (`library:*`)
    *   Reusable business entities (e.g., `Track`, `Artist`, `Player`).
    *   Split into `domain` (contracts/models) and `data` (implementation) modules.
    *   *Rule:* Can depend on `core` modules.

4.  **Feature Modules** (`feature:*`)
    *   User-facing screens and functional flows (e.g., Search, Player UI, Auth Screen).
    *   *Rule:* Depend on `core` and `library` modules. **Must not depend on other feature modules.**

For a detailed architecture deep-dive, refer to [Architecture.md](Architecture.md).

## 🛠 Tech Stack

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   **Dependency Injection**: [Koin](https://insert-koin.io/)
*   **Networking**: [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/)
*   **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
*   **Asynchrony**: Coroutines & Flows
*   **Build System**: Gradle (Kotlin DSL, Version Catalogs, Convention Plugins)

## 🚀 Getting Started

### Prerequisites

*   Android Studio Ladybug (or newer)
*   JDK 17+

### Build the Project

Build the debug APK:

```bash
./gradlew assembleDebug
```

## ✅ Testing

The project employs various testing strategies including Unit Tests and Compose Screenshot Tests.

### Unit Tests

Run unit tests across all modules:

```bash
./gradlew test
```

### Screenshot Tests

This project uses the [Compose Preview Screenshot Testing](https://developer.android.com/studio/preview/compose-screenshot-testing) tool to ensure UI consistency.

**Run Validation**
Checks current previews against stored reference images:
```bash
./gradlew validateScreenshotTest
```

**Update Reference Images**
Generates new reference images (baseline) for previews:
```bash
./gradlew updateScreenshotTest
```

## 📂 Module Structure Overview

```text
DZR/
├── app/                 # Main application module
├── build-logic/         # Gradle convention plugins
├── core/
│   ├── common/          # Utils, Coroutines, Result wrappers
│   ├── design-system/   # UI Kit, Theme, Icons
│   ├── network/         # Retrofit & OkHttp setup
│   └── ...
├── feature/
│   ├── auth/            # Authentication screens
│   ├── player/          # Player screens
│   ├── search/          # Search functionality
│   └── ...
├── library/
│   ├── album/           # Album domain & data
│   ├── artist/          # Artist domain & data
│   └── ...
└── gradle/              # libs.versions.toml & wrapper
```
