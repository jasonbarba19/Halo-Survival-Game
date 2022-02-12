import processing.core.PImage;

import java.util.List;

public abstract class SubEntities extends Entity {
    final int actionPeriod;


    public SubEntities( String id, Point position, List<PImage> images, int actionPeriod){
        super(id, position, images);
        this.actionPeriod = actionPeriod;

    }
    protected int getActionPeriod() {
      return actionPeriod;
   }

    protected abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
