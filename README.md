# StudyHelpers — CP3406 Assignment

StudyHelpers is a productivity & study-management mobile app built in **Kotlin** using **Jetpack Compose**, designed to help students plan tasks, track deadlines, stay organized, and improve study consistency using focus-timers and reminders.

This app was developed as part of the CP3406 Mobile Application Development assignment.

---

## Core Features

### 🏠 Home Dashboard
- Displays list of upcoming and active tasks
- Allows marking tasks as completed
- Tasks sorted and filtered according to completion status
- Motivational quote displayed dynamically using API

### 📝 Task Management
- Add new tasks
- Edit existing tasks
- Delete tasks
- Set:
    - Task **priority** (Major / Normal / Minor)
    - **Due dates**
    - Description
- Persisted via Room Database

### ⏳ Focus Timer
- Pomodoro-style focus session screen
- Start / Reset controls
- Helps increase study productivity

### 👤 User Authentication
- Local user account system
- Register new user
- Login existing user
- User session stored
- Logout option

### 📆 Interactive Calendar *(In Progress)*
- Highlight deadlines by date
- Tap a date to view tasks for that day
- Future goal: Google Calendar Sync

### 🔔 Notifications *(In Progress)*
- Send reminders for upcoming deadlines
- Daily task alert options

---

## 🛠 Tech Stack

| Category | Technologies Used |
|---------|------------------|
UI Toolkit | Jetpack Compose
Architecture | MVVM + Repository Pattern
Dependency Injection | Hilt
Local Storage | Room Database
Navigation | Jetpack Navigation Compose
Async Processing | Kotlin Coroutines + Flow
Platform | Android SDK
Language | Kotlin

---

## 📂 Project Structure
app/
├─ data/
│ ├─ local/
│ │ ├─ dao/
│ │ ├─ entity/
│ │ └─ database/
│ └─ repository/
├─ domain/
│ └─ repository/
├─ ui/
│ ├─ auth/
│ ├─ home/
│ ├─ addtask/
│ ├─ edittask/
│ ├─ focustimer/
│ ├─ calendar/
│ └─ profile/
├─ navigation/
└─ theme/
## 🧪 Testing

Testing availability:

- Unit tests planned
- ViewModel coverage planned
- UI tests planned using `compose-test`
- Manual testing for core features completed

## 📦 Installation

Clone repository:

git clone https://github.com/BuiTranDoan/CP3406_Assignment_BuiTranDoan.git

Open in Android Studio Flamingo+  
Let Gradle sync  
Run on emulator or device

---

## 📸 Promotional Video
A short demo video showcasing core features will be uploaded soon.

---

## 📝 Self-Reflection
A detailed Gibbs Reflective Cycle write-up will be included with submission.

---

## 📌 Roadmap

- Interactive calendar view
- Google Calendar sync
- Notification reminders
- UI polish

---

## 👨‍💻 Developer
**Bui Tran Doan**  
Student ID: JD118209
James Cook University — CP3406 2025

---

## License
For academic use only.  
Not intended for commercial release.
