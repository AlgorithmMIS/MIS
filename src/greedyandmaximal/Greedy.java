package greedyandmaximal;

import java.util.HashSet;

public class Greedy {
	static HashSet<Integer> maximalSet;
	static Graph graph;
	static HashSet<Integer> temp;

	public static void handle(int i) {
		temp.clear();
		temp.addAll(maximalSet);
		temp.retainAll(graph.adList.get(i).list);
		if (temp.isEmpty()) {
			maximalSet.add(graph.adList.get(i).vertex);
		}
	}

	public static void main(String[] args) {
		graph = new Graph();
		maximalSet = new HashSet<Integer>();
		temp = new HashSet<Integer>();
		int max = -1;
		for (int start = 0; start < graph.adList.size() / 3; start++) {
			maximalSet.add(start);
			for (int happy = 0; happy < graph.adList.size(); happy++)
				handle(happy);
			max = max > maximalSet.size() ? max : maximalSet.size();
			maximalSet.clear();
		}
		System.out.println(max);
	}
}
