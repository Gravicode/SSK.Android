package org.apache.poi.hssf.record.aggregates;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.model.RecordStream;
import org.apache.poi.hssf.record.CFHeaderRecord;
import org.apache.poi.hssf.record.CFRuleRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;
import org.apache.poi.hssf.record.formula.AreaErrPtg;
import org.apache.poi.hssf.record.formula.AreaPtg;
import org.apache.poi.hssf.record.formula.FormulaShifter;
import org.apache.poi.hssf.record.formula.Ptg;
import org.apache.poi.p009ss.util.CellRangeAddress;

public final class CFRecordsAggregate extends RecordAggregate {
    private static final int MAX_CONDTIONAL_FORMAT_RULES = 3;
    private final CFHeaderRecord header;
    private final List rules;

    private CFRecordsAggregate(CFHeaderRecord pHeader, CFRuleRecord[] pRules) {
        if (pHeader == null) {
            throw new IllegalArgumentException("header must not be null");
        } else if (pRules == null) {
            throw new IllegalArgumentException("rules must not be null");
        } else if (pRules.length > 3) {
            throw new IllegalArgumentException("No more than 3 rules may be specified");
        } else if (pRules.length != pHeader.getNumberOfConditionalFormats()) {
            throw new RuntimeException("Mismatch number of rules");
        } else {
            this.header = pHeader;
            this.rules = new ArrayList(3);
            for (CFRuleRecord add : pRules) {
                this.rules.add(add);
            }
        }
    }

    public CFRecordsAggregate(CellRangeAddress[] regions, CFRuleRecord[] rules2) {
        this(new CFHeaderRecord(regions, rules2.length), rules2);
    }

    public static CFRecordsAggregate createCFAggregate(RecordStream rs) {
        Record rec = rs.getNext();
        if (rec.getSid() != 432) {
            StringBuilder sb = new StringBuilder();
            sb.append("next record sid was ");
            sb.append(rec.getSid());
            sb.append(" instead of ");
            sb.append(432);
            sb.append(" as expected");
            throw new IllegalStateException(sb.toString());
        }
        CFHeaderRecord header2 = (CFHeaderRecord) rec;
        CFRuleRecord[] rules2 = new CFRuleRecord[header2.getNumberOfConditionalFormats()];
        for (int i = 0; i < rules2.length; i++) {
            rules2[i] = (CFRuleRecord) rs.getNext();
        }
        return new CFRecordsAggregate(header2, rules2);
    }

    public CFRecordsAggregate cloneCFAggregate() {
        CFRuleRecord[] newRecs = new CFRuleRecord[this.rules.size()];
        for (int i = 0; i < newRecs.length; i++) {
            newRecs[i] = (CFRuleRecord) getRule(i).clone();
        }
        return new CFRecordsAggregate((CFHeaderRecord) this.header.clone(), newRecs);
    }

    public CFHeaderRecord getHeader() {
        return this.header;
    }

    private void checkRuleIndex(int idx) {
        if (idx < 0 || idx >= this.rules.size()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Bad rule record index (");
            sb.append(idx);
            sb.append(") nRules=");
            sb.append(this.rules.size());
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public CFRuleRecord getRule(int idx) {
        checkRuleIndex(idx);
        return (CFRuleRecord) this.rules.get(idx);
    }

    public void setRule(int idx, CFRuleRecord r) {
        if (r == null) {
            throw new IllegalArgumentException("r must not be null");
        }
        checkRuleIndex(idx);
        this.rules.set(idx, r);
    }

    public void addRule(CFRuleRecord r) {
        if (r == null) {
            throw new IllegalArgumentException("r must not be null");
        } else if (this.rules.size() >= 3) {
            throw new IllegalStateException("Cannot have more than 3 conditional format rules");
        } else {
            this.rules.add(r);
            this.header.setNumberOfConditionalFormats(this.rules.size());
        }
    }

    public int getNumberOfRules() {
        return this.rules.size();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CF]\n");
        if (this.header != null) {
            buffer.append(this.header.toString());
        }
        for (int i = 0; i < this.rules.size(); i++) {
            buffer.append(((CFRuleRecord) this.rules.get(i)).toString());
        }
        buffer.append("[/CF]\n");
        return buffer.toString();
    }

    public void visitContainedRecords(RecordVisitor rv) {
        rv.visitRecord(this.header);
        for (int i = 0; i < this.rules.size(); i++) {
            rv.visitRecord((CFRuleRecord) this.rules.get(i));
        }
    }

    public boolean updateFormulasAfterCellShift(FormulaShifter shifter, int currentExternSheetIx) {
        CellRangeAddress[] cellRanges = this.header.getCellRanges();
        List temp = new ArrayList();
        int i = 0;
        boolean changed = false;
        for (CellRangeAddress craOld : cellRanges) {
            CellRangeAddress craNew = shiftRange(shifter, craOld, currentExternSheetIx);
            if (craNew == null) {
                changed = true;
            } else {
                temp.add(craNew);
                if (craNew != craOld) {
                    changed = true;
                }
            }
        }
        if (changed) {
            int nRanges = temp.size();
            if (nRanges == 0) {
                return false;
            }
            CellRangeAddress[] newRanges = new CellRangeAddress[nRanges];
            temp.toArray(newRanges);
            this.header.setCellRanges(newRanges);
        }
        while (true) {
            int i2 = i;
            if (i2 >= this.rules.size()) {
                return true;
            }
            CFRuleRecord rule = (CFRuleRecord) this.rules.get(i2);
            Ptg[] ptgs = rule.getParsedExpression1();
            if (ptgs != null && shifter.adjustFormula(ptgs, currentExternSheetIx)) {
                rule.setParsedExpression1(ptgs);
            }
            Ptg[] ptgs2 = rule.getParsedExpression2();
            if (ptgs2 != null && shifter.adjustFormula(ptgs2, currentExternSheetIx)) {
                rule.setParsedExpression2(ptgs2);
            }
            i = i2 + 1;
        }
    }

    private static CellRangeAddress shiftRange(FormulaShifter shifter, CellRangeAddress cra, int currentExternSheetIx) {
        AreaPtg aptg = new AreaPtg(cra.getFirstRow(), cra.getLastRow(), cra.getFirstColumn(), cra.getLastColumn(), false, false, false, false);
        Ptg[] ptgs = {aptg};
        if (!shifter.adjustFormula(ptgs, currentExternSheetIx)) {
            return cra;
        }
        Ptg ptg0 = ptgs[0];
        if (ptg0 instanceof AreaPtg) {
            AreaPtg bptg = (AreaPtg) ptg0;
            return new CellRangeAddress(bptg.getFirstRow(), bptg.getLastRow(), bptg.getFirstColumn(), bptg.getLastColumn());
        } else if (ptg0 instanceof AreaErrPtg) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected shifted ptg class (");
            sb.append(ptg0.getClass().getName());
            sb.append(")");
            throw new IllegalStateException(sb.toString());
        }
    }
}
