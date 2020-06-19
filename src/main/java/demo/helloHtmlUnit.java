package demo;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlBody;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class helloHtmlUnit{

    static Random random = new Random();

    public static void main(String[] args) throws Exception{
        //测试htmlunit是否可用
        //test1();
        //测试循环获取拉钩的页面是否也是五次限制，还是真的能像浏览器一样正常访问.发现其实是一样限流了，超过32秒才能继续访问
        test2();

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
    private static void test2() throws IOException, InterruptedException {
        for(int i=0;i<60;i++){
            String str;
            //创建一个webclient
//            WebClient webClient = new WebClient();
            WebClient webClient = new WebClient(getRandomBrowserVersion());
            //htmlunit 对css和javascript的支持不好，所以请关闭之
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setCssEnabled(false);
            //获取页面
            HtmlPage page = webClient.getPage("https://www.lagou.com/zhaopin/ceo/1/?filterOption=3&sid=f1937baf1115438c9ea9aee62836a985");
            //获取页面的TITLE
//            str = page.getTitleText();
//            System.out.println(str);
//            //获取页面的XML代码
//            str = page.asXml();
//            System.out.println(str);
//            //获取页面的文本
//            str = page.asText();
//            System.out.println(str);
            List<Object> byXPath = page.getByXPath("//div[@class='pager_container']//text()");
            System.out.println(byXPath);
            //关闭webclient
            webClient.close();
            Thread.sleep(3000);
        }
    }
    private static void  test1() throws IOException {
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
        //获取页面的XML代码
        str = page.asXml();
        System.out.println(str);
        //获取页面的文本
        str = page.asText();
        System.out.println(str);
        //关闭webclient
        webClient.close();
    }
}