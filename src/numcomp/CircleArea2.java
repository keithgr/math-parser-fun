/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package numcomp;

import com.udojava.evalex.AbstractFunction;
import com.udojava.evalex.Expression;
import com.udojava.evalex.Function;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author kg392
 */
public class CircleArea2 {

    private static final int THREAD_COUNT = 8;
    private static final int NUM_POINTS = 50_000_000;
    private static final Function COMP = new AbstractFunction("a", 3) {
        @Override
        public BigDecimal eval(List<BigDecimal> vars) {
            var min = vars.get(0);
            var max = vars.get(1);
            var step = vars.get(2);

            var area = BigDecimal.ZERO;
            for (var x = min; x.compareTo(max) == -1; x = x.add(step)) {
                var exp = new Expression("sqrt(1-x^2)").with("x", x);
                area = area.add(exp.eval().multiply(step));
            }

            return (new BigDecimal(4)).multiply(area);
        }
    };

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<BigDecimal>> futures = new ArrayList<>();

        var x = BigDecimal.ZERO;
        var deltaX = BigDecimal.ONE.divide(new BigDecimal(THREAD_COUNT));

        for (int i = 0; i < THREAD_COUNT; i++, x = x.add(deltaX)) {
            var params = Arrays.asList(
                    x,
                    x.add(deltaX),
                    BigDecimal.ONE.divide(new BigDecimal(NUM_POINTS))
            );
            
            Future<BigDecimal> future = es.submit(()->{
                return COMP.eval(params);
            });
            futures.add(future);
        }
        
        var area = BigDecimal.ZERO;
        for(var f : futures) {
            area = area.add(f.get());
        }
        System.out.printf("Area = %s\n", area.toPlainString());
        
        es.shutdown();
    }
}
