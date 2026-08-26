<div align="center">

# 🖐️ Fingerprint Attendance System

**A biometric employee attendance & Daily Time Record (DTR) system for Windows — built with JavaFX, MySQL, and the DigitalPersona U.are.U fingerprint SDK.**

Scan a finger, get timed in. Scan again, get timed out. Then export a ready-to-sign DTR in Microsoft Word format — no manual encoding, no buddy punching.

[![Java](https://img.shields.io/badge/Java-8%2B-orange?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-UI-blue?logo=java&logoColor=white)](https://openjfx.io/)
[![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Apache POI](https://img.shields.io/badge/Apache%20POI-DOCX%20export-D22128?logo=apache&logoColor=white)](https://poi.apache.org/)
[![DigitalPersona](https://img.shields.io/badge/DigitalPersona-U.are.U%204500-005f9e)](https://www.crossmatch.com/)
[![Platform](https://img.shields.io/badge/Platform-Windows-0078D6?logo=windows&logoColor=white)](#-requirements)
[![Stars](https://img.shields.io/github/stars/danodoms/fingerprint-attendance?style=social)](https://github.com/danodoms/fingerprint-attendance/stargazers)

![Fingerprint Attendance System](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/48859ac7-5efe-4f7b-bbdd-acaea01b5de5)

</div>

---

## 📌 What is this?

A complete, offline-first **biometric time & attendance console** for small-to-medium organizations. It runs as a single desktop app that plays two roles at once:

- **A kiosk** — the login screen is always listening to the fingerprint reader. Employees walk up, scan, and their time-in / time-out is recorded with voice and sound confirmation. No keyboard, no ID cards, no login required.
- **An admin console** — HR/admin staff log in with credentials to manage employees, departments, positions, shifts, assignments, holidays, leave, and to generate official DTR documents.

Everything is backed by a local MySQL database, so it keeps working when the internet doesn't.

---

## ✨ Highlights

| | |
|---|---|
| 👆 **True 1:N identification** | No employee ID typing. The reader identifies who you are out of the whole database at a 1-in-100,000 false-accept rate. |
| 🔁 **Double-scan time-out** | Timing out requires a confirming second scan (1:1 verify), so accidental taps never close a shift. |
| 🌓 **AM/PM aware rules** | Understands morning and afternoon sessions, blocks duplicate time-outs, and shows a live countdown until the next allowed time-in. |
| 📄 **One-click DTR in Word** | Fills real `.docx` templates via Apache POI — including holidays and approved leave — then opens the file for you. |
| 🔊 **Voice + sound feedback** | Distinct success / deny / prompt cues plus TTS "timed in" and "timed out" announcements. Usable from across the room. |
| 🔌 **Hot-plug reader detection** | The app polls for the scanner every 5 seconds and re-opens it automatically. Unplug it mid-shift and it recovers. |
| 👥 **Role-based access** | Admin, Records Officer, and Employee privileges with a navigation menu that adapts per role. |
| 📊 **Live dashboard** | Real-time headcount charts, today's time-in/time-out counters, and an auto-refreshing attendance rate. |

---

## 📸 Screenshots

<details open>
<summary><b>Click to expand / collapse the gallery</b></summary>

<br>

| | |
|:---:|:---:|
| ![](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/7f73189c-86d2-4e0f-b2dd-ad9a54af9e92) | ![](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/d8d98a26-c104-45b0-9d7f-25cde8d1f81f) |
| ![](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/c3b66918-ddb1-4091-a6cd-ef5f19f165ab) | ![](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/d2866dd4-33a2-4e4e-8485-841ca4a393eb) |
| ![](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/337e0403-ece7-4c8a-a34b-eabed45a03ed) | ![](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/d1ef1856-cd28-41f8-a31a-1cf1486982eb) |
| ![](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/891c75eb-adc7-4d23-a8c4-ae42366b65a2) | ![](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/c9e0dd6c-f3ae-4821-883b-d4b6f50eeb8b) |
| ![](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/18fa5945-3786-4ce8-a8c2-2c668282365c) | ![](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/a1781637-5b4f-40db-9fe9-ea726488b704) |
| ![](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/fec1f3d3-dc51-4e1b-8552-dcc5a968fab7) | ![](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/f703c443-b6cf-444f-8c33-401c3616affa) |
| ![](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/de67c8f1-bca7-42a1-ac11-620a31942874) | ![](https://github.com/danodoms/fingerprint-attendance-system/assets/116992939/55eb902b-195e-430b-9296-ff9e903401a1) |

</details>

---

## 🚀 Features

### 👆 Biometric Time-In / Time-Out (Kiosk Mode)
- Always-on identification thread on the login screen — no interaction needed to start a scan.
- **1:N identification** against every enrolled fingerprint template, tuned to a `1/100,000` false-positive rate.
- Smart attendance state machine:
  - First scan of the day → **Time In**; an open record (no time-out yet) → **Time Out**.
  - Time-out is only committed after a **second confirming scan** (1:1 verification).
  - AM/PM notation split — already timed out in the morning? Blocked until 12:00, with a live "3 hours and 12 minutes until your next time in" countdown.
  - Configurable **daily attendance limit** — extra scans are politely denied.
  - Displays elapsed time since the last time-in before closing a shift.
- Success / failure result panes with the employee's photo, name, timestamp, and an auto-dismiss progress bar.
- Live **recent attendance** feed on the kiosk screen.
- Audio feedback: success, fail, deny, and prompt cues plus recorded TTS announcements.

### 🖐️ Fingerprint Enrollment
- Guided multi-capture enrollment per employee, with on-screen prompts for each scan.
- Stores the raw biometric template (FMD) plus width, height, resolution, finger position, CBEFF ID, and enrollment date.
- **Duplicate detection** — refuses to enroll a finger that already belongs to someone else.
- Per-employee fingerprint count and last-enrollment date; wipe-and-re-enroll supported.
- Graceful handling when the reader is unplugged mid-enrollment.

### 🔐 Authentication & Roles
- Email + password login with **SHA-256 hashed** credentials and a show/hide password toggle.
- Three privilege levels:
  - **Admin** — full access to every module.
  - **Records Officer** — restricted console (Reports, Attendance, Departments, Positions, Shifts, and Fingerprints are hidden) with a dedicated dashboard.
  - **Employee** — kiosk scanning only; cannot log into the console.
- Singleton session management with role-driven navigation gating.

### 📊 Dashboard
- Live clock and date.
- **Gender distribution bar chart** of active employees.
- **Logged-in vs not-logged-in pie chart**, auto-refreshing every 30 seconds, with an attendance-rate percentage.
- Today's **time-in** and **time-out** counters.
- Recent attendance table.

### 👥 Employee Management
- Full CRUD: name, suffix, sex, birth date, contact number, address, email, password, privilege, and **profile photo**.
- **Soft activate/deactivate** instead of destructive deletes — history is never lost.
- Searchable table with privilege and status filters, plus a detail panel per employee.
- Validation layer covering required fields, name charset, email format, **email uniqueness**, and password strength (≥8 characters, uppercase, digit, confirmation match).

### 🏢 Organization Structure
- **Departments** — CRUD with descriptions, name-uniqueness checks, and active/inactive toggles.
- **Positions** — scoped to a department, with per-department duplicate prevention.
- **Shifts** — named shifts with start and end times.
- **Assignments** — bind an employee to a position + shift with custom hours and an assignment date; blocks duplicate active positions per employee.

### 🗓️ Calendars & Leave
- **Holidays / special calendar** — type, description, date range, and file attachment; duplicate-description guard and deactivation.
- **Time-off** — per-employee leave records with type, description, attachment, and date range.
- Both automatically annotate the generated DTR, so holidays and approved leave appear in the right cells.

### 📄 Attendance Records & Reports
- **Attendance module** — every record with name, date, time in, time out, AM/PM notation, and derived status (`✓` / `Late` / `No Out` / `Present`), plus name search and filter reset.
- **Reports module** — three report views: **DTR summary**, **Tardiness / Late report**, and **Absent report**, with year and month pickers and employee search.
- **Microsoft Word export via Apache POI:**
  - **Regular DTR** → `Name_Month_Year.docx`, generated from the `DTR.docx` template with the full monthly grid, AM/PM in/out times, holiday and leave annotations, and cell merging.
  - **Overload DTR** → `Name_Month_Year_OL.docx`, generated from `OL-DTR.docx` with separate AM and PM overload views.
  - The finished document opens automatically in your default word processor.

---

## 🧱 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| UI | JavaFX (FXML + CSS), custom icon set, sidebar navigation |
| Database | MySQL — plus SQL views (`attendance_summary_view`, `overload_view_am/pm`, `recent_attendance_view`, `employee_status_view`, `special_calendar_view`, `user_calendar_schedule`, `user_timeoff_schedule`, `dtr`) |
| Biometrics | DigitalPersona U.are.U SDK (`com.digitalpersona.uareu`) |
| Documents | Apache POI (XWPF) |
| Audio | JavaFX Media (`AudioClip`) |
| Build | Apache Ant / NetBeans (`build.xml`) |

---

## 🧰 Requirements

- **Windows** (the DigitalPersona driver stack is Windows-only)
- **DigitalPersona U.are.U 4500** fingerprint reader — the only device this has been tested against
- **DigitalPersona driver / RTE** installed
- **JDK 8+** with JavaFX available
- **MySQL** running on `localhost:3306`
- **NetBeans** (or any IDE that can consume the Ant `build.xml`)

---

## ⚡ Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/danodoms/fingerprint-attendance.git
   cd fingerprint-attendance
   ```

2. **Download the dependencies** and add them to your project's classpath:
   👉 [Dependencies folder (Google Drive)](https://drive.google.com/drive/folders/1RaBHu1jCNnFNcuNMin5meaqUYKGq8wh7?usp=sharing)

3. **Set up the database** — import the SQL dump from the same Drive folder into your local MySQL server as a schema named `attendance`.

4. **Configure the connection** in `src/Utilities/DatabaseUtil.java`:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/attendance";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "";
   ```

5. **Install the DigitalPersona driver** so the reader is detected. The app will report `Connected fingerprint reader: ...` in the console once it finds one.

6. **Run** `Main.java`. The kiosk/login screen launches and begins listening for scans immediately.

> 💡 Keep `DTR.docx` and `OL-DTR.docx` in the project root — they are the templates used for report generation.

---

## 📁 Project Structure

```
src/
├── Main.java                # JavaFX entry point + reader bootstrap
├── Controller/              # FXML controllers (login, kiosk, dashboard, admin modules)
├── Model/                   # Domain models + all SQL (User, Attendance, Shift, Assignment, ...)
├── View/                    # FXML layouts
├── Fingerprint/             # DigitalPersona threads: Capture, Enrollment, Identification, Verification
├── Utilities/               # DatabaseUtil, Encryption, SoundUtil, Filter, PaneUtil, ImageUtil, ...
├── Session/                 # Singleton logged-in-user session
├── Style/                   # Stylesheets (admin_pane + bundled light/dark themes)
├── Images/                  # Icons, avatars, scan animation
└── Audio/                   # Success / fail / deny / prompt / TTS clips
```

---

## 🗺️ Roadmap & Known Limitations

Contributions on any of these are very welcome:

- [ ] Move database credentials out of source and into a config file / environment variables
- [ ] Salted password hashing (bcrypt/Argon2) instead of plain SHA-256
- [ ] Remove the developer login-bypass buttons on the kiosk screen
- [ ] Re-enable the commented-out department and year/month filters in the Attendance module
- [ ] Replace the Google Drive dependency bundle with Maven or Gradle
- [ ] Cross-platform reader support beyond Windows / U.are.U 4500
- [ ] Flesh out the empty `Dockerfile` for a containerized MySQL setup
- [ ] Wire up the bundled light/dark themes (`nord`, `primer`, `dracula`, `cupertino`) with a theme switcher

---

## 🤝 Contributing

Issues and pull requests are welcome. If you have a different DigitalPersona model — or any U.are.U-compatible reader — reports on whether it works are especially useful.

---

## ⭐ Support

If this project saved you time or gave you a starting point for your own biometric attendance system, consider **starring the repo** — it genuinely helps others find it.

---

<div align="center">

**Keywords:** biometric attendance system · fingerprint time clock · DTR generator · Daily Time Record · JavaFX desktop app · DigitalPersona U.are.U 4500 · employee attendance management · Java MySQL HR system · Apache POI DOCX export

</div>
