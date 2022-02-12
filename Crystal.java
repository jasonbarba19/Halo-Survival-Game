import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class Crystal extends SpawnerEntities{

    public Crystal(String id, Point position, int actionPeriod, List<PImage> images){
        super(id, position, images, actionPeriod);

    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.getPosition());

        if (openPt.isPresent())
        {
            Grunt grunt = new Grunt(Functions.GRUNT_ID_PREFIX + this.getId(),
                    openPt.get(), Functions.GRUNT_CORRUPT_MIN +
                    Functions.rand.nextInt(Functions.GRUNT_CORRUPT_MAX - Functions.GRUNT_CORRUPT_MIN),
                    imageStore.getImageList(Functions.GRUNT_KEY));
            world.addEntity(grunt);
            grunt.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }
}
