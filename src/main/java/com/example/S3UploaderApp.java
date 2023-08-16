package com.example;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

public class S3UploaderApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("S3 File Uploader");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new javafx.geometry.Insets(20, 20, 20, 20));

        Label accessKeyLabel = new Label("Access Key:");
        TextField accessKeyTextField = new TextField();

        Label secretKeyLabel = new Label("Secret Key:");
        PasswordField secretKeyPasswordField = new PasswordField();

        Label bucketNameLabel = new Label("Bucket Name:");
        TextField bucketNameTextField = new TextField();

        Label keyNameLabel = new Label("Key Name:");
        TextField keyNameTextField = new TextField();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        Button sendButton = new Button("Selecionar e Enviar");
        sendButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File arquivo = fileChooser.showOpenDialog(primaryStage);
                        if (arquivo != null) {
                            sendToS3(accessKeyTextField.getText(), secretKeyPasswordField.getText(),
                                    bucketNameTextField.getText(), keyNameTextField.getText(), arquivo);
                        }
                    }
                });

        Button cancelButton = new Button("Cancelar");
        cancelButton.setOnAction(e -> primaryStage.close());

        gridPane.add(accessKeyLabel, 0, 0);
        gridPane.add(accessKeyTextField, 1, 0);
        gridPane.add(secretKeyLabel, 0, 1);
        gridPane.add(secretKeyPasswordField, 1, 1);
        gridPane.add(bucketNameLabel, 0, 2);
        gridPane.add(bucketNameTextField, 1, 2);
        gridPane.add(keyNameLabel, 0, 3);
        gridPane.add(keyNameTextField, 1, 3);
        gridPane.add(sendButton, 0, 5);
        gridPane.add(cancelButton, 1, 5);

        Scene scene = new Scene(gridPane, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void sendToS3(String accessKey, String secretKey, String bucketName, String keyName, File arquivo) {

        Region region = Region.US_WEST_2;
        S3Client s3 = S3Client.builder()
                .region(region)
                .build();

        putS3Object(s3, bucketName, keyName, arquivo);

    }

    public static void putS3Object(S3Client s3, String bucketName, String objectKey, File arquivo) {
        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("x-amz-meta-myVal", "test");
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .metadata(metadata)
                    .build();

            s3.putObject(putOb, RequestBody.fromFile(arquivo));
            System.out.println("Successfully placed " + objectKey + " into bucket " + bucketName);

        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

}
