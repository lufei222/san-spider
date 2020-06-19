package example;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * 查询 oschina 博客的文章列表
 * @author luosan
 * @since 0.3.2
 */
//@TargetUrl("http://my.oschina.net/flashsword/blog/\\d+")
public class OschinaBlogProcesserForList implements PageProcessor {


    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @ExtractBy("//div[@class='space-index-container']//div[@class='item blog-item']//div[@class='content']//a[1]//allText()")
    private String title;


    public static void main(String[] args) {
        String url = "http://my.oschina.net/flashsword/blog";
        Spider.create(new OschinaBlogProcesserForList()).addUrl(url).thread(5).run();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void process(Page page) {
        //从页面发现后续的url地址加入到队列中去
        List<String> urls = page.getHtml().xpath("div[@class='space-index-container']//div[@class='item blog-item']//a[@class='header']//text()").all();
        page.addTargetRequests(urls);
        //从详情页面获取数据----文本内容
        Selectable content =page.getHtml().xpath("//div[@id='ivs_content']/html()");//outerHtml()
        //从详情页面获取数据----标题
        Selectable titles =page.getHtml().xpath("//div[@id='ivs_title']/text()");
        //从详情页面获取数据----发布时间
        Selectable datess =page.getHtml().xpath("//div[@id='ivs_title']/small[@class='PBtime']/html()");
        //从详情页面获取数据列表----发布日期
        List<String> dates2 =page.getHtml().xpath("//div[@id='pageList']/ul/li/span/html()").all();
        for(int i=0;i<dates2.size();i++){
            System.out.println("dates2:"+dates2.get(i).toString());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }


}
