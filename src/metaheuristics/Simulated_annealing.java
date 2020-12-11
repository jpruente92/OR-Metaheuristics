package metaheuristics;

import java.util.ArrayList;

import solution.Solution;

public abstract class Simulated_annealing extends Metaheuristic {

    double max_nr_iterations_without_improvement = 0;
    double exploitation_factor;

    public void solve() {
        long endtime = System.currentTimeMillis() + 1000 * setting.time_limit_seconds;
        while (System.currentTimeMillis() < endtime) {
            Solution actual_solution = start_solution();
            nr_runs++;
            double actual_value = evaluate(actual_solution);
            update_optimal_solution_value(actual_solution, actual_value);
            double i = 0.0;
            while (i < max_nr_iterations_without_improvement) {
                if (System.currentTimeMillis() > endtime) {
                    break;
                }
                i++;
                double temperature = i / max_nr_iterations_without_improvement;
                Solution neighbor = neighbor_of_solution(actual_solution);
                double neighbor_value = evaluate(neighbor);
                if (probability_of_acceptance(actual_value, neighbor_value, temperature) >= setting.r.nextDouble()) {
                    actual_solution = neighbor;
                    actual_value = neighbor_value;
                    if (update_optimal_solution_value(actual_solution, actual_value)) {
                        if (i > highest_iteration_with_improvement) {
                            highest_iteration_with_improvement = (int) i;
                        }
                        i = 0;
                        nr_improvements++;
                    }
                }
                nr_iterations++;
            }
        }

    }

    private Solution neighbor_of_solution(Solution solution) {
        if (setting.r.nextDouble() < exploitation_factor) {
            return neighbor_of_solution_exploitation(solution);
        }
        return neighbor_of_solution_exploration(solution);
    }

    private double probability_of_acceptance(double actual_value, double neighbor_value, double temperature) {
        if (neighbor_value < actual_value) {
            return 1.0;
        } else {
            return Math.exp(-(neighbor_value - actual_value) / temperature);
        }
    }

    public abstract double evaluate(Solution solution);

    public abstract Solution neighbor_of_solution_exploitation(Solution solution);

    public abstract Solution neighbor_of_solution_exploration(Solution solution);

    public abstract Solution start_solution();

    public abstract boolean update_optimal_solution_value(Solution solution, double value);

}
