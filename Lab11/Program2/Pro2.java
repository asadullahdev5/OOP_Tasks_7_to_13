package Program2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Pro2 extends Application {

    private final List<Book> catalog = new ArrayList<>();
    private final ObservableList<Book> favoriteBooks = FXCollections.observableArrayList();
    private static final String FAVORITES_FILE = "favorite_books.dat";

    private Stage primaryStage;
    private Scene mainScene;
    private Scene favoritesScene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Library Catalog System");

        // Load data sample and saved states
        initializeCatalogData();
        loadFavoritesFromFile();

        // Build both primary scenes
        createMainScene();
        createFavoritesScene();

        primaryStage.setScene(mainScene);
        primaryStage.show();

        // Save states securely when the user closes the application
        primaryStage.setOnCloseRequest(e -> saveFavoritesToFile());
    }

    // --- DATA INITIALIZATION ---
    private void initializeCatalogData() {
        catalog.add(new Book("1", "To Kill a Mockingbird", "Harper Lee", "Fiction", "A classic novel exploring justice and race in the American South."));
        catalog.add(new Book("2", "1984", "George Orwell", "Dystopian", "A chilling story about a totalitarian regime, surveillance, and control."));
        catalog.add(new Book("3", "The Great Gatsby", "F. Scott Fitzgerald", "Classic", "A critique of the American Dream and jazz-age high society materialism."));
        catalog.add(new Book("4", "Dune", "Frank Herbert", "Sci-Fi", "An epic interstellar adventure dealing with politics, religion, and ecology."));
        catalog.add(new Book("5", "The Hobbit", "J.R.R. Tolkien", "Fantasy", "The legendary journey of Bilbo Baggins across Middle-earth."));
        catalog.add(new Book("6", "Sapiens", "Yuval Noah Harari", "History", "An exciting exploration of the history and evolution of humankind."));
    }

    // --- SCENE 1: MAIN CATALOG VIEW ---
    private void createMainScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f6fa;");

        // Header Title
        VBox topBox = new VBox(5);
        topBox.setPadding(new Insets(15));
        topBox.setStyle("-fx-background-color: #2c3e50;");
        Label headerLabel = new Label("Digital Library Catalog");
        headerLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        topBox.getChildren().add(headerLabel);
        root.setTop(topBox);

        // Center Grid Layout (TilePane) for Book Catalog Cards
        TilePane catalogTilePane = new TilePane();
        catalogTilePane.setPadding(new Insets(20));
        catalogTilePane.setHgap(15);
        catalogTilePane.setVgap(15);
        catalogTilePane.setPrefColumns(3); // Sets target alignment grid sizing
        catalogTilePane.setAlignment(Pos.TOP_LEFT);

        // Generate dynamic button components representing the book covers
        for (Book book : catalog) {
            Button bookCardBtn = new Button(book.getTitle() + "\n\nBy: " + book.getAuthor());
            bookCardBtn.setWrapText(true);
            bookCardBtn.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            bookCardBtn.setPrefSize(140, 180); // Mimics book dimensions
            bookCardBtn.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dcdde1; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-weight: bold; -fx-cursor: hand;");

            // Hover styling feedback modifications
            bookCardBtn.setOnMouseEntered(e -> bookCardBtn.setStyle("-fx-background-color: #e1b12c; -fx-border-color: #e1b12c; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;"));
            bookCardBtn.setOnMouseExited(e -> bookCardBtn.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dcdde1; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-weight: bold;"));

            // Event action setup to launch Details pop-up
            bookCardBtn.setOnAction(e -> openBookDetailsWindow(book));

            catalogTilePane.getChildren().add(bookCardBtn);
        }

        ScrollPane scrollPane = new ScrollPane(catalogTilePane);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        // Bottom Dashboard Navigation Bar
        HBox bottomBar = new HBox();
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.setStyle("-fx-background-color: #eaeded; -fx-border-color: #dcdde1; -fx-border-width: 1px 0 0 0;");

        Button viewFavoritesBtn = new Button("View Favorites ★");
        viewFavoritesBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 16px;");
        viewFavoritesBtn.setOnAction(e -> {
            createFavoritesScene(); // Refresh items in view dynamically
            primaryStage.setScene(favoritesScene);
        });

        bottomBar.getChildren().add(viewFavoritesBtn);
        root.setBottom(bottomBar);

        mainScene = new Scene(root, 520, 550);
    }

    // --- SCENE 2: FAVORITES LIST VIEW ---
    private void createFavoritesScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f6fa;");

        VBox topBox = new VBox(5);
        topBox.setPadding(new Insets(15));
        topBox.setStyle("-fx-background-color: #27ae60;");
        Label favHeader = new Label("Your Favorite Books");
        favHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        topBox.getChildren().add(favHeader);
        root.setTop(topBox);

        // ListView showing favorite item string records
        ListView<String> favoritesListView = new ListView<>();
        ObservableList<String> formattedDisplayList = FXCollections.observableArrayList();

        for (Book b : favoriteBooks) {
            formattedDisplayList.add(b.getTitle() + "  —  " + b.getAuthor() + " [" + b.getGenre() + "]");
        }

        favoritesListView.setItems(formattedDisplayList);
        favoritesListView.setPlaceholder(new Label("No books marked as favorite yet. Go add some!"));
        root.setCenter(favoritesListView);

        // Return Navigation Control
        HBox bottomBar = new HBox();
        bottomBar.setPadding(new Insets(15));
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setStyle("-fx-background-color: #eaeded;");

        Button backBtn = new Button("← Back to Catalog");
        backBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-font-weight: bold;");
        backBtn.setOnAction(e -> primaryStage.setScene(mainScene));
        bottomBar.getChildren().add(backBtn);
        root.setBottom(bottomBar);

        favoritesScene = new Scene(root, 520, 550);
    }

    // --- WINDOW 2: INTERACTIVE POP-UP BOOK DETAILS ---
    private void openBookDetailsWindow(Book book) {
        Stage detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL); // Modal block prevents clicks on main background window
        detailStage.initOwner(primaryStage);
        detailStage.setTitle("Book Information — " + book.getTitle());

        VBox layout = new VBox(12);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #ffffff;");

        Label titleLabel = new Label(book.getTitle());
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label authorLabel = new Label("Author: " + book.getAuthor());
        authorLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #7f8c8d;");

        Label genreLabel = new Label("Genre: " + book.getGenre());
        genreLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #e1b12c; -fx-font-weight: bold;");

        Label descHeader = new Label("Description:");
        descHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label descLabel = new Label(book.getDescription());
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #34495e;");

        // Action Item: Mark as Favorite Button logic handler
        Button favActionBtn = new Button();
        updateFavoriteButtonState(favActionBtn, book);

        favActionBtn.setOnAction(e -> {
            if (favoriteBooks.contains(book)) {
                favoriteBooks.remove(book);
            } else {
                favoriteBooks.add(book);
            }
            updateFavoriteButtonState(favActionBtn, book);
        });

        Button closeBtn = new Button("Close Window");
        closeBtn.setStyle("-fx-background-color: #dcdde1; -fx-text-fill: #2c3e50;");
        closeBtn.setOnAction(e -> detailStage.close());

        HBox btnLayout = new HBox(10);
        btnLayout.setAlignment(Pos.CENTER_RIGHT);
        btnLayout.setPadding(new Insets(10, 0, 0, 0));
        btnLayout.getChildren().addAll(favActionBtn, closeBtn);

        layout.getChildren().addAll(titleLabel, authorLabel, genreLabel, new Separator(), descHeader, descLabel, new Separator(), btnLayout);

        Scene detailScene = new Scene(layout, 380, 360);
        detailStage.setScene(detailScene);
        detailStage.showAndWait();
    }

    private void updateFavoriteButtonState(Button button, Book book) {
        if (favoriteBooks.contains(book)) {
            button.setText("★ Unmark Favorite");
            button.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            button.setText("☆ Mark as Favorite");
            button.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");
        }
    }

    // --- PERSISTENCE: FILE I/O MANAGEMENT ---
    private void saveFavoritesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FAVORITES_FILE))) {
            // Convert ObservableList into standard serializable ArrayList wrapper collection
            ArrayList<Book> serializableList = new ArrayList<>(favoriteBooks);
            oos.writeObject(serializableList);
        } catch (IOException e) {
            System.err.println("Error saving user favorite records file structure context.");
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFavoritesFromFile() {
        File file = new File(FAVORITES_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ArrayList<Book> loadedList = (ArrayList<Book>) ois.readObject();

            // Map saved files back matching exact existing catalog data references
            for (Book loadedBook : loadedList) {
                for (Book catalogBook : catalog) {
                    if (catalogBook.getId().equals(loadedBook.getId())) {
                        favoriteBooks.add(catalogBook);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Configuration favorite cache file unreadable or incomplete. Resetting list values.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// --- 2. THE OBJECT SERIALIZABLE DATA MODEL ---
class Book implements Serializable {
    private static final long serialVersionUID = 1L; // Ensures serialization safe key binding

    private final String id;
    private final String title;
    private final String author;
    private final String genre;
    private final String description;

    public Book(String id, String title, String author, String genre, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.description = description;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public String getDescription() { return description; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}