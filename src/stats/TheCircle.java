/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stats;

/**
 *
 * @author kg392
 */
public class TheCircle {
    
    private static final int SIZE = 100;
    private static final double[][] PROBS = new double[SIZE + 1][SIZE + 1];
    static {
        for (int n = 0; n <= SIZE; n++) {
            for (int c = 0; c <= SIZE; c++) {
                PROBS[n][c] = -1.0;
            }
        }
        
        for (int n = 1; n <= SIZE; n++) {
            PROBS[n][1] = 1.0 / n;
        }
    } 
    
    public static void main(String[] args) {
        // Print options header
        for (int n = 1; n <= SIZE; n++) {
            System.out.printf("\tOpts %d", n);
        }
        System.out.printf("\n");
        
        // Print table
        for (int c = 1; c <= SIZE; c++) {
            System.out.printf("Ch %d", c);
            
            for (int n = 1; n <= SIZE; n++) {
                if(n < c) {
                    System.out.printf("\t");
                }
                else {
                    System.out.printf("\t%.12f", getProb(n,c));
                }
            }
            System.out.printf("\n");
        }
    }
    
    private static double getProb(int n, int c) {
        // Case is already solved
        if(PROBS[n][c] >= 0.0) {
            return PROBS[n][c];
        }
        
        // Choices remaining exceeds options
        if(n < c) {
            PROBS[n][c] = 0.0;
            return 0.0;
        }
        
        // Compute solution based on previous solutions
        double sum = 0.0;
        for (int i = 1; i < n; i++) {
            sum += getProb(i, c-1);
        }
        
        // Update table and return solution
        PROBS[n][c] = sum / n;
        return PROBS[n][c];
    }
}
