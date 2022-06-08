package beast.evolution.operators;


import java.util.ArrayList;
import java.util.List;

import beast.core.Input;
import beast.core.Operator;
import beast.core.parameter.IntegerParameter;
import beast.core.parameter.RealParameter;
import beast.util.Randomizer;

public class UnobservedFrequencyConstantExchanger extends Operator{
	public final Input<RealParameter> parameterInput = new Input<>("parameter", "if specified, this parameter is scaled");
	public Input<List<RealParameter>> frequenciesInput = new Input<>("frequencies",
            "List of frequencies", new ArrayList<RealParameter>());
	
	public final Input<IntegerParameter> nstatesInput = new Input<>("nstates", "number of states for each mutation rate");
    public final Input<Double> scaleFactorInput = new Input<>("scaleFactor", "scaling factor: range from 0 to 1. Close to zero is very large jumps, close to 1.0 is very small jumps.", 0.75);
    
    int n;
    int x;
    
	double nstates;
	
    private double scaleFactor;
    RealParameter freqpar;
    double newValueUnobs;
    double newValueObs;
    
	@Override
	public void initAndValidate() {
		scaleFactor = scaleFactorInput.get();

	}

	@Override
	public double proposal() {
		final RealParameter param = parameterInput.get();
		final double oldValue = param.getValue();
        final double scale = getScaler();
		double newValue = oldValue*scale;
		param.setValue(0, newValue);
		List<RealParameter> freqs = frequenciesInput.get();
		for(int i=0; i<freqs.size(); i++) {
			freqpar = frequenciesInput.get().get(i);
			n = freqpar.getDimension();
			x = n-1;
			nstates = nstatesInput.get().getArrayValue(i);
			
			freqpar.setValue(x, newValue);
			newValueObs = (1-newValue)/(x);
			for(int j=0; j<x; j++) {
				freqpar.setValue(j, newValueObs);
			}
			
		}
		
		return -Math.log(scale);
	}
	
	protected double getScaler() {
        return (scaleFactor + (Randomizer.nextDouble() * ((1.0 / scaleFactor) - scaleFactor)));
    }
}
