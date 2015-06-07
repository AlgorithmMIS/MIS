package EWLS;
//package EWLS;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Graph {
	HashMap<Integer,HashSet<edge>>adTable;
	HashMap<Integer,HashSet<Integer>>adis=new HashMap<Integer,HashSet<Integer>>();
//	ArrayList<HashSet<edge>> adTable;
	HashSet<Integer> orivs = new HashSet<Integer>();
	edges oriedges = new edges();
	int numberOfVertex;
	public Graph() {
		System.out.println("Hello");
		this.adTable = new HashMap<Integer,HashSet<edge>>();
		File file = new File("src/frb100-40.mis");
		String line = null;
		edge temp;
		String[] strs;
		try {
			Scanner br = new Scanner(file);
			if (br.hasNextLine())
				line = br.nextLine();
			strs = line.split(" ");
			this.numberOfVertex = Integer.valueOf(strs[2]);
			for(int i=0;i<numberOfVertex;i++){
				adTable.put(i, new HashSet<edge>());
				adis.put(i, new HashSet<Integer>());
			//	adTable.add(new HashSet<edge>());
			}
			while (br.hasNextLine()) {
				line = br.nextLine();
				strs = line.split(" ");
				int v1 = Integer.valueOf(strs[1]) - 1;
				int v2 = Integer.valueOf(strs[2]) - 1;
				orivs.add(v1);
				orivs.add(v2);
				temp = new edge(v1, v2);
				//weigth.put(temp, 1);// 初始权重
				oriedges.add(temp);
				adis.get(v1).add(v2);
				adis.get(v2).add(v1);
				adTable.get(v1).add(temp);
				adTable.get(v2).add(temp);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

