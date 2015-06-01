package ant;
import java.io.*;
import java.util.*;
public class Graph {
	private HashMap<Integer, HashSet<Integer>> adTable;
	public ArrayList<Item> adList = new ArrayList<Item>(); // 按度排序的邻接表
	int numberOfVertex;

	// public HashMap<Integer, HashSet<Integer>> mydus = new HashMap<Integer,
	// HashSet<Integer>>();

	public Graph() {
		this.adTable = new HashMap<Integer, HashSet<Integer>>();
		File file = new File("src/frb30-15-4.mis");
		String line;
		String[] strs;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			line = br.readLine();
			strs = line.split(" ");
			this.numberOfVertex = Integer.valueOf(strs[2]);
			while ((line = br.readLine()) != null) {
				strs = line.split(" ");
				this.addEdge(Integer.valueOf(strs[1]) - 1,
						Integer.valueOf(strs[2]) - 1);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i=0;i<adTable.size();i++) {
			Item it=new Item(i, this.adTable.get(i));
			it.G=(double)1/this.adTable.get(i).size();
			this.adList.add(it);
		}
		//Collections.sort(adList, new Item());
		/*
		 * for (Item t : adList) { if (!mydus.containsKey(t.list.size()))
		 * mydus.put(t.list.size(), new HashSet<Integer>());
		 * mydus.get(t.list.size()).add(t.vertex); }
		 */
	}
	public ArrayList<Item > cloneList(){
		ArrayList<Item> res= new ArrayList<Item>();
		res.addAll(this.adList);
		return res;
	}
	public void shuffle(){
		Collections.shuffle(adList);
	}
	private void addEdge(int v1, int v2) {
		if (!this.adTable.containsKey(v1))
			this.adTable.put(v1, new HashSet<Integer>());
		if (!this.adTable.containsKey(v2))
			this.adTable.put(v2, new HashSet<Integer>());
		this.adTable.get(v1).add(v2);
		this.adTable.get(v2).add(v1);
	}
}

class Item implements Comparator<Item> {
	static final double vanishSpeed=0.95;
	int vertex;
	HashSet<Integer> list;
	double S=10;//信息素强度
	double G;//启发信息,用度衡量
	Item(int i, HashSet<Integer> l) {
		this.vertex = i;
		this.list = l;
	}

	Item() {
	}

	public void updateStrength(int max,int latestMax){
		this.S=1.0/(1+max-latestMax);
		if(S>10)
			S=10;
	}
	public void vanish(){
		this.S=S*vanishSpeed;
	}
	public int compare(Item arg0, Item arg1) {
		return Integer.compare(arg0.list.size(), arg1.list.size());
	}
}
