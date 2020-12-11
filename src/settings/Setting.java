/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import java.util.ArrayList;
import java.util.Random;


/**
 *
 * @author Jonas
 */
public abstract class Setting {
    
    public int seed;
    public Random r;
    public int time_limit_seconds;
    
    public ArrayList<Integer> best_solution;
    public double best_solution_value;
    
    public abstract  double evaluate_solution(ArrayList<Integer> tour) ;
	
    public abstract void print_solution() ;
	
    public abstract void reset_solution();
	
    public abstract boolean solution_feasible(ArrayList<Integer> tour) ;

    public abstract boolean update_optimal_solution(ArrayList<Integer> act_path, double act_length) ;
    
}
