import java.util.ArrayList;
import java.util.Random;

public class ConsumptionFirm extends AbstractFirm{
	
	public double updateInvestmentRound;
	public double pastSales;
	public double expRSales;
	public double pastExp;
	public double markUp;
	public double price;
	
	public double cGoods;
	public int id;
	public double pastPastSales;
	public double positiveSumPositiveFirms=0;
	public double avDelta =0;
	public double avPrice = 0;
	public double avMarkup;
	public double medianPrice;
	public ArrayList<CapitalFirm> kSuppliers = new ArrayList<CapitalFirm>();
	public ArrayList<kGood> kGoods = new ArrayList<kGood>();
	public double kGoodsDouble;
	public double desiredOutput;
	public double pastDesiredOutput;
	public double desiredCap;
	public double cashFlowRate;
	
	public static double priceLowerBound =0.936688447;
	
	public static double targetCashFlow = 0.051875;
	public static double targetCapacityUtlization = 0.8;
	public static double cashFlowRateWeight = 0.01;
	public static double capacityWeight = 0.02;
	public static double capitalProductivity= 1;
	
	public static double laborProductivity=2;
	public static double capitalLaborRatio= 6.4;
	
	public double invest = 0;
	public double expInv = 1000;
	
	public static double invRatio=0.1;
	public static double alpha=0.25;
	public static double maxInvRatio=0.15;
	public static double minInvRatio=0.05;
	
	public ArrayList<ArrayList> LAGGED_VALUES = new ArrayList<ArrayList>();
	public ArrayList<Double> LAG_realSales = new ArrayList<Double>();
	public ArrayList<Double> LAG_capFinancialValue = new ArrayList<Double>();
	public ArrayList<Double> LAG_amortisation = new ArrayList<Double>();
	public ArrayList<Double> LAG_realCapital = new ArrayList<Double>();
	public ArrayList<Double> LAG_laborDemand = new ArrayList<Double>();
	public ArrayList<Integer> LAG_capacity = new ArrayList<Integer>();
	public ArrayList<Double> LAG_investment = new ArrayList<Double>();
	public ArrayList<Double> LAG_inventories = new ArrayList<Double>();
	
	public double resCap;
	public double depreciation;
	
	public static long q1Count = 0;
	public static long q2Count = 0;
	public static long q3Count = 0;
	public static double q3Sum = 0;
	public static long q4Count = 0;
	public static double q4Sum = 0;
	
	public static boolean alternatingFixedOutput = false;
	public static double steadyStateOutput = 0;
	public static double steadyStateInvestment = 0;
	
	public static expMechanisms currentExpMechanism = expMechanisms.JMAB1;
	public static pricingMechanisms currentPricingMechanism = pricingMechanisms.JMAB1;
	public static investmentMechanisms currentInvestmentMechanism = investmentMechanisms.JMAB1;
	
	public enum expMechanisms{
		JMAB1,
		JMAB2,
		ASSENZA,
		DAWIDGATTI
	}
	
	public enum pricingMechanisms{
		JMAB1,
		JMAB2,
		ASSENZA,
		DAWIDGATTI
	}
	
	public enum investmentMechanisms{
		JMAB1,
		JMAB2,
		JMAB1_Lumpy
	}
	
	public void updateExpectationBasedOnMechanism() {
		
		switch(currentExpMechanism) {
		case JMAB1:
			updateRealSalesExpectationExpSmoothing();
			break;
		case JMAB2:
			updateExpectationsJMAB2();
			break;
		case ASSENZA:
			updateExpectationsWithAvPriceAssenza();
			break;
		case DAWIDGATTI:
			updateExpectationsDawidGatti();
			break;
		}
		
	}
	
	public void updatePriceBasedOnMechanism() {
		
		switch(currentPricingMechanism) {
		case JMAB1:
			updatePrice();
			break;
		case JMAB2:
			updatePriceJMAB2();
			break;
		case ASSENZA:
			updatePriceWithAveragePrice();
			break;
		case DAWIDGATTI:
			updatePriceDawidGatti();
			break;
		}
		
	}
	
