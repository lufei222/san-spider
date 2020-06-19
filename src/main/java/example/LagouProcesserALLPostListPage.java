package example;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
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
public class LagouProcesserALLPostListPage implements PageProcessor {


    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @ExtractBy("//li[@class='con_list_item default_list']/data-company")
    private String title;


    public static void main(String[] args) {
        testLimitTimeInPeriod2();
    }

    /**
     * 测试一个时间段内的限流次数
     */
    private static void testLimitTimeInPeriod() {
        String url = "https://www.lagou.com/zhaopin/ceo/1/?filterOption=2&sid=9de38bbecf074f4499e37b5deb050a76";
        for(int i=0;i<20;i++){
            Spider.create(new LagouProcesserALLPostListPage()).addUrl(url).thread(5).run();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 测试一个时间段内的限流次数,发现60秒一次去分页查询是很稳定的
     */
    private static void testLimitTimeInPeriod2() {
        String url1 = "https://www.lagou.com/zhaopin/ceo/" ;
        String url2 = "/?filterOption=2&sid=9de38bbecf074f4499e37b5deb050a76";
        int count =1;
        for(int i=0;i<60;i++){
            int j=i%7;
            //只需要1、2、3、4、5、6页
            if(j!=0){
                ArrayList<Object> list = new ArrayList<>();
                Spider.create(new LagouProcesserALLPostListPage()).addUrl(url1+j+url2).thread(5).run();
                try {
                    if(count==5){
                        count=1;
                        System.out.println("*****************等待30秒***************");
                        for(int q=0;q<60;q++){
                            System.out.println("停顿次数计时 " + (q+1));
                            Thread.sleep(1000);
                        }
                    }else{
                        count++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("出来");

        }
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void process(Page page) {
        //从页面发现后续的url地址加入到队列中去
//        List<String> urls = page.getHtml().xpath("//li[@class='con_list_item default_list']/allText()").all();
        List<String> urls = page.getHtml().xpath("//li[@class='con_list_item default_list']//div[@class='position']//h3/text()").all();
//        page.addTargetRequests(urls);
        //从详情页面获取数据----文本内容
        Selectable content =page.getHtml().xpath("//div[@id='ivs_content']/html()");//outerHtml()
        //从详情页面获取数据----标题
        Selectable titles =page.getHtml().xpath("//div[@id='ivs_title']/text()");
        //从详情页面获取数据----发布时间
        Selectable datess =page.getHtml().xpath("//div[@id='ivs_title']/small[@class='PBtime']/html()");
        //从详情页面获取数据列表----发布日期
        List<String> dates2 =page.getHtml().xpath("//div[@id='pageList']/ul/li/span/html()").all();
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
