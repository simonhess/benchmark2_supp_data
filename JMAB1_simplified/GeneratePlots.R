
library('R.devices')

################# INVESTMENT

plotFormat<-c("svg","pdf")
plotRatio<-2/3
#devOptions("*", path=folder)

cFirmInvestments <- read.csv(paste(getwd(),"/data/MICRO_INVESTMENT_EXPERIMENT_JMAB1_cFirmInvestment.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
cFirmResCap <- read.csv(paste(getwd(),"/data/MICRO_INVESTMENT_EXPERIMENT_JMAB1_cResidualCap.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)

kSales_JMAB1 <- read.csv(paste(getwd(),"/data/MACRO_INVESTMENT_EXPERIMENT_JMAB1_kSales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)

kSales_JMAB2 <- read.csv(paste(getwd(),"/data/MACRO_INVESTMENT_EXPERIMENT_JMAB2_kSales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)

devEval(plotFormat, name="RealInvestmentDebugging", width=1008/72+0.5, height=335/72, {
  par(las=1)
  par(mfrow=c(1,2))
  
  par(mar = c(5.1, 5.1, 4.1, 2.1))

  matplot(cFirmInvestments[1:50, 3],col = "blue", type="l", lwd = 2, ylab="",main="Real Investment and Residual Capacity of one C Firm (Two in total)") # Create first plot
  mtext("(a)", side=1, line=3.85, cex=2.5)
  par(new = TRUE)                             # Add new plot
  matplot(cFirmResCap[1:50, 3], col = "red", lty=2, lwd = 2, type="l",             # Create second plot without axes
          axes = FALSE, xlab = "", ylab = "", ylim=c(18940,19060),)
  axis(side = 4, at = pretty(range(cFirmResCap[1:50, 3])))      # Add second axis
  
  legend("topleft", legend=c("Real Investment (Left axis)", "Residual Capacity (Right axis)"),
         col=c("blue", "red"), lty=c(1,2), cex=1.0, lwd = 2, bg="white")
  #plot.new()
  plot(kSales_JMAB1[1:1000, 2],lwd=2,type = "l",lty=1, col="blue",xlab="", ylab="", main="Real (Aggregated) Investment of one C Firm (One in total)")
  lines(kSales_JMAB2[1:1000, 2],col=2, lty=2, lwd = 2)
  legend("topleft", legend=c("JMAB 1.0", "JMAB 2.0"),
         col=c("blue", "red"), lty=c(1,2), cex=1.0, lwd = 2,bg="white")
  currmar <- par()$mar
  mtext("(b)", side=1, line=3.85, cex=2.5)
})


############# PRICING COMPARISON

avMarkup_JMAB1.0 <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_JMAB1_avMarkup.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
avMarkup_JMAB2.0 <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_JMAB2_avMarkup.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
avMarkup_Assenza5BuyersAlwaysSwitching <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_ASSENZA_avMarkup.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
avMarkup_Assenza5Buyers0.05Switching <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_ASSENZA_SWITCHING_avMarkup.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
avMarkup_Len5BuyersAlwaysSwitching <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_DAWID_GATTI_avMarkup.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
avMarkup_Len5Buyers0.05Switching <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_DAWID_GATTI_SWITCHING_avMarkup.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)

cFirmRealSales_JMAB1.0 <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_JMAB1_sales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
cFirmRealSales_JMAB2.0 <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_JMAB2_sales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
cFirmRealSales_Assenza5BuyersAlwaysSwitching <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_ASSENZA_sales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
cFirmRealSales_Assenza5Buyers0.05Switching <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_ASSENZA_SWITCHING_sales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
cFirmRealSales_Len5BuyersAlwaysSwitching <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_DAWID_GATTI_sales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
cFirmRealSales_Len5Buyers0.05Switching <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_DAWID_GATTI_SWITCHING_sales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)

cFirmExpRealSales_JMAB1.0 <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_JMAB1_cFirmExpRealSales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
cFirmExpRealSales_JMAB2.0 <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_JMAB2_cFirmExpRealSales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
cFirmExpRealSales_Assenza5BuyersAlwaysSwitching <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_ASSENZA_cFirmExpRealSales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
cFirmExpRealSales_Assenza5Buyers0.05Switching <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_ASSENZA_SWITCHING_cFirmExpRealSales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
cFirmExpRealSales_Len5BuyersAlwaysSwitching <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_DAWID_GATTI_cFirmExpRealSales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
cFirmExpRealSales_Len5Buyers0.05Switching <- read.csv(paste(getwd(),"/data/PRICING_COMPARISON_DAWID_GATTI_SWITCHING_cFirmExpRealSales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)

plotFormat<-c("svg","pdf")
plotRatio<-2/3

devEval(plotFormat, name="avMarkups", aspectRatio=plotRatio, {
  par(las=1)
  matplot(avMarkup_JMAB1.0[1:1000, 2],type = "l",lty=1, col=seq(1,21),xlab="", ylab="", main="Average Markup", ylim=c(0.09,0.2))
  lines(avMarkup_JMAB2.0[1:1000, 2],col=2)
  lines(avMarkup_Assenza5BuyersAlwaysSwitching[1:1000, 2],col=3)
  lines(avMarkup_Assenza5Buyers0.05Switching[1:1000, 2],col=4)
  lines(avMarkup_Len5BuyersAlwaysSwitching[1:1000, 2],col=5)
  lines(avMarkup_Len5Buyers0.05Switching[1:1000, 2],col=6)
  legend("topleft", legend=c(expression(paste("JMAB 1.0 (", italic("PS"), " = 0.05)")), expression(paste("JMAB 2.0 (", italic("PS"), " = 0.05)")), "Assenza et al. (Default)", expression(paste("Assenza et al. (", italic("PS"), " = 0.05)")), "Dawid and Gatti (Default)", expression(paste("Dawid and Gatti (", italic("PS"), " = 0.05)"))),
         col=c(1, 2,3,4,5,6), lty=1, cex=0.8)
})

devEval(plotFormat, name="priceQuantityMechanismComparison", aspectRatio=plotRatio, {
  par(las=1)
  par(mar = c(2.25, 4, 2, 2))
  layout(matrix(c(1,2,3,4,5,6,7,8,9,10,11,12), 4, 3, byrow = TRUE))
  
  mainTextSize = 0.9
  
  matplot(cFirmRealSales_JMAB1.0[1:1000, 2:11],type = "l", cex.main=mainTextSize, cex.axis=0.9, lty=1, col=seq(1,21),xlab="", ylab="", main=expression(bold(paste("JMAB 1.0 (", bolditalic("PS"), " = 0.5) Sales"))))
  matplot(cFirmRealSales_JMAB2.0[1:1000, 2:11],type = "l", cex.main=mainTextSize, cex.axis=0.9, lty=1, col=seq(1,21),xlab="", ylab="", main=expression(bold(paste("JMAB 2.0 (", bolditalic("PS"), " = 0.5) Sales"))))
  matplot(cFirmRealSales_Assenza5BuyersAlwaysSwitching[1:1000, 2:11],cex.main=mainTextSize, cex.axis=0.9,type = "l",lty=1, col=seq(1,21),xlab="", ylab="", main="Assenza et al. (Default) Sales")
  
  matplot(cFirmExpRealSales_JMAB1.0[1:1000, 2:11],cex.main=mainTextSize, cex.axis=0.9,type = "l",lty=1, col=seq(1,21),xlab="", ylab="", main=expression(bold(paste("JMAB 1.0 (", bolditalic("PS"), " = 0.5) Exp. Sales"))))
  matplot(cFirmExpRealSales_JMAB2.0[1:1000, 2:11],cex.main=mainTextSize, cex.axis=0.9,type = "l",lty=1, col=seq(1,21),xlab="", ylab="", main=expression(bold(paste("JMAB 2.0 (", bolditalic("PS"), " = 0.5) Exp. Sales"))))
  matplot(cFirmExpRealSales_Assenza5BuyersAlwaysSwitching[1:1000, 2:11],cex.main=mainTextSize, cex.axis=0.9,type = "l",lty=1, col=seq(1,21),xlab="", ylab="", main="Assenza et al. (Default) Exp. Sales")
  
  matplot(cFirmRealSales_Assenza5Buyers0.05Switching[1:1000, 2:11],cex.main=mainTextSize, cex.axis=0.9,type = "l",lty=1, col=seq(1,21),xlab="", ylab="", main=expression(bold(paste("Assenza et al. (", bolditalic("PS"), " = 0.5) Sales"))))
  matplot(cFirmRealSales_Len5BuyersAlwaysSwitching[1:1000, 2:11],cex.main=mainTextSize, cex.axis=0.9,type = "l",lty=1, col=seq(1,21),xlab="", ylab="", main="Dawid and Gatti (Default) Sales")
  matplot(cFirmRealSales_Len5Buyers0.05Switching[1:1000, 2:11],cex.main=mainTextSize, cex.axis=0.9,type = "l",lty=1, col=seq(1,21),xlab="", ylab="", main=expression(bold(paste("Dawid and Gatti (", bolditalic("PS"), " = 0.5) Sales"))))
  
  matplot(cFirmExpRealSales_Assenza5Buyers0.05Switching[1:1000, 2:11],cex.main=mainTextSize, cex.axis=0.9,type = "l",lty=1, col=seq(1,21),xlab="", ylab="", main=expression(bold(paste("Assenza et al. (", bolditalic("PS"), " = 0.5) Exp. Sales"))))
  matplot(cFirmExpRealSales_Len5BuyersAlwaysSwitching[1:1000, 2:11],cex.main=mainTextSize, cex.axis=0.9,type = "l",lty=1, col=seq(1,21),xlab="", ylab="", main="Dawid and Gatti (Default) Exp. Sales")
  matplot(cFirmExpRealSales_Len5Buyers0.05Switching[1:1000, 2:11],cex.main=mainTextSize, cex.axis=0.9,type = "l",lty=1, col=seq(1,21),xlab="", ylab="", main=expression(bold(paste("Dawid and Gatti (", bolditalic("PS"), " = 0.5) Exp. Sales"))))
  
})


################################# SALES EXPECTATION DEBUGGING

sales_JMAB2.0 <- read.csv(paste(getwd(),"/data/EXP_DEBUGGING_JMAB2_sales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
sales_JMAB1.0 <- read.csv(paste(getwd(),"/data/EXP_DEBUGGING_JMAB1_sales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
sales_JMAB1.0_0.5_Switching <- read.csv(paste(getwd(),"/data/EXP_DEBUGGING_JMAB1_PS_0_5_sales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
sales_JMAB1.0_1.0_InvRatio <- read.csv(paste(getwd(),"/data/EXP_DEBUGGING_JMAB1_V_1_sales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)

expSales_JMAB2.0 <- read.csv(paste(getwd(),"/data/EXP_DEBUGGING_JMAB2_cFirmExpRealSales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
expSales_JMAB1.0 <- read.csv(paste(getwd(),"/data/EXP_DEBUGGING_JMAB1_cFirmExpRealSales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
expSales_JMAB1.0_0.5_Switching <- read.csv(paste(getwd(),"/data/EXP_DEBUGGING_JMAB1_PS_0_5_cFirmExpRealSales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)
expSales_JMAB1.0_1.0_InvRatio <- read.csv(paste(getwd(),"/data/EXP_DEBUGGING_JMAB1_V_1_cFirmExpRealSales.csv",sep = ""), header=FALSE, stringsAsFactors=FALSE)

JMAB1.0_data<-ifelse(sales_JMAB1.0[,2:101]>expSales_JMAB1.0[,2:101],1,0)
JMAB2.0_data<-ifelse(sales_JMAB2.0[,2:101]>expSales_JMAB2.0[,2:101],1,0)
JMAB1.0_0.5_Switching_data<-ifelse(sales_JMAB1.0_0.5_Switching[,2:101]>expSales_JMAB1.0_0.5_Switching[,2:101],1,0)
JMAB1.0_1.0_InvRatio_data<-ifelse(sales_JMAB1.0_1.0_InvRatio[,2:101]>expSales_JMAB1.0_1.0_InvRatio[,2:101],1,0)

plotFormat<-c("svg","pdf")
plotRatio<-2/3

meanFrequencySalesGreaterExp <-c(mean(rowSums(JMAB1.0_data)),mean(rowSums(JMAB1.0_0.5_Switching_data)),mean(rowSums(JMAB1.0_1.0_InvRatio_data)),mean(rowSums(JMAB2.0_data)))

lbls <- c(expression(atop("JMAB 1.0","")),expression(atop("JMAB 1.0",paste(italic('PS')," = 0.5"))),
          expression(atop("JMAB 1.0",paste(italic('v')," = 1.0"))), expression(atop("JMAB 2.0","")))

devEval(plotFormat, name="AvFrequencyCFirmsSalesGreaterExp", aspectRatio=1, {
  par(las=1)
  par(mar = c(5.1, 4.1, 4.1, 2.1) )
  p <- barplot(meanFrequencySalesGreaterExp,ylim = c(-0,100), names.arg = c("","","",""), las=1, main="Average Frequency C Firms with Sales > Expectation")
  abline(h=c(50),col="black", lty=2)
  text(x = p, y = meanFrequencySalesGreaterExp + 8, labels = meanFrequencySalesGreaterExp)
  axis(1,at=p, lbls,
       line=2, lwd=0, pos=+50-(50*1.1))
  box()
  print(par('usr'))
})


devEval(plotFormat, name="JMAB1.0_SW0.5_Sales", aspectRatio=1, {
  par(las=1)
  matplot(sales_JMAB1.0_0.5_Switching[1:100, 2],type = "l",lty=1, lwd = 2, col="blue",xlab="", ylab="", main=expression(paste("Sales of one C Firm in JMAB 1.0 (", italic("PS"), " = 0.5)")))
  lines(expSales_JMAB1.0_0.5_Switching[1:100, 2],type = "l",lty=2,lwd = 2, col=2)
  legend("topright", legend=c("Actual", "Expected"),
         col=c("blue", 2), lty=c(1,2), cex=1.25, lwd=c(2,2))
})

# Skewness

library(moments)

JMAB1.0_salesVector<-c(as.matrix(sales_JMAB1.0[1:1000,2:101]))

skewness_sales_JMAB1.0 <- skewness(data.frame(matrix(JMAB1.0_salesVector, nrow = 100)))

JMAB1.0_1.0_InvRatio_salesVector<-as.vector(as.matrix(sales_JMAB1.0_1.0_InvRatio[1:1000,2:101]))

skewness_sales_JMAB1.0_1.0_InvRatio <- skewness(data.frame(matrix(JMAB1.0_1.0_InvRatio_salesVector, nrow = 100)))

JMAB1.0_0.5_Switching_salesVector<-as.vector(as.matrix(sales_JMAB1.0_0.5_Switching[1:1000,2:101]))

skewness_sales_JMAB1.0_0.5_Switching <- skewness(data.frame(matrix(JMAB1.0_0.5_Switching_salesVector, nrow = 100)))

JMAB2.0_salesVector<-c(as.matrix(sales_JMAB2.0[1:1000,2:101]))

skewness_sales_JMAB2.0 <- skewness(data.frame(matrix(JMAB2.0_salesVector, nrow = 100)))

skewness_data <- as.table(skewness_sales_JMAB1.0)
skewness_data <- cbind(skewness_data, skewness_sales_JMAB1.0_0.5_Switching)
skewness_data <- cbind(skewness_data, skewness_sales_JMAB1.0_1.0_InvRatio)
skewness_data <- cbind(skewness_data, skewness_sales_JMAB2.0)


devEval(plotFormat, name="SkewnessSales", aspectRatio=1, {
  par(las=1)
  par(mar = c(5.1, 4.1, 4.1, 2.1) )
  boxplot(skewness_data,  las=1,names = c("","","",""),whisklty=1, xaxt = "n", ylim=c(min(skewness_sales_JMAB1.0_0.5_Switching),abs(min(skewness_sales_JMAB1.0_0.5_Switching))
  ),  main="Distribution of C Firm Sales Skewness (Sections of 100 periods)")
  axis(1,at=1:4, lbls,
       line=2, lwd=0, pos=par('usr')[3]*1.1)
  abline(h=c(0.0),col="black", lty=2)
})

