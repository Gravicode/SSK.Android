package org.apache.commons.math3.geometry.partitioning;

import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitSubHyperplane;

class InsideFinder<S extends Space> {
    private boolean minusFound = false;
    private boolean plusFound = false;
    private final Region<S> region;

    InsideFinder(Region<S> region2) {
        this.region = region2;
    }

    public void recurseSides(BSPTree<S> node, SubHyperplane<S> sub) {
        if (node.getCut() == null) {
            if (((Boolean) node.getAttribute()).booleanValue()) {
                this.plusFound = true;
                this.minusFound = true;
            }
            return;
        }
        SplitSubHyperplane<S> split = sub.split(node.getCut().getHyperplane());
        switch (split.getSide()) {
            case PLUS:
                if (node.getCut().split(sub.getHyperplane()).getSide() == Side.PLUS) {
                    if (!this.region.isEmpty(node.getMinus())) {
                        this.plusFound = true;
                    }
                } else if (!this.region.isEmpty(node.getMinus())) {
                    this.minusFound = true;
                }
                if (!this.plusFound || !this.minusFound) {
                    recurseSides(node.getPlus(), sub);
                    break;
                }
            case MINUS:
                if (node.getCut().split(sub.getHyperplane()).getSide() == Side.PLUS) {
                    if (!this.region.isEmpty(node.getPlus())) {
                        this.plusFound = true;
                    }
                } else if (!this.region.isEmpty(node.getPlus())) {
                    this.minusFound = true;
                }
                if (!this.plusFound || !this.minusFound) {
                    recurseSides(node.getMinus(), sub);
                    break;
                }
            case BOTH:
                recurseSides(node.getPlus(), split.getPlus());
                if (!this.plusFound || !this.minusFound) {
                    recurseSides(node.getMinus(), split.getMinus());
                    break;
                }
            default:
                if (!node.getCut().getHyperplane().sameOrientationAs(sub.getHyperplane())) {
                    if (node.getPlus().getCut() != null || ((Boolean) node.getPlus().getAttribute()).booleanValue()) {
                        this.minusFound = true;
                    }
                    if (node.getMinus().getCut() != null || ((Boolean) node.getMinus().getAttribute()).booleanValue()) {
                        this.plusFound = true;
                        break;
                    }
                } else {
                    if (node.getPlus().getCut() != null || ((Boolean) node.getPlus().getAttribute()).booleanValue()) {
                        this.plusFound = true;
                    }
                    if (node.getMinus().getCut() != null || ((Boolean) node.getMinus().getAttribute()).booleanValue()) {
                        this.minusFound = true;
                        break;
                    }
                }
        }
    }

    public boolean plusFound() {
        return this.plusFound;
    }

    public boolean minusFound() {
        return this.minusFound;
    }
}
