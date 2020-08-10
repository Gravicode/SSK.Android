package org.apache.commons.math3.genetics;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;

public class NPointCrossover<T> implements CrossoverPolicy {
    private final int crossoverPoints;

    public NPointCrossover(int crossoverPoints2) throws NotStrictlyPositiveException {
        if (crossoverPoints2 <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(crossoverPoints2));
        }
        this.crossoverPoints = crossoverPoints2;
    }

    public int getCrossoverPoints() {
        return this.crossoverPoints;
    }

    public ChromosomePair crossover(Chromosome first, Chromosome second) throws DimensionMismatchException, MathIllegalArgumentException {
        if ((first instanceof AbstractListChromosome) && (second instanceof AbstractListChromosome)) {
            return mate((AbstractListChromosome) first, (AbstractListChromosome) second);
        }
        throw new MathIllegalArgumentException(LocalizedFormats.INVALID_FIXED_LENGTH_CHROMOSOME, new Object[0]);
    }

    private ChromosomePair mate(AbstractListChromosome<T> first, AbstractListChromosome<T> second) throws DimensionMismatchException, NumberIsTooLargeException {
        int length = first.getLength();
        if (length != second.getLength()) {
            throw new DimensionMismatchException(second.getLength(), length);
        }
        int i = 0;
        if (this.crossoverPoints >= length) {
            throw new NumberIsTooLargeException(Integer.valueOf(this.crossoverPoints), Integer.valueOf(length), false);
        }
        List<T> parent1Rep = first.getRepresentation();
        List<T> parent2Rep = second.getRepresentation();
        ArrayList arrayList = new ArrayList(length);
        ArrayList arrayList2 = new ArrayList(length);
        RandomGenerator random = GeneticAlgorithm.getRandomGenerator();
        ArrayList arrayList3 = arrayList;
        ArrayList arrayList4 = arrayList2;
        int remainingPoints = this.crossoverPoints;
        int lastIndex = 0;
        while (i < this.crossoverPoints) {
            int crossoverIndex = lastIndex + 1 + random.nextInt((length - lastIndex) - remainingPoints);
            for (int j = lastIndex; j < crossoverIndex; j++) {
                arrayList3.add(parent1Rep.get(j));
                arrayList4.add(parent2Rep.get(j));
            }
            ArrayList arrayList5 = arrayList3;
            arrayList3 = arrayList4;
            arrayList4 = arrayList5;
            lastIndex = crossoverIndex;
            i++;
            remainingPoints--;
        }
        for (int j2 = lastIndex; j2 < length; j2++) {
            arrayList3.add(parent1Rep.get(j2));
            arrayList4.add(parent2Rep.get(j2));
        }
        return new ChromosomePair(first.newFixedLengthChromosome(arrayList), second.newFixedLengthChromosome(arrayList2));
    }
}
