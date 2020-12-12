package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class DWGraph_DS implements directed_weighted_graph {
private int edges=0; 
private int MC=0; 
private HashMap<Integer, node_data> nodes;
private HashMap<Integer, HashMap<Integer, edge_data>> go;
private HashMap<Integer, HashMap<Integer, edge_data>> back;

public DWGraph_DS() {
	this.edges=0;
	this.MC=0;
	this.nodes=new HashMap<Integer, node_data>();
	this.go=new HashMap<Integer, HashMap<Integer, edge_data>>();
	this.back=new  HashMap<Integer, HashMap<Integer, edge_data>>();
}
public DWGraph_DS(DWGraph_DS g) {
	this.edges=g.edges;
	this.MC=g.MC;
	this.nodes=g.nodes;
	this.go=g.go;
	this.back=g.back;
}
	@Override
	public node_data getNode(int key) {
		return nodes.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		return go.get(src).get(dest);
	}

	@Override
	public void addNode(node_data n) {
		this.nodes.put(n.getKey(), n);
		HashMap<Integer, edge_data> nodesgo = new HashMap<>();
		HashMap<Integer, edge_data> nodesback = new HashMap<>();
		this.go.put(n.getKey(), nodesgo);
		this.back.put(n.getKey(), nodesback);
		MC++;

	}

	@Override
	public void connect(int src, int dest, double w) {
		if(!nodes.containsKey(src)||!nodes.containsKey(dest))
			return;
		edge_data goedge =new edgeData(this.nodes.get(src),this.nodes.get(dest)) ;
		go.get(src).put(dest,goedge);
		back.get(dest).put(src, goedge);
		edges++;
		MC++;

	}

	@Override
	public Collection<node_data> getV() {
		return nodes.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		return go.get(node_id).values();
	}

	@Override
	public node_data removeNode(int key) {
	if(!nodes.containsKey(key))	
		return null;
    Iterator<Integer> ig=this.go.get(key).keySet().iterator();
    while(ig.hasNext()) {
	   back.get(ig.next()).remove(key);
     }
    Iterator<Integer> ib=this.back.get(key).keySet().iterator();
    while(ib.hasNext()) {
	   go.get(ib.next()).remove(key);
     }
    edges-=(go.get(key).size()+back.get(key).size());
    MC++;
    go.remove(key);
    node_data ans=nodes.get(key);
    nodes.remove(key);
    return ans;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		if(!nodes.containsKey(src)||!nodes.containsKey(dest))
			return null;
		edge_data ans=go.get(src).get(dest);
		go.get(src).remove(dest);
		back.get(dest).remove(src);
		edges--;
		MC++;
		return ans;
	}

	@Override
	public int nodeSize() {
		return nodes.size();
	}

	@Override
	public int edgeSize() {
		return this.edges;
	}

	@Override
	public int getMC() {
		return MC;
	}
	
	public boolean equals(Object second) {
		DWGraph_DS sec=(DWGraph_DS)second;
		boolean yah=true;
		if(this.getV().size()!=sec.getV().size()) {
			return false;
		}
		for(node_data i: this.getV()) {
			for(edge_data j: this.getE(i.getKey()) ) {
				yah&=sec.getEdge(i.getKey(), j.getDest()).getDest()
						==this.getEdge(i.getKey(), j.getDest()).getDest();
			}
		}
		return yah;
	}
	
}
