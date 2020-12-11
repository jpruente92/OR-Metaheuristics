package misc;

import java.util.ArrayList;

public class Algorithms {

	public static ArrayList<Integer> convert_from_object_list(ArrayList<Object> list){
		ArrayList<Integer> output = new ArrayList<Integer>();
		for (Object o: list) {
			if(o instanceof Integer) {
				output.add((Integer)o);
			}
		}
		return output;
	}
}
