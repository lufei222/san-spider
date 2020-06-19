package example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 *私行推荐理财
 * https://segmentfault.com/a/1190000020005655?utm_source=tag-newest
 *
 */
public class ICBCFinanceSpiderListPage implements PageProcessor {
    private Logger log = LoggerFactory.getLogger(ICBCFinanceSpiderListPage.class);
    // 定义连接失败时，重试机制
    private static Site site = Site.me().setRetryTimes(1).setSleepTime(100);

    public Site getSite() {
        return site;
    }

    static int currentNum = 1;

    @Override
    public void process(Page page) {
        System.out.print("当前页->" +currentNum+ "，当前页条数->");
        String pageNum = page.getHtml().xpath("//*[@id=pageturn]/ul/li[3]/span[2]/b/text()").get();
        int pageNumInt = Integer.parseInt(pageNum);
        for (int i = 1; i < pageNumInt; i++) {
//获取下一页的链接，将当前页数拼接到url上
            String nextUrl = "https://mybank.icbc.com.cn/servlet/ICBCBaseReqServletNoSession?dse_operationName=per_FinanceCurProListP3NSOp&p3bank_error_backid=120103&pageFlag=0&menuLabel=10$17$TJ&Area_code=0200&requestChannel=302&nowPageNum_turn=" + (i + 1);
//将下一页链接添加到爬虫队列中
            page.addTargetRequest(nextUrl);
        }
        List<Selectable> nodes = page.getHtml().xpath("//*[@id=datatableModel]/div").nodes(); //获取列表的条数
        int length = nodes.size();
        System.out.println(length);
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < length - 2; i++) {
            ICBCProduct privateRecommend = new ICBCProduct();
//根据xpath获取对应节点的内容
            privateRecommend.setProductName(page.getHtml().xpath("/html/body/div[1]/div[1]/div[3]/div[" + (i + 2) + "]/div[2]/div[1]/span[1]/span[1]/a/text()").get());
            privateRecommend.setPerformanceBanchmark(page.getHtml().xpath("//*[@id=doublelabel1_" + i + "-content]/text()").get());
            privateRecommend.setUpPurchaseAmount(page.getHtml().xpath("//*[@id=doublelabel2_" + i + "-content]/b/text()").get());
            privateRecommend.setInvestmentPeriod(page.getHtml().xpath("//*[@id=doublelabel2_" + i + "-content]/b/text()").get());
            privateRecommend.setRiskClass(page.getHtml().xpath("//*[@id=tt" + i + "-content]/text()").get());
            privateRecommend.setRaisingPeriod(page.getHtml().xpath("/html/body/div[1]/div[1]/div[3]/div[" + (i + 2) + "]/div[2]/div[1]/span[2]/span/text()").get());
            list.add(privateRecommend);
        }
//将封装的list对象传到pipline中
        page.putField("ICBCProduct", list);
        currentNum++;
    }

    //执行main方法
    public static void main(String[] args) {
        System.out.println("ICBC私行推荐理财爬虫开始 **********");
        Spider.create(new ICBCFinanceSpiderListPage()).addUrl("https://mybank.icbc.com.cn/servlet/ICBCBaseReqServletNoSession?dse_operationName=per_FinanceCurProListP3NSOp&p3bank_error_backid=120103&pageFlag=0&menuLabel=10$17$TJ&Area_code=0200&requestChannel=302")
                .addPipeline(new ConsolePipeline())//pipline中保存数据，此例中consolepipeline直接将内容打印到控制台。可自己定义
                .addPipeline(new FilePipeline("D:\\webmagic\\"))
                .run();
        System.out.print("ICBC私行推荐理财爬虫执行完毕 **********");
    }
}