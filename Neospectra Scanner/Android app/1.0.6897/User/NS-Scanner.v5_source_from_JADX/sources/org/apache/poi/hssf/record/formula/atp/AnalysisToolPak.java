package org.apache.poi.hssf.record.formula.atp;

import java.util.HashMap;
import java.util.Map;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.record.formula.functions.FreeRefFunction;
import org.apache.poi.hssf.record.formula.udf.UDFFinder;
import org.apache.poi.p009ss.formula.OperationEvaluationContext;
import org.apache.poi.p009ss.formula.eval.NotImplementedException;

public final class AnalysisToolPak implements UDFFinder {
    public static final UDFFinder instance = new AnalysisToolPak();
    private final Map<String, FreeRefFunction> _functionsByName = createFunctionsMap();

    private static final class NotImplemented implements FreeRefFunction {
        private final String _functionName;

        public NotImplemented(String functionName) {
            this._functionName = functionName;
        }

        public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
            throw new NotImplementedException(this._functionName);
        }
    }

    private AnalysisToolPak() {
    }

    public FreeRefFunction findFunction(String name) {
        return (FreeRefFunction) this._functionsByName.get(name);
    }

    private Map<String, FreeRefFunction> createFunctionsMap() {
        Map<String, FreeRefFunction> m = new HashMap<>(100);
        m71r(m, "ACCRINT", null);
        m71r(m, "ACCRINTM", null);
        m71r(m, "AMORDEGRC", null);
        m71r(m, "AMORLINC", null);
        m71r(m, "BESSELI", null);
        m71r(m, "BESSELJ", null);
        m71r(m, "BESSELK", null);
        m71r(m, "BESSELY", null);
        m71r(m, "BIN2DEC", null);
        m71r(m, "BIN2HEX", null);
        m71r(m, "BIN2OCT", null);
        m71r(m, "CO MPLEX", null);
        m71r(m, "CONVERT", null);
        m71r(m, "COUPDAYBS", null);
        m71r(m, "COUPDAYS", null);
        m71r(m, "COUPDAYSNC", null);
        m71r(m, "COUPNCD", null);
        m71r(m, "COUPNUM", null);
        m71r(m, "COUPPCD", null);
        m71r(m, "CUMIPMT", null);
        m71r(m, "CUMPRINC", null);
        m71r(m, "DEC2BIN", null);
        m71r(m, "DEC2HEX", null);
        m71r(m, "DEC2OCT", null);
        m71r(m, "DELTA", null);
        m71r(m, "DISC", null);
        m71r(m, "DOLLARDE", null);
        m71r(m, "DOLLARFR", null);
        m71r(m, "DURATION", null);
        m71r(m, "EDATE", null);
        m71r(m, "EFFECT", null);
        m71r(m, "EOMONTH", null);
        m71r(m, "ERF", null);
        m71r(m, "ERFC", null);
        m71r(m, "FACTDOUBLE", null);
        m71r(m, "FVSCHEDULE", null);
        m71r(m, "GCD", null);
        m71r(m, "GESTEP", null);
        m71r(m, "HEX2BIN", null);
        m71r(m, "HEX2DEC", null);
        m71r(m, "HEX2OCT", null);
        m71r(m, "IMABS", null);
        m71r(m, "IMAGINARY", null);
        m71r(m, "IMARGUMENT", null);
        m71r(m, "IMCONJUGATE", null);
        m71r(m, "IMCOS", null);
        m71r(m, "IMDIV", null);
        m71r(m, "IMEXP", null);
        m71r(m, "IMLN", null);
        m71r(m, "IMLOG10", null);
        m71r(m, "IMLOG2", null);
        m71r(m, "IMPOWER", null);
        m71r(m, "IMPRODUCT", null);
        m71r(m, "IMREAL", null);
        m71r(m, "IMSIN", null);
        m71r(m, "IMSQRT", null);
        m71r(m, "IMSUB", null);
        m71r(m, "IMSUM", null);
        m71r(m, "INTRATE", null);
        m71r(m, "ISEVEN", ParityFunction.IS_EVEN);
        m71r(m, "ISODD", ParityFunction.IS_ODD);
        m71r(m, "LCM", null);
        m71r(m, "MDURATION", null);
        m71r(m, "MROUND", null);
        m71r(m, "MULTINOMIAL", null);
        m71r(m, "NETWORKDAYS", null);
        m71r(m, "NOMINAL", null);
        m71r(m, "OCT2BIN", null);
        m71r(m, "OCT2DEC", null);
        m71r(m, "OCT2HEX", null);
        m71r(m, "ODDFPRICE", null);
        m71r(m, "ODDFYIELD", null);
        m71r(m, "ODDLPRICE", null);
        m71r(m, "ODDLYIELD", null);
        m71r(m, "PRICE", null);
        m71r(m, "PRICEDISC", null);
        m71r(m, "PRICEMAT", null);
        m71r(m, "QUOTIENT", null);
        m71r(m, "RANDBETWEEN", RandBetween.instance);
        m71r(m, "RECEIVED", null);
        m71r(m, "SERIESSUM", null);
        m71r(m, "SQRTPI", null);
        m71r(m, "TBILLEQ", null);
        m71r(m, "TBILLPRICE", null);
        m71r(m, "TBILLYIELD", null);
        m71r(m, "WEEKNUM", null);
        m71r(m, "WORKDAY", null);
        m71r(m, "XIRR", null);
        m71r(m, "XNPV", null);
        m71r(m, "YEARFRAC", YearFrac.instance);
        m71r(m, "YIELD", null);
        m71r(m, "YIELDDISC", null);
        m71r(m, "YIELDMAT", null);
        return m;
    }

    /* renamed from: r */
    private static void m71r(Map<String, FreeRefFunction> m, String functionName, FreeRefFunction pFunc) {
        m.put(functionName, pFunc == null ? new NotImplemented(functionName) : pFunc);
    }
}
