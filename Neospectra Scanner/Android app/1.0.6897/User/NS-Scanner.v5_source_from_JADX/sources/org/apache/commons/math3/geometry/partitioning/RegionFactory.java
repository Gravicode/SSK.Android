package org.apache.commons.math3.geometry.partitioning;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.partitioning.BSPTree.LeafMerger;
import org.apache.commons.math3.geometry.partitioning.BSPTree.VanishingCutHandler;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor.Order;
import org.apache.commons.math3.geometry.partitioning.Region.Location;

public class RegionFactory<S extends Space> {
    private final NodesCleaner nodeCleaner = new NodesCleaner<>();

    private class DifferenceMerger implements LeafMerger<S>, VanishingCutHandler<S> {
        private final Region<S> region1;
        private final Region<S> region2;

        DifferenceMerger(Region<S> region12, Region<S> region22) {
            this.region1 = region12.copySelf();
            this.region2 = region22.copySelf();
        }

        public BSPTree<S> merge(BSPTree<S> leaf, BSPTree<S> tree, BSPTree<S> parentTree, boolean isPlusChild, boolean leafFromInstance) {
            if (((Boolean) leaf.getAttribute()).booleanValue()) {
                BSPTree<S> argTree = RegionFactory.this.recurseComplement(leafFromInstance ? tree : leaf);
                argTree.insertInTree(parentTree, isPlusChild, this);
                return argTree;
            }
            BSPTree<S> instanceTree = leafFromInstance ? leaf : tree;
            instanceTree.insertInTree(parentTree, isPlusChild, this);
            return instanceTree;
        }

        public BSPTree<S> fixNode(BSPTree<S> node) {
            Point<S> p = this.region1.buildNew(node.pruneAroundConvexCell(Boolean.TRUE, Boolean.FALSE, null)).getBarycenter();
            return new BSPTree<>(Boolean.valueOf(this.region1.checkPoint(p) == Location.INSIDE && this.region2.checkPoint(p) == Location.OUTSIDE));
        }
    }

    private class IntersectionMerger implements LeafMerger<S> {
        private IntersectionMerger() {
        }

        public BSPTree<S> merge(BSPTree<S> leaf, BSPTree<S> tree, BSPTree<S> parentTree, boolean isPlusChild, boolean leafFromInstance) {
            if (((Boolean) leaf.getAttribute()).booleanValue()) {
                tree.insertInTree(parentTree, isPlusChild, new VanishingToLeaf(true));
                return tree;
            }
            leaf.insertInTree(parentTree, isPlusChild, new VanishingToLeaf(false));
            return leaf;
        }
    }

    private class NodesCleaner implements BSPTreeVisitor<S> {
        private NodesCleaner() {
        }

        public Order visitOrder(BSPTree<S> bSPTree) {
            return Order.PLUS_SUB_MINUS;
        }

        public void visitInternalNode(BSPTree<S> node) {
            node.setAttribute(null);
        }

        public void visitLeafNode(BSPTree<S> bSPTree) {
        }
    }

    private class UnionMerger implements LeafMerger<S> {
        private UnionMerger() {
        }

        public BSPTree<S> merge(BSPTree<S> leaf, BSPTree<S> tree, BSPTree<S> parentTree, boolean isPlusChild, boolean leafFromInstance) {
            if (((Boolean) leaf.getAttribute()).booleanValue()) {
                leaf.insertInTree(parentTree, isPlusChild, new VanishingToLeaf(true));
                return leaf;
            }
            tree.insertInTree(parentTree, isPlusChild, new VanishingToLeaf(false));
            return tree;
        }
    }

    private class VanishingToLeaf implements VanishingCutHandler<S> {
        private final boolean inside;

        VanishingToLeaf(boolean inside2) {
            this.inside = inside2;
        }

        public BSPTree<S> fixNode(BSPTree<S> node) {
            if (node.getPlus().getAttribute().equals(node.getMinus().getAttribute())) {
                return new BSPTree<>(node.getPlus().getAttribute());
            }
            return new BSPTree<>(Boolean.valueOf(this.inside));
        }
    }

    private class XorMerger implements LeafMerger<S> {
        private XorMerger() {
        }

        public BSPTree<S> merge(BSPTree<S> leaf, BSPTree<S> tree, BSPTree<S> parentTree, boolean isPlusChild, boolean leafFromInstance) {
            BSPTree<S> t = tree;
            if (((Boolean) leaf.getAttribute()).booleanValue()) {
                t = RegionFactory.this.recurseComplement(t);
            }
            t.insertInTree(parentTree, isPlusChild, new VanishingToLeaf(true));
            return t;
        }
    }

