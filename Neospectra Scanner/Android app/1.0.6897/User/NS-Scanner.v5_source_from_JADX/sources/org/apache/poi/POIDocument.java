package org.apache.poi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.HPSFException;
import org.apache.poi.hpsf.MutablePropertySet;
import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hpsf.WritingNotSupportedException;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public abstract class POIDocument {
    private static final POILogger logger = POILogFactory.getLogger(POIDocument.class);
    protected DirectoryNode directory;
    private DocumentSummaryInformation dsInf;
    protected POIFSFileSystem filesystem;
    private boolean initialized;
    private SummaryInformation sInf;

    public abstract void write(OutputStream outputStream) throws IOException;

    protected POIDocument(DirectoryNode dir, POIFSFileSystem fs) {
        this.initialized = false;
        this.filesystem = fs;
        this.directory = dir;
    }

    protected POIDocument(POIFSFileSystem fs) {
        this(fs.getRoot(), fs);
    }

    public DocumentSummaryInformation getDocumentSummaryInformation() {
        if (!this.initialized) {
            readProperties();
        }
        return this.dsInf;
    }

    public SummaryInformation getSummaryInformation() {
        if (!this.initialized) {
            readProperties();
        }
        return this.sInf;
    }

    public void createInformationProperties() {
        if (!this.initialized) {
            readProperties();
        }
        if (this.sInf == null) {
            this.sInf = PropertySetFactory.newSummaryInformation();
        }
        if (this.dsInf == null) {
            this.dsInf = PropertySetFactory.newDocumentSummaryInformation();
        }
    }

    /* access modifiers changed from: protected */
    public void readProperties() {
        PropertySet ps = getPropertySet(DocumentSummaryInformation.DEFAULT_STREAM_NAME);
        if (ps != null && (ps instanceof DocumentSummaryInformation)) {
            this.dsInf = (DocumentSummaryInformation) ps;
        } else if (ps != null) {
            logger.log(POILogger.WARN, (Object) "DocumentSummaryInformation property set came back with wrong class - ", (Object) ps.getClass());
        }
        PropertySet ps2 = getPropertySet(SummaryInformation.DEFAULT_STREAM_NAME);
        if (ps2 instanceof SummaryInformation) {
            this.sInf = (SummaryInformation) ps2;
        } else if (ps2 != null) {
            logger.log(POILogger.WARN, (Object) "SummaryInformation property set came back with wrong class - ", (Object) ps2.getClass());
        }
        this.initialized = true;
    }

    /* access modifiers changed from: protected */
    public PropertySet getPropertySet(String setName) {
        if (this.directory == null) {
            return null;
        }
        try {
            try {
                return PropertySetFactory.create(this.directory.createDocumentInputStream(setName));
            } catch (IOException ie) {
                POILogger pOILogger = logger;
                int i = POILogger.WARN;
                StringBuilder sb = new StringBuilder();
                sb.append("Error creating property set with name ");
                sb.append(setName);
                sb.append("\n");
                sb.append(ie);
                pOILogger.log(i, (Object) sb.toString());
                return null;
            } catch (HPSFException he) {
                POILogger pOILogger2 = logger;
                int i2 = POILogger.WARN;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Error creating property set with name ");
                sb2.append(setName);
                sb2.append("\n");
                sb2.append(he);
                pOILogger2.log(i2, (Object) sb2.toString());
                return null;
            }
        } catch (IOException ie2) {
            POILogger pOILogger3 = logger;
            int i3 = POILogger.WARN;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Error getting property set with name ");
            sb3.append(setName);
            sb3.append("\n");
            sb3.append(ie2);
            pOILogger3.log(i3, (Object) sb3.toString());
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void writeProperties(POIFSFileSystem outFS) throws IOException {
        writeProperties(outFS, null);
    }

    /* access modifiers changed from: protected */
    public void writeProperties(POIFSFileSystem outFS, List writtenEntries) throws IOException {
        SummaryInformation si = getSummaryInformation();
        if (si != null) {
            writePropertySet(SummaryInformation.DEFAULT_STREAM_NAME, si, outFS);
            if (writtenEntries != null) {
                writtenEntries.add(SummaryInformation.DEFAULT_STREAM_NAME);
            }
        }
        DocumentSummaryInformation dsi = getDocumentSummaryInformation();
        if (dsi != null) {
            writePropertySet(DocumentSummaryInformation.DEFAULT_STREAM_NAME, dsi, outFS);
            if (writtenEntries != null) {
                writtenEntries.add(DocumentSummaryInformation.DEFAULT_STREAM_NAME);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void writePropertySet(String name, PropertySet set, POIFSFileSystem outFS) throws IOException {
        try {
            MutablePropertySet mSet = new MutablePropertySet(set);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            mSet.write(bOut);
            byte[] data = bOut.toByteArray();
            outFS.createDocument(new ByteArrayInputStream(data), name);
            POILogger pOILogger = logger;
            int i = POILogger.INFO;
            StringBuilder sb = new StringBuilder();
            sb.append("Wrote property set ");
            sb.append(name);
            sb.append(" of size ");
            sb.append(data.length);
            pOILogger.log(i, (Object) sb.toString());
        } catch (WritingNotSupportedException e) {
            PrintStream printStream = System.err;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Couldn't write property set with name ");
            sb2.append(name);
            sb2.append(" as not supported by HPSF yet");
            printStream.println(sb2.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void copyNodes(POIFSFileSystem source, POIFSFileSystem target, List excepts) throws IOException {
        DirectoryEntry root = source.getRoot();
        DirectoryEntry newRoot = target.getRoot();
        Iterator entries = root.getEntries();
        while (entries.hasNext()) {
            Entry entry = (Entry) entries.next();
            if (!isInList(entry.getName(), excepts)) {
                copyNodeRecursively(entry, newRoot);
            }
        }
    }

    private boolean isInList(String entry, List list) {
        for (int k = 0; k < list.size(); k++) {
            if (list.get(k).equals(entry)) {
                return true;
            }
        }
        return false;
    }

    private void copyNodeRecursively(Entry entry, DirectoryEntry target) throws IOException {
        if (entry.isDirectoryEntry()) {
            DirectoryEntry newTarget = target.createDirectory(entry.getName());
            Iterator entries = ((DirectoryEntry) entry).getEntries();
            while (entries.hasNext()) {
                copyNodeRecursively((Entry) entries.next(), newTarget);
            }
            return;
        }
        DocumentEntry dentry = (DocumentEntry) entry;
        DocumentInputStream dstream = new DocumentInputStream(dentry);
        target.createDocument(dentry.getName(), dstream);
        dstream.close();
    }
}
