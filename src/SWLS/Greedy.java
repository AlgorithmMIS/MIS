//package greedyandmaximal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Greedy {
	static HashSet<Integer> CurrentVertexCover;// c
	static HashSet<Integer> RemainVertex;// 剩余的点
	static HashSet<Integer> VertexCover;// c'
	static edges LIST;// L
	static edges UncheckedList;// UL
	static Graph graph;
	static int tabuAdd;
	static int tabuRemove;
	static int step = 0;
	static int ub;
	static final int delta = 90;
	static final int finalstep = 2;
	static HashMap<Integer, Integer> dscores = new HashMap<Integer, Integer>();
	static List<Map.Entry<Integer, Integer>> dscores_value;

	public static void addAnVetex(int v)// 单纯的向C中加一个点。
	{
		ArrayList<edge> temp = new ArrayList<edge>(graph.adTable.get(v));
		CurrentVertexCover.add(v);
		RemainVertex.remove(v);
		for (edge t : temp) {
			LIST.remove(t);
			UncheckedList.remove(t);
		}
	}

	public static void delAnVetex(int v) {
		ArrayList<edge> temp = new ArrayList<edge>(graph.adTable.get(v));
		CurrentVertexCover.remove(v);
		RemainVertex.add(v);
		for (edge t : temp) {
			if (RemainVertex.contains(t.x) && RemainVertex.contains(t.y)) {
				LIST.add(t);
				UncheckedList.add(t);
			}

		}
	}

	public static void sort() {// 将dscores_value进行排序
		dscores_value = new ArrayList<Map.Entry<Integer, Integer>>(
				dscores.entrySet());
		Collections.sort(dscores_value,
				new Comparator<Map.Entry<Integer, Integer>>() {
					public int compare(Map.Entry<Integer, Integer> o1,
							Map.Entry<Integer, Integer> o2) {
						return -(o1.getValue() - o2.getValue());// 从大到小排序；
					}
				});
	}

	public static void ini_dscores() {
		for (Integer t : graph.adTable.keySet()) {
			dscores.put(t, graph.adTable.get(t).size());
		}
		sort();// 对初始的ini_dscores进行排序；
	}

	public static void update_dscores() {
		for (Integer t : dscores.keySet()) {
			dscores.put(t, compute_dscore(t));// 计算新的dscores
		}
		sort();// 对新的dscores进行排序。
	}

	public static int compute_dscore(int v) {
		int cost = 0;
		ArrayList<edge> temp = new ArrayList<edge>(graph.adTable.get(v));// 找出与该点相连的边来；
		if (CurrentVertexCover.contains(v))// 包含在选择点中，此时COST小于0
		{
			for (edge t : temp) {
				if (t.x == v) {
					if (RemainVertex.contains(t.y))// 舍弃之后将不再不该这条边。
						cost -= graph.weigth.get(t);// 获得这掉边的权重
				} else {
					if (RemainVertex.contains(t.x))
						cost -= graph.weigth.get(t);
				}
			}
		} else {// 不再选择的点里，此时的COST是大于0
			for (edge t : temp) {
				if (t.x == v) {
					if (RemainVertex.contains(t.y))
						cost += graph.weigth.get(t);
				} else {
					if (RemainVertex.contains(t.x))
						cost += graph.weigth.get(t);
				}
			}
		}
		return cost;
	}

	public static void initial() {
		ini_dscores();// 求初始的dscores()
		RemainVertex = new HashSet<Integer>(graph.orivs);
		LIST = new edges(graph.oriedges);
		UncheckedList = new edges(graph.oriedges);
		CurrentVertexCover = new HashSet<Integer>();
		for (Entry<Integer, Integer> tmp : dscores_value) {
			addAnVetex(tmp.getKey());
			if (LIST.isEmpty()) {
				VertexCover = new HashSet<Integer>(CurrentVertexCover);// 初始化C‘
				ub = CurrentVertexCover.size();// 初始化开始时候的界ub
				break;
			}
		}
	}

	public static edge serch(int x, int y) {// 寻找某条边。
		ArrayList<edge> temp = new ArrayList<edge>(graph.adTable.get(x));
		for (edge t : temp) {
			// System.out.println(t.x + " " + t.y);
			if (t.x == x && t.y == y || t.y == x && t.x == y)
				return t;
		}
		return null;
	}

	public static int compute_score(int u, int v) {
		edge temp = serch(u, v);
		if (temp == null)
			return dscores.get(u) + dscores.get(v);
		return dscores.get(u) + dscores.get(v) + graph.weigth.get(temp);
	}

	public static edge ChooseSwapPair() {// 这里先只实现一半
		while (!UncheckedList.isEmpty()) {
			edge temp = UncheckedList.getFirst();
			for (Integer t : CurrentVertexCover) {
				if (t != tabuRemove && temp.x != tabuAdd) {
					if (compute_score(t, temp.x) > 0)
						return new edge(t, temp.x);
				}
				if (t != tabuRemove && temp.y != tabuAdd) {
					if (compute_score(t, temp.y) > 0)
						return new edge(t, temp.y);
				}
			}
		}
		return new edge(0, 0);
	}

	public static edge Random() {
		List<Integer> list = new ArrayList<Integer>(CurrentVertexCover);
		List<edge> uList = LIST.getEdges();
		int pos = (Math.abs(new java.util.Random().nextInt())) % list.size();
		int posy = (Math.abs(new java.util.Random().nextInt())) % uList.size();
		int x = list.get(pos / 2);
		edge y = uList.get(posy / 2);
		if (pos % 2 == 0) {
			return new edge(x, y.x);
		}
		return new edge(x, y.y);
	}

	public static HashSet<Integer> greedyLittle() {
		update_dscores();// 更新所有的呢。
		HashSet<Integer> Cplus = new HashSet<Integer>();
		HashMap<Integer, Integer> temp_dscore = new HashMap<Integer, Integer>();
		for (Map.Entry<Integer, Integer> t : dscores_value) {
			if (RemainVertex.contains(t.getKey()))
				temp_dscore.put(t.getKey(), t.getValue());
		}// 构造只属于剩余点的dscores的map。
		for (Integer t : temp_dscore.keySet()) {
			ArrayList<edge> temp = new ArrayList<edge>(graph.adTable.get(t));
			Cplus.add(t);
			RemainVertex.remove(t);
			for (edge myedge : temp) {
				LIST.remove(myedge);
				UncheckedList.remove(myedge);
			}
			if (LIST.isEmpty())
				break;
		}
		return Cplus;
	}

	public static void main(String[] args) {
		graph = new Graph();
		initial();// 初始化；
		System.out.println("hello" + CurrentVertexCover.size());
		// line 7:
		int times = 0;
		for (Entry<Integer, Integer> tmp : dscores_value) {
			delAnVetex(tmp.getKey());
			times++;
			if (times == delta) {
				ub = ub - delta;
				break;
			}
		}
		System.out.println(CurrentVertexCover.size());
		while (step < finalstep) {
			edge swapvetexs = ChooseSwapPair();
			System.out.println("choose:" + swapvetexs.x + " " + swapvetexs.y);
			if (!(swapvetexs.x == 0 && swapvetexs.y == 0)) {
				delAnVetex(swapvetexs.x);
				addAnVetex(swapvetexs.y);
			}// 寻找可以交换的对
			else {
				for (edge t : LIST.getEdges()) {
					graph.weigth.put(t, graph.weigth.get(t) + 1);// 对应的权重加1
				}
				// 此时我们要把UL重新便会L
				UncheckedList.getEdges().clear();
				UncheckedList.getEdges().addAll(LIST.getEdges());
				swapvetexs = Random();
				delAnVetex(swapvetexs.x);
				addAnVetex(swapvetexs.y);
			}// 把随机找出来的交换了
			tabuAdd = swapvetexs.x;
			tabuRemove = swapvetexs.y;
			if (CurrentVertexCover.size() + LIST.size() < ub) {
				ub = CurrentVertexCover.size() + LIST.size();
				if (LIST.isEmpty()) {
					VertexCover.clear();
					VertexCover.addAll(CurrentVertexCover);
				} else {
					VertexCover.addAll(CurrentVertexCover);
					VertexCover.addAll(greedyLittle());
				}
			}
			// 将从C中取出一些元素来缩小规模delta
			update_dscores();
			for (Map.Entry<Integer, Integer> temp : dscores_value) {
				if (CurrentVertexCover.size() == ub - delta)
					break;
				if (CurrentVertexCover.contains(temp.getKey())) {
					delAnVetex(temp.getKey());
				}
			}
			step++;
		}
		System.out.println(VertexCover.size());
	}
}