	public void investBasedOnMechanism() {
		
		switch(currentInvestmentMechanism) {
		case JMAB1:
			investJMAB();
			break;
		case JMAB2:
			investJMAB2();
			break;
		case JMAB1_Lumpy:
			investJMABLumpy();
			break;
		}
		
	}
	
	public void produce() {
	
		double expSales = expRSales;

		double invQuantity = cGoods;
		double expInv=expSales*invRatio;
		pastDesiredOutput=desiredOutput;
		
		if(alternatingFixedOutput) {
		if(id==1) {
			if(Main.round%2==0) {
				desiredOutput= ConsumptionFirm.steadyStateOutput*(1-Main.switchingRate);
			}else {
				desiredOutput=ConsumptionFirm.steadyStateOutput*(1+Main.switchingRate);
			}
		}else {
			if(Main.round%2==0) {
				desiredOutput= ConsumptionFirm.steadyStateOutput*(1+Main.switchingRate);
			}else {
				desiredOutput= ConsumptionFirm.steadyStateOutput*(1-Main.switchingRate);
			}
		}

		}else {
			desiredOutput= Math.max(0,expSales+(expInv-invQuantity));
		}
		
		cGoods += desiredOutput;
		
		// Hire workers
		
		double capUD = Math.min(1,desiredOutput/kGoods.size()*capitalProductivity);
		
		double laborDemand = capUD*kGoods.size()/capitalLaborRatio;
		
		if(Main.normalRounding==true) {
			laborDemand = Math.round(laborDemand);
		}else {
			if(desiredOutput>ConsumptionFirm.steadyStateOutput&&laborDemand%0.5==0) {
				laborDemand = Math.ceil(laborDemand);
			}else if(desiredOutput<ConsumptionFirm.steadyStateOutput&&laborDemand%0.5==0){
				laborDemand = Math.floor(laborDemand);
			}else {
				laborDemand = Math.round(laborDemand);
			}
		}
				
		LAG_laborDemand.add(laborDemand);
		

		while(laborDemand>0) {
			
			Household hh = Household.unemployedHhs.get(0);
			Household.employedHhs.add(hh);
			Household.unemployedHhs.remove(hh);
			
			hh.wage=5;	
			
			hh.employer=this;
			laborDemand--;
			
		}
		
		if(kGoods.size()==0) {	
			this.LAG_capFinancialValue.add((double) 0);
			this.LAG_amortisation.add((double) 0);
			this.LAG_realCapital.add((double) 0);
			this.LAG_capacity.add(0);
		}else {
			this.LAG_capFinancialValue.add(kGoods.size()*kGoods.get(0).price);
			this.LAG_amortisation.add(this.LAG_capFinancialValue.get(this.LAG_capFinancialValue.size()-1)/kGoods.get(0).duration);
			this.LAG_realCapital.add((double) kGoods.size());
			this.LAG_capacity.add(kGoods.size());
		}
		
		
		
	}
	