    public Region<S> buildConvex(Hyperplane<S>... hyperplanes) {
        Hyperplane<S>[] arr$;
        if (hyperplanes == null || hyperplanes.length == 0) {
            return null;
        }
        Region<S> region = hyperplanes[0].wholeSpace();
        BSPTree<S> node = region.getTree(false);
        node.setAttribute(Boolean.TRUE);
        BSPTree<S> node2 = node;
        for (Hyperplane<S> hyperplane : hyperplanes) {
            if (node2.insertCut(hyperplane)) {
                node2.setAttribute(null);
                node2.getPlus().setAttribute(Boolean.FALSE);
                node2 = node2.getMinus();
                node2.setAttribute(Boolean.TRUE);
            } else {
                SubHyperplane<S> s = hyperplane.wholeHyperplane();
                for (BSPTree<S> tree = node2; tree.getParent() != null && s != null; tree = tree.getParent()) {
                    Hyperplane<S> other = tree.getParent().getCut().getHyperplane();
                    switch (s.split(other).getSide()) {
                        case HYPER:
                            if (hyperplane.sameOrientationAs(other)) {
                                break;
                            } else {
                                return getComplement(hyperplanes[0].wholeSpace());
                            }
                        case PLUS:
                            throw new MathIllegalArgumentException(LocalizedFormats.NOT_CONVEX_HYPERPLANES, new Object[0]);
                        default:
                            s = s.split(other).getMinus();
                            break;
                    }
                }
            }
        }
        return region;
    }

    public Region<S> union(Region<S> region1, Region<S> region2) {
        BSPTree<S> tree = region1.getTree(false).merge(region2.getTree(false), new UnionMerger());
        tree.visit(this.nodeCleaner);
        return region1.buildNew(tree);
    }

    public Region<S> intersection(Region<S> region1, Region<S> region2) {
        BSPTree<S> tree = region1.getTree(false).merge(region2.getTree(false), new IntersectionMerger());
        tree.visit(this.nodeCleaner);
        return region1.buildNew(tree);
    }

    public Region<S> xor(Region<S> region1, Region<S> region2) {
        BSPTree<S> tree = region1.getTree(false).merge(region2.getTree(false), new XorMerger());
        tree.visit(this.nodeCleaner);
        return region1.buildNew(tree);
    }

    public Region<S> difference(Region<S> region1, Region<S> region2) {
        BSPTree<S> tree = region1.getTree(false).merge(region2.getTree(false), new DifferenceMerger(region1, region2));
        tree.visit(this.nodeCleaner);
        return region1.buildNew(tree);
    }

    public Region<S> getComplement(Region<S> region) {
        return region.buildNew(recurseComplement(region.getTree(false)));
    }

    /* access modifiers changed from: private */
    public BSPTree<S> recurseComplement(BSPTree<S> node) {
        Map<BSPTree<S>, BSPTree<S>> map = new HashMap<>();
        BSPTree<S> transformedTree = recurseComplement(node, map);
        for (Entry<BSPTree<S>, BSPTree<S>> entry : map.entrySet()) {
            if (((BSPTree) entry.getKey()).getCut() != null) {
                BoundaryAttribute<S> original = (BoundaryAttribute) ((BSPTree) entry.getKey()).getAttribute();
                if (original != null) {
                    BoundaryAttribute<S> transformed = (BoundaryAttribute) ((BSPTree) entry.getValue()).getAttribute();
                    Iterator i$ = original.getSplitters().iterator();
                    while (i$.hasNext()) {
                        transformed.getSplitters().add((BSPTree) map.get((BSPTree) i$.next()));
                    }
                }
            }
        }
        return transformedTree;
    }

    private BSPTree<S> recurseComplement(BSPTree<S> node, Map<BSPTree<S>, BSPTree<S>> map) {
        BSPTree<S> transformedNode;
        if (node.getCut() == null) {
            transformedNode = new BSPTree<>(((Boolean) node.getAttribute()).booleanValue() ? Boolean.FALSE : Boolean.TRUE);
        } else {
            BoundaryAttribute boundaryAttribute = (BoundaryAttribute) node.getAttribute();
            if (boundaryAttribute != null) {
                SubHyperplane<S> plusInside = null;
                SubHyperplane copySelf = boundaryAttribute.getPlusInside() == null ? null : boundaryAttribute.getPlusInside().copySelf();
                if (boundaryAttribute.getPlusOutside() != null) {
                    plusInside = boundaryAttribute.getPlusOutside().copySelf();
                }
                boundaryAttribute = new BoundaryAttribute(copySelf, plusInside, new NodesSet());
            }
            transformedNode = new BSPTree<>(node.getCut().copySelf(), recurseComplement(node.getPlus(), map), recurseComplement(node.getMinus(), map), boundaryAttribute);
        }
        map.put(node, transformedNode);
        return transformedNode;
    }
}
