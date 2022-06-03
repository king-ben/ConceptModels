package beast.core.util;

import java.io.PrintStream;

import beast.core.BEASTObject;
import beast.core.Input;
import beast.core.Loggable;
import beast.core.parameter.RealParameter;

public class UnobservedFrequencyLogger extends BEASTObject implements Loggable {
	public final Input<RealParameter> parameterInput = new Input<>("parameter", "if specified, this parameter is scaled");
	RealParameter param;
	int ind;
	@Override
	public void initAndValidate() {
		param = parameterInput.get();
		ind=param.getDimension()-1;
	}

	@Override
	public void init(PrintStream out) {
		out.print("unobserved."+((BEASTObject)param).getID() + "\t");
	}

	@Override
	public void log(long nSample, PrintStream out) {
		
		double funob = param.getArrayValue(ind);
		out.print(funob + "\t");
	}
	@Override
	public void close(PrintStream out) {
		// TODO Auto-generated method stub
		
	}

}
