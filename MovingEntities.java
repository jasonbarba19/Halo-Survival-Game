import processing.core.PImage;

import java.util.List;

public abstract class MovingEntities extends AnimationEntity {
    protected PathingStrategy pathingStrategy;
    public MovingEntities(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position,images,actionPeriod, animationPeriod);
    }

    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this, new ActivityAction(this,world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, new AnimationAction(this, 0),getAnimationPeriod());
    }

    protected abstract Point nextPosition(WorldModel world, Point destPos);

    protected abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);







}