	public void investJMAB2() {
		
		double capitalValue=0;
		
		for(kGood item: kGoods) {
			capitalValue +=	item.value;
		}
		
		double capacity = kGoods.size()*capitalProductivity;
		
		double investment = 0;
		
		for(kGood k: this.kGoods) {
			
			if(k.age==1) {
				investment+=k.price;
			}
			
		}
		
		// Vanilla ocf
		double cashFlowRate = (LAG_realSales.get(LAG_realSales.size()-2)*this.price-5*this.LAG_laborDemand.get(this.LAG_laborDemand.size()-2))/this.LAG_capFinancialValue.get(this.LAG_capFinancialValue.size()-2);
		this.cashFlowRate=cashFlowRate;
		
		// Vanilla
		
		double desiredRateOfGrowth=cashFlowRateWeight*(cashFlowRate-targetCashFlow)/targetCashFlow+capacityWeight*((desiredOutput/capacity)-targetCapacityUtlization)/targetCapacityUtlization;
		
		 desiredCap = this.LAG_realCapital.get(this.LAG_realCapital.size()-2)*(1+desiredRateOfGrowth);
				
		double residualCap = 0;
		
		for(kGood item:kGoods) {
			
			if(item.age<item.duration) {
				residualCap++;
			}
			
		}
		
		resCap =residualCap;
		
		if(desiredCap>capacity) {
			//invest=invest+4;
			invest=invest+Main.getNextFoldedGaussRandom()*ConsumptionFirm.steadyStateInvestment;
		   // invest=invest+stepsize;
		}else if(desiredCap<capacity) {
			//invest=invest-4;
			invest=invest-Main.getNextFoldedGaussRandom()*ConsumptionFirm.steadyStateInvestment;
			//invest=invest-stepsize;
		}
	
		double kDemand=Math.round(invest);
		
		if(Main.round==1&&id==0&&Main.investmentShockOneFirm==true) {
			kDemand=ConsumptionFirm.steadyStateInvestment*1.01;
		}
		
		LAG_investment.add(kDemand);
		
		CapitalFirm kF = kSuppliers.get(0);
		
		while(kDemand>0&&kF.kGoods.size()>0) {
			this.kGoods.add(kF.kGoods.remove(0));
			kF.pastSales++;
			kF.LAG_realSales.set(kF.LAG_realSales.size()-1,
					kF.LAG_realSales.get(kF.LAG_realSales.size()-1)+1);
			kDemand--;
		}
		
	}
	
	public void investJMAB() {
		
		double capitalValue=0;
		
		for(kGood item: kGoods) {
			capitalValue +=	item.value;
		}
		
		double capacity = kGoods.size()*capitalProductivity;
		

		// Vanilla ocf
		double cashFlowRate = (LAG_realSales.get(LAG_realSales.size()-2)*this.price-5*this.LAG_laborDemand.get(this.LAG_laborDemand.size()-2))/this.LAG_capFinancialValue.get(this.LAG_capFinancialValue.size()-2);
		this.cashFlowRate = cashFlowRate;
		
		// Vanilla
		double desiredRateOfGrowth=cashFlowRateWeight*(cashFlowRate-targetCashFlow)/targetCashFlow+capacityWeight*((desiredOutput/capacity)-targetCapacityUtlization)/targetCapacityUtlization;
	
		desiredCap = kGoods.size()*(1+desiredRateOfGrowth);
		
		double residualCap = 0;
		
		for(kGood item:kGoods) {
			
			if(item.age<item.duration) {
				residualCap++;
			}
			
		}
		resCap =residualCap;
		
		double kDemand =Math.max(desiredCap-residualCap, 0);

		if(Main.normalRounding == true) {
			kDemand =Math.round(kDemand);
		}else {
			if(kDemand>steadyStateInvestment&&kDemand%0.5==0) {
				kDemand = Math.ceil(kDemand);
			}else if(kDemand<steadyStateInvestment&&kDemand%0.5==0){
				kDemand = Math.floor(kDemand);
			}else {
				kDemand = Math.round(kDemand);
			}
		}
		
		if(Main.round==1&&id==0&&Main.investmentShockOneFirm==true) {
			kDemand=ConsumptionFirm.steadyStateInvestment*1.01;
		}
		
		
		
		LAG_investment.add(kDemand);
		
		CapitalFirm kF = kSuppliers.get(0);
		
		while(kDemand>0&&kF.kGoods.size()>0) {
			kF.pastSales++;
			this.kGoods.add(kF.kGoods.remove(0));
			kF.LAG_realSales.set(kF.LAG_realSales.size()-1,
					kF.LAG_realSales.get(kF.LAG_realSales.size()-1)+1);
			kDemand--;
		}
		
	}
	
