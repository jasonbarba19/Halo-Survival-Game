public class ActivityAction extends Action{
    private final WorldModel world;
    private final ImageStore imageStore;
    private final SubEntities subEntities;

    public ActivityAction(SubEntities subEntities, WorldModel world, ImageStore imageStore){
        super();
        this.subEntities = subEntities;
        this.world = world;
        this.imageStore = imageStore;
    }

    protected void executeAction(EventScheduler scheduler)
    {
        this.subEntities.executeActivity(this.world, this.imageStore, scheduler);
    }

}
