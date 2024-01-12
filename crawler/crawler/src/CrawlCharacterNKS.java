
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;



public class CrawlCharacterNKS extends Crawler{

    public CrawlCharacterNKS() {
        super.setRoot("https://nguoikesu.com");
        super.setStart("https://nguoikesu.com/nhan-vat");
        super.setFolder("data\\CharacterNKS.json");
    }

    /**
     * Thu thập dữ liệu ở trang web chỉ định
     * @param url
     * @throws IOException
     */
    @Override
    public void scrapePage(String url) throws IOException {
        Document doc;
        try {
            doc = Jsoup.connect(url).userAgent("Jsoup client").timeout(20000).get();
            doc.charset(Charset.forName("utf-8"));
            // System.out.println(doc.toString());
            Elements link = doc.select("div.item-content > div > h2 > a");
            for (Element element:link){
                // Name and ID
                String name = element.text();
                byte[] utf8Bytes = name.getBytes(StandardCharsets.UTF_8);
                name = new String(utf8Bytes, StandardCharsets.UTF_8);

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