	public void investJMABLumpy() {
		
		double capitalValue=0;
		
		for(kGood item: kGoods) {
			capitalValue +=	item.value;
		}
		
		double capacity = kGoods.size()*capitalProductivity;
		
		double investment = 0;
		
		for(kGood k: this.kGoods) {
			
			if(k.age==1) {
				investment+=k.price;
			}
			
		}
		// Vanilla ocf
		double cashFlowRate = (LAG_realSales.get(LAG_realSales.size()-2)*this.price-5*this.LAG_laborDemand.get(this.LAG_laborDemand.size()-2))/this.LAG_capFinancialValue.get(this.LAG_capFinancialValue.size()-2);
		
		// Vanilla
		double desiredRateOfGrowth=cashFlowRateWeight*(cashFlowRate-targetCashFlow)/targetCashFlow+capacityWeight*((desiredOutput/capacity)-targetCapacityUtlization)/targetCapacityUtlization;
	
		double desiredCap = this.LAG_realCapital.get(this.LAG_realCapital.size()-2)*(1+desiredRateOfGrowth);
		
		double residualCap = 0;
		
		for(kGood item:kGoods) {
			
			if(item.age<item.duration) {
				residualCap++;
			}
			
		}
		resCap =residualCap;
		
		double kDemand =Math.round( Math.max(desiredCap-residualCap, 0));
		
		
		if(this.expRSales<residualCap) {
			
			kDemand=0;
			
		}
		
		LAG_investment.add(kDemand);
		
		CapitalFirm kF = kSuppliers.get(0);
		
		while(kDemand>0&&kF.kGoods.size()>0) {
			this.kGoods.add(kF.kGoods.remove(0));
			kF.pastSales++;
			kDemand--;
		}
		
	}
	
	public void updateRealSalesExpectationExpSmoothing() {
		pastExp=expRSales;

		expRSales =expRSales+ConsumptionFirm.alpha*(LAG_realSales.get(LAG_realSales.size()-2)-expRSales);
	}
	
	public void updateRealSalesExpectationFixed() {
		pastExp=expRSales;

	}
	
	public void updatePriceTargetInventories() {
		
		if(LAG_inventories.get(LAG_inventories.size()-1)/LAG_realSales.get(LAG_realSales.size()-2)<ConsumptionFirm.invRatio) {
			markUp=markUp*(1+Main.getNextFoldedGaussRandom());
			
		q3Count++;
			q3Sum+=Math.abs(markUp-avMarkup);
		}else if(LAG_inventories.get(LAG_inventories.size()-1)/LAG_realSales.get(LAG_realSales.size()-2)>ConsumptionFirm.invRatio) {
			markUp=markUp*(1-Main.getNextFoldedGaussRandom());

			q4Count++;
			q4Sum+=Math.abs(markUp-avMarkup);
		}
		
		if(markUp<0) {
			markUp=0;
		}
		
		price=priceLowerBound*(1+markUp);
		pastPastSales=pastSales;
		pastSales=0;
		
		
	}
	
	public void updatePriceTargetInventoriesFixedStepsize() {
				
		if(LAG_inventories.get(LAG_inventories.size()-1)/LAG_realSales.get(LAG_realSales.size()-2)<ConsumptionFirm.invRatio) {
			markUp=markUp+0.00075;
			
		q3Count++;
			q3Sum+=Math.abs(markUp-avMarkup);
		}else if(LAG_inventories.get(LAG_inventories.size()-1)/LAG_realSales.get(LAG_realSales.size()-2)>ConsumptionFirm.invRatio) {
			
			markUp=markUp-0.00075;

			q4Count++;
			q4Sum+=Math.abs(markUp-avMarkup);
		}
		
		
		if(markUp<0) {
			markUp=0;
		}
		
		price=priceLowerBound*(1+markUp);
		pastPastSales=pastSales;
		pastSales=0;
		
		
	}
	
	
	
