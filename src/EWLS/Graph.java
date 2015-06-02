package EWLS;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Graph {
	HashMap<Integer, HashSet<edge>> adTable;
	HashSet<Integer> orivs = new HashSet<Integer>();
	edges oriedges = new edges();
	int numberOfVertex;
	HashMap<edge, Integer> weigth = new HashMap<edge, Integer>();

	public Graph() {
		this.adTable = new HashMap<Integer, HashSet<edge>>();
		File file = new File("src/frb.mis");
		String line = null;
		edge temp;
		String[] strs;
		try {
			Scanner br = new Scanner(file);
			if (br.hasNextLine())
				line = br.nextLine();
			strs = line.split(" ");
			this.numberOfVertex = Integer.valueOf(strs[2]);
			while (br.hasNextLine()) {
				line = br.nextLine();
				strs = line.split(" ");
				int v1 = Integer.valueOf(strs[1]) - 1;
				int v2 = Integer.valueOf(strs[2]) - 1;
				orivs.add(v1);
				orivs.add(v2);
				temp = new edge(v1, v2);
				weigth.put(temp, 1);// 初始权重
				oriedges.add(temp);
				if (!adTable.containsKey(v1)) {
					adTable.put(v1, new HashSet<edge>());
				}
				if (!adTable.containsKey(v2)) {
					adTable.put(v2, new HashSet<edge>());
				}
				adTable.get(v1).add(temp);
				adTable.get(v2).add(temp);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
