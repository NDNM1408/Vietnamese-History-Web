

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;



public class CrawlEventNKS extends Crawler{
    public CrawlEventNKS() {
        super.setRoot("https://nguoikesu.com");
        super.setFolder("data\\Eventnks.json");
        super.setStart("https://nguoikesu.com/tu-lieu?start=5");
    }
    /**
     * Thu thập dữ liệu ở trang web chỉ định
     * @param url
     * @throws IOException
     */
    @Override
    public void scrapePage(String url) throws IOException{
        Document doc;
        try {
            doc = Jsoup.connect(url).userAgent("Jsoup client").timeout(20000).get();
            Elements link = doc.select("div.item-content > div > h2 > a");
            for (Element element:link){
                // Name and ID
                String name = element.text();
                String id = super.getRoot() + element.attr("href");
                System.out.println(name);
                System.out.println(id);
                JSONObject object = new JSONObject();
                object.put("name",name);
                object.put("id", id);
                // Information
                String information = "";
                information += scrapeInformation(id,"div.com-content-article__body > p:first-of-type");
                object.put("info", information);
                // Connection
                object.put("connect",scrapeConnect(id,"p > a"));

                super.getOutput().add(object);
                System.out.println(super.getOutput().size());
            }
            Elements href = doc.select("div.com-content-category-blog__pagination > nav > ul > li:nth-of-type(13) > a");
            if(href != null){
                for(Element element:href){
                    scrapePage(super.getRoot() + element.attr("href"));
                }
            }
        } catch (UnknownHostException e){
            throw new UnknownHostException("Turn on your Internet please");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
