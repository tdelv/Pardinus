package kodkod.test.pardinus.decomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import kodkod.ast.Formula;
import kodkod.engine.DecomposedPardinusSolver;
import kodkod.engine.ExtendedSolver;
import kodkod.engine.PardinusSolver;
import kodkod.engine.Solution;
import kodkod.engine.config.AbstractReporter;
import kodkod.engine.config.ExtendedOptions;
import kodkod.engine.config.Reporter;
import kodkod.engine.config.DecomposedOptions.DMode;
import kodkod.engine.decomp.DModel;
import kodkod.engine.satlab.SATFactory;
import kodkod.examples.pardinus.decomp.RedBlackTreeP;
import kodkod.examples.pardinus.decomp.RedBlackTreeP.Variant1;
import kodkod.examples.pardinus.decomp.RedBlackTreeP.Variant2;
import kodkod.instance.Bounds;
import kodkod.instance.PardinusBounds;
import kodkod.util.ints.IntSet;

import org.junit.Before;
import org.junit.Test;

public class RedBlackTests {
	PardinusSolver psolver;
	ExtendedOptions opt, opt2;
	
	@Before 
	public void method() throws InterruptedException {
		
		opt = new ExtendedOptions();
		opt.setSymmetryBreaking(20);
		opt.setSolver(SATFactory.Glucose);
		opt.setDecomposedMode(DMode.PARALLEL);
		opt.setThreads(4);
		opt2 = new ExtendedOptions(opt);
		opt2.setRunTarget(false);
		opt2.setSolver(SATFactory.PMaxSAT4J);
		opt.setConfigOptions(opt2);
		Reporter rep = new AbstractReporter() {
			@Override
			public void translatingToBoolean(Formula formula, Bounds bounds) {
				System.out.println("to bool: " + formula.toString() + ", "
						+ bounds);
			}
			
			@Override
			public void detectedSymmetries(Set<IntSet> parts) {
				System.out.println("symmetry: " + parts.toString());
			}

			@Override
			public void debug(String debug) {
				System.out.println(debug);
			}
		};
		opt.setReporter(rep);
		opt2.setReporter(rep);
		psolver = new PardinusSolver(opt);
	}
	
	@Test 
	public void testSAT3() throws InterruptedException {
		int n = 3;
		Variant1 v1 = Variant1.COUNTER;
		Variant2 v2 = Variant2.V1;
		
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());
		
		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), true);
		assertTrue(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns() <= 5);
		assertEquals(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs(), 5);
	}
	
	@Test 
	public void testSAT4() throws InterruptedException {
		int n = 4;
		Variant1 v1 = Variant1.COUNTER;
		Variant2 v2 = Variant2.V1;
		
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());
		
		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), true);
		assertTrue(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns() <= 14);
		assertEquals(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs(), 14);
	}
	
	@Test 
	public void testSAT5() throws InterruptedException {
		int n = 5;
		Variant1 v1 = Variant1.COUNTER;
		Variant2 v2 = Variant2.V1;

		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth()+2);
		opt2.setBitwidth(model.getBitwidth()+2);
		
		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		System.out.println(solution.instance());
		assertEquals(model.shortName()+": SAT", solution.sat(), true);
		assertTrue(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns() <= 42);
		assertEquals(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs(), 42);
	}
	
	@Test 
	public void testSAT6() throws InterruptedException {
		int n = 6;
		Variant1 v1 = Variant1.COUNTER;
		Variant2 v2 = Variant2.V1;
		
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());
		
		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), true);
		assertTrue(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns() <= 132);
		assertEquals(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs(), 132);
	}
	
	@Test 
	public void testUNSAT3() throws InterruptedException {
		int n = 3;
		Variant1 v1 = Variant1.THEOREM;
		Variant2 v2 = Variant2.V1;
		
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());
		
		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), false);
		assertEquals(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns(), 5);
		assertEquals(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs(), 5);
	}
	
	@Test 
	public void testUNSAT4() throws InterruptedException {
		int n = 4;
		Variant1 v1 = Variant1.THEOREM;
		Variant2 v2 = Variant2.V1;
		
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());
		
		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), false);
		assertEquals(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns(), 14);
		assertEquals(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs(), 14);
	}
	
	@Test 
	public void testUNSAT5() throws InterruptedException {
		int n = 5;
		Variant1 v1 = Variant1.THEOREM;
		Variant2 v2 = Variant2.V1;
		
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());

		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), false);
		assertEquals(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns(), 42);
		assertEquals(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs(), 42);
	}
	
	@Test 
	public void testUNSAT6() throws InterruptedException {
		int n = 6;
		Variant1 v1 = Variant1.THEOREM;
		Variant2 v2 = Variant2.V1;
		
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());
		
		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), false);
		assertEquals(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns(), 132);
		assertEquals(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs(), 132);
	}
	
	@Test 
	public void testHSAT3() throws InterruptedException {
		int n = 3;
		Variant1 v1 = Variant1.COUNTER;
		Variant2 v2 = Variant2.V1;
		psolver.options().setDecomposedMode(DMode.HYBRID);
	
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());
		
		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), true);
		assertTrue(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs() <= 5);
		assertTrue(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns() <= 6);
		assertEquals(model.shortName()+": Amalg", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.isAmalgamated(), true);
	}
	
	@Test 
	public void testHSAT4() throws InterruptedException {
		int n = 4;
		Variant1 v1 = Variant1.COUNTER;
		Variant2 v2 = Variant2.V1;
		psolver.options().setDecomposedMode(DMode.HYBRID);
	
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());
		
		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), true);
		assertTrue(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs() <= 14);
		assertTrue(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns() < 14);
