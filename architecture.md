# Project Architecture

This project follows Clean Architecture principles combined with the MVVM (Model-View-ViewModel) pattern to ensure scalability, maintainability, and testability.

## Folder Structure

### 1. Data Layer
Responsible for data persistence and retrieval from various sources (e.g., Room database or remote APIs).
- **`data/local`**: Contains database-related classes such as `BookDatabase` and `BookDao`.
- **`data/repository`**: Holds the concrete implementations of repositories (e.g., `BookRepositoryImpl`).
- **`data/model`**: Defines database entities and data transfer objects (e.g., `BookEntity`).

### 2. Domain Layer
The core of the application, independent of any external frameworks or technologies.
- **`domain/model`**: Contains pure domain data classes (e.g., `Book`).
- **`domain/repository`**: Defines repository interfaces to be implemented by the Data layer (e.g., `BookRepository`).
- **`domain/usecase`**: Contains classes for specific business logic tasks (e.g., `AddBookToLibraryUseCase`).

### 3. UI Layer (Presentation)
Handles everything related to the user interface using Jetpack Compose.
- **`ui/screens`**: High-level components representing entire screens (e.g., `LibraryScreen`, `BookDetailScreen`).
- **`ui/viewmodel`**: Manages the UI state and handles interaction logic (e.g., `LibraryViewModel`).
- **`ui/components`**: Reusable UI widgets and smaller elements (e.g., `BookItemCard`).
