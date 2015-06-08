package ewls;

import java.io.*;
import java.util.*;

public class Graph {
	ArrayList<Integer>[] adTable;
	ArrayList<Edge> edges = new ArrayList<>();
	int nv;

	public ArrayList<Integer> neighbors(int v) {
		return adTable[v];
	}

	public int degree(int v) {
		return adTable[v].size();
	}

	@SuppressWarnings("unchecked")
	public Graph(File file) throws FileNotFoundException {
		// System.out.println("Hello");
		Scanner br = new Scanner(file);
		String line = br.nextLine();
		String[] strs = line.split(" ");
		nv = Integer.parseInt(strs[2]);
		adTable = new ArrayList[nv];
		for (int i = 0; i < nv; i++)
			adTable[i] = new ArrayList<>();
		while (br.hasNextLine()) {
			line = br.nextLine();
			strs = line.split(" ");
			int v1 = Integer.parseInt(strs[1]) - 1;
			int v2 = Integer.parseInt(strs[2]) - 1;
			// weigth.put(temp, 1);// 初始权重
			Edge e = new Edge(v1, v2);
			edges.add(e);
			adTable[v1].add(v2);
			adTable[v2].add(v1);
		}
		br.close();
	}
}
