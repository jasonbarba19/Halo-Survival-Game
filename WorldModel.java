import processing.core.PImage;

import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel {
   private final int numRows;
   private final int numCols;
   private final Background background[][];
   private final Entity occupancy[][];
   private final Set<Entity> entities;

   public int getNumRows(){
      return numRows;
   }

   public int getNumCols(){
      return numCols;
   }

   public Set<Entity> getEntities(){
      return entities;
   }

   public WorldModel(int numRows, int numCols, Background defaultBackground) {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++) {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public Background getBackgroundCell(Point pos) {
      return this.background[pos.getY()][pos.getX()];
   }

   public void setBackgroundCell(Point pos, Background background) {
      this.background[pos.getY()][pos.getX()] = background;
   }

   public boolean withinBounds(Point pos) {
      return pos.getY() >= 0 && pos.getY() < this.numRows &&
              pos.getX() >= 0 && pos.getX() < this.numCols;
   }

   public void setBackground(Point pos, Background background) {
      if (this.withinBounds(pos)) {
         this.setBackgroundCell(pos, background);
      }
   }

   public Optional<Entity> findNearest(Point pos, Class kind) {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : this.entities) {
         if (kind.isInstance(entity)) {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }

   public static Optional<Entity> nearestEntity(List<Entity> entities, Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = nearest.getPosition().distanceSquared(pos);

         for (Entity other : entities)
         {
            int otherDistance = other.getPosition().distanceSquared(pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }



   public Entity getOccupancyCell(Point pos) {
      return this.occupancy[pos.getY()][pos.getX()];
   }

   public boolean isOccupied(Point pos) {
      return this.withinBounds(pos) &&
              this.getOccupancyCell(pos) != null;
   }


   public Optional<Entity> getOccupant(Point pos) {
      if (this.isOccupied(pos)) {
         return Optional.of(this.getOccupancyCell(pos));
      } else {
         return Optional.empty();
      }
   }

   public void setOccupancyCell(Point pos, Entity entity) {
      this.occupancy[pos.getY()][pos.getX()] = entity;
   }

   public void removeEntityAt(Point pos) {
      if (this.withinBounds(pos)
              && this.getOccupancyCell(pos) != null) {
         Entity entity = this.getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         this.entities.remove(entity);
         this.setOccupancyCell(pos, null);
      }
   }

   public void removeEntity(Entity entity) {
      this.removeEntityAt(entity.getPosition());
   }

   public void moveEntity(Entity entity, Point pos) {
      Point oldPos = entity.getPosition();
      if (this.withinBounds(pos) && !pos.equals(oldPos)) {
         this.setOccupancyCell(oldPos, null);
         this.removeEntityAt(pos);
         this.setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }


   public void addEntity(Entity entity)
   {

      if (this.withinBounds(entity.getPosition()))
      {
         this.setOccupancyCell(entity.getPosition(), entity);
         this.entities.add(entity);
      }
   }

   public void tryAddEntity(Entity entity)
   {
   if (!this.isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         //throw new IllegalArgumentException("position occupied");
         this.addEntity(entity);
      }

   }

   public void mousePressedEvent(int mouseX, int mouseY, Viewport viewport, EventScheduler scheduler, ImageStore imageStore){
      this.setBackground((viewport.mouseToViewport(mouseX + 32, mouseY)),
              new Background("lava", imageStore.getImageList("lava")));
      this.setBackground((viewport.mouseToViewport(mouseX - 32, mouseY)),
              new Background("lava", imageStore.getImageList("lava")));
      this.setBackground((viewport.mouseToViewport(mouseX, mouseY + 32)),
              new Background("lava", imageStore.getImageList("lava")));
      this.setBackground((viewport.mouseToViewport(mouseX, mouseY - 32)),
              new Background("lava", imageStore.getImageList("lava")));

      ChiefNotFull chief = new ChiefNotFull("chief", 5, viewport.mouseToViewport(mouseX,mouseY), 813, 100, imageStore.getImageList("chief"));
      this.tryAddEntity(chief);
      chief.scheduleActions(scheduler, this, imageStore);
      Optional<Entity> dead = this.findNearest(viewport.mouseToViewport(mouseX, mouseY), Elite.class);
      if (dead.isPresent())
      {
         Elite thing = (Elite)dead.get();
         thing.transform(this, scheduler, imageStore);
      }
   }

   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -Functions.GRUNT_REACH; dy <= Functions.GRUNT_REACH; dy++)
      {
         for (int dx = -Functions.GRUNT_REACH; dx <= Functions.GRUNT_REACH; dx++)
         {
            Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
            if (this.withinBounds(newPt) &&
                    !this.isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public boolean adjacent(Point p1, Point p2)
   {
      return (p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) ||
              (p1.getY() == p2.getY() && Math.abs(p1.getX() - p2.getX()) == 1);
   }


   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (this.withinBounds(pos))
      {
         return Optional.of((this.getBackgroundCell(pos)).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }
}