/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package numcomp;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 *
 * @author kg392
 */
public class CircleArea {

    private static final int THREAD_COUNT = 4;
    private static final int TASK_SIZE = 10_000;
    private static final int NUM_POINTS = 10_000_000;

    private static final String FORMULA = "sqrt(1 - x^2)";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<BigDecimal>> pendingResults = new ArrayList<>();

        BigDecimal ptInc = BigDecimal.ONE.divide(new BigDecimal(NUM_POINTS));
        BigDecimal taskInc = ptInc.multiply(new BigDecimal(TASK_SIZE));
        for (BigDecimal x = BigDecimal.ZERO; x.compareTo(BigDecimal.ONE) == -1; x = x.add(taskInc)) {
            BigDecimal x_0 = x.abs();
            Callable<BigDecimal> calculation = () -> {
                BigDecimal sum = BigDecimal.ZERO;
                for (BigDecimal xInput = x_0.abs(); xInput.compareTo(x_0.add(taskInc)) == -1; xInput = xInput.add(ptInc)) {
                    sum = sum.add(
                            (BigDecimal.ONE.subtract(xInput.pow(2)))
                                    .sqrt(new MathContext(100))
                                    .multiply(ptInc)
                    );
                }
                return sum;
            };

            Future<BigDecimal> pendingResult = es.submit(calculation);
            pendingResults.add(pendingResult);
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (var pr : pendingResults) {
            BigDecimal result = pr.get();
            sum = sum.add(result);
        }
        System.out.printf("Sum = %s\n", sum.multiply(new BigDecimal(4)).toPlainString());

        es.shutdown();
    }
}
