package metaheuristics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import solution.Solution;


public abstract class Tabu_search extends Metaheuristic{
	
	int size_tabu_list;
	Queue<String> tabu_list= new LinkedList<String>();
	HashSet<String> elements_in_tabu_list = new HashSet<String>();
	double max_nr_iterations_without_improvement =0;
	
	public void solve() {
		long endtime = System.currentTimeMillis()+1000*setting.time_limit_seconds;
		while(System.currentTimeMillis()<endtime) {
			Solution act_solution = start_solution();
			double act_value = evaluate(act_solution);
			update_optimal_solution_value(act_solution,act_value);
			double i=0.0;
			while(i<max_nr_iterations_without_improvement) {
				i++;
				act_solution = best_neighbor(act_solution);
				act_value = evaluate(act_solution);
				update_tabu_list(act_solution);
				if(update_optimal_solution_value(act_solution,act_value)&&i>highest_iteration_with_improvement){
                                    highest_iteration_with_improvement=(int)i;
                                }
				if(System.currentTimeMillis()>endtime) {
					break;
				}
                            nr_iterations++;
			}
			nr_runs++;
		}
	}
	
	

	private void update_tabu_list(Solution act_solution) {
		tabu_list.add(act_solution.toString());
		elements_in_tabu_list.add(act_solution.toString());
		if(tabu_list.size()>size_tabu_list) {
			String removed_object = tabu_list.poll();
			elements_in_tabu_list.remove(removed_object);
		}
	}



	public abstract Solution best_neighbor(Solution act_solution); 



	// abstract methods

	protected abstract double evaluate(Solution act_solution);

	protected abstract Solution start_solution();
	
	protected abstract boolean update_optimal_solution_value(Solution act_solution, double act_value);

}
