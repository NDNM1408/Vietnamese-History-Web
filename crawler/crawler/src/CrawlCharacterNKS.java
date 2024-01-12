
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;


/**
 * Lớp dùng để thu thập nhân vật lịch sử trên Người Kể Sử
 * @since: 15/01/2023
 */
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
                information += scrapeInfobox(id);
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

    public String scrapeInfobox(String url) {
        Document doc;
        String description = "";
        try {
            // Scrape info box
            doc = Jsoup.connect(url).userAgent("Jsoup client").timeout(20000).get();
            Elements tr = doc.select("div.infobox > table > tbody > tr");
            if(tr != null && tr.size() > 0){
                for (int i = 4; i < tr.size(); i++){
                    Element th = tr.get(i).selectFirst("th");
                    Element td = tr.get(i).selectFirst("td");
                    if(th != null && td != null){
                        description += th.text() + ": " + td.text() + "\n";
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return description;
    }
}