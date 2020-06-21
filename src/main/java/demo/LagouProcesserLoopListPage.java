package demo;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 *  爬取拉钩岗位 可以参照思路 https://blog.csdn.net/qq_36251958/article/details/79313035
 *  loop 顾名思义  直接通过for循环去获取每一列的列表数据，
 *  虽然暴力，但是可控，比如可以失败了就线程暂停一点时间后继续重试
 */
//@TargetUrl("http://my.oschina.net/flashsword/blog/\\d+")
public class LagouProcesserLoopListPage implements PageProcessor {

    static List<Object> list = new ArrayList<>();
    static int pageNumInt = 0;
    static int retryNum = 1;

    private static int visitLimitCount =1;
    private static final int VISIT_LIMIT_COUNT_IN_PERIOD = 5;
    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    //设置成睡眠时间达到20秒，那么会基本正确。此外pageNumInt要多设置几次轮回
    //private Site site = Site.me().setRetryTimes(3).setSleepTime(20000);
    //设置成睡眠时间达到20秒，那么会基本正确。此外pageNumInt直接<6也时不时可以，但是经常报错
    //else  直接自带的循环不可行，观测源码才可以看出问题，才能看到返回结果
    //直接把时间缩短到200毫秒，总是第六次刷的时候就获取不到第六页，做了防盗刷处理。除非模拟浏览器请求，或者绕开。难道真的不是爬虫问题？不想尝试了先
    //加上header也是反爬虫没用的
    private Site site = Site.me();



    public static void main(String[] args) {
        String url = "https://www.lagou.com/zhaopin/ceo/1/?filterOption=2&sid=9de38bbecf074f4499e37b5deb050a76";
        //pipline中保存数据，此例中consolepipeline直接将内容打印到控制台。可自己定义
        String url1 = "https://www.lagou.com/zhaopin/ceo/" ;
        String url2 = "/?filterOption=2&sid=9de38bbecf074f4499e37b5deb050a76";
        Spider.create(new LagouProcesserLoopListPage()).addUrl(url).addPipeline(new ConsolePipeline()).run();
        for (int i = 1; i < pageNumInt; i++) {
            int j = i+1;
            System.out.println(j);
            //获取下一页的链接，将当前页数拼接到url上
            limitTime(60000L);
           Spider.create(new LagouProcesserLoopListPage()).addUrl(url1+j+url2).addPipeline(new ConsolePipeline()).run();
           //重置
            list = new ArrayList<>();
        }
    }


    @Override
    public void process(Page page) {

        System.out.println("我来了");
        //从页面发现后续的url地址加入到队列中去
        List<String> urls = page.getHtml().xpath("//li[@class='con_list_item default_list']//div[@class='position']//h3/text()").all();
        String pageNum =  page.getHtml().xpath("//div[@class='pager_container']/a[5]/text()").get();
        //只初始化一次，不然不好获取总页数，会比如6页总数变成4条总数。
        if(pageNumInt ==0){
            pageNumInt = Integer.parseInt(pageNum);
        }
        System.out.println("总页数 "+pageNumInt);

        for(int j=0 ; j<urls.size() ;j++ ){
            List<String> label = page.getHtml().xpath("//li[@class='con_list_item default_list'][" +j+ "]//allText()").all();
            list.add(label);
        }
        List<Object> rmpList = list;
        //将封装的list对象传到pipline中
        page.putField("拉钩", rmpList);

    }

    @Override
    public Site getSite() {
        return site;
    }


    /**
     * 限流，稍稍等待停顿，比较简单粗暴，没去做更多反爬虫操作，避免一定时间内反爬虫查询返回空
     * @param sleepTime
     */
    private static void limitTime(long sleepTime){
        //限流，稍稍等待停顿，比较简单粗暴，没去做更多反爬虫操作，避免一定时间内反爬虫查询返回空
        if(visitLimitCount==VISIT_LIMIT_COUNT_IN_PERIOD){
            try {
                visitLimitCount =1;
                System.out.println("超出爬虫一定时间内限流次数，先等待"+(sleepTime/1000)+"秒");
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            visitLimitCount++;
        }
    }
}
