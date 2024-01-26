# COP4516-AS1

This program demonstrates a multi-threaded approach to a computationally intensive task (finding primes up to 10^8) and showcases how to divide such a task among multiple threads for improved performance. The program uses the Sieve of Eratosthenes Algorithm which is a classic algorithm for finding all prime numbers up to a certain limit. It iteratively marks the multiples of each prime number as non-prime (false in the isPrime array).

I have created test cases to find primes between 1 and 64 and also between 1 and 5. I have noticed that the smaller the max number, the faster the program runs, which is expected due to less data to compute.
