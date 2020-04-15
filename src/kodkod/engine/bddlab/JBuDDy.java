package kodkod.engine.bddlab;

import kodkod.engine.bool.*;
import kodkod.engine.fol2sat.bdd.BooleanTranslation;
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.BuDDyFactory;

import static kodkod.engine.bool.Operator.ITE;
import static kodkod.engine.bool.Operator.NOT;


/**
 * Java wrapper for the JavaBDD BuDDy wrapper.
 * @author Mark Lavrentyev
 */
final class JBuDDy {
    private BooleanTranslation translation;
    private BDDFactory factory;
    private BDD bdd;

    public JBuDDy(BooleanTranslation booleanTranslation) {
        this.translation = booleanTranslation;

        this.factory = BuDDyFactory.init(100000, 10000);
        this.factory.setVarNum(translation.getNumVars());

        // start with empty bdd which is trivially true
        this.bdd = factory.one();
    }

    /**
     * Constructs the BDD from the boolean formula. Returns true if
     * the formula is sat and false if it's unsat.
     * @return true when sat, false when unsat
     */
    public boolean construct() {
        assert factory.isInitialized();
        bdd = translation.getFormula().accept(new BDDConstructor(), new Object());

        return isSat(bdd);
    }

    /**
     * Frees any memory being used by BuDDy and puts it back into its original state.
     * Behavior after calling this is unspecified.
     */
    public void done() {
        factory.done();
    }

    /**
     * Checks whether the given bdd is satisfiable i.e. it's not the false node.
     * @param bddToCheck The bdd to check for satisfiability on.
     * @return true if the bdd is satisfiable and false otherwise.
     */
    private boolean isSat(BDD bddToCheck) {
        return !bddToCheck.isZero();
    }

    /**
     * Constructor to build BDD based on the formula it's given. Implements the
     * BooleanVisitor.
     * @author Mark Lavrentyev
     */
    private class BDDConstructor implements BooleanVisitor<BDD, Object> {
        /**
         * Return a bdd representing the given multigate boolean formula. Recursively
         * builds the bdd for each of the sub-formulas and combines them based on the
         * multigate operator.
         * @param multigate The multigate boolean formula to build a bdd for.
         * @return The bdd for the given multigate formula.
         */
        @Override
        public BDD visit(MultiGate multigate, Object arg) {
            BDD[] branchBDDs = new BDD[multigate.size()];
            for (int i = 0; i < multigate.size(); i++) {
                branchBDDs[i] = multigate.input(i).accept(this, arg);

                if (branchBDDs[i] == null) {
                    return null;
                }
            }

            if (multigate.op().equals(Operator.AND)) {
                assert multigate.size() == 2;
                return branchBDDs[0].and(branchBDDs[1]);

            } else if (multigate.op().equals(Operator.OR)) {
                assert multigate.size() == 2;
                return branchBDDs[0].or(branchBDDs[1]);

            } else {
                return null;
            }
        }

        /**
         * Return a bdd representing an if-then-else (ite) gate. Recursively builds
         * the bdd for each of the three sub-branches and combines them.
         * @param ite The ite boolean formula to build a bdd for.
         * @return The bdd for the ite boolean formula.
         */
        @Override
        public BDD visit(ITEGate ite, Object arg) {
            BDD ifSubBDD = ite.input(0).accept(this, arg);
            BDD thenSubBDD = ite.input(1).accept(this, arg);
            BDD elseSubBDD = ite.input(2).accept(this, arg);

            if (ifSubBDD == null || thenSubBDD == null || elseSubBDD == null) {
                return null;
            }

            return ifSubBDD.ite(thenSubBDD, elseSubBDD);
        }

        /**
         * Return a bdd representing the negation formula passed in.
         * @param negation The not-gate formula to represent with a bdd.
         * @return A bdd representing the not-ed formula.
         */
        @Override
        public BDD visit(NotGate negation, Object arg) {
            BDD subformulaBDD = negation.input(0).accept(this, arg);

            if (subformulaBDD == null) {
                return null;
            }

            return subformulaBDD.not();
        }

        /**
         * Return a bdd representing just the given boolean variable.
         * @param variable The variable to represent.
         * @return The bdd representing the given variable.
         */
        @Override
        public BDD visit(BooleanVariable variable, Object arg) {
            return factory.ithVar(translation.getVarMap(variable));
        }
    }
}