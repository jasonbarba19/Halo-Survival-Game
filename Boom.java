import processing.core.PImage;

import java.awt.*;
import java.util.List;
public class Boom extends StaticEntities{

    public Boom(Point position, List<PImage> images){
        super(Functions.BOOM_ID, position, images, Functions.BOOM_ACTION_PERIOD, Functions.BOOM_ANIMATION_PERIOD);

    }

    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this, new ActivityAction(this, world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, new AnimationAction(this, Functions.BOOM_ANIMATION_REPEAT_COUNT), this.getAnimationPeriod());
    }

}
