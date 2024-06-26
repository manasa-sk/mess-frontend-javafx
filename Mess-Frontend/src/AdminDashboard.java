import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AdminDashboard {
	
	private User user;
	
	public AdminDashboard(User user) {
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

     // Add buttons to the top buttons VBox
        topButtonsBox.getChildren().addAll(dashboardButton, leaveApplicationButton, feedbackButton);

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
        Label dashboardHeading = new Label("Admin Dashboard");
        dashboardHeading.setStyle("-fx-font-weight: bold;");

        // Add space below the mess information
        VBox.setMargin(dashboardHeading, new Insets(0, 0, 10, 0));
        
        HBox infoBox = new HBox();
    	infoBox.setSpacing(10);
        
        Label userLabel = new Label("Admin:");
        Label userValueLabel = new Label(user.name);
        userValueLabel.setStyle("-fx-font-weight: bold;");
        
        infoBox.getChildren().addAll(userLabel, userValueLabel);
        
        VBox.setMargin(infoBox, new Insets(0, 0, 10, 0));

        // Create TableView for the meal booking
        TableView<AdminLeave> adminLeaveTable = new TableView<>();

        // Create TableColumn for meal
        TableColumn<AdminLeave, String> studentColumn = new TableColumn<>("STUDENT");
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("student"));
        
     // Create TableColumn for time
        TableColumn<AdminLeave, String> datesColumn = new TableColumn<>("DATES");
        datesColumn.setCellValueFactory(new PropertyValueFactory<>("dates"));
        
        TableColumn<AdminLeave, Button> approveColumn = new TableColumn<>("APPROVE");
        approveColumn.setCellValueFactory(new PropertyValueFactory<>("approveButton"));

        // Add columns to the table
        adminLeaveTable.getColumns().addAll(studentColumn, datesColumn, approveColumn);

        // Add data to the table
        adminLeaveTable.getItems().addAll(
        		JDBCUtils.getAllLeaves(0)
        );
        
     // Create TableView for the meal booking
        TableView<AdminFB> feedbackTable = new TableView<>();

        // Create TableColumn for meal
        TableColumn<AdminFB, String> student2Column = new TableColumn<>("STUDENT");
        student2Column.setCellValueFactory(new PropertyValueFactory<>("student"));
        
        TableColumn<AdminFB, String> mess2Column = new TableColumn<>("MESS");
        mess2Column.setCellValueFactory(new PropertyValueFactory<>("mess"));

        // Create TableColumn for time
        TableColumn<AdminFB, String> commentColumn = new TableColumn<>("COMMENT");
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        // Add columns to the table
        feedbackTable.getColumns().addAll(student2Column, mess2Column, commentColumn);

        // Add data to the table
        feedbackTable.getItems().addAll(
        		JDBCUtils.getFeedback(0)
        );

        // Set up table pane
        TitledPane table1Pane = new TitledPane("Leave Applications", adminLeaveTable);
        TitledPane table2Pane = new TitledPane("Student Feedback", feedbackTable);
        
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(table1Pane, table2Pane);
        splitPane.setDividerPositions(0.5);
        
        table1Pane.setAlignment(Pos.TOP_LEFT);
        table2Pane.setAlignment(Pos.TOP_LEFT);
        
        dashboardButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().addAll(dashboardHeading, infoBox, splitPane);
        });
        
     // Create VBox to hold the form elements
        VBox leaveApproval = new VBox();
        leaveApproval.setSpacing(10);
        leaveApproval.setAlignment(Pos.CENTER);

        // Create a heading label for the form
        Label headingLabel = new Label("Leave Applications");
        headingLabel.setStyle("-fx-font-weight: bold;");

     // Create TableView for the meal booking
        TableView<AdminLeave> leaveTable = new TableView<>();

        // Create TableColumn for meal
        TableColumn<AdminLeave, String> student3Column = new TableColumn<>("STUDENT");
        student3Column.setCellValueFactory(new PropertyValueFactory<>("student"));
        
     // Create TableColumn for time
        TableColumn<AdminLeave, Integer> id2Column = new TableColumn<>("ID");
        id2Column.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<AdminLeave, String> messColumn = new TableColumn<>("MESS");
        messColumn.setCellValueFactory(new PropertyValueFactory<>("mess"));
        
     // Create TableColumn for time
        TableColumn<AdminLeave, String> dates2Column = new TableColumn<>("DATES");
        dates2Column.setCellValueFactory(new PropertyValueFactory<>("dates"));
        
        
        // Create TableColumn for time
        TableColumn<AdminLeave, String> comment2Column = new TableColumn<>("COMMENT");
        comment2Column.setCellValueFactory(new PropertyValueFactory<>("comment"));

        TableColumn<AdminLeave, Button> approve2Column = new TableColumn<>("APPROVE");
        approve2Column.setCellValueFactory(new PropertyValueFactory<>("approveButton"));

        
        // Add columns to the table
        leaveTable.getColumns().addAll(student3Column, id2Column, messColumn, dates2Column, comment2Column, 
        		approve2Column);

        // Add data to the table
        leaveTable.getItems().addAll(
                JDBCUtils.getAllLeaves(1)
        );

        // Add form elements to the formBox
        leaveApproval.getChildren().addAll(headingLabel, leaveTable);

        // Set padding for the formBox
        leaveApproval.setPadding(new Insets(20));

        leaveApplicationButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().addAll(leaveApproval);
        });
        
     // Create VBox to hold the form elements
        VBox feedback = new VBox();
        feedback.setSpacing(10);
        feedback.setAlignment(Pos.CENTER);

        // Create a heading label for the form
        Label headingLabel2 = new Label("Leave Applications");
        headingLabel2.setStyle("-fx-font-weight: bold;");
        
        // Create TableView for the meal booking
        TableView<AdminFB> feedback2Table = new TableView<>();

        // Create TableColumn for meal
        TableColumn<AdminFB, String> student4Column = new TableColumn<>("STUDENT");
        student4Column.setCellValueFactory(new PropertyValueFactory<>("student"));

        TableColumn<AdminFB, Integer> id4Column = new TableColumn<>("ID");
        id4Column.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<AdminFB, Integer> mess3Column = new TableColumn<>("MESS");
        mess3Column.setCellValueFactory(new PropertyValueFactory<>("mess"));
        
        // Create TableColumn for time
        TableColumn<AdminFB, String> comment3Column = new TableColumn<>("COMMENT");
        comment3Column.setCellValueFactory(new PropertyValueFactory<>("comment"));

        // Add columns to the table
        feedback2Table.getColumns().addAll(student4Column, id4Column, mess3Column, comment3Column);

        // Add data to the table
        feedback2Table.getItems().addAll(
                JDBCUtils.getFeedback(1)
        );

        // Add nodes to feedback form
        feedback.getChildren().addAll(headingLabel2, feedback2Table);


        feedbackButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().add(feedback);
        });
        
        // Create and return the scene
        Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());

        return scene;
    }
}