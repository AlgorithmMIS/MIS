import java.util.HashSet;

public class Graph {
	private HashMap<Integer, HashSet<Integer>> adTable;
	public ArrayList<Item> adList = new ArrayList<Item>(); // 按度排序的邻接表
	int numberOfVertex;

	// public HashMap<Integer, HashSet<Integer>> mydus = new HashMap<Integer,
	// HashSet<Integer>>();

	public Graph() {
		this.adTable = new HashMap<Integer, HashSet<Integer>>();
		File file = new File("src/frb.mis");
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
		for (Integer i : adTable.keySet()) {
			this.adList.add(new Item(i, this.adTable.get(i)));
		}
		Collections.sort(adList, new Item());
		/*
		 * for (Item t : adList) { if (!mydus.containsKey(t.list.size()))
		 * mydus.put(t.list.size(), new HashSet<Integer>());
		 * mydus.get(t.list.size()).add(t.vertex); }
		 */
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
	int vertex;
	HashSet<Integer> list;

	Item(int i, HashSet<Integer> l) {
		this.vertex = i;
		this.list = l;
	}

	Item() {
	}

	public int compare(Item arg0, Item arg1) {
		return Integer.compare(arg0.list.size(), arg1.list.size());
	}
}
