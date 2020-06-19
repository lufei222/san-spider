package example;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询 oschina 博客的文章列表
 * @author luosan
 * @since 0.3.2
 */
//@TargetUrl("http://my.oschina.net/flashsword/blog/\\d+")
public class LagouProcesserListPage implements PageProcessor {


    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(20000);

    @ExtractBy("//li[@class='con_list_item default_list']/data-company")
    private String title;


    public static void main(String[] args) {
        String url = "https://www.lagou.com/zhaopin/ceo/1/?filterOption=2&sid=9de38bbecf074f4499e37b5deb050a76";
        //pipline中保存数据，此例中consolepipeline直接将内容打印到控制台。可自己定义
        Spider.create(new LagouProcesserListPage()).addUrl(url).addPipeline(new ConsolePipeline()).run();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void process(Page page) {
        System.out.println("我来了");
        //从页面发现后续的url地址加入到队列中去
//        List<String> urls = page.getHtml().xpath("//li[@class='con_list_item default_list']/allText()").all();
        List<String> urls = page.getHtml().xpath("//li[@class='con_list_item default_list']//div[@class='position']//h3/text()").all();
        String pageNum =  page.getHtml().xpath("//div[@class='pager_container']/a[5]/text()").get();
        int pageNumInt = Integer.parseInt(pageNum);
        for (int i = 0; i < pageNumInt; i++) {
//获取下一页的链接，将当前页数拼接到url上
            String nextUrl = "https://www.lagou.com/zhaopin/ceo/"+(i+1)+"/?filterOption=3&sid=d1a2ad7c52424738a127db67692dadc3";
//将下一页链接添加到爬虫队列中
            page.addTargetRequest(nextUrl);
        }
//        page.addTargetRequests(urls);
        //从详情页面获取数据----文本内容
        Selectable content =page.getHtml().xpath("//div[@id='ivs_content']/html()");//outerHtml()
        //从详情页面获取数据----标题
        Selectable titles =page.getHtml().xpath("//div[@id='ivs_title']/text()");
        //从详情页面获取数据----发布时间
        Selectable datess =page.getHtml().xpath("//div[@id='ivs_title']/small[@class='PBtime']/html()");
        //从详情页面获取数据列表----发布日期
        List<String> dates2 =page.getHtml().xpath("//div[@id='pageList']/ul/li/span/html()").all();
//        for(int i=0;i<dates2.size();i++){
//            System.out.println("dates2:"+dates2.get(i).toString());
//        }
        List<Object> list = new ArrayList<>();
        for(int j=0 ; j<urls.size() ;j++ ){
            List<String> label = page.getHtml().xpath("//li[@class='con_list_item default_list'][" +j+ "]//allText()").all();
            list.add(label);
        }
        //将封装的list对象传到pipline中
        page.putField("拉钩", list);
    }

    @Override
    public Site getSite() {
        return site;
    }


}
