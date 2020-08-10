package org.apache.poi.poifs.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.util.IntList;
import org.apache.poi.util.LittleEndian;

public final class BlockAllocationTableReader {
    private static final int MAX_BLOCK_COUNT = 65535;
    private final IntList _entries;
    private POIFSBigBlockSize bigBlockSize;

    public BlockAllocationTableReader(POIFSBigBlockSize bigBlockSize2, int block_count, int[] block_array, int xbat_count, int xbat_index, BlockList raw_block_list) throws IOException {
        int i = block_count;
        int[] iArr = block_array;
        BlockList blockList = raw_block_list;
        this(bigBlockSize2);
        if (i <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Illegal block count; minimum count is 1, got ");
            sb.append(i);
            sb.append(" instead");
            throw new IOException(sb.toString());
        } else if (i > 65535) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Block count ");
            sb2.append(i);
            sb2.append(" is too high. POI maximum is ");
            sb2.append(65535);
            sb2.append(".");
            throw new IOException(sb2.toString());
        } else {
            int limit = Math.min(i, iArr.length);
            RawDataBlock[] blocks = new RawDataBlock[i];
            int block_index = 0;
            while (block_index < limit) {
                int nextOffset = iArr[block_index];
                if (nextOffset > raw_block_list.blockCount()) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Your file contains ");
                    sb3.append(raw_block_list.blockCount());
                    sb3.append(" sectors, but the initial DIFAT array at index ");
                    sb3.append(block_index);
                    sb3.append(" referenced block # ");
                    sb3.append(nextOffset);
                    sb3.append(". This isn't allowed and ");
                    sb3.append(" your file is corrupt");
                    throw new IOException(sb3.toString());
                }
                blocks[block_index] = (RawDataBlock) blockList.remove(nextOffset);
                block_index++;
            }
            if (block_index < i) {
                if (xbat_index >= 0) {
                    int chain_index = xbat_index;
                    int max_entries_per_block = bigBlockSize2.getXBATEntriesPerBlock();
                    int chain_index_offset = bigBlockSize2.getNextXBATChainOffset();
                    int i2 = limit;
                    int j = 0;
                    while (true) {
                        if (j >= xbat_count) {
                            break;
                        }
                        int limit2 = Math.min(i - block_index, max_entries_per_block);
                        byte[] data = blockList.remove(chain_index).getData();
                        int offset = 0;
                        int offset2 = block_index;
                        int k = 0;
                        while (k < limit2) {
                            int block_index2 = offset2 + 1;
                            blocks[offset2] = (RawDataBlock) blockList.remove(LittleEndian.getInt(data, offset));
                            offset += 4;
                            k++;
                            offset2 = block_index2;
                        }
                        chain_index = LittleEndian.getInt(data, chain_index_offset);
                        if (chain_index == -2) {
                            block_index = offset2;
                            break;
                        } else {
                            j++;
                            block_index = offset2;
                        }
                    }
                } else {
                    throw new IOException("BAT count exceeds limit, yet XBAT index indicates no valid entries");
                }
            } else {
                int i3 = xbat_count;
                int i4 = limit;
            }
            if (block_index != i) {
                throw new IOException("Could not find all blocks");
            }
            setEntries(blocks, blockList);
        }
    }

    BlockAllocationTableReader(POIFSBigBlockSize bigBlockSize2, ListManagedBlock[] blocks, BlockList raw_block_list) throws IOException {
        this(bigBlockSize2);
        setEntries(blocks, raw_block_list);
    }

    BlockAllocationTableReader(POIFSBigBlockSize bigBlockSize2) {
        this.bigBlockSize = bigBlockSize2;
        this._entries = new IntList();
    }

    /* access modifiers changed from: 0000 */
    public ListManagedBlock[] fetchBlocks(int startBlock, int headerPropertiesStartBlock, BlockList blockList) throws IOException {
        List<ListManagedBlock> blocks = new ArrayList<>();
        int currentBlock = startBlock;
        boolean firstPass = true;
        while (currentBlock != -2) {
            try {
                blocks.add(blockList.remove(currentBlock));
                currentBlock = this._entries.get(currentBlock);
                firstPass = false;
            } catch (IOException e) {
                if (currentBlock == headerPropertiesStartBlock) {
                    System.err.println("Warning, header block comes after data blocks in POIFS block listing");
                    currentBlock = -2;
                } else if (currentBlock != 0 || !firstPass) {
                    throw e;
                } else {
                    System.err.println("Warning, incorrectly terminated empty data blocks in POIFS block listing (should end at -2, ended at 0)");
                    currentBlock = -2;
                }
            }
        }
        return (ListManagedBlock[]) blocks.toArray(new ListManagedBlock[blocks.size()]);
    }

    /* access modifiers changed from: 0000 */
    public boolean isUsed(int index) {
        boolean z = false;
        try {
            if (this._entries.get(index) != -1) {
                z = true;
            }
            return z;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    public int getNextBlockIndex(int index) throws IOException {
        if (isUsed(index)) {
            return this._entries.get(index);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("index ");
        sb.append(index);
        sb.append(" is unused");
        throw new IOException(sb.toString());
    }

    private void setEntries(ListManagedBlock[] blocks, BlockList raw_blocks) throws IOException {
        int limit = this.bigBlockSize.getBATEntriesPerBlock();
        for (int block_index = 0; block_index < blocks.length; block_index++) {
            byte[] data = blocks[block_index].getData();
            int offset = 0;
            for (int k = 0; k < limit; k++) {
                int entry = LittleEndian.getInt(data, offset);
                if (entry == -1) {
                    raw_blocks.zap(this._entries.size());
                }
                this._entries.add(entry);
                offset += 4;
            }
            blocks[block_index] = null;
        }
        raw_blocks.setBAT(this);
    }
}
