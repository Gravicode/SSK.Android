package org.apache.commons.math3.genetics;

public abstract class Chromosome implements Comparable<Chromosome>, Fitness {
    private static final double NO_FITNESS = Double.NEGATIVE_INFINITY;
    private double fitness = NO_FITNESS;

    public double getFitness() {
        if (this.fitness == NO_FITNESS) {
            this.fitness = fitness();
        }
        return this.fitness;
    }

    public int compareTo(Chromosome another) {
        return Double.compare(getFitness(), another.getFitness());
    }

    /* access modifiers changed from: protected */
    public boolean isSame(Chromosome another) {
        return false;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Incorrect type for immutable var: ssa=org.apache.commons.math3.genetics.Population, code=org.apache.commons.math3.genetics.Population<org.apache.commons.math3.genetics.Chromosome>, for r4v0, types: [org.apache.commons.math3.genetics.Population<org.apache.commons.math3.genetics.Chromosome>, org.apache.commons.math3.genetics.Population] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.commons.math3.genetics.Chromosome findSameChromosome(org.apache.commons.math3.genetics.Population<org.apache.commons.math3.genetics.Chromosome> r4) {
        /*
            r3 = this;
            java.util.Iterator r0 = r4.iterator()
        L_0x0004:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0018
            java.lang.Object r1 = r0.next()
            org.apache.commons.math3.genetics.Chromosome r1 = (org.apache.commons.math3.genetics.Chromosome) r1
            boolean r2 = r3.isSame(r1)
            if (r2 == 0) goto L_0x0017
            return r1
        L_0x0017:
            goto L_0x0004
        L_0x0018:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.genetics.Chromosome.findSameChromosome(org.apache.commons.math3.genetics.Population):org.apache.commons.math3.genetics.Chromosome");
    }

    public void searchForFitnessUpdate(Population population) {
        Chromosome sameChromosome = findSameChromosome(population);
        if (sameChromosome != null) {
            this.fitness = sameChromosome.getFitness();
        }
    }
}
