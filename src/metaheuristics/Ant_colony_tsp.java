package metaheuristics;

import java.util.ArrayList;
import java.util.HashSet;

import heuristics.Heuristics;
import misc.Algorithms;
import settings.Tsp;

public class Ant_colony_tsp extends Ant_colony {

    Tsp tsp;

    public Ant_colony_tsp(int num_ants, int num_ants_spreading_pheromone, int max_nr_iterations_without_improvement, double val_q, double val_alpha, double val_beta, double pheromone_vanish_factor, double percentage_of_considered_edges, double pheromone_level_start_solution, int attractiveness_heuristic, Tsp tsp) {
        nr_ants = num_ants;
        nr_vertices = tsp.nr_vertices;
        this.max_nr_iterations_without_improvement = max_nr_iterations_without_improvement;
        this.nr_ants_spreading_pheromone = num_ants_spreading_pheromone;
        q = val_q;
        alpha = val_alpha;
        beta = val_beta;
        setting = tsp;
        this.tsp = tsp;
        this.pheromone_level_start_solution = pheromone_level_start_solution;
        this.pheromone_vanish_factor = pheromone_vanish_factor;
        this.percentage_of_considered_edges = percentage_of_considered_edges;
        this.attractiveness_heuristic = attractiveness_heuristic;
        initialize_distance_matrix();
    }

    public double compute_attractiveness(int start, int end, ArrayList<Object> subtour) {
        if (attractiveness_heuristic == 0) {
            ArrayList<Integer> tour = Heuristics.nearest_neighbor_tsp(tsp, Algorithms.convert_from_object_list(subtour));
            double length = tsp.evaluate_solution(tour);
            return 1.0 / length;
        }
        return 1.0 / tsp.distance_matrix[start][end];
    }

    public ArrayList<Object> compute_path_for_ant() {
        ArrayList<Object> path = new ArrayList<Object>();
        path.add(0);
        int start = 0;
        HashSet<Integer> not_visited_nodes = new HashSet<Integer>();
        for (int i = 1; i < tsp.nr_vertices; i++) {
            not_visited_nodes.add(i);
        }
        while (!not_visited_nodes.isEmpty()) {
            // find new end customer
            ArrayList<Double> probabilities = new ArrayList<Double>();
            ArrayList<Integer> indices_for_probabilities = new ArrayList<Integer>();
            for (int i = 0; i < percentage_of_considered_edges * pheromones.length; i++) {
                int current_end = (int) edges_ordered_by_length_for_each_start_node.get(start).get(i);
                if (not_visited_nodes.contains(current_end)) {
                    probabilities.add(prob_of_edge(start, current_end, path));
                    indices_for_probabilities.add(current_end);
                }
            }
            int end = 0;
            if (!probabilities.isEmpty()) {
                end = indices_for_probabilities.get(find_index_from_probabilities(probabilities));
            } else {
                for (int i = (int) percentage_of_considered_edges * pheromones.length; i < pheromones.length; i++) {
                    int current_end = (int) edges_ordered_by_length_for_each_start_node.get(start).get(i);
                    if (not_visited_nodes.contains(current_end)) {
                        end = current_end;
                        break;
                    }
                }
            }
            path.add(end);
            not_visited_nodes.remove(end);
            start = end;
        }
        return path;
    }

    public double evaluate(ArrayList<Object> solution) {
        return tsp.evaluate_solution(Algorithms.convert_from_object_list(solution));
    }

    public void initialize_distance_matrix() {
        distance_matrix = tsp.distance_matrix.clone();
    }

    public void initialize_pheromon(ArrayList<Object> start_solution) {
        pheromones = new double[start_solution.size()][start_solution.size()];
        for (int i = 0; i < tsp.nr_vertices; i++) {
            for (int j = 0; j < tsp.nr_vertices; j++) {
                if (i != j) {
                    pheromones[i][j] = 1.0;
                }
            }
        }
        for (int i = 0; i < start_solution.size() - 1; i++) {
            pheromones[(int) start_solution.get(i)][(int) start_solution.get(i + 1)] = pheromone_level_start_solution;
        }
        pheromones[(int) start_solution.get(start_solution.size() - 1)][(int) start_solution.get(0)] = pheromone_level_start_solution;
    }

    public ArrayList<Object> start_solution() {
        if (start_solution_type == 0) {
            ArrayList<Integer> nearest_neighbor_solution = Heuristics.nearest_neighbor_tsp(tsp, new ArrayList<Integer>());
            ArrayList<Object> start_solution = new ArrayList<Object>();
            start_solution.addAll(nearest_neighbor_solution);
            return start_solution;
        }

        ArrayList<Integer> unused_nodes = new ArrayList<Integer>();
        for (int i = 0; i < tsp.nr_vertices; i++) {
            unused_nodes.add(i);
        }
        ArrayList<Object> start_solution = new ArrayList<Object>();
        while (!unused_nodes.isEmpty()) {
            start_solution.add(unused_nodes.remove(setting.r.nextInt(unused_nodes.size())));
        }
        return start_solution;
    }

    public boolean update_optimal_solution_value(ArrayList<Object> solution, double value) {
        if (tsp.update_optimal_solution(Algorithms.convert_from_object_list(solution), value)) {
            nr_improvements++;
            return true;
        }
        return false;
    }

}
