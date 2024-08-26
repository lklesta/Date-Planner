package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class datePlanner extends Application {

    Stage window;
    Scene mainScene, reservationScene;
    ToggleGroup dateGroup;
    RadioButton dinnerOreganos, dinnerSauce, activityMiniGolf, activityMuseum, rsvpOption;
    TextArea otherOpinions;
    TextField timeTextField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        // Main Page
        Label mainLabel = new Label("Would you like to go on a date with (Persons Name)?"); // User will enter their name
        Button yesButton = new Button("Yes!");
        Button noButton = new Button("No :(");

        // Basic GUI Styling
        yesButton.setStyle("-fx-background-color: green;");
        noButton.setStyle("-fx-background-color: red;");

        VBox mainLayout = new VBox(20);
        mainLayout.getChildren().addAll(mainLabel, yesButton, noButton);
        mainLayout.setAlignment(Pos.CENTER);

        mainScene = new Scene(mainLayout, 400, 200);

        // Reservation Page
        // Day of week picker
        Label pickDateLabel = new Label("Pick Date:");
        dateGroup = new ToggleGroup();
        VBox dateButtons = new VBox(5);
        String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String day : daysOfWeek) {
            RadioButton radioButton = new RadioButton(day);
            radioButton.setToggleGroup(dateGroup);
            dateButtons.getChildren().add(radioButton);
        }
        
        // Best time input
        Label enterTimeLabel = new Label("Enter best time available:");
        timeTextField = new TextField();

        Label placesLabel = new Label("Places to go (pick one row or enter options in other):");
        ToggleGroup placeGroup = new ToggleGroup();
        
        // Choose Dinner or activity
        VBox placesButtons = new VBox(10);
        RadioButton dinnerButton = new RadioButton("Dinner");
        RadioButton activityButton = new RadioButton("Activity");
        dinnerButton.setToggleGroup(placeGroup);
        activityButton.setToggleGroup(placeGroup);

        // Dinner options (restaurants used as examples)
        HBox dinnerOptions = new HBox(10);
        dinnerOreganos = new RadioButton("Oreganos");
        dinnerSauce = new RadioButton("Sauce");
        dinnerOreganos.setToggleGroup(placeGroup);
        dinnerSauce.setToggleGroup(placeGroup);
        dinnerOptions.getChildren().addAll(new Label("Dinner Options:"), dinnerOreganos, dinnerSauce);

        // Activity options (activities used as examples)
        HBox activityOptions = new HBox(10);
        activityMiniGolf = new RadioButton("MiniGolf");
        activityMuseum = new RadioButton("Museum/Aquarium");
        activityMiniGolf.setToggleGroup(placeGroup);
        activityMuseum.setToggleGroup(placeGroup);
        activityOptions.getChildren().addAll(new Label("Activity Options:"), activityMiniGolf, activityMuseum);

        // Organize Dinner and activity selections
        VBox dinnerBox = new VBox(5);
        dinnerBox.getChildren().addAll(dinnerOptions);

        VBox activityBox = new VBox(5);
        activityBox.getChildren().addAll(activityOptions);

        // Any additional options can be entered in a text field
        otherOpinions = new TextArea();
        otherOpinions.setPromptText("Enter other opinions here");

        VBox opinionsBox = new VBox(5);
        opinionsBox.getChildren().addAll(new Label("Other:"), otherOpinions);

        // Organizes all elements of placing a reservation
        VBox reservationLayout = new VBox(10);
        reservationLayout.getChildren().addAll(pickDateLabel, dateButtons, enterTimeLabel, timeTextField, placesLabel, dinnerBox, activityBox, opinionsBox);
        reservationLayout.setPadding(new Insets(10));
        reservationScene = new Scene(reservationLayout, 700, 500);

        // RSVP Option
        rsvpOption = new RadioButton("RSVP another time");
        rsvpOption.setToggleGroup(placeGroup);
        placesButtons.getChildren().add(rsvpOption);

        // Button actions
        yesButton.setOnAction(e -> window.setScene(reservationScene));

        noButton.setOnAction(e -> {Platform.exit();});
        
        // Save button to save information to txt file
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: blue;");
        saveButton.setOnAction(e -> saveReservation());

        VBox bottomLayout = new VBox(10);
        bottomLayout.setAlignment(Pos.CENTER);
        bottomLayout.getChildren().addAll(new Label("If no dates work select RSVP another time (text 'Persons name')"),
                placesButtons, saveButton);

        reservationLayout.getChildren().add(bottomLayout);

        window.setScene(mainScene);
        window.setTitle("Date Planner");
        window.show();
    }

    // Main function to save the information to a text file
    private void saveReservation() {
        try {
            String folderName = "data";
            File folder = new File(folderName);
            if (!folder.exists()) {
                folder.mkdir();
            }

            String fileName = folderName + "/reservation.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            StringBuilder sb = new StringBuilder();
            RadioButton selectedRadioButton = (RadioButton) dateGroup.getSelectedToggle();
            if (selectedRadioButton != null) {
                sb.append("Date: ").append(selectedRadioButton.getText()).append("\n");
            }
            if (dinnerOreganos.isSelected()) {
                sb.append("Dinner: Oreganos\n");
            } else if (dinnerSauce.isSelected()) {
                sb.append("Dinner: Sauce\n");
            }
            if (activityMiniGolf.isSelected()) {
                sb.append("Activity: MiniGolf\n");
            } else if (activityMuseum.isSelected()) {
                sb.append("Activity: Museum/Aquarium\n");
            }
            if (!timeTextField.getText().isEmpty()) {
                sb.append("Time: ").append(timeTextField.getText()).append("\n");
            }
            if (!otherOpinions.getText().isEmpty()) {
                sb.append("Other Opinions: ").append(otherOpinions.getText()).append("\n");
            }
            if (rsvpOption.isSelected()) {
                sb.append("RSVP: Yes\n");
            }

            // Display information
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Reservation Details:");
            alert.setContentText(sb.toString());
            alert.showAndWait();

            // Write data to file
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            
            window.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
