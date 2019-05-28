package com.almasb.battleship;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
    private static String name;
    public static String display(String title, String message){
        Stage confirmBox = new Stage();
        confirmBox.initModality(Modality.APPLICATION_MODAL);
        confirmBox.setTitle(title);
        confirmBox.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        TextField nameInput = new TextField();

        Button confirmButton = new Button("OK");
        confirmButton.setPrefWidth(60);
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(100);

        confirmButton.setOnAction(e -> {
            name = nameInput.getText();
            confirmBox.close();
            if (name.equals("")){
                name = "Unnamed";
            }
        });

        cancelButton.setOnAction(e -> {
            name = "Unnamed";
            confirmBox.close();
        });

        VBox layout = new VBox(10);
        layout.setPrefSize(300, 300);
        layout.setPadding(new Insets(10, 50, 10, 50));
        layout.getChildren().addAll(label, nameInput, confirmButton, cancelButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        confirmBox.setScene(scene);
        confirmBox.showAndWait();

        return name;
    }
}
