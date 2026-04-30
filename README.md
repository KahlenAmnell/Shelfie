# Shelfie – Twoja Osobista Biblioteka

Shelfie to aplikacja na system Android, która pozwala zarządzać domową biblioteką. Dzięki niej możesz dodawać książki ręcznie lub skanując kody ISBN, śledzić status czytania oraz synchronizować dane w chmurze.

## Funkcje
- 📚 **Zarządzanie książkami**: Dodawanie, edytowanie i usuwanie pozycji z biblioteki.
- 🔍 **Skaner ISBN**: Szybkie dodawanie książek przy użyciu kamery i Google Books API.
- ☁️ **Synchronizacja Firebase**: Twoje dane są bezpieczne i dostępne na wielu urządzeniach.
- 🌙 **Statusy czytania**: Oznaczaj książki jako "Chcę przeczytać", "W trakcie" lub "Przeczytane".

---

## Instrukcja uruchomienia

### Wymagania wstępne
- **Android Studio** (wersja Ladybug lub nowsza).
- **JDK 17**.
- Urządzenie z systemem Android (fizyczne lub emulator) z dostępem do usług Google Play.

### Kroki instalacji
1. Sklonuj repozytorium:
   ```bash
   git clone https://github.com/KahlenAmnell/Shelfie.git
   ```
2. Otwórz projekt w Android Studio.
3. Skonfiguruj klucze API (zobacz sekcję **Konfiguracja Google Books API**).
4. Zsynchronizuj projekt z plikami Gradle (**Sync Project with Gradle Files**).
5. Uruchom aplikację na urządzeniu.

---

## Konfiguracja Firebase

Aplikacja korzysta z Firebase Authentication oraz Realtime Database. Aby użyć własnej instancji:

1. Przejdź do [Firebase Console](https://console.firebase.google.com/).
2. Utwórz nowy projekt i dodaj aplikację Android (pakiet: `com.bernat.shelfie`).
3. Pobierz plik `google-services.json` i umieść go w katalogu `app/`.
4. **Firebase Auth**:
   - Włącz metodę logowania "Email/Password".
5. **Realtime Database**:
   - Utwórz bazę danych (zalecany region: `europe-west1`).
   - Ustaw reguły bezpieczeństwa (Rules), aby umożliwić dostęp zalogowanym użytkownikom:
     ```json
     {
       "rules": {
         ".read": "auth != null",
         ".write": "auth != null"
       }
     }
     ```
   - Jeśli Twoja baza ma inny adres niż domyślny, zaktualizuj `databaseUrl` w klasie `FirebaseBooksRepository.kt`.

---

## Konfiguracja Google Books API

Aplikacja wymaga klucza API do pobierania informacji o książkach po kodzie ISBN.

1. Uzyskaj klucz API w [Google Cloud Console](https://console.cloud.google.com/).
2. Dodaj klucz do pliku `local.properties` w głównym folderze projektu:
   ```properties
   GOOGLE_BOOKS_API_KEY=TWÓJ_KLUCZ_API
   ```

---

## Plik APK

Gotowy plik instalacyjny APK można wygenerować samodzielnie w Android Studio:
1. Przejdź do menu `Build` > `Build Bundle(s) / APK(s)` > `Build APK(s)`.
2. Po zakończeniu procesu, plik znajdzie się w lokalizacji:
   `app/build/outputs/apk/debug/app-debug.apk`

---

## Technologie
- **UI**: Jetpack Compose
- **Baza danych**: Firebase Realtime Database & Room (lokalne statystyki)
- **Auth**: Firebase Authentication
- **Networking**: Retrofit & Gson
- **Skanowanie**: ML Kit Barcode Scanning
- **Obrazy**: Coil