//		assertEquals(model.shortName()+": Amalg", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.isAmalgamated(), true);
	}
	
	@Test 
	public void testHSAT5() throws InterruptedException {
		int n = 5;
		Variant1 v1 = Variant1.COUNTER;
		Variant2 v2 = Variant2.V1;
		psolver.options().setDecomposedMode(DMode.HYBRID);
	
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());
		
		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), true);
		assertTrue(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs() <= 42);
		assertTrue(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns() < 42);
		assertEquals(model.shortName()+": Amalg", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.isAmalgamated(), true);
	}
	
	@Test 
	public void testHSAT6() throws InterruptedException {
		int n = 6;
		Variant1 v1 = Variant1.COUNTER;
		Variant2 v2 = Variant2.V1;
		psolver.options().setDecomposedMode(DMode.HYBRID);
	
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());
		
		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), true);
		assertTrue(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs() <= 132);
		assertTrue(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns() < 132);
		assertEquals(model.shortName()+": Amalg", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.isAmalgamated(), true);
	}
	
	@Test 
	public void testHUNSAT3() throws InterruptedException {
		int n = 3;
		Variant1 v1 = Variant1.THEOREM;
		Variant2 v2 = Variant2.V1;
		psolver.options().setDecomposedMode(DMode.HYBRID);
	
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());
		
		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), false);
		assertTrue(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs() <= 5);
		assertTrue(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns() <= 6);
		assertEquals(model.shortName()+": Amalg", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.isAmalgamated(), true);
	}
	
	@Test 
	public void testHUNSAT4() throws InterruptedException {
		int n = 4;
		Variant1 v1 = Variant1.THEOREM;
		Variant2 v2 = Variant2.V1;
		psolver.options().setDecomposedMode(DMode.HYBRID);
	
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());

		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), false);
		assertTrue(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs() <= 14);
		assertTrue(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns() < 14);
		assertEquals(model.shortName()+": Amalg", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.isAmalgamated(), true);
	}
	
	@Test 
	public void testHUNSAT5() throws InterruptedException {
		int n = 5;
		Variant1 v1 = Variant1.THEOREM;
		Variant2 v2 = Variant2.V1;
		psolver.options().setDecomposedMode(DMode.HYBRID);
	
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());
		
		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), false);
		assertTrue(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs() <= 42);
		assertTrue(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns() < 42);
		assertEquals(model.shortName()+": Amalg", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.isAmalgamated(), true);
	}
	
	@Test 
	public void testHUNSAT6() throws InterruptedException {
		int n = 6;
		Variant1 v1 = Variant1.THEOREM;
		Variant2 v2 = Variant2.V1;
		psolver.options().setDecomposedMode(DMode.HYBRID);
	
		String[] args = new String[]{n+"",v1.name(),v2.name()};
		DModel model = new RedBlackTreeP(args);

		opt.setBitwidth(model.getBitwidth());
		opt2.setBitwidth(model.getBitwidth());
		
		final PardinusBounds b1 = model.bounds1();
		final Bounds b2 = model.bounds2();
		final Formula f1 = model.partition1();
		final Formula f2 = model.partition2();
		
		Solution solution = psolver.solve(f1.and(f2), new PardinusBounds(b1, b2));
		assertEquals(model.shortName()+": SAT", solution.sat(), false);
		assertTrue(model.shortName()+": #Configs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumConfigs() <= 132);
		assertTrue(model.shortName()+": #Runs", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.getNumRuns() < 132);
		assertEquals(model.shortName()+": Amalg", ((DecomposedPardinusSolver<ExtendedSolver>) psolver.solver).executor().monitor.isAmalgamated(), true);
	}
}
