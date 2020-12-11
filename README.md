# OR-Metaheuristics
Implementation of different heuristics and metaheuristics in Java for the Traveling Salesman Problem.
Covered algorithms:
 - Nearest Neighbor 
 - Ant Colony
 - Simulated Annealing
 - Tabu Search

The code can be easily extended to solve different settings e.g. for the Simulated Annealing algorithm it is just necessary to provide problem specific functions for evaluating a solution, computing a start solution and computing a neighbor solution.
Some tests suggest that Simulated Annealing and Tabu Search outperform the Ant Colony algorithm for this setting but there are various hyperparameters that can be tuned.
