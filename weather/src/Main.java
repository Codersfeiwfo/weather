import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends Application {

    private Label weatherLabel;
    private TableView<WeatherRecord> tableView;
    private ObservableList<WeatherRecord> weatherHistory;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Label titleLabel = new Label("🌤 Weather");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setEffect(new DropShadow(2, Color.rgb(0, 0, 0, 0.3)));


        TextField cityField = new TextField();
        cityField.setPromptText("Enter city name...");
        cityField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10;" +
                        "-fx-font-size: 14;" +
                        "-fx-border-color: #dddddd;" +
                        "-fx-border-radius: 15;"
        );


        Button fetchButton = new Button("Update Weather");
        fetchButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #ffffff44, #ffffff33);" +
                        "-fx-background-radius: 20;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 30 10 30;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 5, 0, 0, 3);"
        );

        fetchButton.setOnMouseEntered(e -> fetchButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #ffffff66, #ffffff44);" +
                        "-fx-background-radius: 20;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 30 10 30;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.35), 7, 0, 0, 4);"
        ));

        fetchButton.setOnMouseExited(e -> fetchButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #ffffff44, #ffffff33);" +
                        "-fx-background-radius: 20;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 30 10 30;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 5, 0, 0, 3);"
        ));

        // Weather info label
        weatherLabel = new Label("           - Weather info will appear here -");
        weatherLabel.setFont(Font.font("Segoe UI", 16));
        weatherLabel.setTextFill(Color.WHITE);
        weatherLabel.setWrapText(true);
        weatherLabel.setMaxWidth(320);

        // Table
        weatherHistory = FXCollections.observableArrayList();
        tableView = new TableView<>(weatherHistory);

        TableColumn<WeatherRecord, String> cityCol = new TableColumn<>("City");
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));

        TableColumn<WeatherRecord, String> tempCol = new TableColumn<>("Temperature");
        tempCol.setCellValueFactory(new PropertyValueFactory<>("temperature"));

        TableColumn<WeatherRecord, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<WeatherRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("datetime"));

        tableView.getColumns().addAll(cityCol, tempCol, descCol, dateCol);
        tableView.setPrefHeight(180);
        tableView.setMaxWidth(420);

        // Clear button
        Button clearButton = new Button("Clear Table");
        clearButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        clearButton.setOnAction(e -> weatherHistory.clear());

        // Save button
        Button saveButton = new Button("Save to File");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        saveButton.setOnAction(e -> saveTableToFile());

        HBox buttonBox = new HBox(10, clearButton, saveButton);
        buttonBox.setAlignment(Pos.CENTER);


        fetchButton.setOnAction(e -> {
            String city = cityField.getText().trim();
            if (!city.isEmpty()) {
                weatherLabel.setText("Fetching weather for: " + city + "...");
                String weather = WeatherAPI.getWeather(city);
                weatherLabel.setText(weather);


                if (weather.contains("Temperature:")) {
                    try {
                        String temp = weather.substring(weather.indexOf("Temperature: ") + 13, weather.indexOf("°C") + 2);
                        String desc = weather.substring(weather.indexOf("°C,") + 3).trim();
                        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        weatherHistory.add(new WeatherRecord(city, temp, desc, datetime));
                    } catch (Exception ex) {
                        weatherLabel.setText("Error parsing weather info.");
                    }
                }
            } else {
                weatherLabel.setText("Please enter a city name.");
            }
        });

        VBox contentBox = new VBox(15, titleLabel, cityField, fetchButton, weatherLabel, tableView, buttonBox);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));
        contentBox.setMaxWidth(460);
        contentBox.setStyle("-fx-background-color: rgba(255,255,255,0.08); -fx-background-radius: 25;");

        // Background
        StackPane root = new StackPane(contentBox);
        BackgroundFill gradientFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#74ebd5")),
                        new Stop(1, Color.web("#ACB6E5"))
                ),
                CornerRadii.EMPTY,
                Insets.EMPTY
        );
        root.setBackground(new Background(gradientFill));

        Scene scene = new Scene(root, 500, 650);
        primaryStage.setTitle("Weather - OneUI Inspired");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveTableToFile() {
        try (FileWriter writer = new FileWriter("weather_history.csv")) {
            writer.write("City,Temperature,Description,DateTime\n");
            for (WeatherRecord record : weatherHistory) {
                writer.write(String.format("%s,%s,%s,%s\n",
                        record.getCity(),
                        record.getTemperature(),
                        record.getDescription(),
                        record.getDatetime()));
            }
            weatherLabel.setText("History saved to weather_history.csv");
        } catch (IOException e) {
            weatherLabel.setText("Error saving file.");
            e.printStackTrace();
        }
    }
}
