package demo;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.openqa.selenium.Cookie;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import util.NotifyUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static util.NotifyUtil.outletsNotify;

/**
 * 什么值得买爬取列表页面优惠产品信息
 *
 */
public class SMZDMSpider implements PageProcessor {
	private Site site = Site
			.me()
			.setRetryTimes(3)
			.setSleepTime(1000)
			.setTimeOut(10000);

	// 用来存储cookie信息
	private Set<Cookie> cookies = new HashSet<>();
 
	@Override
	public void process(Page page) {

		System.out.println("爬取开始");
		List<String> listNodes = page.getHtml().xpath("//ul[@id='feed-main-list']/").all();
		ArrayList<String > productList = new ArrayList<>();
		for(int i=0; i < listNodes.size(); i++) {
			Elements select = Jsoup.parse(String.valueOf(listNodes.get(i))).select(".feed-block-title a");
			if(select!=null && select.size()>1){
				String productInfo = select.get(0).text();
				String productPriceInfo  = select.get(1).text();
				productList.add(productInfo+ " -> " +productPriceInfo);
			}
		}
		System.out.println(productList);
		page.putField("SMZDM " , productList);
		//降价消息通知
		NotifyUtil.outletsNotify();

	}
 
	@Override
	public Site getSite() {
		return site;
	}


 
	public static void main(String[] args) throws UnsupportedEncodingException {

		String keywordEn = URLEncoder.encode("牛奶", "utf-8");
		String url = "https://search.smzdm.com/?c=home&s="+keywordEn+"&v=b"; // 地址
		SMZDMSpider dome = new SMZDMSpider();
		Spider.create(dome)
				.addUrl(url)
				//输出内容到控制台
				.addPipeline(new ConsolePipeline())
				//输出内容到文件
				.addPipeline(new FilePipeline("D:\\webmagic\\jd"))
				.run();
	}


}