package Program01;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Properties;

public class Pro1 extends Application {

    private TreeView<File> directoryTreeView;
    private TableView<FileItem> fileTableView;
    private TextArea previewTextArea;
    private TextField searchField;
    private Label footerStatsLabel;

    private ObservableList<FileItem> masterFileData = FXCollections.observableArrayList();
    private FilteredList<FileItem> filteredFileData;

    private static final String CONFIG_FILE = "file_manager_config.properties";
    private static final String DEFAULT_ROOT_DIR = System.getProperty("user.home");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dynamic File Management System");

        // Load saved state (Last accessed directory and search filter)
        Properties props = loadSettings();
        String savedPath = props.getProperty("lastDirectory", DEFAULT_ROOT_DIR);
        String savedSearch = props.getProperty("lastSearch", "");

        File rootDir = new File(savedPath);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            rootDir = new File(DEFAULT_ROOT_DIR);
        }

        // --- 1. TOP PANE: Header & Search Bar ---
        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(15));
        topBox.setStyle("-fx-background-color: #2c3e50;");

        Label titleLabel = new Label("File Management System");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox searchLayout = new HBox(10);
        searchLayout.setAlignment(Pos.CENTER_LEFT);
        Label searchLabel = new Label("Search Files:");
        searchLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        searchField = new TextField(savedSearch);
        searchField.setPromptText("Type file name to filter...");
        searchField.setPrefWidth(300);
        searchLayout.getChildren().addAll(searchLabel, searchField);

        topBox.getChildren().addAll(titleLabel, searchLayout);

        // --- 2. LEFT PANE: Directory TreeView ---
        VBox leftBox = new VBox(5);
        leftBox.setPadding(new Insets(10));
        Label treeLabel = new Label("Directory Browser");
        treeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");

        TreeItem<File> rootTreeItem = createTreeNodes(rootDir);
        directoryTreeView = new TreeView<>(rootTreeItem);
        directoryTreeView.setPrefWidth(220);
        leftBox.getChildren().addAll(treeLabel, directoryTreeView);

        // --- 3. CENTER PANE: File Details TableView ---
        fileTableView = new TableView<>();

        TableColumn<FileItem, String> nameCol = new TableColumn<>("File Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameCol.setPrefWidth(200);

        TableColumn<FileItem, String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
        sizeCol.setPrefWidth(100);

        TableColumn<FileItem, String> dateCol = new TableColumn<>("Last Modified");
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        dateCol.setPrefWidth(150);

        fileTableView.getColumns().addAll(nameCol, sizeCol, dateCol);

        // Wrap master data into a FilteredList for dynamic searching
        filteredFileData = new FilteredList<>(masterFileData, p -> true);
        fileTableView.setItems(filteredFileData);
        fileTableView.setPlaceholder(new Label("Select a folder from the tree or clear your search filter."));

        // --- 4. RIGHT PANE: Plain Text Preview ---
        VBox rightBox = new VBox(5);
        rightBox.setPadding(new Insets(10));
        Label previewLabel = new Label("File Preview (Text Only)");
        previewLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");

        previewTextArea = new TextArea();
        previewTextArea.setEditable(false);
        previewTextArea.setWrapText(true);
        previewTextArea.setPrefWidth(250);
        previewTextArea.setPromptText("Select a file from the table to preview its content.");
        rightBox.getChildren().addAll(previewLabel, previewTextArea);

        // --- 5. BOTTOM PANE: Footer Statistics ---
        HBox footerBox = new HBox();
        footerBox.setPadding(new Insets(10, 15, 10, 15));
        footerBox.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 1px 0px 0px 0px;");
        footerStatsLabel = new Label("Total Files: 0  |  Combined Size: 0 KB");
        footerStatsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        footerBox.getChildren().add(footerStatsLabel);

        // --- INTERACTIVE FUNCTIONALITY & EVENT HANDLERS ---

        // A. Clicking on directory tree node updates the Table View
        directoryTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadDirectoryFiles(newValue.getValue());
            }
        });

        // B. Real-time Search Input Filter Listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredFileData.setPredicate(fileItem -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return fileItem.getName().toLowerCase().contains(lowerCaseFilter);
            });
            updateFooterStats();
        });

        // C. Clicking on a row file updates the Preview pane
        fileTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                previewFileContent(newValue.getFileRef());
            } else {
                previewTextArea.clear();
            }
        });

        // --- MAIN LAYOUT ASSEMBLY ---
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topBox);
        borderPane.setLeft(leftBox);
        borderPane.setCenter(fileTableView);
        borderPane.setRight(rightBox);
        borderPane.setBottom(footerBox);

        // Apply saved search state filter immediately if it exists
        if (!savedSearch.isEmpty()) {
            searchField.fireEvent(new javafx.scene.input.KeyEvent(
                    javafx.scene.input.KeyEvent.KEY_RELEASED, "", "", null, false, false, false, false
            ));
        }

        // Explicitly focus and run initial folder scan
        directoryTreeView.getSelectionModel().select(rootTreeItem);
        loadDirectoryFiles(rootDir);

        Scene scene = new Scene(borderPane, 950, 600);
        primaryStage.setScene(scene);

        // Save state before program window exits
        File finalRootDir = rootDir;
        primaryStage.setOnCloseRequest( windowEvent -> {
            TreeItem<File> selectedItem = directoryTreeView.getSelectionModel().getSelectedItem();
            File activeDir = (selectedItem != null) ? selectedItem.getValue() : finalRootDir;
            saveSettings(activeDir.getAbsolutePath(), searchField.getText());
        });

        primaryStage.show();
    }

    // Recursively generates TreeView architecture nodes
    private TreeItem<File> createTreeNodes(File directory) {
        TreeItem<File> item = new TreeItem<>(directory) {
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;
            private boolean isLeaf;

            @Override
            public ObservableList<TreeItem<File>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;
                    super.getChildren().setAll(buildChildren(this));
                }
                return super.getChildren();
            }

            @Override
            public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
                    File f = getValue();
                    isLeaf = f.isFile();
                }
                return isLeaf;
            }

            private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> TreeItem) {
                File f = TreeItem.getValue();
                if (f != null && f.isDirectory()) {
                    File[] files = f.listFiles(File::isDirectory); // Show folders only in TreeView
                    if (files != null) {
                        ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();
                        for (File childFile : files) {
                            children.add(createTreeNodes(childFile));
                        }
                        return children;
                    }
                }
                return FXCollections.emptyObservableList();
            }

            @Override
            public String toString() {
                return getValue().getName().isEmpty() ? getValue().getPath() : getValue().getName();
            }
        };

        // Simple override to display directory names cleanly on labels
        item.setValue(directory);
        return item;
    }

    // Scans folder elements and updates the Table data source
    private void loadDirectoryFiles(File folder) {
        masterFileData.clear();
        if (folder != null && folder.isDirectory()) {
            File[] files = folder.listFiles(File::isFile); // Populate only files inside Table
            if (files != null) {
                for (File file : files) {
                    masterFileData.add(new FileItem(file));
                }
            }
        }
        updateFooterStats();
    }

    // Iterates filtered data and sums metrics for the footer bar UI
    private void updateFooterStats() {
        int totalFiles = filteredFileData.size();
        long combinedSizeBytes = 0;

        for (FileItem item : filteredFileData) {
            combinedSizeBytes += item.getFileRef().length();
        }

        double combinedSizeKB = combinedSizeBytes / 1024.0;
        footerStatsLabel.setText(String.format("Total Files Visible: %d  |  Combined Size: %.2f KB", totalFiles, combinedSizeKB));
    }

    // Reads selected text files to render inside Preview Area
    private void previewFileContent(File file) {
        previewTextArea.clear();

        // Safety guard for file sizes to prevent memory overflow crashes on large files
        if (file.length() > 500 * 1024) {
            previewTextArea.setText("File is too large to preview (> 500 KB).");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            int linesRead = 0;
            // Preview maximum first 150 lines for UI speed consistency
            while ((line = br.readLine()) != null && linesRead < 150) {
                content.append(line).append("\n");
                linesRead++;
            }
            if (br.readLine() != null) {
                content.append("\n[...Content truncated for preview length limit...]");
            }
            previewTextArea.setText(content.toString());
        } catch (IOException e) {
            previewTextArea.setText("Error reading file or file format is incompatible.");
        }
    }

    // Persistent States: Storing configs
    private void saveSettings(String directoryPath, String searchQuery) {
        Properties props = new Properties();
        props.setProperty("lastDirectory", directoryPath);
        props.setProperty("lastSearch", searchQuery);

        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            props.store(output, "File Manager Persistence Settings");
        } catch (IOException io) {
            System.err.println("Failed to write persistence configuration file framework settings.");
        }
    }

    // Persistent States: Loading configs
    private Properties loadSettings() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            props.load(input);
        } catch (IOException ex) {
            // File doesn't exist yet on first boot, handled cleanly
        }
        return props;
    }

    public static void main(String[] args) {
        launch(args);
    }

    // --- INNER CLASS: Table Object Model Definition ---
    public static class FileItem {
        private final SimpleStringProperty name;
        private final SimpleStringProperty size;
        private final SimpleStringProperty date;
        private final File fileRef;

        public FileItem(File file) {
            this.fileRef = file;
            this.name = new SimpleStringProperty(file.getName());
            this.size = new SimpleStringProperty(String.format("%.1f KB", file.length() / 1024.0));
            this.date = new SimpleStringProperty(new Date(file.lastModified()).toString());
        }

        public String getName() { return name.get(); }
        public SimpleStringProperty nameProperty() { return name; }

        public String getSize() { return size.get(); }
        public SimpleStringProperty sizeProperty() { return size; }

        public String getDate() { return date.get(); }
        public SimpleStringProperty dateProperty() { return date; }

        public File getFileRef() { return fileRef; }
    }
}