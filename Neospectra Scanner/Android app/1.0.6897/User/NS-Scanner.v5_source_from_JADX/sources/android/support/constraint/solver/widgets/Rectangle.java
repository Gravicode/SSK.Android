package android.support.constraint.solver.widgets;

public class Rectangle {
    public int height;
    public int width;

    /* renamed from: x */
    public int f31x;

    /* renamed from: y */
    public int f32y;

    public void setBounds(int x, int y, int width2, int height2) {
        this.f31x = x;
        this.f32y = y;
        this.width = width2;
        this.height = height2;
    }

    /* access modifiers changed from: 0000 */
    public void grow(int w, int h) {
        this.f31x -= w;
        this.f32y -= h;
        this.width += w * 2;
        this.height += h * 2;
    }

    /* access modifiers changed from: 0000 */
    public boolean intersects(Rectangle bounds) {
        return this.f31x >= bounds.f31x && this.f31x < bounds.f31x + bounds.width && this.f32y >= bounds.f32y && this.f32y < bounds.f32y + bounds.height;
    }

    public boolean contains(int x, int y) {
        return x >= this.f31x && x < this.f31x + this.width && y >= this.f32y && y < this.f32y + this.height;
    }

    public int getCenterX() {
        return (this.f31x + this.width) / 2;
    }

    public int getCenterY() {
        return (this.f32y + this.height) / 2;
    }
}
