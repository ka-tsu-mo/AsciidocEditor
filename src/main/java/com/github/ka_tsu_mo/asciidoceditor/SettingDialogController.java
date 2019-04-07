package com.github.ka_tsu_mo.asciidoceditor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class SettingDialogController implements Initializable {

    @FXML
    ChoiceBox<String> docType;
    @FXML
    ChoiceBox<String> attributeMissing;
    @FXML
    ChoiceBox<String> attributeUndefined;
    @FXML
    ChoiceBox<String> sectNumLevels;
    @FXML
    ChoiceBox<String> icons;
    @FXML
    ChoiceBox<String> sourceHighlighter;

    @FXML
    TextField imagesDir;
    @FXML
    Button imgDirButton;
    @FXML
    TextField iconsDir;
    @FXML
    Button iconsDirButton;

    @FXML
    CheckBox dataUri;
    @FXML
    CheckBox showTitle;
    @FXML
    CheckBox allowUriRead;
    @FXML
    CheckBox noFooter;
    @FXML
    CheckBox hideUriScheme;
    @FXML
    CheckBox hardBreaks;
    @FXML
    CheckBox linkCss;

    @FXML
    ChoiceBox<String> aceTheme;
    @FXML
    ChoiceBox<String> keyBoardHandler;

    @FXML
    CheckBox useSoftTabs;
    @FXML
    CheckBox showGutter;
    @FXML
    CheckBox wrap;
    @FXML
    CheckBox highlightActiveLine;

    @FXML
    Slider scrollSpeed;
    @FXML
    Spinner<Integer> fontSize;
    @FXML
    Spinner<Integer> tabSize;

    private ContentFormatter contentFormatter = ContentFormatter.getInstance();
    private AppController appController = AsciidocEditor.appController;
    Properties properties = new Properties();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try (InputStream inputStream = Files.newInputStream(Paths.get(AsciidocEditor.HOME, "asciidocEditor.properties"))) {
            properties.load(inputStream);
        } catch(IOException e) {
            e.printStackTrace();
        }

        initChoiceBox();
        initCheckBox();
        initSpinner();
        initTextField();

        imgDirButton.setOnAction(event -> browse(imagesDir));

        iconsDirButton.setOnAction(event -> browse(iconsDir));

        docType.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            contentFormatter.attributes.setDocType(newValue);
            properties.setProperty("docType", newValue);
        });

        attributeMissing.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            contentFormatter.attributes.setAttributeMissing(newValue);
            properties.setProperty("attributeMissing", newValue);
        });

        attributeUndefined.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            contentFormatter.attributes.setAttributeUndefined(newValue);
            properties.setProperty("attributeUndefined", newValue);
        }));

        sectNumLevels.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                contentFormatter.attributes.setSectNumLevels(Integer.parseInt(newValue));
                properties.setProperty("sectNumLevels", newValue);
        });

        icons.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("image")) {
                iconsDirButton.setDisable(false);
                iconsDir.setDisable(false);
                if (properties.getProperty("iconsDir").equals("null"))
                    contentFormatter.attributes.setIcons(null);
            } else {
                iconsDirButton.setDisable(true);
                iconsDir.setDisable(true);
                contentFormatter.attributes.setIcons(newValue);
            }
            properties.setProperty("icons", newValue);
        });

        iconsDir.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                iconsDir.positionCaret(iconsDir.getText().length());
            }
        });

        imagesDir.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                imagesDir.positionCaret(imagesDir.getText().length());
            }
        });

        sourceHighlighter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            contentFormatter.attributes.setSourceHighlighter(newValue.toLowerCase().replace(".", ""));
            properties.setProperty("sourceHighlighter", newValue);
        });

        dataUri.setOnAction(event -> {
            contentFormatter.attributes.setDataUri(dataUri.isSelected());
            properties.setProperty("dataUri", String.valueOf(dataUri.isSelected()));
        });

        showTitle.setOnAction(event -> {
            contentFormatter.attributes.setShowTitle(showTitle.isSelected());
            properties.setProperty("showTitle", String.valueOf(showTitle.isSelected()));
        });

        allowUriRead.setOnAction(event -> {
            contentFormatter.attributes.setAllowUriRead(allowUriRead.isSelected());
            properties.setProperty("allowUriRead", String.valueOf(allowUriRead.isSelected()));
        });

        noFooter.setOnAction(event -> {
            contentFormatter.attributes.setNoFooter(noFooter.isSelected());
            properties.setProperty("noFooter", String.valueOf(noFooter.isSelected()));
        });

        hideUriScheme.setOnAction(event -> {
            contentFormatter.attributes.setHideUriScheme(hideUriScheme.isSelected());
            properties.setProperty("hideUriScheme", String.valueOf(hideUriScheme.isSelected()));
        });

        hardBreaks.setOnAction(event -> {
            contentFormatter.attributes.setHardbreaks(hardBreaks.isSelected());
            properties.setProperty("hardBreaks", String.valueOf(hardBreaks.isSelected()));
        });

        linkCss.setOnAction(event -> {
            contentFormatter.attributes.setLinkCss(linkCss.isSelected());
            properties.setProperty("linkCss", String.valueOf(linkCss.isSelected()));
        });

        aceTheme.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            JSObject localStorage = (JSObject) appController.selectedTab.engine.executeScript("localStorage");
            localStorage.call("setItem", "aceTheme", "ace/theme/" + newValue.toLowerCase().replace(" ", "_"));
            properties.setProperty("aceTheme", newValue);
        }));

        keyBoardHandler.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            JSObject localStorage = (JSObject)appController.selectedTab.engine.executeScript("localStorage");
            localStorage.call("setItem", "keyBoardHandler", "ace/keyboard/" + newValue);
            properties.setProperty("keyBoardHandler", newValue);
        });

        useSoftTabs.setOnAction(event -> {
            JSObject localStorage = (JSObject)appController.selectedTab.engine.executeScript("localStorage");
            localStorage.call("setItem", "useSoftTabs", useSoftTabs.isSelected());
            properties.setProperty("useSoftTabs", String.valueOf(useSoftTabs.isSelected()));
        });

        showGutter.setOnAction(event -> {
            JSObject localStorage = (JSObject)appController.selectedTab.engine.executeScript("localStorage");
            localStorage.call("setItem", "showGutter", showGutter.isSelected());
            properties.setProperty("showGutter", String.valueOf(showGutter.isSelected()));
        });

        wrap.setOnAction(event -> {
            JSObject localStorage = (JSObject)appController.selectedTab.engine.executeScript("localStorage");
            localStorage.call("setItem", "wrap", wrap.isSelected());
            properties.setProperty("wrap", String.valueOf(wrap.isSelected()));
        });

        highlightActiveLine.setOnAction(event -> {
            JSObject localStorage = (JSObject)appController.selectedTab.engine.executeScript("localStorage");
            localStorage.call("setItem", "highlightActiveLine", highlightActiveLine.isSelected());
            properties.setProperty("highlightActiveLine", String.valueOf(highlightActiveLine.isSelected()));
        });

        fontSize.valueProperty().addListener((observable, oldValue, newValue) -> {
            JSObject localStorage = (JSObject)appController.selectedTab.engine.executeScript("localStorage");
            localStorage.call("setItem", "fontSize", newValue);
            properties.setProperty("fontSize", String.valueOf(newValue));
        });

        tabSize.valueProperty().addListener((observable, oldValue, newValue) -> {
            JSObject localStorage = (JSObject)appController.selectedTab.engine.executeScript("localStorage");
            localStorage.call("setItem", "tabSize", newValue);
            properties.setProperty("tabSize", String.valueOf(newValue));
        });

        scrollSpeed.setValue(Double.parseDouble(properties.getProperty("scrollSpeed")));
        scrollSpeed.valueProperty().addListener((observable, oldValue, newValue) -> {
            JSObject localStorage = (JSObject)appController.selectedTab.engine.executeScript("localStorage");
            localStorage.call("setItem", "scrollSpeed", newValue);
            properties.setProperty("scrollSpeed", String.valueOf(newValue));
        });

    }

    private void initChoiceBox() {

        docType.getItems().addAll("article", "book", "manpage", "inline");
        docType.setValue(properties.getProperty("docType"));

        attributeMissing.getItems().addAll("skip", "drop", "drop-line", "warn");
        attributeMissing.setValue(properties.getProperty("attributeMissing"));

        attributeUndefined.getItems().addAll("drop", "drop-line");
        attributeUndefined.setValue(properties.getProperty("attributeUndefined"));

        sectNumLevels.getItems().addAll("0", "1", "2", "3", "4", "5");
        sectNumLevels.setValue(properties.getProperty("sectNumLevels"));

        icons.getItems().addAll("image", "font");
        icons.setValue(properties.getProperty("icons"));

        sourceHighlighter.getItems().addAll("CodeRay", "highlight.js", "prettify");
        sourceHighlighter.setValue(properties.getProperty("sourceHighlighter"));

        aceTheme.getItems().addAll(
            "Ambiance",
                "Chaos",
                "Chrome",
                "Clouds",
                "Clouds Midnight",
                "Cobalt",
                "Crimson Editor",
                "Dawn",
                "Dracula",
                "Dreamweaver",
                "Eclipse",
                "Github",
                "Gob",
                "Gruvbox",
                "Idle Fingers",
                "Iplastic",
                "Katzenmilch",
                "Kr Theme",
                "Kurior",
                "Merbivore",
                "Merbivore Soft",
                "Mono Industrial",
                "Monokai",
                "Pastel On Dark",
                "Solarized Dark",
                "Solarized Light",
                "SQL Server",
                "Terminal",
                "Textmate",
                "Tomorrow",
                "Tomorrow Night",
                "Tomorrow Night Blue",
                "Tomorrow Night Bright",
                "Tomorrow Night Eighties",
                "Twilight",
                "Vibrant Ink",
                "XCode"
        );
        aceTheme.setValue(properties.getProperty("aceTheme"));

        keyBoardHandler.getItems().addAll("ace", "emacs", "vim", "sublime");
        keyBoardHandler.setValue(properties.getProperty("keyBoardHandler"));

    }

    private void initCheckBox() {
        Set<CheckBox> checkBoxes = new HashSet<>(Arrays.asList(dataUri, showTitle, allowUriRead, noFooter, hideUriScheme, hardBreaks, linkCss, useSoftTabs, showGutter, wrap, highlightActiveLine));
        checkBoxes.forEach((CheckBox checkBox) -> {
            checkBox.setSelected((Boolean.valueOf(properties.getProperty(checkBox.getId()))));
        });
    }

    private void initSpinner() {

        SpinnerValueFactory<Integer> fontSizeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, Integer.parseInt(properties.getProperty("fontSize")));
        SpinnerValueFactory<Integer> tabSizeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, Integer.parseInt(properties.getProperty("tabSize")));
        fontSize.setValueFactory(fontSizeValueFactory);
        tabSize.setValueFactory(tabSizeValueFactory);

    }

    private void initTextField() {
        TextField[] textFields = {imagesDir, iconsDir};
        for (TextField textField : textFields) {
            textField.textProperty().setValue(properties.getProperty(textField.getId()));
            textField.setEditable(false);
        }
    }

    private void browse(TextField textField) {
        textField.setEditable(true);
        String directoryName = properties.getProperty(textField.getId());
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (!directoryName.equals("null"))
            directoryChooser.setInitialDirectory(new File(properties.getProperty(textField.getId())));
        File file = directoryChooser.showDialog(null);
        if (file == null) {
            return;
        } else {
            if (textField.getId().equals("imagesDir")) contentFormatter.attributes.setImagesDir(file.getAbsolutePath());
            if (textField.getId().equals("iconsDir")) contentFormatter.attributes.setIconsDir(file.getAbsolutePath());
            textField.textProperty().setValue(file.getAbsolutePath());
            properties.setProperty(textField.getId(), file.getAbsolutePath());
        }
        textField.setEditable(false);
    }

}
