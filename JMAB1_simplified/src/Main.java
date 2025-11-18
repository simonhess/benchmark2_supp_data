import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Main {

	public static Random ra = new Random(1);
	
	public static double getNextGaussRandom(){
		return ra.nextGaussian()*0.0094+0;	
	}
	public static double getNextFoldedGaussRandom(){
		return Math.abs(Main.getNextGaussRandom());
	}
	
	public enum CapitalGoodDistribution{
		EQUAL, // Every firms gets an equal amount of capital goods of each age assigned in the steady state
		RANDOM, // All capital of all ages goods are randomly distributed among firms
		RANDOM2, // All capital of all ages goods are randomly distributed among firms
		SAME_AGE, // Every firms gets capital goods of one age assigned
		LUMPY // Every firms gets capital goods of a different age assigned
	}
	
	public static CapitalGoodDistribution CapitalGoodDistributionStrategy = CapitalGoodDistribution.EQUAL;
	
	public static double avSales;
	
	public static int round = 0; // The current round
	// switchingStrategy: true means households only switch their supplier with a certain probability or if the supplier is out of stock
	//false means that households are not tied to their previous supplier
	public static boolean switchingStrategy = true; 
	public static double switchingRate = 0.05; // The probability that a household will switch its supplier
	// normalRounding: False means that firms with an capital good demand above steady state level round their kDemand up.
	public static boolean normalRounding = true; 
	public static boolean investmentShockOneFirm = false; // If true firm invest additional units in period 1
	
	public static int rounds = 1000;
	public static int cSize = 100;
	public static int kSize = 1;
	public static int hhSize = 8000;
	public static int nbSellersCmarket = 5;
	
	public static double realSales = 32000;
	public static double realSalesKGoods = 2000;
	public static int kGoodDuration =20;
	public static double kGoodcFirms = 40000;
	public static double kGoodPrice = 2.6875;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Configurations.setConfiguration(args[0]);
			
		// Init cFirms
		
		ArrayList<ConsumptionFirm> cFirms = new ArrayList<ConsumptionFirm>();
		ArrayList<CapitalFirm> kFirms = new ArrayList<CapitalFirm>();
		ArrayList<Household> hhs = new ArrayList<Household>();
		ArrayList<ConsumptionFirm> cFirmsOrdered = new ArrayList<ConsumptionFirm>();
		
		ConsumptionFirm.steadyStateOutput=realSales/cSize;
		ConsumptionFirm.steadyStateInvestment=realSalesKGoods/cSize;
			
		for(int i=0;i<kSize;i++) {
			CapitalFirm k = new CapitalFirm();
			k.price = kGoodPrice;
			
			k.pastSales=realSalesKGoods/kSize;
			
			k.LAG_realSales.add(realSalesKGoods/kSize);
			
			k.pastPastSales=k.pastSales;
			
			k.expRSales=realSalesKGoods/kSize;
			
			k.pastExp=realSalesKGoods/kSize;
			
			k.markUp = CapitalFirm.steadyStateMarkup;

			double inv=realSalesKGoods/kSize*0.1;
			
			k.kGoodDuration=kGoodDuration;
			
			for(i = 0; i<inv;i++) {
				kGood newkGood = new kGood();
				newkGood.duration=kGoodDuration;
				newkGood.price=kGoodPrice;
				k.kGoods.add(newkGood);
			}
			
			k.id=i;
			kFirms.add(k);
		}
		
		
		// k good initializer
		ArrayList<kGood> initkGoods = new ArrayList<kGood>();			
		
		int ageCount = 1;
		for (int j = 0; j < kGoodcFirms; j++) {
		kGood newkGood = new kGood();
		newkGood.age = ageCount;
		newkGood.duration=kGoodDuration;
		newkGood.price=kGoodPrice;
		initkGoods.add(newkGood);
		ageCount++;
		if(ageCount==21)ageCount =1;
		}
		
		
		
		for(int i=0;i<cSize;i++) {
			ConsumptionFirm c = new ConsumptionFirm();
			
			c.pastSales=realSales/cSize;
			
			c.LAG_realSales.add(realSales/cSize);
			
			c.pastPastSales=c.pastSales;
			
			c.expRSales=realSales/cSize;
			
			c.pastExp=realSales/cSize;
			
			c.markUp = 0.1+getNextGaussRandom()*0.1;
			
			c.price = ConsumptionFirm.priceLowerBound*(1+c.markUp);
			c.avPrice=c.price;
			
			c.cGoods=realSales/cSize*ConsumptionFirm.invRatio;
			c.id=i;
			
			// Capital good supplier
			c.kSuppliers = kFirms;
			
			
			double kGoodsPerFirm = 0;
			
			kGoodsPerFirm = (kGoodcFirms / cSize);
			
			 switch(CapitalGoodDistributionStrategy) {
	            
	            case EQUAL:
	            	// With normal investment
	    			
	    			for (int j = 0; j < kGoodsPerFirm; j++) {
	    				kGood newkGood = new kGood();
	    				newkGood.age = (j % kGoodDuration) + 1;
	    				newkGood.duration=kGoodDuration;
	    				newkGood.price=kGoodPrice;
	    				c.kGoods.add(newkGood);
	    			}
	            	break;
	            case SAME_AGE:
	    			for (int j = 0; j < kGoodsPerFirm; j++) {
	    				kGood newkGood = new kGood();
	    				if(j<=kGoodsPerFirm/2) {
	    					newkGood.age=1;
	    				}else {
	    					newkGood.age=10;
	    				}
	    				//newkGood.age = (0 % kGoodDuration) + 1;
	    				newkGood.duration=kGoodDuration;
	    				newkGood.price=kGoodPrice;
	    				c.kGoods.add(newkGood);
	    			}
	            	break;
	            case LUMPY:
	            	
	    			// With max lumpy investment
	    			
					int age = (i%20)+1;
					
								for (int j = 0; j < kGoodsPerFirm; j++) {
									kGood newkGood = new kGood();
									newkGood.age = age;
									newkGood.duration=kGoodDuration;
									newkGood.price=kGoodPrice;
									c.kGoods.add(newkGood);
								}
	    						
	    						
	            	
	            	break;
	            case RANDOM:
	            	
	            	// Random investment initializer
	    			Collections.shuffle(initkGoods,ra);
	    			
	    						for (int j = 0; j < kGoodsPerFirm; j++) {
	    							int index = ra.nextInt(initkGoods.size());
	    							c.kGoods.add(initkGoods.get(index));
	    						}
					
	            	break;
	            case RANDOM2:
	            	
	    			// Random investment initializer 2			
	    			
					Collections.shuffle(initkGoods,ra);
					
					while(c.kGoods.size()<kGoodsPerFirm) {
						int index = ra.nextInt(initkGoods.size());
					c.kGoods.add(initkGoods.get(index));
						if(c.kGoods.size()<kGoodsPerFirm&&initkGoods.size()==0)break;
					}
	            	break;
	            }
			
			c.LAG_capFinancialValue.add(c.kGoods.size()*c.kGoods.get(0).price);
			c.LAG_amortisation.add(c.LAG_capFinancialValue.get(0)/c.kGoods.get(0).duration);
			c.LAG_realCapital.add((double) c.kGoods.size());
			c.LAG_laborDemand.add((double) (5000/cSize));
			c.LAG_capacity.add(c.kGoods.size());
			c.LAG_inventories.add(c.cGoods);
			
			c.LAGGED_VALUES.add(c.LAG_capFinancialValue);
			c.LAGGED_VALUES.add(c.LAG_amortisation);
			c.LAGGED_VALUES.add(c.LAG_realCapital);
			c.LAGGED_VALUES.add(c.LAG_laborDemand);
			c.LAGGED_VALUES.add(c.LAG_capacity);
			c.LAGGED_VALUES.add(c.LAG_investment);
			
			c.invest=c.kGoods.size()/c.kGoods.get(0).duration;
			c.desiredOutput=c.pastPastSales;
			
			cFirms.add(c);
			cFirmsOrdered.add(c);
			
		}	
		
		int hhPercFirm = hhSize/cSize;
		
		ArrayList<Household> tempHhs = new ArrayList<Household>();
		
		for(int i=0;i<hhSize;i++) {
			Household hh = new Household();
			int sellerId= (int) i/hhPercFirm;
			
			hh.previousSupplier=cFirms.get(sellerId);
			hhs.add(hh);
			Household.unemployedHhs.add(hh);
			tempHhs.add(hh);
		}
		
		
		// Simulation
		
		ArrayList<ConsumptionFirm> cFirmsMarketPop = new ArrayList<ConsumptionFirm>();

		HashMap<Long, ConsumptionFirm> suppliersWithNewBuyer = new HashMap<Long, ConsumptionFirm>();
		HashMap<Long, ConsumptionFirm> suppliersLosingBuyer = new HashMap<Long, ConsumptionFirm>();
		
		// CSV
		
		String folderName = "data/";
		
        try{
            FileWriter file = new FileWriter(folderName+"avMarkup.csv");
            PrintWriter write = new PrintWriter(file);
            FileWriter avDeltaFile = new FileWriter(folderName+"avDelta.csv");
            PrintWriter avDeltaWrite = new PrintWriter(avDeltaFile);
            FileWriter avSwitchingSalesFile = new FileWriter(folderName+"avSwitching.csv");
            PrintWriter avSwitchingSalesWrite = new PrintWriter(avSwitchingSalesFile);
            FileWriter switchingRateFile = new FileWriter(folderName+"switchingRate.csv");
            PrintWriter switchingRateWrite = new PrintWriter(switchingRateFile);
            FileWriter adjustmentSizeFile = new FileWriter(folderName+"adjustmentSize.csv");
            PrintWriter adjustmentSizeWrite = new PrintWriter(adjustmentSizeFile);
            FileWriter avPastSalesDiffFile = new FileWriter(folderName+"avPastSalesDiff.csv");
            PrintWriter avPastSalesDiffWrite = new PrintWriter(avPastSalesDiffFile);
            
            FileWriter deltaFile = new FileWriter(folderName+"delta.csv");
            PrintWriter deltaWrite = new PrintWriter(deltaFile);
            FileWriter markupFile = new FileWriter(folderName+"markup.csv");
            PrintWriter markupWrite = new PrintWriter(markupFile);
            FileWriter salesFile = new FileWriter(folderName+"sales.csv");
            PrintWriter salesWrite = new PrintWriter(salesFile);
            
            FileWriter kSalesFile = new FileWriter(folderName+"kSales.csv");
            PrintWriter kSalesWrite = new PrintWriter(kSalesFile);
            
            FileWriter unemploymentFile = new FileWriter(folderName+"unemployment.csv");
            PrintWriter unemploymentWrite = new PrintWriter(unemploymentFile);
            
            FileWriter cFirmOCFFile = new FileWriter(folderName+"cFirmOCF.csv");
            PrintWriter cFirmOCFWrite = new PrintWriter(cFirmOCFFile);
		
            FileWriter cFirmFinancialValueFile = new FileWriter(folderName+"cFirmFinancialValue.csv");
            PrintWriter cFirmFinancialValueWrite = new PrintWriter(cFirmFinancialValueFile);
            
            FileWriter cFirmCapacityFile = new FileWriter(folderName+"cFirmCapacity.csv");
            PrintWriter cFirmCapacityWrite = new PrintWriter(cFirmCapacityFile);
            
            FileWriter cFirmAmortisationFile = new FileWriter(folderName+"cFirmAmortisation.csv");
            PrintWriter cFirmAmortisationWrite = new PrintWriter(cFirmAmortisationFile);
            
            FileWriter capUtilizationFile = new FileWriter(folderName+"capUtilization.csv");
            PrintWriter capUtilizationWrite = new PrintWriter(capUtilizationFile);
            
            FileWriter cFirmInvestmentFile = new FileWriter(folderName+"cFirmInvestment.csv");
            PrintWriter cFirmInvestmentWrite = new PrintWriter(cFirmInvestmentFile);
            
            FileWriter cFirmDesiredOutputFile = new FileWriter(folderName+"cFirmDesiredOutput.csv");
            PrintWriter cFirmDesiredOutputWrite = new PrintWriter(cFirmDesiredOutputFile);
            
            FileWriter expRealSalesFile = new FileWriter(folderName+"cFirmExpRealSales.csv");
            PrintWriter expRealSalesWrite = new PrintWriter(expRealSalesFile);
            
            FileWriter cFirmCustomersFile = new FileWriter(folderName+"cFirmCustomers.csv");
            PrintWriter cFirmCustomersWrite = new PrintWriter(cFirmCustomersFile);
            
            FileWriter cPriceFile = new FileWriter(folderName+"cPrice.csv");
            PrintWriter cPriceWrite = new PrintWriter(cPriceFile);
            
            FileWriter cResidualCapFile = new FileWriter(folderName+"cResidualCap.csv");
            PrintWriter cResidualCapWrite = new PrintWriter(cResidualCapFile);
            
            FileWriter cFirmInventoriesCapFile = new FileWriter(folderName+"cFirmInventories.csv");
            PrintWriter cFirmInventoriesWrite = new PrintWriter(cFirmInventoriesCapFile);
            
            FileWriter cFirmPriceQuantityFile = new FileWriter(folderName+"cFirmPriceQuantity.csv");
            PrintWriter cFirmPriceQuantityWrite = new PrintWriter(cFirmPriceQuantityFile);
            
            FileWriter cFirmDesiredCapFile = new FileWriter(folderName+"cFirmDesiredCap.csv");
            PrintWriter cFirmDesiredCapWrite = new PrintWriter(cFirmDesiredCapFile);
            
            FileWriter cFirmCashFlowFile = new FileWriter(folderName+"cFirmCashFlowRate.csv");
            PrintWriter cFirmCashFlowWrite = new PrintWriter(cFirmCashFlowFile);
            
         
		for(int i = 1; i<=rounds;i++) {
			
			round=i;
			
			String deltaString = i+"";
			String markUpString = i+"";
			String salesString = i+"";
			String unemloymentString = i+"";
			String cFirmOFCString = i+"";
			String cFirmFinancialValueString = i+"";
			String cFirmCapacityString = i+"";
			String cFirmAmortisationString = i+"";
			String cFirmCapUtilizationString = i+"";
			String cFirmInvestmentString = i+"";
			String cFirmDesiredOutputString = i+"";
			String cFirmExpRealSalesString = i+"";
			String cFirmCustomersString = i+"";
			String cPriceString = i+"";
			String cResidualCapString = i+"";
			String cFirmInventoriesString = i+"";
			String cFirmPriceQuantityString = i+"";
			String cFirmDesiredCapString = i+"";
			String cFirmCashFlowRateString = i+"";
			
			double avDelta=0;
			for(ConsumptionFirm cF: cFirms) {
				double delta = cF.pastExp-cF.pastSales;
				deltaString+=","+delta;
	
				avDelta+=Math.abs(delta);
				}
			avDelta/=cFirms.size();
			System.out.println("avDelta: "+avDelta);
			
			double sumSwitchingSales = 0;
			double sumSwitching = 0;
			
			
			Collections.shuffle(Household.unemployedHhs,ra);
				
			for(ConsumptionFirm c: cFirms) {
				c.LAG_realSales.add((double) 0);
				c.updateExpectationBasedOnMechanism();
				c.produce();
				c.updatePriceBasedOnMechanism();

				if(c.cGoods>0) {
					cFirmsMarketPop.add(c);
				}
				
				double capU = c.desiredOutput/c.kGoods.size();
				cFirmCapUtilizationString+=","+Double.toString(capU);
				
				int customers = 0;
				
				for(Household h: hhs) {
					if(h.previousSupplier==c) {
						customers++;
					}
				}
				
				cFirmCapUtilizationString+=","+Double.toString(customers);
				
				cFirmCustomersString+=","+Double.toString(customers);
				
			}
			
			
			
			for(CapitalFirm k: kFirms) {
				k.LAG_realSales.add((double) 0);
				k.updateExpectations();

				k.produce();
				k.updatePrice();
			}
			

			
			for(ConsumptionFirm c: cFirms) {
				
				c.investBasedOnMechanism();
				
				c.amortisation();
			}
			
			double demandSum = 0;
			
			for(Household hh: hhs) {
				if(Household.unemployedHhs.contains(hh)) {
					hh.wage=5*0.2;
				}
				hh.computeDemand();
				//hh.demand=4;
				demandSum+=hh.demand;
			}
			
			double u = (Household.unemployedHhs.size()/8000.00);
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Unemployment Rate: "+u);
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<kSales : "+kFirms.get(0).pastSales);
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Demand : "+demandSum);
			
			unemloymentString = unemloymentString+","+ Double.toString(1.00*Household.unemployedHhs.size());
			
			// Set workforce free
			ArrayList<Household> employees = new ArrayList<Household>();
			
			for(Household hh: Household.employedHhs) {
				
				employees.add(hh);
			}
			
			for(Household hh: employees) {
				Household.employedHhs.remove(hh);
				Household.unemployedHhs.add(hh);
			}	
			
			// market
			Collections.shuffle(hhs,ra);
			
			
			for(Household hh: hhs) {
				//hh.demand=realSales/hhSize;
				

				while(hh.demand>0&&cFirmsMarketPop.size()>0) {
		
					Collections.shuffle(cFirmsMarketPop,ra);
					ArrayList<ConsumptionFirm> potentialSellers = new ArrayList<ConsumptionFirm>();
					for(int a = 0; a<Math.min(cFirmsMarketPop.size(),nbSellersCmarket);a++) {
						potentialSellers.add(cFirmsMarketPop.get(a));
					}
					// Determine Lowest Price
					ConsumptionFirm cheapestSeller = potentialSellers.get(0);
					for(int a = 1; a<potentialSellers.size();a++) {
						if(potentialSellers.get(a).price<cheapestSeller.price) {
							cheapestSeller=potentialSellers.get(a);
						}
					}
					boolean switching = false;
					
					if(switchingStrategy) {
						if((cheapestSeller.price<hh.previousSupplier.price&&ra.nextDouble()<=Main.switchingRate)||hh.previousSupplier.cGoods==0) {
							//if((cheapestSeller.price<hh.previousSupplier.price&&hh.previousSupplier.price-cheapestSeller.price>0.0001)||hh.previousSupplier.cGoods==0) {
							suppliersLosingBuyer.put((long) hh.previousSupplier.id, hh.previousSupplier);
							hh.previousSupplier=cheapestSeller;
							suppliersWithNewBuyer.put((long) hh.previousSupplier.id, hh.previousSupplier);
							switching=true;
						}
					}else {
						suppliersLosingBuyer.put((long) hh.previousSupplier.id, hh.previousSupplier);
						hh.previousSupplier=cheapestSeller;
						suppliersWithNewBuyer.put((long) hh.previousSupplier.id, hh.previousSupplier);
						switching=true;
					}
					
					
					
					// Choose cheapest seller and buy product
					
					double quantity=Math.min(hh.demand,hh.previousSupplier.cGoods);
					hh.demand-=quantity;
					hh.previousSupplier.cGoods-=quantity;
					hh.previousSupplier.pastSales+=quantity;
					hh.previousSupplier.LAG_realSales.set(hh.previousSupplier.LAG_realSales.size()-1,
							hh.previousSupplier.LAG_realSales.get(hh.previousSupplier.LAG_realSales.size()-1)+quantity);
					if(hh.previousSupplier.cGoods==0) {
						cFirmsMarketPop.remove(hh.previousSupplier);
					}
					if(switching==true) {
						sumSwitchingSales+=quantity;
						sumSwitching++;
					}
					
				}
			}
			
			cFirmsMarketPop.clear();
			
			
			// Print Markups
			
			System.out.println("###################################################### Round: "+i);
			
			// Print real GDP
			
			double sum = 0;
			double avMarkup = 0;
			double avAdjustmentSize = 0;
			double avPastSales=0;
			double positiveCount = 0;
			double negativeCount = 0;
			double positiveSum = 0;
			double negativeSum = 0;
			double avPrice = 0;
			
			Double[] prices = new Double[cFirms.size()];
			
			int index = 0;
			
			for(ConsumptionFirm cF: cFirmsOrdered) {
				avPrice+=cF.price;
				
				prices[index] = cF.markUp;
				index++;
				
				
			if(cF.pastSales-cF.pastPastSales>0) {
				positiveCount++;
				positiveSum+=cF.pastSales-cF.pastPastSales;
			}else {
				negativeCount++;
				negativeSum-=cF.pastSales-cF.pastPastSales;
			}
			avPastSales+=Math.abs(cF.pastSales-cF.pastPastSales);
			sum+=cF.pastSales;
			avMarkup+=cF.markUp;
			}
			
			avPrice/=cFirms.size();
			avMarkup/=cFirms.size();
			
			Arrays.sort(prices);
			double median;
			if (prices.length % 2 == 0)
			    median = ((double)prices[prices.length/2] + (double)prices[prices.length/2 - 1])/2;
			else
			    median = (double) prices[prices.length/2];		
			
			for(ConsumptionFirm cF: cFirms) {
			cF.avDelta=avDelta;
			cF.avPrice=avPrice;
			cF.avMarkup=avMarkup;
			cF.medianPrice=median;
			cF.LAG_inventories.add(cF.cGoods);
			
			markUpString+=","+cF.markUp;
			salesString+=","+cF.LAG_realSales.get(cF.LAG_realSales.size()-1);
			
			cFirmOFCString+=","+(cF.price*cF.pastSales-5*cF.LAG_laborDemand.get(cF.LAG_laborDemand.size()-1));
			cFirmFinancialValueString+=","+cF.LAG_capFinancialValue.get(cF.LAG_capFinancialValue.size()-1);
			cFirmCapacityString +=","+(cF.LAG_capacity.get(cF.LAG_capacity.size()-1)*1.0);
			cFirmAmortisationString+=","+cF.LAG_amortisation.get(cF.LAG_amortisation.size()-1);
			//cFirmAmortisationString+=","+cF.depreciation;
			cFirmInvestmentString+=","+cF.LAG_investment.get(cF.LAG_investment.size()-1);
			cFirmDesiredOutputString+=","+cF.desiredOutput;
			cFirmExpRealSalesString+=","+cF.expRSales;
			cPriceString +=","+cF.price;
			cResidualCapString +=","+cF.resCap;
			cFirmDesiredCapString +=","+cF.desiredCap;
			
			cFirmInventoriesString +=","+cF.cGoods;
			
			cFirmCashFlowRateString +=","+cF.cashFlowRate;
			
			}
			
			cFirmPriceQuantityString+=","+ConsumptionFirm.q1Count+","+ConsumptionFirm.q2Count+","+ConsumptionFirm.q3Count+","+ConsumptionFirm.q4Count;
			
			avAdjustmentSize/=cFirms.size();
			avPastSales/=cFirms.size();
			
			Main.avSales=avPastSales;
			
			
			write.println(i+","+avMarkup);
			avDeltaWrite.println(i+","+avDelta);
			double avSwitchingSales = sumSwitchingSales/(suppliersWithNewBuyer.size());
			avSwitchingSalesWrite.println(i+","+Double.toString(avSwitchingSales));
			
			switchingRateWrite.println(i+","+Double.toString(sumSwitching/hhSize));
			
			adjustmentSizeWrite.println(i+","+Double.toString(avAdjustmentSize));
			
			avPastSalesDiffWrite.println(i+","+Double.toString(avPastSales));
			
			deltaWrite.println(deltaString);
			
			markupWrite.println(markUpString);
			
			salesWrite.println(salesString);
			
			kSalesWrite.println((i+","+Double.toString(kFirms.get(0).LAG_realSales.get(kFirms.get(0).LAG_realSales.size()-1))));
			
			unemploymentWrite.println(unemloymentString);
			
			cFirmOCFWrite.println(cFirmOFCString);
			
			cFirmFinancialValueWrite.println(cFirmFinancialValueString);
			
			cFirmAmortisationWrite.println(cFirmAmortisationString);
			
			capUtilizationWrite.println(cFirmCapUtilizationString);
			
			cFirmCapacityWrite.println(cFirmCapacityString);
			
			cFirmInvestmentWrite.println(cFirmInvestmentString);
			
			cFirmDesiredOutputWrite.println(cFirmDesiredOutputString);
			
			expRealSalesWrite.println(cFirmExpRealSalesString);
			
			cFirmCustomersWrite.println(cFirmCustomersString);
			
			cPriceWrite.println(cPriceString);
			
			cResidualCapWrite.println(cResidualCapString);
			
			cFirmInventoriesWrite.println(cFirmInventoriesString);
			
			cFirmPriceQuantityWrite.println(cFirmPriceQuantityString);
			
			cFirmDesiredCapWrite.println(cFirmDesiredCapString);
			
			cFirmCashFlowWrite.println(cFirmCashFlowRateString);
			
			System.out.println(sum);
		
			System.out.println("SwitchingFirms: "+suppliersWithNewBuyer.size());
			System.out.println("suppliersLosingBuyer: "+suppliersLosingBuyer.size());
			System.out.println("avMarkup: "+ avMarkup);

			suppliersWithNewBuyer.clear();
			suppliersLosingBuyer.clear();
			
			System.out.println("Switching Rate: "+Main.switchingRate);
				
			System.out.println("Q1: "+ConsumptionFirm.q1Count);
			System.out.println("Q2: "+ConsumptionFirm.q2Count);
			System.out.println("Q3: "+ConsumptionFirm.q3Count);
			System.out.println("Q4: "+ConsumptionFirm.q4Count);
			System.out.println("Q3Sum: "+ConsumptionFirm.q3Sum);
			System.out.println("Q4Sum: "+ConsumptionFirm.q4Sum);
			
			// Remove unnecessary lagged values
			for(ConsumptionFirm cF: cFirms) {
				
				for(ArrayList list: cF.LAGGED_VALUES) {
					while(list.size()>4) {
						list.remove(0);
					}
					
				}
			}
		
	}
		
		write.close();
		avDeltaWrite.close();
		avSwitchingSalesWrite.close();
		switchingRateWrite.close();
		adjustmentSizeWrite.close();
		avPastSalesDiffWrite.close();
		deltaWrite.close();
		markupWrite.close();
		salesWrite.close();
		kSalesWrite.close();
		unemploymentWrite.close();
		cFirmOCFWrite.close();
		
		cFirmFinancialValueWrite.close();
		
		cFirmAmortisationWrite.close();
		
		capUtilizationWrite.close();
		
		cFirmCapacityWrite.close();
		
		cFirmInvestmentWrite.close();
		
		cFirmDesiredOutputWrite.close();
		
		cFirmCustomersWrite.close();
		
		cPriceWrite.close();
		
		cResidualCapWrite.close();
		
		cFirmInventoriesWrite.close();
		
		cFirmPriceQuantityWrite.close();
		
		expRealSalesWrite.close();
		
		cFirmDesiredCapWrite.close();
		
		cFirmCashFlowWrite.close();
        }
	    catch(IOException exe){
	        System.out.println("Cannot create file");
	    }

}
}
