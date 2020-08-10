package org.apache.commons.math3.geometry.euclidean.twod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor.Order;
import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

public class PolygonsSet extends AbstractRegion<Euclidean2D, Euclidean1D> {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;
    private Vector2D[][] vertices;

    private static class ConnectableSegment extends Segment {
        private final BSPTree<Euclidean2D> endNode;
        private ConnectableSegment next = null;
        private final BSPTree<Euclidean2D> node;
        private ConnectableSegment previous = null;
        private boolean processed = false;
        private final BSPTree<Euclidean2D> startNode;

        ConnectableSegment(Vector2D start, Vector2D end, Line line, BSPTree<Euclidean2D> node2, BSPTree<Euclidean2D> startNode2, BSPTree<Euclidean2D> endNode2) {
            super(start, end, line);
            this.node = node2;
            this.startNode = startNode2;
            this.endNode = endNode2;
        }

        public BSPTree<Euclidean2D> getNode() {
            return this.node;
        }

        public BSPTree<Euclidean2D> getStartNode() {
            return this.startNode;
        }

        public BSPTree<Euclidean2D> getEndNode() {
            return this.endNode;
        }

        public ConnectableSegment getPrevious() {
            return this.previous;
        }

        public void setPrevious(ConnectableSegment previous2) {
            this.previous = previous2;
        }

        public ConnectableSegment getNext() {
            return this.next;
        }

        public void setNext(ConnectableSegment next2) {
            this.next = next2;
        }

        public void setProcessed(boolean processed2) {
            this.processed = processed2;
        }

        public boolean isProcessed() {
            return this.processed;
        }
    }

    private static class Edge {
        private final Vertex end;
        private final Line line;
        private BSPTree<Euclidean2D> node = null;
        private final Vertex start;

        Edge(Vertex start2, Vertex end2, Line line2) {
            this.start = start2;
            this.end = end2;
            this.line = line2;
            start2.setOutgoing(this);
            end2.setIncoming(this);
        }

        public Vertex getStart() {
            return this.start;
        }

        public Vertex getEnd() {
            return this.end;
        }

        public Line getLine() {
            return this.line;
        }

        public void setNode(BSPTree<Euclidean2D> node2) {
            this.node = node2;
        }

        public BSPTree<Euclidean2D> getNode() {
            return this.node;
        }

        public Vertex split(Line splitLine) {
            Vertex splitVertex = new Vertex(this.line.intersection(splitLine));
            splitVertex.bindWith(splitLine);
            Edge startHalf = new Edge(this.start, splitVertex, this.line);
            Edge endHalf = new Edge(splitVertex, this.end, this.line);
            startHalf.node = this.node;
            endHalf.node = this.node;
            return splitVertex;
        }
    }

    private static class SegmentsBuilder implements BSPTreeVisitor<Euclidean2D> {
        private final List<ConnectableSegment> segments = new ArrayList();
        private final double tolerance;

        SegmentsBuilder(double tolerance2) {
            this.tolerance = tolerance2;
        }

        public Order visitOrder(BSPTree<Euclidean2D> bSPTree) {
            return Order.MINUS_SUB_PLUS;
        }

        public void visitInternalNode(BSPTree<Euclidean2D> node) {
            BoundaryAttribute<Euclidean2D> attribute = (BoundaryAttribute) node.getAttribute();
            Iterable<BSPTree<Euclidean2D>> splitters = attribute.getSplitters();
            if (attribute.getPlusOutside() != null) {
                addContribution(attribute.getPlusOutside(), node, splitters, false);
            }
            if (attribute.getPlusInside() != null) {
                addContribution(attribute.getPlusInside(), node, splitters, true);
            }
        }

        public void visitLeafNode(BSPTree<Euclidean2D> bSPTree) {
        }

