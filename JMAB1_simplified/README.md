# benchmark2_supp_data
This folder contains the simplified JMAB 1.0 model which was used to conduct experiments on the JMAB 1.0 mechanisms. 

To reproduce the data used for the plots in the paper import the project into Eclipse and run the Main class with the experiment name as an argument (EXP_DEBUGGING_JMAB1 for example).

The following experiments are available:

Experiments on the expectation of firms sales:\
EXP_DEBUGGING_JMAB1\
EXP_DEBUGGING_JMAB1_PS_0_5\
EXP_DEBUGGING_JMAB1_V_1\
EXP_DEBUGGING_JMAB2

Experiments on the different price-quantity mechanisms:\
PRICING_COMPARISON_JMAB1\
PRICING_COMPARISON_JMAB2\
PRICING_COMPARISON_DAWID_GATTI\
PRICING_COMPARISON_DAWID_GATTI_SWITCHING\
PRICING_COMPARISON_ASSENZA\
PRICING_COMPARISON_ASSENZA_SWITCHING

Experiments on investment on the micro and macro level:\
MICRO_INVESTMENT_EXPERIMENT_JMAB1\
MACRO_INVESTMENT_EXPERIMENT_JMAB1\
MACRO_INVESTMENT_EXPERIMENT_JMAB2

The Configurations.java file in the /src folder contains the exact model parameters for each experiment.

## Reproduce the plots in the JMAB 2.0 paper

To reproduce the plots contained in the JMAB 2.0 paper first run the project in Eclipse once for each experiment.

After that run the GeneratePlots.R file in R studio. The plots will be created and saved in the /figures folder.