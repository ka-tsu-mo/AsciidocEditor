package com.github.ka_tsu_mo.asciidoceditor;

import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AceEditorTab extends Tab {

    private AppController controller;
    private final WebView editor = new WebView();
    final WebEngine engine = editor.getEngine();

    boolean isLoaded = false;
    private Path path = Paths.get("unsaved");
    public Path getPath() {
        return this.path;
    }
    public void setPath(Path path) { this.path = path; }

    AceEditorTab(String title, TabPane tabPane, AppController controller) {

        super(title);
        this.controller = controller;
        tabPane.getTabs().add(this);
        this.setContent(editor);
        controller.editorPane.getSelectionModel().select(this);
        this.setOnCloseRequest((event) -> {
            if (tabPane.getTabs().size() == 1) event.consume();
        });

        initEditor();
    }

    private void initEditor() {

        editor.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCodeCombination copy = new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN);
                if (copy.match(event)) {
                    copy();
                    event.consume();
                }
            }
        });
        editor.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCodeCombination paste = new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN);
                if (paste.match(event)) {
                    paste();
                    event.consume();
                }
            }
        });
        editor.setContextMenuEnabled(false);

        engine.setUserDataDirectory(AsciidocEditor.localStorage);
        engine.setJavaScriptEnabled(true);
        engine.getLoadWorker().stateProperty().addListener((ObservableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject win = (JSObject) engine.executeScript("window");
                win.setMember("convertService", controller.convertService);
                isLoaded = true;
            }
        });

        engine.load(getClass().getResource("/ace.html").toExternalForm());

    }

    private JSObject aceEditor() {
        return (JSObject) engine.executeScript("editor");
    }

    public String getValue() {
        JSObject aceEditor = aceEditor();
        JSObject session = (JSObject) aceEditor.call("getSession");
        JSObject document = (JSObject) session.call("getDocument");
        return (String) document.call("getValue");
    }

    public void setValue(String value) {
        JSObject aceEditor = aceEditor();
        aceEditor.call("setValue", value, 1);
    }

    private Clipboard clipboard = Clipboard.getSystemClipboard();

    private void copy() {
        JSObject aceEditor = aceEditor();
        String copyText = (String) aceEditor.call("getCopyText");
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(copyText);
        clipboard.setContent(clipboardContent);
    }

    private void paste() {
        if (clipboard.hasString()) {
            String clipboardContent = clipboard.getString();
            JSObject aceEditor = aceEditor();
            JSObject aceSession = (JSObject) aceEditor.call("getSession");
            JSObject cursorPosition = (JSObject) aceEditor.call("getCursorPosition");
            aceSession.call("insert", cursorPosition, clipboardContent);
        }
    }

}
