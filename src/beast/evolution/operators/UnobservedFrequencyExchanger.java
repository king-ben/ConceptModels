package beast.evolution.operators;


import java.text.DecimalFormat;

import beast.core.Input;
import beast.core.Operator;
import beast.core.parameter.RealParameter;
import beast.util.Randomizer;

public class UnobservedFrequencyExchanger extends Operator{
	public final Input<RealParameter> parameterInput = new Input<>("parameter", "if specified, this parameter is scaled");
	final public Input<Double> windowSizeInput = new Input<>("windowSize", "the size of the window both up and down when using uniform interval OR standard deviation when using Gaussian", Input.Validate.REQUIRED);	int n;
	int x;
	double windowSize = 1;
	@Override
	public void initAndValidate() {
		windowSize = windowSizeInput.get();
		n = parameterInput.get().getDimension();
		x=n-1;
	}

	@Override
	public double proposal() {
		final RealParameter param = parameterInput.get();
		double oldValue=parameterInput.get().getArrayValue(x);
		double newValue = oldValue;
		newValue += Randomizer.nextDouble() * 2 * windowSize - windowSize;
		if (newValue < 0 || newValue > 1) {
            return Double.NEGATIVE_INFINITY;
        }
        if (newValue == oldValue) {
            // this saves calculating the posterior
            return Double.NEGATIVE_INFINITY;
        }
		double newValueObs = (1-newValue)/x;
		param.setValue(x, newValue);
		for(int i=0; i<x; i++) {
			param.setValue(i, newValueObs);
		}
		return 0.0;
	}
	
	 @Override
	    public void optimize(double logAlpha) {
	        // must be overridden by operator implementation to have an effect
	        double delta = calcDelta(logAlpha);

	        delta += Math.log(windowSize);
	        windowSize = Math.exp(delta);
	    }

	    @Override
	    public final String getPerformanceSuggestion() {
	        double prob = m_nNrAccepted / (m_nNrAccepted + m_nNrRejected + 0.0);
	        double targetProb = getTargetAcceptanceProbability();

	        double ratio = prob / targetProb;
	        if (ratio > 2.0) ratio = 2.0;
	        if (ratio < 0.5) ratio = 0.5;

	        // new scale factor
	        double newWindowSize = windowSize * ratio;

	        DecimalFormat formatter = new DecimalFormat("#.###");
	        if (prob < 0.10) {
	            return "Try setting window size to about " + formatter.format(newWindowSize);
	        } else if (prob > 0.40) {
	            return "Try setting window size to about " + formatter.format(newWindowSize);
	        } else return "";
	    }

}
