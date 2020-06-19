package example;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

public class TestGetListDemo implements PageProcessor {
 
    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
    
    @Override
    public void process(Page page) {
      //从页面发现后续的url地址加入到队列中去
      List<String> urls = page.getHtml().xpath("//div[@id='pageList']/ul/li/a").links().regex(".*html.*").all();     
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
     
    public static void main( String[] args ) {
        String url = "http://www.shanghai.gov.cn/nw2/nw2314/nw2319/nw39197/index.html";
        Spider.create(new TestGetListDemo()).addUrl(url).thread(5).run();
    }
 
}