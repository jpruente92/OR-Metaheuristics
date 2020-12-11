import java.util.ArrayList;
import java.util.Random;

import heuristics.Heuristics;
import metaheuristics.Ant_colony;
import metaheuristics.Ant_colony_tsp;
import metaheuristics.Simulated_annealing;
import metaheuristics.Simulated_annealing_tsp;
import metaheuristics.Tabu_search;
import metaheuristics.Tabu_search_tsp;
import settings.Setting;
import settings.Tsp;

public class Main_metaheuristics {

	public static void main(String[] args) {
		
		// parameters that can be adjusted
		int number_stops=50;
		int seed =1706;
		int timelimit= 30;

		// create a new TSP instance
		Tsp setting = new Tsp();
		setting.create_new_setting(number_stops, timelimit, seed);



		// Comparison of different heuristics
		Ant_colony ant_colony;
		Simulated_annealing sa;
		double exploitation_factor;
		Tabu_search ts;
		long time;

		System.out.println("Nearest Neighbor:");
		time=System.currentTimeMillis();
		Heuristics.nearest_neighbor_tsp(setting);
		System.out.println("Time:\t"+(System.currentTimeMillis()-time)+" milliseconds");

		setting.print_solution();


		setting.reset_solution();

		System.out.println("Nearest Neighbor all starts:");
		time=System.currentTimeMillis();
		Heuristics.nearest_neighbor_all_possible_start_nodes_tsp(setting);
		System.out.println("Time:\t"+(System.currentTimeMillis()-time)+" milliseconds");
		setting.print_solution();


		setting.reset_solution();

		System.out.println("Tabu Search:");
		time=System.currentTimeMillis();
		ts= new Tabu_search_tsp(100,50, setting);
		ts.solve();
		System.out.println("Time:\t"+(System.currentTimeMillis()-time)/1000+" seconds");
		ts.print_stats();
		setting.print_solution();


		setting.reset_solution();

		exploitation_factor = 0.5;
		System.out.println("Simulated Annealing with exploitation factor "+exploitation_factor+":");
		time=System.currentTimeMillis();
		sa = new Simulated_annealing_tsp(10000.0,exploitation_factor, setting);
		sa.solve();
		System.out.println("Time:\t"+(System.currentTimeMillis()-time)/1000+" seconds");
		sa.print_stats();
		setting.print_solution();
		
		setting.reset_solution();

		System.out.println("Ant Colony Algorithm v1:");
		ant_colony = new Ant_colony_tsp(25,5,10, 5.0, 1.0,0.5, 0.1, 0.1, 2.0, 1, setting);
		time=System.currentTimeMillis();
		ant_colony.solve();
		System.out.println("Time:\t"+(System.currentTimeMillis()-time)/1000+" seconds");
		ant_colony.print_stats();
		setting.print_solution();

	}

}
