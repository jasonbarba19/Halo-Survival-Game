import processing.core.PImage;
import java.util.List;
public class Grunt extends SpawnerEntities{
    public Grunt(String id, Point position, int actionPeriod, List<PImage> images){
        super(id, position, images, actionPeriod);

    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
        SubEntities elite;

        int change = Functions.rand.nextInt(0, 100);
        if (change > 5){
            elite = new Elite(this.getId() + Functions.ELITE_ID_SUFFIX,
                    pos, Functions.rand.nextInt(Functions.ELITE_ACTION_MIN, Functions.ELITE_ACTION_MAX),
                    Functions.rand.nextInt(Functions.ELITE_ANIMATION_MAX - Functions.ELITE_ANIMATION_MIN),
                    imageStore.getImageList(Functions.ELITE_KEY));
            world.addEntity(elite);
            elite.scheduleActions(scheduler, world, imageStore);

        }
        else{

            elite = new Elite2(this.getId(),
                    pos, 1000, Functions.ELITE_ANIMATION_MIN ,
                    imageStore.getImageList("elite2"));
            world.addEntity(elite);
            elite.scheduleActions(scheduler, world, imageStore);
        }


    }


}
