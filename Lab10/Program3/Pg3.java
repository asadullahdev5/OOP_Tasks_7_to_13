import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Pg3 extends Application {

    @Override
    public void start(Stage stage) {
        Label title = new Label("Temperature Converter");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label celsiusLabel = new Label("Celsius:");
        TextField celsiusField = new TextField();
        celsiusField.setPromptText("Enter temperature in Celsius");
        celsiusField.setMaxWidth(300);
        Tooltip cTip = new Tooltip("Enter Celsius value then click Convert to Fahrenheit");
        celsiusField.setTooltip(cTip);

        Label fahrenheitLabel = new Label("Fahrenheit:");
        TextField fahrenheitField = new TextField();
        fahrenheitField.setPromptText("Enter temperature in Fahrenheit");
        fahrenheitField.setMaxWidth(300);
        Tooltip fTip = new Tooltip("Enter Fahrenheit value then click Convert to Celsius");
        fahrenheitField.setTooltip(fTip);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button toFahrenheitBtn = new Button("Convert to Fahrenheit");
        toFahrenheitBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        toFahrenheitBtn.setMaxWidth(250);
        toFahrenheitBtn.setTooltip(new Tooltip("Formula: F = C * 9/5 + 32"));

        Button toCelsiusBtn = new Button("Convert to Celsius");
        toCelsiusBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        toCelsiusBtn.setMaxWidth(250);
        toCelsiusBtn.setTooltip(new Tooltip("Formula: C = (F - 32) * 5/9"));

        toFahrenheitBtn.setOnAction(e -> {
            errorLabel.setText("");
            try {
                double celsius = Double.parseDouble(celsiusField.getText().trim());
                double fahrenheit = celsius * 9.0 / 5.0 + 32;
                fahrenheitField.setText(String.format("%.2f", fahrenheit));
            } catch(NumberFormatException ex) {
                errorLabel.setText("Invalid input! Please enter a valid number.");
                fahrenheitField.clear();
            }
        });

        toCelsiusBtn.setOnAction(e -> {
            errorLabel.setText("");
            try {
                double fahrenheit = Double.parseDouble(fahrenheitField.getText().trim());
                double celsius = (fahrenheit - 32) * 5.0 / 9.0;
                celsiusField.setText(String.format("%.2f", celsius));
            } catch(NumberFormatException ex) {
                errorLabel.setText("Invalid input! Please enter a valid number.");
                celsiusField.clear();
            }
        });

        VBox layout = new VBox(15, title, celsiusLabel, celsiusField, fahrenheitLabel, fahrenheitField, toFahrenheitBtn, toCelsiusBtn, errorLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Scene scene = new Scene(layout, 400, 420);
        stage.setTitle("Temperature Converter");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
