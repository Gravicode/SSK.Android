package org.apache.commons.math3.linear;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

public class BlockRealMatrix extends AbstractRealMatrix implements Serializable {
    public static final int BLOCK_SIZE = 52;
    private static final long serialVersionUID = 4991895511313664478L;
    private final int blockColumns;
    private final int blockRows;
    private final double[][] blocks;
    private final int columns;
    private final int rows;

    public BlockRealMatrix(int rows2, int columns2) throws NotStrictlyPositiveException {
        super(rows2, columns2);
        this.rows = rows2;
        this.columns = columns2;
        this.blockRows = ((rows2 + 52) - 1) / 52;
        this.blockColumns = ((columns2 + 52) - 1) / 52;
        this.blocks = createBlocksLayout(rows2, columns2);
    }

    public BlockRealMatrix(double[][] rawData) throws DimensionMismatchException, NotStrictlyPositiveException {
        this(rawData.length, rawData[0].length, toBlocksLayout(rawData), false);
    }

    public BlockRealMatrix(int rows2, int columns2, double[][] blockData, boolean copyArray) throws DimensionMismatchException, NotStrictlyPositiveException {
        super(rows2, columns2);
        this.rows = rows2;
        this.columns = columns2;
        this.blockRows = ((rows2 + 52) - 1) / 52;
        this.blockColumns = ((columns2 + 52) - 1) / 52;
        if (copyArray) {
            this.blocks = new double[(this.blockRows * this.blockColumns)][];
        } else {
            this.blocks = blockData;
        }
        int index = 0;
        int iBlock = 0;
        while (iBlock < this.blockRows) {
            int iHeight = blockHeight(iBlock);
            int index2 = index;
            int jBlock = 0;
            while (jBlock < this.blockColumns) {
                if (blockData[index2].length != blockWidth(jBlock) * iHeight) {
                    throw new DimensionMismatchException(blockData[index2].length, blockWidth(jBlock) * iHeight);
                }
                if (copyArray) {
                    this.blocks[index2] = (double[]) blockData[index2].clone();
                }
                jBlock++;
                index2++;
            }
            iBlock++;
            index = index2;
        }
    }

