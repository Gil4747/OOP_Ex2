package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Ex2 implements Runnable {
    private static MyFrame _win;
    private static Arena _ar;
    private static HashMap<Integer, edge_data> choose = new HashMap<>();

    public static void main(String[] a) {
        Thread client = new Thread(new Ex2());
        client.start();
    }

    @Override
    public void run() {
        int scenario_num = 11;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 999;
        //	game.login(id);
        String g = game.getGraph();
        String pks = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        init(game);

        game.startGame();
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) " + game.toString());
        int ind = 0;
        long dt = 100;

        while (game.isRunning()) {
            moveAgants(game, gg);
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }

    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     *
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        //ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        for (int i = 0; i < log.size(); i++) {
            CL_Agent ag = log.get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if (dest == -1) {
                dest = nextNode(game, ag);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
            }
        }
    }

    /**
     * a very simple random walk implementation!
     *
     * @param g
     * @param
     * @return
     */
    private static int nextNode(game_service g, CL_Agent ag) {
        int ans = -1;
        if(choose.get(ag.getID())!=null&&ag.getSrcNode()==choose.get(ag.getID()).getDest())
            closestPoc(g,ag);
        if(choose.get(ag.getID())!=null&&ag.getSrcNode()==choose.get(ag.getID()).getSrc())
            return choose.get(ag.getID()).getDest();
        DWGraph_Algo algo =new DWGraph_Algo();
        algo.load(g.getGraph());
        List<node_data> way= algo.shortestPath(ag.getSrcNode(),closestPoc(g,ag).getSrc());
        if(way!=null&&way.size()>=2)
            ans=way.get(1).getKey();
        return ans;
    }

    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        //gg.init(g);
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        _win = new MyFrame("test Ex2");
        _win.setSize(1000, 700);
        _win.update(_ar);


        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            for (int a = 0; a < cl_fs.size(); a++) {
                Arena.updateEdge(cl_fs.get(a), gg);
            }
            for (int a = 0; a < rs; a++) {
                int ind = a % cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if (c.getType() < 0) {
                    nn = c.get_edge().getSrc();
                }


                game.addAgent(nn);
            }
            for(CL_Agent i: Arena.getAgents(game.getAgents(),_ar.getGraph())){
                closestPoc(game,i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static edge_data updateEdge(CL_Pokemon fr, directed_weighted_graph g) {
        //	oop_edge_data ans = null;
        Iterator<node_data> itr = g.getV().iterator();
        while (itr.hasNext()) {
            node_data v = itr.next();
            Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
            while (iter.hasNext()) {
                edge_data e = iter.next();
                boolean f = Arena.isOnEdge(fr.getLocation(), e, fr.getType(), g);
                if (f) {
                    return e;
                }
            }
        }
        return null;
    }
    public static  edge_data closestPoc(game_service game, CL_Agent ag){
        DWGraph_Algo algo =new DWGraph_Algo();
        algo.load(game.getGraph());
        double shortest= Double.MAX_VALUE;
        edge_data ans=null;
        List<CL_Pokemon> pocs=Arena.json2Pokemons(game.getPokemons());
        for(CL_Pokemon i : pocs){
            if(choose.containsValue(updateEdge(i,algo.getGraph()))){
                pocs.remove(i);
            }
        }
        for(CL_Pokemon i: pocs){
            double temp = algo.shortestPathDist(ag.getSrcNode(),updateEdge(i,algo.getGraph()).getSrc());
            if(temp<shortest&&temp!=-1){
                shortest=temp;
                ans = updateEdge(i,algo.getGraph());
        }}
        choose.put(ag.getID(),ans);
            return ans;
    }
}