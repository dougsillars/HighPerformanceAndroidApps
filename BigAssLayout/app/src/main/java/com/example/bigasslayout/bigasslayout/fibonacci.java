package com.example.bigasslayout.bigasslayout;

/**
 * Created by demo on 12/23/14.
 */
public class fibonacci {
    //recursive fibonacci
    public static int fib(int n) {
        if (n <= 0)
            return 0;
        if (n == 1)
            return 1;
        return fib(n - 1) + fib(n - 2);
    }
}
