import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginSignUpApp extends Application {

    private Stage primaryStage;
    private TextField emailField;
    private PasswordField passwordField;
    private CheckBox rememberMeCheckBox;
    private double defaultWidth = 300;
    private double defaultHeight = 250;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Login");

        Label headingLabel = new Label("Mess Management System");
        headingLabel.setStyle("-fx-font-weight: bold;");

        Label emailLabel = new Label("Email:");
        emailField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        rememberMeCheckBox = new CheckBox("Remember Me");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
        	// Assuming login is successful, switch to the student dashboard
            StudentDashboard studentDashboard = new StudentDashboard();
            primaryStage.setScene(studentDashboard.createScene());
        });

        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(event -> {
            // Switch to the signup page
            showSignUpPage();
        });
        
     // Add Manager and Admin buttons
        Button managerButton = new Button("Manager");
        managerButton.setOnAction(event -> {
        	// Assuming login is successful, switch to the manager dashboard
            ManagerDashboard managerDashboard = new ManagerDashboard();
            primaryStage.setScene(managerDashboard.createScene());
        });

        Button adminButton = new Button("Admin");
        adminButton.setOnAction(event -> {
        	// Assuming login is successful, switch to the student dashboard
            AdminDashboard adminDashboard = new AdminDashboard();
            primaryStage.setScene(adminDashboard.createScene());
        });

        HBox managerAdminBox = new HBox(managerButton, adminButton);
        managerAdminBox.setAlignment(Pos.CENTER_RIGHT);
        managerAdminBox.setSpacing(10);

        GridPane loginLayout = new GridPane();
        loginLayout.setPadding(new Insets(20));
        loginLayout.setHgap(10);
        loginLayout.setVgap(10);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.getColumnConstraints().add(new ColumnConstraints(100));
        loginLayout.getColumnConstraints().add(new ColumnConstraints(200));
        loginLayout.add(headingLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headingLabel, HPos.CENTER);
        loginLayout.add(emailLabel, 0, 1);
        loginLayout.add(emailField, 1, 1);
        loginLayout.add(passwordLabel, 0, 2);
        loginLayout.add(passwordField, 1, 2);
        loginLayout.add(rememberMeCheckBox, 0, 3, 2, 1);
        loginLayout.add(loginButton, 0, 4, 2, 1);
        loginLayout.add(signUpButton, 0, 5, 2, 1);
        loginLayout.add(managerAdminBox, 1, 6, 1, 1); // Add manager and admin buttons to the layout


        // Set window size
        setWindowSize(primaryStage);

        Scene scene = new Scene(loginLayout, defaultWidth, defaultHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showSignUpPage() {
        primaryStage.setTitle("Sign Up");

        Label headingLabel = new Label("Mess Management System");
        headingLabel.setStyle("-fx-font-weight: bold;");

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField signUpEmailField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField signUpPasswordField = new PasswordField();

        Label confirmPasswordLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();

        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(event -> {
            // Add signup logic here
        });

        Button backToLoginButton = new Button("Back to Login");
        backToLoginButton.setOnAction(event -> {
            // Switch back to the login page
            start(primaryStage);
        });

        GridPane signUpLayout = new GridPane();
        signUpLayout.setPadding(new Insets(20));
        signUpLayout.setHgap(10);
        signUpLayout.setVgap(10);
        signUpLayout.setAlignment(Pos.CENTER);
        signUpLayout.getColumnConstraints().add(new ColumnConstraints(100));
        signUpLayout.getColumnConstraints().add(new ColumnConstraints(200));
        signUpLayout.add(headingLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headingLabel, HPos.CENTER);
        signUpLayout.add(nameLabel, 0, 1);
        signUpLayout.add(nameField, 1, 1);
        signUpLayout.add(emailLabel, 0, 2);
        signUpLayout.add(signUpEmailField, 1, 2);
        signUpLayout.add(passwordLabel, 0, 3);
        signUpLayout.add(signUpPasswordField, 1, 3);
        signUpLayout.add(confirmPasswordLabel, 0, 4);
        signUpLayout.add(confirmPasswordField, 1, 4);
        signUpLayout.add(signUpButton, 0, 5, 2, 1);
        signUpLayout.add(backToLoginButton, 0, 6, 2, 1);

        // Set window size
        setWindowSize(primaryStage);

        Scene scene = new Scene(signUpLayout, defaultWidth, defaultHeight);
        primaryStage.setScene(scene);
    }


    private void setWindowSize(Stage stage) {
        if (stage.isMaximized()) {
            // If window is maximized, set default size to screen dimensions
            double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
            double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
            defaultWidth = screenWidth;
            defaultHeight = screenHeight;
        } else {
            // If window is not maximized, set default size to 300 width and 250 height
            defaultWidth = 300;
            defaultHeight = 250;
        }
    }
}