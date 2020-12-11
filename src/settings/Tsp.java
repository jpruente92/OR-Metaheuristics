package settings;

import java.util.ArrayList;
import java.util.Random;

public class Tsp extends Setting {
	
	
	
	public int nr_vertices;
	public double[][] distance_matrix;
	public double[][] coordinates_nodes;
	
	
	
	
	
	
	
	public Tsp clone() {
		Tsp clone = new Tsp();
                clone.seed=seed;
		clone.nr_vertices=nr_vertices;
		clone.distance_matrix=distance_matrix.clone();
		clone.coordinates_nodes=coordinates_nodes.clone();
		clone.best_solution=(ArrayList<Integer>) best_solution.clone();
		clone.best_solution_value= best_solution_value;
		return clone;
	}
	
	public void create_new_setting(int nr_vertices, int time_limit_seconds, int seed) {
		this.time_limit_seconds=time_limit_seconds;
		this.r=new Random(seed);
		this.seed = seed;
		this.nr_vertices=nr_vertices;
		
		coordinates_nodes=new double[nr_vertices][2];
		for (int i = 0; i < coordinates_nodes.length; i++) {
			coordinates_nodes[i][0]=r.nextDouble()*1000;
			coordinates_nodes[i][1]=r.nextDouble()*1000;
		}
		
		distance_matrix= new double[nr_vertices][nr_vertices];
		for (int i = 0; i < coordinates_nodes.length; i++) {
			for (int j = i+1; j < coordinates_nodes.length; j++) {
				distance_matrix[i][j]=Math.sqrt(Math.pow(coordinates_nodes[i][0]-coordinates_nodes[j][0], 2)+Math.pow(coordinates_nodes[i][1]-coordinates_nodes[j][1], 2));
				distance_matrix[j][i]=distance_matrix[i][j];
			}
		}
		
		reset_solution();
	}
	
	
	
	public double evaluate_solution(ArrayList<Integer> tour) {
            assert tour.size()>0;
		double cost =0.0;
		for (int i = 0; i < tour.size()-1; i++) {
			cost+=distance_matrix[tour.get(i)][tour.get(i+1)];
		}
		cost+=distance_matrix[tour.get(tour.size()-1)][tour.get(0)];
		return cost;
	}
	
	public void print_solution() {
		System.out.print("Best solution :\t");
		for (int i: best_solution) {
			System.out.print(i+" ");
		}
		System.out.println();
		System.out.println();
		System.out.println("Best solution value:\t"+best_solution_value);
		System.out.println();
	}
	
	public void reset_solution() {
            r=new Random(seed);
		best_solution = new ArrayList<Integer>();
		best_solution_value = Double.MAX_VALUE;
	}
	
	public boolean solution_feasible(ArrayList<Integer> tour) {
		if(tour.size()!=nr_vertices) {
			return false;
		}
		for (int i = 0; i < nr_vertices; i++) {
			if(!tour.contains(i)) {
				return false;
			}
		}
		return true;
	}

	public boolean update_optimal_solution(ArrayList<Integer> act_path, double act_length) {
		assert solution_feasible(act_path);
		if(act_length<best_solution_value) {
			best_solution_value=act_length;
			best_solution=(ArrayList<Integer>) act_path.clone();
			return true;
		}
		return false;
	}

}
