import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class StudentDashboard {

    @SuppressWarnings("unchecked")
	public Scene createScene() {
    	// Create a VBox for the left column
        VBox leftColumn = new VBox();
        leftColumn.setSpacing(10);
        leftColumn.setPadding(new Insets(20, 0, 20, 0)); // Adding padding at the bottom

        // Create a VBox for the top buttons
        VBox topButtonsBox = new VBox();
        topButtonsBox.setSpacing(10);
        topButtonsBox.setAlignment(Pos.TOP_LEFT); // Aligning top buttons to the top left

        
        // Create clickable options for dashboard, leave application, feedback, and request
        Button dashboardButton = new Button("Dashboard");
        Button leaveApplicationButton = new Button("Leave Application");
        Button feedbackButton = new Button("Feedback");
        Button requestButton = new Button("Request");

     // Add buttons to the top buttons VBox
        topButtonsBox.getChildren().addAll(dashboardButton, leaveApplicationButton, feedbackButton, requestButton);

     // Sign-out button
        Button signOutButton = new Button("Sign Out");
        signOutButton.setOnAction(event -> {
            // Handle sign-out button click
            // Redirect to the login page
            LoginSignUpApp loginSignUpApp = new LoginSignUpApp();
            Stage stage = (Stage) signOutButton.getScene().getWindow();
            loginSignUpApp.start(stage);
        });
        
        // Add the top buttons VBox and sign-out button to the left column VBox
        leftColumn.getChildren().addAll(topButtonsBox, signOutButton);


        // Create a VBox for the right content
        VBox rightContent = new VBox();
        rightContent.setAlignment(Pos.CENTER);

        // Set up layout
        BorderPane root = new BorderPane();
        root.setLeft(leftColumn);
        root.setCenter(rightContent);
        
        
        // Set default selection to Dashboard
        dashboardButton.fire();
        
     // Dashboard heading
        Label dashboardHeading = new Label("Dashboard");
        dashboardHeading.setStyle("-fx-font-weight: bold;");

        // Add space below the heading
        VBox.setMargin(dashboardHeading, new Insets(0, 0, 10, 0));

    	
        // Mess information
        Label messLabel = new Label("Mess:");
        Label messValueLabel = new Label("MESS 1");
        messValueLabel.setStyle("-fx-font-weight: bold;");
        VBox messInfoBox = new VBox(messLabel, messValueLabel);
        messInfoBox.setAlignment(Pos.CENTER_LEFT);

        // Add space below the mess information
        VBox.setMargin(messInfoBox, new Insets(0, 0, 20, 0));

        // Create TableView for the meal booking
        TableView<MealEntry> mealBookingTable = new TableView<>();

        // Create TableColumn for meal
        TableColumn<MealEntry, String> mealColumn = new TableColumn<>("MEAL");
        mealColumn.setCellValueFactory(new PropertyValueFactory<>("meal"));

        // Create TableColumn for time
        TableColumn<MealEntry, String> timeColumn = new TableColumn<>("TIME");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Create TableColumn for opt
        TableColumn<MealEntry, Button> optColumn = new TableColumn<>("OPT");
        optColumn.setCellValueFactory(new PropertyValueFactory<>("optButton"));

        // Add columns to the table
        mealBookingTable.getColumns().addAll(mealColumn, timeColumn, optColumn);

        // Add data to the table
        mealBookingTable.getItems().addAll(
                new MealEntry("Breakfast", "7:30am - 9:30am", new Button("OPTED")),
                new MealEntry("Lunch", "12pm - 2pm", new Button("OPT")),
                new MealEntry("Dinner", "7:30pm - 9:30pm", new Button("OPTED"))
        );
        
     // Create TableView for the meal booking
        TableView<LeaveApplication> leaveAppTable = new TableView<>();

        // Create TableColumn for meal
        TableColumn<LeaveApplication, String> datesColumn = new TableColumn<>("DATES");
        datesColumn.setCellValueFactory(new PropertyValueFactory<>("dates"));

        // Create TableColumn for time
        TableColumn<LeaveApplication, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add columns to the table
        leaveAppTable.getColumns().addAll(datesColumn, statusColumn);

        // Add data to the table
        leaveAppTable.getItems().addAll(
                new LeaveApplication("29th Mar - 3rd Mar", "Approved"),
                new LeaveApplication("4th Apr - 10th Apr", "Pending")
        );

        // Set up table pane
        TitledPane table1Pane = new TitledPane("Meal Booking", mealBookingTable);
        TitledPane table2Pane = new TitledPane("Leave Application", leaveAppTable);
        
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(table1Pane, table2Pane);
        splitPane.setDividerPositions(0.5);
        
        table1Pane.setAlignment(Pos.TOP_LEFT);
        table2Pane.setAlignment(Pos.TOP_LEFT);
        
        dashboardButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().addAll(dashboardHeading, messInfoBox, splitPane);
        });
        
     // Create VBox to hold the form elements
        VBox leaveForm = new VBox();
        leaveForm.setSpacing(10);
        leaveForm.setAlignment(Pos.CENTER);

        // Create a heading label for the form
        Label headingLabel = new Label("Leave Application");
        headingLabel.setStyle("-fx-font-weight: bold;");

        // Create an HBox for the date inputs
        HBox dateInputBox = new HBox();
        dateInputBox.setSpacing(10);

        // Left side - Start Date input
        Label startDateLabel = new Label("Start Date:");
        TextField startDateField = new TextField(); // Assuming TextField for input
        startDateField.setPromptText("DD/MM/YYYY"); // Set prompt text
        startDateField.setPrefWidth(100); // Set preferred width
        startDateField.setTextFormatter(FormatterHelper.createDateFormatter()); // Apply formatter
        dateInputBox.getChildren().addAll(startDateLabel, startDateField);
        
     // Right side - End Date input
        Label endDateLabel = new Label("End Date:");
        TextField endDateField = new TextField(); // Assuming TextField for input
        endDateField.setPromptText("DD/MM/YYYY"); // Set prompt text
        endDateField.setPrefWidth(100); // Set preferred width
        endDateField.setTextFormatter(FormatterHelper.createDateFormatter()); // Apply formatter
        dateInputBox.getChildren().addAll(endDateLabel, endDateField);

        // Create a label and text area for comments input
        Label commentsLabel = new Label("Comments:");
        TextArea commentsTextArea = new TextArea();
        commentsTextArea.setPrefWidth(300); // Set preferred width
        commentsTextArea.setPrefHeight(100); // Set preferred height

        // Create a submit button
        Button submitButton = new Button("Submit");
        submitButton.setAlignment(Pos.CENTER);

        // Add form elements to the formBox
        leaveForm.getChildren().addAll(headingLabel, dateInputBox, commentsLabel, commentsTextArea, submitButton);

        // Set padding for the formBox
        leaveForm.setPadding(new Insets(20));

        leaveApplicationButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().addAll(leaveForm);
        });
        
        VBox feedbackForm = new VBox();
        feedbackForm.setAlignment(Pos.TOP_CENTER);
        feedbackForm.setSpacing(10);
        feedbackForm.setPadding(new Insets(20));

        // Feedback heading
        Label feedbackHeading = new Label("Feedback");
        feedbackHeading.setStyle("-fx-font-weight: bold;");
        feedbackHeading.setAlignment(Pos.CENTER);
        
        // Add space below the heading
        VBox.setMargin(feedbackHeading, new Insets(0, 0, 10, 0));

        // Provide Feedback label
        Label provideFeedbackLabel = new Label("Provide Feedback:");

        // Text box for feedback input
        TextArea feedbackInput = new TextArea();
        feedbackInput.setWrapText(true);
        feedbackInput.setPrefWidth(300); // Adjust width as needed
        feedbackInput.setPrefHeight(100); // Adjust height as needed

        // Submit button
        Button submitFBButton = new Button("Submit");
        submitFBButton.setAlignment(Pos.CENTER);

        // Add nodes to feedback form
        feedbackForm.getChildren().addAll(feedbackHeading, provideFeedbackLabel, feedbackInput, submitFBButton);


        feedbackButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().add(feedbackForm);
        });
        
     // VBox for request form
        VBox requestForm = new VBox();
        requestForm.setAlignment(Pos.TOP_CENTER);
        requestForm.setSpacing(10);
        requestForm.setPadding(new Insets(20));

        // Feedback heading
        Label requestHeading = new Label("Food Requests");
        requestHeading.setStyle("-fx-font-weight: bold;");
        requestHeading.setAlignment(Pos.CENTER);

        // Left pane for food package requests
        VBox foodPackagePane = new VBox();
        foodPackagePane.setSpacing(10);

        // Food package heading
        Label foodPackageHeading = new Label("Food Package");
        foodPackageHeading.setStyle("-fx-font-weight: bold;");
        foodPackagePane.getChildren().add(foodPackageHeading);

        // Checkboxes for food package options
        CheckBox breakfastCheckBox = new CheckBox("Breakfast");
        CheckBox lunchCheckBox = new CheckBox("Lunch");
        CheckBox dinnerCheckBox = new CheckBox("Dinner");

        // Button for ordering food package
        Button orderFoodPackageButton = new Button("Order");

        // Add nodes to food package pane
        foodPackagePane.getChildren().addAll(breakfastCheckBox, lunchCheckBox, dinnerCheckBox, orderFoodPackageButton);

        // Right pane for add-ons requests
        VBox addOnsPane = new VBox();
        addOnsPane.setSpacing(10);

        // Add-ons heading
        Label addOnsHeading = new Label("Add Ons");
        addOnsHeading.setStyle("-fx-font-weight: bold;");
        addOnsPane.getChildren().add(addOnsHeading);

        // Checkboxes for add-ons options
        CheckBox kadhaiPaneerCheckBox = new CheckBox("Kadhai Paneer");
        CheckBox steamedRiceCheckBox = new CheckBox("Steamed Rice");
        CheckBox dalMakhaniCheckBox = new CheckBox("Dal Makhani");

        // Button for ordering add-ons
        Button orderAddOnsButton = new Button("Order");

        // Add nodes to add-ons pane
        addOnsPane.getChildren().addAll(kadhaiPaneerCheckBox, steamedRiceCheckBox, dalMakhaniCheckBox, orderAddOnsButton);

        // Create SplitPane to hold the left and right panes
        SplitPane splitPaneReq = new SplitPane();
        splitPaneReq.getItems().addAll(foodPackagePane, addOnsPane);
        splitPaneReq.setDividerPositions(0.5);

        // Add nodes to request form
        requestForm.getChildren().addAll(requestHeading, splitPaneReq);


        requestButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().add(requestForm);
        });
        
        // Create and return the scene
        Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());

        return scene;
    }
}