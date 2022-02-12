import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Main extends AnimationEntity{
    private int killCount = 0;
    public Main(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images){
        super(id, position, images,actionPeriod, animationPeriod);

    }

    public int getKillCount() {
        return killCount;
    }

    public void minusKillCount(int killCount){
        this.killCount -= killCount;
    }

    public boolean shift_position(Point shift, WorldModel world){
        if ((world.withinBounds(shift)) && (!world.isOccupied(shift))){
            this.setPosition(shift);
            return true;
        }
        return false;
    }

    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this, new ActivityAction(this,world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, new AnimationAction(this, 0),getAnimationPeriod());
    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> mainTarget = world.getOccupant(new Point(this.getPosition().getX() + 1, this.getPosition().getY()));

        long nextPeriod = this.getActionPeriod();

        if (mainTarget.isPresent() &&
                (mainTarget.get().getClass() == Grunt.class || mainTarget.get().getClass() == Elite.class ||
                        mainTarget.get().getClass() == Elite2.class)) {
            Point tgtPos = mainTarget.get().getPosition();
            world.removeEntity(mainTarget.get());
            killCount += 1;
            Boom boom = new Boom(tgtPos,
                    imageStore.getImageList(Functions.BOOM_KEY));

            world.addEntity(boom);
            nextPeriod += this.getActionPeriod();
            boom.scheduleActions(scheduler, world, imageStore);

        }
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                nextPeriod);
    }


}
