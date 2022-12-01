package helloworld;

import java.math.BigInteger;
import java.util.Arrays;

import static java.math.BigInteger.*;

public class MatrixAddition {

    public static void main(String[] args) {
        int[][] matrix = new int[10000][10000];
        for (int n=0; n < matrix[0].length; n++) {
            for (int j=0; j< matrix[n].length; j++ ) {
                matrix[n][j] = 1;
            }
        }
        System.out.println();
        System.out.println(Arrays.stream(matrix).flatMapToInt(Arrays::stream).mapToObj(BigInteger::valueOf).reduce(ZERO,BigInteger::add));
    }
}
