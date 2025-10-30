# Giphy Search Android App

### Clean Architecture Layers

```
presentation/  UI Layer (Compose, ViewModels)
domain/        Business Logic (Models, Use Cases, Repository Interfaces)
data/          Data Layer (API, DTOs, Repository Implementation)
```

### Key Components

- **Presentation Layer**: Jetpack Compose screens and ViewModels
- **Domain Layer**: Business models and use cases
- **Data Layer**: API integration, DTOs, and data mapping
- **DI Layer**: Hilt modules for dependency injection

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose + Material 3
- **Architecture**: Clean Architecture + MVVM
- **Async**: Kotlin Coroutines + Flow
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil (with GIF support)
- **Pagination**: Paging 3
- **Navigation**: Compose Navigation
- **Testing**: JUnit, MockK, Turbine

### Setup Instructions

**Configure API Key**
    - Open `app/build.gradle.kts`
    - Replace `YOUR_API_KEY_HERE` with your actual API key:
   ```kotlin
   buildConfigField("String", "GIPHY_API_KEY", "\"your_actual_api_key\"")
   ```