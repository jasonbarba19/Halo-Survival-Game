import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class ChiefNotFull extends Chief {


    public ChiefNotFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod, List<PImage> images){
        super( id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
        this.pathingStrategy = new AStarPathingStrategy();
    }

    protected boolean transform( WorldModel world,EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit())
        {
            ChiefFull chief = new ChiefFull(this.getId(), this.getResourceLimit(),
                    this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                    this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(chief);
            chief.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    protected boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (world.adjacent(this.getPosition(), target.getPosition()))
        {
            this.addResourceCount();
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

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {   Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
            Elite.class);
        if (!notFullTarget.isPresent()) {
            Optional<Entity> notFullTarget1 = world.findNearest(this.getPosition(), Grunt.class);
            notFullTarget = notFullTarget1;
        }

        if (!notFullTarget.isPresent() ||
                !this.moveTo(world, notFullTarget.get(), scheduler) ||
                !transform( world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this, new ActivityAction(this, world, imageStore), this.getActionPeriod());
        }
    }
}
