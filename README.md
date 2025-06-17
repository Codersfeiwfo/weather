Weather Dashboard 🌤️
A JavaFX application that displays current weather by city using OpenWeatherMap API and stores the history in a local SQLite database.

📷 Preview
<img src="screenshot.png" width="500"/>
🚀 Features
🔍 Enter city name and get current weather (temperature & description)

☁️ Real-time weather from OpenWeatherMap API

💾 Save results to local SQLite database

📋 Model class WeatherRecord for clean data handling

🧪 JSON parsing using org.json

🎨 Simple and responsive JavaFX GUI

🛠️ Technologies Used
Java 17+ / 21+ / 24

JavaFX SDK 21+ or 24

SQLite (via JDBC driver)

org.json (2024+)

OpenWeatherMap API

📁 Project Structure
graphql

weather/
│
├── lib/
│   ├── json-20240303.jar
│   └── sqlite-jdbc-3.43.2.2.jar
│
├── src/
│   ├── Main.java            
│   ├── WeatherAPI.java       
│   ├── DatabaseManager.java   
│   └── WeatherRecord.java     
│
├── weather.db                 
├── README.md
└── .gitignore
📄 WeatherRecord.java
Represents a single weather record:

✅ How to Run
Clone the repository:

bash
git clone https://github.com/yourusername/weather-dashboard.git
cd weather-dashboard
Add your OpenWeatherMap API key in WeatherAPI.java:

java
private static final String API_KEY = "your_api_key_here";
Make sure in IntelliJ:

JavaFX SDK is added in Project Structure

VM options contain:
vbnet
--module-path C:\Java\javafx-sdk-24.0.1\lib --add-modules javafx.controls,javafx.fxml
sqlite-jdbc jar is added to the lib folder and project dependencies

Run Main.java and enjoy!

📌 To Do
Add TableView to show history
Add delete/clear history feature

Export data to CSV
Handle edge cases (invalid city, no internet)
