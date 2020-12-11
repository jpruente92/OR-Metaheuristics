package metaheuristics;

import java.util.ArrayList;

import heuristics.Heuristics;
import misc.Algorithms;
import settings.Tsp;
import solution.Solution;
import solution.Solution_tsp;

public class Tabu_search_tsp extends Tabu_search {
    
        Tsp tsp;
	
	public Tabu_search_tsp(int size_tabu_list,int max_nr_iterations_without_improvement, Tsp setting) {
		this.setting=setting;
                tsp=setting;
		this.size_tabu_list=size_tabu_list;
		this.max_nr_iterations_without_improvement=max_nr_iterations_without_improvement;
	}
	
	public double evaluate (Solution solution) {
            Solution_tsp solution_tsp = (Solution_tsp) solution;
//            assert solution_tsp.tour.size()>0;
		return setting.evaluate_solution(solution_tsp.tour);
	}
	
	public Solution start_solution(){
		if(start_solution_type==0) {
			ArrayList<Integer> subtour = new ArrayList<Integer>();
			subtour.add(setting.r.nextInt(tsp.nr_vertices));
			ArrayList<Integer> tour = Heuristics.nearest_neighbor_tsp(tsp,subtour);
//                        assert tour.size()>0;
			return new Solution_tsp(tour);
		}
		
		ArrayList<Integer> unused_nodes = new ArrayList<Integer>();
		for (int i = 0; i < tsp.nr_vertices; i++) {
			unused_nodes.add(i);
		}
		ArrayList<Integer> tour = new ArrayList<Integer>();
		while(!unused_nodes.isEmpty()) {
			tour.add(unused_nodes.remove(setting.r.nextInt(unused_nodes.size())));
		}
		return new Solution_tsp(tour);
	}
	
	public boolean update_optimal_solution_value(Solution solution, double value) {
            Solution_tsp solution_tsp = (Solution_tsp) solution;
		if(setting.update_optimal_solution(solution_tsp.tour, value)) {
			nr_improvements++;
			return true;
		}
		return false;
	}
	
	public Solution best_neighbor(Solution act_solution){
		ArrayList<Integer> best_tour = new ArrayList<Integer>();
                double best_value= Double.MAX_VALUE;
                Solution_tsp solution_tsp = (Solution_tsp) act_solution;
                ArrayList<Integer> act_tour = solution_tsp.tour;
		
               
		for (int i =0;i<act_tour.size();i++) {
			ArrayList<Integer> clone = (ArrayList<Integer>) act_tour.clone();
			Integer customer_to_change =clone.remove(i);
			for (int j = 0; j < clone.size(); j++) {
				ArrayList<Integer> current_tour =(ArrayList<Integer>) clone.clone();
				current_tour.add(j, customer_to_change);
				if(elements_in_tabu_list.contains(list_to_String(current_tour))) {
					continue;
				}
				double current_value=setting.evaluate_solution(current_tour);
				if(current_value<best_value) {
					best_value=current_value;
					best_tour=current_tour;
				}
			}
		}
//		assert best_tour.size()>0;
		return new Solution_tsp(best_tour);
	}
        
        public String list_to_String(ArrayList<Integer> tour){
        String s = new String();
        for (Integer integer : tour) {
            s+=integer+".";
        }
        return s;
    }

}
