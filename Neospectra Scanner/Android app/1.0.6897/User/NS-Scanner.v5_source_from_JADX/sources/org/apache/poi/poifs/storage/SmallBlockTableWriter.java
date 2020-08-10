package org.apache.poi.poifs.storage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.poifs.filesystem.BATManaged;
import org.apache.poi.poifs.property.RootProperty;

public class SmallBlockTableWriter implements BlockWritable, BATManaged {
    private int _big_block_count;
    private RootProperty _root;
    private BlockAllocationTableWriter _sbat;
    private List _small_blocks = new ArrayList();

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.apache.poi.poifs.filesystem.POIFSDocument>, for r8v0, types: [java.util.List, java.util.List<org.apache.poi.poifs.filesystem.POIFSDocument>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SmallBlockTableWriter(org.apache.poi.poifs.common.POIFSBigBlockSize r7, java.util.List<org.apache.poi.poifs.filesystem.POIFSDocument> r8, org.apache.poi.poifs.property.RootProperty r9) {
        /*
            r6 = this;
            r6.<init>()
            org.apache.poi.poifs.storage.BlockAllocationTableWriter r0 = new org.apache.poi.poifs.storage.BlockAllocationTableWriter
            r0.<init>(r7)
            r6._sbat = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r6._small_blocks = r0
            r6._root = r9
            java.util.Iterator r0 = r8.iterator()
        L_0x0017:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0047
            java.lang.Object r1 = r0.next()
            org.apache.poi.poifs.filesystem.POIFSDocument r1 = (org.apache.poi.poifs.filesystem.POIFSDocument) r1
            org.apache.poi.poifs.storage.BlockWritable[] r2 = r1.getSmallBlocks()
            int r3 = r2.length
            if (r3 == 0) goto L_0x0042
            org.apache.poi.poifs.storage.BlockAllocationTableWriter r3 = r6._sbat
            int r4 = r2.length
            int r3 = r3.allocateSpace(r4)
            r1.setStartBlock(r3)
            r3 = 0
        L_0x0035:
            int r4 = r2.length
            if (r3 >= r4) goto L_0x0046
            java.util.List r4 = r6._small_blocks
            r5 = r2[r3]
            r4.add(r5)
            int r3 = r3 + 1
            goto L_0x0035
        L_0x0042:
            r3 = -2
            r1.setStartBlock(r3)
        L_0x0046:
            goto L_0x0017
        L_0x0047:
            org.apache.poi.poifs.storage.BlockAllocationTableWriter r1 = r6._sbat
            r1.simpleCreateBlocks()
            org.apache.poi.poifs.property.RootProperty r1 = r6._root
            java.util.List r2 = r6._small_blocks
            int r2 = r2.size()
            r1.setSize(r2)
            java.util.List r1 = r6._small_blocks
            int r1 = org.apache.poi.poifs.storage.SmallDocumentBlock.fill(r7, r1)
            r6._big_block_count = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.poifs.storage.SmallBlockTableWriter.<init>(org.apache.poi.poifs.common.POIFSBigBlockSize, java.util.List, org.apache.poi.poifs.property.RootProperty):void");
    }

    public int getSBATBlockCount() {
        return (this._big_block_count + 15) / 16;
    }

    public BlockAllocationTableWriter getSBAT() {
        return this._sbat;
    }

    public int countBlocks() {
        return this._big_block_count;
    }

    public void setStartBlock(int start_block) {
        this._root.setStartBlock(start_block);
    }

    public void writeBlocks(OutputStream stream) throws IOException {
        for (BlockWritable writeBlocks : this._small_blocks) {
            writeBlocks.writeBlocks(stream);
        }
    }
}
