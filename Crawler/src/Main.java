import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by cyf on 2017/12/18.
 */
public class Main {
    public static void main(String[] args){
        Crawl();
    }
    public static void Crawl() {
        for(int i = 1; i <= 1; i++ ) {
            String url = "https://github.com/eclipse/che/issues?page=" + i + "&q=is%3Aissue+is%3Aopen";
            try {
                Document page = Jsoup.connect(url).get();
                Elements title = page.select(".float-left.col-9.p-2.lh-condensed .link-gray-dark.no-underline.h4.js-navigation-open");
                for(int j = 0; j < title.size(); j++){
                    System.out.println(title.get(j).text());
                }
                Elements labels = page.select(".float-left.col-9.p-2.lh-condensed .labels");
                for(int j = 0; j < labels.size(); j++){
                    System.out.println(labels.get(j).text());
                }
                System.out.println();
            } catch (IOException e) {
                System.out.println("Connection error");
            }
        }
    }
}