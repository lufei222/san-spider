package demo;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * 拉钩多种不同方式实现的爬虫
 * 测试htmlunit、jsoup、httpclient直接爬取
 */
public class LagouMulSpider {

    static Random random = new Random();
    private static String LAGOU_URL = "https://www.lagou.com/zhaopin/ceo/1/?filterOption=3&sid=f1937baf1115438c9ea9aee62836a985";


    public static void main(String[] args) throws IOException, InterruptedException {
        //直接httpclient爬取拉钩网页，会提示存在恶意访问行为被拦截，
        testHttpClient();
        testJsoup();
        testHtmlunitLagou();
        testHtmlunitBaidu();
    }
    private static void testJsoup() throws IOException, InterruptedException {
        System.out.println("************************testJsoup************************");
        for(int i=0;i<2;i++) {
            Document doc = Jsoup.connect(LAGOU_URL).get();
            Elements newsHeadlines = doc.select(".pager_container");
            System.out.println(newsHeadlines);
            Thread.sleep(200);
        }
    }


    private static void testHttpClient() {
        try {
            System.out.println("************************testHttpClient************************");
            CloseableHttpClient httpClient = HttpClients.createDefault();//创建httpClient
            HttpGet httpGet = new HttpGet(LAGOU_URL);//创建httpget实例

            CloseableHttpResponse response = httpClient.execute(httpGet);//执行get请求
            HttpEntity entity = response.getEntity();//获取返回实体
            String content = EntityUtils.toString(entity, "utf-8");//网页内容
            response.close();//关闭流和释放系统资源

            Jsoup.parse(content);
            Document doc = Jsoup.parse(content);//解析网页得到文档对象
            Elements elements = doc.getElementsByTag("title");//获取tag是title的所有dom文档
        }catch (Exception e){
            System.out.println(e.getCause());
        }
    }

    private static BrowserVersion getRandomBrowserVersion(){
        int i = random.nextInt(5);
        BrowserVersion browserVersion = BrowserVersion.getDefault();
        switch (i){
            case 1:  browserVersion = BrowserVersion.CHROME  ;break;
            case 2:  browserVersion = BrowserVersion.FIREFOX  ;break;
            case 3:  browserVersion = BrowserVersion.FIREFOX_68  ;break;
            case 4:  browserVersion = BrowserVersion.BEST_SUPPORTED  ;break;
            case 5:  browserVersion = BrowserVersion.INTERNET_EXPLORER  ;break;
            default: ;
        }
        return browserVersion;
    }

    /**
     * 测试循环获取拉钩的页面是否也是五次限制，还是真的能像浏览器一样正常访问.发现其实是一样限流了，超过32秒才能继续访问
     * @throws IOException
     * @throws InterruptedException
     */
    private static void testHtmlunitLagou() throws IOException, InterruptedException {
        System.out.println("************************testHtmlunitLagou************************");
        for(int i=0;i<2;i++){
            //创建一个webclient
            WebClient webClient = new WebClient(getRandomBrowserVersion());
            //htmlunit 对css和javascript的支持不好，所以请关闭之
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setCssEnabled(false);
            //获取页面
            HtmlPage page = webClient.getPage(LAGOU_URL);
            List<Object> byXPath = page.getByXPath("//div[@class='pager_container']//text()");
            System.out.println(byXPath);
            //关闭webclient
            webClient.close();
        }
    }
    private static void testHtmlunitBaidu() throws IOException {
        System.out.println("************************testHtmlunitBaidu************************");
        String str;
        //创建一个webclient
        WebClient webClient = new WebClient();
        //htmlunit 对css和javascript的支持不好，所以请关闭之
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        //获取页面
        HtmlPage page = webClient.getPage("http://www.baidu.com/");
        //获取页面的TITLE
        str = page.getTitleText();
        System.out.println(str);
//        //获取页面的XML代码
//        str = page.asXml();
//        System.out.println(str);
//        //获取页面的文本
//        str = page.asText();
//        System.out.println(str);
        //关闭webclient
        webClient.close();
    }
}