	public void updateExpectations() {
		
		pastExp=expRSales;
		
		double delta = pastExp-LAG_realSales.get(LAG_realSales.size()-2);
		
		if(delta<0) {

			expRSales=expRSales+Main.getNextFoldedGaussRandom()*320;
			//expRSales=expRSales+320*(Math.abs(ra.nextDouble())*0.01);
			q1Count++;
		}else if(delta>0) {
			expRSales=expRSales-Main.getNextFoldedGaussRandom()*320;
			//expRSales=expRSales-320*(Math.abs(ra.nextDouble())*0.01);
			q2Count++;
		}	
		
	}
	
public void updateExpectationsJMAB2fixedStepsize() {
		
		pastExp=expRSales;
		
		if(pastExp<LAG_realSales.get(LAG_realSales.size()-2)) {

			expRSales++;
			//expRSales+=32000*0.01;
			q1Count++;
		}else if(pastExp>LAG_realSales.get(LAG_realSales.size()-2)) {

			expRSales--;
	
			//expRSales-=32000*0.01;
			
			q2Count++;
		}	
		
	}
	
	public void updateExpectationsJMAB2() {
		
		pastExp=expRSales;
		
		if(pastExp<LAG_realSales.get(LAG_realSales.size()-2)) {

			expRSales=expRSales+Main.getNextFoldedGaussRandom()*steadyStateOutput;
			//expRSales++;
			q1Count++;
		}else if(pastExp>LAG_realSales.get(LAG_realSales.size()-2)) {
			expRSales=expRSales-Main.getNextFoldedGaussRandom()*steadyStateOutput;
			//expRSales--;
	
			q2Count++;
		}	
		
	}
	
	public void updatePriceJMAB2() {

		double averageMarkup=(avPrice/priceLowerBound)-1;
		this.avMarkup=averageMarkup;
		
		if(pastExp<LAG_realSales.get(LAG_realSales.size()-2)) {
	
			markUp = averageMarkup+Main.getNextFoldedGaussRandom()*0.1;
			
			q3Count++;
			q3Sum+=Math.abs(markUp-avMarkup);
		}else if(pastExp>LAG_realSales.get(LAG_realSales.size()-2)) {
					
			markUp = averageMarkup-Main.getNextFoldedGaussRandom()*0.1;
			
			q4Count++;
			q4Sum+=Math.abs(markUp-avMarkup);
		}
		
		if(markUp<0) {
			markUp=0;
		}
		
		price=priceLowerBound*(1+markUp);

		pastPastSales=pastSales;
		pastSales=0;
		
		
	}
	
	public void updatePrice() {
		double delta = pastExp-LAG_realSales.get(LAG_realSales.size()-2);
			
		double averageMarkup=(avPrice/priceLowerBound)-1;
		this.avMarkup=averageMarkup;
		
		if(delta<0) {
	
			markUp = markUp*(1+Main.getNextFoldedGaussRandom());
			
			q3Count++;
			q3Sum+=Math.abs(markUp-avMarkup);
		}else if(delta>0) {
					
			markUp = markUp*(1-Main.getNextFoldedGaussRandom());
			
			q4Count++;
			q4Sum+=Math.abs(markUp-avMarkup);
		}
		
		if(delta==0) {
			System.out.println("DELTA ==0 ###################################################################");
		}
		
		if(markUp<0) {
			markUp=0;
		}
		
		price=priceLowerBound*(1+markUp);
		pastPastSales=pastSales;
		pastSales=0;
		
		
	}
	
public void updateExpectationsWithAvPrice() {
		
		pastExp=expRSales;
		
		double delta = pastExp-LAG_realSales.get(LAG_realSales.size()-2);
			
		if(delta<0&&this.price>this.avPrice) {
			
			expRSales=expRSales*(1+Main.getNextFoldedGaussRandom());
			
			q1Count++;
		}else if(delta>0&&this.price<this.avPrice) {

			expRSales=expRSales*(1-Main.getNextFoldedGaussRandom());
			
			q2Count++;
		}
	
		
	}

	public void updateExpectationsWithAvPriceAssenza() {
	
	pastExp=expRSales;
	
	double delta = pastExp-LAG_realSales.get(LAG_realSales.size()-2);
	
	double param = 0.9;
	
	if(delta<=0&&this.price>this.avPrice) {
		
		expRSales=expRSales+param*-delta;
		
		q1Count++;
	}else if(delta>0&&this.price<this.avPrice) {

		expRSales=expRSales-param*delta;
		
		q2Count++;
	}
	
	
}
	
