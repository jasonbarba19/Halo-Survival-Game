/*
Action: ideally what our various entities might do in our virutal world
 */

public abstract class Action
{
//   public void executeActivityAction(EventScheduler scheduler)
//   {
//      ((SubEntities)this.entity).executeActivity(this.world, this.imageStore, scheduler);
//   }
//
//
//   public void executeAnimationAction(EventScheduler scheduler)
//   {
//      this.entity.nextImage();
//
//      if (this.repeatCount != 1)
//      {
//         scheduler.scheduleEvent(this.entity,
//                 this.entity.createAnimationAction(
//                         Math.max(this.repeatCount - 1, 0)),
//                 this.entity.getAnimationPeriod());
//      }
//   }

   protected abstract void executeAction(EventScheduler scheduler);






}
