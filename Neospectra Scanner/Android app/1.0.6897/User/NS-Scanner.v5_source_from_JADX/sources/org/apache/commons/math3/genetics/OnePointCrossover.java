package org.apache.commons.math3.genetics;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class OnePointCrossover<T> implements CrossoverPolicy {
    public ChromosomePair crossover(Chromosome first, Chromosome second) throws DimensionMismatchException, MathIllegalArgumentException {
        if ((first instanceof AbstractListChromosome) && (second instanceof AbstractListChromosome)) {
            return crossover((AbstractListChromosome) first, (AbstractListChromosome) second);
        }
        throw new MathIllegalArgumentException(LocalizedFormats.INVALID_FIXED_LENGTH_CHROMOSOME, new Object[0]);
    }

    private ChromosomePair crossover(AbstractListChromosome<T> first, AbstractListChromosome<T> second) throws DimensionMismatchException {
        int length = first.getLength();
        if (length != second.getLength()) {
            throw new DimensionMismatchException(second.getLength(), length);
        }
        List<T> parent1Rep = first.getRepresentation();
        List<T> parent2Rep = second.getRepresentation();
        List<T> child1Rep = new ArrayList<>(length);
        List<T> child2Rep = new ArrayList<>(length);
        int crossoverIndex = GeneticAlgorithm.getRandomGenerator().nextInt(length - 2) + 1;
        for (int i = 0; i < crossoverIndex; i++) {
            child1Rep.add(parent1Rep.get(i));
            child2Rep.add(parent2Rep.get(i));
        }
        for (int i2 = crossoverIndex; i2 < length; i2++) {
            child1Rep.add(parent2Rep.get(i2));
            child2Rep.add(parent1Rep.get(i2));
        }
        return new ChromosomePair(first.newFixedLengthChromosome(child1Rep), second.newFixedLengthChromosome(child2Rep));
    }
}