    public static double[][] toBlocksLayout(double[][] rawData) throws DimensionMismatchException {
        int columns2;
        double[][] dArr = rawData;
        int rows2 = dArr.length;
        int p = dArr[0].length;
        int blockRows2 = ((rows2 + 52) - 1) / 52;
        int blockColumns2 = ((p + 52) - 1) / 52;
        for (double[] length : dArr) {
            int length2 = length.length;
            if (length2 != p) {
                throw new DimensionMismatchException(p, length2);
            }
        }
        double[][] blocks2 = new double[(blockRows2 * blockColumns2)][];
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < blockRows2) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, rows2);
            int iHeight = pEnd - pStart;
            int blockIndex2 = blockIndex;
            int jBlock = 0;
            while (jBlock < blockColumns2) {
                int qStart = jBlock * 52;
                int jWidth = FastMath.min(qStart + 52, p) - qStart;
                double[] block = new double[(iHeight * jWidth)];
                blocks2[blockIndex2] = block;
                int rows3 = rows2;
                int index = 0;
                int index2 = pStart;
                while (true) {
                    columns2 = p;
                    int p2 = index2;
                    if (p2 >= pEnd) {
                        break;
                    }
                    int blockRows3 = blockRows2;
                    System.arraycopy(dArr[p2], qStart, block, index, jWidth);
                    index += jWidth;
                    index2 = p2 + 1;
                    p = columns2;
                    blockRows2 = blockRows3;
                }
                blockIndex2++;
                jBlock++;
                rows2 = rows3;
                p = columns2;
            }
            int i = p;
            int i2 = blockRows2;
            iBlock++;
            blockIndex = blockIndex2;
        }
        int i3 = p;
        int i4 = blockRows2;
        return blocks2;
    }

    public static double[][] createBlocksLayout(int rows2, int columns2) {
        int blockRows2 = ((rows2 + 52) - 1) / 52;
        int blockColumns2 = ((columns2 + 52) - 1) / 52;
        double[][] blocks2 = new double[(blockRows2 * blockColumns2)][];
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < blockRows2) {
            int pStart = iBlock * 52;
            int iHeight = FastMath.min(pStart + 52, rows2) - pStart;
            int blockIndex2 = blockIndex;
            for (int jBlock = 0; jBlock < blockColumns2; jBlock++) {
                int qStart = jBlock * 52;
                blocks2[blockIndex2] = new double[(iHeight * (FastMath.min(qStart + 52, columns2) - qStart))];
                blockIndex2++;
            }
            iBlock++;
            blockIndex = blockIndex2;
        }
        return blocks2;
    }

    public BlockRealMatrix createMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        return new BlockRealMatrix(rowDimension, columnDimension);
    }

    public BlockRealMatrix copy() {
        BlockRealMatrix copied = new BlockRealMatrix(this.rows, this.columns);
        for (int i = 0; i < this.blocks.length; i++) {
            System.arraycopy(this.blocks[i], 0, copied.blocks[i], 0, this.blocks[i].length);
        }
        return copied;
    }

    public BlockRealMatrix add(RealMatrix m) throws MatrixDimensionMismatchException {
        RealMatrix realMatrix = m;
        try {
            return add((BlockRealMatrix) realMatrix);
        } catch (ClassCastException e) {
            ClassCastException classCastException = e;
            MatrixUtils.checkAdditionCompatible(this, m);
            BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
            int blockIndex = 0;
            int iBlock = 0;
            while (iBlock < out.blockRows) {
                int blockIndex2 = blockIndex;
                for (int jBlock = 0; jBlock < out.blockColumns; jBlock++) {
                    double[] outBlock = out.blocks[blockIndex2];
                    double[] tBlock = this.blocks[blockIndex2];
                    int pStart = iBlock * 52;
                    int pEnd = FastMath.min(pStart + 52, this.rows);
                    int qStart = jBlock * 52;
                    int qEnd = FastMath.min(qStart + 52, this.columns);
                    int k = 0;
                    int p = pStart;
                    while (p < pEnd) {
                        int k2 = k;
                        int k3 = qStart;
                        while (true) {
                            int q = k3;
                            if (q >= qEnd) {
                                break;
                            }
                            outBlock[k2] = tBlock[k2] + realMatrix.getEntry(p, q);
                            k2++;
                            k3 = q + 1;
                        }
                        p++;
                        k = k2;
                    }
                    blockIndex2++;
                }
                iBlock++;
                blockIndex = blockIndex2;
            }
            return out;
        }
    }

    public BlockRealMatrix add(BlockRealMatrix m) throws MatrixDimensionMismatchException {
        MatrixUtils.checkAdditionCompatible(this, m);
        BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            double[] outBlock = out.blocks[blockIndex];
            double[] tBlock = this.blocks[blockIndex];
            double[] mBlock = m.blocks[blockIndex];
            for (int k = 0; k < outBlock.length; k++) {
                outBlock[k] = tBlock[k] + mBlock[k];
            }
        }
        return out;
    }

    public BlockRealMatrix subtract(RealMatrix m) throws MatrixDimensionMismatchException {
        RealMatrix realMatrix = m;
        try {
            return subtract((BlockRealMatrix) realMatrix);
        } catch (ClassCastException e) {
            ClassCastException classCastException = e;
            MatrixUtils.checkSubtractionCompatible(this, m);
            BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
            int blockIndex = 0;
            int iBlock = 0;
            while (iBlock < out.blockRows) {
                int blockIndex2 = blockIndex;
                for (int jBlock = 0; jBlock < out.blockColumns; jBlock++) {
                    double[] outBlock = out.blocks[blockIndex2];
                    double[] tBlock = this.blocks[blockIndex2];
                    int pStart = iBlock * 52;
                    int pEnd = FastMath.min(pStart + 52, this.rows);
                    int qStart = jBlock * 52;
                    int qEnd = FastMath.min(qStart + 52, this.columns);
                    int k = 0;
                    int p = pStart;
                    while (p < pEnd) {
                        int k2 = k;
                        int k3 = qStart;
                        while (true) {
                            int q = k3;
                            if (q >= qEnd) {
                                break;
                            }
                            outBlock[k2] = tBlock[k2] - realMatrix.getEntry(p, q);
                            k2++;
                            k3 = q + 1;
                        }
                        p++;
                        k = k2;
                    }
                    blockIndex2++;
                }
                iBlock++;
                blockIndex = blockIndex2;
            }
            return out;
        }
    }

    public BlockRealMatrix subtract(BlockRealMatrix m) throws MatrixDimensionMismatchException {
        MatrixUtils.checkSubtractionCompatible(this, m);
        BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            double[] outBlock = out.blocks[blockIndex];
            double[] tBlock = this.blocks[blockIndex];
            double[] mBlock = m.blocks[blockIndex];
            for (int k = 0; k < outBlock.length; k++) {
                outBlock[k] = tBlock[k] - mBlock[k];
            }
        }
        return out;
    }

    public BlockRealMatrix scalarAdd(double d) {
        BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            double[] outBlock = out.blocks[blockIndex];
            double[] tBlock = this.blocks[blockIndex];
            for (int k = 0; k < outBlock.length; k++) {
                outBlock[k] = tBlock[k] + d;
            }
        }
        return out;
    }

    public RealMatrix scalarMultiply(double d) {
        BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            double[] outBlock = out.blocks[blockIndex];
            double[] tBlock = this.blocks[blockIndex];
            for (int k = 0; k < outBlock.length; k++) {
                outBlock[k] = tBlock[k] * d;
            }
        }
        return out;
    }

    public BlockRealMatrix multiply(RealMatrix m) throws DimensionMismatchException {
        int pEnd;
        int qStart;
        BlockRealMatrix blockRealMatrix = this;
        RealMatrix realMatrix = m;
        try {
            return blockRealMatrix.multiply((BlockRealMatrix) realMatrix);
        } catch (ClassCastException e) {
            ClassCastException cce = e;
            MatrixUtils.checkMultiplicationCompatible(this, m);
            BlockRealMatrix out = new BlockRealMatrix(blockRealMatrix.rows, m.getColumnDimension());
            int blockIndex = 0;
            int iBlock = 0;
            while (iBlock < out.blockRows) {
                int pStart = iBlock * 52;
                int q = FastMath.min(pStart + 52, blockRealMatrix.rows);
                int blockIndex2 = blockIndex;
                int jBlock = 0;
                while (jBlock < out.blockColumns) {
                    int l = jBlock * 52;
                    int qEnd = FastMath.min(l + 52, m.getColumnDimension());
                    double[] outBlock = out.blocks[blockIndex2];
                    int kBlock = 0;
                    while (kBlock < blockRealMatrix.blockColumns) {
                        int kWidth = blockRealMatrix.blockWidth(kBlock);
                        ClassCastException cce2 = cce;
                        double[] tBlock = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock) + kBlock];
                        int r = kBlock * 52;
                        int k = 0;
                        int k2 = pStart;
                        while (true) {
                            int p = k2;
                            if (p >= q) {
                                break;
                            }
                            int lStart = (p - pStart) * kWidth;
                            int pStart2 = pStart;
                            int pStart3 = lStart + kWidth;
                            int q2 = l;
                            while (true) {
                                pEnd = q;
                                int pEnd2 = q2;
                                if (pEnd2 >= qEnd) {
                                    break;
                                }
                                double sum = 0.0d;
                                int i = r;
                                int rStart = r;
                                int r2 = lStart;
                                while (true) {
                                    qStart = l;
                                    int l2 = r2;
                                    if (l2 >= pStart3) {
                                        break;
                                    }
                                    sum += tBlock[l2] * realMatrix.getEntry(r, pEnd2);
                                    r++;
                                    r2 = l2 + 1;
                                    l = qStart;
                                }
                                outBlock[k] = outBlock[k] + sum;
                                k++;
                                q2 = pEnd2 + 1;
                                q = pEnd;
                                r = rStart;
                                l = qStart;
                            }
                            int rStart2 = r;
                            int i2 = l;
                            k2 = p + 1;
                            pStart = pStart2;
                            q = pEnd;
                        }
                        int i3 = q;
                        int i4 = l;
                        kBlock++;
                        cce = cce2;
                        blockRealMatrix = this;
                    }
                    int i5 = pStart;
                    int i6 = q;
                    int i7 = l;
                    blockIndex2++;
                    jBlock++;
                    blockRealMatrix = this;
                }
                iBlock++;
                blockIndex = blockIndex2;
                blockRealMatrix = this;
            }
            return out;
        }
    }

    public BlockRealMatrix multiply(BlockRealMatrix m) throws DimensionMismatchException {
        int pStart;
        int kWidth;
        BlockRealMatrix blockRealMatrix = this;
        BlockRealMatrix blockRealMatrix2 = m;
        MatrixUtils.checkMultiplicationCompatible(this, m);
        BlockRealMatrix out = new BlockRealMatrix(blockRealMatrix.rows, blockRealMatrix2.columns);
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < out.blockRows) {
            int nStart = iBlock * 52;
            int pEnd = FastMath.min(nStart + 52, blockRealMatrix.rows);
            int blockIndex2 = blockIndex;
            int jBlock = 0;
            while (jBlock < out.blockColumns) {
                int jWidth = out.blockWidth(jBlock);
                int jWidth2 = jWidth + jWidth;
                int jWidth3 = jWidth2 + jWidth;
                int jWidth4 = jWidth3 + jWidth;
                double[] outBlock = out.blocks[blockIndex2];
                int kBlock = 0;
                while (kBlock < blockRealMatrix.blockColumns) {
                    int kWidth2 = blockRealMatrix.blockWidth(kBlock);
                    BlockRealMatrix out2 = out;
                    double[] tBlock = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock) + kBlock];
                    double[] mBlock = blockRealMatrix2.blocks[(blockRealMatrix2.blockColumns * kBlock) + jBlock];
                    int k = 0;
                    int p = nStart;
                    while (p < pEnd) {
                        int lStart = (p - nStart) * kWidth2;
                        int lEnd = lStart + kWidth2;
                        int k2 = k;
                        int n = 0;
                        while (true) {
                            pStart = nStart;
                            int nStart2 = n;
                            if (nStart2 >= jWidth) {
                                break;
                            }
                            double sum = 0.0d;
                            int pEnd2 = pEnd;
                            int l = lStart;
                            int n2 = nStart2;
                            while (true) {
                                kWidth = kWidth2;
                                if (l >= lEnd - 3) {
                                    break;
                                }
                                sum += (tBlock[l] * mBlock[n2]) + (tBlock[l + 1] * mBlock[n2 + jWidth]) + (tBlock[l + 2] * mBlock[n2 + jWidth2]) + (tBlock[l + 3] * mBlock[n2 + jWidth3]);
                                l += 4;
                                n2 += jWidth4;
                                kWidth2 = kWidth;
                            }
                            while (l < lEnd) {
                                sum += tBlock[l] * mBlock[n2];
                                n2 += jWidth;
                                l++;
                            }
                            outBlock[k2] = outBlock[k2] + sum;
                            k2++;
                            n = nStart2 + 1;
                            nStart = pStart;
                            pEnd = pEnd2;
                            kWidth2 = kWidth;
                        }
                        int i = kWidth2;
                        p++;
                        k = k2;
                        nStart = pStart;
                        BlockRealMatrix blockRealMatrix3 = m;
                    }
                    int pStart2 = nStart;
                    int i2 = pEnd;
                    kBlock++;
                    out = out2;
                    blockRealMatrix = this;
                    blockRealMatrix2 = m;
                }
                int i3 = nStart;
                int i4 = pEnd;
                blockIndex2++;
                jBlock++;
                blockRealMatrix = this;
                blockRealMatrix2 = m;
            }
            iBlock++;
            blockIndex = blockIndex2;
            blockRealMatrix = this;
            blockRealMatrix2 = m;
        }
        return out;
    }

    public double[][] getData() {
        double[][] data = (double[][]) Array.newInstance(double.class, new int[]{getRowDimension(), getColumnDimension()});
        int lastColumns = this.columns - ((this.blockColumns - 1) * 52);
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, this.rows);
            int lastPos = 0;
            int regularPos = 0;
            for (int p = pStart; p < pEnd; p++) {
                double[] dataP = data[p];
                int dataPos = 0;
                int dataPos2 = this.blockColumns * iBlock;
                int jBlock = 0;
                while (jBlock < this.blockColumns - 1) {
                    int blockIndex = dataPos2 + 1;
                    System.arraycopy(this.blocks[dataPos2], regularPos, dataP, dataPos, 52);
                    dataPos += 52;
                    jBlock++;
                    dataPos2 = blockIndex;
                }
                System.arraycopy(this.blocks[dataPos2], lastPos, dataP, dataPos, lastColumns);
                regularPos += 52;
                lastPos += lastColumns;
            }
        }
        return data;
    }

    public double getNorm() {
        double[] colSums = new double[52];
        double maxColSum = 0.0d;
        int jBlock = 0;
        while (jBlock < this.blockColumns) {
            int jWidth = blockWidth(jBlock);
            Arrays.fill(colSums, 0, jWidth, 0.0d);
            for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
                int iHeight = blockHeight(iBlock);
                double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
                for (int j = 0; j < jWidth; j++) {
                    double sum = 0.0d;
                    for (int i = 0; i < iHeight; i++) {
                        sum += FastMath.abs(block[(i * jWidth) + j]);
                    }
                    colSums[j] = colSums[j] + sum;
                }
            }
            double maxColSum2 = maxColSum;
            for (int j2 = 0; j2 < jWidth; j2++) {
                maxColSum2 = FastMath.max(maxColSum2, colSums[j2]);
            }
            jBlock++;
            maxColSum = maxColSum2;
        }
        return maxColSum;
    }

    public double getFrobeniusNorm() {
        double sum2 = 0.0d;
        int blockIndex = 0;
        while (blockIndex < this.blocks.length) {
            double sum22 = sum2;
            for (double entry : this.blocks[blockIndex]) {
                sum22 += entry * entry;
            }
            blockIndex++;
            sum2 = sum22;
        }
        return FastMath.sqrt(sum2);
    }

    public BlockRealMatrix getSubMatrix(int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        int iBlock;
        int qBlock;
        int jBlock;
        BlockRealMatrix out;
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        BlockRealMatrix out2 = new BlockRealMatrix((endRow - startRow) + 1, (endColumn - startColumn) + 1);
        int rowsShift = startRow % 52;
        int blockStartColumn = startColumn / 52;
        int columnsShift = startColumn % 52;
        int pBlock = startRow / 52;
        int pBlock2 = 0;
        while (true) {
            int iBlock2 = pBlock2;
            if (iBlock2 >= out2.blockRows) {
                return out2;
            }
            int iHeight = out2.blockHeight(iBlock2);
            int qBlock2 = blockStartColumn;
            int qBlock3 = 0;
            while (true) {
                int jBlock2 = qBlock3;
                if (jBlock2 >= out2.blockColumns) {
                    break;
                }
                int jWidth = out2.blockWidth(jBlock2);
                double[] outBlock = out2.blocks[(out2.blockColumns * iBlock2) + jBlock2];
                int index = (this.blockColumns * pBlock) + qBlock2;
                int width = blockWidth(qBlock2);
                int heightExcess = (iHeight + rowsShift) - 52;
                int widthExcess = (jWidth + columnsShift) - 52;
                if (heightExcess <= 0) {
                    jBlock = jBlock2;
                    qBlock = qBlock2;
                    iBlock = iBlock2;
                    out = out2;
                    if (widthExcess > 0) {
                        int width2 = blockWidth(qBlock + 1);
                        int i = rowsShift;
                        double[] dArr = outBlock;
                        int i2 = jWidth;
                        copyBlockPart(this.blocks[index], width, i, iHeight + rowsShift, columnsShift, 52, dArr, i2, 0, 0);
                        copyBlockPart(this.blocks[index + 1], width2, i, iHeight + rowsShift, 0, widthExcess, dArr, i2, 0, jWidth - widthExcess);
                    } else {
                        copyBlockPart(this.blocks[index], width, rowsShift, iHeight + rowsShift, columnsShift, jWidth + columnsShift, outBlock, jWidth, 0, 0);
                    }
                } else if (widthExcess > 0) {
                    int width22 = blockWidth(qBlock2 + 1);
                    int i3 = rowsShift;
                    jBlock = jBlock2;
                    double[] dArr2 = outBlock;
                    qBlock = qBlock2;
                    int qBlock4 = jWidth;
                    iBlock = iBlock2;
                    out = out2;
                    copyBlockPart(this.blocks[index], width, i3, 52, columnsShift, 52, dArr2, qBlock4, 0, 0);
                    copyBlockPart(this.blocks[index + 1], width22, i3, 52, 0, widthExcess, dArr2, qBlock4, 0, jWidth - widthExcess);
                    int i4 = heightExcess;
                    copyBlockPart(this.blocks[index + this.blockColumns], width, 0, i4, columnsShift, 52, dArr2, qBlock4, iHeight - heightExcess, 0);
                    copyBlockPart(this.blocks[index + this.blockColumns + 1], width22, 0, i4, 0, widthExcess, dArr2, qBlock4, iHeight - heightExcess, jWidth - widthExcess);
                } else {
                    jBlock = jBlock2;
                    qBlock = qBlock2;
                    iBlock = iBlock2;
                    out = out2;
                    int i5 = width;
                    int i6 = columnsShift;
                    double[] dArr3 = outBlock;
                    int i7 = jWidth;
                    copyBlockPart(this.blocks[index], i5, rowsShift, 52, i6, jWidth + columnsShift, dArr3, i7, 0, 0);
                    copyBlockPart(this.blocks[index + this.blockColumns], i5, 0, heightExcess, i6, jWidth + columnsShift, dArr3, i7, iHeight - heightExcess, 0);
                }
                qBlock2 = qBlock + 1;
                qBlock3 = jBlock + 1;
                out2 = out;
                iBlock2 = iBlock;
            }
            int i8 = qBlock2;
            BlockRealMatrix blockRealMatrix = out2;
            pBlock++;
            pBlock2 = iBlock2 + 1;
        }
    }

    private void copyBlockPart(double[] srcBlock, int srcWidth, int srcStartRow, int srcEndRow, int srcStartColumn, int srcEndColumn, double[] dstBlock, int dstWidth, int dstStartRow, int dstStartColumn) {
        int length = srcEndColumn - srcStartColumn;
        int dstPos = (dstStartRow * dstWidth) + dstStartColumn;
        int srcPos = (srcStartRow * srcWidth) + srcStartColumn;
        for (int srcRow = srcStartRow; srcRow < srcEndRow; srcRow++) {
            System.arraycopy(srcBlock, srcPos, dstBlock, dstPos, length);
            srcPos += srcWidth;
            dstPos += dstWidth;
        }
    }

    public void setSubMatrix(double[][] subMatrix, int row, int column) throws OutOfRangeException, NoDataException, NullArgumentException, DimensionMismatchException {
        double[][] arr$;
        BlockRealMatrix blockRealMatrix = this;
        double[][] dArr = subMatrix;
        int i = row;
        int i2 = column;
        MathUtils.checkNotNull(subMatrix);
        int refLength = dArr[0].length;
        if (refLength == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
        }
        int endRow = (dArr.length + i) - 1;
        int endColumn = (i2 + refLength) - 1;
        MatrixUtils.checkSubMatrixIndex(blockRealMatrix, i, endRow, i2, endColumn);
        for (double[] subRow : dArr) {
            if (subRow.length != refLength) {
                throw new DimensionMismatchException(refLength, subRow.length);
            }
        }
        int blockStartRow = i / 52;
        int blockEndRow = (endRow + 52) / 52;
        int blockStartColumn = i2 / 52;
        int blockEndColumn = (endColumn + 52) / 52;
        int iBlock = blockStartRow;
        while (iBlock < blockEndRow) {
            int iHeight = blockRealMatrix.blockHeight(iBlock);
            int firstRow = iBlock * 52;
            int iStart = FastMath.max(i, firstRow);
            int blockStartRow2 = blockStartRow;
            int iEnd = FastMath.min(endRow + 1, firstRow + iHeight);
            int jBlock = blockStartColumn;
            while (jBlock < blockEndColumn) {
                int jWidth = blockRealMatrix.blockWidth(jBlock);
                int refLength2 = refLength;
                int refLength3 = jBlock * 52;
                int jStart = FastMath.max(i2, refLength3);
                int endRow2 = endRow;
                int endColumn2 = endColumn;
                int jEnd = FastMath.min(endColumn + 1, refLength3 + jWidth);
                int jLength = jEnd - jStart;
                int i3 = jEnd;
                int blockEndRow2 = blockEndRow;
                double[] block = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock) + jBlock];
                int i4 = iStart;
                while (i4 < iEnd) {
                    System.arraycopy(dArr[i4 - i], jStart - i2, block, ((i4 - firstRow) * jWidth) + (jStart - refLength3), jLength);
                    i4++;
                    dArr = subMatrix;
                    i = row;
                }
                jBlock++;
                refLength = refLength2;
                endRow = endRow2;
                endColumn = endColumn2;
                blockEndRow = blockEndRow2;
                blockRealMatrix = this;
                dArr = subMatrix;
                i = row;
            }
            int i5 = endRow;
            int i6 = endColumn;
            int i7 = blockEndRow;
            iBlock++;
            blockStartRow = blockStartRow2;
            blockRealMatrix = this;
            dArr = subMatrix;
            i = row;
        }
        int i8 = refLength;
        int i9 = endRow;
        int i10 = endColumn;
        int i11 = blockEndRow;
    }

    public BlockRealMatrix getRowMatrix(int row) throws OutOfRangeException {
        MatrixUtils.checkRowIndex(this, row);
        BlockRealMatrix out = new BlockRealMatrix(1, this.columns);
        int iBlock = row / 52;
        int iRow = row - (iBlock * 52);
        int outIndex = 0;
        double[] outBlock = out.blocks[0];
        int outBlockIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int available = outBlock.length - outIndex;
            if (jWidth > available) {
                System.arraycopy(block, iRow * jWidth, outBlock, outIndex, available);
                outBlockIndex++;
                outBlock = out.blocks[outBlockIndex];
                System.arraycopy(block, iRow * jWidth, outBlock, 0, jWidth - available);
                outIndex = jWidth - available;
            } else {
                System.arraycopy(block, iRow * jWidth, outBlock, outIndex, jWidth);
                outIndex += jWidth;
            }
        }
        return out;
    }

    public void setRowMatrix(int row, RealMatrix matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        try {
            setRowMatrix(row, (BlockRealMatrix) matrix);
        } catch (ClassCastException e) {
            super.setRowMatrix(row, matrix);
        }
    }

    public void setRowMatrix(int row, BlockRealMatrix matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        if (matrix.getRowDimension() == 1 && matrix.getColumnDimension() == nCols) {
            int iBlock = row / 52;
            int iRow = row - (iBlock * 52);
            int mIndex = 0;
            double[] mBlock = matrix.blocks[0];
            int mBlockIndex = 0;
            for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                int jWidth = blockWidth(jBlock);
                double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
                int available = mBlock.length - mIndex;
                if (jWidth > available) {
                    System.arraycopy(mBlock, mIndex, block, iRow * jWidth, available);
                    mBlockIndex++;
                    mBlock = matrix.blocks[mBlockIndex];
                    System.arraycopy(mBlock, 0, block, iRow * jWidth, jWidth - available);
                    mIndex = jWidth - available;
                } else {
                    System.arraycopy(mBlock, mIndex, block, iRow * jWidth, jWidth);
                    mIndex += jWidth;
                }
            }
            return;
        }
        throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), 1, nCols);
    }

    public BlockRealMatrix getColumnMatrix(int column) throws OutOfRangeException {
        MatrixUtils.checkColumnIndex(this, column);
        BlockRealMatrix out = new BlockRealMatrix(this.rows, 1);
        int jBlock = column / 52;
        int jColumn = column - (jBlock * 52);
        int jWidth = blockWidth(jBlock);
        double[] outBlock = out.blocks[0];
        int outIndex = 0;
        int outIndex2 = 0;
        int iBlock = 0;
        while (iBlock < this.blockRows) {
            int iHeight = blockHeight(iBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int outBlockIndex = outIndex2;
            int i = 0;
            while (i < iHeight) {
                if (outIndex >= outBlock.length) {
                    outBlockIndex++;
                    outBlock = out.blocks[outBlockIndex];
                    outIndex = 0;
                }
                int outIndex3 = outIndex + 1;
                outBlock[outIndex] = block[(i * jWidth) + jColumn];
                i++;
                outIndex = outIndex3;
            }
            iBlock++;
            outIndex2 = outBlockIndex;
        }
        return out;
    }

    public void setColumnMatrix(int column, RealMatrix matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        try {
            setColumnMatrix(column, (BlockRealMatrix) matrix);
        } catch (ClassCastException e) {
            super.setColumnMatrix(column, matrix);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setColumnMatrix(int column, BlockRealMatrix matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        BlockRealMatrix blockRealMatrix = matrix;
        MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        if (matrix.getRowDimension() == nRows && matrix.getColumnDimension() == 1) {
            int jBlock = column / 52;
            int jColumn = column - (jBlock * 52);
            int jWidth = blockWidth(jBlock);
            double[] mBlock = blockRealMatrix.blocks[0];
            int mIndex = 0;
            int mIndex2 = 0;
            int iBlock = 0;
            while (iBlock < this.blockRows) {
                int iHeight = blockHeight(iBlock);
                double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
                int mBlockIndex = mIndex2;
                int i = 0;
                while (i < iHeight) {
                    if (mIndex >= mBlock.length) {
                        mBlockIndex++;
                        mBlock = blockRealMatrix.blocks[mBlockIndex];
                        mIndex = 0;
                    }
                    int mIndex3 = mIndex + 1;
                    block[(i * jWidth) + jColumn] = mBlock[mIndex];
                    i++;
                    mIndex = mIndex3;
                }
                iBlock++;
                mIndex2 = mBlockIndex;
            }
            return;
        }
        throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), nRows, 1);
    }

    public RealVector getRowVector(int row) throws OutOfRangeException {
        MatrixUtils.checkRowIndex(this, row);
        double[] outData = new double[this.columns];
        int iBlock = row / 52;
        int iRow = row - (iBlock * 52);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            System.arraycopy(this.blocks[(this.blockColumns * iBlock) + jBlock], iRow * jWidth, outData, outIndex, jWidth);
            outIndex += jWidth;
        }
        return new ArrayRealVector(outData, false);
    }

    public void setRowVector(int row, RealVector vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        try {
            setRow(row, ((ArrayRealVector) vector).getDataRef());
        } catch (ClassCastException e) {
            super.setRowVector(row, vector);
        }
    }

    public RealVector getColumnVector(int column) throws OutOfRangeException {
        MatrixUtils.checkColumnIndex(this, column);
        double[] outData = new double[this.rows];
        int jBlock = column / 52;
        int jColumn = column - (jBlock * 52);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        int iBlock = 0;
        while (iBlock < this.blockRows) {
            int iHeight = blockHeight(iBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int outIndex2 = outIndex;
            int i = 0;
            while (i < iHeight) {
                int outIndex3 = outIndex2 + 1;
                outData[outIndex2] = block[(i * jWidth) + jColumn];
                i++;
                outIndex2 = outIndex3;
            }
            iBlock++;
            outIndex = outIndex2;
        }
        return new ArrayRealVector(outData, false);
    }

    public void setColumnVector(int column, RealVector vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        try {
            setColumn(column, ((ArrayRealVector) vector).getDataRef());
        } catch (ClassCastException e) {
            super.setColumnVector(column, vector);
        }
    }

    public double[] getRow(int row) throws OutOfRangeException {
        MatrixUtils.checkRowIndex(this, row);
        double[] out = new double[this.columns];
        int iBlock = row / 52;
        int iRow = row - (iBlock * 52);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            System.arraycopy(this.blocks[(this.blockColumns * iBlock) + jBlock], iRow * jWidth, out, outIndex, jWidth);
            outIndex += jWidth;
        }
        return out;
    }

    public void setRow(int row, double[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        if (array.length != nCols) {
            throw new MatrixDimensionMismatchException(1, array.length, 1, nCols);
        }
        int iBlock = row / 52;
        int iRow = row - (iBlock * 52);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            System.arraycopy(array, outIndex, this.blocks[(this.blockColumns * iBlock) + jBlock], iRow * jWidth, jWidth);
            outIndex += jWidth;
        }
    }

    public double[] getColumn(int column) throws OutOfRangeException {
        MatrixUtils.checkColumnIndex(this, column);
        double[] out = new double[this.rows];
        int jBlock = column / 52;
        int jColumn = column - (jBlock * 52);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        int iBlock = 0;
        while (iBlock < this.blockRows) {
            int iHeight = blockHeight(iBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int outIndex2 = outIndex;
            int i = 0;
            while (i < iHeight) {
                int outIndex3 = outIndex2 + 1;
                out[outIndex2] = block[(i * jWidth) + jColumn];
                i++;
                outIndex2 = outIndex3;
            }
            iBlock++;
            outIndex = outIndex2;
        }
        return out;
    }

    public void setColumn(int column, double[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        double[] dArr = array;
        MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        if (dArr.length != nRows) {
            throw new MatrixDimensionMismatchException(dArr.length, 1, nRows, 1);
        }
        int jBlock = column / 52;
        int jColumn = column - (jBlock * 52);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        int iBlock = 0;
        while (iBlock < this.blockRows) {
            int iHeight = blockHeight(iBlock);
            double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int outIndex2 = outIndex;
            int i = 0;
            while (i < iHeight) {
                int outIndex3 = outIndex2 + 1;
                block[(i * jWidth) + jColumn] = dArr[outIndex2];
                i++;
                outIndex2 = outIndex3;
            }
            iBlock++;
            outIndex = outIndex2;
        }
    }

    public double getEntry(int row, int column) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        int iBlock = row / 52;
        int jBlock = column / 52;
        return this.blocks[(this.blockColumns * iBlock) + jBlock][((row - (iBlock * 52)) * blockWidth(jBlock)) + (column - (jBlock * 52))];
    }

    public void setEntry(int row, int column, double value) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        int iBlock = row / 52;
        int jBlock = column / 52;
        this.blocks[(this.blockColumns * iBlock) + jBlock][((row - (iBlock * 52)) * blockWidth(jBlock)) + (column - (jBlock * 52))] = value;
    }

    public void addToEntry(int row, int column, double increment) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        int iBlock = row / 52;
        int jBlock = column / 52;
        int k = ((row - (iBlock * 52)) * blockWidth(jBlock)) + (column - (jBlock * 52));
        double[] dArr = this.blocks[(this.blockColumns * iBlock) + jBlock];
        dArr[k] = dArr[k] + increment;
    }

    public void multiplyEntry(int row, int column, double factor) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        int iBlock = row / 52;
        int jBlock = column / 52;
        int k = ((row - (iBlock * 52)) * blockWidth(jBlock)) + (column - (jBlock * 52));
        double[] dArr = this.blocks[(this.blockColumns * iBlock) + jBlock];
        dArr[k] = dArr[k] * factor;
    }

    public BlockRealMatrix transpose() {
        BlockRealMatrix out = new BlockRealMatrix(getColumnDimension(), getRowDimension());
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < this.blockColumns) {
            int blockIndex2 = blockIndex;
            for (int jBlock = 0; jBlock < this.blockRows; jBlock++) {
                double[] outBlock = out.blocks[blockIndex2];
                double[] tBlock = this.blocks[(this.blockColumns * jBlock) + iBlock];
                int pStart = iBlock * 52;
                int pEnd = FastMath.min(pStart + 52, this.columns);
                int qStart = jBlock * 52;
                int qEnd = FastMath.min(qStart + 52, this.rows);
                int k = 0;
                int p = pStart;
                while (p < pEnd) {
                    int lInc = pEnd - pStart;
                    int l = p - pStart;
                    int k2 = k;
                    for (int q = qStart; q < qEnd; q++) {
                        outBlock[k2] = tBlock[l];
                        k2++;
                        l += lInc;
                    }
                    p++;
                    k = k2;
                }
                blockIndex2++;
            }
            iBlock++;
            blockIndex = blockIndex2;
        }
        return out;
    }

    public int getRowDimension() {
        return this.rows;
    }

    public int getColumnDimension() {
        return this.columns;
    }

    public double[] operate(double[] v) throws DimensionMismatchException {
        double[] dArr = v;
        if (dArr.length != this.columns) {
            throw new DimensionMismatchException(dArr.length, this.columns);
        }
        double[] out = new double[this.rows];
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, this.rows);
            for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
                int qStart = jBlock * 52;
                int qEnd = FastMath.min(qStart + 52, this.columns);
                int k = 0;
                int p = pStart;
                while (p < pEnd) {
                    double sum = 0.0d;
                    int k2 = k;
                    int q = qStart;
                    while (q < qEnd - 3) {
                        sum += (block[k2] * dArr[q]) + (block[k2 + 1] * dArr[q + 1]) + (block[k2 + 2] * dArr[q + 2]) + (block[k2 + 3] * dArr[q + 3]);
                        k2 += 4;
                        q += 4;
                    }
                    while (q < qEnd) {
                        int k3 = k2 + 1;
                        double d = block[k2];
                        sum += d * dArr[q];
                        q++;
                        k2 = k3;
                    }
                    out[p] = out[p] + sum;
                    p++;
                    k = k2;
                }
            }
        }
        return out;
    }

    public double[] preMultiply(double[] v) throws DimensionMismatchException {
        int p;
        BlockRealMatrix blockRealMatrix = this;
        double[] dArr = v;
        if (dArr.length != blockRealMatrix.rows) {
            throw new DimensionMismatchException(dArr.length, blockRealMatrix.rows);
        }
        double[] out = new double[blockRealMatrix.columns];
        int jBlock = 0;
        while (jBlock < blockRealMatrix.blockColumns) {
            int jWidth = blockRealMatrix.blockWidth(jBlock);
            int jWidth2 = jWidth + jWidth;
            int jWidth3 = jWidth2 + jWidth;
            int jWidth4 = jWidth3 + jWidth;
            int qStart = jBlock * 52;
            int qEnd = FastMath.min(qStart + 52, blockRealMatrix.columns);
            int iBlock = 0;
            while (iBlock < blockRealMatrix.blockRows) {
                double[] block = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock) + jBlock];
                int pStart = iBlock * 52;
                int pEnd = FastMath.min(pStart + 52, blockRealMatrix.rows);
                int q = qStart;
                while (q < qEnd) {
                    double sum = 0.0d;
                    int k = q - qStart;
                    int k2 = pStart;
                    while (true) {
                        p = k2;
                        if (p >= pEnd - 3) {
                            break;
                        }
                        sum += (block[k] * dArr[p]) + (block[k + jWidth] * dArr[p + 1]) + (block[k + jWidth2] * dArr[p + 2]) + (block[k + jWidth3] * dArr[p + 3]);
                        k += jWidth4;
                        k2 = p + 4;
                    }
                    while (p < pEnd) {
                        sum += block[k] * dArr[p];
                        k += jWidth;
                        p++;
                    }
                    out[q] = out[q] + sum;
                    q++;
                }
                iBlock++;
                blockRealMatrix = this;
            }
            jBlock++;
            blockRealMatrix = this;
        }
        return out;
    }

    public double walkInRowOrder(RealMatrixChangingVisitor visitor) {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, this.rows);
            for (int p = pStart; p < pEnd; p++) {
                for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                    int jWidth = blockWidth(jBlock);
                    int qStart = jBlock * 52;
                    int qEnd = FastMath.min(qStart + 52, this.columns);
                    double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
                    int k = (p - pStart) * jWidth;
                    for (int q = qStart; q < qEnd; q++) {
                        block[k] = visitor.visit(p, q, block[k]);
                        k++;
                    }
                }
            }
        }
        return visitor.end();
    }

    public double walkInRowOrder(RealMatrixPreservingVisitor visitor) {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, this.rows);
            for (int p = pStart; p < pEnd; p++) {
                for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                    int jWidth = blockWidth(jBlock);
                    int qStart = jBlock * 52;
                    int qEnd = FastMath.min(qStart + 52, this.columns);
                    double[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
                    int k = (p - pStart) * jWidth;
                    for (int q = qStart; q < qEnd; q++) {
                        visitor.visit(p, q, block[k]);
                        k++;
                    }
                }
            }
        }
        return visitor.end();
    }

    public double walkInRowOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        BlockRealMatrix blockRealMatrix = this;
        int i = startRow;
        int i2 = endRow;
        int i3 = startColumn;
        int i4 = endColumn;
        MatrixUtils.checkSubMatrixIndex(blockRealMatrix, i, i2, i3, i4);
        visitor.start(blockRealMatrix.rows, blockRealMatrix.columns, i, i2, i3, i4);
        int iBlock = i / 52;
        while (iBlock < (i2 / 52) + 1) {
            int p0 = iBlock * 52;
            int pStart = FastMath.max(i, p0);
            int pEnd = FastMath.min((iBlock + 1) * 52, i2 + 1);
            int p = pStart;
            while (p < pEnd) {
                int jBlock = i3 / 52;
                while (jBlock < (i4 / 52) + 1) {
                    int jWidth = blockRealMatrix.blockWidth(jBlock);
                    int q0 = jBlock * 52;
                    int qStart = FastMath.max(i3, q0);
                    int qEnd = FastMath.min((jBlock + 1) * 52, i4 + 1);
                    int pStart2 = pStart;
                    double[] block = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock) + jBlock];
                    int k = (((p - p0) * jWidth) + qStart) - q0;
                    int q = qStart;
                    while (q < qEnd) {
                        int jWidth2 = jWidth;
                        block[k] = visitor.visit(p, q, block[k]);
                        k++;
                        q++;
                        jWidth = jWidth2;
                        int i5 = startRow;
                    }
                    RealMatrixChangingVisitor realMatrixChangingVisitor = visitor;
                    jBlock++;
                    pStart = pStart2;
                    blockRealMatrix = this;
                    int i6 = startRow;
                }
                RealMatrixChangingVisitor realMatrixChangingVisitor2 = visitor;
                int i7 = pStart;
                p++;
                blockRealMatrix = this;
                int i8 = startRow;
            }
            RealMatrixChangingVisitor realMatrixChangingVisitor3 = visitor;
            iBlock++;
            blockRealMatrix = this;
            i = startRow;
        }
        RealMatrixChangingVisitor realMatrixChangingVisitor4 = visitor;
        return visitor.end();
    }

    public double walkInRowOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        BlockRealMatrix blockRealMatrix = this;
        int i = startRow;
        int i2 = endRow;
        int i3 = startColumn;
        int i4 = endColumn;
        MatrixUtils.checkSubMatrixIndex(blockRealMatrix, i, i2, i3, i4);
        visitor.start(blockRealMatrix.rows, blockRealMatrix.columns, i, i2, i3, i4);
        int iBlock = i / 52;
        while (iBlock < (i2 / 52) + 1) {
            int p0 = iBlock * 52;
            int pStart = FastMath.max(i, p0);
            int pEnd = FastMath.min((iBlock + 1) * 52, i2 + 1);
            int p = pStart;
            while (p < pEnd) {
                int jBlock = i3 / 52;
                while (jBlock < (i4 / 52) + 1) {
                    int jWidth = blockRealMatrix.blockWidth(jBlock);
                    int q0 = jBlock * 52;
                    int qStart = FastMath.max(i3, q0);
                    int qEnd = FastMath.min((jBlock + 1) * 52, i4 + 1);
                    int pStart2 = pStart;
                    double[] block = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock) + jBlock];
                    int k = (((p - p0) * jWidth) + qStart) - q0;
                    int q = qStart;
                    while (q < qEnd) {
                        int jWidth2 = jWidth;
                        visitor.visit(p, q, block[k]);
                        k++;
                        q++;
                        jWidth = jWidth2;
                        int i5 = startRow;
                    }
                    RealMatrixPreservingVisitor realMatrixPreservingVisitor = visitor;
                    jBlock++;
                    pStart = pStart2;
                    blockRealMatrix = this;
                    int i6 = startRow;
                }
                RealMatrixPreservingVisitor realMatrixPreservingVisitor2 = visitor;
                int i7 = pStart;
                p++;
                blockRealMatrix = this;
                int i8 = startRow;
            }
            RealMatrixPreservingVisitor realMatrixPreservingVisitor3 = visitor;
            iBlock++;
            blockRealMatrix = this;
            i = startRow;
        }
        RealMatrixPreservingVisitor realMatrixPreservingVisitor4 = visitor;
        return visitor.end();
    }

    public double walkInOptimizedOrder(RealMatrixChangingVisitor visitor) {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < this.blockRows) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, this.rows);
            int blockIndex2 = blockIndex;
            for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                int qStart = jBlock * 52;
                int qEnd = FastMath.min(qStart + 52, this.columns);
                double[] block = this.blocks[blockIndex2];
                int k = 0;
                int p = pStart;
                while (p < pEnd) {
                    int k2 = k;
                    for (int q = qStart; q < qEnd; q++) {
                        block[k2] = visitor.visit(p, q, block[k2]);
                        k2++;
                    }
                    p++;
                    k = k2;
                }
                blockIndex2++;
            }
            iBlock++;
            blockIndex = blockIndex2;
        }
        return visitor.end();
    }

    public double walkInOptimizedOrder(RealMatrixPreservingVisitor visitor) {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < this.blockRows) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, this.rows);
            int blockIndex2 = blockIndex;
            for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                int qStart = jBlock * 52;
                int qEnd = FastMath.min(qStart + 52, this.columns);
                double[] block = this.blocks[blockIndex2];
                int k = 0;
                int p = pStart;
                while (p < pEnd) {
                    int k2 = k;
                    for (int q = qStart; q < qEnd; q++) {
                        visitor.visit(p, q, block[k2]);
                        k2++;
                    }
                    p++;
                    k = k2;
                }
                blockIndex2++;
            }
            iBlock++;
            blockIndex = blockIndex2;
        }
        return visitor.end();
    }

    public double walkInOptimizedOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        BlockRealMatrix blockRealMatrix = this;
        int i = startRow;
        int i2 = endRow;
        int i3 = startColumn;
        int i4 = endColumn;
        MatrixUtils.checkSubMatrixIndex(blockRealMatrix, i, i2, i3, i4);
        visitor.start(blockRealMatrix.rows, blockRealMatrix.columns, i, i2, i3, i4);
        int iBlock = i / 52;
        while (iBlock < (i2 / 52) + 1) {
            int p0 = iBlock * 52;
            int pStart = FastMath.max(i, p0);
            int pEnd = FastMath.min((iBlock + 1) * 52, i2 + 1);
            int jBlock = i3 / 52;
            while (jBlock < (i4 / 52) + 1) {
                int jWidth = blockRealMatrix.blockWidth(jBlock);
                int q0 = jBlock * 52;
                int qStart = FastMath.max(i3, q0);
                int qEnd = FastMath.min((jBlock + 1) * 52, i4 + 1);
                double[] block = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock) + jBlock];
                int p = pStart;
                while (p < pEnd) {
                    int k = (((p - p0) * jWidth) + qStart) - q0;
                    int k2 = qStart;
                    while (true) {
                        int q = k2;
                        if (q >= qEnd) {
                            break;
                        }
                        int p02 = p0;
                        int pStart2 = pStart;
                        int pEnd2 = pEnd;
                        block[k] = visitor.visit(p, q, block[k]);
                        k++;
                        k2 = q + 1;
                        p0 = p02;
                        pStart = pStart2;
                        pEnd = pEnd2;
                    }
                    int i5 = pStart;
                    int pEnd3 = pEnd;
                    RealMatrixChangingVisitor realMatrixChangingVisitor = visitor;
                    p++;
                    pEnd = pEnd3;
                }
                int i6 = pStart;
                int pEnd4 = pEnd;
                RealMatrixChangingVisitor realMatrixChangingVisitor2 = visitor;
                jBlock++;
                pEnd = pEnd4;
                blockRealMatrix = this;
            }
            RealMatrixChangingVisitor realMatrixChangingVisitor3 = visitor;
            iBlock++;
            blockRealMatrix = this;
        }
        RealMatrixChangingVisitor realMatrixChangingVisitor4 = visitor;
        return visitor.end();
    }

    public double walkInOptimizedOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        BlockRealMatrix blockRealMatrix = this;
        int i = startRow;
        int i2 = endRow;
        int i3 = startColumn;
        int i4 = endColumn;
        MatrixUtils.checkSubMatrixIndex(blockRealMatrix, i, i2, i3, i4);
        visitor.start(blockRealMatrix.rows, blockRealMatrix.columns, i, i2, i3, i4);
        int iBlock = i / 52;
        while (iBlock < (i2 / 52) + 1) {
            int p0 = iBlock * 52;
            int pStart = FastMath.max(i, p0);
            int pEnd = FastMath.min((iBlock + 1) * 52, i2 + 1);
            int jBlock = i3 / 52;
            while (jBlock < (i4 / 52) + 1) {
                int jWidth = blockRealMatrix.blockWidth(jBlock);
                int q0 = jBlock * 52;
                int qStart = FastMath.max(i3, q0);
                int qEnd = FastMath.min((jBlock + 1) * 52, i4 + 1);
                double[] block = blockRealMatrix.blocks[(blockRealMatrix.blockColumns * iBlock) + jBlock];
                int p = pStart;
                while (p < pEnd) {
                    int k = (((p - p0) * jWidth) + qStart) - q0;
                    int k2 = qStart;
                    while (true) {
                        int q = k2;
                        if (q >= qEnd) {
                            break;
                        }
                        int p02 = p0;
                        int pStart2 = pStart;
                        int pEnd2 = pEnd;
                        visitor.visit(p, q, block[k]);
                        k++;
                        k2 = q + 1;
                        p0 = p02;
                        pStart = pStart2;
                        pEnd = pEnd2;
                    }
                    int i5 = pStart;
                    int pEnd3 = pEnd;
                    RealMatrixPreservingVisitor realMatrixPreservingVisitor = visitor;
                    p++;
                    pEnd = pEnd3;
                }
                int i6 = pStart;
                int pEnd4 = pEnd;
                RealMatrixPreservingVisitor realMatrixPreservingVisitor2 = visitor;
                jBlock++;
                pEnd = pEnd4;
                blockRealMatrix = this;
            }
            RealMatrixPreservingVisitor realMatrixPreservingVisitor3 = visitor;
            iBlock++;
            blockRealMatrix = this;
        }
        RealMatrixPreservingVisitor realMatrixPreservingVisitor4 = visitor;
        return visitor.end();
    }

    private int blockHeight(int blockRow) {
        if (blockRow == this.blockRows - 1) {
            return this.rows - (blockRow * 52);
        }
        return 52;
    }

    private int blockWidth(int blockColumn) {
        if (blockColumn == this.blockColumns - 1) {
            return this.columns - (blockColumn * 52);
        }
        return 52;
    }
}
