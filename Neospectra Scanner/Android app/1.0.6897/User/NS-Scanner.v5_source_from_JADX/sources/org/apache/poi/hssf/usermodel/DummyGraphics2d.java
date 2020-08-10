package org.apache.poi.hssf.usermodel;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.PrintStream;
import java.text.AttributedCharacterIterator;
import java.util.Map;

public class DummyGraphics2d extends Graphics2D {
    private Graphics2D g2D = this.img.getGraphics();
    BufferedImage img = new BufferedImage(1000, 1000, 2);

    public void addRenderingHints(Map hints) {
        System.out.println("addRenderingHinds(Map):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("  hints = ");
        sb.append(hints);
        printStream.println(sb.toString());
        this.g2D.addRenderingHints(hints);
    }

    public void clip(Shape s) {
        System.out.println("clip(Shape):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("  s = ");
        sb.append(s);
        printStream.println(sb.toString());
        this.g2D.clip(s);
    }

    public void draw(Shape s) {
        System.out.println("draw(Shape):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("s = ");
        sb.append(s);
        printStream.println(sb.toString());
        this.g2D.draw(s);
    }

    public void drawGlyphVector(GlyphVector g, float x, float y) {
        System.out.println("drawGlyphVector(GlyphVector, float, float):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("g = ");
        sb.append(g);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("x = ");
        sb2.append(x);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("y = ");
        sb3.append(y);
        printStream3.println(sb3.toString());
        this.g2D.drawGlyphVector(g, x, y);
    }

    public void drawImage(BufferedImage img2, BufferedImageOp op, int x, int y) {
        System.out.println("drawImage(BufferedImage, BufferedImageOp, x, y):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("img = ");
        sb.append(img2);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("op = ");
        sb2.append(op);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("x = ");
        sb3.append(x);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("y = ");
        sb4.append(y);
        printStream4.println(sb4.toString());
        this.g2D.drawImage(img2, op, x, y);
    }

    public boolean drawImage(Image img2, AffineTransform xform, ImageObserver obs) {
        System.out.println("drawImage(Image,AfflineTransform,ImageObserver):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("img = ");
        sb.append(img2);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("xform = ");
        sb2.append(xform);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("obs = ");
        sb3.append(obs);
        printStream3.println(sb3.toString());
        return this.g2D.drawImage(img2, xform, obs);
    }

    public void drawRenderableImage(RenderableImage img2, AffineTransform xform) {
        System.out.println("drawRenderableImage(RenderableImage, AfflineTransform):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("img = ");
        sb.append(img2);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("xform = ");
        sb2.append(xform);
        printStream2.println(sb2.toString());
        this.g2D.drawRenderableImage(img2, xform);
    }

    public void drawRenderedImage(RenderedImage img2, AffineTransform xform) {
        System.out.println("drawRenderedImage(RenderedImage, AffineTransform):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("img = ");
        sb.append(img2);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("xform = ");
        sb2.append(xform);
        printStream2.println(sb2.toString());
        this.g2D.drawRenderedImage(img2, xform);
    }

    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
        System.out.println("drawString(AttributedCharacterIterator):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("iterator = ");
        sb.append(iterator);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("x = ");
        sb2.append(x);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("y = ");
        sb3.append(y);
        printStream3.println(sb3.toString());
        this.g2D.drawString(iterator, x, y);
    }

    public void drawString(String s, float x, float y) {
        System.out.println("drawString(s,x,y):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("s = ");
        sb.append(s);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("x = ");
        sb2.append(x);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("y = ");
        sb3.append(y);
        printStream3.println(sb3.toString());
        this.g2D.drawString(s, x, y);
    }

    public void fill(Shape s) {
        System.out.println("fill(Shape):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("s = ");
        sb.append(s);
        printStream.println(sb.toString());
        this.g2D.fill(s);
    }

    public Color getBackground() {
        System.out.println("getBackground():");
        return this.g2D.getBackground();
    }

    public Composite getComposite() {
        System.out.println("getComposite():");
        return this.g2D.getComposite();
    }

    public GraphicsConfiguration getDeviceConfiguration() {
        System.out.println("getDeviceConfiguration():");
        return this.g2D.getDeviceConfiguration();
    }

    public FontRenderContext getFontRenderContext() {
        System.out.println("getFontRenderContext():");
        return this.g2D.getFontRenderContext();
    }

    public Paint getPaint() {
        System.out.println("getPaint():");
        return this.g2D.getPaint();
    }

    public Object getRenderingHint(Key hintKey) {
        System.out.println("getRenderingHint(RenderingHints.Key):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("hintKey = ");
        sb.append(hintKey);
        printStream.println(sb.toString());
        return this.g2D.getRenderingHint(hintKey);
    }

    public RenderingHints getRenderingHints() {
        System.out.println("getRenderingHints():");
        return this.g2D.getRenderingHints();
    }

    public Stroke getStroke() {
        System.out.println("getStroke():");
        return this.g2D.getStroke();
    }

    public AffineTransform getTransform() {
        System.out.println("getTransform():");
        return this.g2D.getTransform();
    }

    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        System.out.println("hit(Rectangle, Shape, onStroke):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("rect = ");
        sb.append(rect);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("s = ");
        sb2.append(s);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("onStroke = ");
        sb3.append(onStroke);
        printStream3.println(sb3.toString());
        return this.g2D.hit(rect, s, onStroke);
    }

    public void rotate(double theta) {
        System.out.println("rotate(theta):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("theta = ");
        sb.append(theta);
        printStream.println(sb.toString());
        this.g2D.rotate(theta);
    }

    public void rotate(double theta, double x, double y) {
        System.out.println("rotate(double,double,double):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("theta = ");
        sb.append(theta);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("x = ");
        sb2.append(x);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("y = ");
        sb3.append(y);
        printStream3.println(sb3.toString());
        this.g2D.rotate(theta, x, y);
    }

    public void scale(double sx, double sy) {
        System.out.println("scale(double,double):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("sx = ");
        sb.append(sx);
        printStream.println(sb.toString());
        System.out.println("sy");
        this.g2D.scale(sx, sy);
    }

    public void setBackground(Color color) {
        System.out.println("setBackground(Color):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("color = ");
        sb.append(color);
        printStream.println(sb.toString());
        this.g2D.setBackground(color);
    }

    public void setComposite(Composite comp) {
        System.out.println("setComposite(Composite):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("comp = ");
        sb.append(comp);
        printStream.println(sb.toString());
        this.g2D.setComposite(comp);
    }

    public void setPaint(Paint paint) {
        System.out.println("setPain(Paint):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("paint = ");
        sb.append(paint);
        printStream.println(sb.toString());
        this.g2D.setPaint(paint);
    }

    public void setRenderingHint(Key hintKey, Object hintValue) {
        System.out.println("setRenderingHint(RenderingHints.Key, Object):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("hintKey = ");
        sb.append(hintKey);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("hintValue = ");
        sb2.append(hintValue);
        printStream2.println(sb2.toString());
        this.g2D.setRenderingHint(hintKey, hintValue);
    }

    public void setRenderingHints(Map hints) {
        System.out.println("setRenderingHints(Map):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("hints = ");
        sb.append(hints);
        printStream.println(sb.toString());
        this.g2D.setRenderingHints(hints);
    }

    public void setStroke(Stroke s) {
        System.out.println("setStroke(Stoke):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("s = ");
        sb.append(s);
        printStream.println(sb.toString());
        this.g2D.setStroke(s);
    }

    public void setTransform(AffineTransform Tx) {
        System.out.println("setTransform():");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("Tx = ");
        sb.append(Tx);
        printStream.println(sb.toString());
        this.g2D.setTransform(Tx);
    }

    public void shear(double shx, double shy) {
        System.out.println("shear(shx, dhy):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("shx = ");
        sb.append(shx);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("shy = ");
        sb2.append(shy);
        printStream2.println(sb2.toString());
        this.g2D.shear(shx, shy);
    }

    public void transform(AffineTransform Tx) {
        System.out.println("transform(AffineTransform):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("Tx = ");
        sb.append(Tx);
        printStream.println(sb.toString());
        this.g2D.transform(Tx);
    }

    public void translate(double tx, double ty) {
        System.out.println("translate(double, double):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("tx = ");
        sb.append(tx);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("ty = ");
        sb2.append(ty);
        printStream2.println(sb2.toString());
        this.g2D.translate(tx, ty);
    }

    public void clearRect(int x, int y, int width, int height) {
        System.out.println("clearRect(int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        this.g2D.clearRect(x, y, width, height);
    }

    public void clipRect(int x, int y, int width, int height) {
        System.out.println("clipRect(int, int, int, int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        this.g2D.clipRect(x, y, width, height);
    }

    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        System.out.println("copyArea(int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        this.g2D.copyArea(x, y, width, height, dx, dy);
    }

    public Graphics create() {
        System.out.println("create():");
        return this.g2D.create();
    }

    public Graphics create(int x, int y, int width, int height) {
        System.out.println("create(int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        return this.g2D.create(x, y, width, height);
    }

    public void dispose() {
        System.out.println("dispose():");
        this.g2D.dispose();
    }

    public void draw3DRect(int x, int y, int width, int height, boolean raised) {
        System.out.println("draw3DRect(int,int,int,int,boolean):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("raised = ");
        sb5.append(raised);
        printStream5.println(sb5.toString());
        this.g2D.draw3DRect(x, y, width, height, raised);
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        System.out.println("drawArc(int,int,int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("startAngle = ");
        sb5.append(startAngle);
        printStream5.println(sb5.toString());
        PrintStream printStream6 = System.out;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("arcAngle = ");
        sb6.append(arcAngle);
        printStream6.println(sb6.toString());
        this.g2D.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    public void drawBytes(byte[] data, int offset, int length, int x, int y) {
        System.out.println("drawBytes(byte[],int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("data = ");
        sb.append(data);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("offset = ");
        sb2.append(offset);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("length = ");
        sb3.append(length);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("x = ");
        sb4.append(x);
        printStream4.println(sb4.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("y = ");
        sb5.append(y);
        printStream5.println(sb5.toString());
        this.g2D.drawBytes(data, offset, length, x, y);
    }

    public void drawChars(char[] data, int offset, int length, int x, int y) {
        System.out.println("drawChars(data,int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("data = ");
        sb.append(data.toString());
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("offset = ");
        sb2.append(offset);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("length = ");
        sb3.append(length);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("x = ");
        sb4.append(x);
        printStream4.println(sb4.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("y = ");
        sb5.append(y);
        printStream5.println(sb5.toString());
        this.g2D.drawChars(data, offset, length, x, y);
    }

    public boolean drawImage(Image img2, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        System.out.println("drawImage(Image,int,int,int,int,int,int,int,int,ImageObserver):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("img = ");
        Image image = img2;
        sb.append(image);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("dx1 = ");
        int i = dx1;
        sb2.append(i);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("dy1 = ");
        int i2 = dy1;
        sb3.append(i2);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("dx2 = ");
        int i3 = dx2;
        sb4.append(i3);
        printStream4.println(sb4.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("dy2 = ");
        int i4 = dy2;
        sb5.append(i4);
        printStream5.println(sb5.toString());
        PrintStream printStream6 = System.out;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("sx1 = ");
        int i5 = sx1;
        sb6.append(i5);
        printStream6.println(sb6.toString());
        PrintStream printStream7 = System.out;
        StringBuilder sb7 = new StringBuilder();
        sb7.append("sy1 = ");
        int i6 = sy1;
        sb7.append(i6);
        printStream7.println(sb7.toString());
        PrintStream printStream8 = System.out;
        StringBuilder sb8 = new StringBuilder();
        sb8.append("sx2 = ");
        sb8.append(sx2);
        printStream8.println(sb8.toString());
        PrintStream printStream9 = System.out;
        StringBuilder sb9 = new StringBuilder();
        sb9.append("sy2 = ");
        sb9.append(sy2);
        printStream9.println(sb9.toString());
        PrintStream printStream10 = System.out;
        StringBuilder sb10 = new StringBuilder();
        sb10.append("observer = ");
        sb10.append(observer);
        printStream10.println(sb10.toString());
        return this.g2D.drawImage(image, i, i2, i3, i4, i5, i6, sx2, sy2, observer);
    }

    public boolean drawImage(Image img2, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        System.out.println("drawImage(Image,int,int,int,int,int,int,int,int,Color,ImageObserver):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("img = ");
        Image image = img2;
        sb.append(image);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("dx1 = ");
        int i = dx1;
        sb2.append(i);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("dy1 = ");
        int i2 = dy1;
        sb3.append(i2);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("dx2 = ");
        int i3 = dx2;
        sb4.append(i3);
        printStream4.println(sb4.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("dy2 = ");
        int i4 = dy2;
        sb5.append(i4);
        printStream5.println(sb5.toString());
        PrintStream printStream6 = System.out;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("sx1 = ");
        int i5 = sx1;
        sb6.append(i5);
        printStream6.println(sb6.toString());
        PrintStream printStream7 = System.out;
        StringBuilder sb7 = new StringBuilder();
        sb7.append("sy1 = ");
        int i6 = sy1;
        sb7.append(i6);
        printStream7.println(sb7.toString());
        PrintStream printStream8 = System.out;
        StringBuilder sb8 = new StringBuilder();
        sb8.append("sx2 = ");
        sb8.append(sx2);
        printStream8.println(sb8.toString());
        PrintStream printStream9 = System.out;
        StringBuilder sb9 = new StringBuilder();
        sb9.append("sy2 = ");
        sb9.append(sy2);
        printStream9.println(sb9.toString());
        PrintStream printStream10 = System.out;
        StringBuilder sb10 = new StringBuilder();
        sb10.append("bgcolor = ");
        sb10.append(bgcolor);
        printStream10.println(sb10.toString());
        PrintStream printStream11 = System.out;
        StringBuilder sb11 = new StringBuilder();
        sb11.append("observer = ");
        sb11.append(observer);
        printStream11.println(sb11.toString());
        return this.g2D.drawImage(image, i, i2, i3, i4, i5, i6, sx2, sy2, bgcolor, observer);
    }

    public boolean drawImage(Image img2, int x, int y, Color bgcolor, ImageObserver observer) {
        System.out.println("drawImage(Image,int,int,Color,ImageObserver):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("img = ");
        sb.append(img2);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("x = ");
        sb2.append(x);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("y = ");
        sb3.append(y);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("bgcolor = ");
        sb4.append(bgcolor);
        printStream4.println(sb4.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("observer = ");
        sb5.append(observer);
        printStream5.println(sb5.toString());
        return this.g2D.drawImage(img2, x, y, bgcolor, observer);
    }

    public boolean drawImage(Image img2, int x, int y, ImageObserver observer) {
        System.out.println("drawImage(Image,int,int,observer):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("img = ");
        sb.append(img2);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("x = ");
        sb2.append(x);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("y = ");
        sb3.append(y);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("observer = ");
        sb4.append(observer);
        printStream4.println(sb4.toString());
        return this.g2D.drawImage(img2, x, y, observer);
    }

    public boolean drawImage(Image img2, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        System.out.println("drawImage(Image,int,int,int,int,Color,ImageObserver):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("img = ");
        Image image = img2;
        sb.append(image);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("x = ");
        int i = x;
        sb2.append(i);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("y = ");
        int i2 = y;
        sb3.append(i2);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("width = ");
        int i3 = width;
        sb4.append(i3);
        printStream4.println(sb4.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("height = ");
        int i4 = height;
        sb5.append(i4);
        printStream5.println(sb5.toString());
        PrintStream printStream6 = System.out;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("bgcolor = ");
        Color color = bgcolor;
        sb6.append(color);
        printStream6.println(sb6.toString());
        PrintStream printStream7 = System.out;
        StringBuilder sb7 = new StringBuilder();
        sb7.append("observer = ");
        ImageObserver imageObserver = observer;
        sb7.append(imageObserver);
        printStream7.println(sb7.toString());
        return this.g2D.drawImage(image, i, i2, i3, i4, color, imageObserver);
    }

    public boolean drawImage(Image img2, int x, int y, int width, int height, ImageObserver observer) {
        System.out.println("drawImage(Image,int,int,width,height,observer):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("img = ");
        sb.append(img2);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("x = ");
        sb2.append(x);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("y = ");
        sb3.append(y);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("width = ");
        sb4.append(width);
        printStream4.println(sb4.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("height = ");
        sb5.append(height);
        printStream5.println(sb5.toString());
        PrintStream printStream6 = System.out;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("observer = ");
        sb6.append(observer);
        printStream6.println(sb6.toString());
        return this.g2D.drawImage(img2, x, y, width, height, observer);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        System.out.println("drawLine(int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x1 = ");
        sb.append(x1);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y1 = ");
        sb2.append(y1);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("x2 = ");
        sb3.append(x2);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("y2 = ");
        sb4.append(y2);
        printStream4.println(sb4.toString());
        this.g2D.drawLine(x1, y1, x2, y2);
    }

    public void drawOval(int x, int y, int width, int height) {
        System.out.println("drawOval(int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        this.g2D.drawOval(x, y, width, height);
    }

    public void drawPolygon(Polygon p) {
        System.out.println("drawPolygon(Polygon):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("p = ");
        sb.append(p);
        printStream.println(sb.toString());
        this.g2D.drawPolygon(p);
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        System.out.println("drawPolygon(int[],int[],int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("xPoints = ");
        sb.append(xPoints);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("yPoints = ");
        sb2.append(yPoints);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("nPoints = ");
        sb3.append(nPoints);
        printStream3.println(sb3.toString());
        this.g2D.drawPolygon(xPoints, yPoints, nPoints);
    }

    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        System.out.println("drawPolyline(int[],int[],int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("xPoints = ");
        sb.append(xPoints);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("yPoints = ");
        sb2.append(yPoints);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("nPoints = ");
        sb3.append(nPoints);
        printStream3.println(sb3.toString());
        this.g2D.drawPolyline(xPoints, yPoints, nPoints);
    }

    public void drawRect(int x, int y, int width, int height) {
        System.out.println("drawRect(int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        this.g2D.drawRect(x, y, width, height);
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        System.out.println("drawRoundRect(int,int,int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("arcWidth = ");
        sb5.append(arcWidth);
        printStream5.println(sb5.toString());
        PrintStream printStream6 = System.out;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("arcHeight = ");
        sb6.append(arcHeight);
        printStream6.println(sb6.toString());
        this.g2D.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        System.out.println("drawString(AttributedCharacterIterator,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("iterator = ");
        sb.append(iterator);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("x = ");
        sb2.append(x);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("y = ");
        sb3.append(y);
        printStream3.println(sb3.toString());
        this.g2D.drawString(iterator, x, y);
    }

    public void drawString(String str, int x, int y) {
        System.out.println("drawString(str,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("str = ");
        sb.append(str);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("x = ");
        sb2.append(x);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("y = ");
        sb3.append(y);
        printStream3.println(sb3.toString());
        this.g2D.drawString(str, x, y);
    }

    public void fill3DRect(int x, int y, int width, int height, boolean raised) {
        System.out.println("fill3DRect(int,int,int,int,boolean):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("raised = ");
        sb5.append(raised);
        printStream5.println(sb5.toString());
        this.g2D.fill3DRect(x, y, width, height, raised);
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        System.out.println("fillArc(int,int,int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("startAngle = ");
        sb5.append(startAngle);
        printStream5.println(sb5.toString());
        PrintStream printStream6 = System.out;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("arcAngle = ");
        sb6.append(arcAngle);
        printStream6.println(sb6.toString());
        this.g2D.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    public void fillOval(int x, int y, int width, int height) {
        System.out.println("fillOval(int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        this.g2D.fillOval(x, y, width, height);
    }

    public void fillPolygon(Polygon p) {
        System.out.println("fillPolygon(Polygon):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("p = ");
        sb.append(p);
        printStream.println(sb.toString());
        this.g2D.fillPolygon(p);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        System.out.println("fillPolygon(int[],int[],int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("xPoints = ");
        sb.append(xPoints);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("yPoints = ");
        sb2.append(yPoints);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("nPoints = ");
        sb3.append(nPoints);
        printStream3.println(sb3.toString());
        this.g2D.fillPolygon(xPoints, yPoints, nPoints);
    }

    public void fillRect(int x, int y, int width, int height) {
        System.out.println("fillRect(int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        this.g2D.fillRect(x, y, width, height);
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        System.out.println("fillRoundRect(int,int,int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        this.g2D.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void finalize() {
        System.out.println("finalize():");
        this.g2D.finalize();
    }

    public Shape getClip() {
        System.out.println("getClip():");
        return this.g2D.getClip();
    }

    public Rectangle getClipBounds() {
        System.out.println("getClipBounds():");
        return this.g2D.getClipBounds();
    }

    public Rectangle getClipBounds(Rectangle r) {
        System.out.println("getClipBounds(Rectangle):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("r = ");
        sb.append(r);
        printStream.println(sb.toString());
        return this.g2D.getClipBounds(r);
    }

    public Rectangle getClipRect() {
        System.out.println("getClipRect():");
        return this.g2D.getClipRect();
    }

    public Color getColor() {
        System.out.println("getColor():");
        return this.g2D.getColor();
    }

    public Font getFont() {
        System.out.println("getFont():");
        return this.g2D.getFont();
    }

    public FontMetrics getFontMetrics() {
        System.out.println("getFontMetrics():");
        return this.g2D.getFontMetrics();
    }

    public FontMetrics getFontMetrics(Font f) {
        System.out.println("getFontMetrics():");
        return this.g2D.getFontMetrics(f);
    }

    public boolean hitClip(int x, int y, int width, int height) {
        System.out.println("hitClip(int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        return this.g2D.hitClip(x, y, width, height);
    }

    public void setClip(Shape clip) {
        System.out.println("setClip(Shape):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("clip = ");
        sb.append(clip);
        printStream.println(sb.toString());
        this.g2D.setClip(clip);
    }

    public void setClip(int x, int y, int width, int height) {
        System.out.println("setClip(int,int,int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("width = ");
        sb3.append(width);
        printStream3.println(sb3.toString());
        PrintStream printStream4 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("height = ");
        sb4.append(height);
        printStream4.println(sb4.toString());
        this.g2D.setClip(x, y, width, height);
    }

    public void setColor(Color c) {
        System.out.println("setColor():");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("c = ");
        sb.append(c);
        printStream.println(sb.toString());
        this.g2D.setColor(c);
    }

    public void setFont(Font font) {
        System.out.println("setFont(Font):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("font = ");
        sb.append(font);
        printStream.println(sb.toString());
        this.g2D.setFont(font);
    }

    public void setPaintMode() {
        System.out.println("setPaintMode():");
        this.g2D.setPaintMode();
    }

    public void setXORMode(Color c1) {
        System.out.println("setXORMode(Color):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("c1 = ");
        sb.append(c1);
        printStream.println(sb.toString());
        this.g2D.setXORMode(c1);
    }

    public String toString() {
        System.out.println("toString():");
        return this.g2D.toString();
    }

    public void translate(int x, int y) {
        System.out.println("translate(int,int):");
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("x = ");
        sb.append(x);
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("y = ");
        sb2.append(y);
        printStream2.println(sb2.toString());
        this.g2D.translate(x, y);
    }
}
