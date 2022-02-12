import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {

        Comparator<Point> pointComparator =
                (Point p1, Point p2)->
                        (p1.getG() + p1.getH())
                                - (p2.getG() + p2.getH());
        Comparator<Point> duplicateComparator =
                (Point p1, Point p2) -> p2.getG() - p1.getG();



        PriorityQueue<Point> openList = new PriorityQueue(pointComparator.thenComparing(duplicateComparator));
        HashMap<Point, Integer> openDictList = new HashMap<>();
        HashMap<Point, Integer> closedDictList = new HashMap<>();
        start.setG(0);
        openList.add(start);
        openDictList.put(start, start.getG());
        while ((openList.peek() != null) && (openList.peek().getG() != 3) && (!withinReach.test(openList.peek(), end))){
            Point current = openList.poll();
            List<Point> neighbors = potentialNeighbors.apply(current).filter(canPassThrough).collect(Collectors.toList());
            for (Point neighbor : neighbors) {
                if(!closedDictList.containsKey(neighbor)) {
                    neighbor.setG(current.getG() + 1);
                    neighbor.setH(Math.abs(end.getX() - neighbor.getX()) + Math.abs(end.getY() - neighbor.getY()));
                    neighbor.setPrevious(current);
                    if ((openDictList.containsKey(neighbor)) && (openDictList.get(neighbor) > neighbor.getG())) {
                        openDictList.replace(neighbor, neighbor.getG());
                        openList.add(neighbor);
                    }
                    openList.add(neighbor);
                    openDictList.put(neighbor, neighbor.getG());
                }
            }
            closedDictList.put(current, current.getG());
        }
        Point lastNode = openList.poll();
        List<Point> path = new ArrayList<>();


        while((lastNode != null) && (lastNode != start))
        {
            path.add(0, lastNode);
            lastNode = lastNode.getPrevious();
        }

        return path;
    }
}
