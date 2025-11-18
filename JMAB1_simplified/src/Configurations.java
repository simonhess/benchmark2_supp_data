
public class Configurations {
	
	// These are the configuration names. Run the main Method with a configuration name to run the program with the respective configuration.
	
	public enum configurationNames{
		STANDARD,
		EXP_DEBUGGING_JMAB1,
		EXP_DEBUGGING_JMAB1_PS_0_5,
		EXP_DEBUGGING_JMAB1_V_1,
		EXP_DEBUGGING_JMAB2,
		PRICING_COMPARISON_JMAB1,
		PRICING_COMPARISON_JMAB2,
		PRICING_COMPARISON_DAWID_GATTI,
		PRICING_COMPARISON_DAWID_GATTI_SWITCHING,
		PRICING_COMPARISON_ASSENZA,
		PRICING_COMPARISON_ASSENZA_SWITCHING,
		MICRO_INVESTMENT_EXPERIMENT_JMAB1,
		MACRO_INVESTMENT_EXPERIMENT_JMAB1,
		MACRO_INVESTMENT_EXPERIMENT_JMAB2,
	}
	
	public static void setConfiguration(String configName) {
		
		configurationNames configEnumName = configurationNames.valueOf(configName);
		
		if(configName == "") {
			configEnumName = configurationNames.STANDARD;
		}
		
		switch(configEnumName) {
		
		case STANDARD:
			
			break;
		case EXP_DEBUGGING_JMAB1:
			ConsumptionFirm.capacityWeight=0.0;
			ConsumptionFirm.cashFlowRateWeight=0.0;
			ConsumptionFirm.currentExpMechanism = ConsumptionFirm.expMechanisms.JMAB1;
			ConsumptionFirm.currentPricingMechanism = ConsumptionFirm.pricingMechanisms.JMAB2;
			break;
		case EXP_DEBUGGING_JMAB1_PS_0_5:
			ConsumptionFirm.capacityWeight=0.0;
			ConsumptionFirm.cashFlowRateWeight=0.0;
			ConsumptionFirm.currentExpMechanism = ConsumptionFirm.expMechanisms.JMAB1;
			ConsumptionFirm.currentPricingMechanism = ConsumptionFirm.pricingMechanisms.JMAB2;
			Main.switchingRate = 0.5;
			break;
		case EXP_DEBUGGING_JMAB1_V_1:
			ConsumptionFirm.capacityWeight=0.0;
			ConsumptionFirm.cashFlowRateWeight=0.0;
			ConsumptionFirm.currentExpMechanism = ConsumptionFirm.expMechanisms.JMAB1;
			ConsumptionFirm.currentPricingMechanism = ConsumptionFirm.pricingMechanisms.JMAB2;
			ConsumptionFirm.invRatio = 1.0;
			break;
		case EXP_DEBUGGING_JMAB2:
			ConsumptionFirm.capacityWeight=0.0;
			ConsumptionFirm.cashFlowRateWeight=0.0;
			ConsumptionFirm.currentExpMechanism = ConsumptionFirm.expMechanisms.JMAB2;
			ConsumptionFirm.currentPricingMechanism = ConsumptionFirm.pricingMechanisms.JMAB2;
			break;
		case PRICING_COMPARISON_JMAB1:
			ConsumptionFirm.capacityWeight=0.0;
			ConsumptionFirm.cashFlowRateWeight=0.0;
			ConsumptionFirm.currentExpMechanism = ConsumptionFirm.expMechanisms.JMAB1;
			ConsumptionFirm.currentPricingMechanism = ConsumptionFirm.pricingMechanisms.JMAB1;
			break;
		case PRICING_COMPARISON_JMAB2:
			ConsumptionFirm.capacityWeight=0.0;
			ConsumptionFirm.cashFlowRateWeight=0.0;
			ConsumptionFirm.currentExpMechanism = ConsumptionFirm.expMechanisms.JMAB2;
			ConsumptionFirm.currentPricingMechanism = ConsumptionFirm.pricingMechanisms.JMAB2;
			break;
		case PRICING_COMPARISON_DAWID_GATTI:
			ConsumptionFirm.capacityWeight=0.0;
			ConsumptionFirm.cashFlowRateWeight=0.0;
			ConsumptionFirm.currentExpMechanism = ConsumptionFirm.expMechanisms.DAWIDGATTI;
			ConsumptionFirm.currentPricingMechanism = ConsumptionFirm.pricingMechanisms.DAWIDGATTI;
			Main.switchingStrategy = false;
			break;
		case PRICING_COMPARISON_DAWID_GATTI_SWITCHING:
			ConsumptionFirm.capacityWeight=0.0;
			ConsumptionFirm.cashFlowRateWeight=0.0;
			ConsumptionFirm.currentExpMechanism = ConsumptionFirm.expMechanisms.DAWIDGATTI;
			ConsumptionFirm.currentPricingMechanism = ConsumptionFirm.pricingMechanisms.DAWIDGATTI;
			break;
		case PRICING_COMPARISON_ASSENZA:
			ConsumptionFirm.capacityWeight=0.0;
			ConsumptionFirm.cashFlowRateWeight=0.0;
			ConsumptionFirm.currentExpMechanism = ConsumptionFirm.expMechanisms.ASSENZA;
			ConsumptionFirm.currentPricingMechanism = ConsumptionFirm.pricingMechanisms.ASSENZA;
			Main.switchingStrategy = false;
			break;
		case PRICING_COMPARISON_ASSENZA_SWITCHING:
			ConsumptionFirm.capacityWeight=0.0;
			ConsumptionFirm.cashFlowRateWeight=0.0;
			ConsumptionFirm.currentExpMechanism = ConsumptionFirm.expMechanisms.ASSENZA;
			ConsumptionFirm.currentPricingMechanism = ConsumptionFirm.pricingMechanisms.ASSENZA;
			break;
		case MICRO_INVESTMENT_EXPERIMENT_JMAB1:
			ConsumptionFirm.capacityWeight=0.02;
			ConsumptionFirm.cashFlowRateWeight=0.0;
			ConsumptionFirm.alternatingFixedOutput=true;
			ConsumptionFirm.currentInvestmentMechanism = ConsumptionFirm.investmentMechanisms.JMAB1;
			Main.cSize = 2;
			break;
		case MACRO_INVESTMENT_EXPERIMENT_JMAB1:
			ConsumptionFirm.capacityWeight=0.02;
			ConsumptionFirm.cashFlowRateWeight=0.0;
			ConsumptionFirm.alternatingFixedOutput=false;
			ConsumptionFirm.currentInvestmentMechanism = ConsumptionFirm.investmentMechanisms.JMAB1;
			ConsumptionFirm.currentExpMechanism = ConsumptionFirm.expMechanisms.JMAB2;
			ConsumptionFirm.currentPricingMechanism = ConsumptionFirm.pricingMechanisms.JMAB2;
			Main.cSize = 2;
            Main.investmentShockOneFirm = true;
			break;
		case MACRO_INVESTMENT_EXPERIMENT_JMAB2:
			ConsumptionFirm.capacityWeight=0.02;
			ConsumptionFirm.cashFlowRateWeight=0.0;
			ConsumptionFirm.alternatingFixedOutput=false;
			ConsumptionFirm.currentInvestmentMechanism = ConsumptionFirm.investmentMechanisms.JMAB2;
			ConsumptionFirm.currentExpMechanism = ConsumptionFirm.expMechanisms.JMAB2;
			ConsumptionFirm.currentPricingMechanism = ConsumptionFirm.pricingMechanisms.JMAB2;
			Main.cSize = 2;
			Main.investmentShockOneFirm = true;
			break;
		}
		
		
		
	}

}
