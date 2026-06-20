import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private Stage primaryStage;
    private Scene registrationScene;
    private Scene profileScene;

    // Registration UI Components
    private TextField usernameInput;
    private PasswordField passwordInput;
    private TextField emailInput;
    private Label errorLabel;

    // Profile UI Components
    private Label profileUsernameLabel;
    private Label profileEmailLabel;

    @Override
    public void start ( Stage primaryStage ) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle ( "User Portal" );

        // Build both scenes
        createRegistrationScene ( );
        createProfileScene ( );

        // Show the initial registration scene
        primaryStage.setScene ( registrationScene );
        primaryStage.show ( );
    }

    private void createRegistrationScene () {
        // Layout container
        VBox root = new VBox ( 15 ); // 15px spacing
        root.getStyleClass ( ).add ( "vbox-container" );
        root.setAlignment ( Pos.CENTER );

        // Header
        Label headerLabel = new Label ( "Create Account" );
        headerLabel.getStyleClass ( ).add ( "header-label" );

        // Input Fields & Labels
        VBox formGroup = new VBox ( 8 );
        formGroup.setAlignment ( Pos.CENTER_LEFT );
        formGroup.setMaxWidth ( 300 );

        Label userLabel = new Label ( "Username" );
        usernameInput = new TextField ( );
        usernameInput.setPromptText ( "Enter your username" );

        Label passLabel = new Label ( "Password" );
        passwordInput = new PasswordField ( );
        passwordInput.setPromptText ( "Enter your password" );

        Label emailLabel = new Label ( "Email" );
        emailInput = new TextField ( );
        emailInput.setPromptText ( "Enter your email" );

        formGroup.getChildren ( ).addAll (
                userLabel , usernameInput ,
                passLabel , passwordInput ,
                emailLabel , emailInput
        );

        // Error message label
        errorLabel = new Label ( );
        errorLabel.getStyleClass ( ).add ( "error-label" );
        errorLabel.setWrapText ( true );

        // Register Button
        Button registerButton = new Button ( "Register" );
        registerButton.getStyleClass ( ).add ( "primary-button" );
        registerButton.setOnAction ( e -> handleRegistration ( ) );

        root.getChildren ( ).addAll ( headerLabel , formGroup , errorLabel , registerButton );

        // Instantiate Scene and attach CSS
        registrationScene = new Scene ( root , 400 , 500 );
        registrationScene.getStylesheets ( ).add ( getClass ( ).getResource ( "style.css" ).toExternalForm ( ) );
    }

    private void createProfileScene () {
        VBox root = new VBox ( 20 );
        root.getStyleClass ( ).add ( "vbox-container" );
        root.setAlignment ( Pos.CENTER );

        // Header
        Label headerLabel = new Label ( "User Profile" );
        headerLabel.getStyleClass ( ).add ( "header-label" );

        // Display Labels
        profileUsernameLabel = new Label ( );
        profileUsernameLabel.getStyleClass ( ).add ( "profile-info" );

        profileEmailLabel = new Label ( );
        profileEmailLabel.getStyleClass ( ).add ( "profile-info" );

        // Back Button
        Button backButton = new Button ( "Back to Registration" );
        backButton.getStyleClass ( ).add ( "secondary-button" );
        backButton.setOnAction ( e -> animateSceneChange ( registrationScene ) );

        root.getChildren ( ).addAll ( headerLabel , profileUsernameLabel , profileEmailLabel , backButton );

        // Instantiate Scene and attach CSS
        profileScene = new Scene ( root , 400 , 500 );
        profileScene.getStylesheets ( ).add ( getClass ( ).getResource ( "style.css" ).toExternalForm ( ) );
    }

    private void handleRegistration () {
        String username = usernameInput.getText ( ).trim ( );
        String password = passwordInput.getText ( ).trim ( );
        String email = emailInput.getText ( ).trim ( );

        // Validation logic
        if ( username.isEmpty ( ) || password.isEmpty ( ) || email.isEmpty ( ) ) {
            errorLabel.setText ( "Error: All fields are required!" );
            return;
        }

        // If valid, clear error, update profile labels, and transition
        errorLabel.setText ( "" );
        profileUsernameLabel.setText ( "Username: " + username );
        profileEmailLabel.setText ( "Email: " + email );

        animateSceneChange ( profileScene );
    }

    // UX Enhancement: Fade transition when switching scenes
    private void animateSceneChange ( Scene nextScene ) {
        FadeTransition fadeOut = new FadeTransition ( Duration.millis ( 250 ) , primaryStage.getScene ( ).getRoot ( ) );
        fadeOut.setFromValue ( 1.0 );
        fadeOut.setToValue ( 0.0 );

        fadeOut.setOnFinished ( e -> {
            primaryStage.setScene ( nextScene );

            FadeTransition fadeIn = new FadeTransition ( Duration.millis ( 250 ) , nextScene.getRoot ( ) );
            fadeIn.setFromValue ( 0.0 );
            fadeIn.setToValue ( 1.0 );
            fadeIn.play ( );
        } );

        fadeOut.play ( );
    }

    public static void Main ( String[] args ) {
        launch ( args );
    }
}