package metaheuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import heuristics.Heuristics;
import settings.Tsp;

public abstract class Ant_colony extends Metaheuristic {

    int nr_ants;
    int nr_ants_spreading_pheromone;

    double[][] distance_matrix;
    double[][] pheromones;
    ArrayList<ArrayList<Object>> edges_ordered_by_length_for_each_start_node;
    int nr_vertices;

    int max_nr_iterations_without_improvement;
    double q;
    double alpha; // greater equal 0
    double beta; // greater equal 1
    double pheromone_vanish_factor; // between 0 and 1
    double percentage_of_considered_edges;

    double pheromone_level_start_solution;
    int attractiveness_heuristic;

    public void solve() {
        long endtime = System.currentTimeMillis() + 1000 * setting.time_limit_seconds;
        compute_sorted_edge_lists();

        while (System.currentTimeMillis() < endtime) {
            ArrayList<Object> start_solution = start_solution();
            initialize_pheromon(start_solution);
            update_optimal_solution_value(start_solution, evaluate(start_solution));
            double cnt = 0.0;
            while (cnt < max_nr_iterations_without_improvement) {
                if(System.currentTimeMillis()>endtime){
                    break;
                }
                cnt++;
                ArrayList<ArrayList<Object>> new_paths = new ArrayList<ArrayList<Object>>();
                ArrayList<Double> length_of_new_paths = new ArrayList<Double>();
                for (int i = 0; i < nr_ants; i++) {
                    ArrayList<Object> act_path = compute_path_for_ant();
                    double act_length = evaluate(act_path);
                    if(update_optimal_solution_value(act_path, act_length)){
                        if(cnt>highest_iteration_with_improvement){
                            highest_iteration_with_improvement=(int) cnt;
                        }
                        cnt=0.0;
                    }
                    new_paths.add(act_path);
                    length_of_new_paths.add(act_length);
                }
                update_pheromon(new_paths, length_of_new_paths);
                nr_iterations++;
            }
            nr_runs++;
        }
    }

    private void compute_sorted_edge_lists() {
        edges_ordered_by_length_for_each_start_node = new ArrayList<ArrayList<Object>>();
        for (int i = 0; i < nr_vertices; i++) {
            double[][] indices_and_edge_length = new double[nr_vertices - 1][2];
            int cnt = 0;
            for (int j = 0; j < nr_vertices; j++) {
                if (i != j) {
                    indices_and_edge_length[cnt][0] = distance_matrix[i][j];
                    indices_and_edge_length[cnt][1] = j;
                    cnt++;
                }
            }
            // sort indices by edge_length
            Arrays.sort(indices_and_edge_length, Comparator.comparingDouble(o -> o[0]));
            ArrayList<Object> list_for_node_i = new ArrayList<Object>();
            for (int j = 0; j < nr_vertices - 1; j++) {
                list_for_node_i.add((int) indices_and_edge_length[j][1]);
            }
            edges_ordered_by_length_for_each_start_node.add(list_for_node_i);
        }
    }

    public int find_index_from_probabilities(ArrayList<Double> probabilities) {
        double sum = 0.0;
        for (double d : probabilities) {
            sum += d;
        }
        double sample = setting.r.nextDouble() * sum;
        sum = 0.0;
        for (int i = 0; i < probabilities.size(); i++) {
            sum += probabilities.get(i);
            if (sample < sum) {
                return i;
            }
        }
        return probabilities.size() - 1;
    }

    public double prob_of_edge(int start, int end, ArrayList<Object> subtour) {
        double mu = compute_attractiveness(start, end, subtour);
        double tau = pheromones[start][end];
        if (tau == 0.0) {
            return 0.0;
        }
        double divisor = 0.0;
        for (int i = 0; i < nr_vertices; i++) {
            for (int j = 0; j < nr_vertices; j++) {
                if (i != j) {
                    divisor += Math.pow(compute_attractiveness(i, j, subtour), beta) * Math.pow(pheromones[i][j], alpha);
                }
            }
        }
        return Math.pow(mu, beta) * Math.pow(tau, alpha) / divisor;
    }

    public void update_pheromon(ArrayList<ArrayList<Object>> new_paths, ArrayList<Double> length_of_new_paths) {
        // simulate vanishing pheromone
        for (int i = 0; i < pheromones.length; i++) {
            for (int j = 0; j < pheromones[i].length; j++) {
                pheromones[i][j] *= pheromone_vanish_factor;
            }
        }
        ArrayList<ArrayList<Object>> new_paths_plus_length = new ArrayList<ArrayList<Object>>();
        for (int i = 0; i < new_paths.size(); i++) {
            ArrayList<Object> next_element = (ArrayList<Object>) new_paths.get(i).clone();
            next_element.add(length_of_new_paths.get(i));
            new_paths_plus_length.add(next_element);
        }
        // sort by length
        Collections.sort(new_paths_plus_length, new Comparator<ArrayList<Object>>() {
            @Override
            public int compare(ArrayList<Object> first, ArrayList<Object> second) {
                return Double.compare((double) first.get(first.size() - 1), (double) second.get(second.size() - 1));
            }
        });
        // add new pheromone from last run
        for (int i = 0; i < nr_ants_spreading_pheromone; i++) {
            ArrayList<Object> path = new_paths_plus_length.get(i);
            path.remove(path.size() - 1);
            for (int j = 0; j < path.size() - 1; j++) {
                pheromones[(int) path.get(j)][(int) path.get(j + 1)] += q / length_of_new_paths.get(i);
            }
            pheromones[(int) path.get(path.size() - 1)][(int) path.get(0)] += q / length_of_new_paths.get(i);
        }

    }

    // abstract methods 
    public abstract double compute_attractiveness(int start, int end, ArrayList<Object> subtour);

    public abstract ArrayList<Object> compute_path_for_ant();

    public abstract double evaluate(ArrayList<Object> solution);

    public abstract void initialize_distance_matrix();

    public abstract void initialize_pheromon(ArrayList<Object> start_solution);

    public abstract ArrayList<Object> start_solution();

    public abstract boolean update_optimal_solution_value(ArrayList<Object> solution, double value);

}
