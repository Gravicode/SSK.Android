package org.apache.poi.p009ss.formula;

import java.util.Stack;
import org.apache.poi.hssf.record.formula.AttrPtg;
import org.apache.poi.hssf.record.formula.MemAreaPtg;
import org.apache.poi.hssf.record.formula.MemErrPtg;
import org.apache.poi.hssf.record.formula.MemFuncPtg;
import org.apache.poi.hssf.record.formula.OperationPtg;
import org.apache.poi.hssf.record.formula.ParenthesisPtg;
import org.apache.poi.hssf.record.formula.Ptg;

/* renamed from: org.apache.poi.ss.formula.FormulaRenderer */
public class FormulaRenderer {
    public static String toFormulaString(FormulaRenderingWorkbook book, Ptg[] ptgs) {
        if (ptgs == null || ptgs.length == 0) {
            throw new IllegalArgumentException("ptgs must not be null");
        }
        Stack<String> stack = new Stack<>();
        for (Ptg ptg : ptgs) {
            if (!(ptg instanceof MemAreaPtg) && !(ptg instanceof MemFuncPtg) && !(ptg instanceof MemErrPtg)) {
                if (ptg instanceof ParenthesisPtg) {
                    String contents = (String) stack.pop();
                    StringBuilder sb = new StringBuilder();
                    sb.append("(");
                    sb.append(contents);
                    sb.append(")");
                    stack.push(sb.toString());
                } else if (ptg instanceof AttrPtg) {
                    AttrPtg attrPtg = (AttrPtg) ptg;
                    if (!attrPtg.isOptimizedIf() && !attrPtg.isOptimizedChoose() && !attrPtg.isSkip() && !attrPtg.isSpace() && !attrPtg.isSemiVolatile()) {
                        if (attrPtg.isSum()) {
                            stack.push(attrPtg.toFormulaString(getOperands(stack, attrPtg.getNumberOfOperands())));
                        } else {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Unexpected tAttr: ");
                            sb2.append(attrPtg.toString());
                            throw new RuntimeException(sb2.toString());
                        }
                    }
                } else if (ptg instanceof WorkbookDependentFormula) {
                    stack.push(((WorkbookDependentFormula) ptg).toFormulaString(book));
                } else if (!(ptg instanceof OperationPtg)) {
                    stack.push(ptg.toFormulaString());
                } else {
                    OperationPtg o = (OperationPtg) ptg;
                    stack.push(o.toFormulaString(getOperands(stack, o.getNumberOfOperands())));
                }
            }
        }
        if (stack.isEmpty() != 0) {
            throw new IllegalStateException("Stack underflow");
        }
        String result = (String) stack.pop();
        if (stack.isEmpty()) {
            return result;
        }
        throw new IllegalStateException("too much stuff left on the stack");
    }

    private static String[] getOperands(Stack<String> stack, int nOperands) {
        String[] operands = new String[nOperands];
        for (int j = nOperands - 1; j >= 0; j--) {
            if (stack.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Too few arguments supplied to operation. Expected (");
                sb.append(nOperands);
                sb.append(") operands but got (");
                sb.append((nOperands - j) - 1);
                sb.append(")");
                throw new IllegalStateException(sb.toString());
            }
            operands[j] = (String) stack.pop();
        }
        return operands;
    }
}
