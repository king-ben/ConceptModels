package beast.evolution.operators;

import java.util.ArrayList;
import java.util.List;

import beast.core.Input;
import beast.core.Operator;
import beast.core.parameter.IntegerParameter;
import beast.core.parameter.RealParameter;
import beast.util.Randomizer;

public class MutationRateReweight extends Operator{
	public final Input<RealParameter> parameterInput = new Input<>("parameter", "if specified, this parameter is scaled");
	public Input<List<RealParameter>> mutationRatesInput = new Input<>("mutationrate",
            "List of mutation rates", new ArrayList<RealParameter>());
	
	public final Input<IntegerParameter> nstatesInput = new Input<>("nstates", "number of states for each mutation rate");
    public final Input<Double> scaleFactorInput = new Input<>("scaleFactor", "scaling factor: range from 0 to 1. Close to zero is very large jumps, close to 1.0 is very small jumps.", 0.75);
    int n;
    
	double[] nstates_powered;
	double spread_parameter;
	
    private double scaleFactor;
    RealParameter mutpar;

	@Override
	public void initAndValidate() {
		scaleFactor = scaleFactorInput.get();
		n=nstatesInput.get().getDimension();
		nstates_powered = new double[n];
		for(int i=0; i<n; i++) {
			nstates_powered[i] = Math.pow(nstatesInput.get().getArrayValue(i), 1.0);
		}
		
	}

	@Override
	public double proposal() {

		final RealParameter param = parameterInput.get();
		final double oldValue = param.getValue();
        final double scale = getScaler();
		double newValue = oldValue*scale;
		param.setValue(0, newValue);
		List<RealParameter> mutationRates = mutationRatesInput.get();
		double nstates_powered_total = 0;
		for(int i=0; i<n; i++) {
			nstates_powered[i] =Math.pow(nstatesInput.get().getArrayValue(i), newValue);
			nstates_powered_total += nstates_powered[i];
		}
		Double powered_mean=nstates_powered_total/n;
		for(int i=0; i<n ; i++) {
			mutpar = mutationRatesInput.get().get(i);
			Double mutnew=nstates_powered[i]/powered_mean;
			mutpar.setValue(0, mutnew);
			mutationRates.set(i, mutpar);
		}
		return -Math.log(scale);
	}
	
	protected double getScaler() {
        return (scaleFactor + (Randomizer.nextDouble() * ((1.0 / scaleFactor) - scaleFactor)));
    }
}
