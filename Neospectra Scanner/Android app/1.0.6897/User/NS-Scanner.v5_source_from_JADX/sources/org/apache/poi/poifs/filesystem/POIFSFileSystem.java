package org.apache.poi.poifs.filesystem;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.poi.poifs.dev.POIFSViewable;
import org.apache.poi.poifs.property.DirectoryProperty;
import org.apache.poi.poifs.property.Property;
import org.apache.poi.poifs.property.PropertyTable;
import org.apache.poi.poifs.storage.BATBlock;
import org.apache.poi.poifs.storage.BlockAllocationTableReader;
import org.apache.poi.poifs.storage.BlockAllocationTableWriter;
import org.apache.poi.poifs.storage.BlockList;
import org.apache.poi.poifs.storage.BlockWritable;
import org.apache.poi.poifs.storage.HeaderBlockConstants;
import org.apache.poi.poifs.storage.HeaderBlockReader;
import org.apache.poi.poifs.storage.HeaderBlockWriter;
import org.apache.poi.poifs.storage.RawDataBlockList;
import org.apache.poi.poifs.storage.SmallBlockTableReader;
import org.apache.poi.poifs.storage.SmallBlockTableWriter;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.LongField;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class POIFSFileSystem implements POIFSViewable {
    private static final POILogger _logger = POILogFactory.getLogger(POIFSFileSystem.class);
    private List _documents;
    private PropertyTable _property_table;
    private DirectoryNode _root;
    private POIFSBigBlockSize bigBlockSize;

    private static final class CloseIgnoringInputStream extends InputStream {
        private final InputStream _is;

        public CloseIgnoringInputStream(InputStream is) {
            this._is = is;
        }

        public int read() throws IOException {
            return this._is.read();
        }

        public int read(byte[] b, int off, int len) throws IOException {
            return this._is.read(b, off, len);
        }

        public void close() {
        }
    }

    public static InputStream createNonClosingInputStream(InputStream is) {
        return new CloseIgnoringInputStream(is);
    }

    public POIFSFileSystem() {
        this.bigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
        this._property_table = new PropertyTable(this.bigBlockSize);
        this._documents = new ArrayList();
        this._root = null;
    }

    /* JADX INFO: finally extract failed */
    public POIFSFileSystem(InputStream stream) throws IOException {
        this();
        try {
            HeaderBlockReader header_block_reader = new HeaderBlockReader(stream);
            this.bigBlockSize = header_block_reader.getBigBlockSize();
            RawDataBlockList data_blocks = new RawDataBlockList(stream, this.bigBlockSize);
            closeInputStream(stream, true);
            new BlockAllocationTableReader(header_block_reader.getBigBlockSize(), header_block_reader.getBATCount(), header_block_reader.getBATArray(), header_block_reader.getXBATCount(), header_block_reader.getXBATIndex(), data_blocks);
            PropertyTable properties = new PropertyTable(this.bigBlockSize, header_block_reader.getPropertyStart(), data_blocks);
            processProperties(SmallBlockTableReader.getSmallDocumentBlocks(this.bigBlockSize, data_blocks, properties.getRoot(), header_block_reader.getSBATStart()), data_blocks, properties.getRoot().getChildren(), null, header_block_reader.getPropertyStart());
            getRoot().setStorageClsid(properties.getRoot().getStorageClsid());
        } catch (Throwable th) {
            closeInputStream(stream, false);
            throw th;
        }
    }

    private void closeInputStream(InputStream stream, boolean success) {
        if (stream.markSupported() && !(stream instanceof ByteArrayInputStream)) {
            StringBuilder sb = new StringBuilder();
            sb.append("POIFS is closing the supplied input stream of type (");
            sb.append(stream.getClass().getName());
            sb.append(") which supports mark/reset.  ");
            sb.append("This will be a problem for the caller if the stream will still be used.  ");
            sb.append("If that is the case the caller should wrap the input stream to avoid this close logic.  ");
            sb.append("This warning is only temporary and will not be present in future versions of POI.");
            _logger.log(POILogger.WARN, (Object) sb.toString());
        }
        try {
            stream.close();
        } catch (IOException e) {
            if (success) {
                throw new RuntimeException(e);
            }
            e.printStackTrace();
        }
    }

    public static boolean hasPOIFSHeader(InputStream inp) throws IOException {
        inp.mark(8);
        byte[] header = new byte[8];
        IOUtils.readFully(inp, header);
        LongField signature = new LongField(0, header);
        if (inp instanceof PushbackInputStream) {
            ((PushbackInputStream) inp).unread(header);
        } else {
            inp.reset();
        }
        if (signature.get() == HeaderBlockConstants._signature) {
            return true;
        }
        return false;
    }

    public DocumentEntry createDocument(InputStream stream, String name) throws IOException {
        return getRoot().createDocument(name, stream);
    }

    public DocumentEntry createDocument(String name, int size, POIFSWriterListener writer) throws IOException {
        return getRoot().createDocument(name, size, writer);
    }

    public DirectoryEntry createDirectory(String name) throws IOException {
        return getRoot().createDirectory(name);
    }

    public void writeFilesystem(OutputStream stream) throws IOException {
        this._property_table.preWrite();
        SmallBlockTableWriter sbtw = new SmallBlockTableWriter(this.bigBlockSize, this._documents, this._property_table.getRoot());
        BlockAllocationTableWriter bat = new BlockAllocationTableWriter(this.bigBlockSize);
        List<BATManaged> bm_objects = new ArrayList<>();
        bm_objects.addAll(this._documents);
        bm_objects.add(this._property_table);
        bm_objects.add(sbtw);
        bm_objects.add(sbtw.getSBAT());
        for (BATManaged bmo : bm_objects) {
            int block_count = bmo.countBlocks();
            if (block_count != 0) {
                bmo.setStartBlock(bat.allocateSpace(block_count));
            }
        }
        int batStartBlock = bat.createBlocks();
        HeaderBlockWriter header_block_writer = new HeaderBlockWriter(this.bigBlockSize);
        BATBlock[] xbat_blocks = header_block_writer.setBATBlocks(bat.countBlocks(), batStartBlock);
        header_block_writer.setPropertyStart(this._property_table.getStartBlock());
        header_block_writer.setSBATStart(sbtw.getSBAT().getStartBlock());
        header_block_writer.setSBATBlockCount(sbtw.getSBATBlockCount());
        List<BlockWritable> writers = new ArrayList<>();
        writers.add(header_block_writer);
        writers.addAll(this._documents);
        writers.add(this._property_table);
        writers.add(sbtw);
        writers.add(sbtw.getSBAT());
        writers.add(bat);
        for (BATBlock add : xbat_blocks) {
            writers.add(add);
        }
        for (BlockWritable writer : writers) {
            writer.writeBlocks(stream);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("two arguments required: input filename and output filename");
            System.exit(1);
        }
        FileInputStream istream = new FileInputStream(args[0]);
        FileOutputStream ostream = new FileOutputStream(args[1]);
        new POIFSFileSystem(istream).writeFilesystem(ostream);
        istream.close();
        ostream.close();
    }

    public DirectoryNode getRoot() {
        if (this._root == null) {
            this._root = new DirectoryNode(this._property_table.getRoot(), this, null);
        }
        return this._root;
    }

    public DocumentInputStream createDocumentInputStream(String documentName) throws IOException {
        return getRoot().createDocumentInputStream(documentName);
    }

    /* access modifiers changed from: 0000 */
    public void addDocument(POIFSDocument document) {
        this._documents.add(document);
        this._property_table.addProperty(document.getDocumentProperty());
    }

    /* access modifiers changed from: 0000 */
    public void addDirectory(DirectoryProperty directory) {
        this._property_table.addProperty(directory);
    }

    /* access modifiers changed from: 0000 */
    public void remove(EntryNode entry) {
        this._property_table.removeProperty(entry.getProperty());
        if (entry.isDocumentEntry()) {
            this._documents.remove(((DocumentNode) entry).getDocument());
        }
    }

    private void processProperties(BlockList small_blocks, BlockList big_blocks, Iterator properties, DirectoryNode dir, int headerPropertiesStartAt) throws IOException {
        POIFSDocument pOIFSDocument;
        while (properties.hasNext()) {
            Property property = (Property) properties.next();
            String name = property.getName();
            DirectoryNode parent = dir == null ? getRoot() : dir;
            if (property.isDirectory()) {
                DirectoryNode new_dir = (DirectoryNode) parent.createDirectory(name);
                new_dir.setStorageClsid(property.getStorageClsid());
                processProperties(small_blocks, big_blocks, ((DirectoryProperty) property).getChildren(), new_dir, headerPropertiesStartAt);
            } else {
                int startBlock = property.getStartBlock();
                int size = property.getSize();
                if (property.shouldUseSmallBlocks()) {
                    pOIFSDocument = new POIFSDocument(name, small_blocks.fetchBlocks(startBlock, headerPropertiesStartAt), size);
                } else {
                    pOIFSDocument = new POIFSDocument(name, big_blocks.fetchBlocks(startBlock, headerPropertiesStartAt), size);
                }
                parent.createDocument(pOIFSDocument);
            }
        }
    }

    public Object[] getViewableArray() {
        if (preferArray()) {
            return getRoot().getViewableArray();
        }
        return new Object[0];
    }

    public Iterator getViewableIterator() {
        if (!preferArray()) {
            return getRoot().getViewableIterator();
        }
        return Collections.EMPTY_LIST.iterator();
    }

    public boolean preferArray() {
        return getRoot().preferArray();
    }

    public String getShortDescription() {
        return "POIFS FileSystem";
    }

    public int getBigBlockSize() {
        return this.bigBlockSize.getBigBlockSize();
    }

    public POIFSBigBlockSize getBigBlockSizeDetails() {
        return this.bigBlockSize;
    }
}
