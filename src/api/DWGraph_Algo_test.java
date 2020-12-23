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

public class DWGraph_Algo_test{
    private static Random _rnd = null;
    /**
     * Checking the copy of the graph.
     */
    @Test
    void copy() {
        directed_weighted_graph g= myGraph();
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(g);
        directed_weighted_graph cop=ga.copy();
        assertEquals(cop,g);
    }

    @Test
    void isConnected() {
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
        g0.connect(2,3,10.0);
        dw_graph_algorithms GA= new DWGraph_Algo();
        GA.init(g0);
        boolean b=GA.isConnected();
        assertTrue(b);
        directed_weighted_graph g1 = new DWGraph_DS();
        for (int i = 0; i < 4; i++) {
            geoLocation location= new geoLocation(0, 0, i);
            nodeData n = new nodeData(i, location);
            g1.addNode(n);
        }
        GA.init(g1);
        b=GA.isConnected();
        assertFalse(b);
    }
    /**
     * Examines the shortest path in a weighted graph from one vertex to another.
     */
    @Test
    void shortestPathDist() {
        directed_weighted_graph g0 = myGraph();
        dw_graph_algorithms ag0 = new DWGraph_Algo();
        ag0.init(g0);
        double ans = ag0.shortestPathDist(0,2);
        assertEquals(ans, -1);
        g0.connect(2,0,20.0);
        g0.connect(1,2,0.1);
        assertTrue(ag0.isConnected());
        ans = ag0.shortestPathDist(0,2);
        assertEquals(ans, 1.1);
    }
    /**
     * Checking the vertices through them is the shortest way to get from one vertex to another.
     */
    @Test
    void shortestPath() {
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
        dw_graph_algorithms GA= new DWGraph_Algo();
        GA.init(g0);
        assertEquals(3, GA.shortestPath(0, 2).size());
        directed_weighted_graph g1 = new DWGraph_DS();
        for (int i = 0; i < 4; i++) {
            geoLocation location= new geoLocation(0, 0, i);
            nodeData n = new nodeData(i, location);
            g1.addNode(n);
        }
        GA.init(g1);
        assertEquals(1, GA.shortestPath(0, 0).size());
    }
    @Test
    void save_load() throws FileNotFoundException  {
        directed_weighted_graph g0 = graph_creator(2,3,1);
        dw_graph_algorithms ag0 = new DWGraph_Algo();
        ag0.init(g0);
        // System.out.println();
        String str = "file.json";
        assertTrue(ag0.save(str));
        directed_weighted_graph g1 =graph_creator(2,3,1);
        ag0.load(str);
        assertTrue(((DWGraph_Algo) ag0).graph_equals(g1));
        g0.removeNode(1);
        assertFalse(((DWGraph_Algo) ag0).graph_equals(g1));
        ag0.load("C:\\Users\\Gil\\OneDrive\\מסמכים\\GitHub\\Ariel_OOP_2020\\Assignments\\Ex2\\data\\A0");
        assertEquals(ag0.shortestPathDist(0, 1), -1);
        assertTrue(ag0.load("C:\\Users\\Gil\\OneDrive\\מסמכים\\GitHub\\Ariel_OOP_2020\\Assignments\\Ex2\\data\\A0.json"));
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
        g0.connect(1,3,10.0);
        return g0;
    }
    /**
     * Generate a random graph with v_size nodes and e_size edges
     * @param v_size
     * @param e_size
     * @param seed
     * @return
     */

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
