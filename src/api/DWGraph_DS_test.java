package api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

public class DWGraph_DS_test {
    private static Random _rnd = null;

    /**
     * Checks the amount of vertices in the graph.
     */
    @Test
    void nodeSize() {
        directed_weighted_graph g = new DWGraph_DS();
        for (int i = 0; i < 4; i++) {
            geoLocation location= new geoLocation(0, 0, i);
            nodeData n = new nodeData(i, location);
            g.addNode(n);
        }
        int s = g.nodeSize();
        assertEquals(4,s);
        g.removeNode(0);
        g.removeNode(2);
        g.removeNode(4);
        s = g.nodeSize();
        assertEquals(2,s);
    }
    /**
     * Checks the amount of arcs in the graph.
     */
    @Test
    void edgeSize() {
        directed_weighted_graph g = new DWGraph_DS();
        for (int i=0; i<4; i++){
            node_data ni=new nodeData();
            System.out.println(ni.getKey());
            g.addNode(ni);
        }
        g.connect(4,5,2.0);
        g.connect(4,6,2.0);
        g.connect(5,6,1.0);
        g.connect(4,4,10.0);
        g.connect(7,6,8.0);
        g.connect(4,7,8.0);
        g.connect(7,4,8.0);
        g.connect(4,5,9.0);
        int sizeEdge =  g.edgeSize();
        System.out.println(g.getEdge(4,7));
        assertEquals(7, sizeEdge);
        double x1 = g.getEdge(4,7).getWeight();
        double x2 = g.getEdge(7,4).getWeight();
        assertEquals(x1, x2);
        assertEquals(x2, 8.0);
    }
    /**
     * Checks if there is no vertex that does not exist in the collection.
     */
    @Test
    void getV() {
        directed_weighted_graph g = new DWGraph_DS();
        Collection<node_data> x = g.getV();
        Iterator<node_data> i = x.iterator();
        while (i.hasNext()) {
            node_data n = i.next();
            assertNotNull(n);
        }
        for (int j=0; j<4; j++){
            node_data nj=new nodeData();
            g.addNode(nj);
        }
        Collection<node_data> c = g.getV();
        Iterator<node_data> path = c.iterator();
        while (path.hasNext()) {
            node_data node = path.next();
            assertNotNull(node);
        }
    }
    @Test
    void connect() {
        directed_weighted_graph g= new DWGraph_DS();
        for (int i = 0; i < 4; i++) {
            geoLocation location= new geoLocation(0, 0, i);
            nodeData n = new nodeData(i, location);
            g.addNode(n);
        }
        g.connect(0,1,1);
        g.connect(0,3,4.0);
        g.connect(3,0,4.0);
        g.connect(1,2,0.1);
        g.connect(1,3,10.0);
        assertEquals(null,g.getEdge(3,4));
        assertEquals(9,g.getMC());
        assertEquals(10.0,g.getEdge(1,3).getWeight());
        assertEquals(null,g.getEdge(0,2));
    }
    /**
     *Checks whether the vertex has been deleted from
     *the graph and so have all the arcs that were attached to it.
     */
    @Test
    void removeNode() {
        directed_weighted_graph g= new DWGraph_DS();
        for (int i = 0; i < 4; i++) {
            geoLocation location= new geoLocation(0, 0, i);
            nodeData n = new nodeData(i, location);
            g.addNode(n);
        }
        g.connect(0,1,1);
        g.connect(0,3,4.0);
        g.connect(3,0,4.0);
        g.connect(1,2,0.1);
        g.connect(1,3,10.0);
        g.connect(3,0,5.8);
        g.connect(4,5,6.0);
        int e = g.edgeSize();
        assertEquals(5,e);
        g.removeNode(2);
        g.removeNode(2);
        g.removeNode(0);
        g.removeNode(10);
        e = g.edgeSize();
        int nodes=g.nodeSize();
        assertEquals(1,e);
        assertEquals(2,nodes);
    }
    /**
     * Checks that the arc has been deleted from the graph.
     */
    @Test
    void removeEdge() {
        directed_weighted_graph g= new DWGraph_DS();
        for (int i = 0; i < 4; i++) {
            geoLocation location= new geoLocation(0, 0, i);
            nodeData n = new nodeData(i, location);
            g.addNode(n);
        }
        g.connect(0,1,1);
        g.connect(0,3,4.0);
        g.connect(3,0,4.0);
        g.connect(1,2,0.1);
        g.connect(1,3,10.0);
        g.connect(3,0,5.8);
        g.connect(4,5,6.0);
        g.removeEdge(0,1);
        g.removeEdge(5,4);
        g.removeEdge(3,3);
        g.removeEdge(0,3);
        g.removeEdge(4,5);
        assertEquals(3,g.edgeSize());
        assertEquals(null,g.getEdge(0,1));
        assertEquals(null,g.getEdge(4,5));
        double x2 = g.getEdge(1,2).getWeight();
        assertEquals(x2,0.1);
    }

    private directed_weighted_graph myGraph() {
        directed_weighted_graph g0 = new DWGraph_DS();
        for (int i = 0; i < 4; i++) {
            geoLocation location= new geoLocation(0, 0, i);
            nodeData n = new nodeData(i, location);
            g0.addNode(n);
        }
        g0.connect(0,1,1);
        g0.connect(0,3,4.0);
        g0.connect(3,0,4.0);
        g0.connect(1,2,0.1);
        g0.connect(1,3,10.0);
        g0.connect(2,10,20.0);
        return g0;
    }

    public static directed_weighted_graph graph_creator(int v_size, int e_size, int seed) {
        directed_weighted_graph g = new DWGraph_DS();
        _rnd = new Random(seed);
        for (int i = 0; i < v_size; i++) {
            geoLocation location= new geoLocation(0, 0, i);
            nodeData n = new nodeData(i, location);
            g.addNode(n);
        }
        int[] nodes = nodes(g);
        while(g.edgeSize() < e_size) {
            int a = nextRnd(0,v_size);
            int b = nextRnd(0,v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = _rnd.nextDouble();
            g.connect(i,j, w);
        }
        return g;
    }
    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
     * @param g
     * @return
     */
    private static int[] nodes(directed_weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_data> V = g.getV();
        node_data[] nodes = new node_data[size];
        V.toArray(nodes);
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }
}
