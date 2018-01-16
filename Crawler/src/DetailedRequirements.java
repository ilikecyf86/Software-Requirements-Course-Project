import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by cyf on 2017/12/18.
 */
public class DetailedRequirements {
    private static Queue <String> accessQueue = new LinkedList<String>();
    private static String domain = "https://github.com";
//    private static String path = "/Users/cyf/Documents/作业/软件需求工程/DataOpen/";
    private static String path = "/Users/cyf/Documents/作业/软件需求工程/DataTest/";
    public static void main(String[] args){
        Crawl();
    }
    public static void Crawl() {
        for(int i = 120; i <= 143; i++ ) {
//            String url = "https://github.com/eclipse/che/issues?page=" + i + "&q=is%3Aissue+is%3Aopen";
            String url = "https://github.com/eclipse/che/issues?page=" + i + "&q=is%3Aissue+is%3Aclosed";
            try {
                Document page = Jsoup.connect(url).get();
                Elements title = page.select(".float-left.col-9.p-2.lh-condensed .link-gray-dark.no-underline.h4.js-navigation-open[href]");
                for(int j = 0; j < title.size(); j++){
                    accessQueue.offer(title.get(j).attr("href"));
                }
                System.out.println();
            } catch (IOException e) {
                System.out.println("Connection error");
            }
        }
        int count = 0;
        while(!accessQueue.isEmpty()){
            String url = domain + accessQueue.poll();
            System.out.println(url);
            try {
                Document page = Jsoup.connect(url).get();
                Element title = page.select(".js-issue-title").first();
//                System.out.println(title.text());
                // 根据需要修改此处选择内容
                Element content = page.select(".d-block.comment-body.markdown-body.js-comment-body li").first();
//                System.out.println(content.text());
                Elements labels = page.select(".label-color");
//                for(int j = 0; j < labels.size(); j++){
//                    System.out.println(labels.get(j).text());
//                }
                String filePath = path + count + ".txt";
                File file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream outStream = new FileOutputStream(file);
                outStream.write(title.text().getBytes());
                outStream.write("\n".getBytes());
                for(int j = 0; j < labels.size(); j++){
                    outStream.write(labels.get(j).text().getBytes());
                    outStream.write(" ".getBytes());
                }
                outStream.write("\n".getBytes());
                outStream.write(content.text().getBytes());
                outStream.close();
                count++;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
