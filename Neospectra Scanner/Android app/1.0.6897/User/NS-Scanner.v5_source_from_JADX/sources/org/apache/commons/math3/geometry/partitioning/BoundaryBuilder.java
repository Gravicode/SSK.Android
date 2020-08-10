package org.apache.commons.math3.geometry.partitioning;

import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor.Order;

class BoundaryBuilder<S extends Space> implements BSPTreeVisitor<S> {
    BoundaryBuilder() {
    }

    public Order visitOrder(BSPTree<S> bSPTree) {
        return Order.PLUS_MINUS_SUB;
    }

    public void visitInternalNode(BSPTree<S> node) {
        SubHyperplane<S> plusOutside = null;
        SubHyperplane<S> plusInside = null;
        NodesSet nodesSet = null;
        Characterization<S> plusChar = new Characterization<>(node.getPlus(), node.getCut().copySelf());
        if (plusChar.touchOutside()) {
            Characterization<S> minusChar = new Characterization<>(node.getMinus(), plusChar.outsideTouching());
            if (minusChar.touchInside()) {
                plusOutside = minusChar.insideTouching();
                nodesSet = new NodesSet();
                nodesSet.addAll(minusChar.getInsideSplitters());
                nodesSet.addAll(plusChar.getOutsideSplitters());
            }
        }
        if (plusChar.touchInside()) {
            Characterization<S> minusChar2 = new Characterization<>(node.getMinus(), plusChar.insideTouching());
            if (minusChar2.touchOutside()) {
                plusInside = minusChar2.outsideTouching();
                if (nodesSet == null) {
                    nodesSet = new NodesSet();
                }
                nodesSet.addAll(minusChar2.getOutsideSplitters());
                nodesSet.addAll(plusChar.getInsideSplitters());
            }
        }
        if (nodesSet != null) {
            for (BSPTree<S> up = node.getParent(); up != null; up = up.getParent()) {
                nodesSet.add(up);
            }
        }
        node.setAttribute(new BoundaryAttribute(plusOutside, plusInside, nodesSet));
    }

    public void visitLeafNode(BSPTree<S> bSPTree) {
    }
}
