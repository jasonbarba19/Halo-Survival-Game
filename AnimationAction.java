public class AnimationAction extends Action{
    private final int repeatCount;
    private final AnimationEntity animationEntity;

    public AnimationAction(AnimationEntity animationEntity, int repeatCount){
        super();
        this.animationEntity = animationEntity;
        this.repeatCount = repeatCount;
    }

    protected void executeAction(EventScheduler scheduler)
    {
        this.animationEntity.nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.animationEntity,
                    new AnimationAction( this.animationEntity,
                            Math.max(this.repeatCount - 1, 0)),
                    this.animationEntity.getAnimationPeriod());
        }
    }
}
