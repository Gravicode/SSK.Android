package org.apache.poi.hssf.record.formula;

public final class FormulaShifter {
    private final int _amountToMove;
    private final int _externSheetIndex;
    private final int _firstMovedIndex;
    private final int _lastMovedIndex;

    private FormulaShifter(int externSheetIndex, int firstMovedIndex, int lastMovedIndex, int amountToMove) {
        if (amountToMove == 0) {
            throw new IllegalArgumentException("amountToMove must not be zero");
        } else if (firstMovedIndex > lastMovedIndex) {
            throw new IllegalArgumentException("firstMovedIndex, lastMovedIndex out of order");
        } else {
            this._externSheetIndex = externSheetIndex;
            this._firstMovedIndex = firstMovedIndex;
            this._lastMovedIndex = lastMovedIndex;
            this._amountToMove = amountToMove;
        }
    }

    public static FormulaShifter createForRowShift(int externSheetIndex, int firstMovedRowIndex, int lastMovedRowIndex, int numberOfRowsToMove) {
        return new FormulaShifter(externSheetIndex, firstMovedRowIndex, lastMovedRowIndex, numberOfRowsToMove);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName());
        sb.append(" [");
        sb.append(this._firstMovedIndex);
        sb.append(this._lastMovedIndex);
        sb.append(this._amountToMove);
        return sb.toString();
    }

    public boolean adjustFormula(Ptg[] ptgs, int currentExternSheetIx) {
        boolean refsWereChanged = false;
        for (int i = 0; i < ptgs.length; i++) {
            Ptg newPtg = adjustPtg(ptgs[i], currentExternSheetIx);
            if (newPtg != null) {
                refsWereChanged = true;
                ptgs[i] = newPtg;
            }
        }
        return refsWereChanged;
    }

    private Ptg adjustPtg(Ptg ptg, int currentExternSheetIx) {
        return adjustPtgDueToRowMove(ptg, currentExternSheetIx);
    }

    private Ptg adjustPtgDueToRowMove(Ptg ptg, int currentExternSheetIx) {
        if (ptg instanceof RefPtg) {
            if (currentExternSheetIx != this._externSheetIndex) {
                return null;
            }
            return rowMoveRefPtg((RefPtg) ptg);
        } else if (ptg instanceof Ref3DPtg) {
            Ref3DPtg rptg = (Ref3DPtg) ptg;
            if (this._externSheetIndex != rptg.getExternSheetIndex()) {
                return null;
            }
            return rowMoveRefPtg(rptg);
        } else if (ptg instanceof Area2DPtgBase) {
            if (currentExternSheetIx != this._externSheetIndex) {
                return ptg;
            }
            return rowMoveAreaPtg((Area2DPtgBase) ptg);
        } else if (!(ptg instanceof Area3DPtg)) {
            return null;
        } else {
            Area3DPtg aptg = (Area3DPtg) ptg;
            if (this._externSheetIndex != aptg.getExternSheetIndex()) {
                return null;
            }
            return rowMoveAreaPtg(aptg);
        }
    }

    private Ptg rowMoveRefPtg(RefPtgBase rptg) {
        int refRow = rptg.getRow();
        if (this._firstMovedIndex > refRow || refRow > this._lastMovedIndex) {
            int destFirstRowIndex = this._firstMovedIndex + this._amountToMove;
            int destLastRowIndex = this._lastMovedIndex + this._amountToMove;
            if (destLastRowIndex < refRow || refRow < destFirstRowIndex) {
                return null;
            }
            if (destFirstRowIndex <= refRow && refRow <= destLastRowIndex) {
                return createDeletedRef(rptg);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Situation not covered: (");
            sb.append(this._firstMovedIndex);
            sb.append(", ");
            sb.append(this._lastMovedIndex);
            sb.append(", ");
            sb.append(this._amountToMove);
            sb.append(", ");
            sb.append(refRow);
            sb.append(", ");
            sb.append(refRow);
            sb.append(")");
            throw new IllegalStateException(sb.toString());
        }
        rptg.setRow(this._amountToMove + refRow);
        return rptg;
    }

    private Ptg rowMoveAreaPtg(AreaPtgBase aptg) {
        int aFirstRow = aptg.getFirstRow();
        int aLastRow = aptg.getLastRow();
        if (this._firstMovedIndex > aFirstRow || aLastRow > this._lastMovedIndex) {
            int destFirstRowIndex = this._firstMovedIndex + this._amountToMove;
            int destLastRowIndex = this._lastMovedIndex + this._amountToMove;
            if (aFirstRow >= this._firstMovedIndex || this._lastMovedIndex >= aLastRow) {
                if (this._firstMovedIndex > aFirstRow || aFirstRow > this._lastMovedIndex) {
                    if (this._firstMovedIndex > aLastRow || aLastRow > this._lastMovedIndex) {
                        if (destLastRowIndex < aFirstRow || aLastRow < destFirstRowIndex) {
                            return null;
                        }
                        if (destFirstRowIndex <= aFirstRow && aLastRow <= destLastRowIndex) {
                            return createDeletedRef(aptg);
                        }
                        if (aFirstRow <= destFirstRowIndex && destLastRowIndex <= aLastRow) {
                            return null;
                        }
                        if (destFirstRowIndex < aFirstRow && aFirstRow <= destLastRowIndex) {
                            aptg.setFirstRow(destLastRowIndex + 1);
                            return aptg;
                        } else if (destFirstRowIndex >= aLastRow || aLastRow > destLastRowIndex) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Situation not covered: (");
                            sb.append(this._firstMovedIndex);
                            sb.append(", ");
                            sb.append(this._lastMovedIndex);
                            sb.append(", ");
                            sb.append(this._amountToMove);
                            sb.append(", ");
                            sb.append(aFirstRow);
                            sb.append(", ");
                            sb.append(aLastRow);
                            sb.append(")");
                            throw new IllegalStateException(sb.toString());
                        } else {
                            aptg.setLastRow(destFirstRowIndex - 1);
                            return aptg;
                        }
                    } else if (this._amountToMove > 0) {
                        aptg.setLastRow(this._amountToMove + aLastRow);
                        return aptg;
                    } else if (destLastRowIndex < aFirstRow) {
                        return null;
                    } else {
                        int newLastRowIx = this._amountToMove + aLastRow;
                        if (destFirstRowIndex > aFirstRow) {
                            aptg.setLastRow(newLastRowIx);
                            return aptg;
                        }
                        int areaRemainingBottomRowIx = this._firstMovedIndex - 1;
                        if (destLastRowIndex < areaRemainingBottomRowIx) {
                            newLastRowIx = areaRemainingBottomRowIx;
                        }
                        aptg.setFirstRow(Math.min(aFirstRow, destFirstRowIndex));
                        aptg.setLastRow(newLastRowIx);
                        return aptg;
                    }
                } else if (this._amountToMove < 0) {
                    aptg.setFirstRow(this._amountToMove + aFirstRow);
                    return aptg;
                } else if (destFirstRowIndex > aLastRow) {
                    return null;
                } else {
                    int newFirstRowIx = this._amountToMove + aFirstRow;
                    if (destLastRowIndex < aLastRow) {
                        aptg.setFirstRow(newFirstRowIx);
                        return aptg;
                    }
                    int areaRemainingTopRowIx = this._lastMovedIndex + 1;
                    if (destFirstRowIndex > areaRemainingTopRowIx) {
                        newFirstRowIx = areaRemainingTopRowIx;
                    }
                    aptg.setFirstRow(newFirstRowIx);
                    aptg.setLastRow(Math.max(aLastRow, destLastRowIndex));
                    return aptg;
                }
            } else if (destFirstRowIndex < aFirstRow && aFirstRow <= destLastRowIndex) {
                aptg.setFirstRow(destLastRowIndex + 1);
                return aptg;
            } else if (destFirstRowIndex > aLastRow || aLastRow >= destLastRowIndex) {
                return null;
            } else {
                aptg.setLastRow(destFirstRowIndex - 1);
                return aptg;
            }
        } else {
            aptg.setFirstRow(this._amountToMove + aFirstRow);
            aptg.setLastRow(this._amountToMove + aLastRow);
            return aptg;
        }
    }

    private static Ptg createDeletedRef(Ptg ptg) {
        if (ptg instanceof RefPtg) {
            return new RefErrorPtg();
        }
        if (ptg instanceof Ref3DPtg) {
            return new DeletedRef3DPtg(((Ref3DPtg) ptg).getExternSheetIndex());
        }
        if (ptg instanceof AreaPtg) {
            return new AreaErrPtg();
        }
        if (ptg instanceof Area3DPtg) {
            return new DeletedArea3DPtg(((Area3DPtg) ptg).getExternSheetIndex());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected ref ptg class (");
        sb.append(ptg.getClass().getName());
        sb.append(")");
        throw new IllegalArgumentException(sb.toString());
    }
}
