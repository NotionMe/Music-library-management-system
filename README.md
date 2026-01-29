# Music Library Management System 🎵

[**🔗 Відкрити релізи / Open Releases**](https://github.com/NotionMe/Music-library-management-system/releases)

---

## 🇺🇦 Українська

### ℹ️ Опис
**Music Library Management System** — це консольний додаток для управління вашою музичною колекцією. Програма дозволяє:
*   📂 Створювати та керувати плейлистами (приватні/публічні).
*   🔍 Шукати треки за назвою, виконавцем або жанром.
*   👤 Керувати профілем користувача та завантажувати власні треки.
*   🎧 **Відтворювати музику** за допомогою вбудованого плеєра.
*   📧 Відновлювати пароль через Email.

### ⚠️ Важливо: VLC Media Player
Для коректної роботи аудіоплеєра **ОБОВ'ЯЗКОВО** повинен бути встановлений **VLC Media Player**.
Програма використовує бібліотеку `vlcj`, яка потребує нативних бібліотек VLC.

👉 [Завантажити VLC можна тут](https://www.videolan.org/vlc/)

### 🛠️ Як зібрати проект (Build from Source)
1. Переконайтеся, що у вас встановлено **Java JDK 21+** та **Apache Ant**.
2. Відкрийте термінал у папці проекту.
3. Запустіть команду компіляції:
   ```bash
   ant compile
   ```
4. Для створення JAR файлу:
   ```bash
   ant jar
   ```

### 📦 Залежності
Всі залежності вже знаходяться у папці `lib/`. Основні бібліотеки:
*   `vlcj` (4.12.1) — для відтворення аудіо (потребує VLC).
*   `password4j` (1.8.4) — для хешування паролів.
*   `jakarta.mail` (2.0.2) — для відправки листів.
*   `datafaker` (2.5.3) — для генерації даних.
*   `jline` (3.30.6) — для красивого консольного інтерфейсу (TUI).
*   `gson` (2.13.2) — для роботи з JSON.

### 🚀 Запуск
**Windows:**
Запустіть файл `run.bat` або виконайте:
```cmd
java -cp "build;lib/*" ua.notion.musiclibrary.Main
```

**Linux / macOS:**
Запустіть скрипт:
```bash
./run.sh
```

---

## 🇺🇸 English

### ℹ️ Description
**Music Library Management System** is a console-based application for managing your music library. Features include:
*   📂 Create and manage playlists (private/public).
*   🔍 Search tracks by title, artist, or genre.
*   👤 User profile management and track uploading.
*   🎧 **Music playback** via integrated player.
*   📧 Password recovery via Email.

### ⚠️ Important: VLC Media Player
A working installation of **VLC Media Player** is **MANDATORY** for the audio player to function.
The application uses the `vlcj` library, which relies on VLC native libraries.

👉 [Download VLC here](https://www.videolan.org/vlc/)

### 🛠️ Build from Source
1. Ensure **Java JDK 17+** and **Apache Ant** are installed.
2. Open a terminal in the project root.
3. Run the compile command:
   ```bash
   ant compile
   ```
4. To build the JAR file:
   ```bash
   ant jar
   ```

### 📦 Dependencies
All dependencies are included in the `lib/` directory. Key libraries:
*   `vlcj` (4.12.1) — for audio playback (requires VLC).
*   `password4j` (1.8.4) — for password hashing.
*   `jakarta.mail` (2.0.2) — for email services.
*   `datafaker` (2.5.3) — for data generation.
*   `jline` (3.30.6) — for the Terminal User Interface (TUI).
*   `gson` (2.13.2) — for JSON processing.

### 🚀 How to Run
**Windows:**
Run `run.bat` or execute manually:
```cmd
java -cp "build;lib/*" ua.notion.musiclibrary.Main
```

**Linux / macOS:**
Run the script:
```bash
./run.sh
```
