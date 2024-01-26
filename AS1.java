import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AS1 implements Runnable {

    // Threads to use
    private static final int numThreads = 8;

    // Finding primes in up to 100 million
    private static final int Max = 100000000;

    // private static final int Max = 64;

    // private static final int Max = 5;

    // The range of numbers to check for primes in each thread
    private int currStart = 0;
    private int currEnd = 0;

    // Using a boolean array to store primes by using Sieve of Erathosthenes
    // algorithm found on Wikipedia
    private static final boolean[] isPrime = new boolean[Max];

    // Using a synchronized list to store all primes found as it is thread safe
    private static List<Integer> allPrimes = Collections.synchronizedList(new ArrayList<>());

    public AS1(int start, int end) {
        this.currStart = start;
        this.currEnd = end;
    }

    @Override
    public void run() {
        for (int i = currStart; i < currEnd; i++) {
            if (isPrime[i]) {
                allPrimes.add(i);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        // Initialize the prime array and fill all values to be true and assume every
        // number is prime
        // Initialize the prime array
        for (int i = 2; i < Max; i++) {
            isPrime[i] = true;
        }

        // Sieve of Eratosthenes algorithm
        for (int p = 2; p * p < Max; p++) {
            if (isPrime[p]) {
                for (int i = p * p; i < Max; i += p) {
                    isPrime[i] = false;
                }
            }
        }

        // ExecutorService for managing threads
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int threadSize = (Max + numThreads - 1) / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int start = i * threadSize;
            int end = Math.min((i + 1) * threadSize, Max);
            executor.execute(new AS1(start, end));
        }

        // Shutdown the executor and wait for termination
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        // Aggregate results
        long sum = allPrimes.stream().mapToLong(Integer::longValue).sum();
        // Reverse primes array
        allPrimes.sort(Collections.reverseOrder());
        List<Integer> topTenPrimes = allPrimes.subList(0, Math.min(10, allPrimes.size()));

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        try {
            FileWriter writer = new FileWriter("primes5.txt");
            writer.write(
                    "execution time: " + executionTime + " total primes found: " + allPrimes.size()
                            + " sum of all primes: " + sum + "\n");
            writer.write("Top 10 maximum primes: ");
            for (int prime : topTenPrimes) {
                writer.write(prime + " ");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Execution Complete. Check primes.txt for output.");
    }

}
