package com.github.ka_tsu_mo.asciidoceditor;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebView;

public class ConvertService extends Service {

    private ContentFormatter contentFormatter = ContentFormatter.getInstance();
    private WebView viewer;
    private String adocData;

    ConvertService(AppController controller) {
        this.viewer = controller.viewer;
    }

    public void restart(String adocData) {
        this.adocData = adocData;
        super.restart();
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() {
                contentFormatter.format(adocData);
                Platform.runLater(() -> viewer.getEngine().loadContent(contentFormatter.document.toString()));
                return null;
            }
        };
    }
}
