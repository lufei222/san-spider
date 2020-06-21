package demo;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.openqa.selenium.Cookie;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ZMZDMSpider implements PageProcessor {
	private Site site = Site
			.me()
			.setRetryTimes(3)
			.setSleepTime(1000)
			.setTimeOut(10000);

	// 用来存储cookie信息
	private Set<Cookie> cookies = new HashSet<>();
 
	@Override
	public void process(Page page) {

		System.out.println("come");
		Html html = page.getHtml();
//		Jsoup.parse(String.valueOf(html)).select("");
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
//		List<String> productsInfo = page.getHtml().xpath("//h5[@class='feed-block-title']/a[1]/text()").all();
//		List<String> productsPriceInfo = page.getHtml().xpath("//h5[@class='feed-block-title']/a[2]/div/text()").all();
//		page.getHtml();
//		ArrayList<String> prices = new ArrayList<>();
//		for(int i=0;i<productsInfo.size();i++){
//			String s = productsInfo.get(i) + productsPriceInfo.get(i);
//			prices.add(s);
//		}
		System.out.println(productList);
	}
 
	@Override
	public Site getSite() {
		return site;
	}

	/**
	 * 降价提醒声音通知
	 */
	private static void outletsNotify() {
		try {
			FileInputStream fileInputStream = new FileInputStream("a.wav");
			InputStream musicStream = ZMZDMSpider.class.getResourceAsStream("a.wav");
			AudioStream as = new AudioStream(musicStream);
			AudioPlayer.player.start(as);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
 
	public static void main(String[] args) throws UnsupportedEncodingException {

		String keywordEn = URLEncoder.encode("牛奶", "utf-8");
		String url = "https://search.smzdm.com/?c=home&s="+keywordEn+"&v=b"; // 地址
		ZMZDMSpider dome = new ZMZDMSpider();
		Spider.create(dome).addUrl(url).addPipeline(new ConsolePipeline()).run();
	}


}