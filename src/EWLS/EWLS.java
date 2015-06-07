package EWLS;
//package EWLS;
//package greedyandmaximal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class EWLS {
	static HashSet<Integer> CurrentVertexCover;// c
	static HashSet<Integer> RemainVertex;// 剩余的点
	static HashSet<Integer> VertexCover;// 已经找到的最优解
	static edges LIST;// L
	// static edges UncheckedList;// UL
	static Graph graph;
	static int tabuAdd = -1;
	static int tabuRemove = -1;
	static int step = 0;
	static int ub;
	static final int delta = 1;
	static final int finalstep = 2111;
	static HashMap<Integer, Integer> dscores = new HashMap<Integer, Integer>();
	static List<Map.Entry<Integer, Integer>> dscores_value;
	public static void addAnVetex(int v)// 单纯的向C中加一个点。
	{

		CurrentVertexCover.add(v);
		RemainVertex.remove(v);
		LIST.remove(graph.adTable.get(v));
		// UncheckedList.remove(temp);
	}

	public static void delAnVetex(int v) {
		// ArrayList<edge> temp = new ArrayList<edge>(graph.adTable.get(v));
		CurrentVertexCover.remove(v);
		RemainVertex.add(v);
		for (edge t : graph.adTable.get(v)) {
			if (RemainVertex.contains(t.x) && RemainVertex.contains(t.y)) {
				/*
				 * if(CurrentVertexCover.contains(t.x)||CurrentVertexCover.contains
				 * (t.y)){ //do nothing }else
				 */
				LIST.addFirst(t);// Add first!
				// UncheckedList.add(t);
			}

		}
		// System.out.println("after del "+LIST.isEmpty());
	}

	public static void sort() {// 将dscores_value进行排序
		dscores_value = new ArrayList<Map.Entry<Integer, Integer>>(
				dscores.entrySet());
		Collections.sort(dscores_value, new CMP());
	}

	public static void ini_dscores() {
		for (int t = 0; t < graph.adTable.size(); t++) {
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
		HashSet<edge> temp = graph.adTable.get(v);
		// ArrayList<edge> temp = new ArrayList<edge>(graph.adTable.get(v));//
		// 找出与该点相连的边来；
		if (CurrentVertexCover.contains(v))// 包含在选择点中，此时COST小于0
		{
			for (edge t : temp) {
				if (t.x == v) {
					if (RemainVertex.contains(t.y))// 舍弃之后将不再不该这条边。
						cost -= t.weight;// 获得这掉边的权重
				} else {
					if (RemainVertex.contains(t.x))
						cost -= t.weight;
				}
			}
		} else {// 不再选择的点里，此时的COST是大于0
			for (edge t : temp) {
				if (t.x == v) {
					if (RemainVertex.contains(t.y))
						cost += t.weight;
				} else {
					if (RemainVertex.contains(t.x))
						cost += t.weight;
				}
			}
		}
		return cost;
	}

	public static void initial() {
		ini_dscores();// 求初始的dscores(),after sorting the sorted dscore is stored
						// in dscores_value
		RemainVertex = new HashSet<Integer>(graph.orivs);
		LIST = new edges(graph.oriedges);
		CurrentVertexCover = new HashSet<Integer>();
	
		for (Entry<Integer, Integer> tmp : dscores_value) {
			addAnVetex(tmp.getKey());
			if (LIST.isEmpty()) {
				VertexCover = new HashSet<Integer>(CurrentVertexCover);// 初始化C‘
				ub = CurrentVertexCover.size();// 初始化开始时候的界ub
					update_dscores();
				break;
			}
		}
	}

	public static int compute_score(int u, int v) {
		for (edge t : graph.adTable.get(u)) {
			// System.out.println(t.x + " " + t.y);
			if (t.x == u && t.y == v || t.y == u && t.x == v)
				return dscores.get(u) + dscores.get(v) + t.weight;
		}
		return dscores.get(u) + dscores.get(v);
	}

	// very important part
	public static edge ChooseSwapPair() {// 这里先只实现一半
		// Old Zhang will finish another half
	//	int pointer = LIST.size();
		//HashSet<edge> readyForSelect = new HashSet<edge>();

		for(edge temp:LIST.getEdges()){
			if (Math.random() < 0.5) {
				temp.swap();
			}
			for (Integer u : CurrentVertexCover) {
				if (u != tabuRemove && temp.x != tabuAdd) {
					if (compute_score(u, temp.x) > 0)
						return new edge(u,temp.x);
						//readyForSelect.add(new edge(u, temp.x));
				}
				if (u != tabuRemove && temp.y != tabuAdd) {
					if (compute_score(u, temp.y) > 0)
						//readyForSelect.add(new edge(u, temp.y));
						return new edge(u,temp.y);
				}
	/*			if (!readyForSelect.isEmpty()) {
					return readyForSelect.iterator().next();
				}*/
			}
		}
	//	System.out.println("choose failed");
		return null;
	}

	public static edge Random() {
		List<Integer> list = new ArrayList<Integer>(CurrentVertexCover);
		List<edge> uList = LIST.getEdges();
		Random rd = new Random();
		int pos = (Math.abs(rd.nextInt())) % list.size();
		int posy = (Math.abs(rd.nextInt())) % uList.size();
		int x = list.get(pos); // 
		edge y = uList.get(posy);
		if (pos % 2 == 0) {
			return new edge(x, y.x);
		}
		return new edge(x, y.y);
	}

	public static HashSet<Integer> greedyLittle() {
//		update_dscores();// 更新所有的呢。
		HashSet<Integer> Cplus = new HashSet<Integer>();
		HashMap<Integer, Integer> temp_dscore = new HashMap<Integer, Integer>();
		for(Integer t:RemainVertex)
		{
			temp_dscore.put(t, compute_dscore(t));
		}
		ArrayList<Map.Entry<Integer, Integer>>temp_value = new ArrayList<Map.Entry<Integer, Integer>>(
				temp_dscore.entrySet());
		Collections.sort(temp_value, new CMP());
		HashSet<edge> l=new HashSet<edge>(LIST.getEdges());
		for (Map.Entry<Integer, Integer> t :temp_value) {
	//		ArrayList<edge> temp = new ArrayList<edge>(graph.adTable.get(t.getKey()));
			Cplus.add(t.getKey());
			// RemainVertex.remove(t);
			l.removeAll(graph.adTable.get(t.getKey()));
			/*for (edge myedge : temp) {

				l.remove(myedge);
			}*/
			if (l.isEmpty())
				break;
		}
		return Cplus;
	}

	public static void main(String[] args) {
		graph = new Graph();
		System.out.println("hello");
		// initial
		initial();// 初始化；

		System.out.println("hello CurrentVertexCover.size() "
				+ CurrentVertexCover.size());

		// remove some vertex(big dscore vertex) till |CVC|=ub-delta
		int times = 0;
		for (Entry<Integer, Integer> tmp : dscores_value) {
			delAnVetex(tmp.getKey());
			times++;
			if (times == delta) {
				// ub = ub - delta; why update upbound here?
				break;
			}
		}
		HashSet<Integer>changed=new HashSet<Integer>();
		while (step++ < finalstep) {
			changed.clear();
			System.out.println(step);
			if (!LIST.isEmpty()) {
				edge swapvetexs = ChooseSwapPair();
				// System.out.println("choose:" + swapvetexs.x + " " +
				// swapvetexs.y);
				if (swapvetexs!=null) {
					delAnVetex(swapvetexs.x);
					addAnVetex(swapvetexs.y);
				}// 寻找可以交换的对
				else {
					for (edge t : LIST.getEdges()) {
						t.weight += 1;// 对应的权重加1
					}
					swapvetexs = Random();
					delAnVetex(swapvetexs.x);
					addAnVetex(swapvetexs.y);

				}// 把随机找出来的交换了

				tabuAdd = swapvetexs.x;
				tabuRemove = swapvetexs.y;
				changed.add(tabuAdd);
				changed.addAll(graph.adis.get(tabuAdd));
				changed.add(tabuRemove);
				changed.addAll(graph.adis.get(tabuRemove));
			}
			if (CurrentVertexCover.size() + LIST.size() < ub) {
				ub = CurrentVertexCover.size() + LIST.size();
				if (LIST.isEmpty()) {
					VertexCover.clear();
					VertexCover.addAll(CurrentVertexCover);
				} else {
					VertexCover.clear();
					VertexCover.addAll(CurrentVertexCover);
					VertexCover.addAll(greedyLittle());
				}
			}
			// 将从C中取出一些元素来缩小规模delta
			for(Integer t:changed){
				dscores.put(t, compute_dscore(t));
			}
			//这里并没有排序。这样才快，让我静静。。
			for (Map.Entry<Integer, Integer> temp : dscores_value) {
		//		if (CurrentVertexCover.size() == ub - delta)
			//		break;
				if (CurrentVertexCover.contains(temp.getKey())) {
					delAnVetex(temp.getKey());
					break;//delta是1..
				}
			}

			// System.out.println(VertexCover.size());
		}
		System.out.println(VertexCover.size());
	}
}

class CMP implements Comparator<Map.Entry<Integer, Integer>> {
	public int compare(Map.Entry<Integer, Integer> o1,
			Map.Entry<Integer, Integer> o2) {
		return -(o1.getValue() - o2.getValue());// 从大到小排序；
	}
}

