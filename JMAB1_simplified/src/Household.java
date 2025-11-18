import java.util.ArrayList;

public class Household {

	public double demand;
	public ConsumptionFirm previousSupplier;
	public AbstractFirm employer;
	public double wage;
	public double netWealth;
	
	public static ArrayList<Household> employedHhs = new ArrayList<Household>();
	public static ArrayList<Household> unemployedHhs = new ArrayList<Household>();
	
	
	public void computeDemand() {
		
		this.demand=wage;
		
	}
	
}
