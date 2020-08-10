package org.apache.poi.poifs.dev;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.property.PropertyTable;
import org.apache.poi.poifs.storage.BlockAllocationTableReader;
import org.apache.poi.poifs.storage.BlockList;
import org.apache.poi.poifs.storage.HeaderBlockReader;
import org.apache.poi.poifs.storage.ListManagedBlock;
import org.apache.poi.poifs.storage.RawDataBlockList;
import org.apache.poi.poifs.storage.SmallBlockTableReader;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.IntList;

public class POIFSHeaderDumper {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Must specify at least one file to view");
            System.exit(1);
        }
        for (String viewFile : args) {
            viewFile(viewFile);
        }
    }

    public static void viewFile(String filename) throws Exception {
        InputStream inp = new FileInputStream(filename);
        HeaderBlockReader header_block_reader = new HeaderBlockReader(inp);
        displayHeader(header_block_reader);
        POIFSBigBlockSize bigBlockSize = header_block_reader.getBigBlockSize();
        RawDataBlockList data_blocks = new RawDataBlockList(inp, bigBlockSize);
        displayRawBlocksSummary(data_blocks);
        BlockAllocationTableReader batReader = new BlockAllocationTableReader(header_block_reader.getBigBlockSize(), header_block_reader.getBATCount(), header_block_reader.getBATArray(), header_block_reader.getXBATCount(), header_block_reader.getXBATIndex(), data_blocks);
        displayBATReader(batReader);
        BlockList smallDocumentBlocks = SmallBlockTableReader.getSmallDocumentBlocks(bigBlockSize, data_blocks, new PropertyTable(header_block_reader.getBigBlockSize(), header_block_reader.getPropertyStart(), data_blocks).getRoot(), header_block_reader.getSBATStart());
    }

    public static void displayHeader(HeaderBlockReader header_block_reader) throws Exception {
        System.out.println("Header Details:");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append(" Block size: ");
        sb.append(header_block_reader.getBigBlockSize());
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" BAT (FAT) header blocks: ");
        sb2.append(header_block_reader.getBATArray().length);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(" BAT (FAT) block count: ");
        sb3.append(header_block_reader.getBATCount());
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(" XBAT (FAT) block count: ");
        sb4.append(header_block_reader.getXBATCount());
        printStream4.println(sb4.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(" XBAT (FAT) block 1 at: ");
        sb5.append(header_block_reader.getXBATIndex());
        printStream5.println(sb5.toString());
        PrintStream printStream6 = System.out;
        StringBuilder sb6 = new StringBuilder();
        sb6.append(" SBAT (MiniFAT) block count: ");
        sb6.append(header_block_reader.getSBATCount());
        printStream6.println(sb6.toString());
        PrintStream printStream7 = System.out;
        StringBuilder sb7 = new StringBuilder();
        sb7.append(" SBAT (MiniFAT) block 1 at: ");
        sb7.append(header_block_reader.getSBATStart());
        printStream7.println(sb7.toString());
        PrintStream printStream8 = System.out;
        StringBuilder sb8 = new StringBuilder();
        sb8.append(" Property table at: ");
        sb8.append(header_block_reader.getPropertyStart());
        printStream8.println(sb8.toString());
        System.out.println("");
    }

    public static void displayRawBlocksSummary(RawDataBlockList data_blocks) throws Exception {
        System.out.println("Raw Blocks Details:");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append(" Number of blocks: ");
        sb.append(data_blocks.blockCount());
        printStream.println(sb.toString());
        Method gbm = data_blocks.getClass().getSuperclass().getDeclaredMethod("get", new Class[]{Integer.TYPE});
        gbm.setAccessible(true);
        for (int i = 0; i < Math.min(16, data_blocks.blockCount()); i++) {
            ListManagedBlock block = (ListManagedBlock) gbm.invoke(data_blocks, new Object[]{new Integer(i)});
            byte[] data = new byte[Math.min(48, block.getData().length)];
            System.arraycopy(block.getData(), 0, data, 0, data.length);
            PrintStream printStream2 = System.out;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" Block #");
            sb2.append(i);
            sb2.append(":");
            printStream2.println(sb2.toString());
            System.out.println(HexDump.dump(data, 0, 0));
        }
        System.out.println("");
    }

    public static void displayBATReader(BlockAllocationTableReader batReader) throws Exception {
        System.out.println("Sectors, as referenced from the FAT:");
        Field entriesF = batReader.getClass().getDeclaredField("_entries");
        entriesF.setAccessible(true);
        IntList entries = (IntList) entriesF.get(batReader);
        for (int i = 0; i < entries.size(); i++) {
            int bn = entries.get(i);
            String bnS = Integer.toString(bn);
            if (bn == -2) {
                bnS = "End Of Chain";
            } else if (bn == -4) {
                bnS = "DI Fat Block";
            } else if (bn == -3) {
                bnS = "Normal Fat Block";
            } else if (bn == -1) {
                bnS = "Block Not Used (Free)";
            }
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("  Block  # ");
            sb.append(i);
            sb.append(" -> ");
            sb.append(bnS);
            printStream.println(sb.toString());
        }
        System.out.println("");
    }
}
