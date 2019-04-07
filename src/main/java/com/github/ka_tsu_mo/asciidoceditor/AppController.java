package com.github.ka_tsu_mo.asciidoceditor;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    @FXML
    WebView viewer;
    @FXML
    TabPane editorPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menu;
    @FXML
    private MenuItem makeFile;
    @FXML
    private MenuItem openFile;
    @FXML
    private MenuItem saveAs;
    @FXML
    private MenuItem save;
    @FXML
    private MenuItem exportAsHTML;

    @FXML
    private ToggleButton toggleEditModeButton;
    @FXML
    private ToggleButton toggleViewerModeButton;
    @FXML
    Button settingButton;

    private FileService fileService;
    ConvertService convertService;

    AceEditorTab selectedTab;

    private boolean isComposing = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        final String OS = System.getProperty("os.name");
        if (OS.startsWith("Mac")) {
            menuBar.setUseSystemMenuBar(true);
        } else {
            menuBar.setStyle(
                    "-fx-min-height: 25;" +
                    "-fx-max-height: 25;");
            menu.setStyle(
                    "-fx-min-height: 25;" +
                    "-fx-max-height: 25;");
        }

        convertService = new ConvertService(this);
        fileService = new FileService(this);

        viewer.getEngine().setJavaScriptEnabled(true);
        selectedTab = new AceEditorTab("untitled.adoc", editorPane, this);

        editorPane.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (newValue instanceof AceEditorTab) {
                selectedTab = (AceEditorTab) newValue;
            }
            if (selectedTab.isLoaded && !convertService.isRunning()) {
                convertService.reset();
                convertService.restart(selectedTab.getValue());
            }
        });

        //Apply Ace to Webview (resolve problem of JapanseIM on macOS)
        editorPane.addEventFilter(InputMethodEvent.ANY, new EventHandler<InputMethodEvent>() {
            @Override
            public void handle(InputMethodEvent event) {
                isComposing = !(event.getComposed().size() == 0);
            }
        });
        editorPane.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (isComposing) event.consume();
            }
        });
        editorPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCodeCombination keyCodeCombination = new KeyCodeCombination(KeyCode.COMMA, KeyCombination.SHORTCUT_DOWN);
                if (keyCodeCombination.match(event)) {
                    settingButton.fire();
                    event.consume();
                }
            }
        });

        //Open link in default browser
        viewer.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SCHEDULED) {
                String urlLocation = viewer.getEngine().getLocation();
                if (urlLocation.contains("http:") || urlLocation.contains("https:")) {
                    viewer.getEngine().getLoadWorker().cancel();
                    if (!convertService.isRunning()) {
                        convertService.reset();
                        convertService.restart(selectedTab.getValue());
                    }
                    try {
                        Desktop.getDesktop().browse(new URI(urlLocation));
                    } catch (URISyntaxException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        toggleEditModeButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                gridPane.getColumnConstraints().get(0).setPercentWidth(50);
                gridPane.getColumnConstraints().get(1).setPercentWidth(50);
            }
            toggleViewerModeButton.selectedProperty().setValue(!newValue);
        });
        toggleViewerModeButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                gridPane.getColumnConstraints().get(0).setPercentWidth(0);
                gridPane.getColumnConstraints().get(1).setPercentWidth(100);
            }
            toggleEditModeButton.selectedProperty().setValue(!newValue);
        });

    }

    @FXML
    private void openFile(ActionEvent actionEvent) {
        fileService.openFile();
    }

    @FXML
    private void newFile(ActionEvent actionEvent) {
        fileService.newFile();
    }

    @FXML
    private void saveAs(ActionEvent actionEvent) {
        fileService.saveAs();
    }

    @FXML
    private void save(ActionEvent actionEvent) {
        fileService.save();
    }

    @FXML
    private void exportAsHTML(ActionEvent actionEvent) {
        fileService.export();
    }

    @FXML
    private void changeSettings() {
        Dialog<ButtonType> settingDialog = new Dialog<>();
        settingDialog.initOwner(AsciidocEditor.stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/settingDialog.fxml"));
        try {
            settingDialog.setDialogPane(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SettingDialogController settingDialogController = loader.getController();
        settingDialog.showAndWait()
            .filter(response -> response == ButtonType.APPLY)
            .ifPresent(response -> {
                try(OutputStream outputStream = Files.newOutputStream(Paths.get(AsciidocEditor.HOME,"asciidocEditor.properties"))) {
                    settingDialogController.properties.store(outputStream, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                editorPane.getTabs().forEach(tab -> {
                    AceEditorTab aceEditorTab = (AceEditorTab) tab;
                    aceEditorTab.engine.executeScript("dispatchStorageEvent()");
                });
                if (selectedTab.isLoaded && !convertService.isRunning()){
                    convertService.reset();
                    convertService.restart(selectedTab.getValue());
                }
            });

    }

    void enableSettingButtonAccelerator() {
        settingButton.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.COMMA, KeyCombination.SHORTCUT_DOWN), new Runnable() {
            @Override
            public void run() {
                settingButton.fire();
            }
        });
    }

    void initEditorOnFirstLaunch() {

        Properties properties = new Properties();

        try {
            properties.load(getClass().getResourceAsStream("/asciidocEditor.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        selectedTab.engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject localStorage = (JSObject) selectedTab.engine.executeScript("localStorage");
                localStorage.call("setItem", "aceTheme", "ace/theme/" + properties.getProperty("aceTheme").toLowerCase().replace(" ", "_"));
                localStorage.call("setItem", "keyBoardHandler", "ace/keyboard/" + properties.getProperty("keyBoardHandler"));
                localStorage.call("setItem", "useSoftTabs", properties.getProperty("useSoftTabs"));
                localStorage.call("setItem", "showGutter", properties.getProperty("showGutter"));
                localStorage.call("setItem", "wrap", properties.getProperty("wrap"));
                localStorage.call("setItem", "highlightActiveLine", properties.getProperty("highlightActiveLine"));
                localStorage.call("setItem", "fontSize", properties.getProperty("fontSize"));
                localStorage.call("setItem", "tabSize", properties.getProperty("tabSize"));
                localStorage.call("setItem", "scrollSpeed", properties.getProperty("scrollSpeed"));
                selectedTab.engine.executeScript("dispatchStorageEvent()");
            }
        });

    }

    void initImagesDir() {

        Properties properties = new Properties();

        try (InputStream inputStream = Files.newInputStream(Paths.get(AsciidocEditor.HOME, "asciidocEditor.properties"))) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        properties.setProperty("imagesDir", Paths.get(AsciidocEditor.HOME, "images").toString());

        try (OutputStream outputStream = Files.newOutputStream(Paths.get(AsciidocEditor.HOME, "asciidocEditor.properties"))) {
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ContentFormatter.getInstance().attributes.setImagesDir(Paths.get(AsciidocEditor.HOME, "images").toString());

    }

}
