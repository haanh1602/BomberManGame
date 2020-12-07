package uet.oop.bomberman.entities.moveEntities.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.moveEntities.enemy.graph.Graph;
import uet.oop.bomberman.entities.moveEntities.enemy.graph.Point;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Kondoria extends Enemy {
    // TODO: đuổi theo bomber có thuật toán A*
    boolean panic = false;
    int count = 1;

    List<Point> astar_direct = new ArrayList<>();
    public Kondoria(double x, double y, Image img) {
        super(x, y, img);
        this.img = Sprite.kondoria_left1.getFxImage();
        this.speed = 3;
        this.type = "kondoria";
        this.point = 1000;
    }

    List<Point> close = new ArrayList<>();

    public double cost(Point a, Point b) {
        return Entity.round1(Math.abs(a.f - b.f));
    }

    public boolean isClose(Point x) {
        return close.contains(x);
    }

    public boolean isClose(int x, int y) {
        Point p = getPoint(x, y);
        return isClose(p);
    }

    public Point getPoint(int x, int y) {
        for(int i = 0; i < BombermanGame.graph.graph.size(); i++) {
            for(int j = 0; j < BombermanGame.graph.graph.get(i).size(); j++) {
                if(BombermanGame.graph.graph.get(i).get(j).x == x
                        && BombermanGame.graph.graph.get(i).get(j).y == y ) {
                    return BombermanGame.graph.graph.get(i).get(j);
                }
            }
        }
        return null;
    }

    public int findMinPathIdx(List<List<Point>> open, Point target) {
        int res = -1;
        double minF = 100000;
        for(int i = 0; i < open.size(); i++) {
            if(cost(open.get(i).get(open.get(i).size() - 1), target) < minF) {
                res = i;
                minF = cost(open.get(i).get(open.get(i).size() - 1), target);
            }
        }
        return res;
    }

    public List<List<Point>> next(List<Point> p) {
        List<List<Point>> res = new ArrayList<>();
        Point x = p.get(p.size() - 1);
        // left
        Point left = getPoint(x.x - 1, x.y);
        if(left != null && !left.cannotMove && !isClose(left.x, left.y)) {
            List<Point> tempLeft = new ArrayList<>();
            for(int i = 0; i < p.size(); i++) {
                tempLeft.add(p.get(i));
            }
            tempLeft.add(left);
            res.add(tempLeft);
        }
        // right
        Point right = getPoint(x.x + 1, x.y);
        if(right != null && !right.cannotMove && !isClose(right.x, right.y)) {
            List<Point> tempRight = new ArrayList<>();
            for(int i = 0; i < p.size(); i++) {
                tempRight.add(p.get(i));
            }
            tempRight.add(right);
            res.add(tempRight);
        }
        // down
        Point down = getPoint(x.x, x.y + 1);
        if(down != null && !down.cannotMove && !isClose(down.x, down.y)) {
            List<Point> tempDown = new ArrayList<>();
            for(int i = 0; i < p.size(); i++) {
                tempDown.add(p.get(i));
            }
            tempDown.add(down);
            res.add(tempDown);
        }
        // up
        Point up = getPoint(x.x, x.y - 1);
        if(up != null && !up.cannotMove && !isClose(up.x, up.y)) {
            List<Point> tempUp = new ArrayList<>();
            for(int i = 0; i < p.size(); i++) {
                tempUp.add(p.get(i));
            }
            tempUp.add(up);
            res.add(tempUp);
        }
        return res;
    }

    public List<Point> astarAlgorithm(Point s, Point t) {
        List<List<Point>> open = new ArrayList<>(0);
        close = new ArrayList<>();
        List<Point> direct = new ArrayList<>();
        direct.add(s);
        open.add(direct);
        while(!open.isEmpty()) {
            int minIdx = findMinPathIdx(open, t);
            List<Point> p = open.get(minIdx);
            open.remove(minIdx);
            Point x = p.get(p.size() - 1);
            if(isClose(x)) {
                continue;
            }
            if(x.equal(t)) {
                return p;
            }
            close.add(x);
            List<List<Point>> next_list = next(p);
            for(int i = 0; i < next_list.size(); i++) {
                open.add(next_list.get(i));
            }
        }
        return null;
    }

    public void gotoNextPoint(Point next) {
        if(round2(x) < next.x) {
            this.x += 0.02 + 0.01 * (speed - 1);
            setImg("kondoria_right");
//            System.out.println("go right");
        } else if(round2(y) < next.y) {
            this.y += 0.02 + 0.01 * (speed - 1);
            setImg("kondoria_right");
//            System.out.println("go down");
        } else if(round2(x) > next.x) {
            this.x -= 0.02 + 0.01 * (speed - 1);
            setImg("kondoria_left");
//            System.out.println("go left");
        } else if(round2(y) > next.y) {
            this.y -= 0.02 + 0.01 * (speed - 1);
            setImg("kondoria_left");
//            System.out.println("go up");
        }
    }

    public void AStar() {
        astar_direct = new ArrayList<>();
        Point k = getPoint((int)Math.round(x), (int)Math.round(y));
        Point bomber = getPoint((int)Math.round(BombermanGame.getBomber().getX()),
                (int)Math.round(BombermanGame.getBomber().getY()));
        List<Point> res = astarAlgorithm(k, bomber);
        if(res != null) {
            for(int i = 0; i < res.size(); i++) {
                astar_direct.add(res.get(i));
            }
        }
    }

    public double round2(double n) {
        return ((double)Math.round(n * 100)/100);
    }

    @Override
    public void moveHandle() {
        x = round2(x);
        y = round2(y);
        if (panic) {
            if (count % (int) (100 / 16) == 0) {
                count = 1;
                panic = false;
            } else {
                setDifOpsDirect();
                super.moveHandle();
                count++;
            }
        }
        if (!panic) {
            if (astar_direct.isEmpty()) {
                if (Entity.round1(x) == Math.round(x) && Entity.round1(y) == Math.round(y)) {
                    AStar();
                }
                if (astar_direct.isEmpty()) {
                    panic = true;
                    astar_direct = new ArrayList<>();
                }
            }
            if (astar_direct.size() > 1) {
                if (round2(x) != astar_direct.get(1).x || round2(y) != astar_direct.get(1).y) {
                    gotoNextPoint(astar_direct.get(1));
                } else {
                    if (Entity.round1(x) == Math.round(x) && Entity.round1(y) == Math.round(y)) {
                        AStar();
                    }
                }
            } else {
                if (astar_direct.size() == 1) {
                    panic = true;
                    astar_direct = new ArrayList<>();
                }
            }
        }
    }
}