	public void updatePriceWithAveragePrice() {
		double delta = pastExp-LAG_realSales.get(LAG_realSales.size()-2);
		
		if(delta<0&&this.price<this.avPrice) {
			markUp=markUp*(1+Main.getNextFoldedGaussRandom());
			q3Count++;
		}else if(delta>0&&this.price>this.avPrice) {
			markUp=markUp*(1-Main.getNextFoldedGaussRandom());
			q4Count++;
		}
		
		if(markUp<0) {
			markUp=0;
		}
		
		price=priceLowerBound*(1+markUp);
		pastPastSales=pastSales;
		pastSales=0;
	}
	
	public void updateExpectationsDawidGatti() {
		
		pastExp=expRSales;
		double invSalesRatio = LAG_inventories.get(LAG_inventories.size()-1)/LAG_realSales.get(LAG_realSales.size()-2);

		if(LAG_inventories.get(LAG_inventories.size()-1)/LAG_realSales.get(LAG_realSales.size()-2)<ConsumptionFirm.minInvRatio&&this.price>this.avPrice) {
			
			expRSales=expRSales*(1+Main.getNextFoldedGaussRandom());
			
			q1Count++;
		}else if(LAG_inventories.get(LAG_inventories.size()-1)/LAG_realSales.get(LAG_realSales.size()-2)>ConsumptionFirm.maxInvRatio&&this.price<this.avPrice) {

			expRSales=expRSales*(1-Main.getNextFoldedGaussRandom());
			
			q2Count++;
		}
		
		
	}
		
		public void updatePriceDawidGatti() {
			
			if(LAG_inventories.get(LAG_inventories.size()-1)/LAG_realSales.get(LAG_realSales.size()-2)<ConsumptionFirm.minInvRatio&&this.price<this.avPrice) {
				markUp=markUp*(1+Main.getNextFoldedGaussRandom());
				
			q3Count++;
				q3Sum+=Math.abs(markUp-avMarkup);
			}else if(LAG_inventories.get(LAG_inventories.size()-1)/LAG_realSales.get(LAG_realSales.size()-2)>ConsumptionFirm.maxInvRatio&&this.price>this.avPrice) {
				markUp=markUp*(1-Main.getNextFoldedGaussRandom());

				q4Count++;
				q4Sum+=Math.abs(markUp-avMarkup);
			}
			
			if(markUp<0) {
				markUp=0;
			}
			
			price=priceLowerBound*(1+markUp);
			pastPastSales=pastSales;
			pastSales=0;
			
			
		}
		
	public void amortisation() {
		
		ArrayList<kGood> itemsToRemove = new ArrayList<kGood>();
		
		this.depreciation = 0;
		
		for(kGood item: kGoods) {			
			item.age++;	
			
			if(item.age>item.duration) {
				itemsToRemove.add(item);
				this.depreciation++;
			}
		}
		
		
		
		for(kGood item: itemsToRemove) {
			kGoods.remove(item);
		}
		
		itemsToRemove.clear();

	}
	
	public static double sd (ArrayList<Double> table)
	{
	    // Step 1: 
	    double mean = mean(table);
	    double temp = 0;

	    for (int i = 0; i < table.size(); i++)
	    {
	        double val = table.get(i);

	        // Step 2:
	        double squrDiffToMean = Math.pow(val - mean, 2);

	        // Step 3:
	        temp += squrDiffToMean;
	    }

	    // Step 4:
	    double meanOfDiffs = (double) temp / (double) (table.size());

	    // Step 5:
	    return Math.sqrt(meanOfDiffs);
	}
	
	public static double mean (ArrayList<Double> table)
    {
        double total = 0;

        for ( int i= 0;i < table.size(); i++)
        {
            double currentNum = table.get(i);
            total+= currentNum;
        }
        return total/table.size();
    }
	
	
}
