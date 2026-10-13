# Task Manager App

Aplikasi manajemen task sederhana menggunakan Room Database untuk tugas pertemuan 7.

## Fitur

- CRUD task (tambah, edit, hapus, toggle selesai)
- Kategori dengan foreign key relationship
- Filter task berdasarkan kategori
- Search dengan debounce
- Priority (Normal/High)
- Export task aktif ke file txt
- Database migration dari versi 1 ke 2

## Teknologi

- Kotlin
- Jetpack Compose
- Room Database
- Coroutines & Flow
- ViewModel
- Navigation Compose

## Struktur Database

### Task Table
- id (PK, auto increment)
- title
- description
- is_done
- category_id (FK)
- priority
- created_at

### Category Table
- id (PK, auto increment)
- name
- color

## Setup

1. Clone repository
2. Buka di Android Studio
3. Sync Gradle
4. Run app

Sample data akan otomatis ditambahkan saat pertama kali dibuka.

## Build

```bash
./gradlew build
```

## Database Inspector

Untuk melihat isi database saat development:
- View → Tool Windows → App Inspection → Database Inspector

## License

Tugas kuliah - Mobile Platform Apps Development
