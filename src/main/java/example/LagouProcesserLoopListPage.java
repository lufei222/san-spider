package example;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 *  爬取拉钩岗位
 *
 爬取拉钩岗位 过时了  但是可以参照思路
 https://blog.csdn.net/qq_36251958/article/details/79313035

 * @author luosan
 * @since 0.3.2
 */
//@TargetUrl("http://my.oschina.net/flashsword/blog/\\d+")
public class LagouProcesserLoopListPage implements PageProcessor {


    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    //设置成睡眠时间达到20秒，那么会基本正确。此外pageNumInt要多设置几次轮回
    //private Site site = Site.me().setRetryTimes(3).setSleepTime(20000);
    //设置成睡眠时间达到20秒，那么会基本正确。此外pageNumInt直接<6也时不时可以，但是经常报错
    //else  直接自带的循环不可行，观测源码才可以看出问题，才能看到返回结果
    //直接把时间缩短到200毫秒，总是第六次刷的时候就获取不到第六页，做了防盗刷处理。除非模拟浏览器请求，或者绕开。难道真的不是爬虫问题？不想尝试了先
    //加上header也是反爬虫没用的
    private Site site = Site.me()
            .setDomain("lagou.com")
            .setRetryTimes(3)
            .setSleepTime(200)
            .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
            .addHeader("Accept-Encoding"," gzip, deflate, br")
            .addHeader("Accept-Language","zh-CN,zh;q=0.9,zh-TW;q=0.8")
            .addHeader("Connection","keep-alive")
            //.addHeader("Content-Length","23")
            .addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
//            .addHeader("Cookie","user_trace_token=20200608203542-57587a30-2662-4440-9929-a12ee74db696; JSESSIONID=ABAAAECAAEBABII66D2B7E8CF21AEB10E64C22ECAE89FE4; WEBTJ-ID=20200616142927-172bbd17aaf3e6-076810211f817e-f7d123e-1049088-172bbd17ab0549; LGUID=20200616142927-205b7ecb-1cd3-4402-93b4-fd1b20a864eb; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2217293edf2a43ca-00ff501a44dda4-f7d123e-1049088-17293edf2a51a3%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%2C%22%24os%22%3A%22Windows%22%2C%22%24browser%22%3A%22Chrome%22%2C%22%24browser_version%22%3A%2283.0.4103.97%22%2C%22%24latest_referrer_host%22%3A%22%22%7D%2C%22%24device_id%22%3A%2217293edf2a43ca-00ff501a44dda4-f7d123e-1049088-17293edf2a51a3%22%7D; _ga=GA1.2.409144016.1592288995; Hm_lvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1592288995; _gid=GA1.2.1717401262.1592288995; RECOMMEND_TIP=true; index_location_city=%E5%85%A8%E5%9B%BD; TG-TRACK-CODE=index_navigation; X_MIDDLE_TOKEN=c4813811720ab06d6019e47f06ff3db4; SEARCH_ID=5d777042750b4d1cb00ef80adc68cc39; X_HTTP_TOKEN=35c85c07b2c4eb1f63765329513d62cefaf484bb5d; Hm_lpvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1592356737; LGRID=20200617091856-de5eb385-0169-4043-8dc6-6b2bc5885353")
            .addHeader("Host","www.lagou.com")
            .addHeader("Origin","https://www.lagou.com")
            .addHeader("Referer","https://www.lagou.com/zhaopin/ceo/4/?filterOption=3&sid=6b6a5bca9d334c498ca39f71b4cacab4")
            .addHeader("User-Agent","-Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Mobile Safari/537.36")
            .addHeader("X-Anit-Forge-Code","0")
            .addHeader("X-Anit-Forge-Token","None")
            .addHeader("X-Requested-With","XMLHttpRequest")
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");;

    @ExtractBy("//li[@class='con_list_item default_list']/data-company")
    private String title;

    static List<Object> list = new ArrayList<>();
    static int pageNumInt = 0;
    static int retryNum = 1;

    public static void main(String[] args) {
        String url = "https://www.lagou.com/zhaopin/ceo/1/?filterOption=2&sid=9de38bbecf074f4499e37b5deb050a76";
        //pipline中保存数据，此例中consolepipeline直接将内容打印到控制台。可自己定义
        System.out.println(1);
        Spider.create(new LagouProcesserLoopListPage()).addUrl(url).addPipeline(new ConsolePipeline()).run();
        for (int i = 1; i < pageNumInt; i++) {
            int j = i+1;
            System.out.println(j);
            //获取下一页的链接，将当前页数拼接到url上
            String nextUrl = "https://www.lagou.com/zhaopin/ceo/"+j+"/?filterOption=3&sid=d1a2ad7c52424738a127db67692dadc3";
            //将下一页链接添加到爬虫队列中
            try {

                if(j==6){
//                    Thread.sleep(3000);
                    System.out.println();
                }
//                Spider.create(new LagouProcesserLoopListPage()).addUrl(nextUrl).addPipeline(new ConsolePipeline()).run();
                    for(int z=0;z<retryNum;z++){
                        Spider.create(new LagouProcesserLoopListPage()).addUrl(nextUrl).addPipeline(new ConsolePipeline()).run();
                        if (list.size() > 0 ){
                            break;
                        }
                    }
                list = new ArrayList<>();

            }catch (Exception e){
                System.out.println("fsfsfsfs");
            }
        }
    }

    public String getTitle() {
        return title;
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


}
