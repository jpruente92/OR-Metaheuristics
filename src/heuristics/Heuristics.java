package heuristics;

import java.util.ArrayList;
import settings.Setting;

import settings.Tsp;

public class Heuristics {
	
	
	public static void nearest_neighbor_all_possible_start_nodes_tsp(Tsp setting) {
		for (int i = 0; i < setting.nr_vertices; i++) {
			ArrayList<Integer> subtour = new ArrayList<Integer>();
			subtour.add(i);
			ArrayList<Integer> actual_tour = Heuristics.nearest_neighbor_tsp(setting, subtour);
			setting.update_optimal_solution(actual_tour, setting.evaluate_solution(actual_tour));
		}
	}

	public static void nearest_neighbor_tsp(Tsp setting) {
		ArrayList<Integer> tour = Heuristics.nearest_neighbor_tsp(setting, new ArrayList<Integer>());
		setting.update_optimal_solution(tour, setting.evaluate_solution(tour));
	}
	
	public static ArrayList<Integer> nearest_neighbor_tsp(Tsp setting,ArrayList<Integer> subtour) {
		ArrayList<Integer> tour = (ArrayList<Integer>) subtour.clone();
		ArrayList<Integer> unused_nodes = new ArrayList<Integer>();
		for (int i = 0; i < setting.nr_vertices; i++) {
			unused_nodes.add(i);
		}
		unused_nodes.removeAll(tour);
		int start = 0;
		if(!subtour.isEmpty()) {
			 start =tour.get(tour.size()-1);
		}
		else {
			tour.add(0);
			unused_nodes.remove(0);
		}
		while(!unused_nodes.isEmpty()) {
			int index_nearest=0;
			double dist_nearest=Double.MAX_VALUE;
			for (int i = 0; i < unused_nodes.size(); i++) {
				double actual_distance = setting.distance_matrix[start][unused_nodes.get(i)];
				if(actual_distance<dist_nearest) {
					index_nearest=i;
					dist_nearest=actual_distance;
				}
			}
			start = unused_nodes.remove(index_nearest);
			tour.add(start);
		}
		return tour;
	}
}
