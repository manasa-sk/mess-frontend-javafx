## STEP-WISE INSTALL AND SETUP
### 1. Install JDK, JavaFX, Eclipse, MySQL
### 2. Install Plugins and Connectors
Install JavaFX plug-in from help -> eclipse marketplace  
Download MySQL Connector J (zip file)

### 3. Add Libraries
Right-click on Project folder, select Build Path -> Configure Build Path  
In Libraries:  
A. Modulepath: Add JavaFX library (add all jar files in it), Select JDK  
B. Classpath: Add mysql j connector jar file  

### 4. Add VM Arguments
Right-click on MainApp.java, select Run As -> Run Configs  
Create new run config for MainApp class  
Under Arguments tab, paste this in VM arguments:  
```
--module-path "C:\Program Files\javafx-sdk-22\lib" --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web
```  
Replace with your path to javaFX lib

### 5. Run
Right-click on MainApp.java, select Run As -> 1 java application  
