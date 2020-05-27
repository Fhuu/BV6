// BV Ue1 SS2019 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-11

package bv_ss19;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("AppView.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("BV6 - SS2019 - <Baloska, Son>");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
