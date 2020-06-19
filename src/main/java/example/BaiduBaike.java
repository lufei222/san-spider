package example;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 0.4.0
 * @author code4crafter@gmail.com
 */
public class BaiduBaike{

    @ExtractBy("//h1/allText()")
    private String name;

    @ExtractBy("//div[@class='lemma-summary']//div[@class='para']/allText()")
    private String description;

    @Override
    public String toString() {
        return "BaiduBaike{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static void main(String[] args) {
        OOSpider ooSpider = OOSpider.create(Site.me().setSleepTime(0), BaiduBaike.class);
        //single download
        String urlTemplate = "http://baike.baidu.com/search/word?word=%s&pic=1&sug=1&enc=utf8";
        BaiduBaike baike = ooSpider.<BaiduBaike>get("http://baike.baidu.com/search/word?word=httpclient&pic=1&sug=1&enc=utf8");
        System.out.println(baike);

        //multidownload
        List<String> list = new ArrayList<String>();
        list.add(String.format(urlTemplate,"风力发电"));
        list.add(String.format(urlTemplate,"太阳能"));
        list.add(String.format(urlTemplate,"地热发电"));
        list.add(String.format(urlTemplate,"地热发电"));
        List<BaiduBaike> resultItemses = ooSpider.<BaiduBaike>getAll(list);
        for (BaiduBaike resultItemse : resultItemses) {
            System.out.println(resultItemse);
        }
        ooSpider.close();
    }

//    public static void main(String[] args) {
//        //results will be saved to "/data/webmagic/" in json format
//        OOSpider.create(Site.me(), new JsonFilePageModelPipeline("/luostar"), BaiduBaike.class)
//                .addUrl("http://baike.baidu.com/search/word?word=%s&pic=1&sug=1&enc=utf8").run();
//    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
