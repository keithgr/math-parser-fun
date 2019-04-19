/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package numcomp;

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

    private static final int THREAD_COUNT = 6;
    private static final String FORMULA = "sin(x)cos(y) + cos(x)sin(y)";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(THREAD_COUNT);

        List<Future<Double>> pendingResults = new ArrayList<>();
        for (int x = 0; x < 10_000; x++) {
            for (int y = 0; y < 1_000; y += 1) {
                int x1 = x;
                int y1 = y;
                Callable<Double> calculation = () -> {
                    return new ExpressionBuilder(FORMULA)
                            .variables("x", "y")
                            .build()
                            .setVariable("x", x1)
                            .setVariable("y", y1)
                            .evaluate();
                };

                Future<Double> pendingResult = es.submit(calculation);
                pendingResults.add(pendingResult);
            }
        }

        double sum = 0.0;
        for (var pr : pendingResults) {
            double result = pr.get();
            sum += result;
        }
        System.out.printf("Sum = %.6f\n", sum);

        es.shutdown();
    }
}
