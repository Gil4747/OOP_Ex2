package gameClient;

import api.*;
import api.DWGraph_DS;
import org.json.JSONObject;
import gameClient.util.Point3D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Agents {
    ArrayList<CL_Agent> myAgents = new ArrayList<>();
    DWGraph_DS graph;

    /**
     * init All the Robots
     * @param game - rhe game after start
     * @param g - the graph of the game
     */
    public Agents(game_service game, DWGraph_DS g) {
        graph = g;
        List<CL_Agent> log = Arena.getAgents(game.getAgents(),(directed_weighted_graph) graph);
        if (log != null) {
            for (int i = 0; i < log.size(); i++) {
              CL_Agent a = log.get(i);
                    int Id = a.getID();
                    geo_location position = new geoLocation(a.getLocation().x(), a.getLocation().y());
                    CL_Agent temp = new CL_Agent(Id,position);
                    myAgents.add(temp);

            }
        }
    }

    /**
     * regular Iterator
     * @return Iterator of the robot
     */

    public Iterator<CL_Agent> iterator(){
        return myAgents.iterator();
    }

    /**
     *
     * @return Collection of the Robots
     */
    public Collection<CL_Agent> collection(){
        return myAgents;
    }

    /**
     * return robot by ID
     * @param id - id of the robot
     * @return Robot
     */
    public CL_Agent getAgent(int id){
        Iterator<CL_Agent> robotIterator = iterator();
        while (robotIterator.hasNext()){
            CL_Agent temp = robotIterator.next();
            if (id==temp.getID()){
                return temp;
            }
        }
        return null;
    }

    /**
     *
     * @return amount of Robot in int
     */
    public int zise() {
        return myAgents.size();
    }
}
