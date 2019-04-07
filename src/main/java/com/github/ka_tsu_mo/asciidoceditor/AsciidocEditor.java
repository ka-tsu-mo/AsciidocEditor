package com.github.ka_tsu_mo.asciidoceditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AsciidocEditor extends Application {

    static AppController appController;
    static Stage stage;

    static final String HOME = System.getProperty("user.home") + File.separator + ".AsciidocEditor";
    static final File localStorage = new File(HOME);

    public static void main(String[] args) {
        launch(args);
    }

    private boolean firstLaunch = false;

    @Override
    public void init() {

        if (Files.notExists(Paths.get(HOME))) {

            firstLaunch = true;

            try {
                Files.createDirectories(Paths.get(HOME, "stylesheets"));
                Files.createDirectory(Paths.get(HOME, "images"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (InputStream inputStream = getClass().getResourceAsStream("/asciidocEditor.properties")) {
                Files.copy(inputStream, Paths.get(HOME, "asciidocEditor.properties"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] cssFiles = { "application.css", "asciidoctor-default.css", "coderay-asciidoctor.css" };
            for (String fileName : cssFiles) {
                try (InputStream inputStream = getClass().getResourceAsStream("/stylesheets/" + fileName)) {
                    Files.copy(inputStream, Paths.get(HOME, "stylesheets", fileName), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        stage = primaryStage;
        Parent root = fxmlLoader.load(getClass().getResourceAsStream("/asciidocEditor.fxml"));
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(Paths.get(HOME, "stylesheets", "application.css").toUri().toString());
        stage.setTitle("Asciidoc Editor");
        stage.setScene(scene);
        stage.show();
        appController = fxmlLoader.getController();
        appController.enableSettingButtonAccelerator();
        if (firstLaunch) {
            appController.initEditorOnFirstLaunch();
            appController.initImagesDir();
        }
    }

}
