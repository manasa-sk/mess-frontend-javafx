import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ManagerDashboard {
	
	private User user;
	
	public ManagerDashboard(User user) {
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
        Button requestButton = new Button("Request");
        Button leftoverButton = new Button("Leftovers");

     // Add buttons to the top buttons VBox
        topButtonsBox.getChildren().addAll(dashboardButton,requestButton, leftoverButton);

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
        Label dashboardHeading = new Label("Manager Dashboard");
        dashboardHeading.setStyle("-fx-font-weight: bold;");

        // Add space below the heading
        VBox.setMargin(dashboardHeading, new Insets(0, 0, 10, 0));

    	
        // Mess information        
        HBox managerInfoBox = new HBox();
        managerInfoBox.setSpacing(10);
        
        Label messLabel = new Label("Mess:");
        Label messValueLabel = new Label(JDBCUtils.messName.get(user.messId));
        messValueLabel.setStyle("-fx-font-weight: bold;");
        
        managerInfoBox.getChildren().addAll(messLabel, messValueLabel);
        
        Label userLabel = new Label("Manager:");
        Label userValueLabel = new Label(user.name);
        userValueLabel.setStyle("-fx-font-weight: bold;");
        
        managerInfoBox.getChildren().addAll(userLabel, userValueLabel);
        
        Label leftoverLabel = new Label("Today's Leftover:");
        Label leftoverValueLabel = new Label(Integer.toString(JDBCUtils.getTotalLeftover())+" PLATES");
        leftoverValueLabel.setStyle("-fx-font-weight: bold;");
        
        managerInfoBox.getChildren().addAll(leftoverLabel, leftoverValueLabel);
        
        managerInfoBox.setAlignment(Pos.CENTER_LEFT);

        // Add space below the mess information
        VBox.setMargin(managerInfoBox, new Insets(0, 0, 20, 0));

        // Create TableView for the meal booking
        TableView<ManagerFPReq> FPReqTable = new TableView<>();

        // Create TableColumn for meal
        TableColumn<ManagerFPReq, String> nameColumn = new TableColumn<>("NAME");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<ManagerFPReq, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Create TableColumn for time
        TableColumn<ManagerFPReq, String> mealColumn = new TableColumn<>("MEAL");
        mealColumn.setCellValueFactory(new PropertyValueFactory<>("item"));

        // Create TableColumn for opt
        TableColumn<ManagerFPReq, ManagerFPReq.ReqStatus> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add columns to the table
        FPReqTable.getColumns().addAll(nameColumn, idColumn, mealColumn, statusColumn);        

        // Add data to the table
        FPReqTable.getItems().addAll(
                JDBCUtils.getFoodRequests(user.messId, 1)
        );
        
     // Create TableView for the meal booking
        TableView<ManagerFPReq> addOnTable = new TableView<>();

     // Create TableColumn for meal
        TableColumn<ManagerFPReq, String> name2Column = new TableColumn<>("NAME");
        name2Column.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<ManagerFPReq, Integer> id2Column = new TableColumn<>("ID");
        id2Column.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Create TableColumn for time
        TableColumn<ManagerFPReq, String> itemColumn = new TableColumn<>("ITEM");
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));

        // Create TableColumn for opt
        TableColumn<ManagerFPReq, ManagerFPReq.ReqStatus> status2Column = new TableColumn<>("STATUS");
        status2Column.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add columns to the table
        addOnTable.getColumns().addAll(name2Column, id2Column, itemColumn, status2Column);

        // Add data to the table
        addOnTable.getItems().addAll(
        		JDBCUtils.getFoodRequests(user.messId, 2)
        );

        // Set up table pane
        TitledPane table1Pane = new TitledPane("Food Package Requests", FPReqTable);
        TitledPane table2Pane = new TitledPane("Add On Requests", addOnTable);
        
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(table1Pane, table2Pane);
        splitPane.setDividerPositions(0.5);
        
        table1Pane.setAlignment(Pos.TOP_LEFT);
        table2Pane.setAlignment(Pos.TOP_LEFT);
        
        dashboardButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().addAll(dashboardHeading, managerInfoBox, splitPane);
        });
        
     // Create VBox to hold the form elements
        VBox requestList = new VBox();
        requestList.setSpacing(10);
        requestList.setAlignment(Pos.CENTER);

        // Create a heading label for the form
        Label headingLabel = new Label("Food Requests");
        headingLabel.setStyle("-fx-font-weight: bold;");

     // Create TableView for the meal booking
        TableView<ManagerFPReq> requestTable = new TableView<>();

        // Create TableColumn for meal
        TableColumn<ManagerFPReq, String> student3Column = new TableColumn<>("NAME");
        student3Column.setCellValueFactory(new PropertyValueFactory<>("name"));
        
     // Create TableColumn for time
        TableColumn<ManagerFPReq, String> id3Column = new TableColumn<>("ID");
        id3Column.setCellValueFactory(new PropertyValueFactory<>("id"));
        
     // Create TableColumn for time
        TableColumn<ManagerFPReq, String> typeColumn = new TableColumn<>("TYPE");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        
        
        // Create TableColumn for time
        TableColumn<ManagerFPReq, String> item2Column = new TableColumn<>("ITEM");
        item2Column.setCellValueFactory(new PropertyValueFactory<>("item"));

        TableColumn<ManagerFPReq, Button> status3Column = new TableColumn<>("STATUS");
        status3Column.setCellValueFactory(new PropertyValueFactory<>("statusButton"));

        
        // Add columns to the table
        requestTable.getColumns().addAll(student3Column, id3Column, typeColumn, item2Column, status3Column);

        List<ManagerFPReq> allReqs = JDBCUtils.getFoodRequests(user.messId, 1);
        allReqs.addAll(JDBCUtils.getFoodRequests(user.messId, 2));
        // Add data to the table
        requestTable.getItems().addAll(allReqs);

        // Add form elements to the formBox
        requestList.getChildren().addAll(headingLabel, requestTable);

        // Set padding for the formBox
        requestList.setPadding(new Insets(20));


        requestButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().add(requestList);
        });
        
        VBox leftoverForm = new VBox();
        leftoverForm.setSpacing(10);
        leftoverForm.setPadding(new Insets(20));
        
        VBox heading = new VBox();
        heading.setAlignment(Pos.CENTER);
        
     // Create a heading label for the form
        Label heading2Label = new Label("Leftovers");
        heading2Label.setStyle("-fx-font-weight: bold;");
        
        heading.getChildren().add(heading2Label);
        
        VBox.setMargin(heading, new Insets(0, 0, 10, 0));
        
              
        Label radioLabel = new Label("Meal");
        
        ToggleGroup toggleGroup = new ToggleGroup();
        
        RadioButton breakfastRadio = new RadioButton("BREAKFAST");
        breakfastRadio.setToggleGroup(toggleGroup);
        RadioButton lunchRadio = new RadioButton("LUNCH");
        lunchRadio.setToggleGroup(toggleGroup);
        RadioButton dinnerRadio = new RadioButton("DINNER");
        dinnerRadio.setToggleGroup(toggleGroup);
        
        Label mealsLabel = new Label("Leftover Meals");
        
        TextField numericalMeals = new TextField();
        numericalMeals.setMaxWidth(100);        
        
        // Add an event handler to allow only numerical input
        numericalMeals.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numericalMeals.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        VBox.setMargin(numericalMeals, new Insets(0, 0, 5, 0));
        
        VBox button = new VBox();
        button.setAlignment(Pos.CENTER);
        
        Label error3Label = new Label("Error: Leftovers not updated");
        error3Label.setTextFill(Color.RED);
        error3Label.setVisible(false);
        
     // Submit button
        Button submitButton = new Button("UPDATE");
        submitButton.setOnAction(event -> {
        	if(toggleGroup.getSelectedToggle()!=null) {
        		String time = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
        		if(JDBCUtils.createLeftovers(time, Integer.parseInt(numericalMeals.getText()), user.messId)) {
            		error3Label.setText(time+" Leftovers Updated");
            		error3Label.setTextFill(Color.GREEN);
            	}
        		error3Label.setVisible(true);
        	}
        	
        });
        
        button.getChildren().add(submitButton);
        
        
     // Add form elements to the formBox
        leftoverForm.getChildren().addAll(heading, radioLabel, breakfastRadio, lunchRadio, dinnerRadio, mealsLabel, numericalMeals, error3Label, button);

        // Set padding for the formBox
        leftoverForm.setPadding(new Insets(20));


        leftoverButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().add(leftoverForm);
        });
        
        
        // Create and return the scene
        Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());

        return scene;
    }
}