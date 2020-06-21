package demo;

 public class ICBCProduct {
    /**主键*/
    private Long id;
    /**
     * 名称
     */
    private String productName;
    /**
     *预期年化收益率
     */
    private String performanceBanchmark;
    /**
     *起购金额
     */
    private String upPurchaseAmount;
    /**
     *期限
     */
    private String investmentPeriod;
    /**
     * 风险等级
     */
    private String riskClass;
    /**
     * 最近购买开放日
     */
    private String raisingPeriod;
    /**
     * 更新日期
     */
    private String updateTime;


    public Long getId() {
       return id;
    }

    public void setId(Long id) {
       this.id = id;
    }

    public String getProductName() {
       return productName;
    }

    public void setProductName(String productName) {
       this.productName = productName;
    }

    public String getPerformanceBanchmark() {
       return performanceBanchmark;
    }

    public void setPerformanceBanchmark(String performanceBanchmark) {
       this.performanceBanchmark = performanceBanchmark;
    }

    public String getUpPurchaseAmount() {
       return upPurchaseAmount;
    }

    public void setUpPurchaseAmount(String upPurchaseAmount) {
       this.upPurchaseAmount = upPurchaseAmount;
    }

    public String getInvestmentPeriod() {
       return investmentPeriod;
    }

    public void setInvestmentPeriod(String investmentPeriod) {
       this.investmentPeriod = investmentPeriod;
    }

    public String getRiskClass() {
       return riskClass;
    }

    public void setRiskClass(String riskClass) {
       this.riskClass = riskClass;
    }

    public String getRaisingPeriod() {
       return raisingPeriod;
    }

    public void setRaisingPeriod(String raisingPeriod) {
       this.raisingPeriod = raisingPeriod;
    }

    public String getUpdateTime() {
       return updateTime;
    }

    public void setUpdateTime(String updateTime) {
       this.updateTime = updateTime;
    }

    @Override
    public String toString() {
       return "ICBCProduct{" +
               "产品名称='" + productName + '\'' +
               ", 业绩比较基准='" + performanceBanchmark + '\'' +
               ", 起购金额='" + upPurchaseAmount + '\'' +
               ", 期限='" + investmentPeriod + '\'' +
               ", 风险等级='" + riskClass + '\'' +
               ", '" + raisingPeriod + '\'' +
               '}';
    }
 }