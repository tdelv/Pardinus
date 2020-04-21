package kodkod.engine.fol2sat;

import kodkod.ast.Relation;
import kodkod.engine.bddlab.BDDSolution;
import kodkod.engine.bddlab.BDDSolver;
import kodkod.engine.bool.BooleanFormula;
import kodkod.engine.bool.BooleanVariable;
import kodkod.engine.config.Options;
import kodkod.instance.Bounds;
import kodkod.instance.Instance;
import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.util.ints.IndexedEntry;
import kodkod.util.ints.IntIterator;
import kodkod.util.ints.IntSet;
import kodkod.util.ints.Ints;

import java.util.HashMap;
import java.util.Map;

public class BooleanTranslation {
    private final Bounds bounds;
    private final Options options;
    private final Map<Relation, IntSet> primaryVarUsage;
    private final int maxPrimaryVar;

    private TranslationLog log;

    private final BooleanFormula formula;
    private HashMap<BooleanVariable, Integer> variableMapping;
    private final BDDSolver solver;

    /**
     * Create the boolean translation object from a formula, bounds, options, and vars. Initializes
     * the bdd solver based on the solver type the options object specifies.
     */
    BooleanTranslation(BooleanFormula formula, Bounds bounds, Options options, Map<Relation, IntSet> varUsage, int maxPrimaryVar) {
        assert options.solverType() == Options.SolverType.BDD;

        this.formula = formula;
        this.bounds = bounds;
        this.options = options;
        this.solver = options.bddSolver().instance();
        this.primaryVarUsage = varUsage;
        this.maxPrimaryVar = maxPrimaryVar;
    }

    /**
     * Same as {@link #BooleanTranslation(BooleanFormula, Bounds, Options, Map, int)} except it also
     * takes the translation log and stores it.
     * @param log The log of the translation process.
     */
    BooleanTranslation(BooleanFormula formula, Bounds bounds, Options options, Map<Relation, IntSet> varUsage, int maxPrimaryVar, TranslationLog log) {
        this(formula, bounds, options, varUsage, maxPrimaryVar);
        this.log = log;
    }

    public IntSet primaryVariables(Relation relation) {
        final IntSet vars = primaryVarUsage.get(relation);
        return vars==null ? Ints.EMPTY_SET : vars;
    }


    /**
     * Gets the boolean formula in this translation.
     * @return the translated boolean formula.
     */
    public BooleanFormula getFormula() {
        return formula;
    }

    /**
     * Gets the bdd variable id this boolean variable maps to.
     * @param var The variable to get the mapping for.
     * @return The id of the variable in the solver.
     */
    public int getVarMap(BooleanVariable var) {
        return variableMapping.get(var);
    }

    /**
     * Gets the current bdd solver in use for this problem.
     * @return The bdd solver in use for this problem.
     */
    public BDDSolver solver() {
        return solver;
    }

    /**
     * Gets the options used in this translation.
     * @return The options used in this translation
     */
    public Options options() {
        return options;
    }

    /**
     * Gets the first solution from the bdd solver and interprets it into an instance, similar
     * to how {@link Translation#interpret()} works.
     * @requires solver.isReady() = true
     * @requires solver.isSat() = true
     * @return the instance that corresponds to the first solution from the solver.
     */
    public final Instance interpret() {
        // get a solution
        if (!solver.isReady()) {
            throw new IllegalStateException("BDD solver must be ready before getting solution.");
        } else if (!solver.isSat()) {
            throw new IllegalStateException("BDD solver must be SAT to get a solution.");
        }
        BDDSolution.Total totalSolution = solver.next().next();

        // build instance from the boolean solution
        final Instance instance = new Instance(bounds.universe());
        final TupleFactory f = bounds.universe().factory();

        for(IndexedEntry<TupleSet> entry : bounds.intBounds()) {
            instance.add(entry.index(), entry.value());
        }

        for(Relation r : bounds.relations()) {
            TupleSet lower = bounds.lowerBound(r);
            IntSet indices = Ints.bestSet(lower.capacity());
            indices.addAll(lower.indexView());
            IntSet vars = primaryVariables(r);

            if (!vars.isEmpty()) {
                int lit = vars.min();
                for(IntIterator iter = bounds.upperBound(r).indexView().iterator(); iter.hasNext();) {
                    final int index = iter.next();
                    if (!indices.contains(index) && totalSolution.valueOfBool(lit++))
                        indices.add(index);
                }
            }
            instance.add(r, f.setOf(r.arity(), indices));
        }

        return instance;
    }
}
