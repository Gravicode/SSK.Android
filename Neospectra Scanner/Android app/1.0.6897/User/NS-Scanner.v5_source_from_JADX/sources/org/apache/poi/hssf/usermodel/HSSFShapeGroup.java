package org.apache.poi.hssf.usermodel;

import java.util.ArrayList;
import java.util.List;

public class HSSFShapeGroup extends HSSFShape implements HSSFShapeContainer {
    List<HSSFShape> shapes = new ArrayList();

    /* renamed from: x1 */
    int f871x1 = 0;

    /* renamed from: x2 */
    int f872x2 = IEEEDouble.EXPONENT_BIAS;

    /* renamed from: y1 */
    int f873y1 = 0;

    /* renamed from: y2 */
    int f874y2 = 255;

    public HSSFShapeGroup(HSSFShape parent, HSSFAnchor anchor) {
        super(parent, anchor);
    }

    public HSSFShapeGroup createGroup(HSSFChildAnchor anchor) {
        HSSFShapeGroup group = new HSSFShapeGroup(this, anchor);
        group.anchor = anchor;
        this.shapes.add(group);
        return group;
    }

    public HSSFSimpleShape createShape(HSSFChildAnchor anchor) {
        HSSFSimpleShape shape = new HSSFSimpleShape(this, anchor);
        shape.anchor = anchor;
        this.shapes.add(shape);
        return shape;
    }

    public HSSFTextbox createTextbox(HSSFChildAnchor anchor) {
        HSSFTextbox shape = new HSSFTextbox(this, anchor);
        shape.anchor = anchor;
        this.shapes.add(shape);
        return shape;
    }

    public HSSFPolygon createPolygon(HSSFChildAnchor anchor) {
        HSSFPolygon shape = new HSSFPolygon(this, anchor);
        shape.anchor = anchor;
        this.shapes.add(shape);
        return shape;
    }

    public HSSFPicture createPicture(HSSFChildAnchor anchor, int pictureIndex) {
        HSSFPicture shape = new HSSFPicture(this, anchor);
        shape.anchor = anchor;
        shape.setPictureIndex(pictureIndex);
        this.shapes.add(shape);
        return shape;
    }

    public List<HSSFShape> getChildren() {
        return this.shapes;
    }

    public void setCoordinates(int x1, int y1, int x2, int y2) {
        this.f871x1 = x1;
        this.f873y1 = y1;
        this.f872x2 = x2;
        this.f874y2 = y2;
    }

    public int getX1() {
        return this.f871x1;
    }

    public int getY1() {
        return this.f873y1;
    }

    public int getX2() {
        return this.f872x2;
    }

    public int getY2() {
        return this.f874y2;
    }

    public int countOfAllChildren() {
        int count = this.shapes.size();
        for (HSSFShape shape : this.shapes) {
            count += shape.countOfAllChildren();
        }
        return count;
    }
}
