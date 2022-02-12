import processing.core.PImage;

import java.util.List;

public abstract class SpawnerEntities extends SubEntities {

    public SpawnerEntities(String id, Point position, List<PImage> images, int actionPeriod)
    {
        super(id, position,images,actionPeriod);
    }
    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,  new ActivityAction(this, world, imageStore), this.getActionPeriod());
    }

    }


