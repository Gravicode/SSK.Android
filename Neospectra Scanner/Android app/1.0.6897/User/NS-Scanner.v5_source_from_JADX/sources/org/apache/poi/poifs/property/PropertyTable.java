package org.apache.poi.poifs.property;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.filesystem.BATManaged;
import org.apache.poi.poifs.storage.BlockWritable;
import org.apache.poi.poifs.storage.PropertyBlock;
import org.apache.poi.poifs.storage.RawDataBlockList;

public final class PropertyTable implements BATManaged, BlockWritable {
    private POIFSBigBlockSize _bigBigBlockSize;
    private BlockWritable[] _blocks;
    private List<Property> _properties;
    private int _start_block;

    public PropertyTable(POIFSBigBlockSize bigBlockSize) {
        this._bigBigBlockSize = bigBlockSize;
        this._start_block = -2;
        this._properties = new ArrayList();
        addProperty(new RootProperty());
        this._blocks = null;
    }

    public PropertyTable(POIFSBigBlockSize bigBlockSize, int startBlock, RawDataBlockList blockList) throws IOException {
        this._bigBigBlockSize = bigBlockSize;
        this._start_block = -2;
        this._blocks = null;
        this._properties = PropertyFactory.convertToProperties(blockList.fetchBlocks(startBlock, -1));
        populatePropertyTree((DirectoryProperty) this._properties.get(0));
    }

    public void addProperty(Property property) {
        this._properties.add(property);
    }

    public void removeProperty(Property property) {
        this._properties.remove(property);
    }

    public RootProperty getRoot() {
        return (RootProperty) this._properties.get(0);
    }

    public void preWrite() {
        Property[] properties = (Property[]) this._properties.toArray(new Property[0]);
        for (int k = 0; k < properties.length; k++) {
            properties[k].setIndex(k);
        }
        this._blocks = PropertyBlock.createPropertyBlockArray(this._bigBigBlockSize, this._properties);
        for (Property preWrite : properties) {
            preWrite.preWrite();
        }
    }

    public int getStartBlock() {
        return this._start_block;
    }

    private void populatePropertyTree(DirectoryProperty root) throws IOException {
        int index = root.getChildIndex();
        if (Property.isValidIndex(index)) {
            Stack<Property> children = new Stack<>();
            children.push(this._properties.get(index));
            while (!children.empty()) {
                Property property = (Property) children.pop();
                root.addChild(property);
                if (property.isDirectory()) {
                    populatePropertyTree((DirectoryProperty) property);
                }
                int index2 = property.getPreviousChildIndex();
                if (Property.isValidIndex(index2)) {
                    children.push(this._properties.get(index2));
                }
                int index3 = property.getNextChildIndex();
                if (Property.isValidIndex(index3)) {
                    children.push(this._properties.get(index3));
                }
            }
        }
    }

    public int countBlocks() {
        if (this._blocks == null) {
            return 0;
        }
        return this._blocks.length;
    }

    public void setStartBlock(int index) {
        this._start_block = index;
    }

    public void writeBlocks(OutputStream stream) throws IOException {
        if (this._blocks != null) {
            for (BlockWritable writeBlocks : this._blocks) {
                writeBlocks.writeBlocks(stream);
            }
        }
    }
}
