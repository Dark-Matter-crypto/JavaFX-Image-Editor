package csc2b.gui;

import csc2b.client.ImageProcessor;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EditorPane extends StackPane {
    private ImageView rawImage;
    private ImageView resultImage;
    private String fileName;
    private ImageProcessor service;
    private File originalImageFile;


    public EditorPane(){
        rawImage = new ImageView();
        resultImage = new ImageView();
        rawImage.setFitHeight(400);
        rawImage.setFitWidth(400);
        rawImage.setPreserveRatio(true);
        resultImage.setFitHeight(400);
        resultImage.setFitWidth(400);
        resultImage.setPreserveRatio(true);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./data/client"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        TextArea messageForm = new TextArea("");
        messageForm.setEditable(false);

        Button selectFile = new Button("Open Image");
        Button connectService = new Button("Connect");
        Button crop = new Button("Crop");
        Button greyScale = new Button("GrayScale");
        Button rotate = new Button("Rotate");
        Button erosion = new Button("Erosion");
        Button dilation = new Button("Dilation");
        Button canny = new Button("Canny");
        Button fast = new Button("Fast");
        Button orb = new Button("ORB");
        Button download = new Button("Save Image");

        selectFile.setPrefWidth(200);
        crop.setPrefWidth(200);
        greyScale.setPrefWidth(200);
        rotate.setPrefWidth(200);
        erosion.setPrefWidth(200);
        dilation.setPrefWidth(200);
        canny.setPrefWidth(200);
        fast.setPrefWidth(200);
        orb.setPrefWidth(200);
        connectService.setPrefWidth(200);
        download.setPrefWidth(200);

        Label processingLabel = new Label("Pre-processing: ");
        Label featureLabel = new Label("Feature extraction:");
        Label downloadLabel = new Label("Download Result:");
        Label originalImageLabel = new Label("Original ");
        Label resultImageLabel = new Label("Result");
        Font font = Font.font("Verdana", FontWeight.BOLD,20);
        originalImageLabel.setFont(font);
        resultImageLabel.setFont(font);

        processingLabel.setPadding(new Insets(20, 0, 20, 0));
        featureLabel.setPadding(new Insets(20, 0, 20, 0));
        downloadLabel.setPadding(new Insets(20, 0, 20, 0));
        originalImageLabel.setPadding(new Insets(50, 0, 50, 0));
        resultImageLabel.setPadding(new Insets(50, 0, 50, 0));


        //Connect to server
        connectService.setOnAction((ActionEvent e) -> {
            Connect(messageForm);
        });

        //Open Image
        selectFile.setOnAction((ActionEvent e) -> {
            File selectedFile = fileChooser.showOpenDialog(null);
            Image originalImage = new Image("file:data/client/" + selectedFile.getName());
            if (originalImage != null) {
                fileName = selectedFile.getName();
                originalImageFile = selectedFile;
                rawImage.setImage(originalImage);
            }
        });

        //Crop image
        crop.setOnAction((ActionEvent e) -> {
            Image result = service.processImage(originalImageFile, "CROP");
            resultImage.setImage(result);
            Connect(messageForm);
        });

        //Grayscale image
        greyScale.setOnAction((ActionEvent e) -> {
            Image result = service.processImage(originalImageFile, "GREYSCALE");
            resultImage.setImage(result);
            Connect(messageForm);
        });

        //Rotate image
        rotate.setOnAction((ActionEvent e) -> {
            Image result = service.processImage(originalImageFile, "ROTATE");
            resultImage.setImage(result);
            Connect(messageForm);
        });

        //Erotion effect
        erosion.setOnAction((ActionEvent e) -> {
            Image result = service.processImage(originalImageFile, "EROSION");
            resultImage.setImage(result);
            Connect(messageForm);
        });

        //Dialation effect
        dilation.setOnAction((ActionEvent e) -> {
            Image result = service.processImage(originalImageFile, "DILATION");
            resultImage.setImage(result);
            Connect(messageForm);
        });

        //Canny effect
        canny.setOnAction((ActionEvent e) -> {
            Image result = service.processImage(originalImageFile, "CANNY");
            resultImage.setImage(result);
            Connect(messageForm);
        });

        //Fast effect
        fast.setOnAction((ActionEvent e) -> {
            Image result = service.processImage(originalImageFile, "FAST");
            resultImage.setImage(result);
            Connect(messageForm);
        });

        //ORB effect
        orb.setOnAction((ActionEvent e) -> {
            Image result = service.processImage(originalImageFile, "ORB");
            resultImage.setImage(result);
            Connect(messageForm);
        });

        //Download Image result
        download.setOnAction((ActionEvent e) -> {
            Image result = resultImage.getImage();
            File downloadImage = new File("data/download/result.jpg");
            BufferedImage image = SwingFXUtils.fromFXImage(result, null);
            try{
                ImageIO.write(image, "jpg", downloadImage);
                messageForm.appendText("Image result downloaded to download folder\r\n");

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        VBox buttons = new VBox();
        buttons.setPadding(new Insets(50, 15, 10, 15));
        buttons.setPrefWidth(300);
        buttons.getChildren().addAll(connectService, selectFile, processingLabel, crop, greyScale, rotate, erosion, dilation, featureLabel, canny, fast, orb, downloadLabel, download);

        VBox originalBox = new VBox();
        originalBox.setPadding(new Insets(0, 15, 10, 15));
        originalBox.setPrefWidth(500);
        originalBox.setAlignment(Pos.CENTER);
        originalBox.getChildren().addAll(originalImageLabel, rawImage);

        VBox resultBox = new VBox();
        resultBox.setPadding(new Insets(0, 15, 10, 15));
        resultBox.setPrefWidth(500);
        resultBox.setAlignment(Pos.CENTER);
        resultBox.getChildren().addAll(resultImageLabel, resultImage);

        HBox imageBoxes = new HBox();
        imageBoxes.getChildren().addAll(originalBox, resultBox);

        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER);
        messageBox.getChildren().add(messageForm);

        VBox rightPane = new VBox();
        rightPane.getChildren().addAll(imageBoxes, messageBox);

        //Add nodes to pane
        HBox nodes = new HBox();
        nodes.setPadding(new Insets(30, 15, 10, 15));
        nodes.getChildren().addAll(buttons, rightPane);

        StackPane body = new StackPane();
        body.getChildren().add(nodes);

        //Set the root node of the Scene
        getChildren().clear();
        getChildren().addAll(body);
    }

    //Method to connect to server
    private void Connect(TextArea messageForm){
        service = new ImageProcessor(5000);

        if (service.isConnected()){
            messageForm.setText("Connected to Server.\r\n");
        }
        else{
            messageForm.setText("Failed to connect to Server.\r\n");
        }
    }
}
