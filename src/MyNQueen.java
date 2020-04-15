//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.BDDFactory.CacheStats;

import java.sql.SQLOutput;
import java.util.Iterator;
import java.util.List;

public class MyNQueen {
    static BDDFactory B;
    static boolean TRACE;
    static int N;
    static BDD[][] X;
    static BDD queen;
    static BDD solution;

    public MyNQueen() {
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("USAGE:  java NQueens N");
        } else {
            N = Integer.parseInt(args[0]);
            if (N <= 0) {
                System.err.println("USAGE:  java NQueens N");
            } else {
                TRACE = true;
                long time = System.currentTimeMillis();
                runTest();
                freeAll();
                time = System.currentTimeMillis() - time;
                System.out.println("Time: " + (double)time / 1000.0D + " seconds");
                CacheStats cachestats = B.getCacheStats();
                if (cachestats != null && cachestats.uniqueAccess > 0) {
                    System.out.println(cachestats);
                }

                B.done();
                B = null;
            }
        }
    }

    public static double runTest() {
        B = BDDFactory.init(100, 100);
        B.setVarNum(3);

        BDD a = B.ithVar(2);
        BDD b = B.ithVar(1);
        BDD c = B.ithVar(0);
        BDD bdd = c.or(a.and(b));
        B.autoReorder(BDDFactory.REORDER_WIN3ITE, 10);


        System.out.println("BDD: " + bdd);


        List allSat = bdd.allsat();
        for (int i = 0; i < allSat.size(); i++) {
            byte[] sol = (byte[]) allSat.get(i);
            for (int j = 0; j < sol.length; j++) {
                System.out.print(j + ": " + sol[j] + ", ");
            }
            System.out.println("");
        }
        System.out.println("\nOption2:");

        Iterator it = bdd.iterator2(bdd.support());

        System.out.println(it.next());
        System.out.println(it.next());
        System.out.println(it.next());


        return 0;
    }

    public static void freeAll() {
        for(int i = 0; i < N; ++i) {
            for(int j = 0; j < N; ++j) {
                X[i][j].free();
            }
        }

        queen.free();
        solution.free();
    }

    static void build(int i, int j) {
        BDD a = B.one();
        BDD b = B.one();
        BDD c = B.one();
        BDD d = B.one();

        BDD u;
        for(int l = 0; l < N; ++l) {
            if (l != j) {
                u = X[i][l].apply(X[i][j], BDDFactory.nand);
                a.andWith(u);
            }
        }

        int k;
        for(k = 0; k < N; ++k) {
            if (k != i) {
                u = X[i][j].apply(X[k][j], BDDFactory.nand);
                b.andWith(u);
            }
        }

        BDD u2;
        int ll;
        for(k = 0; k < N; ++k) {
            ll = k - i + j;
            if (ll >= 0 && ll < N && k != i) {
                u2 = X[i][j].apply(X[k][ll], BDDFactory.nand);
                c.andWith(u2);
            }
        }

        for(k = 0; k < N; ++k) {
            ll = i + j - k;
            if (ll >= 0 && ll < N && k != i) {
                u2 = X[i][j].apply(X[k][ll], BDDFactory.nand);
                d.andWith(u2);
            }
        }

        c.andWith(d);
        b.andWith(c);
        a.andWith(b);
        queen.andWith(a);
    }
}
