package org.apache.poi.poifs.storage;

import java.io.IOException;

abstract class BlockListImpl implements BlockList {
    private BlockAllocationTableReader _bat = null;
    private ListManagedBlock[] _blocks = new ListManagedBlock[0];

    protected BlockListImpl() {
    }

    /* access modifiers changed from: protected */
    public void setBlocks(ListManagedBlock[] blocks) {
        this._blocks = blocks;
    }

    public void zap(int index) {
        if (index >= 0 && index < this._blocks.length) {
            this._blocks[index] = null;
        }
    }

    /* access modifiers changed from: protected */
    public ListManagedBlock get(int index) {
        return this._blocks[index];
    }

    public ListManagedBlock remove(int index) throws IOException {
        try {
            ListManagedBlock result = this._blocks[index];
            if (result == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("block[ ");
                sb.append(index);
                sb.append(" ] already removed - ");
                sb.append("does your POIFS have circular or duplicate block references?");
                throw new IOException(sb.toString());
            }
            this._blocks[index] = null;
            return result;
        } catch (ArrayIndexOutOfBoundsException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Cannot remove block[ ");
            sb2.append(index);
            sb2.append(" ]; out of range[ 0 - ");
            sb2.append(this._blocks.length - 1);
            sb2.append(" ]");
            throw new IOException(sb2.toString());
        }
    }

    public ListManagedBlock[] fetchBlocks(int startBlock, int headerPropertiesStartBlock) throws IOException {
        if (this._bat != null) {
            return this._bat.fetchBlocks(startBlock, headerPropertiesStartBlock, this);
        }
        throw new IOException("Improperly initialized list: no block allocation table provided");
    }

    public void setBAT(BlockAllocationTableReader bat) throws IOException {
        if (this._bat != null) {
            throw new IOException("Attempt to replace existing BlockAllocationTable");
        }
        this._bat = bat;
    }

    public int blockCount() {
        return this._blocks.length;
    }

    /* access modifiers changed from: protected */
    public int remainingBlocks() {
        int c = 0;
        for (ListManagedBlock listManagedBlock : this._blocks) {
            if (listManagedBlock != null) {
                c++;
            }
        }
        return c;
    }
}
