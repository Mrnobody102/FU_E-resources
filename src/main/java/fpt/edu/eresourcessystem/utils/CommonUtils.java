package fpt.edu.eresourcessystem.utils;

import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.apache.tika.parser.txt.TXTParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CommonUtils {

    // Paging format
    public static List<Integer> pagingFormat(int totalPage, int pageIndex) {
        int delta = 2;
        List<Integer> pages = new ArrayList<>();
        for (int i = 1; i <= totalPage; i++) {
            if (i == 1 || i == totalPage || (i >= pageIndex - delta && i <= pageIndex + delta)) {
                pages.add(i);
            } else if (i == pageIndex - (delta + 1) || i == pageIndex + (delta + 1)) {
                pages.add(-1); // "..."
            }
        }
        return pages;
    }

    public static String extractTextFromFile(InputStream fileBytes) throws IOException, TikaException, SAXException {
//        File file = new File("src/main/1mb.pdf");

        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(10000000);
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        parser.parse(fileBytes, handler, metadata, context);
        return handler.toString();
    }

    public static String convertToPlainText(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.body().getAllElements();
        StringBuilder sb = new StringBuilder();

        for (Element element : elements) {
            String text = element.ownText();
            if (!text.isEmpty()) {
                sb.append(text).append("\n");
            }
        }

        return sb.toString();
    }
}
