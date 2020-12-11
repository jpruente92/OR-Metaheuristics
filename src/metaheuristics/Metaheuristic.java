package metaheuristics;

import settings.Setting;

public class Metaheuristic {

    int start_solution_type = 0;
    Setting setting;

    // stats
    int nr_runs = 0;
    int nr_iterations = 0;
    int nr_improvements = 0;
    int highest_iteration_with_improvement=0;
    
    public void print_stats() {
        System.out.println("highest iteration with improvement:\t" + highest_iteration_with_improvement);
        System.out.println("# iterations:\t" + nr_iterations);
        System.out.println("# runs:\t" + nr_runs);
        System.out.println("# improvements:\t" + nr_improvements);
        System.out.println();
    }

}
