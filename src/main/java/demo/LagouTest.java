package demo;

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

/**
 * Author: TaoTao  2019/9/26
 */
public class LagouTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        //直接httpclient爬取拉钩网页，会提示存在恶意访问行为被拦截，
        //testHttpClient();
        //
        testJsoup();
    }
    private static void testJsoup() throws IOException, InterruptedException {
        for(int i=0;i<10;i++) {
            Document doc = Jsoup.connect("https://www.lagou.com/zhaopin/ceo/1/?filterOption=3&sid=f1937baf1115438c9ea9aee62836a985").get();
            Elements newsHeadlines = doc.select(".pager_container");
            System.out.println(newsHeadlines);
            Thread.sleep(2000);
        }
    }


    private static void testHttpClient() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();//创建httpClient
        HttpGet httpGet = new HttpGet("https://www.lagou.com/zhaopin/ceo/1/?filterOption=3&sid=f1937baf1115438c9ea9aee62836a985");//创建httpget实例

        CloseableHttpResponse response = httpClient.execute(httpGet);//执行get请求
        HttpEntity entity = response.getEntity();//获取返回实体
        String content =  EntityUtils.toString(entity,"utf-8");//网页内容
        response.close();//关闭流和释放系统资源

        Jsoup.parse(content);
        Document doc = Jsoup.parse(content);//解析网页得到文档对象
        Elements elements = doc.getElementsByTag("body");//获取tag是title的所有dom文档
        Element element = elements.get(0);//获取第一个元素
        String title = element.text(); //.html是返回html
        System.out.println("网页标题："+title);
        Element element1 = doc.getElementById("tab_pos");//获取id=site_nav_top标签
        String str = element1.text();
        System.out.println("str:"+str);
    }
}