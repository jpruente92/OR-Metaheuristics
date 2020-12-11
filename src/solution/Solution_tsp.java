/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solution;

import java.util.ArrayList;

/**
 *
 * @author Jonas
 */
public class Solution_tsp extends Solution{

    public ArrayList<Integer> tour;

    public Solution_tsp(ArrayList<Integer> tour) {
        this.tour = tour;
    }

    public String toString(){
        String s = new String();
        for (Integer integer : tour) {
            s+=integer+".";
        }
        return s;
    }
}
