package org.apache.commons.math3.genetics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class CycleCrossover<T> implements CrossoverPolicy {
    private final boolean randomStart;

    public CycleCrossover() {
        this(false);
    }

    public CycleCrossover(boolean randomStart2) {
        this.randomStart = randomStart2;
    }

    public boolean isRandomStart() {
        return this.randomStart;
    }

    public ChromosomePair crossover(Chromosome first, Chromosome second) throws DimensionMismatchException, MathIllegalArgumentException {
        if ((first instanceof AbstractListChromosome) && (second instanceof AbstractListChromosome)) {
            return mate((AbstractListChromosome) first, (AbstractListChromosome) second);
        }
        throw new MathIllegalArgumentException(LocalizedFormats.INVALID_FIXED_LENGTH_CHROMOSOME, new Object[0]);
    }

    /* access modifiers changed from: protected */
    public ChromosomePair mate(AbstractListChromosome<T> first, AbstractListChromosome<T> second) throws DimensionMismatchException {
        int length = first.getLength();
        if (length != second.getLength()) {
            throw new DimensionMismatchException(second.getLength(), length);
        }
        List<T> parent1Rep = first.getRepresentation();
        List<T> parent2Rep = second.getRepresentation();
        List<T> child1Rep = new ArrayList<>(second.getRepresentation());
        List<T> child2Rep = new ArrayList<>(first.getRepresentation());
        Set<Integer> visitedIndices = new HashSet<>(length);
        List<Integer> indices = new ArrayList<>(length);
        int idx = this.randomStart ? GeneticAlgorithm.getRandomGenerator().nextInt(length) : 0;
        int idx2 = 1;
        while (visitedIndices.size() < length) {
            indices.add(Integer.valueOf(idx));
            for (int idx3 = parent1Rep.indexOf(parent2Rep.get(idx)); idx3 != ((Integer) indices.get(0)).intValue(); idx3 = parent1Rep.indexOf(parent2Rep.get(idx3))) {
                indices.add(Integer.valueOf(idx3));
            }
            int cycle = idx2 + 1;
            if (idx2 % 2 != 0) {
                for (Integer intValue : indices) {
                    int i = intValue.intValue();
                    T tmp = child1Rep.get(i);
                    child1Rep.set(i, child2Rep.get(i));
                    child2Rep.set(i, tmp);
                }
            }
            visitedIndices.addAll(indices);
            idx = (((Integer) indices.get(0)).intValue() + 1) % length;
            while (visitedIndices.contains(Integer.valueOf(idx)) && visitedIndices.size() < length) {
                idx++;
                if (idx >= length) {
                    idx = 0;
                }
            }
            indices.clear();
            idx2 = cycle;
        }
        return new ChromosomePair(first.newFixedLengthChromosome(child1Rep), second.newFixedLengthChromosome(child2Rep));
    }
}
