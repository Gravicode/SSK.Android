package org.apache.commons.math3.geometry.spherical.twod;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor.Order;
import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
import org.apache.commons.math3.geometry.spherical.oned.Arc;
import org.apache.commons.math3.geometry.spherical.oned.ArcsSet;
import org.apache.commons.math3.geometry.spherical.oned.S1Point;

class EdgesBuilder implements BSPTreeVisitor<Sphere2D> {
    private final Map<Edge, BSPTree<Sphere2D>> edgeToNode = new IdentityHashMap();
    private final Map<BSPTree<Sphere2D>, List<Edge>> nodeToEdgesList = new IdentityHashMap();
    private final BSPTree<Sphere2D> root;
    private final double tolerance;

    EdgesBuilder(BSPTree<Sphere2D> root2, double tolerance2) {
        this.root = root2;
        this.tolerance = tolerance2;
    }

    public Order visitOrder(BSPTree<Sphere2D> bSPTree) {
        return Order.MINUS_SUB_PLUS;
    }

    public void visitInternalNode(BSPTree<Sphere2D> node) {
        this.nodeToEdgesList.put(node, new ArrayList());
        BoundaryAttribute<Sphere2D> attribute = (BoundaryAttribute) node.getAttribute();
        if (attribute.getPlusOutside() != null) {
            addContribution((SubCircle) attribute.getPlusOutside(), false, node);
        }
        if (attribute.getPlusInside() != null) {
            addContribution((SubCircle) attribute.getPlusInside(), true, node);
        }
    }

    public void visitLeafNode(BSPTree<Sphere2D> bSPTree) {
    }

    private void addContribution(SubCircle sub, boolean reversed, BSPTree<Sphere2D> node) {
        Edge edge;
        BSPTree<Sphere2D> bSPTree = node;
        Circle circle = (Circle) sub.getHyperplane();
        Iterator i$ = ((ArcsSet) sub.getRemainingRegion()).asList().iterator();
        while (true) {
            Iterator i$2 = i$;
            if (i$2.hasNext()) {
                Arc a = (Arc) i$2.next();
                Vertex start = new Vertex(circle.toSpace((Point) new S1Point(a.getInf())));
                Vertex end = new Vertex(circle.toSpace((Point) new S1Point(a.getSup())));
                start.bindWith(circle);
                end.bindWith(circle);
                if (reversed) {
                    edge = new Edge(end, start, a.getSize(), circle.getReverse());
                    Vertex vertex = end;
                    Vertex vertex2 = start;
                } else {
                    Vertex vertex3 = end;
                    Vertex vertex4 = start;
                    edge = new Edge(start, end, a.getSize(), circle);
                }
                Edge edge2 = edge;
                this.edgeToNode.put(edge2, bSPTree);
                ((List) this.nodeToEdgesList.get(bSPTree)).add(edge2);
                i$ = i$2;
            } else {
                return;
            }
        }
    }

    private Edge getFollowingEdge(Edge previous) throws MathIllegalStateException {
        S2Point point = previous.getEnd().getLocation();
        List<BSPTree<Sphere2D>> candidates = this.root.getCloseCuts(point, this.tolerance);
        double closest = this.tolerance;
        Edge following = null;
        for (BSPTree<Sphere2D> node : candidates) {
            for (Edge edge : (List) this.nodeToEdgesList.get(node)) {
                if (edge != previous && edge.getStart().getIncoming() == null) {
                    double gap = Vector3D.angle(point.getVector(), edge.getStart().getLocation().getVector());
                    if (gap <= closest) {
                        closest = gap;
                        following = edge;
                    }
                }
            }
        }
        if (following != null) {
            return following;
        }
        if (Vector3D.angle(point.getVector(), previous.getStart().getLocation().getVector()) <= this.tolerance) {
            return previous;
        }
        throw new MathIllegalStateException(LocalizedFormats.OUTLINE_BOUNDARY_LOOP_OPEN, new Object[0]);
    }

    public List<Edge> getEdges() throws MathIllegalStateException {
        for (Edge previous : this.edgeToNode.keySet()) {
            previous.setNextEdge(getFollowingEdge(previous));
        }
        return new ArrayList(this.edgeToNode.keySet());
    }
}