        private void addContribution(SubHyperplane<Euclidean2D> sub, BSPTree<Euclidean2D> node, Iterable<BSPTree<Euclidean2D>> splitters, boolean reversed) {
            SegmentsBuilder segmentsBuilder = this;
            Iterable<BSPTree<Euclidean2D>> iterable = splitters;
            Line line = (Line) sub.getHyperplane();
            Iterator i$ = ((IntervalsSet) ((AbstractSubHyperplane) sub).getRemainingRegion()).asList().iterator();
            while (true) {
                Iterator i$2 = i$;
                if (i$2.hasNext()) {
                    Interval i = (Interval) i$2.next();
                    Vector2D vector2D = null;
                    Vector2D startV = Double.isInfinite(i.getInf()) ? null : line.toSpace((Point) new Vector1D(i.getInf()));
                    if (!Double.isInfinite(i.getSup())) {
                        vector2D = line.toSpace((Point) new Vector1D(i.getSup()));
                    }
                    Vector2D endV = vector2D;
                    BSPTree<Euclidean2D> startN = segmentsBuilder.selectClosest(startV, iterable);
                    BSPTree<Euclidean2D> endN = segmentsBuilder.selectClosest(endV, iterable);
                    if (reversed) {
                        List<ConnectableSegment> list = segmentsBuilder.segments;
                        ConnectableSegment connectableSegment = new ConnectableSegment(endV, startV, line.getReverse(), node, endN, startN);
                        list.add(connectableSegment);
                    } else {
                        List<ConnectableSegment> list2 = segmentsBuilder.segments;
                        ConnectableSegment connectableSegment2 = r4;
                        Vector2D vector2D2 = endV;
                        Vector2D vector2D3 = startV;
                        ConnectableSegment connectableSegment3 = new ConnectableSegment(startV, endV, line, node, startN, endN);
                        list2.add(connectableSegment2);
                    }
                    i$ = i$2;
                    segmentsBuilder = this;
                } else {
                    return;
                }
            }
        }

        private BSPTree<Euclidean2D> selectClosest(Vector2D point, Iterable<BSPTree<Euclidean2D>> candidates) {
            BSPTree<Euclidean2D> selected = null;
            double min = Double.POSITIVE_INFINITY;
            for (BSPTree<Euclidean2D> node : candidates) {
                double distance = FastMath.abs(node.getCut().getHyperplane().getOffset(point));
                if (distance < min) {
                    selected = node;
                    min = distance;
                }
            }
            if (min <= this.tolerance) {
                return selected;
            }
            return null;
        }

        public List<ConnectableSegment> getSegments() {
            return this.segments;
        }
    }

    private static class Vertex {
        private Edge incoming = null;
        private final List<Line> lines = new ArrayList();
        private final Vector2D location;
        private Edge outgoing = null;

        Vertex(Vector2D location2) {
            this.location = location2;
        }

        public Vector2D getLocation() {
            return this.location;
        }

        public void bindWith(Line line) {
            this.lines.add(line);
        }

        public Line sharedLineWith(Vertex vertex) {
            for (Line line1 : this.lines) {
                Iterator i$ = vertex.lines.iterator();
                while (true) {
                    if (i$.hasNext()) {
                        if (line1 == ((Line) i$.next())) {
                            return line1;
                        }
                    }
                }
            }
            return null;
        }

        public void setIncoming(Edge incoming2) {
            this.incoming = incoming2;
            bindWith(incoming2.getLine());
        }

        public Edge getIncoming() {
            return this.incoming;
        }

        public void setOutgoing(Edge outgoing2) {
            this.outgoing = outgoing2;
            bindWith(outgoing2.getLine());
        }

        public Edge getOutgoing() {
            return this.outgoing;
        }
    }

    public PolygonsSet(double tolerance) {
        super(tolerance);
    }

    public PolygonsSet(BSPTree<Euclidean2D> tree, double tolerance) {
        super(tree, tolerance);
    }

    public PolygonsSet(Collection<SubHyperplane<Euclidean2D>> boundary, double tolerance) {
        super(boundary, tolerance);
    }

    public PolygonsSet(double xMin, double xMax, double yMin, double yMax, double tolerance) {
        super((Hyperplane<S>[]) boxBoundary(xMin, xMax, yMin, yMax, tolerance), tolerance);
    }

    public PolygonsSet(double hyperplaneThickness, Vector2D... vertices2) {
        super(verticesToTree(hyperplaneThickness, vertices2), hyperplaneThickness);
    }

    @Deprecated
    public PolygonsSet() {
        this(1.0E-10d);
    }

