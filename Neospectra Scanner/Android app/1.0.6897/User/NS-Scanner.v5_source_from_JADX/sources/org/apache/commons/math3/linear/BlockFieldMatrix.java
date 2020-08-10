package org.apache.commons.math3.linear;

import java.io.Serializable;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

public class BlockFieldMatrix<T extends FieldElement<T>> extends AbstractFieldMatrix<T> implements Serializable {
    public static final int BLOCK_SIZE = 36;
    private static final long serialVersionUID = -4602336630143123183L;
    private final int blockColumns;
    private final int blockRows;
    private final T[][] blocks;
    private final int columns;
    private final int rows;

    public BlockFieldMatrix(Field<T> field, int rows2, int columns2) throws NotStrictlyPositiveException {
        super(field, rows2, columns2);
        this.rows = rows2;
        this.columns = columns2;
        this.blockRows = ((rows2 + 36) - 1) / 36;
        this.blockColumns = ((columns2 + 36) - 1) / 36;
        this.blocks = createBlocksLayout(field, rows2, columns2);
    }

    public BlockFieldMatrix(T[][] rawData) throws DimensionMismatchException {
        this(rawData.length, rawData[0].length, toBlocksLayout(rawData), false);
    }

    public BlockFieldMatrix(int rows2, int columns2, T[][] blockData, boolean copyArray) throws DimensionMismatchException, NotStrictlyPositiveException {
        super(extractField(blockData), rows2, columns2);
        this.rows = rows2;
        this.columns = columns2;
        this.blockRows = ((rows2 + 36) - 1) / 36;
        this.blockColumns = ((columns2 + 36) - 1) / 36;
        if (copyArray) {
            this.blocks = (FieldElement[][]) MathArrays.buildArray(getField(), this.blockRows * this.blockColumns, -1);
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
                    this.blocks[index2] = (FieldElement[]) blockData[index2].clone();
                }
                jBlock++;
                index2++;
            }
            iBlock++;
            index = index2;
        }
    }

    public static <T extends FieldElement<T>> T[][] toBlocksLayout(T[][] rawData) throws DimensionMismatchException {
        int blockRows2;
        T[][] tArr = rawData;
        int rows2 = tArr.length;
        int columns2 = tArr[0].length;
        int p = ((rows2 + 36) - 1) / 36;
        int blockColumns2 = ((columns2 + 36) - 1) / 36;
        for (T[] length : tArr) {
            int length2 = length.length;
            if (length2 != columns2) {
                throw new DimensionMismatchException(columns2, length2);
            }
        }
        Field<T> field = extractField(rawData);
        T[][] blocks2 = (FieldElement[][]) MathArrays.buildArray(field, p * blockColumns2, -1);
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < p) {
            int pStart = iBlock * 36;
            int pEnd = FastMath.min(pStart + 36, rows2);
            int iHeight = pEnd - pStart;
            int blockIndex2 = blockIndex;
            int jBlock = 0;
            while (jBlock < blockColumns2) {
                int qStart = jBlock * 36;
                int jWidth = FastMath.min(qStart + 36, columns2) - qStart;
                int rows3 = rows2;
                T[] block = (FieldElement[]) MathArrays.buildArray(field, iHeight * jWidth);
                blocks2[blockIndex2] = block;
                int columns3 = columns2;
                int index = 0;
                int index2 = pStart;
                while (true) {
                    blockRows2 = p;
                    int blockRows3 = index2;
                    if (blockRows3 >= pEnd) {
                        break;
                    }
                    int blockColumns3 = blockColumns2;
                    System.arraycopy(tArr[blockRows3], qStart, block, index, jWidth);
                    index += jWidth;
                    index2 = blockRows3 + 1;
                    p = blockRows2;
                    blockColumns2 = blockColumns3;
                }
                blockIndex2++;
                jBlock++;
                rows2 = rows3;
                columns2 = columns3;
                p = blockRows2;
            }
            int i = columns2;
            int i2 = p;
            int i3 = blockColumns2;
            iBlock++;
            blockIndex = blockIndex2;
        }
        int i4 = columns2;
        int i5 = p;
        int i6 = blockColumns2;
        return blocks2;
    }

    public static <T extends FieldElement<T>> T[][] createBlocksLayout(Field<T> field, int rows2, int columns2) {
        Field<T> field2 = field;
        int i = rows2;
        int i2 = columns2;
        int blockRows2 = ((i + 36) - 1) / 36;
        int blockColumns2 = ((i2 + 36) - 1) / 36;
        T[][] blocks2 = (FieldElement[][]) MathArrays.buildArray(field2, blockRows2 * blockColumns2, -1);
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < blockRows2) {
            int pStart = iBlock * 36;
            int iHeight = FastMath.min(pStart + 36, i) - pStart;
            int blockIndex2 = blockIndex;
            for (int jBlock = 0; jBlock < blockColumns2; jBlock++) {
                int qStart = jBlock * 36;
                blocks2[blockIndex2] = (FieldElement[]) MathArrays.buildArray(field2, iHeight * (FastMath.min(qStart + 36, i2) - qStart));
                blockIndex2++;
            }
            iBlock++;
            blockIndex = blockIndex2;
        }
        return blocks2;
    }

    public FieldMatrix<T> createMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        return new BlockFieldMatrix(getField(), rowDimension, columnDimension);
    }

    public FieldMatrix<T> copy() {
        BlockFieldMatrix<T> copied = new BlockFieldMatrix<>(getField(), this.rows, this.columns);
        for (int i = 0; i < this.blocks.length; i++) {
            System.arraycopy(this.blocks[i], 0, copied.blocks[i], 0, this.blocks[i].length);
        }
        return copied;
    }

    public FieldMatrix<T> add(FieldMatrix<T> m) throws MatrixDimensionMismatchException {
        BlockFieldMatrix blockFieldMatrix = this;
        FieldMatrix<T> fieldMatrix = m;
        try {
            return blockFieldMatrix.add((BlockFieldMatrix) fieldMatrix);
        } catch (ClassCastException e) {
            ClassCastException cce = e;
            checkAdditionCompatible(m);
            BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), blockFieldMatrix.rows, blockFieldMatrix.columns);
            int blockIndex = 0;
            int iBlock = 0;
            while (iBlock < out.blockRows) {
                int blockIndex2 = blockIndex;
                int jBlock = 0;
                while (jBlock < out.blockColumns) {
                    T[] outBlock = out.blocks[blockIndex2];
                    T[] tBlock = blockFieldMatrix.blocks[blockIndex2];
                    int pStart = iBlock * 36;
                    int pEnd = FastMath.min(pStart + 36, blockFieldMatrix.rows);
                    int qStart = jBlock * 36;
                    int qEnd = FastMath.min(qStart + 36, blockFieldMatrix.columns);
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
                            ClassCastException cce2 = cce;
                            outBlock[k2] = (FieldElement) tBlock[k2].add(fieldMatrix.getEntry(p, q));
                            k2++;
                            k3 = q + 1;
                            cce = cce2;
                        }
                        p++;
                        k = k2;
                    }
                    blockIndex2++;
                    jBlock++;
                    blockFieldMatrix = this;
                }
                iBlock++;
                blockIndex = blockIndex2;
                blockFieldMatrix = this;
            }
            return out;
        }
    }

    public BlockFieldMatrix<T> add(BlockFieldMatrix<T> m) throws MatrixDimensionMismatchException {
        checkAdditionCompatible(m);
        BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            T[] outBlock = out.blocks[blockIndex];
            T[] tBlock = this.blocks[blockIndex];
            T[] mBlock = m.blocks[blockIndex];
            for (int k = 0; k < outBlock.length; k++) {
                outBlock[k] = (FieldElement) tBlock[k].add(mBlock[k]);
            }
        }
        return out;
    }

    public FieldMatrix<T> subtract(FieldMatrix<T> m) throws MatrixDimensionMismatchException {
        BlockFieldMatrix blockFieldMatrix = this;
        FieldMatrix<T> fieldMatrix = m;
        try {
            return blockFieldMatrix.subtract((BlockFieldMatrix) fieldMatrix);
        } catch (ClassCastException e) {
            ClassCastException cce = e;
            checkSubtractionCompatible(m);
            BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), blockFieldMatrix.rows, blockFieldMatrix.columns);
            int blockIndex = 0;
            int iBlock = 0;
            while (iBlock < out.blockRows) {
                int blockIndex2 = blockIndex;
                int jBlock = 0;
                while (jBlock < out.blockColumns) {
                    T[] outBlock = out.blocks[blockIndex2];
                    T[] tBlock = blockFieldMatrix.blocks[blockIndex2];
                    int pStart = iBlock * 36;
                    int pEnd = FastMath.min(pStart + 36, blockFieldMatrix.rows);
                    int qStart = jBlock * 36;
                    int qEnd = FastMath.min(qStart + 36, blockFieldMatrix.columns);
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
                            ClassCastException cce2 = cce;
                            outBlock[k2] = (FieldElement) tBlock[k2].subtract(fieldMatrix.getEntry(p, q));
                            k2++;
                            k3 = q + 1;
                            cce = cce2;
                        }
                        p++;
                        k = k2;
                    }
                    blockIndex2++;
                    jBlock++;
                    blockFieldMatrix = this;
                }
                iBlock++;
                blockIndex = blockIndex2;
                blockFieldMatrix = this;
            }
            return out;
        }
    }

    public BlockFieldMatrix<T> subtract(BlockFieldMatrix<T> m) throws MatrixDimensionMismatchException {
        checkSubtractionCompatible(m);
        BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            T[] outBlock = out.blocks[blockIndex];
            T[] tBlock = this.blocks[blockIndex];
            T[] mBlock = m.blocks[blockIndex];
            for (int k = 0; k < outBlock.length; k++) {
                outBlock[k] = (FieldElement) tBlock[k].subtract(mBlock[k]);
            }
        }
        return out;
    }

    public FieldMatrix<T> scalarAdd(T d) {
        BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            T[] outBlock = out.blocks[blockIndex];
            T[] tBlock = this.blocks[blockIndex];
            for (int k = 0; k < outBlock.length; k++) {
                outBlock[k] = (FieldElement) tBlock[k].add(d);
            }
        }
        return out;
    }

    public FieldMatrix<T> scalarMultiply(T d) {
        BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            T[] outBlock = out.blocks[blockIndex];
            T[] tBlock = this.blocks[blockIndex];
            for (int k = 0; k < outBlock.length; k++) {
                outBlock[k] = (FieldElement) tBlock[k].multiply(d);
            }
        }
        return out;
    }

    public FieldMatrix<T> multiply(FieldMatrix<T> m) throws DimensionMismatchException {
        int kWidth;
        int rStart;
        BlockFieldMatrix blockFieldMatrix = this;
        FieldMatrix<T> fieldMatrix = m;
        try {
            return blockFieldMatrix.multiply((BlockFieldMatrix) fieldMatrix);
        } catch (ClassCastException e) {
            ClassCastException cce = e;
            checkMultiplicationCompatible(m);
            BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), blockFieldMatrix.rows, m.getColumnDimension());
            FieldElement fieldElement = (FieldElement) getField().getZero();
            int blockIndex = 0;
            int iBlock = 0;
            while (iBlock < out.blockRows) {
                int pStart = iBlock * 36;
                int pEnd = FastMath.min(pStart + 36, blockFieldMatrix.rows);
                int blockIndex2 = blockIndex;
                int jBlock = 0;
                while (jBlock < out.blockColumns) {
                    int qStart = jBlock * 36;
                    int qEnd = FastMath.min(qStart + 36, m.getColumnDimension());
                    T[] outBlock = out.blocks[blockIndex2];
                    int kBlock = 0;
                    while (kBlock < blockFieldMatrix.blockColumns) {
                        int q = blockFieldMatrix.blockWidth(kBlock);
                        ClassCastException cce2 = cce;
                        int qStart2 = qStart;
                        T[] tBlock = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * iBlock) + kBlock];
                        int l = kBlock * 36;
                        int k = 0;
                        int k2 = pStart;
                        while (true) {
                            int p = k2;
                            if (p >= pEnd) {
                                break;
                            }
                            int lStart = (p - pStart) * q;
                            int pStart2 = pStart;
                            int lEnd = lStart + q;
                            int q2 = qStart2;
                            while (true) {
                                kWidth = q;
                                int kWidth2 = q2;
                                if (kWidth2 >= qEnd) {
                                    break;
                                }
                                FieldElement fieldElement2 = fieldElement;
                                FieldElement fieldElement3 = fieldElement;
                                int pEnd2 = pEnd;
                                int l2 = lStart;
                                int r = l;
                                while (true) {
                                    rStart = l;
                                    int l3 = l2;
                                    if (l3 >= lEnd) {
                                        break;
                                    }
                                    fieldElement = (FieldElement) fieldElement.add(tBlock[l3].multiply(fieldMatrix.getEntry(r, kWidth2)));
                                    r++;
                                    l2 = l3 + 1;
                                    l = rStart;
                                    lEnd = lEnd;
                                    tBlock = tBlock;
                                }
                                T[] tBlock2 = tBlock;
                                int i = lEnd;
                                outBlock[k] = (FieldElement) outBlock[k].add(fieldElement);
                                k++;
                                q2 = kWidth2 + 1;
                                q = kWidth;
                                fieldElement = fieldElement3;
                                pEnd = pEnd2;
                                l = rStart;
                                tBlock = tBlock2;
                            }
                            FieldElement fieldElement4 = fieldElement;
                            int i2 = pEnd;
                            int i3 = l;
                            k2 = p + 1;
                            pStart = pStart2;
                            q = kWidth;
                        }
                        FieldElement fieldElement5 = fieldElement;
                        int i4 = pStart;
                        int i5 = pEnd;
                        kBlock++;
                        cce = cce2;
                        qStart = qStart2;
                        blockFieldMatrix = this;
                    }
                    FieldElement fieldElement6 = fieldElement;
                    int i6 = pStart;
                    int i7 = pEnd;
                    int i8 = qStart;
                    blockIndex2++;
                    jBlock++;
                    blockFieldMatrix = this;
                }
                FieldElement fieldElement7 = fieldElement;
                iBlock++;
                blockIndex = blockIndex2;
                blockFieldMatrix = this;
            }
            FieldElement fieldElement8 = fieldElement;
            return out;
        }
    }

    public BlockFieldMatrix<T> multiply(BlockFieldMatrix<T> m) throws DimensionMismatchException {
        int kWidth;
        int pEnd;
        BlockFieldMatrix blockFieldMatrix = this;
        BlockFieldMatrix<T> blockFieldMatrix2 = m;
        checkMultiplicationCompatible(m);
        BlockFieldMatrix blockFieldMatrix3 = new BlockFieldMatrix(getField(), blockFieldMatrix.rows, blockFieldMatrix2.columns);
        FieldElement fieldElement = (FieldElement) getField().getZero();
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < blockFieldMatrix3.blockRows) {
            int pStart = iBlock * 36;
            int pEnd2 = FastMath.min(pStart + 36, blockFieldMatrix.rows);
            int blockIndex2 = blockIndex;
            int jBlock = 0;
            while (jBlock < blockFieldMatrix3.blockColumns) {
                int jWidth = blockFieldMatrix3.blockWidth(jBlock);
                int jWidth2 = jWidth + jWidth;
                int jWidth3 = jWidth2 + jWidth;
                int jWidth4 = jWidth3 + jWidth;
                T[] outBlock = blockFieldMatrix3.blocks[blockIndex2];
                int kBlock = 0;
                while (kBlock < blockFieldMatrix.blockColumns) {
                    int nStart = blockFieldMatrix.blockWidth(kBlock);
                    BlockFieldMatrix blockFieldMatrix4 = blockFieldMatrix3;
                    int blockIndex3 = blockIndex2;
                    T[] tBlock = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * iBlock) + kBlock];
                    T[] mBlock = blockFieldMatrix2.blocks[(blockFieldMatrix2.blockColumns * kBlock) + jBlock];
                    int k = 0;
                    int p = pStart;
                    while (p < pEnd2) {
                        int lStart = (p - pStart) * nStart;
                        int lEnd = lStart + nStart;
                        int k2 = k;
                        int n = 0;
                        while (true) {
                            kWidth = nStart;
                            int nStart2 = n;
                            if (nStart2 >= jWidth) {
                                break;
                            }
                            FieldElement fieldElement2 = fieldElement;
                            int pStart2 = pStart;
                            FieldElement fieldElement3 = fieldElement;
                            int l = lStart;
                            int n2 = nStart2;
                            while (true) {
                                pEnd = pEnd2;
                                if (l >= lEnd - 3) {
                                    break;
                                }
                                FieldElement fieldElement4 = fieldElement3;
                                fieldElement3 = (FieldElement) ((FieldElement) ((FieldElement) ((FieldElement) fieldElement3.add(tBlock[l].multiply(mBlock[n2]))).add(tBlock[l + 1].multiply(mBlock[n2 + jWidth]))).add(tBlock[l + 2].multiply(mBlock[n2 + jWidth2]))).add(tBlock[l + 3].multiply(mBlock[n2 + jWidth3]));
                                l += 4;
                                n2 += jWidth4;
                                pEnd2 = pEnd;
                                iBlock = iBlock;
                            }
                            int iBlock2 = iBlock;
                            FieldElement fieldElement5 = fieldElement3;
                            while (l < lEnd) {
                                fieldElement5 = (FieldElement) fieldElement5.add(tBlock[l].multiply(mBlock[n2]));
                                n2 += jWidth;
                                l++;
                            }
                            outBlock[k2] = (FieldElement) outBlock[k2].add(fieldElement5);
                            k2++;
                            n = nStart2 + 1;
                            nStart = kWidth;
                            fieldElement = fieldElement2;
                            pStart = pStart2;
                            pEnd2 = pEnd;
                            iBlock = iBlock2;
                        }
                        FieldElement fieldElement6 = fieldElement;
                        int i = iBlock;
                        int i2 = pStart;
                        int i3 = pEnd2;
                        p++;
                        k = k2;
                        nStart = kWidth;
                        BlockFieldMatrix<T> blockFieldMatrix5 = m;
                    }
                    FieldElement fieldElement7 = fieldElement;
                    int i4 = iBlock;
                    int i5 = pStart;
                    int i6 = pEnd2;
                    kBlock++;
                    blockFieldMatrix3 = blockFieldMatrix4;
                    blockIndex2 = blockIndex3;
                    blockFieldMatrix = this;
                    blockFieldMatrix2 = m;
                }
                BlockFieldMatrix blockFieldMatrix6 = blockFieldMatrix3;
                FieldElement fieldElement8 = fieldElement;
                int i7 = iBlock;
                int i8 = pStart;
                int i9 = pEnd2;
                blockIndex2++;
                jBlock++;
                blockFieldMatrix = this;
                blockFieldMatrix2 = m;
            }
            BlockFieldMatrix blockFieldMatrix7 = blockFieldMatrix3;
            FieldElement fieldElement9 = fieldElement;
            iBlock++;
            blockIndex = blockIndex2;
            blockFieldMatrix = this;
            blockFieldMatrix2 = m;
        }
        FieldElement fieldElement10 = fieldElement;
        return blockFieldMatrix3;
    }

    public T[][] getData() {
        T[][] data = (FieldElement[][]) MathArrays.buildArray(getField(), getRowDimension(), getColumnDimension());
        int lastColumns = this.columns - ((this.blockColumns - 1) * 36);
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 36;
            int pEnd = FastMath.min(pStart + 36, this.rows);
            int lastPos = 0;
            int regularPos = 0;
            for (int p = pStart; p < pEnd; p++) {
                T[] dataP = data[p];
                int dataPos = 0;
                int dataPos2 = this.blockColumns * iBlock;
                int jBlock = 0;
                while (jBlock < this.blockColumns - 1) {
                    int blockIndex = dataPos2 + 1;
                    System.arraycopy(this.blocks[dataPos2], regularPos, dataP, dataPos, 36);
                    dataPos += 36;
                    jBlock++;
                    dataPos2 = blockIndex;
                }
                System.arraycopy(this.blocks[dataPos2], lastPos, dataP, dataPos, lastColumns);
                regularPos += 36;
                lastPos += lastColumns;
            }
        }
        return data;
    }

    public FieldMatrix<T> getSubMatrix(int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        int iBlock;
        int qBlock;
        int jBlock;
        BlockFieldMatrix blockFieldMatrix;
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        BlockFieldMatrix blockFieldMatrix2 = new BlockFieldMatrix(getField(), (endRow - startRow) + 1, (endColumn - startColumn) + 1);
        int rowsShift = startRow % 36;
        int blockStartColumn = startColumn / 36;
        int columnsShift = startColumn % 36;
        int pBlock = startRow / 36;
        int pBlock2 = 0;
        while (true) {
            int iBlock2 = pBlock2;
            if (iBlock2 >= blockFieldMatrix2.blockRows) {
                return blockFieldMatrix2;
            }
            int iHeight = blockFieldMatrix2.blockHeight(iBlock2);
            int qBlock2 = blockStartColumn;
            int qBlock3 = 0;
            while (true) {
                int jBlock2 = qBlock3;
                if (jBlock2 >= blockFieldMatrix2.blockColumns) {
                    break;
                }
                int jWidth = blockFieldMatrix2.blockWidth(jBlock2);
                T[] outBlock = blockFieldMatrix2.blocks[(blockFieldMatrix2.blockColumns * iBlock2) + jBlock2];
                int index = (this.blockColumns * pBlock) + qBlock2;
                int width = blockWidth(qBlock2);
                int heightExcess = (iHeight + rowsShift) - 36;
                int widthExcess = (jWidth + columnsShift) - 36;
                if (heightExcess <= 0) {
                    jBlock = jBlock2;
                    qBlock = qBlock2;
                    iBlock = iBlock2;
                    blockFieldMatrix = blockFieldMatrix2;
                    if (widthExcess > 0) {
                        int width2 = blockWidth(qBlock + 1);
                        int i = rowsShift;
                        T[] tArr = outBlock;
                        int i2 = jWidth;
                        copyBlockPart(this.blocks[index], width, i, iHeight + rowsShift, columnsShift, 36, tArr, i2, 0, 0);
                        copyBlockPart(this.blocks[index + 1], width2, i, iHeight + rowsShift, 0, widthExcess, tArr, i2, 0, jWidth - widthExcess);
                    } else {
                        copyBlockPart(this.blocks[index], width, rowsShift, iHeight + rowsShift, columnsShift, jWidth + columnsShift, outBlock, jWidth, 0, 0);
                    }
                } else if (widthExcess > 0) {
                    int width22 = blockWidth(qBlock2 + 1);
                    int i3 = rowsShift;
                    jBlock = jBlock2;
                    T[] tArr2 = outBlock;
                    qBlock = qBlock2;
                    int qBlock4 = jWidth;
                    iBlock = iBlock2;
                    blockFieldMatrix = blockFieldMatrix2;
                    copyBlockPart(this.blocks[index], width, i3, 36, columnsShift, 36, tArr2, qBlock4, 0, 0);
                    copyBlockPart(this.blocks[index + 1], width22, i3, 36, 0, widthExcess, tArr2, qBlock4, 0, jWidth - widthExcess);
                    int i4 = heightExcess;
                    copyBlockPart(this.blocks[index + this.blockColumns], width, 0, i4, columnsShift, 36, tArr2, qBlock4, iHeight - heightExcess, 0);
                    copyBlockPart(this.blocks[index + this.blockColumns + 1], width22, 0, i4, 0, widthExcess, tArr2, qBlock4, iHeight - heightExcess, jWidth - widthExcess);
                } else {
                    jBlock = jBlock2;
                    qBlock = qBlock2;
                    iBlock = iBlock2;
                    blockFieldMatrix = blockFieldMatrix2;
                    int i5 = width;
                    int i6 = columnsShift;
                    T[] tArr3 = outBlock;
                    int i7 = jWidth;
                    copyBlockPart(this.blocks[index], i5, rowsShift, 36, i6, jWidth + columnsShift, tArr3, i7, 0, 0);
                    copyBlockPart(this.blocks[index + this.blockColumns], i5, 0, heightExcess, i6, jWidth + columnsShift, tArr3, i7, iHeight - heightExcess, 0);
                }
                qBlock2 = qBlock + 1;
                qBlock3 = jBlock + 1;
                blockFieldMatrix2 = blockFieldMatrix;
                iBlock2 = iBlock;
            }
            int i8 = qBlock2;
            BlockFieldMatrix blockFieldMatrix3 = blockFieldMatrix2;
            pBlock++;
            pBlock2 = iBlock2 + 1;
        }
    }

    private void copyBlockPart(T[] srcBlock, int srcWidth, int srcStartRow, int srcEndRow, int srcStartColumn, int srcEndColumn, T[] dstBlock, int dstWidth, int dstStartRow, int dstStartColumn) {
        int length = srcEndColumn - srcStartColumn;
        int dstPos = (dstStartRow * dstWidth) + dstStartColumn;
        int srcPos = (srcStartRow * srcWidth) + srcStartColumn;
        for (int srcRow = srcStartRow; srcRow < srcEndRow; srcRow++) {
            System.arraycopy(srcBlock, srcPos, dstBlock, dstPos, length);
            srcPos += srcWidth;
            dstPos += dstWidth;
        }
    }

    public void setSubMatrix(T[][] subMatrix, int row, int column) throws DimensionMismatchException, OutOfRangeException, NoDataException, NullArgumentException {
        FieldElement[][] arr$;
        BlockFieldMatrix blockFieldMatrix = this;
        FieldElement[][] fieldElementArr = subMatrix;
        int i = row;
        int i2 = column;
        MathUtils.checkNotNull(subMatrix);
        int refLength = fieldElementArr[0].length;
        if (refLength == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
        }
        int endRow = (fieldElementArr.length + i) - 1;
        int endColumn = (i2 + refLength) - 1;
        blockFieldMatrix.checkSubMatrixIndex(i, endRow, i2, endColumn);
        for (FieldElement[] fieldElementArr2 : fieldElementArr) {
            if (fieldElementArr2.length != refLength) {
                throw new DimensionMismatchException(refLength, fieldElementArr2.length);
            }
        }
        int blockStartRow = i / 36;
        int blockEndRow = (endRow + 36) / 36;
        int blockStartColumn = i2 / 36;
        int blockEndColumn = (endColumn + 36) / 36;
        int iBlock = blockStartRow;
        while (iBlock < blockEndRow) {
            int iHeight = blockFieldMatrix.blockHeight(iBlock);
            int firstRow = iBlock * 36;
            int iStart = FastMath.max(i, firstRow);
            int blockStartRow2 = blockStartRow;
            int iEnd = FastMath.min(endRow + 1, firstRow + iHeight);
            int jBlock = blockStartColumn;
            while (jBlock < blockEndColumn) {
                int jWidth = blockFieldMatrix.blockWidth(jBlock);
                int refLength2 = refLength;
                int refLength3 = jBlock * 36;
                int jStart = FastMath.max(i2, refLength3);
                int endRow2 = endRow;
                int endColumn2 = endColumn;
                int jEnd = FastMath.min(endColumn + 1, refLength3 + jWidth);
                int jLength = jEnd - jStart;
                int i3 = jEnd;
                int blockEndRow2 = blockEndRow;
                T[] block = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * iBlock) + jBlock];
                int i4 = iStart;
                while (i4 < iEnd) {
                    System.arraycopy(fieldElementArr[i4 - i], jStart - i2, block, ((i4 - firstRow) * jWidth) + (jStart - refLength3), jLength);
                    i4++;
                    fieldElementArr = subMatrix;
                    i = row;
                }
                jBlock++;
                refLength = refLength2;
                endRow = endRow2;
                endColumn = endColumn2;
                blockEndRow = blockEndRow2;
                blockFieldMatrix = this;
                fieldElementArr = subMatrix;
                i = row;
            }
            int i5 = endRow;
            int i6 = endColumn;
            int i7 = blockEndRow;
            iBlock++;
            blockStartRow = blockStartRow2;
            blockFieldMatrix = this;
            fieldElementArr = subMatrix;
            i = row;
        }
        int i8 = refLength;
        int i9 = endRow;
        int i10 = endColumn;
        int i11 = blockEndRow;
    }

    public FieldMatrix<T> getRowMatrix(int row) throws OutOfRangeException {
        checkRowIndex(row);
        BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), 1, this.columns);
        int iBlock = row / 36;
        int iRow = row - (iBlock * 36);
        int outIndex = 0;
        T[] outBlock = out.blocks[0];
        int outBlockIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
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

    public void setRowMatrix(int row, FieldMatrix<T> matrix) throws MatrixDimensionMismatchException, OutOfRangeException {
        try {
            setRowMatrix(row, (BlockFieldMatrix) matrix);
        } catch (ClassCastException e) {
            super.setRowMatrix(row, matrix);
        }
    }

    public void setRowMatrix(int row, BlockFieldMatrix<T> matrix) throws MatrixDimensionMismatchException, OutOfRangeException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (matrix.getRowDimension() == 1 && matrix.getColumnDimension() == nCols) {
            int iBlock = row / 36;
            int iRow = row - (iBlock * 36);
            int mIndex = 0;
            T[] mBlock = matrix.blocks[0];
            int mBlockIndex = 0;
            for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                int jWidth = blockWidth(jBlock);
                T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
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

    public FieldMatrix<T> getColumnMatrix(int column) throws OutOfRangeException {
        checkColumnIndex(column);
        BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), this.rows, 1);
        int jBlock = column / 36;
        int jColumn = column - (jBlock * 36);
        int jWidth = blockWidth(jBlock);
        T[] outBlock = out.blocks[0];
        int outIndex = 0;
        int outIndex2 = 0;
        int iBlock = 0;
        while (iBlock < this.blockRows) {
            int iHeight = blockHeight(iBlock);
            T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
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

    public void setColumnMatrix(int column, FieldMatrix<T> matrix) throws MatrixDimensionMismatchException, OutOfRangeException {
        try {
            setColumnMatrix(column, (BlockFieldMatrix) matrix);
        } catch (ClassCastException e) {
            super.setColumnMatrix(column, matrix);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setColumnMatrix(int column, BlockFieldMatrix<T> matrix) throws MatrixDimensionMismatchException, OutOfRangeException {
        BlockFieldMatrix<T> blockFieldMatrix = matrix;
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (matrix.getRowDimension() == nRows && matrix.getColumnDimension() == 1) {
            int jBlock = column / 36;
            int jColumn = column - (jBlock * 36);
            int jWidth = blockWidth(jBlock);
            T[] mBlock = blockFieldMatrix.blocks[0];
            int mIndex = 0;
            int mIndex2 = 0;
            int iBlock = 0;
            while (iBlock < this.blockRows) {
                int iHeight = blockHeight(iBlock);
                T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
                int mBlockIndex = mIndex2;
                int i = 0;
                while (i < iHeight) {
                    if (mIndex >= mBlock.length) {
                        mBlockIndex++;
                        mBlock = blockFieldMatrix.blocks[mBlockIndex];
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

    public FieldVector<T> getRowVector(int row) throws OutOfRangeException {
        checkRowIndex(row);
        T[] outData = (FieldElement[]) MathArrays.buildArray(getField(), this.columns);
        int iBlock = row / 36;
        int iRow = row - (iBlock * 36);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            System.arraycopy(this.blocks[(this.blockColumns * iBlock) + jBlock], iRow * jWidth, outData, outIndex, jWidth);
            outIndex += jWidth;
        }
        return new ArrayFieldVector(getField(), outData, false);
    }

    public void setRowVector(int row, FieldVector<T> vector) throws MatrixDimensionMismatchException, OutOfRangeException {
        try {
            setRow(row, ((ArrayFieldVector) vector).getDataRef());
        } catch (ClassCastException e) {
            super.setRowVector(row, vector);
        }
    }

    public FieldVector<T> getColumnVector(int column) throws OutOfRangeException {
        checkColumnIndex(column);
        T[] outData = (FieldElement[]) MathArrays.buildArray(getField(), this.rows);
        int jBlock = column / 36;
        int jColumn = column - (jBlock * 36);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        int iBlock = 0;
        while (iBlock < this.blockRows) {
            int iHeight = blockHeight(iBlock);
            T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
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
        return new ArrayFieldVector(getField(), outData, false);
    }

    public void setColumnVector(int column, FieldVector<T> vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        try {
            setColumn(column, ((ArrayFieldVector) vector).getDataRef());
        } catch (ClassCastException e) {
            super.setColumnVector(column, vector);
        }
    }

    public T[] getRow(int row) throws OutOfRangeException {
        checkRowIndex(row);
        T[] out = (FieldElement[]) MathArrays.buildArray(getField(), this.columns);
        int iBlock = row / 36;
        int iRow = row - (iBlock * 36);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            System.arraycopy(this.blocks[(this.blockColumns * iBlock) + jBlock], iRow * jWidth, out, outIndex, jWidth);
            outIndex += jWidth;
        }
        return out;
    }

    public void setRow(int row, T[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (array.length != nCols) {
            throw new MatrixDimensionMismatchException(1, array.length, 1, nCols);
        }
        int iBlock = row / 36;
        int iRow = row - (iBlock * 36);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            System.arraycopy(array, outIndex, this.blocks[(this.blockColumns * iBlock) + jBlock], iRow * jWidth, jWidth);
            outIndex += jWidth;
        }
    }

    public T[] getColumn(int column) throws OutOfRangeException {
        checkColumnIndex(column);
        T[] out = (FieldElement[]) MathArrays.buildArray(getField(), this.rows);
        int jBlock = column / 36;
        int jColumn = column - (jBlock * 36);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        int iBlock = 0;
        while (iBlock < this.blockRows) {
            int iHeight = blockHeight(iBlock);
            T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
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

    public void setColumn(int column, T[] array) throws MatrixDimensionMismatchException, OutOfRangeException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (array.length != nRows) {
            throw new MatrixDimensionMismatchException(array.length, 1, nRows, 1);
        }
        int jBlock = column / 36;
        int jColumn = column - (jBlock * 36);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        int iBlock = 0;
        while (iBlock < this.blockRows) {
            int iHeight = blockHeight(iBlock);
            T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
            int outIndex2 = outIndex;
            int i = 0;
            while (i < iHeight) {
                int outIndex3 = outIndex2 + 1;
                block[(i * jWidth) + jColumn] = array[outIndex2];
                i++;
                outIndex2 = outIndex3;
            }
            iBlock++;
            outIndex = outIndex2;
        }
    }

    public T getEntry(int row, int column) throws OutOfRangeException {
        checkRowIndex(row);
        checkColumnIndex(column);
        int iBlock = row / 36;
        int jBlock = column / 36;
        return this.blocks[(this.blockColumns * iBlock) + jBlock][((row - (iBlock * 36)) * blockWidth(jBlock)) + (column - (jBlock * 36))];
    }

    public void setEntry(int row, int column, T value) throws OutOfRangeException {
        checkRowIndex(row);
        checkColumnIndex(column);
        int iBlock = row / 36;
        int jBlock = column / 36;
        this.blocks[(this.blockColumns * iBlock) + jBlock][((row - (iBlock * 36)) * blockWidth(jBlock)) + (column - (jBlock * 36))] = value;
    }

    public void addToEntry(int row, int column, T increment) throws OutOfRangeException {
        checkRowIndex(row);
        checkColumnIndex(column);
        int iBlock = row / 36;
        int jBlock = column / 36;
        int k = ((row - (iBlock * 36)) * blockWidth(jBlock)) + (column - (jBlock * 36));
        T[] blockIJ = this.blocks[(this.blockColumns * iBlock) + jBlock];
        blockIJ[k] = (FieldElement) blockIJ[k].add(increment);
    }

    public void multiplyEntry(int row, int column, T factor) throws OutOfRangeException {
        checkRowIndex(row);
        checkColumnIndex(column);
        int iBlock = row / 36;
        int jBlock = column / 36;
        int k = ((row - (iBlock * 36)) * blockWidth(jBlock)) + (column - (jBlock * 36));
        T[] blockIJ = this.blocks[(this.blockColumns * iBlock) + jBlock];
        blockIJ[k] = (FieldElement) blockIJ[k].multiply(factor);
    }

    public FieldMatrix<T> transpose() {
        int nRows = getRowDimension();
        BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), getColumnDimension(), nRows);
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < this.blockColumns) {
            int blockIndex2 = blockIndex;
            for (int jBlock = 0; jBlock < this.blockRows; jBlock++) {
                T[] outBlock = out.blocks[blockIndex2];
                T[] tBlock = this.blocks[(this.blockColumns * jBlock) + iBlock];
                int pStart = iBlock * 36;
                int pEnd = FastMath.min(pStart + 36, this.columns);
                int qStart = jBlock * 36;
                int qEnd = FastMath.min(qStart + 36, this.rows);
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

    public T[] operate(T[] v) throws DimensionMismatchException {
        BlockFieldMatrix blockFieldMatrix = this;
        T[] tArr = v;
        if (tArr.length != blockFieldMatrix.columns) {
            throw new DimensionMismatchException(tArr.length, blockFieldMatrix.columns);
        }
        T[] out = (FieldElement[]) MathArrays.buildArray(getField(), blockFieldMatrix.rows);
        FieldElement fieldElement = (FieldElement) getField().getZero();
        int iBlock = 0;
        while (iBlock < blockFieldMatrix.blockRows) {
            int pStart = iBlock * 36;
            int pEnd = FastMath.min(pStart + 36, blockFieldMatrix.rows);
            int jBlock = 0;
            while (jBlock < blockFieldMatrix.blockColumns) {
                T[] block = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * iBlock) + jBlock];
                int qStart = jBlock * 36;
                int qEnd = FastMath.min(qStart + 36, blockFieldMatrix.columns);
                int k = 0;
                int p = pStart;
                while (p < pEnd) {
                    FieldElement fieldElement2 = fieldElement;
                    int k2 = k;
                    int q = qStart;
                    while (q < qEnd - 3) {
                        fieldElement2 = (FieldElement) ((FieldElement) ((FieldElement) ((FieldElement) fieldElement2.add(block[k2].multiply(tArr[q]))).add(block[k2 + 1].multiply(tArr[q + 1]))).add(block[k2 + 2].multiply(tArr[q + 2]))).add(block[k2 + 3].multiply(tArr[q + 3]));
                        k2 += 4;
                        q += 4;
                        fieldElement = fieldElement;
                    }
                    FieldElement fieldElement3 = fieldElement;
                    while (q < qEnd) {
                        fieldElement2 = (FieldElement) fieldElement2.add(block[k2].multiply(tArr[q]));
                        k2++;
                        q++;
                    }
                    out[p] = (FieldElement) out[p].add(fieldElement2);
                    p++;
                    k = k2;
                    fieldElement = fieldElement3;
                }
                FieldElement fieldElement4 = fieldElement;
                jBlock++;
                blockFieldMatrix = this;
            }
            FieldElement fieldElement5 = fieldElement;
            iBlock++;
            blockFieldMatrix = this;
        }
        FieldElement fieldElement6 = fieldElement;
        return out;
    }

    public T[] preMultiply(T[] v) throws DimensionMismatchException {
        FieldElement fieldElement;
        int qStart;
        int p;
        BlockFieldMatrix blockFieldMatrix = this;
        T[] tArr = v;
        if (tArr.length != blockFieldMatrix.rows) {
            throw new DimensionMismatchException(tArr.length, blockFieldMatrix.rows);
        }
        T[] out = (FieldElement[]) MathArrays.buildArray(getField(), blockFieldMatrix.columns);
        FieldElement fieldElement2 = (FieldElement) getField().getZero();
        int jBlock = 0;
        while (jBlock < blockFieldMatrix.blockColumns) {
            int jWidth = blockFieldMatrix.blockWidth(jBlock);
            int jWidth2 = jWidth + jWidth;
            int jWidth3 = jWidth2 + jWidth;
            int jWidth4 = jWidth3 + jWidth;
            int qStart2 = jBlock * 36;
            int qEnd = FastMath.min(qStart2 + 36, blockFieldMatrix.columns);
            int iBlock = 0;
            while (iBlock < blockFieldMatrix.blockRows) {
                T[] block = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * iBlock) + jBlock];
                int pStart = iBlock * 36;
                int pEnd = FastMath.min(pStart + 36, blockFieldMatrix.rows);
                int q = qStart2;
                while (q < qEnd) {
                    FieldElement fieldElement3 = fieldElement2;
                    int k = q - qStart2;
                    int k2 = pStart;
                    while (true) {
                        fieldElement = fieldElement2;
                        qStart = qStart2;
                        p = k2;
                        if (p >= pEnd - 3) {
                            break;
                        }
                        FieldElement fieldElement4 = fieldElement3;
                        fieldElement3 = (FieldElement) ((FieldElement) ((FieldElement) ((FieldElement) fieldElement3.add(block[k].multiply(tArr[p]))).add(block[k + jWidth].multiply(tArr[p + 1]))).add(block[k + jWidth2].multiply(tArr[p + 2]))).add(block[k + jWidth3].multiply(tArr[p + 3]));
                        k += jWidth4;
                        k2 = p + 4;
                        fieldElement2 = fieldElement;
                        qStart2 = qStart;
                        qEnd = qEnd;
                    }
                    FieldElement fieldElement5 = fieldElement3;
                    int qEnd2 = qEnd;
                    while (p < pEnd) {
                        fieldElement3 = (FieldElement) fieldElement3.add(block[k].multiply(tArr[p]));
                        k += jWidth;
                        p++;
                    }
                    out[q] = (FieldElement) out[q].add(fieldElement3);
                    q++;
                    fieldElement2 = fieldElement;
                    qStart2 = qStart;
                    qEnd = qEnd2;
                }
                FieldElement fieldElement6 = fieldElement2;
                int i = qStart2;
                int i2 = qEnd;
                iBlock++;
                blockFieldMatrix = this;
            }
            FieldElement fieldElement7 = fieldElement2;
            jBlock++;
            blockFieldMatrix = this;
        }
        FieldElement fieldElement8 = fieldElement2;
        return out;
    }

    public T walkInRowOrder(FieldMatrixChangingVisitor<T> visitor) {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 36;
            int pEnd = FastMath.min(pStart + 36, this.rows);
            for (int p = pStart; p < pEnd; p++) {
                for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                    int jWidth = blockWidth(jBlock);
                    int qStart = jBlock * 36;
                    int qEnd = FastMath.min(qStart + 36, this.columns);
                    T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
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

    public T walkInRowOrder(FieldMatrixPreservingVisitor<T> visitor) {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 36;
            int pEnd = FastMath.min(pStart + 36, this.rows);
            for (int p = pStart; p < pEnd; p++) {
                for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                    int jWidth = blockWidth(jBlock);
                    int qStart = jBlock * 36;
                    int qEnd = FastMath.min(qStart + 36, this.columns);
                    T[] block = this.blocks[(this.blockColumns * iBlock) + jBlock];
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

    public T walkInRowOrder(FieldMatrixChangingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        BlockFieldMatrix blockFieldMatrix = this;
        int i = startRow;
        int i2 = endRow;
        int i3 = startColumn;
        int i4 = endColumn;
        blockFieldMatrix.checkSubMatrixIndex(i, i2, i3, i4);
        visitor.start(blockFieldMatrix.rows, blockFieldMatrix.columns, i, i2, i3, i4);
        int iBlock = i / 36;
        while (iBlock < (i2 / 36) + 1) {
            int p0 = iBlock * 36;
            int pStart = FastMath.max(i, p0);
            int pEnd = FastMath.min((iBlock + 1) * 36, i2 + 1);
            int p = pStart;
            while (p < pEnd) {
                int jBlock = i3 / 36;
                while (jBlock < (i4 / 36) + 1) {
                    int jWidth = blockFieldMatrix.blockWidth(jBlock);
                    int q0 = jBlock * 36;
                    int qStart = FastMath.max(i3, q0);
                    int qEnd = FastMath.min((jBlock + 1) * 36, i4 + 1);
                    int pStart2 = pStart;
                    T[] block = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * iBlock) + jBlock];
                    int k = (((p - p0) * jWidth) + qStart) - q0;
                    int q = qStart;
                    while (q < qEnd) {
                        int p02 = p0;
                        block[k] = visitor.visit(p, q, block[k]);
                        k++;
                        q++;
                        p0 = p02;
                    }
                    int p03 = p0;
                    FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor = visitor;
                    jBlock++;
                    pStart = pStart2;
                    p0 = p03;
                    blockFieldMatrix = this;
                }
                int p04 = p0;
                int i5 = pStart;
                FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor2 = visitor;
                p++;
                p0 = p04;
                blockFieldMatrix = this;
            }
            FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor3 = visitor;
            iBlock++;
            blockFieldMatrix = this;
        }
        FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor4 = visitor;
        return visitor.end();
    }

    public T walkInRowOrder(FieldMatrixPreservingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        BlockFieldMatrix blockFieldMatrix = this;
        int i = startRow;
        int i2 = endRow;
        int i3 = startColumn;
        int i4 = endColumn;
        blockFieldMatrix.checkSubMatrixIndex(i, i2, i3, i4);
        visitor.start(blockFieldMatrix.rows, blockFieldMatrix.columns, i, i2, i3, i4);
        int iBlock = i / 36;
        while (iBlock < (i2 / 36) + 1) {
            int p0 = iBlock * 36;
            int pStart = FastMath.max(i, p0);
            int pEnd = FastMath.min((iBlock + 1) * 36, i2 + 1);
            int p = pStart;
            while (p < pEnd) {
                int jBlock = i3 / 36;
                while (jBlock < (i4 / 36) + 1) {
                    int jWidth = blockFieldMatrix.blockWidth(jBlock);
                    int q0 = jBlock * 36;
                    int qStart = FastMath.max(i3, q0);
                    int qEnd = FastMath.min((jBlock + 1) * 36, i4 + 1);
                    int pStart2 = pStart;
                    T[] block = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * iBlock) + jBlock];
                    int k = (((p - p0) * jWidth) + qStart) - q0;
                    int q = qStart;
                    while (q < qEnd) {
                        int p02 = p0;
                        visitor.visit(p, q, block[k]);
                        k++;
                        q++;
                        p0 = p02;
                    }
                    int p03 = p0;
                    FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor = visitor;
                    jBlock++;
                    pStart = pStart2;
                    p0 = p03;
                    blockFieldMatrix = this;
                }
                int p04 = p0;
                int i5 = pStart;
                FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor2 = visitor;
                p++;
                p0 = p04;
                blockFieldMatrix = this;
            }
            FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor3 = visitor;
            iBlock++;
            blockFieldMatrix = this;
        }
        FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor4 = visitor;
        return visitor.end();
    }

    public T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> visitor) {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < this.blockRows) {
            int pStart = iBlock * 36;
            int pEnd = FastMath.min(pStart + 36, this.rows);
            int blockIndex2 = blockIndex;
            for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                int qStart = jBlock * 36;
                int qEnd = FastMath.min(qStart + 36, this.columns);
                T[] block = this.blocks[blockIndex2];
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

    public T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> visitor) {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        int blockIndex = 0;
        int iBlock = 0;
        while (iBlock < this.blockRows) {
            int pStart = iBlock * 36;
            int pEnd = FastMath.min(pStart + 36, this.rows);
            int blockIndex2 = blockIndex;
            for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                int qStart = jBlock * 36;
                int qEnd = FastMath.min(qStart + 36, this.columns);
                T[] block = this.blocks[blockIndex2];
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

    public T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        BlockFieldMatrix blockFieldMatrix = this;
        int i = startRow;
        int i2 = endRow;
        int i3 = startColumn;
        int i4 = endColumn;
        blockFieldMatrix.checkSubMatrixIndex(i, i2, i3, i4);
        visitor.start(blockFieldMatrix.rows, blockFieldMatrix.columns, i, i2, i3, i4);
        int iBlock = i / 36;
        while (iBlock < (i2 / 36) + 1) {
            int p0 = iBlock * 36;
            int pStart = FastMath.max(i, p0);
            int pEnd = FastMath.min((iBlock + 1) * 36, i2 + 1);
            int jBlock = i3 / 36;
            while (jBlock < (i4 / 36) + 1) {
                int jWidth = blockFieldMatrix.blockWidth(jBlock);
                int q0 = jBlock * 36;
                int qStart = FastMath.max(i3, q0);
                int qEnd = FastMath.min((jBlock + 1) * 36, i4 + 1);
                T[] block = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * iBlock) + jBlock];
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
                        block[k] = visitor.visit(p, q, block[k]);
                        k++;
                        k2 = q + 1;
                        p0 = p02;
                        pStart = pStart2;
                    }
                    int pStart3 = pStart;
                    FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor = visitor;
                    p++;
                    pStart = pStart3;
                }
                int pStart4 = pStart;
                FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor2 = visitor;
                jBlock++;
                pStart = pStart4;
                blockFieldMatrix = this;
            }
            FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor3 = visitor;
            iBlock++;
            blockFieldMatrix = this;
        }
        FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor4 = visitor;
        return visitor.end();
    }

    public T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        BlockFieldMatrix blockFieldMatrix = this;
        int i = startRow;
        int i2 = endRow;
        int i3 = startColumn;
        int i4 = endColumn;
        blockFieldMatrix.checkSubMatrixIndex(i, i2, i3, i4);
        visitor.start(blockFieldMatrix.rows, blockFieldMatrix.columns, i, i2, i3, i4);
        int iBlock = i / 36;
        while (iBlock < (i2 / 36) + 1) {
            int p0 = iBlock * 36;
            int pStart = FastMath.max(i, p0);
            int pEnd = FastMath.min((iBlock + 1) * 36, i2 + 1);
            int jBlock = i3 / 36;
            while (jBlock < (i4 / 36) + 1) {
                int jWidth = blockFieldMatrix.blockWidth(jBlock);
                int q0 = jBlock * 36;
                int qStart = FastMath.max(i3, q0);
                int qEnd = FastMath.min((jBlock + 1) * 36, i4 + 1);
                T[] block = blockFieldMatrix.blocks[(blockFieldMatrix.blockColumns * iBlock) + jBlock];
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
                        visitor.visit(p, q, block[k]);
                        k++;
                        k2 = q + 1;
                        p0 = p02;
                        pStart = pStart2;
                    }
                    int pStart3 = pStart;
                    FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor = visitor;
                    p++;
                    pStart = pStart3;
                }
                int pStart4 = pStart;
                FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor2 = visitor;
                jBlock++;
                pStart = pStart4;
                blockFieldMatrix = this;
            }
            FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor3 = visitor;
            iBlock++;
            blockFieldMatrix = this;
        }
        FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor4 = visitor;
        return visitor.end();
    }

    private int blockHeight(int blockRow) {
        if (blockRow == this.blockRows - 1) {
            return this.rows - (blockRow * 36);
        }
        return 36;
    }

    private int blockWidth(int blockColumn) {
        if (blockColumn == this.blockColumns - 1) {
            return this.columns - (blockColumn * 36);
        }
        return 36;
    }
}
