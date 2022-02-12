import processing.core.PImage;

import java.util.List;

public abstract class Chief extends MovingEntities{
    private final int resourceLimit;
    private int resourceCount;

    public Chief(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }
    protected int getResourceLimit(){
      return resourceLimit;
    }

   protected int getResourceCount(){
      return resourceCount;
   }

   protected void addResourceCount(){
      this.resourceCount += 1;
   }

    protected Point nextPosition(WorldModel world, Point destPos)
    {
        List<Point> newPos = this.pathingStrategy.computePath(this.getPosition(), destPos, p -> world.withinBounds(p) &&
                        (!world.isOccupied(p) || world.getOccupant(p).get().getClass() == Grunt.class),
                (p1, p2) -> world.adjacent(p1, p2), PathingStrategy.CARDINAL_NEIGHBORS);

        if (newPos.isEmpty()){
            return this.getPosition();
        }
        else{
            return newPos.get(0);
        }
    }

    protected abstract boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
}
