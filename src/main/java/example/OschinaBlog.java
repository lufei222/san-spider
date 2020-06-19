package example;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.3.2
 */
//@TargetUrl("http://my.oschina.net/flashsword/blog/\\d+")
public class OschinaBlog {

    @ExtractBy("//div[@class='space-index-container']//div[@class='item blog-item']//div[@class='content']//a[1]//allText()")
    private String title;
//
//    @ExtractBy(value = "div.BlogContent", type    = ExtractBy.Type.Css)
//    private String content;
//
//    @ExtractBy(value = "//div[@class='content']/a/text()", multi = true)
//    private List<String> tags;
//
//    @ExtractBy("//div[@class='BlogStat']/regex('\\d+-\\d+-\\d+\\s+\\d+:\\d+')")
//    private Date date;

//    public static void main(String[] args) {
////        //results will be saved to "/data/webmagic/" in json format
////        OOSpider.create(Site.me(), new JsonFilePageModelPipeline("/luostar"), OschinaBlog.class)
////                .addUrl("http://my.oschina.net/flashsword/blog").run();
////    }

    public static void main(String[] args) {
        //results will be saved to "/data/webmagic/" in json format
        OOSpider.create(Site.me(), new ConsolePageModelPipeline(), OschinaBlog.class)
                .addUrl("http://my.oschina.net/flashsword/blog").run();
    }

    public String getTitle() {
        return title;
    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public List<String> getTags() {
//        return tags;
//    }
//
//    public Date getDate() {
//        return date;
//    }

}
