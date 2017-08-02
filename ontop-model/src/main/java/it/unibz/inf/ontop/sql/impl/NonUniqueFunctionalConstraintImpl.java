package it.unibz.inf.ontop.sql.impl;

import com.google.common.collect.ImmutableSet;
import it.unibz.inf.ontop.sql.Attribute;
import it.unibz.inf.ontop.sql.NonUniqueFunctionalConstraint;

import java.util.HashSet;
import java.util.Set;

public class NonUniqueFunctionalConstraintImpl implements NonUniqueFunctionalConstraint {

    private final ImmutableSet<Attribute> determinants;
    private final ImmutableSet<Attribute> dependents;

    private NonUniqueFunctionalConstraintImpl(ImmutableSet<Attribute> determinants,
                                             ImmutableSet<Attribute> dependents) {
        this.determinants = determinants;
        this.dependents = dependents;
    }

    @Override
    public ImmutableSet<Attribute> getDeterminants() {
        return determinants;
    }

    @Override
    public ImmutableSet<Attribute> getDependents() {
        return dependents;
    }


    public static class BuilderImpl implements Builder {

        private final Set<Attribute> determinants;
        private final Set<Attribute> dependents;

        public BuilderImpl() {
            determinants = new HashSet<>();
            dependents = new HashSet<>();
        }


        @Override
        public Builder addDeterminant(Attribute determinant) {
            determinants.add(determinant);
            return this;
        }

        @Override
        public Builder addDependent(Attribute dependent) {
            dependents.add(dependent);
            return this;
        }

        @Override
        public NonUniqueFunctionalConstraint build() {
            return new NonUniqueFunctionalConstraintImpl(ImmutableSet.copyOf(determinants),
                    ImmutableSet.copyOf(dependents));
        }
    }
}