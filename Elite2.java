import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Elite2 extends MovingEntities {


    public Elite2(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images){
        super(id, position, images, actionPeriod, animationPeriod);
        this.pathingStrategy = new AStarPathingStrategy();

    }
    protected boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (world.adjacent(this.getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    protected Point nextPosition(WorldModel world, Point destPos)
    {

        List<Point> path = this.pathingStrategy.computePath(this.getPosition(), destPos,
            p -> (world.withinBounds(p) &&
                    !world.isOccupied(p)),
            (p1, p2) -> world.adjacent(p1,p2), PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.isEmpty()){
            return this.getPosition();
        }
        else{
            return path.get(0);
        }


    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> crabTarget = world.findNearest(this.getPosition(), ChiefNotFull.class);
        if (!crabTarget.isPresent()){
            crabTarget = world.findNearest(this.getPosition(), WashingMachine.class);
        }
        long nextPeriod = this.getActionPeriod();

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().getPosition();

            if (this.moveTo(world, crabTarget.get(), scheduler))
            {
                Boom boom = new Boom(tgtPos,
                        imageStore.getImageList(Functions.BOOM_KEY));

                world.addEntity(boom);
                nextPeriod += this.getActionPeriod();
                boom.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                nextPeriod);
    }
}
