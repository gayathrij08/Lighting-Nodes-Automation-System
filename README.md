# 💡 Lighting Nodes Automation System

A Java Swing-based desktop application that simulates an intelligent lighting node management system using alternating lighting patterns and automatic government holiday detection to improve energy efficiency.

---

## 📌 Overview

The Lighting Nodes Automation System (LNAS) is designed to automate the operation of street lighting nodes based on the selected date and predefined scheduling rules.

The application automatically detects Indian Government Holidays, applies lighting patterns, calculates energy savings, and displays the active/inactive lighting nodes through an interactive dashboard.

---

## ✨ Features

- 📅 Automatic date selection using an integrated calendar
- 🇮🇳 Automatic Indian Government Holiday detection
- 🔄 Alternating Pattern A and Pattern B lighting logic
- 💡 7 Lighting Nodes simulation
- 📊 Real-time energy savings calculation
- 📈 Dashboard displaying:
  - Selected Date
  - Day Name
  - Holiday Status
  - Current Pattern
  - Pattern Shift Count
  - Lights ON
  - Lights OFF
  - Energy Saved
  - System Status
- 📆 Holiday Insights
  - Previous Government Holiday
  - Next Government Holiday
  - Days Remaining
- 🎨 Professional Java Swing GUI
- 🌙 Light/Dark Theme Support
- 🖥️ Responsive dashboard layout
- ⚡ Modular and package-based architecture

---

## 🛠️ Technologies Used

- Java
- Java Swing
- AWT
- Java Time API (LocalDate)
- Object-Oriented Programming

---

## 📂 Project Structure

```
Lighting-Nodes-Automation-System
│
├── StreetLightGUI.java              # Application launcher
│
├── com
│   └── streetlight
│       ├── model
│       │     ├── PatternController.java
│       │     ├── SimulationResult.java
│       │     └── StreetLightSimulation.java
│       │
│       ├── service
│       │     └── HolidayService.java
│       │
│       ├── ui
│       │     ├── CalendarPanel.java
│       │     ├── LightIndicator.java
│       │     ├── RoundedButton.java
│       │     ├── RoundedPanel.java
│       │     ├── StreetLightGUI.java
│       │     ├── UIConstants.java
│       │     └── UIFactory.java
│       │
│       └── util
│             └── DateUtilities.java
│
├── index.html
├── script.js
├── style.css
└── README.md
```

---

## ⚙️ System Workflow

1. User selects a date.
2. The application checks whether the selected date is a Government Holiday.
3. If it is a holiday:
   - All lighting nodes remain OFF.
   - Maximum energy savings are displayed.
4. Otherwise:
   - Pattern A or Pattern B is selected.
   - Active and inactive nodes are displayed.
   - Dashboard updates with simulation statistics.
5. Holiday insights and energy analytics are refreshed.

---

## 🔄 Lighting Pattern Logic

### Pattern A

Active Nodes:

- LN-01
- LN-03
- LN-05
- LN-07

Inactive Nodes:

- LN-02
- LN-04
- LN-06

---

### Pattern B

Active Nodes:

- LN-02
- LN-04
- LN-06

Inactive Nodes:

- LN-01
- LN-03
- LN-05
- LN-07

---

### Government Holiday

All lighting nodes remain OFF to maximize energy conservation.

---

## 🚀 How to Run

### Clone Repository

```bash
git clone https://github.com/gayathrij08/Lighting-Nodes-Automation-System.git
```

Open the project folder.

### Compile

```bash
javac -d . StreetLightGUI.java com\streetlight\util\*.java com\streetlight\service\*.java com\streetlight\model\*.java com\streetlight\ui\*.java
```

### Run

```bash
java StreetLightGUI
```

---

## 📸 Screenshots

Add screenshots of the application here.

Example:

```
screenshots/dashboard.png
screenshots/calendar.png
```

---

## 🎯 Future Enhancements

- Database integration
- IoT-enabled lighting node monitoring
- Sensor-based automation
- Live weather integration
- Web dashboard
- Mobile application
- Cloud analytics
- Real-time node monitoring

---

## 👩‍💻 Author

**Gayathri J**

GitHub:
https://github.com/gayathrij08

---

## 📄 License

This project is developed for educational and learning purposes.
