import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginSignUpApp extends Application {

    private Stage primaryStage;
    private TextField emailField;
    private PasswordField passwordField;
    private double defaultWidth = 400;
    private double defaultHeight = 350;

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

        Label errorLabel = new Label("Invalid email id or password");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        
        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
        	String email = emailField.getText();
            String password = passwordField.getText();

            Object[] loginSuccessful = JDBCUtils.authenticateLogin(email, password);

            switch((Integer)loginSuccessful[0]) {
        	case 1:
        		StudentDashboard studentDashboard = new StudentDashboard((User)loginSuccessful[1]);
                primaryStage.setScene(studentDashboard.createScene());
                break;
        	case 2:
        		AdminDashboard adminDashboard = new AdminDashboard((User)loginSuccessful[1]);
                primaryStage.setScene(adminDashboard.createScene());
                break;
        	case 3:
        		ManagerDashboard managerDashboard = new ManagerDashboard((User)loginSuccessful[1]);
                primaryStage.setScene(managerDashboard.createScene());
                break;
            default:
            	errorLabel.setVisible(true);
        	}
            
        });

        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(event -> {
            // Switch to the signup page
            showSignUpPage();
        });

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
        loginLayout.add(loginButton, 0, 3, 2, 1);
        loginLayout.add(signUpButton, 0, 4, 2, 1);
        loginLayout.add(errorLabel, 1, 5, 2, 1);


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
        
        Label hostelLabel = new Label("Hostel:");
        TextField hostelField = new TextField();
        
        Label messLabel = new Label("Mess:");
        
        ToggleGroup messGroup = new ToggleGroup();
        RadioButton mess1RadioButton = new RadioButton("Mess 1");
        mess1RadioButton.setToggleGroup(messGroup);
        RadioButton mess2RadioButton = new RadioButton("Mess 2");
        mess2RadioButton.setToggleGroup(messGroup);

        // Role label
        Label roleLabel = new Label("Role:");

        // Radio buttons
        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton studentRadioButton = new RadioButton("Student");
        studentRadioButton.setToggleGroup(roleGroup);
        RadioButton managerRadioButton = new RadioButton("Manager");
        managerRadioButton.setToggleGroup(roleGroup);
        RadioButton adminRadioButton = new RadioButton("Admin");
        adminRadioButton.setToggleGroup(roleGroup);

        Label errorLabel = new Label("User already exists");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        
        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(event -> {
            // Add signup logic here
        	if(!isValidEmail(signUpEmailField.getText())) {
        		errorLabel.setText("Invalid Email Format");
        		errorLabel.setVisible(true);
        	}
        	else {
        		// Proceed to dashboard
        		if(roleGroup.getSelectedToggle()!=null) {
        			String selectedRole = ((RadioButton) roleGroup.getSelectedToggle()).getText();
        			String name = nameField.getText();
        			String email = signUpEmailField.getText();
        			String password = signUpPasswordField.getText();
        			String hostel = hostelField.getText();
        			String mess = "";
        			if(messGroup.getSelectedToggle()!=null) {
        				mess = ((RadioButton) messGroup.getSelectedToggle()).getText();
        			}
        			
        			boolean accountCreated = false;        			
        			
        			switch(selectedRole) {
        			case "Student":
        				if(hostel.isEmpty() || mess.isEmpty() || name.isEmpty() || email.isEmpty() || 
        						password.isEmpty()) {
        					errorLabel.setText("Fill required information");
        					errorLabel.setVisible(true);
        				}
        				else {
        					accountCreated = JDBCUtils.createUser(name, email, password, mess, hostel, selectedRole);
        				}
                        break;
        			case "Admin":
        				if(name.isEmpty() || email.isEmpty() || password.isEmpty()) {
        					errorLabel.setText("Fill required information");
        					errorLabel.setVisible(true);
        				}
        				else {
        					accountCreated = JDBCUtils.createUser(name, email, password, mess, hostel, selectedRole);
        				}
                        break;
                	case "Manager":
                		if(name.isEmpty() || email.isEmpty() || password.isEmpty() || mess.isEmpty()) {
        					errorLabel.setText("Fill required information");
        					errorLabel.setVisible(true);
        				}
        				else {
        					accountCreated = JDBCUtils.createUser(name, email, password, mess, hostel, selectedRole);
        				}
                        break;
                    default:
                    	errorLabel.setVisible(true);
        			}
        			
        			if(accountCreated) {
        				errorLabel.setText(selectedRole + " account created!");
        				errorLabel.setTextFill(Color.GREEN);
    					errorLabel.setVisible(true);
        			}
        		}
        		else {
        			errorLabel.setText("Fill required information");
					errorLabel.setVisible(true);
        		}
        	}
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
        signUpLayout.add(hostelLabel, 0, 5);
        signUpLayout.add(hostelField, 1, 5);
        
        // Add spacing between Confirm Password field and Role label
        signUpLayout.add(new Region(), 0, 6); // Spacer
        signUpLayout.add(roleLabel, 0, 7);    // Role label

        // Add radio buttons horizontally with spacing
        signUpLayout.add(studentRadioButton, 1, 8);
        signUpLayout.add(adminRadioButton, 1, 9);
        signUpLayout.add(managerRadioButton, 1, 10);
        signUpLayout.add(messLabel, 0, 12);    // Role label

        // Add radio buttons horizontally with spacing
        signUpLayout.add(mess1RadioButton, 0, 13);
        signUpLayout.add(mess2RadioButton, 1, 13);
        signUpLayout.add(new Region(), 0, 14);

        signUpLayout.add(signUpButton, 0, 15, 2, 1);
        signUpLayout.add(errorLabel, 1, 15, 2, 1);
        signUpLayout.add(backToLoginButton, 0, 16, 2, 1);

        // Set window size
        setWindowSize(primaryStage);

        Scene scene = new Scene(signUpLayout, defaultWidth, defaultHeight);
        primaryStage.setScene(scene);
    }

    public static boolean isValidEmail(String email) {
        // Regular expression for valid email format
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);

        // Create matcher object
        Matcher matcher = pattern.matcher(email);

        // Return true if email matches the pattern, otherwise false
        return matcher.matches();
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
            defaultWidth = 400;
            defaultHeight = 350;
        }
    }
}