package uet.oop.bomberman.entities.moveEntities.enemy.graph;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillEntities.immortal.Wall;
import uet.oop.bomberman.entities.stillEntities.mortal.Bomb;
import uet.oop.bomberman.entities.stillEntities.mortal.Brick;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    public int x, y;
    public int w, h;
    public List<List<Point>> graph = new ArrayList<>(0);
    public Graph(int x, int y, int w, int h) {
        for(int i = 0; i < h; i++) {
            this.graph.add(new ArrayList<>());
            for(int j = 0; j < w; j++) {
                this.graph.get(i).add(new Point(j + x, i + y,
                        (double)i + j + Entity.round1(Math.sqrt((i + 1)*(i + 1) + (j + 1) * (j + 1)))));
                setCannotMove(this.graph.get(i).get(j));
            }
        }
    }

    public void add(int x, int y) {
        graph.get(y).get(x).cannotMove = true;
    }

    public void remove(int x, int y) {
        graph.get(y).get(x).cannotMove = false;
    }

    private void setCannotMove(Point p) {
        for(int i = 0; i < BombermanGame.stillObjects.size(); i++) {
            Entity still = BombermanGame.stillObjects.get(i);
            if(Math.round(still.getX()) == Math.round(p.x) && Math.round(still.getY()) == Math.round(p.y)) {
                if(still instanceof Bomb || still instanceof Brick || still instanceof Wall) {
                    p.cannotMove = true;
                    return;
                }
            }
        }
        p.cannotMove = false;
        return;
    }

    public void printGraph() {
        for(int i = 0; i < graph.size(); i++) {
            for(int j = 0; j < graph.get(i).size(); j++) {
                String heuristics;
                if(graph.get(i).get(j).cannotMove) {
                    graph.get(i).get(j).f = 0;
                    heuristics = "x";
                } else {
                    heuristics = String.valueOf(graph.get(i).get(j).f);
                }
                String tab = "\t";
                if(graph.get(i).get(j).f < 10) {
                    tab += "\t";
                }
                System.out.print(heuristics + tab);
            }
            System.out.println();
        }
    }
}
