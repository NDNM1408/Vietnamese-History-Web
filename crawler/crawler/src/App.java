import java.io.File;

public class App {
    public static void main(String[] args) throws Exception {
        File f1 = new File("data");
        boolean bool = f1.mkdir();
        if(bool) System.out.println("Created folder");
        else System.out.println("Existed folder");
        CrawlCharacterNKS crawlCharacter = new CrawlCharacterNKS();
        crawlCharacter.crawlAndSave();
    }
}
