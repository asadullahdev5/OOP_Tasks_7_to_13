package LAB12Task2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Random;

public class TwoFactorAuthSystem extends Application {

    private Stage primaryStage;
    private BorderPane mainRootLayout;

    // Login Form Fields
    private TextField usernameField;
    private PasswordField passwordField;
    private ComboBox<String> roleComboBox;
    private Label errorLabel;
    private Button loginBtn;

    // 2FA Fields
    private TextField otpInputField;
    private Label otpErrorLabel;
    private Button verifyOtpBtn;

    // State Variables for Simulation
    private String generatedOTP;
    private int otpAttemptsRemaining = 3;
    private String validatedRole = "";

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Secure Portal - Two-Factor Authentication");

        mainRootLayout = new BorderPane();
        mainRootLayout.setStyle("-fx-background-color: #f5f6fa;");

        showLoginScreen();

        Scene scene = new Scene(mainRootLayout, 500, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- 1. LOGIN SCREEN VIEW ---
    private void showLoginScreen() {
        VBox loginBox = new VBox(15);
        loginBox.setPadding(new Insets(30));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setMaxWidth(380);

        Label headerLabel = new Label("Secure Login Portal");
        headerLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(12);
        formGrid.setAlignment(Pos.CENTER);

        Label userLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        Label passLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Label roleLabel = new Label("Select Role:");
        roleComboBox = new ComboBox<>(FXCollections.observableArrayList("Admin", "Manager", "User"));
        roleComboBox.setPromptText("Choose role");
        roleComboBox.setMaxWidth(Double.MAX_VALUE);

        formGrid.add(userLabel, 0, 0);
        formGrid.add(usernameField, 1, 0);
        formGrid.add(passLabel, 0, 1);
        formGrid.add(passwordField, 1, 1);
        formGrid.add(roleLabel, 0, 2);
        formGrid.add(roleComboBox, 1, 2);

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 13px;");
        errorLabel.setWrapText(true);

        loginBtn = new Button("Verify Identity");
        loginBtn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 25px; -fx-cursor: hand;");
        loginBtn.setOnAction(e -> handlePrimaryAuthentication());

        loginBox.getChildren().addAll(headerLabel, formGrid, errorLabel, loginBtn);
        mainRootLayout.setCenter(loginBox);
    }

    // --- 2. PRIMARY INSTANCE HANDLER (STEP 1) ---
    private void handlePrimaryAuthentication() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String selectedRole = roleComboBox.getValue();

        errorLabel.setText("");

        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            errorLabel.setText("Error: Fields cannot be blank.");
            return;
        }

        // Credential Checks
        boolean isValid = false;
        if (selectedRole.equals("Admin") && username.equals("admin") && password.equals("admin123")) {
            isValid = true;
        } else if (selectedRole.equals("Manager") && username.equals("manager") && password.equals("manager123")) {
            isValid = true;
        } else if (selectedRole.equals("User") && username.equals("user") && password.equals("user123")) {
            isValid = true;
        }

