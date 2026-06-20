import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// ==========================================
// MAIN JAVAFX APPLICATION CLASS
// ==========================================
/**
 * Note: Kyunki class ka naam UserFormApp hai,
 * isliye file ka naam bhi 'UserFormApp.java' hona zaroori hai.
 */
public class UserFormApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Information System");

        // 1. Label for the Title
        Label titleLabel = new Label("User Information Form");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // 2. Text Field for User Input (e.g., Full Name)
        Label nameLabel = new Label("Full Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        HBox nameLayout = new HBox(10, nameLabel, nameField);
        nameLayout.setAlignment(Pos.CENTER_LEFT);

        // 3. Combo Box for Selecting a City
        Label cityLabel = new Label("Select City:");
        ComboBox<String> cityComboBox = new ComboBox<>();
        cityComboBox.getItems().addAll("Karachi", "Lahore", "Islamabad", "Rawalpindi", "Faisalabad");
        cityComboBox.setPromptText("Choose your city");
        HBox cityLayout = new HBox(10, cityLabel, cityComboBox);
        cityLayout.setAlignment(Pos.CENTER_LEFT);

        // 4. Date Picker for Selecting a Date
        Label dateLabel = new Label("Date of Birth:");
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select a date");
        HBox dateLayout = new HBox(10, dateLabel, datePicker);
        dateLayout.setAlignment(Pos.CENTER_LEFT);

        // 5. Two Buttons ("Submit" and "Cancel") arranged horizontally using HBox
        Button submitButton = new Button("Submit");
        Button cancelButton = new Button("Cancel");

        // Styling buttons professionally
        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox buttonLayout = new HBox(15, submitButton, cancelButton);
        buttonLayout.setAlignment(Pos.CENTER_RIGHT); // Buttons right-align karne ke liye
        buttonLayout.setPadding(new Insets(10, 0, 0, 0));

        // 6. Form Submission Logic (Scenario Simulation Actions)
        submitButton.setOnAction(e -> {
            if (nameField.getText().isEmpty() || cityComboBox.getValue() == null || datePicker.getValue() == null) {
                System.out.println("[ERROR] Please fill out all fields before submitting!");
            } else {
                System.out.println("=== Form Submitted Successfully ===");
                System.out.println("Name: " + nameField.getText());
                System.out.println("City: " + cityComboBox.getValue());
                System.out.println("Date: " + datePicker.getValue());
            }
        });

        cancelButton.setOnAction(e -> {
            System.out.println("[INFO] Form cleared by user.");
            nameField.clear();
            cityComboBox.setValue(null);
            datePicker.setValue(null);
        });

        // 7. Arranging all main components vertically using VBox
        VBox mainLayout = new VBox(20); // 20px vertical spacing components ke darmiyan
        mainLayout.setPadding(new Insets(25)); // Margin around the entire window
        mainLayout.setAlignment(Pos.TOP_LEFT);

        // VBox mein saare components aur sub-HBoxes add karna
        mainLayout.getChildren().addAll(titleLabel, nameLayout, cityLayout, dateLayout, buttonLayout);

        // 8. Scene aur Stage setup
        Scene scene = new Scene(mainLayout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Window size lock karne ke liye
        primaryStage.show();
    }

    // Main method runner
    public static void main(String[] args) {
        // JavaFX Application launch karne ke liye call kiya jata hai
        launch(args);
    }
}