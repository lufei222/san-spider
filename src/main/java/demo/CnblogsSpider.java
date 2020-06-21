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
 * HttpClients
 */
public class CnblogsSpider {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();//创建httpClient
        HttpGet httpGet = new HttpGet("http://www.cnblogs.com/");//创建httpget实例

        CloseableHttpResponse response = httpClient.execute(httpGet);//执行get请求
        HttpEntity entity = response.getEntity();//获取返回实体
        String content =  EntityUtils.toString(entity,"utf-8");//网页内容
        response.close();//关闭流和释放系统资源

        Jsoup.parse(content);
        Document doc = Jsoup.parse(content);//解析网页得到文档对象
        Elements elements = doc.getElementsByTag("title");//获取tag是title的所有dom文档
        Element element = elements.get(0);//获取第一个元素
        String title = element.text(); //.html是返回html
        System.out.println("网页标题："+title);
        Element element1 = doc.getElementById("site_nav_top");//获取id=site_nav_top标签
        String str = element1.text();
        System.out.println("str:"+str);
    }
}