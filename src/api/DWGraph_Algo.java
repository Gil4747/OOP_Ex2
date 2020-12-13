package api;
import java.io.BufferedWriter;
import java.io.File;
import java.io.*;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.json.simple.parser.JSONParser;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import org.json.simple.parser.ParseException;


public class DWGraph_Algo implements dw_graph_algorithms {
	private DWGraph_DS graph;

	public DWGraph_Algo() {
		this.graph= new DWGraph_DS();
	}
	public DWGraph_Algo(DWGraph_DS G) {
		this.graph= G;
	}

	@Override
	public void init(directed_weighted_graph g) {
		this.graph = (DWGraph_DS) g;
	}

	@Override
	public directed_weighted_graph getGraph() {
		return this.graph;
	}

	@Override
	public directed_weighted_graph copy() {
		DWGraph_DS copy = new DWGraph_DS();
		if(!graph.getV().isEmpty()) {
			for (node_data i : graph.getV()) {
				copy.addNode(i);
			}
			for (node_data i : graph.getV()) {
				for (edge_data j : graph.getE(i.getKey())) {
					copy.connect(j.getSrc(), j.getDest(), j.getWeight());
				}
			}}
		return copy;
	}


	@Override
	public boolean isConnected() {
		if(graph==null||graph.getV().size()==0||graph.getV().size()==1)
			return true;
		if(DFS(this.graph,graph.getV().iterator().next())==-1)
			return false;
		if(DFS(transpose(),transpose().getV().iterator().next())==-1) {
			return false;
		}
		return true;
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		if(graph.getNode(src)==null||graph.getNode(dest)==null||shortestPath(src, dest)==null)
			return -1;
		if(src==dest)
			return 0;
		double ans = 0;
		int key = -1;
		for (node_data i : shortestPath(src, dest)) {
			if (key == -1) {
				key = i.getKey();
				continue;
			}
			ans += graph.getEdge(key, i.getKey()).getWeight();
			key = i.getKey();
		}

		return ans;
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		if (graph.getV().isEmpty() || graph.getV() == null || graph.nodeSize() == 0 || graph.nodeSize() == 1)
			return null;
		Map<Integer, node_data> visited = new HashMap<Integer, node_data>();
		List<node_data> ans = new LinkedList<node_data>();
		HashMap<Integer, Double> dis = new HashMap<>();
		PriorityQueue<node_data> que = new PriorityQueue<node_data>(new Comparator<node_data>() {
			@Override
			public int compare(node_data o1, node_data o2) {
				return -Double.compare(dis.get(o2.getKey()),dis.get(o1.getKey()));
			}
		});
		node_data start = graph.getNode(src);
		node_data s = start;
		node_data end = graph.getNode(dest);
		visited.put(graph.getNode(src).getKey(), graph.getNode(src));
		que.add(graph.getNode(src));
		dis.put(graph.getNode(src).getKey(), 0.0);
		while (que.size() != 0 && (!dis.containsKey(dest) || dis.get(que.peek().getKey()) < dis.get(dest))) {
			start = que.poll();
			visited.put(start.getKey(), start);
			for (edge_data n : graph.getE(start.getKey())) {
				if(!dis.containsKey(n.getDest())||dis.get(n.getDest())>dis.get(n.getSrc())+n.getWeight()){
					dis.put(n.getDest(),dis.get(n.getSrc())+n.getWeight());
					graph.getNode(n.getDest()).setTag(n.getSrc());
					que.add(graph.getNode(n.getDest()));
				}
			}
		}
		if (end.getTag() != 0 || dis.containsKey(dest)) {
			while (visited.get((int) end.getTag()) != null && end.getKey() != s.getKey()) {
				ans.add(end);
				end =  visited.get((int) end.getTag());
			}
			ans.add(s);
			cleanTags(visited);
			Collections.reverse(ans);
			return ans;
		}		return null;
	}

