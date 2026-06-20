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

public class RoleAuthSystem extends Application {

    private Stage primaryStage;
    private BorderPane mainRootLayout;

    // UI Input References
    private TextField usernameField;
    private PasswordField passwordField;
    private ComboBox<String> roleComboBox;
    private Label errorLabel;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Secure Portal - Role Authentication");

        // Use a base BorderPane to seamlessly swap inner panels/views
        mainRootLayout = new BorderPane();
        mainRootLayout.setStyle("-fx-background-color: #f5f6fa;");

        // Initially show the login view
        showLoginScreen();

        Scene scene = new Scene(mainRootLayout, 500, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- 1. LOGIN SCREEN INTERFACE ---
    private void showLoginScreen() {
        VBox loginBox = new VBox(15);
        loginBox.setPadding(new Insets(30));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setMaxWidth(380);

        // Header Title
        Label headerLabel = new Label("System Login");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Grid Form for alignment
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
        roleComboBox.setMaxWidth(Double.MAX_VALUE); // Stretch to fit column

        // Layout grid coordinates (Column, Row)
        formGrid.add(userLabel, 0, 0);
        formGrid.add(usernameField, 1, 0);
        formGrid.add(passLabel, 0, 1);
        formGrid.add(passwordField, 1, 1);
        formGrid.add(roleLabel, 0, 2);
        formGrid.add(roleComboBox, 1, 2);

        // Feedback Label for Errors
        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 13px;");
        errorLabel.setWrapText(true);

        // Login Button Action Trigger
        Button loginBtn = new Button("Sign In");
        loginBtn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 25px; -fx-font-size: 14px; -fx-cursor: hand;");
        loginBtn.setOnAction(e -> handleLoginAuthentication());

        loginBox.getChildren().addAll(headerLabel, formGrid, errorLabel, loginBtn);

        // Wrap container inside center frame
        mainRootLayout.setCenter(loginBox);
    }

    // --- 2. AUTHENTICATION CONTROLLER LOGIC ---
    private void handleLoginAuthentication() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String selectedRole = roleComboBox.getValue();

        errorLabel.setText(""); // Reset warnings

        // Validation Check: Look for blank fields
        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            errorLabel.setText("Access Denied: Missing credentials or role selection.");
            return;
        }

        // Validate matching combinations against credentials framework matrix
        if (selectedRole.equals("Admin") && username.equals("admin") && password.equals("admin123")) {
            loadAdminDashboard();
        } else if (selectedRole.equals("Manager") && username.equals("manager") && password.equals("manager123")) {
            loadManagerDashboard();
        } else if (selectedRole.equals("User") && username.equals("user") && password.equals("user123")) {
            loadUserDashboard();
        } else {
            // Mismatch structural error block trigger
            errorLabel.setText("Authentication Failed: Bad username, password, or role mismatch.");
        }
    }

    // --- 3. ROLE-SPECIFIC INTERFACES ---

    // VIEW A: ADMIN DASHBOARD
    private void loadAdminDashboard() {
        VBox view = createDashboardBaseContainer("Admin Control panel", "#e74c3c");

        Label featureDesc = new Label("Global Administrative Actions Available:");
        featureDesc.setStyle("-fx-font-weight: bold;");

        ListView<String> userManagementList = new ListView<>(FXCollections.observableArrayList(
                "Modify User Records Database",
                "Review Security Log Metrics",
                "Configure Global Networking Privileges"
        ));
        userManagementList.setMaxHeight(120);

        Button manageUsersBtn = new Button("Add New System Account");
        manageUsersBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        view.getChildren().addAll(featureDesc, userManagementList, manageUsersBtn, createLogoutButton());
        mainRootLayout.setCenter(view);
    }

    // VIEW B: MANAGER DASHBOARD
    private void loadManagerDashboard() {
        VBox view = createDashboardBaseContainer("Manager Analytics Desk", "#3498db");

        Label featureDesc = new Label("Team Allocation Metrics & Overviews:");
        featureDesc.setStyle("-fx-font-weight: bold;");

        TextArea teamNotes = new TextArea("Project Status: Alpha Sprint Complete.\nTeam Performance: On Track.\nBudget Allocated: 84% Remaining.");
        teamNotes.setMaxHeight(100);
        teamNotes.setEditable(true);

        Button saveProgressBtn = new Button("Commit Team Updates");
        saveProgressBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        view.getChildren().addAll(featureDesc, teamNotes, saveProgressBtn, createLogoutButton());
        mainRootLayout.setCenter(view);
    }

    // VIEW C: USER WELCOME DASHBOARD
    private void loadUserDashboard() {
        VBox view = createDashboardBaseContainer("User Portal Main Hub", "#2ecc71");

        Label welcomeText = new Label("Welcome back to your workspace!");
        welcomeText.setStyle("-fx-font-size: 16px;");

        Label informativeMessage = new Label("You have standard browsing permissions. Reach out to an administrator if your current departmental clearances need elevation.");
        informativeMessage.setWrapText(true);
        informativeMessage.setStyle("-fx-text-fill: #7f8c8d;");

        view.getChildren().addAll(welcomeText, informativeMessage, createLogoutButton());
        mainRootLayout.setCenter(view);
    }

    // --- HELPER LAYOUT REUSE FUNCTIONS ---

    private VBox createDashboardBaseContainer(String titleText, String accentHexColor) {
        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.setAlignment(Pos.TOP_LEFT);

        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + accentHexColor + ";");
        container.getChildren().addAll(title, new Separator());

        return container;
    }

    private Button createLogoutButton() {
        Button logoutBtn = new Button("Secure Log Out →");
        logoutBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-font-weight: bold; -fx-margin-top: 15px;");
        // Clear old inputs fields and swap back cleanly to login menu
        logoutBtn.setOnAction(e -> {
            showLoginScreen();
            usernameField.clear();
            passwordField.clear();
        });
        return logoutBtn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}