    @Deprecated
    public PolygonsSet(BSPTree<Euclidean2D> tree) {
        this(tree, 1.0E-10d);
    }

    @Deprecated
    public PolygonsSet(Collection<SubHyperplane<Euclidean2D>> boundary) {
        this(boundary, 1.0E-10d);
    }

    @Deprecated
    public PolygonsSet(double xMin, double xMax, double yMin, double yMax) {
        this(xMin, xMax, yMin, yMax, 1.0E-10d);
    }

    private static Line[] boxBoundary(double xMin, double xMax, double yMin, double yMax, double tolerance) {
        double d = xMin;
        double d2 = xMax;
        double d3 = yMin;
        double d4 = yMax;
        double d5 = tolerance;
        if (d >= d2 - d5 || d3 >= d4 - d5) {
            return null;
        }
        Vector2D minMin = new Vector2D(d, d3);
        Vector2D minMax = new Vector2D(d, d4);
        Vector2D maxMin = new Vector2D(d2, d3);
        Vector2D maxMax = new Vector2D(d2, d4);
        return new Line[]{new Line(minMin, maxMin, d5), new Line(maxMin, maxMax, d5), new Line(maxMax, minMax, d5), new Line(minMax, minMin, d5)};
    }

    private static BSPTree<Euclidean2D> verticesToTree(double hyperplaneThickness, Vector2D... vertices2) {
        ArrayList arrayList;
        double d = hyperplaneThickness;
        Vector2D[] vector2DArr = vertices2;
        int n = vector2DArr.length;
        if (n == 0) {
            return new BSPTree<>(Boolean.TRUE);
        }
        Vertex[] vArray = new Vertex[n];
        for (int i = 0; i < n; i++) {
            vArray[i] = new Vertex(vector2DArr[i]);
        }
        List<Edge> arrayList2 = new ArrayList<>(n);
        for (int i2 = 0; i2 < n; i2++) {
            Vertex start = vArray[i2];
            Vertex end = vArray[(i2 + 1) % n];
            Line line = start.sharedLineWith(end);
            if (line == null) {
                line = new Line(start.getLocation(), end.getLocation(), d);
            }
            arrayList2.add(new Edge(start, end, line));
            Vertex[] arr$ = vArray;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Vertex vertex = arr$[i$];
                if (vertex == start || vertex == end) {
                    arrayList = arrayList2;
                } else {
                    arrayList = arrayList2;
                    if (FastMath.abs(line.getOffset((Point<Euclidean2D>) vertex.getLocation())) <= d) {
                        vertex.bindWith(line);
                    }
                }
                i$++;
                arrayList2 = arrayList;
            }
            List<Edge> edges = arrayList2;
        }
        List<Edge> edges2 = arrayList2;
        BSPTree<Euclidean2D> tree = new BSPTree<>();
        insertEdges(d, tree, arrayList2);
        return tree;
    }

    private static void insertEdges(double hyperplaneThickness, BSPTree<Euclidean2D> node, List<Edge> edges) {
        int index;
        double d = hyperplaneThickness;
        BSPTree<Euclidean2D> bSPTree = node;
        int index2 = 0;
        Edge inserted = null;
        while (inserted == null && index2 < edges.size()) {
            int index3 = index2 + 1;
            inserted = (Edge) edges.get(index2);
            if (inserted.getNode() != null) {
                inserted = null;
            } else if (bSPTree.insertCut(inserted.getLine())) {
                inserted.setNode(bSPTree);
            } else {
                inserted = null;
            }
            index2 = index3;
        }
        List<Edge> list = edges;
        if (inserted == null) {
            BSPTree<Euclidean2D> parent = node.getParent();
            if (parent == null || bSPTree == parent.getMinus()) {
                bSPTree.setAttribute(Boolean.TRUE);
            } else {
                bSPTree.setAttribute(Boolean.FALSE);
            }
            return;
        }
        List<Edge> plusList = new ArrayList<>();
        List<Edge> minusList = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge != inserted) {
                double startOffset = inserted.getLine().getOffset((Point<Euclidean2D>) edge.getStart().getLocation());
                double endOffset = inserted.getLine().getOffset((Point<Euclidean2D>) edge.getEnd().getLocation());
                Side startSide = FastMath.abs(startOffset) <= d ? Side.HYPER : startOffset < 0.0d ? Side.MINUS : Side.PLUS;
                Side endSide = FastMath.abs(endOffset) <= d ? Side.HYPER : endOffset < 0.0d ? Side.MINUS : Side.PLUS;
                switch (startSide) {
                    case PLUS:
                        index = index2;
                        if (endSide != Side.MINUS) {
                            plusList.add(edge);
                            break;
                        } else {
                            Vertex splitPoint = edge.split(inserted.getLine());
                            minusList.add(splitPoint.getOutgoing());
                            plusList.add(splitPoint.getIncoming());
                            break;
                        }
                    case MINUS:
                        index = index2;
                        if (endSide != Side.PLUS) {
                            minusList.add(edge);
                            break;
                        } else {
                            Vertex splitPoint2 = edge.split(inserted.getLine());
                            minusList.add(splitPoint2.getIncoming());
                            plusList.add(splitPoint2.getOutgoing());
                            break;
                        }
                    default:
                        index = index2;
                        if (endSide != Side.PLUS) {
                            if (endSide != Side.MINUS) {
                                break;
                            } else {
                                minusList.add(edge);
                                break;
                            }
                        } else {
                            plusList.add(edge);
                            break;
                        }
                }
            } else {
                index = index2;
            }
            index2 = index;
            List<Edge> list2 = edges;
        }
        if (plusList.isEmpty() == 0) {
            insertEdges(d, node.getPlus(), plusList);
        } else {
            node.getPlus().setAttribute(Boolean.FALSE);
        }
        if (!minusList.isEmpty()) {
            insertEdges(d, node.getMinus(), minusList);
        } else {
            node.getMinus().setAttribute(Boolean.TRUE);
        }
    }

    public PolygonsSet buildNew(BSPTree<Euclidean2D> tree) {
        return new PolygonsSet(tree, getTolerance());
    }

    /* access modifiers changed from: protected */
    public void computeGeometricalProperties() {
        Vector2D[][] arr$;
        Vector2D[] arr$2;
        Vector2D[][] v = getVertices();
        if (v.length == 0) {
            BSPTree<Euclidean2D> tree = getTree(false);
            if (tree.getCut() != null || !((Boolean) tree.getAttribute()).booleanValue()) {
                setSize(0.0d);
                setBarycenter((Point<S>) new Vector2D<S>(0.0d, 0.0d));
                return;
            }
            setSize(Double.POSITIVE_INFINITY);
            setBarycenter((Point<S>) Vector2D.NaN);
        } else if (v[0][0] == null) {
            setSize(Double.POSITIVE_INFINITY);
            setBarycenter((Point<S>) Vector2D.NaN);
        } else {
            double sumY = 0.0d;
            double sumX = 0.0d;
            double sum = 0.0d;
            for (Vector2D[] loop : v) {
                double x1 = loop[loop.length - 1].getX();
                double y1 = loop[loop.length - 1].getY();
                for (Vector2D point : loop) {
                    double x0 = x1;
                    double y0 = y1;
                    x1 = point.getX();
                    y1 = point.getY();
                    double factor = (x0 * y1) - (y0 * x1);
                    sum += factor;
                    sumX += (x0 + x1) * factor;
                    sumY += (y0 + y1) * factor;
                }
            }
            if (sum < 0.0d) {
                setSize(Double.POSITIVE_INFINITY);
                setBarycenter((Point<S>) Vector2D.NaN);
                return;
            }
            setSize(sum / 2.0d);
            setBarycenter((Point<S>) new Vector2D<S>(sumX / (sum * 3.0d), sumY / (3.0d * sum)));
        }
    }

    public Vector2D[][] getVertices() {
        int pending;
        List list;
        SegmentsBuilder visitor;
        SegmentsBuilder visitor2;
        List list2;
        int pending2;
        List list3;
        int j;
        int pending3;
        if (this.vertices == null) {
            int i = 0;
            if (getTree(false).getCut() == null) {
                this.vertices = new Vector2D[0][];
            } else {
                SegmentsBuilder visitor3 = new SegmentsBuilder(getTolerance());
                int i2 = 1;
                getTree(true).visit(visitor3);
                List segments = visitor3.getSegments();
                int pending4 = segments.size() - naturalFollowerConnections(segments);
                if (pending4 > 0) {
                    pending4 -= splitEdgeConnections(segments);
                }
                if (pending4 > 0) {
                    pending4 -= closeVerticesConnections(segments);
                }
                ArrayList<List<Segment>> loops = new ArrayList<>();
                while (true) {
                    ConnectableSegment s = getUnprocessed(segments);
                    if (s == null) {
                        break;
                    }
                    List<Segment> loop = followLoop(s);
                    if (loop != null) {
                        if (((Segment) loop.get(0)).getStart() == null) {
                            loops.add(0, loop);
                        } else {
                            loops.add(loop);
                        }
                    }
                }
                this.vertices = new Vector2D[loops.size()][];
                int i3 = 0;
                Iterator i$ = loops.iterator();
                while (i$.hasNext()) {
                    List<Segment> loop2 = (List) i$.next();
                    if (loop2.size() < 2) {
                        visitor2 = visitor3;
                        list2 = segments;
                        pending2 = pending4;
                    } else if (loop2.size() == 2 && ((Segment) loop2.get(i)).getStart() == null && ((Segment) loop2.get(i2)).getEnd() == null) {
                        visitor2 = visitor3;
                        list2 = segments;
                        pending2 = pending4;
                    } else {
                        if (((Segment) loop2.get(i)).getStart() == null) {
                            Vector2D[] array = new Vector2D[(loop2.size() + 2)];
                            int j2 = 0;
                            for (Segment segment : loop2) {
                                SegmentsBuilder visitor4 = visitor3;
                                if (j2 == 0) {
                                    double x = segment.getLine().toSubSpace((Point) segment.getEnd()).getX();
                                    list3 = segments;
                                    double x2 = x - FastMath.max(1.0d, FastMath.abs(x / 2.0d));
                                    int j3 = j2 + 1;
                                    array[j2] = null;
                                    j2 = j3 + 1;
                                    array[j3] = segment.getLine().toSpace((Point) new Vector1D(x2));
                                } else {
                                    list3 = segments;
                                }
                                if (j2 < array.length - 1) {
                                    j = j2 + 1;
                                    array[j2] = segment.getEnd();
                                } else {
                                    j = j2;
                                }
                                if (j == array.length - 1) {
                                    double x3 = segment.getLine().toSubSpace((Point) segment.getStart()).getX();
                                    pending3 = pending4;
                                    int j4 = j + 1;
                                    array[j] = segment.getLine().toSpace((Point) new Vector1D(x3 + FastMath.max(1.0d, FastMath.abs(x3 / 2.0d))));
                                    j2 = j4;
                                } else {
                                    pending3 = pending4;
                                    j2 = j;
                                }
                                visitor3 = visitor4;
                                segments = list3;
                                pending4 = pending3;
                            }
                            visitor = visitor3;
                            list = segments;
                            pending = pending4;
                            int i4 = i3 + 1;
                            this.vertices[i3] = array;
                            i3 = i4;
                        } else {
                            visitor = visitor3;
                            list = segments;
                            pending = pending4;
                            Vector2D[] array2 = new Vector2D[loop2.size()];
                            int j5 = 0;
                            for (Segment segment2 : loop2) {
                                int j6 = j5 + 1;
                                array2[j5] = segment2.getStart();
                                j5 = j6;
                            }
                            int i5 = i3 + 1;
                            this.vertices[i3] = array2;
                            i3 = i5;
                        }
                        i = 0;
                        visitor3 = visitor;
                        segments = list;
                        pending4 = pending;
                        i2 = 1;
                    }
                    i = 0;
                    Line line = ((Segment) loop2.get(0)).getLine();
                    int i6 = i3 + 1;
                    this.vertices[i3] = new Vector2D[]{null, line.toSpace((Point) new Vector1D(-3.4028234663852886E38d)), line.toSpace((Point) new Vector1D(3.4028234663852886E38d))};
                    i3 = i6;
                    visitor3 = visitor;
                    segments = list;
                    pending4 = pending;
                    i2 = 1;
                }
            }
        }
        return (Vector2D[][]) this.vertices.clone();
    }

    private int naturalFollowerConnections(List<ConnectableSegment> segments) {
        int connected = 0;
        for (ConnectableSegment segment : segments) {
            if (segment.getNext() == null) {
                BSPTree<Euclidean2D> node = segment.getNode();
                BSPTree<Euclidean2D> end = segment.getEndNode();
                Iterator i$ = segments.iterator();
                while (true) {
                    if (!i$.hasNext()) {
                        break;
                    }
                    ConnectableSegment candidateNext = (ConnectableSegment) i$.next();
                    if (candidateNext.getPrevious() == null && candidateNext.getNode() == end && candidateNext.getStartNode() == node) {
                        segment.setNext(candidateNext);
                        candidateNext.setPrevious(segment);
                        connected++;
                        break;
                    }
                }
            }
        }
        return connected;
    }

    private int splitEdgeConnections(List<ConnectableSegment> segments) {
        int connected = 0;
        for (ConnectableSegment segment : segments) {
            if (segment.getNext() == null) {
                Hyperplane<Euclidean2D> hyperplane = segment.getNode().getCut().getHyperplane();
                BSPTree<Euclidean2D> end = segment.getEndNode();
                Iterator i$ = segments.iterator();
                while (true) {
                    if (!i$.hasNext()) {
                        break;
                    }
                    ConnectableSegment candidateNext = (ConnectableSegment) i$.next();
                    if (candidateNext.getPrevious() == null && candidateNext.getNode().getCut().getHyperplane() == hyperplane && candidateNext.getStartNode() == end) {
                        segment.setNext(candidateNext);
                        candidateNext.setPrevious(segment);
                        connected++;
                        break;
                    }
                }
            }
        }
        return connected;
    }

    private int closeVerticesConnections(List<ConnectableSegment> segments) {
        int connected = 0;
        for (ConnectableSegment segment : segments) {
            if (segment.getNext() == null && segment.getEnd() != null) {
                Vector2D end = segment.getEnd();
                ConnectableSegment selectedNext = null;
                double min = Double.POSITIVE_INFINITY;
                for (ConnectableSegment candidateNext : segments) {
                    if (candidateNext.getPrevious() == null && candidateNext.getStart() != null) {
                        double distance = Vector2D.distance(end, candidateNext.getStart());
                        if (distance < min) {
                            selectedNext = candidateNext;
                            min = distance;
                        }
                    }
                }
                if (min <= getTolerance()) {
                    segment.setNext(selectedNext);
                    selectedNext.setPrevious(segment);
                    connected++;
                }
            }
        }
        return connected;
    }

    private ConnectableSegment getUnprocessed(List<ConnectableSegment> segments) {
        for (ConnectableSegment segment : segments) {
            if (!segment.isProcessed()) {
                return segment;
            }
        }
        return null;
    }

    private List<Segment> followLoop(ConnectableSegment defining) {
        List<Segment> loop = new ArrayList<>();
        loop.add(defining);
        defining.setProcessed(true);
        ConnectableSegment next = defining.getNext();
        while (next != defining && next != null) {
            loop.add(next);
            next.setProcessed(true);
            next = next.getNext();
        }
        if (next == null) {
            for (ConnectableSegment previous = defining.getPrevious(); previous != null; previous = previous.getPrevious()) {
                loop.add(0, previous);
                previous.setProcessed(true);
            }
        }
        filterSpuriousVertices(loop);
        if (loop.size() != 2 || ((Segment) loop.get(0)).getStart() == null) {
            return loop;
        }
        return null;
    }

    private void filterSpuriousVertices(List<Segment> loop) {
        int i = 0;
        while (i < loop.size()) {
            Segment previous = (Segment) loop.get(i);
            int j = (i + 1) % loop.size();
            Segment next = (Segment) loop.get(j);
            if (next != null && Precision.equals(previous.getLine().getAngle(), next.getLine().getAngle(), Precision.EPSILON)) {
                loop.set(j, new Segment(previous.getStart(), next.getEnd(), previous.getLine()));
                int i2 = i - 1;
                loop.remove(i);
                i = i2;
            }
            i++;
        }
    }
}
