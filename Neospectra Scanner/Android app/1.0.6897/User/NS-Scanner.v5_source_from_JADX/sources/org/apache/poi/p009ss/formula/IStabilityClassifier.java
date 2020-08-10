package org.apache.poi.p009ss.formula;

/* renamed from: org.apache.poi.ss.formula.IStabilityClassifier */
public interface IStabilityClassifier {
    public static final IStabilityClassifier TOTALLY_IMMUTABLE = new IStabilityClassifier() {
        public boolean isCellFinal(int sheetIndex, int rowIndex, int columnIndex) {
            return true;
        }
    };

    boolean isCellFinal(int i, int i2, int i3);
}
