import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class StudentDashboard {
	
	private User user;
	
	public StudentDashboard(User user) {
		this.user = user;
	}

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
        
     // Dashboard heading
        Label dashboardHeading = new Label("Student Dashboard");
        dashboardHeading.setStyle("-fx-font-weight: bold;");

        // Add space below the heading
        VBox.setMargin(dashboardHeading, new Insets(0, 0, 10, 0));

    	HBox infoBox = new HBox();
    	infoBox.setSpacing(10);
        
        // Mess information
        Label messLabel = new Label("Mess:");
        Label messValueLabel = new Label(JDBCUtils.messName.get(user.messId));
        messValueLabel.setStyle("-fx-font-weight: bold;");
        
        infoBox.getChildren().addAll(messLabel, messValueLabel);
        
        Label userLabel = new Label("Student:");
        Label userValueLabel = new Label(user.name);
        userValueLabel.setStyle("-fx-font-weight: bold;");
        
        infoBox.getChildren().addAll(userLabel, userValueLabel);

        // Add space below the mess information
        VBox.setMargin(infoBox, new Insets(0, 0, 20, 0));

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
        
        List<MealEntry> mealEntries = new ArrayList<>();
        
        String[] meals = JDBCUtils.loadMeals(user.userId);
        
        for(Meal.MealType meal: Meal.MealType.values()) {
        	switch(meal) {
        	case BREAKFAST:
        		mealEntries.add(new MealEntry("Breakfast", "7:30am - 9:30am", createMealButton(meals[0], Meal.MealType.BREAKFAST)));
        		break;
        	case LUNCH:
        		mealEntries.add(new MealEntry("Lunch", "12pm - 2pm", createMealButton(meals[1], Meal.MealType.LUNCH)));
        		break;
        	case DINNER:
        		mealEntries.add(new MealEntry("Dinner", "7:30pm - 9:30pm", createMealButton(meals[2], Meal.MealType.DINNER)));
        		break;
        	default:
        		break;
        	}
        }

        // Add data to the table
        mealBookingTable.getItems().addAll(mealEntries);
        
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
                JDBCUtils.getStudentLeaves(user.userId)
        );

        // Set up table pane
        TitledPane table1Pane = new TitledPane("Meal Booking", mealBookingTable);
        TitledPane table2Pane = new TitledPane("Leave Application", leaveAppTable);
        
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(table1Pane, table2Pane);
        splitPane.setDividerPositions(0.5);
        
        table1Pane.setAlignment(Pos.TOP_LEFT);
        table2Pane.setAlignment(Pos.TOP_LEFT);
        
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

        StringConverter<LocalDate> converter = new LocalDateStringConverter(DateTimeFormatter.ofPattern("dd/MM/yyyy"), null);
        
        // Left side - Start Date input
        Label startDateLabel = new Label("Start Date:");
        TextField startDateField = new TextField(); // Assuming TextField for input
        startDateField.setPromptText("DD/MM/YYYY"); // Set prompt text
        startDateField.setPrefWidth(100); // Set preferred width
        startDateField.setTextFormatter(new TextFormatter<>(converter)); // Apply formatter
        dateInputBox.getChildren().addAll(startDateLabel, startDateField);
        
     // Right side - End Date input
        Label endDateLabel = new Label("End Date:");
        TextField endDateField = new TextField(); // Assuming TextField for input
        endDateField.setPromptText("DD/MM/YYYY"); // Set prompt text
        endDateField.setPrefWidth(100); // Set preferred width
        endDateField.setTextFormatter(new TextFormatter<>(converter)); // Apply formatter
        dateInputBox.getChildren().addAll(endDateLabel, endDateField);

        // Create a label and text area for comments input
        Label commentsLabel = new Label("Comments:");
        TextArea commentsTextArea = new TextArea();
        commentsTextArea.setPrefWidth(300); // Set preferred width
        commentsTextArea.setPrefHeight(100); // Set preferred height
        
        Label errorLabel = new Label("Leave Applied Successfully!");
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.setVisible(false);

        // Create a submit button
        Button submitButton = new Button("Submit");
        submitButton.setAlignment(Pos.CENTER);
        submitButton.setOnAction(event -> {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        	LocalDate startdate = LocalDate.parse(startDateField.getText(), formatter);
        	LocalDate enddate = LocalDate.parse(endDateField.getText(), formatter);
        	
        	if(!JDBCUtils.createLeaveApp(startdate, enddate, commentsTextArea.getText(), user.userId)) {
        		errorLabel.setText("Error: Application Not Made");
        		errorLabel.setTextFill(Color.RED);
        	}
        	errorLabel.setVisible(true);
        });

        // Add form elements to the formBox
        leaveForm.getChildren().addAll(headingLabel, dateInputBox, commentsLabel, commentsTextArea, errorLabel, submitButton);

        // Set padding for the formBox
        leaveForm.setPadding(new Insets(20));
        
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
        
        Label error2Label = new Label("Feedback Submitted Successfully!");
        error2Label.setTextFill(Color.GREEN);
        error2Label.setAlignment(Pos.CENTER);
        error2Label.setVisible(false);

        // Submit button
        Button submitFBButton = new Button("Submit");
        submitFBButton.setAlignment(Pos.CENTER);
        submitFBButton.setOnAction(event -> {
        	if(!JDBCUtils.createFeedback(feedbackInput.getText(), user.userId)) {
        		error2Label.setText("Error: Feeback Not Submitted");
        		error2Label.setTextFill(Color.RED);
        	}
        	error2Label.setVisible(true);
        });

        // Add nodes to feedback form
        feedbackForm.getChildren().addAll(feedbackHeading, provideFeedbackLabel, feedbackInput, error2Label, submitFBButton);

        
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
        
        List<String> selectedMeals = new ArrayList<>();
        List<String> selectedItems = new ArrayList<>();
        
        EventHandler<ActionEvent> checkBoxHandler = event -> {
            CheckBox checkBox = (CheckBox) event.getSource();
            if (checkBox.isSelected()) {
                selectedMeals.add(checkBox.getText());
            } else {
                selectedMeals.remove(checkBox.getText());
            }
        };
        
        EventHandler<ActionEvent> addOnHandler = event -> {
            CheckBox checkBox = (CheckBox) event.getSource();
            if (checkBox.isSelected()) {
                selectedItems.add(checkBox.getText());
            } else {
                selectedItems.remove(checkBox.getText());
            }
        };

        // Checkboxes for food package options
        CheckBox breakfastCheckBox = new CheckBox("Breakfast");
        CheckBox lunchCheckBox = new CheckBox("Lunch");
        CheckBox dinnerCheckBox = new CheckBox("Dinner");
        
        breakfastCheckBox.setOnAction(checkBoxHandler);
        lunchCheckBox.setOnAction(checkBoxHandler);
        dinnerCheckBox.setOnAction(checkBoxHandler);

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
        
        kadhaiPaneerCheckBox.setOnAction(addOnHandler);
        steamedRiceCheckBox.setOnAction(addOnHandler);
        dalMakhaniCheckBox.setOnAction(addOnHandler);

        // Button for ordering add-ons
        Button orderAddOnsButton = new Button("Order");

        // Add nodes to add-ons pane
        addOnsPane.getChildren().addAll(kadhaiPaneerCheckBox, steamedRiceCheckBox, dalMakhaniCheckBox, orderAddOnsButton);

        // Create SplitPane to hold the left and right panes
        SplitPane splitPaneReq = new SplitPane();
        splitPaneReq.getItems().addAll(foodPackagePane, addOnsPane);
        splitPaneReq.setDividerPositions(0.5);
        
        Label error3Label =  new Label("Error: Food Package(s) not ordered");
        error3Label.setTextFill(Color.RED);
        error3Label.setAlignment(Pos.CENTER);
        error3Label.setVisible(false);
        
        orderFoodPackageButton.setOnAction(event -> {
        	Object[] result = JDBCUtils.requestFood("FOOD PACKAGE", selectedMeals, user.userId);
        	if((Boolean)result[0]) {
        		error3Label.setText("Order(s) {"+((Collection<Integer>)result[1]).stream().map(Object::toString).collect(Collectors.joining(", "))
        				+"} placed succesfully");
        		error3Label.setTextFill(Color.GREEN);
        	}
        	breakfastCheckBox.setSelected(false);
            lunchCheckBox.setSelected(false);
            dinnerCheckBox.setSelected(false);
        	error3Label.setVisible(true);
        });
        
        orderAddOnsButton.setOnAction(event -> {
        	Object[] result = JDBCUtils.requestFood("ADD ON", selectedItems, user.userId);
        	if((Boolean)result[0]) {
        		error3Label.setText("Order(s) {"+((Collection<Integer>) result[1]).stream().map(Object::toString).collect(Collectors.joining(", "))
        				+"} placed succesfully");
        		error3Label.setTextFill(Color.GREEN);
        	}
        	kadhaiPaneerCheckBox.setSelected(false);
            steamedRiceCheckBox.setSelected(false);
            dalMakhaniCheckBox.setSelected(false);
        	error3Label.setVisible(true);
        });

        // Add nodes to request form
        requestForm.getChildren().addAll(requestHeading, splitPaneReq, error3Label);
        
        Node[] obsList = {dashboardHeading, infoBox, splitPane, leaveForm, feedbackForm, requestForm};


        dashboardButton.setOnAction(event -> {
            rightContent.getChildren().removeAll(obsList);
            rightContent.getChildren().addAll(dashboardHeading, infoBox, splitPane);
        });
        
        dashboardButton.fire();
        
        leaveApplicationButton.setOnAction(event -> {
            rightContent.getChildren().removeAll(obsList);
            rightContent.getChildren().add(leaveForm);
        });
        
        feedbackButton.setOnAction(event -> {
            rightContent.getChildren().removeAll(obsList);
            rightContent.getChildren().add(feedbackForm);
        });
        
        requestButton.setOnAction(event -> {
            rightContent.getChildren().removeAll(obsList);
            rightContent.getChildren().add(requestForm);
        });
        
        // Create and return the scene
        Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());

        return scene;
    }
    
    private Button createMealButton(String text, Meal.MealType mealType) {
        Button button = new Button(text);
        if(text.equals("OPT")) {
        	button.setOnAction(event -> {
                // Perform the desired action when the button is clicked
                Meal meal = new Meal(LocalDate.now(), mealType, user);
                if(JDBCUtils.updateMeal(meal)) {
                	button.setText("OPTED");
                }
                else {
                	button.setText("FULL");
                }
                button.setDisable(true);
            });
        }
        else {
        	button.setDisable(true);
        }
        
        return button;
    }
}