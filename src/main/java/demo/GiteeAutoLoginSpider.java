package demo;
 
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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


/**
 * gitee自动化登录获取私有项目
 */
public class GiteeAutoLoginSpider implements PageProcessor {

	private static String GITEE_NAME = System.getenv("GITEE_NAME");
	private static String GITEE_USERNAME = System.getenv("GITEE_USERNAME");
	private static String GITEE_PASSWORD = System.getenv("GITEE_PASSWORD");
	private static String GITEE_URL = "https://gitee.com/login";


	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

	// 用来存储cookie信息
	private Set<Cookie> cookies = new HashSet<>();

	@Override
	public Site getSite() {
		// 将获取到的cookie信息添加到webmagic中
		for (Cookie cookie : cookies) {
			site.addCookie(cookie.getName(), cookie.getValue());
		}
		return site;
	}

	/**
	 * 解析网页节点具体业务逻辑
	 * @param page
	 */
	@Override
	public void process(Page page) {

		System.out.println("开始解析");
		String  tabName = page.getHtml().xpath("//a[@class='item f-bold']//allText()").get();
		System.out.println(tabName);
		List<String> projects = page.getHtml().xpath("//span[@class='project-title']//allText()").all();
		List<String> privateProject = projects.stream().filter(x -> x.contains("san")).distinct().collect(Collectors.toList());
		System.out.println(privateProject);
		page.putField("gitee project ", privateProject);

	}
 


    /**
	 * 登录获取cookie的操作
	 *
     * 使用selenium+chromedriver驱动完成自动登录gitee获取cookie的操作
     * 对于大多数网站可以直接获得cookie
     * 对于大型的验证比较多的网站，会比较麻烦，建议可以百度 或者 github参照其他项目的selenium自动登录实现
     * 在自动登录实现不可行的时候，更快的方式是直接浏览器登录手动复制cookie，以便后续登录之后的操作继续正常进行
     */
	public void login() {
		// 登陆
		System.setProperty("webdriver.chrome.driver", "D:/chromedriver/chromedriver.exe"); // 注册驱动
		WebDriver driver = new ChromeDriver();
		driver.get(GITEE_URL);// 打开网址
		// 防止页面未能及时加载出来而设置一段时间延迟
		try {
			Thread.sleep(1000);
			// 设置用户名密码
			driver.findElement(By.id("user_login")).sendKeys(GITEE_USERNAME); // 用户名
			driver.findElement(By.id("user_password")).sendKeys(GITEE_PASSWORD); // 密码
			// 模拟点击
			driver.findElement(By.name("commit")).click();
			// 防止页面未能及时加载出来而设置一段时间延迟
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 获取cookie信息
		cookies = driver.manage().getCookies();
		System.out.println("cookie " + cookies);

		driver.close();

	}
 
	public static void main(String[] args) {
 
		String url = "https://gitee.com/"+GITEE_NAME+"/dashboard/projects?scope=private&&sort="; // 地址
		GiteeAutoLoginSpider dome = new GiteeAutoLoginSpider();
        // 登陆
		dome.login();
		Spider.create(dome)
				.addUrl(url)
				//输出内容到控制台
				.addPipeline(new ConsolePipeline())
				//输出内容到文件
				.addPipeline(new FilePipeline("D:\\webmagic\\gitee"))
				.run();

	}
}