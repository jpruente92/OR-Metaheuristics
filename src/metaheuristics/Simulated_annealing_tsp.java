package metaheuristics;

import java.util.ArrayList;

import heuristics.Heuristics;
import misc.Algorithms;
import settings.Tsp;
import solution.Solution;
import solution.Solution_tsp;

public class Simulated_annealing_tsp extends Simulated_annealing {

    Tsp tsp;

    public Simulated_annealing_tsp(double max_nr_iterations, double exploitations_factor, Tsp setting) {
        this.exploitation_factor = exploitations_factor;
        this.max_nr_iterations_without_improvement = max_nr_iterations;
        this.setting = setting;
        tsp = setting;
    }

    public double evaluate(Solution solution) {
        Solution_tsp solution_tsp = (Solution_tsp) solution;
        return setting.evaluate_solution(solution_tsp.tour);
    }

    public Solution neighbor_of_solution_exploitation(Solution solution) {
        Solution_tsp solution_tsp = (Solution_tsp) solution;
        ArrayList<Integer> tour = (ArrayList<Integer>) solution_tsp.tour.clone();
        int index_to_remove = setting.r.nextInt(tour.size());
        Integer removed_node = tour.remove(index_to_remove);
        int best_index_to_insert = 0;
        double best_val = Double.MAX_VALUE;
        for (int i = 0; i < tour.size(); i++) {
            ArrayList<Integer> clone = (ArrayList<Integer>) tour.clone();
            clone.add(i, removed_node);
            double current_val = setting.evaluate_solution(clone);
            if (current_val < best_val) {
                best_val = current_val;
                best_index_to_insert = i;
            }
        }
        tour.add(best_index_to_insert, removed_node);
        return new Solution_tsp(tour);
    }

    public Solution neighbor_of_solution_exploration(Solution solution) {
        Solution_tsp solution_tsp = (Solution_tsp) solution;
        ArrayList<Integer> tour = (ArrayList<Integer>) solution_tsp.tour.clone();
        int index_to_remove = setting.r.nextInt(tour.size());
        Integer removed_node = tour.remove(index_to_remove);
        int index_to_insert = setting.r.nextInt(tour.size());
        tour.add(index_to_insert, removed_node);
        return new Solution_tsp(tour);
    }

    public Solution start_solution() {
        if (start_solution_type == 0) {
            ArrayList<Integer> subtour = new ArrayList<Integer>();
            subtour.add(setting.r.nextInt(tsp.nr_vertices));
            ArrayList<Integer> nearest_neighbor_solution = Heuristics.nearest_neighbor_tsp(tsp, subtour);
            return new Solution_tsp(nearest_neighbor_solution);
        }

        ArrayList<Integer> unused_nodes = new ArrayList<Integer>();
        for (int i = 0; i < tsp.nr_vertices; i++) {
            unused_nodes.add(i);
        }
        ArrayList<Integer> start_solution = new ArrayList<Integer>();
        while (!unused_nodes.isEmpty()) {
            start_solution.add(unused_nodes.remove(setting.r.nextInt(unused_nodes.size())));
        }
        return new Solution_tsp(start_solution);
    }

    public boolean update_optimal_solution_value(Solution solution, double value) {
        Solution_tsp solution_tsp = (Solution_tsp) solution;
        return setting.update_optimal_solution(solution_tsp.tour, value);
    }

}
