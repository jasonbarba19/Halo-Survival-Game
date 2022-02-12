import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class ChiefFull extends Chief {

    public ChiefFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod,
                     List<PImage> images){
        super(id, position, images, resourceLimit, resourceLimit, actionPeriod, animationPeriod);
        this.pathingStrategy = new AStarPathingStrategy();
    }

    protected boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        ChiefNotFull chief = new ChiefNotFull(this.getId(), this.getResourceLimit(),
                this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(chief);
        chief.scheduleActions(scheduler, world, imageStore);
        return true;
    }

    protected boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (world.adjacent(this.getPosition(), target.getPosition()))
        {
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
    {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(),
                WashingMachine.class);

        if (fullTarget.isPresent() &&
                this.moveTo(world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation, Im not too sure about this one
            //((WashingMachine)fullTarget.get()).scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            transform(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this, new ActivityAction(this, world, imageStore),this.getActionPeriod());
        }
    }

}
