import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Optional;

public class Pg4 extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("To-Do List Manager");

        // 1. Title Header
        Label headerLabel = new Label("My Task List");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // 2. Input Field for tasks
        TextField taskInputField = new TextField();
        taskInputField.setPromptText("Enter a new task here...");
        taskInputField.setTooltip(new Tooltip("Type your task name and click 'Add Task'"));
        taskInputField.setStyle("-fx-padding: 8px; -fx-font-size: 14px;");

        // 3. Add Task Button
        Button addTaskBtn = new Button("Add Task");
        addTaskBtn.setTooltip(new Tooltip("Click to insert task into the list below"));
        addTaskBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 15px;");

        // 4. ListView to hold and display tasks
        // We use an ObservableList so that updates instantly sync with the UI Component
        ObservableList<String> taskDataList = FXCollections.observableArrayList();
        ListView<String> taskListView = new ListView<>(taskDataList);
        taskListView.setPlaceholder(new Label("No tasks added yet. Plan your day!"));
        taskListView.setStyle("-fx-background-radius: 5px; -fx-border-radius: 5px;");

        // 5. Remove Task Button
        Button removeTaskBtn = new Button("Remove Selected Task");
        removeTaskBtn.setTooltip(new Tooltip("Select a task from the list above and click here to delete it"));
        removeTaskBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 15px;");

        // --- FUNCTIONALITY LOGIC ---

        // Add Task Action
        addTaskBtn.setOnAction(e -> {
            String taskText = taskInputField.getText().trim();

            // Validation: Ensure field isn't empty
            if (taskText.isEmpty()) {
                Alert emptyAlert = new Alert(Alert.AlertType.WARNING);
                emptyAlert.setTitle("Validation Warning");
                emptyAlert.setHeaderText(null);
                emptyAlert.setContentText("Task content cannot be empty. Please type something!");
                emptyAlert.showAndWait();
                return;
            }

            // Add item to list data and clear user input
            taskDataList.add(taskText);
            taskInputField.clear();
            taskInputField.requestFocus(); // Return focus to input for better flow
        });

        // Remove Task Action with Confirmation Dialog
        removeTaskBtn.setOnAction(e -> {
            int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();

            // Validation: Ensure a row is actually highlighted
            if (selectedIndex < 0) {
                Alert selectionAlert = new Alert(Alert.AlertType.WARNING);
                selectionAlert.setTitle("No Selection Found");
                selectionAlert.setHeaderText(null);
                selectionAlert.setContentText("Please select a task from the list first to delete it.");
                selectionAlert.showAndWait();
                return;
            }

            String selectedTaskText = taskListView.getSelectionModel().getSelectedItem();

            // Enhancement: Setup a Confirmation Alert Dialog
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Delete Task Confirmation");
            confirmAlert.setContentText("Are you sure you want to delete the task: \n\"" + selectedTaskText + "\"?");

            // Capture user choice
            Optional<ButtonType> userChoice = confirmAlert.showAndWait();
            if (userChoice.isPresent() && userChoice.get() == ButtonType.OK) {
                taskDataList.remove(selectedIndex);
            }
        });

        // 6. Layout Management via VBox
        VBox root = new VBox(12); // 12px margin spacing between child rows
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20px; -fx-background-color: #f8f9fa;");

        // Populate layout
        root.getChildren().addAll(
                headerLabel,
                taskInputField,
                addTaskBtn,
                taskListView,
                removeTaskBtn
        );

        // 7. Initialize Scene & Stage Window
        Scene scene = new Scene(root, 400, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}