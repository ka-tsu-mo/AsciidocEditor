package com.github.ka_tsu_mo.asciidoceditor;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.print.attribute.URISyntax;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

import static org.asciidoctor.Asciidoctor.Factory.create;
import static org.asciidoctor.AttributesBuilder.attributes;
import static org.asciidoctor.OptionsBuilder.options;

public class ContentFormatter {

    private Asciidoctor asciidoctor = create();
    private Options options;
    Attributes attributes;

    private static class ContentFormatterHolder {
        private static final ContentFormatter CONTENT_FORMATTER = new ContentFormatter();
    }

    private ContentFormatter() {

        Properties properties = new Properties();
        try(InputStream inputStream = Files.newInputStream(Paths.get(AsciidocEditor.HOME, "asciidocEditor.properties"))) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        asciidoctor.requireLibrary("asciidoctor-diagram");

        attributes = attributes()
                .backend("html5")
                .styleSheetName(Paths.get(AsciidocEditor.HOME, "stylesheets", "asciidoctor-default.css").toString())
                .linkCss(Boolean.valueOf(properties.getProperty("linkCss")))
                .docType(properties.getProperty("docType"))
                .dataUri(Boolean.valueOf(properties.getProperty("dataUri")))
                .sectNumLevels(Integer.parseInt(properties.getProperty("sectNumLevels")))
                .allowUriRead(Boolean.valueOf(properties.getProperty("allowUriRead")))
                .attributeMissing(properties.getProperty("attributeMissing"))
                .attributeUndefined(properties.getProperty("attributeUndefined"))
                .noFooter(Boolean.valueOf(properties.getProperty("noFooter")))
                .hardbreaks(Boolean.valueOf(properties.getProperty("hardBreaks")))
                .showTitle(Boolean.valueOf(properties.getProperty("showTitle")))
                .hiddenUriScheme(Boolean.valueOf(properties.getProperty("hideUriScheme")))
                .sourceHighlighter(properties.getProperty("sourceHighlighter").toLowerCase().replace(".", ""))
                .icons(properties.getProperty("icons"))
                .imagesDir(Paths.get(AsciidocEditor.HOME, "images").toString())
                .stylesDir(Paths.get(AsciidocEditor.HOME, "stylesheets").toString())
                .get();

        options = options()
                .headerFooter(true)
                .safe(SafeMode.UNSAFE)
                .attributes(attributes)
                .get();

        if (!properties.getProperty("iconsDir").equals("null")) {
            attributes.setIconsDir(properties.getProperty("iconsDir"));
            if (properties.getProperty("icons").equals("image"))
                attributes.setIcons(null);
        }

    }

    public static ContentFormatter getInstance() {
        return ContentFormatterHolder.CONTENT_FORMATTER;
    }

    Document document;

    public void convert(String adocData) {
        String converted = asciidoctor.convert(adocData, options);
        document = Jsoup.parse(converted);
    }

    public void format(String adocData) {
        convert(adocData);
        addScheme();
        addAnchorLinkScript();
    }

    private void addScheme() {

        ArrayList<Element> src = document.getElementsByAttribute("src");
        ArrayList<Element> href = document.getElementsByAttribute("href");

        for (Element e : src) {
            String attributeValue = e.attr("src");
            if (!attributeValue.contains("data:") &&
                !attributeValue.contains("https:") &&
                !attributeValue.contains("file:")
            ) {
                attributeValue = Paths.get(attributeValue).toUri().toString();
                e.attr("src", attributeValue);
            }
        }

        for (Element e : href) {
            if (e.attr("href").contains("coderay-asciidoctor.css"))
                e.attr("href", Paths.get(AsciidocEditor.HOME, "stylesheets", "coderay-asciidoctor.css").toUri().toString());

            if (e.attr("href").contains("asciidoctor-default.css"))
                e.attr("href", Paths.get(AsciidocEditor.HOME, "stylesheets", "asciidoctor-default.css").toUri().toString());
        }

    }

    private void addAnchorLinkScript() {

        String anchorLinkScript =
                "function anchorlink(id_name) {" +
                    "obj = document.getElementById(id_name);" +
                    "obj.scrollIntoView(true);" +
                "}";

        Element body = document.body();
        body.appendElement("script").append(anchorLinkScript);

        ArrayList<Element> elements = document.getElementsByTag("a");
        for (Element e : elements) {
            if (!(
                e.attr("href").contains("http:") ||
                e.attr("href").contains("https:")
            )) {
                String idName = e.attr("href").substring(1);
                e.attr("onclick", "anchorlink(\'" + idName + "\')");
            }
        }
    }

}
