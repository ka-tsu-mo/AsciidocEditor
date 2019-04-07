package com.github.ka_tsu_mo.asciidoceditor;

import javafx.concurrent.Worker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileService {

    private AppController controller;

    FileService(AppController controller) {
        this.controller = controller;
    }

    void openFile() {

        TabPane editorPane = controller.editorPane;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Opne File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("AsciiDoc", "*.adoc", "*.asc", "*.asciidoc")
        );

        File file = fileChooser.showOpenDialog(AsciidocEditor.stage);
        Path path;
        if (file == null) {
            return;
        } else {
            path = file.toPath();
        }

        for (Tab tab : editorPane.getTabs()) {
            AceEditorTab aceEditorTab = (AceEditorTab) tab;
            if (aceEditorTab.getPath().equals(path)) {
                editorPane.getSelectionModel().select(aceEditorTab);
                return;
            }
        }

        String adocData;

        try {
            adocData = Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        new AceEditorTab(file.getName(), controller.editorPane, controller);
        controller.selectedTab.engine.getLoadWorker().stateProperty().addListener((ObservableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                controller.selectedTab.setValue(adocData);
                controller.selectedTab.setPath(file.toPath());
            }
        });


    }

    void newFile() {

        TextInputDialog dialog = new TextInputDialog("untitled");
        dialog.initOwner(AsciidocEditor.stage);
        dialog.setContentText("Name:");
        dialog.setTitle("Create File");
        dialog.setGraphic(null);
        dialog.setHeaderText(null);
        String fileName = dialog.showAndWait().orElse("untitled");
        if (dialog.getResult() != null) {
            new AceEditorTab(fileName + ".adoc", controller.editorPane, controller);
        }
        controller.viewer.getEngine().reload();
    }

    void saveAs() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(controller.selectedTab.getText());
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("AsciiDoc", "*.adoc", "*.asc", "*.asciidoc")
        );
        File file = fileChooser.showSaveDialog(AsciidocEditor.stage);
        if (file != null) {
            try {
                 Files.writeString(file.toPath(), controller.selectedTab.getValue(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                controller.selectedTab.setText(file.getName());
                controller.selectedTab.setPath(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    void save() {

        Path path = controller.selectedTab.getPath();

        if (controller.selectedTab.getPath().equals(Paths.get("unsaved"))) {
            saveAs();
            return;
        }

        try {
            Files.writeString(path, controller.selectedTab.getValue(), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void export() {

        ContentFormatter contentFormatter = ContentFormatter.getInstance();
        contentFormatter.convert(controller.selectedTab.getValue());

        FileChooser fileChooser = new FileChooser();
        String fileName = controller.selectedTab.getText();
        fileChooser.setInitialFileName(fileName.substring(0, fileName.lastIndexOf('.')) + ".html");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("HTML", "*.html")
        );
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                Files.writeString(file.toPath(), contentFormatter.document.outerHtml(), StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
