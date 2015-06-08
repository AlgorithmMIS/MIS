package ewls;

import java.io.*;
import java.util.*;

class RNG {
	private Random r = new Random();

	public int next() {
		return r.nextInt() >>> 1;
	}
}

class Pair {
	public final int x, y;

	public Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

class Edge {
	public final int x, y;

	public boolean incident(int v) {
		return x == v || y == v;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		return x == other.x && y == other.y;
	}

	public Edge(int x, int y) {
		if (x < y) {
			this.x = x;
			this.y = y;
		} else {
			this.x = y;
			this.y = x;
		}
	}
}

public class EWLS {
	HashSet<Integer> RemainVertex;// 剩余的点(independent set)
	LinkedList<Edge> LIST;// L (oldest first)
	Graph graph;
	int tabuAdd = -1;
	int tabuRemove = -1;
	int step = 0;
	int ub;
	static final int finalstep = 100000;
	int[] dscore;
	RNG rng = new RNG();
	// C contains v if and only if inc[v]
	boolean[] inc;
	int[][] edgeWeight;
	int nv;

	private int csize() {
		return nv - RemainVertex.size();
	}

	// 单纯的向C中加一个点
	public void add(int v) {
		assert !inc[v];
		inc[v] = true;
		RemainVertex.remove(v);
		// remove edges covered by v...
		Iterator<Edge> it = LIST.iterator();
		while (it.hasNext()) {
			Edge e = it.next();
			if (e.incident(v))
				it.remove();
		}
		// update dscore
		for (int neighbor : graph.neighbors(v))
			dscore[neighbor] -= edgeWeight[v][neighbor];
	}

	// remove a vertex from C
	public void remove(int v) {
		assert inc[v];
		inc[v] = false;
		RemainVertex.add(v);
		for (int neighbor : graph.neighbors(v)) {
			if (!inc[neighbor])
				LIST.add(new Edge(v, neighbor));
			// update dscore
			dscore[neighbor] += edgeWeight[v][neighbor];
		}
	}

	public void init() {
		nv = graph.nv;
		inc = new boolean[nv];

		// initialize edge weight
		edgeWeight = new int[nv][nv];
		for (int i = 0; i < nv; i++)
			for (int j : graph.neighbors(i))
				edgeWeight[i][j] = 1;

		// number of uncovered edges
		int uncov = graph.edges.size();

		// number of UNCOVERED incident edges
		int[] w = new int[nv];
		for (int i = 0; i < nv; i++)
			w[i] = graph.degree(i);

		// Now, construct initial VC.
		while (uncov > 0) {
			// choose vertex v not in C with max w[v]
			int max = -1;
			int maxv = -1;
			for (int i = 0; i < nv; i++) {
				if (inc[i])
					continue;
				if (w[i] > max) {
					max = w[i];
					maxv = i;
				}
			}
			inc[maxv] = true;
			ub++;
			uncov -= max;
			for (int neighbor : graph.neighbors(maxv))
				w[neighbor]--;
		}
		assert uncov == 0;

		// 求初始dscore
		dscore = new int[nv];
		for (int i = 0; i < nv; i++)
			for (int j : graph.neighbors(i))
				if (!inc[j])
					dscore[i]++;

		RemainVertex = new HashSet<Integer>();
		for (int i = 0; i < nv; i++)
			if (!inc[i])
				RemainVertex.add(i);

		LIST = new LinkedList<>();
		for (int i : RemainVertex)
			for (int j : RemainVertex)
				if (edgeWeight[i][j] > 0)
					LIST.add(new Edge(i, j));
	}

	public int compute_score(int u, int v) {
		return dscore[v] - dscore[u] + edgeWeight[u][v];
	}

	public Pair chooseSwapPair() {
		for (Edge e : LIST) {
			assert !inc[e.x];
			assert !inc[e.y];
			int r = rng.next() % nv;
			for (int _u = 0; _u < nv; _u++) {
				int u = (_u + r) % nv;
				if (!inc[u])
					continue;
				if (u != tabuRemove && e.x != tabuAdd) {
					if (compute_score(u, e.x) > 0)
						return (new Pair(u, e.x));
				}
				if (u != tabuRemove && e.y != tabuAdd) {
					if (compute_score(u, e.y) > 0)
						return (new Pair(u, e.y));
				}
			}
		}
		return new Pair(-1, -1);
	}

	public Pair randomSwapPair() {
		int x;
		do
			x = rng.next() % nv;
		while (!inc[x]);
		Edge uncov = LIST.get(rng.next() % LIST.size());
		if (rng.next() % 2 == 0) {
			return new Pair(x, uncov.x);
		}
		return new Pair(x, uncov.y);
	}

	public EWLS(Graph graph) {
		this.graph = graph;
	}

	private static void usage() {
		System.err.println("usage?");
		System.exit(2);
	}

	// remove vertex with highest dscore until |C|=ub-delta
	private void removeDelta() {
		int max = Integer.MIN_VALUE;
		int maxv = -1;
		for (int i = 0; i < nv; i++) {
			if (inc[i] && dscore[i] > max) {
				max = dscore[i];
				maxv = i;
			}
		}
		remove(maxv);
	}

	public void solve() {
		init();// 初始化；
		System.out.printf("init complete, ub=%d, size of IS is %d%n", ub,
				RemainVertex.size());

		removeDelta();

		while (step++ < finalstep) {
			if (step % 1000 == 0) {
				System.out.printf("step %d, %d uncovered%n", step, LIST.size());
			}
			if (!LIST.isEmpty()) {
				// 寻找可以交换的对
				Pair swappair = chooseSwapPair();
				if (swappair.x >= 0) {
					// System.out.printf("%d,%d%n", swappair.x, swappair.y);
					assert inc[swappair.x];
					assert !inc[swappair.y];
				} else {
					// stuck at local optimum
					// increase weight of uncovered edges
					for (Edge e : LIST) {
						edgeWeight[e.x][e.y]++;
						edgeWeight[e.y][e.x]++;
						// also update dscore
						if (!inc[e.x])
							dscore[e.y]++;
						if (!inc[e.y])
							dscore[e.x]++;
					}
					// 把随机找出来的交换了
					swappair = randomSwapPair();
				}
				remove(swappair.x);
				add(swappair.y);
				tabuAdd = swappair.x;
				tabuRemove = swappair.y;
			}
			if (csize() + LIST.size() < ub) {
				// we've found a better solution
				// update upper bound of MVC size
				ub = csize() + LIST.size();
				// TODO
				if (LIST.isEmpty()) {
					System.out.println(RemainVertex.size());
				} else {
					System.out.printf("%d (%d)%n", RemainVertex.size(),
							LIST.size());
				}
				removeDelta();
			}
		}
	}

	public static void main(String[] args) {
		if (args.length != 1)
			usage();
		try {
			EWLS solver = new EWLS(new Graph(new File(args[0])));
			solver.solve();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
