package org.apache.commons.math3.geometry.spherical.twod;

import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.spherical.oned.Arc;
import org.apache.commons.math3.util.MathUtils;

public class Edge {
    private final Circle circle;
    private Vertex end;
    private final double length;
    private final Vertex start;

    Edge(Vertex start2, Vertex end2, double length2, Circle circle2) {
        this.start = start2;
        this.end = end2;
        this.length = length2;
        this.circle = circle2;
        start2.setOutgoing(this);
        end2.setIncoming(this);
    }

    public Vertex getStart() {
        return this.start;
    }

    public Vertex getEnd() {
        return this.end;
    }

    public double getLength() {
        return this.length;
    }

    public Circle getCircle() {
        return this.circle;
    }

    public Vector3D getPointAt(double alpha) {
        return this.circle.getPointAt(this.circle.getPhase(this.start.getLocation().getVector()) + alpha);
    }

    /* access modifiers changed from: 0000 */
    public void setNextEdge(Edge next) {
        this.end = next.getStart();
        this.end.setIncoming(this);
        this.end.bindWith(getCircle());
    }

    /* access modifiers changed from: 0000 */
    public void split(Circle splitCircle, List<Edge> outsideList, List<Edge> insideList) {
        Vertex previousVertex;
        double edgeStart = this.circle.getPhase(this.start.getLocation().getVector());
        Circle circle2 = splitCircle;
        Arc arc = this.circle.getInsideArc(circle2);
        double arcRelativeStart = MathUtils.normalizeAngle(arc.getInf(), 3.141592653589793d + edgeStart) - edgeStart;
        double arcRelativeEnd = arcRelativeStart + arc.getSize();
        double unwrappedEnd = arcRelativeEnd - 6.283185307179586d;
        double tolerance = this.circle.getTolerance();
        Vertex previousVertex2 = this.start;
        if (unwrappedEnd >= this.length - tolerance) {
            insideList.add(this);
            return;
        }
        List<Edge> list = insideList;
        double alreadyManagedLength = 0.0d;
        if (unwrappedEnd >= 0.0d) {
            Vertex vertex = previousVertex2;
            alreadyManagedLength = unwrappedEnd;
            previousVertex = addSubEdge(previousVertex2, new Vertex(new S2Point(this.circle.getPointAt(edgeStart + unwrappedEnd))), unwrappedEnd, insideList, circle2);
        } else {
            previousVertex = previousVertex2;
        }
        if (arcRelativeStart < this.length - tolerance) {
            Vertex vertex2 = previousVertex;
            Vertex previousVertex3 = addSubEdge(vertex2, new Vertex(new S2Point(this.circle.getPointAt(edgeStart + arcRelativeStart))), arcRelativeStart - alreadyManagedLength, outsideList, circle2);
            double alreadyManagedLength2 = arcRelativeStart;
            if (arcRelativeEnd >= this.length - tolerance) {
                Vertex addSubEdge = addSubEdge(previousVertex3, this.end, this.length - alreadyManagedLength2, insideList, circle2);
                return;
            }
            Circle circle3 = circle2;
            Vertex previousVertex4 = addSubEdge(previousVertex3, new Vertex(new S2Point(this.circle.getPointAt(edgeStart + arcRelativeStart))), arcRelativeStart - alreadyManagedLength2, insideList, circle3);
            double alreadyManagedLength3 = arcRelativeStart;
            Vertex addSubEdge2 = addSubEdge(previousVertex4, this.end, this.length - alreadyManagedLength3, outsideList, circle3);
        } else if (unwrappedEnd >= 0.0d) {
            Vertex addSubEdge3 = addSubEdge(previousVertex, this.end, this.length - alreadyManagedLength, outsideList, circle2);
        } else {
            outsideList.add(this);
            Vertex vertex3 = previousVertex;
        }
    }

    private Vertex addSubEdge(Vertex subStart, Vertex subEnd, double subLength, List<Edge> list, Circle splitCircle) {
        if (subLength <= this.circle.getTolerance()) {
            return subStart;
        }
        subEnd.bindWith(splitCircle);
        Edge edge = new Edge(subStart, subEnd, subLength, this.circle);
        list.add(edge);
        return subEnd;
    }
}