	@Override
	public boolean save(String file) {
		JSONArray ansE=new JSONArray();
		JSONArray ansN=new JSONArray();
		JSONObject ans=new JSONObject();

		FileWriter file2=null;
		try {
			file2 = new FileWriter(file);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		Iterator<node_data> it=graph.getV().iterator();
		while(it.hasNext()) {
			node_data temp=it.next();
			JSONObject n=new JSONObject();
			try {
				n.put("pos:", temp.getLocation().x()+" ,"+temp.getLocation().y()+" ,"+temp.getLocation().z());
				n.put("id:",temp.getKey());
			}
			catch (JSONException e1) {
				e1.printStackTrace();
			}
			ansN.put(n);	
			Iterator<edge_data> it2=graph.getE(temp.getKey()).iterator();
			while(it2.hasNext()) {		
				edge_data temp2=it2.next();
				JSONObject e=new JSONObject();
				try {
					e.put("src", temp.getKey());
					e.put("w", temp2.getWeight());
					e.put("dest", temp2.getDest());
					ansE.put(e);
				}
				catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		}

		try {
			ans.put("Edges:",ansE);
			ans.put("Nodes",ansN);
		}
		catch (JSONException e1) {
			e1.printStackTrace();
		}
		try {
			file2.write(ans.toString());
			file2.flush();
			System.out.println(ans.toString());

		}

		catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;
	}


	@Override
	public boolean load(String file) {
		if (file.charAt(0) == '{') {
			try {
				JSONObject jsonObject = new JSONObject(file);
				JSONArray Edges = (JSONArray) jsonObject.get("Edges");
				JSONArray Nodes = (JSONArray) jsonObject.get("Nodes");
				//System.out.println(Nodes.toString());
				DWGraph_DS g = new DWGraph_DS();
				for (int i = 0; i < Nodes.length(); i++) {
					JSONParser parser1 = new JSONParser();
					JSONObject NodeD = new JSONObject(Nodes.get(i).toString());
					String[] s = NodeD.get("pos").toString().split(",");
					geo_location location1 = new geoLocation(Double.parseDouble(s[0]), Double.parseDouble(s[1]), Double.parseDouble(s[2]));
					nodeData node = new nodeData((int) Double.parseDouble(NodeD.get("id").toString()), location1);
					g.addNode(node);
				}
				for (int i = 0; i < Edges.length(); i++) {
					JSONParser parser1 = new JSONParser();
					JSONObject NodeD = new JSONObject(Edges.get(i).toString());
					g.connect((int) Double.parseDouble(NodeD.get("src").toString()), (int) Double.parseDouble(NodeD.get("dest").toString()), Double.parseDouble(NodeD.get("w").toString()));
				}
				init(g);
			} catch (JSONException e) {
				return false;
			}
			try {
				try {
					FileReader reader = new FileReader((file));
					JSONParser parser = new JSONParser();
					// JsonReader jsonReader= reader;
					JSONObject jsonObject = (JSONObject) parser.parse(reader);
					JSONArray Edges = (JSONArray) jsonObject.get("Edges");
					JSONArray Nodes = (JSONArray) jsonObject.get("Nodes");
					//System.out.println(Nodes.toString());
					DWGraph_DS g = new DWGraph_DS();
					for (int i = 0; i < Nodes.length(); i++) {
						JSONParser parser1 = new JSONParser();
						JSONObject NodeD = (JSONObject) parser1.parse(Nodes.get(i).toString());
						String[] s = NodeD.get("pos").toString().split(",");
						geo_location location1 = new geoLocation(Double.parseDouble(s[0]), Double.parseDouble(s[1]), Double.parseDouble(s[2]));
						nodeData node = new nodeData((int) Double.parseDouble(NodeD.get("id").toString()), location1);
						g.addNode(node);
					}
					for (int i = 0; i < Edges.length(); i++) {
						JSONParser parser1 = new JSONParser();
						JSONObject NodeD = (JSONObject) parser1.parse(Edges.get(i).toString());
						g.connect((int) Double.parseDouble(NodeD.get("src").toString()), (int) Double.parseDouble(NodeD.get("dest").toString()), Double.parseDouble(NodeD.get("w").toString()));
					}
					init(g);
				} catch (FileNotFoundException e) {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return true;

	}


	public boolean equals(DWGraph_DS sec) {
		boolean yah=true;
		if(this.graph.getV().size()!=sec.getV().size()) {
			return false;
		}
		for(node_data i: this.graph.getV()) {
			for(edge_data j: this.graph.getE(i.getKey()) ) {
				yah&=sec.getEdge(i.getKey(), j.getDest()).getDest()
						==this.graph.getEdge(i.getKey(), j.getDest()).getDest();
			}
		}
		return yah;
	}

	public int DFS(directed_weighted_graph g,node_data start) {
		// hashmap saves all nodes which visited by bfs algorithm
		Map<Integer, node_data> visited = new HashMap<Integer, node_data>();
		// queue of BFS
		Stack<node_data> queue = new Stack<node_data>();
		visited.put(start.getKey(), start);
		queue.push(start);
		int ans=-1;
		while (queue.size() != 0) {
			// take the first-added node, poll it from the queue, name it start and repeat the process
			start = queue.pop();
			//check all start node neighbours and add the unchecked nodes to the queue and the hashmap
			for (edge_data n : g.getE(start.getKey())) {
				if (!visited.containsKey(n.getDest())) {
					visited.put(n.getDest(), g.getNode(n.getDest()));
					queue.push(g.getNode(n.getDest()));
					if(visited.size()==g.nodeSize()) {
						ans = n.getDest();
					}
				}
			}
		}
		//returns the number of connected nodes
		return ans;
	}

	public directed_weighted_graph transpose() {
		DWGraph_DS trans = new DWGraph_DS();
		for (node_data i : graph.getV()) {
			trans.addNode(i);
		}
		for (node_data i : graph.getV()) {
			for (edge_data j : graph.getE(i.getKey())) {
				trans.connect(j.getDest(),j.getSrc(), j.getWeight());
			}
		}
		return trans;
	}
	// the function set back the graph's node's tags to 0
	public void cleanTags(Map<Integer, node_data> hashMap) {
		hashMap.forEach((key, value) -> {
			value.setTag(0);
		});
	}
	/**
	 * computes a relatively short path which visit each node in the targets List.
	 * Note: this is NOT the classical traveling salesman problem,
	 * as you can visit a node more than once, and there is no need to return to source node -
	 * just a simple path going over all nodes in the list.
	 * @param targets
	 * @return the path of the way in list of node_data
	 */

	public List<node_data> TSP(List<Integer> targets) {
		if (targets == null || targets.isEmpty()) return null;
		if (graph.getV()!= null) {
			for (node_data n : graph.getV()) {
				n.setTag(0);
			}
		}
		int i = 0;
		List<node_data> ans = new LinkedList<node_data>();

		HashMap<Integer,Boolean> hashMap = new LinkedHashMap<>();
		while (i<targets.size()){
			if(!hashMap.containsKey(targets.get(i))){
				hashMap.put(targets.get(i),true);
				i++;
			}
			else {
				targets.remove(i);
			}
		}
		i=0;
		int temp = 0;
		int temp2;
		List<node_data> tempN;
		if (!targets.isEmpty()) {
			temp = targets.remove(0);
		}
		while (!targets.isEmpty()){
			temp2 = targets.remove(0);
			tempN = shortestPath(temp,temp2);
			if(tempN == null) return null;
			for (node_data nk: tempN){
				if (targets.contains(nk.getKey())){
					targets.remove((Integer)nk.getKey());
				}
			}
			ans.addAll(tempN);
			temp = temp2;
		}

		i = 0;
		while (i < ans.size() - 1) {
			if (ans.get(i).equals(ans.get(i + 1)))
				ans.remove(i);
			else
				i++;
		}
	
		return ans;
	}

}