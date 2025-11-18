import java.util.ArrayList;
import java.util.Random;

public class CapitalFirm extends AbstractFirm{

	public double pastSales;
	public double expRSales;
	public double pastExp;
	public double markUp;
	public static double steadyStateMarkup = 0.075;
	public double price;
	public static double priceLowerBound =2.5;
	public int id;
	public double pastPastSales;

	public double positiveSumPositiveFirms=0;
	public double avDelta =0;
	public double avPrice = 0;
	
	public static double laborProductivity = 2;
	public static double invRatio=0.1;
	
	public ArrayList<kGood> kGoods = new ArrayList<kGood>();
	public int kGoodDuration;
	
	public ArrayList<Double> LAG_realSales = new ArrayList<Double>();
	
	public void produce() {
		
		double expSales = expRSales;

		double invQuantity = kGoods.size();
		double expInv=expSales*invRatio;
		double output = Math.max(0,expSales+(expInv-invQuantity));
		//output=2000;
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ OUTPUT "+output);
		for(int i = 0; i < output; i++) {
			kGood newkGood = new kGood();
			newkGood.price=this.price;
			newkGood.duration = kGoodDuration;
			newkGood.quantity = 1;
			kGoods.add(newkGood);
		}
		
		// Hire workers
				
				double laborDemand = output/laborProductivity;
				
				laborDemand = Math.round(laborDemand);
				

				while(laborDemand>0) {
					
					Household hh = Household.unemployedHhs.get(0);
					Household.employedHhs.add(hh);
					Household.unemployedHhs.remove(hh);
					hh.wage=5;
					hh.employer=this;
					laborDemand--;
					
				}
		
		
	}
	
	public void updateExpectations() {
		
		
		pastExp=expRSales;
		
		double delta = pastExp-LAG_realSales.get(LAG_realSales.size()-2);
		//delta=0;
		
		if(delta<0) {
			expRSales=expRSales+1;
		}else if(delta>0) {
			expRSales=expRSales-1;
		}
		
	}
	
	public void updatePrice() {
		double delta = pastExp-LAG_realSales.get(LAG_realSales.size()-2);
				
		
		if(delta<0) {


		}else if(delta>0) {

		}
		if(delta==0) {
			System.out.println("DELTA ==0 ##");
		}
		
		if(markUp<0) {
			markUp=0;
		}
		
		price=priceLowerBound*(1+markUp);
		pastPastSales=pastSales;
		pastSales=0;
	}
	
	
}
