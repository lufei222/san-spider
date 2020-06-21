package demo;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 拉钩岗位分页查询
 * 通过webmagic的自带的page.addTargetRequest(nextUrl);
 * @author luosan
 */
public class LagouListPageSpider implements PageProcessor {


    private int visitLimitCount =1;
    private final int VISIT_LIMIT_COUNT_IN_PERIOD = 5;
    static int totalPageNum = 0;

    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(200);


    public static void main(String[] args) {
        String url = "https://www.lagou.com/zhaopin/ceo/1/?filterOption=2&sid=9de38bbecf074f4499e37b5deb050a76";
        //pipline中保存数据，此例中consolepipeline直接将内容打印到控制台。可自己定义
        Spider.create(new LagouListPageSpider())
                .addUrl(url)
                //输出内容到控制台
                .addPipeline(new ConsolePipeline())
                //输出内容到文件
                .addPipeline(new FilePipeline("D:\\webmagic\\lagou"))
                .run();
    }

    @Override
    public void process(Page page) {
        System.out.println("开始解析");
        //从页面发现后续的url地址加入到队列中去
        List<String> urls = page.getHtml().xpath("//li[@class='con_list_item default_list']//div[@class='position']//h3/text()").all();
        Integer curPageNum = Integer.valueOf(page.getHtml().xpath("//a[@class='page_no pager_is_current']/text()").get());
        //只初始化一次，不然不好获取总页数，会比如6页总数变成4条总数。
        if(totalPageNum ==0){
            List<String> all = page.getHtml().xpath("//div[@class='pager_container']/a/allText()").all();
            if(all.size()>1){
                String tmpTotalPageNum =  all.get(all.size()-2);
                totalPageNum = Integer.parseInt(tmpTotalPageNum);
            }
        }
        limitTime(60000L);
        System.out.println("总页数 "+ totalPageNum);

        for (int i = 1; i < totalPageNum; i++) {
            //获取下一页的链接，将当前页数拼接到url上
            String nextUrl = "https://www.lagou.com/zhaopin/ceo/" + (i + 1) + "/?filterOption=3&sid=d1a2ad7c52424738a127db67692dadc3";
            //将下一页链接添加到爬虫队列中
            page.addTargetRequest(nextUrl);
        }
        List<Object> list = new ArrayList<>();
        for (int j = 1; j < urls.size(); j++) {
            List<String> label = page.getHtml().xpath("//li[@class='con_list_item default_list'][" + j + "]//allText()").all();
            list.add(label);
        }
        //将封装的list对象传到pipeline中
        page.putField("拉钩 当前页 "+curPageNum , list);
        curPageNum++;

    }

    @Override
    public Site getSite() {
        return site;
    }


    /****
     * 限流，稍稍等待停顿，比较简单粗暴，没去做更多反爬虫操作，避免一定时间内反爬虫查询返回空
     * @param sleepTime
     */
    private void limitTime(long sleepTime){
        //限流，稍稍等待停顿，比较简单粗暴，没去做更多反爬虫操作，避免一定时间内反爬虫查询返回空
        if(visitLimitCount==VISIT_LIMIT_COUNT_IN_PERIOD){
            try {
                visitLimitCount =1;
                System.out.println("超出爬虫一定时间内限流次数，先等待60秒");
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            visitLimitCount++;
        }
    }
}
