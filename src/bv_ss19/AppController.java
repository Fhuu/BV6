package bv_ss19;

import java.io.File;
import bv_ss19.DPCM.FilterType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;


public class AppController {

    private static final String initialFileName = "test1.jpg";
    private static File fileOpenPath = new File(".");
    private static final DPCM dpcm = new DPCM();

    @FXML
    private ImageView originalImage;

    @FXML
    private ImageView predictionErrorImage;

    @FXML
    private ImageView reconstructedImage;

    @FXML
    private Label entropyOrImg;

    @FXML
    private Label entropyPredImg;

    @FXML
    private ComboBox<FilterType> predictionBox;

    @FXML
    private Slider quantizationSlider;

    @FXML
    private Label quantizationLabel;

    @FXML
    private Label entropyReImg;

    @FXML
    private Label MSE;

    @FXML
    private Label messageLabel;

    @FXML
    void openImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(fileOpenPath);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images (*.jpg, *.png, *.gif)", "*.jpeg", "*.jpg", "*.png", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null) {
            fileOpenPath = selectedFile.getParentFile();
            RasterImage img = new RasterImage(selectedFile);
            img.convertToGray();
            img.setToView(originalImage);
            processImages();
            messageLabel.getScene().getWindow().sizeToScene();
        }
    }

    @FXML
    public void quantizationSliderChanged() {
        quantizationLabel.setText(String.format("%.2f", quantizationSlider.getValue()));
        processImages();
    }

    @FXML
    public void predictorChanged () {
        processImages();
    }

    @FXML
    public void initialize() {
        // set combo boxes items
        predictionBox.getItems().addAll(FilterType.values());
        predictionBox.setValue(FilterType.A);

        // initialize parameters

        // load and process default image
        RasterImage img = new RasterImage(new File(initialFileName));
        img.convertToGray();
        img.setToView(originalImage);
        processImages();
    }
    private void processImages() {
        if(originalImage.getImage() == null) return;

        long startTime = System.currentTimeMillis();

        RasterImage oriImg = new RasterImage(originalImage);
        RasterImage preImg = new RasterImage(oriImg.width, oriImg.height);
        RasterImage recImg = new RasterImage(oriImg.width, oriImg.height);


        dpcm.setLength(oriImg.argb.length);

        switch (predictionBox.getValue()) {
            case A:
                dpcm.horizontal(oriImg, preImg,(float) quantizationSlider.getValue(), recImg);
                break;
            case B:
                dpcm.vertical(oriImg, preImg,(float) quantizationSlider.getValue(), recImg);
                break;
            case C:
                dpcm.diagonal(oriImg, preImg,(float) quantizationSlider.getValue(), recImg);
                break;
            case ABC:
                dpcm.abc(oriImg, preImg,(float) quantizationSlider.getValue(), recImg);
                break;
            case adaptiv:
                dpcm.adaptiv(oriImg, preImg,(float) quantizationSlider.getValue(), recImg);
                break;
        }

        preImg.setToView(predictionErrorImage);
        recImg.setToView(reconstructedImage);

        entropyOrImg.setText(String.format("%.2f", dpcm.entropy(oriImg)));
        entropyPredImg.setText(String.format("%.2f", dpcm.entropy(preImg)));
        entropyReImg.setText(String.format("%.2f", dpcm.entropy(recImg)));
        MSE.setText("" + dpcm.getMSE());
        messageLabel.setText("Processing time: " + (System.currentTimeMillis() - startTime) + " ms");

    }
}
