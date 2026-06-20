import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FeedbackSystemApp extends Application {

    // Form Inputs
    private TextField nameField;
    private TextField emailField;
    private ComboBox<Integer> ratingComboBox;
    private TextArea commentsArea;
    private Label errorLabel;

    // Table view & Filtering
    private TableView<FeedbackRecord> table;
    private final ObservableList<FeedbackRecord> masterFeedbackList = FXCollections.observableArrayList();
    private FilteredList<FeedbackRecord> filteredFeedbackList;
    private ComboBox<String> filterRatingComboBox;

    // Analytics Summary Labels
    private Label totalSubmissionsLabel;
    private Label averageRatingLabel;
    private Label keywordSummaryLabel;
    private BarChart<String, Number> ratingBarChart;

    private static final String FILE_NAME = "feedback_records.txt";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Event Participant Feedback System");

        // --- LEFT PANEL: FEEDBACK SUBMISSION FORM ---
        VBox formContainer = new VBox(15);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle("-fx-background-color: #ffffff;");
        formContainer.setPrefWidth(340);

        Label formTitle = new Label("Submit Your Feedback");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);

        Label nameLabel = new Label("Full Name:");
        nameField = new TextField();
        nameField.setPromptText("Enter your name");

        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setPromptText("name@example.com");

        Label ratingLabel = new Label("Rating (1-5):");
        ratingComboBox = new ComboBox<>(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        ratingComboBox.setPromptText("Select");
        ratingComboBox.setMaxWidth(Double.MAX_VALUE);

        Label commentsLabel = new Label("Comments:");
        commentsArea = new TextArea();
        commentsArea.setPromptText("Share your detailed thoughts here...");
        commentsArea.setPrefRowCount(4);
        commentsArea.setWrapText(true);

        grid.add(nameLabel, 0, 0);       grid.add(nameField, 1, 0);
        grid.add(emailLabel, 0, 1);      grid.add(emailField, 1, 1);
        grid.add(ratingLabel, 0, 2);     grid.add(ratingComboBox, 1, 2);
        grid.add(commentsLabel, 0, 3);   grid.add(commentsArea, 1, 3);

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 12px;");
        errorLabel.setWrapText(true);

        Button submitBtn = new Button("Submit Feedback");
        submitBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-cursor: hand;");
        submitBtn.setOnAction(e -> handleFeedbackSubmission());

        formContainer.getChildren().addAll(formTitle, new Separator(), grid, errorLabel, submitBtn);

        // --- RIGHT PANEL: TAB PANE SYSTEM (TABLE & DATA ANALYSIS) ---
        TabPane tabPane = new TabPane();

        // TAB 1: Structured Records Table
        Tab tableTab = new Tab("Feedback Records Data Table");
        tableTab.setClosable(false);

        VBox tableTabContainer = new VBox(10);
        tableTabContainer.setPadding(new Insets(15));

        // Interactive Rating Filtering Ribbon Layout
        HBox filterBar = new HBox(10);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        Label filterLabel = new Label("Filter by Rating:");
        filterLabel.setStyle("-fx-font-weight: bold;");
        filterRatingComboBox = new ComboBox<>(FXCollections.observableArrayList("All Ratings", "1 Stars", "2 Stars", "3 Stars", "4 Stars", "5 Stars"));
        filterRatingComboBox.setValue("All Ratings");
        filterRatingComboBox.setOnAction(e -> applyRatingSelectionFilter());
        filterBar.getChildren().addAll(filterLabel, filterRatingComboBox);

        setupFeedbackTableView();
        tableTabContainer.getChildren().addAll(filterBar, table);
        tableTab.setContent(tableTabContainer);

        // TAB 2: Dynamic Charts & Analytical Statistics Overviews
        Tab analyticsTab = new Tab("Live Analytics Overviews");
        analyticsTab.setClosable(false);

        setupAnalyticsDashboardView();
        analyticsTab.setContent(setupAnalyticsDashboardView());

        tabPane.getTabs().addAll(tableTab, analyticsTab);

        // --- ROOT SPLIT CONTAINER LAYOUT ---
        SplitPane rootSplitPane = new SplitPane(formContainer, tabPane);
        rootSplitPane.setDividerPositions(0.36); // Allocates roughly 36% space to form panel inputs

        // Load background configurations from text databases immediately at launch
        readFeedbackRecordsFromFile();
        recalculateAnalyticsMetricsAndCharts();

        Scene scene = new Scene(rootSplitPane, 1020, 580);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- SUB-INITIALIZATION WINDOW BUILDERS ---

    private void setupFeedbackTableView() {
        table = new TableView<>();

        TableColumn<FeedbackRecord, String> nameCol = new TableColumn<>("Participant Name");
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        nameCol.setPrefWidth(140);

        TableColumn<FeedbackRecord, String> emailCol = new TableColumn<>("Email Address");
        emailCol.setCellValueFactory(c -> c.getValue().emailProperty());
        emailCol.setPrefWidth(160);

        TableColumn<FeedbackRecord, Integer> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(c -> c.getValue().ratingProperty().asObject());
        ratingCol.setPrefWidth(65);

        TableColumn<FeedbackRecord, String> commentCol = new TableColumn<>("Detailed Comments Summary");
        commentCol.setCellValueFactory(c -> c.getValue().commentProperty());
        commentCol.setPrefWidth(260);

        table.getColumns().addAll(nameCol, emailCol, ratingCol, commentCol);

        filteredFeedbackList = new FilteredList<>(masterFeedbackList, p -> true);
        table.setItems(filteredFeedbackList);
        table.setPlaceholder(new Label("No feedback logs match the filter criteria."));
    }

    private ScrollPane setupAnalyticsDashboardView() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: #f8f9fa;");

        // Top Statistics Info Summary Row cards
        HBox cardRow = new HBox(20);
        cardRow.setAlignment(Pos.CENTER);

        VBox totalCard = createMetricCard("Total Responses", totalSubmissionsLabel = new Label("0"), "#3498db");
        VBox scoreCard = createMetricCard("Average Event Rating", averageRatingLabel = new Label("0.0"), "#e67e22");
        cardRow.getChildren().addAll(totalCard, scoreCard);

        // Rating BarChart distributions configuration structures
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Rating Score (Stars)");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Submission Counter");
        ratingBarChart = new BarChart<>(xAxis, yAxis);
        ratingBarChart.setTitle("Star Allocation Quantities Distribution Graph");
        ratingBarChart.setLegendVisible(false);
        ratingBarChart.setPrefHeight(250);

        // Feedback Text Miner Summary Box Block
        VBox analysisCard = new VBox(8);
        analysisCard.setPadding(new Insets(12));
        analysisCard.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-radius: 6px; -fx-background-radius: 6px;");
        Label keywordTitle = new Label("Common Comments Tracker & Sentiment Markers");
        keywordTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        keywordSummaryLabel = new Label("Analyzing context variables text matrices...");
        keywordSummaryLabel.setWrapText(true);
        analysisCard.getChildren().addAll(keywordTitle, new Separator(), keywordSummaryLabel);

        container.getChildren().addAll(cardRow, ratingBarChart, analysisCard);

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private VBox createMetricCard(String headline, Label valLabel, String colorHex) {
        VBox block = new VBox(5);
        block.setAlignment(Pos.CENTER);
        block.setPadding(new Insets(12, 30, 12, 30));
        block.setStyle("-fx-background-color: white; -fx-border-color: " + colorHex + "; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        block.setPrefWidth(240);

        Label headlineLabel = new Label(headline);
        headlineLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px; -fx-font-weight: bold;");
        valLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + colorHex + ";");

        block.getChildren().addAll(headlineLabel, valLabel);
        return block;
    }

    // --- FORM ACTION AND CONTROLLER SUBMISSION LOGIC ---
    private void handleFeedbackSubmission() {
        errorLabel.setText("");

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        Integer rating = ratingComboBox.getValue();
        String comment = commentsArea.getText().trim();

        // 1. Missing Input Verification
        if (name.isEmpty() || email.isEmpty() || rating == null || comment.isEmpty()) {
            errorLabel.setText("Incomplete Data: Please complete all data fields to proceed.");
            return;
        }

        // 2. Email Structural Syntax Check
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Pattern.matches(emailRegex, email)) {
            errorLabel.setText("Invalid Format: Please input a valid email formatting route code address.");
            return;
        }

        // Clean values to prevent file structure breakage from injected comma rows
        String cleanName = name.replace(",", " ");
        String cleanComment = comment.replace(",", " ").replace("\n", " ");

        // Append line formatted row straight to the flat data storage file structure
        String logLineEntry = String.format("%s,%s,%d,%s", cleanName, email, rating, cleanComment);
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_NAME, true)))) {
            out.println(logLineEntry);
        } catch (IOException e) {
            errorLabel.setText("Critical IO Error: Failed to commit record log directly onto physical file tree storage.");
            return;
        }

        // Update active program tables memory collections instantly
        masterFeedbackList.add(new FeedbackRecord(cleanName, email, rating, cleanComment));

        // Recompute dashboards data, graph elements and clear forms back to empty inputs
        recalculateAnalyticsMetricsAndCharts();
        nameField.clear();
        emailField.clear();
        ratingComboBox.setValue(null);
        commentsArea.clear();
    }

    // --- FILTER EXECUTION ACTION ---
    private void applyRatingSelectionFilter() {
        String selectedOption = filterRatingComboBox.getValue();
        filteredFeedbackList.setPredicate(record -> {
            if (selectedOption == null || selectedOption.equals("All Ratings")) {
                return true;
            }
            // Parse rating integer value digit character directly from option string text (e.g. "4 Stars" -> 4)
            int scoreCriteria = Integer.parseInt(selectedOption.split(" ")[0]);
            return record.getRating() == scoreCriteria;
        });
    }

    // --- METRICS COMPUTATION AND CHART ENGINE DATA FEEDS ---
    private void recalculateAnalyticsMetricsAndCharts() {
        int total = masterFeedbackList.size();
        totalSubmissionsLabel.setText(String.valueOf(total));

        if (total == 0) {
            averageRatingLabel.setText("0.0");
            keywordSummaryLabel.setText("Awaiting initial submissions streams data records log profiles before generating trackers text trends.");
            ratingBarChart.getData().clear();
            return;
        }

        double totalScoreSum = 0;
        int[] starDistributionCounters = new int[6]; // Indices 1-5 track star ratings count
        Map<String, Integer> tokenFrequencyMap = new HashMap<>();

        // Analyze feedback records
        for (FeedbackRecord r : masterFeedbackList) {
            totalScoreSum += r.getRating();
            if (r.getRating() >= 1 && r.getRating() <= 5) {
                starDistributionCounters[r.getRating()]++;
            }

            // Clean text to parse keywords for comment aggregation analysis
            String[] words = r.getComment().toLowerCase().replaceAll("[^a-zA-Z ]", "").split("\\s+");
            for (String w : words) {
                if (w.length() > 4) { // Filter out simple common short filler words (the, and, a, etc.)
                    tokenFrequencyMap.put(w, tokenFrequencyMap.getOrDefault(w, 0) + 1);
                }
            }
        }

        // Render calculated average results back to display card labels
        averageRatingLabel.setText(String.format("%.2f", totalScoreSum / total));

        // Rebuild and refresh BarChart structural metrics values distribution
        ratingBarChart.getData().clear();
        XYChart.Series<String, Number> distributionSeries = new XYChart.Series<>();
        for (int i = 1; i <= 5; i++) {
            distributionSeries.getData().add(new XYChart.Data<>(i + " Star", starDistributionCounters[i]));
        }
        ratingBarChart.getData().add(distributionSeries);

        // Build Keyword Suggestion String Metrics from the frequency mapping variables
        StringBuilder commonKeywordsSummary = new StringBuilder("Top recurring contextual markers detected: ");
        tokenFrequencyMap.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())) // Sort by highest count descending
                .limit(4) // Cap summary at the top 4 most frequent keywords
                .forEach(entry -> commonKeywordsSummary.append("'").append(entry.getKey()).append("' (x").append(entry.getValue()).append("), "));

        if (tokenFrequencyMap.isEmpty()) {
            keywordSummaryLabel.setText("Insufficient rich descriptive metrics collected to draw word summary patterns yet.");
        } else {
            String outputStr = commonKeywordsSummary.toString();
            keywordSummaryLabel.setText(outputStr.substring(0, outputStr.length() - 2)); // Trim trailing punctuation cleanly
        }
    }

    // --- DATA FILE READER SYSTEM ---
    private void readFeedbackRecordsFromFile() {
        masterFeedbackList.clear();
        File logFile = new File(FILE_NAME);

        if (!logFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String rowLine;
            while ((rowLine = reader.readLine()) != null) {
                if (rowLine.trim().isEmpty()) continue;

                String[] columns = rowLine.split(",");
                if (columns.length >= 4) {
                    try {
                        String name = columns[0].trim();
                        String email = columns[1].trim();
                        int rating = Integer.parseInt(columns[2].trim());

                        // Reconstruct any trailing comments that might have included nested text formatting
                        StringBuilder commentBuilder = new StringBuilder();
                        for (int i = 3; i < columns.length; i++) {
                            commentBuilder.append(columns[i]);
                            if (i < columns.length - 1) commentBuilder.append(",");
                        }

                        masterFeedbackList.add(new FeedbackRecord(name, email, rating, commentBuilder.toString().trim()));
                    } catch (NumberFormatException nfe) {
                        System.err.println("Skipping structural corruption line trace parsing error due to non-numeric rating score formatting.");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Critical Error loading local feedback data records.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // --- 2. INNER CLASS DATA ARCHITECTURE STRUCTURAL MODEL ---
    public static class FeedbackRecord {
        private final SimpleStringProperty name;
        private final SimpleStringProperty email;
        private final SimpleIntegerProperty rating;
        private final SimpleStringProperty comment;

        public FeedbackRecord(String name, String email, int rating, String comment) {
            this.name = new SimpleStringProperty(name);
            this.email = new SimpleStringProperty(email);
            this.rating = new SimpleIntegerProperty(rating);
            this.comment = new SimpleStringProperty(comment);
        }

        public String getName() { return name.get(); }
        public SimpleStringProperty nameProperty() { return name; }

        public String getEmail() { return email.get(); }
        public SimpleStringProperty emailProperty() { return email; }

        public int getRating() { return rating.get(); }
        public SimpleIntegerProperty ratingProperty() { return rating; }

        public String getComment() { return comment.get(); }
        public SimpleStringProperty commentProperty() { return comment; }
    }
}