        if (isValid) {
            validatedRole = selectedRole; // Cache for step 2 redirect
            triggerOtpGeneration();       // Initialize step 2
        } else {
            errorLabel.setText("Authentication Failed: Invalid credentials or role mismatch.");
        }
    }

    // --- 3. 2FA CORE OTP PROCESSING MECHANISM ---
    private void triggerOtpGeneration() {
        // Generate random 4-Digit numerical string code
        Random rand = new Random();
        int num = 1000 + rand.nextInt(9000);
        generatedOTP = String.valueOf(num);

        // Required Simulation Console Output
        System.out.println("\n=================================");
        System.out.println("SYSTEM SECURITY BANNER [2FA]");
        System.out.println("Your simulated One-Time Password is: " + generatedOTP);
        System.out.println("=================================\n");

        // Reset tracking limits
        otpAttemptsRemaining = 3;

        // Transition to OTP verification panel UI screen view
        showOtpVerificationScreen();
    }

    // --- 4. OTP VERIFICATION SCREEN VIEW (STEP 2) ---
    private void showOtpVerificationScreen() {
        VBox otpBox = new VBox(15);
        otpBox.setPadding(new Insets(30));
        otpBox.setAlignment(Pos.CENTER);
        otpBox.setMaxWidth(380);

        Label headerLabel = new Label("Two-Step Verification");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label instructionsLabel = new Label("A security code has been transmitted to your console window. Enter the code below to finalize session approval.");
        instructionsLabel.setWrapText(true);
        instructionsLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        instructionsLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px;");

        otpInputField = new TextField();
        otpInputField.setPromptText("Enter 4-digit OTP");
        otpInputField.setAlignment(Pos.CENTER);
        otpInputField.setMaxWidth(180);
        otpInputField.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-letter-spacing: 2px;");

        otpErrorLabel = new Label("Attempts Remaining: " + otpAttemptsRemaining);
        otpErrorLabel.setStyle("-fx-text-fill: #d35400; -fx-font-weight: bold;");

        verifyOtpBtn = new Button("Validate Token");
        verifyOtpBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-cursor: hand;");
        verifyOtpBtn.setOnAction(e -> handleOtpVerification());

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #dcdde1; -fx-text-fill: #2c3e50;");
        cancelBtn.setOnAction(e -> showLoginScreen());

        HBox actions = new HBox(10, verifyOtpBtn, cancelBtn);
        actions.setAlignment(Pos.CENTER);

        otpBox.getChildren().addAll(headerLabel, instructionsLabel, otpInputField, otpErrorLabel, actions);
        mainRootLayout.setCenter(otpBox);
    }

    private void handleOtpVerification() {
        String enteredOtp = otpInputField.getText().trim();

        if (enteredOtp.equals(generatedOTP)) {
            // Success! Load Dashboard View based on cached role
            loadTargetRoleDashboard();
        } else {
            otpAttemptsRemaining--;

            if (otpAttemptsRemaining <= 0) {
                // LOCKOUT STATE CONDITION TRIGGERED
                triggerSystemLockout();
            } else {
                otpErrorLabel.setText("Invalid OTP Code! Attempts remaining: " + otpAttemptsRemaining);
                otpInputField.clear();
                otpInputField.requestFocus();
            }
        }
    }

    // --- 5. SECURE LOCKOUT VIEW ---
    private void triggerSystemLockout() {
        VBox lockBox = new VBox(15);
        lockBox.setPadding(new Insets(35));
        lockBox.setAlignment(Pos.CENTER);

        Label alertIcon = new Label("⚠️");
        alertIcon.setStyle("-fx-font-size: 48px;");

        Label errorTitle = new Label("Access Prohibited - Terminal Locked");
        errorTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #c0392b;");

        Label msg = new Label("Maximum authorization credentials attempts exceeded. This login route sequence session has been frozen for safety compliance.");
        msg.setWrapText(true);
        msg.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        msg.setStyle("-fx-text-fill: #7f8c8d;");

        lockBox.getChildren().addAll(alertIcon, errorTitle, msg);
        mainRootLayout.setCenter(lockBox);
    }

    // --- 6. TARGET CONTEXT REDIRECTS ---
    private void loadTargetRoleDashboard() {
        VBox view = new VBox(15);
        view.setPadding(new Insets(30));
        view.setAlignment(Pos.CENTER);

        Label successIcon = new Label("🔒 Success");
        successIcon.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2ecc71;");

        Label welcomeMsg = new Label("Login Successful! Granted authorization profile clearance level: " + validatedRole.toUpperCase());
        welcomeMsg.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        welcomeMsg.setWrapText(true);

        Button logoutBtn = new Button("Terminate Session");
        logoutBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> {
            showLoginScreen();
            usernameField.clear();
            passwordField.clear();
        });

        view.getChildren().addAll(successIcon, welcomeMsg, logoutBtn);
        mainRootLayout.setCenter(view);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
