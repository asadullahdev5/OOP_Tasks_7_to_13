import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class ProfileManagementApp extends Application {

    // Form Fields
    private TextField nameField;
    private TextField emailField;
    private ToggleGroup genderGroup;
    private RadioButton maleRadio;
    private RadioButton femaleRadio;
    private RadioButton otherRadio;
    private TextField addressField;
    private DatePicker dobPicker;
    private Label errorLabel;

    // Table View
    private TableView<UserProfile> profileTable;
    private final ObservableList<UserProfile> profileDataList = FXCollections.observableArrayList();

    private static final String FILE_NAME = "user_profiles.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Profile Management System");

        // --- LEFT PANE: Registration & Form Input ---
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(20));
        formBox.setStyle("-fx-background-color: #ffffff;");
        formBox.setPrefWidth(350);

        Label formTitle = new Label("Create User Profile");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);

        // Name
        Label nameLabel = new Label("Full Name:");
        nameField = new TextField();
        nameField.setPromptText("John Doe");

        // Email
        Label emailLabel = new Label("Email Address:");
        emailField = new TextField();
        emailField.setPromptText("john@example.com");

        // Gender
        Label genderLabel = new Label("Gender:");
        genderGroup = new ToggleGroup();
        maleRadio = new RadioButton("Male");
        maleRadio.setToggleGroup(genderGroup);
        maleRadio.setSelected(true); // Default
        femaleRadio = new RadioButton("Female");
        femaleRadio.setToggleGroup(genderGroup);
        otherRadio = new RadioButton("Other");
        otherRadio.setToggleGroup(genderGroup);

        HBox genderBox = new HBox(8, maleRadio, femaleRadio, otherRadio);

        // Address
        Label addressLabel = new Label("Address:");
        addressField = new TextField();
        addressField.setPromptText("123 Street, Karachi");
        addressField.setTooltip(new Tooltip("Must contain a street address and city separated by a comma"));

        // Date of Birth
        Label dobLabel = new Label("Date of Birth:");
        dobPicker = new DatePicker();
        dobPicker.setPromptText("Select Date");
        dobPicker.setMaxWidth(Double.MAX_VALUE);

        // Adding items to grid layout
        grid.add(nameLabel, 0, 0);  grid.add(nameField, 1, 0);
        grid.add(emailLabel, 0, 1); grid.add(emailField, 1, 1);
        grid.add(genderLabel, 0, 2); grid.add(genderBox, 1, 2);
        grid.add(addressLabel, 0, 3); grid.add(addressField, 1, 3);
        grid.add(dobLabel, 0, 4);   grid.add(dobPicker, 1, 4);

        // Validation Error Display Message Label
        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 12px;");
        errorLabel.setWrapText(true);

        // Action Save Button
        Button saveButton = new Button("Save Profile");
        saveButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-cursor: hand;");
        saveButton.setOnAction(e -> handleProfileSaving());

        formBox.getChildren().addAll(formTitle, new Separator(), grid, errorLabel, saveButton);

        // --- RIGHT PANE: Structured Data Display (TableView) ---
        VBox tableBox = new VBox(10);
        tableBox.setPadding(new Insets(20));
        tableBox.setStyle("-fx-background-color: #f8f9fa;");

        Label tableTitle = new Label("Stored User Profiles");
        tableTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        profileTable = new TableView<>();

        TableColumn<UserProfile, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        nameCol.setPrefWidth(120);

        TableColumn<UserProfile, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(c -> c.getValue().emailProperty());
        emailCol.setPrefWidth(140);

        TableColumn<UserProfile, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(c -> c.getValue().genderProperty());
        genderCol.setPrefWidth(70);

        TableColumn<UserProfile, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(c -> c.getValue().addressProperty());
        addressCol.setPrefWidth(160);

        TableColumn<UserProfile, String> dobCol = new TableColumn<>("Date of Birth");
        dobCol.setCellValueFactory(c -> c.getValue().dobProperty());
        dobCol.setPrefWidth(100);

        profileTable.getColumns().addAll(nameCol, emailCol, genderCol, addressCol, dobCol);
        profileTable.setItems(profileDataList);
        profileTable.setPlaceholder(new Label("No profile records found in text database file."));

        tableBox.getChildren().addAll(tableTitle, profileTable);

        // --- COMBINE BOTH WINDOWS USING A SPLITPANE ---
        SplitPane splitPane = new SplitPane(formBox, tableBox);
        splitPane.setDividerPositions(0.4); // 40% left, 60% right

        // Read pre-existing profile database collections immediately on boot
        readProfilesFromFile();

        Scene scene = new Scene(splitPane, 960, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- LOGIC AND DATA ACTION VALIDATION PIPELINES ---
    private void handleProfileSaving() {
        errorLabel.setText(""); // Reset log

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        LocalDate dob = dobPicker.getValue();

        // Determine chosen gender string
        String gender = "Other";
        if (maleRadio.isSelected()) gender = "Male";
        else if (femaleRadio.isSelected()) gender = "Female";

        // 1. Basic Emptiness Validation Checks
        if (name.isEmpty() || email.isEmpty() || address.isEmpty() || dob == null) {
            errorLabel.setText("Validation Error: All layout entry fields are required.");
            return;
        }

        // 2. Email Regular Expression Validation Checklist
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Pattern.matches(emailRegex, email)) {
            errorLabel.setText("Validation Error: Invalid email formatting structure.");
            return;
        }

        // 3. Address Content Rules Check (Must contain street AND city, simulated by comma-split context)
        if (!address.contains(",") || address.split(",").length < 2 || address.split(",")[1].trim().isEmpty()) {
            errorLabel.setText("Validation Error: Address must contain both Street and City (separated by a comma).");
            return;
        }

        // 4. Date of Birth verification context formatting check
        String formattedDob;
        try {
            formattedDob = DATE_FORMATTER.format(dob);
        } catch (DateTimeParseException ex) {
            errorLabel.setText("Validation Error: Date of birth format initialization crash error.");
            return;
        }

        // If validation steps succeed, compose CSV structural representation record row string
        // We replace any comma entries within fields to prevent database injection corruption
        String cleanName = name.replace(",", " ");
        String cleanAddress = address.replace(",", " -");

        String recordRowStr = String.format("%s,%s,%s,%s,%s", cleanName, email, gender, cleanAddress, formattedDob);

        // Save entry straight into flat-text storage file directory layout
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_NAME, true)))) {
            out.println(recordRowStr);
        } catch (IOException e) {
            showAlertDialog(Alert.AlertType.ERROR, "System IO Failure", "Could not commit record to text storage disk layout directory setup.");
            return;
        }

        // Instantly push item reference onto table dataset collection view list framework
        profileDataList.add(new UserProfile(cleanName, email, gender, cleanAddress, formattedDob));

        // Wipe data input elements fields so user can input next record cleanly
        nameField.clear();
        emailField.clear();
        addressField.clear();
        dobPicker.setValue(null);
    }

    // --- DATABASE INTEGRATION FILE SYSTEM I/O READ pipelines ---
    private void readProfilesFromFile() {
        profileDataList.clear();
        File dbFile = new File(FILE_NAME);

        if (!dbFile.exists()) {
            return; // Safe exit point if database doesn't exist yet on first boot run
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dbFile))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split(",");
                // Ensure data row consistency matching all 5 database parameters column slots
                if (tokens.length == 5) {
                    profileDataList.add(new UserProfile(tokens[0].trim(), tokens[1].trim(), tokens[2].trim(), tokens[3].trim(), tokens[4].trim()));
                } else {
                    System.err.println("Database formatting skipping corrupted record array at tracking index line: " + lineNumber);
                }
            }
        } catch (IOException e) {
            showAlertDialog(Alert.AlertType.ERROR, "Database Access Exception", "Failed to compile background profiles parsing from disk flat system.");
        }
    }

    private void showAlertDialog(Alert.AlertType type, String title, String description) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(description);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // --- INNER CLASS DATA MODEL FOR TABLE POPULATION ---
    public static class UserProfile {
        private final SimpleStringProperty name;
        private final SimpleStringProperty email;
        private final SimpleStringProperty gender;
        private final SimpleStringProperty address;
        private final SimpleStringProperty dob;

        public UserProfile(String name, String email, String gender, String address, String dob) {
            this.name = new SimpleStringProperty(name);
            this.email = new SimpleStringProperty(email);
            this.gender = new SimpleStringProperty(gender);
            this.address = new SimpleStringProperty(address);
            this.dob = new SimpleStringProperty(dob);
        }

        public String getName() { return name.get(); }
        public SimpleStringProperty nameProperty() { return name; }

        public String getEmail() { return email.get(); }
        public SimpleStringProperty emailProperty() { return email; }

        public String getGender() { return gender.get(); }
        public SimpleStringProperty genderProperty() { return gender; }

        public String getAddress() { return address.get(); }
        public SimpleStringProperty addressProperty() { return address; }

        public String getDob() { return dob.get(); }
        public SimpleStringProperty dobProperty() { return dob; }
    }
}