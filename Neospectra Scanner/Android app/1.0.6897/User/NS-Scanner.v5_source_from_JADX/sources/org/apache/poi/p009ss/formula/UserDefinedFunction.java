package org.apache.poi.p009ss.formula;

import org.apache.poi.hssf.record.formula.eval.NameEval;
import org.apache.poi.hssf.record.formula.eval.NameXEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.record.formula.functions.FreeRefFunction;
import org.apache.poi.p009ss.formula.eval.NotImplementedException;

/* renamed from: org.apache.poi.ss.formula.UserDefinedFunction */
final class UserDefinedFunction implements FreeRefFunction {
    public static final FreeRefFunction instance = new UserDefinedFunction();

    private UserDefinedFunction() {
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        String functionName;
        int nIncomingArgs = args.length;
        if (nIncomingArgs < 1) {
            throw new RuntimeException("function name argument missing");
        }
        ValueEval nameArg = args[0];
        if (nameArg instanceof NameEval) {
            functionName = ((NameEval) nameArg).getFunctionName();
        } else if (nameArg instanceof NameXEval) {
            functionName = ec.getWorkbook().resolveNameXText(((NameXEval) nameArg).getPtg());
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("First argument should be a NameEval, but got (");
            sb.append(nameArg.getClass().getName());
            sb.append(")");
            throw new RuntimeException(sb.toString());
        }
        FreeRefFunction targetFunc = ec.findUserDefinedFunction(functionName);
        if (targetFunc == null) {
            throw new NotImplementedException(functionName);
        }
        int nOutGoingArgs = nIncomingArgs - 1;
        ValueEval[] outGoingArgs = new ValueEval[nOutGoingArgs];
        System.arraycopy(args, 1, outGoingArgs, 0, nOutGoingArgs);
        return targetFunc.evaluate(outGoingArgs, ec);
    }
}
