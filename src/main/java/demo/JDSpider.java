package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 京东爬虫实例，只展示根据cookie查询个人已登录之后的收获地址
 * 利用京东登录之后获得的cookie，可以实现提交订单功能，
 * 比如口罩抢购，针对验证码之类的则需要具体参照其他的事例，比如看看github最新的jd爬虫
 * 附：获取下单列表：Request URL: https://order.jd.com/lazy/getOrderProductInfo.action
 * 要获取下单列表，则需要素有页面加载完成之后去获取，使用htmlunit或者其他抓取动态页面的方式
 */
public class JDSpider implements PageProcessor {

	private static String JD_USERNAME = "";
	private static String JD_PASSWORD = "";
	private static String JD_COOKIE = System.getenv("JD_COOKIE");
	private static String JD_LOGIN_URL = "https://passport.jd.com/new/login.aspx?ReturnUrl=https%3A%2F%2Fwww.jd.com%2F";

	private Site site = Site
			.me()
			.setRetryTimes(3)
			.setSleepTime(1000)
			.setTimeOut(10000)
//			.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
//			.addHeader("Accept-Encoding"," gzip, deflate, br")
//			.addHeader("Accept-Language","zh-CN,zh;q=0.9")
//			.addHeader("Connection","keep-alive")
//			.addHeader("Cache-Control","max-age=0")
//			//.addHeader("Content-Length","23")
//			.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
            .addHeader("Cookie",JD_COOKIE)
//			.addHeader("Host","gitee.com")
//			.addHeader("Referer","https://gitee.com")
//			.addHeader("User-Agent","-Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Mobile Safari/537.36")
//			.addHeader("Sec-Fetch-Mode","navigate")
//			.addHeader("Sec-Fetch-Site:","same-origin")
//			.addHeader("Sec-Fetch-User","?1\n")
//			.addHeader("Upgrade-Insecure-Requests","1")
			;
	// 用来存储cookie信息
	private Set<Cookie> cookies = new HashSet<>();
 
	@Override
	public void process(Page page) {

		System.out.println("开始解析");
		List<String> addressList = page.getHtml().xpath("//div[@class='prompt-01 prompt-02']//div[@class='pc']//allText()").all();
		System.out.println(addressList);
		page.putField("jd myAddressList ", addressList);

	}
 
	@Override
	public Site getSite() {
 
		// 将获取到的cookie信息添加到webmagic中
		for (Cookie cookie : cookies) {
			site.addCookie(cookie.getName(), cookie.getValue());
		}

		return site;
	}
 
	public void login() {
		// 登陆
		System.setProperty("webdriver.chrome.driver",
				"D:/chromedriver/chromedriver.exe"); // 注册驱动
		WebDriver driver = new ChromeDriver();
 
		driver.get(JD_LOGIN_URL);// 打开网址
 
		// 防止页面未能及时加载出来而设置一段时间延迟
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 设置用户名密码
		driver.findElement(By.id("loginname")).sendKeys(JD_USERNAME); // 用户名
		driver.findElement(By.id("nloginpwd")).sendKeys(JD_PASSWORD); // 密码
		// 模拟点击
		driver.findElement(By.id("loginsubmit"))
				.click(); // xpath语言：id为form-group-login的form下的button

		// 防止页面未能及时加载出来而设置一段时间延迟
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 获取cookie信息
		cookies = driver.manage().getCookies();
		System.out.println("cookie " + cookies);

		driver.close();
		//退出浏览器，关闭所有页面
		driver.quit();

	}
 
	public static void main(String[] args) {
 
		String url = "https://order.jd.com/center/list.action";
		JDSpider dome = new JDSpider();
		Spider.create(dome)
				.addUrl(url)
				//输出内容到控制台
				.addPipeline(new ConsolePipeline())
				//输出内容到文件
				.addPipeline(new FilePipeline("D:\\webmagic\\jd"))
				.run();

	}
}