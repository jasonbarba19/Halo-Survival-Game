import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


public class Entity {
   private final String id;
   private Point position;
   private final List<PImage> images;
   private int imageIndex;



   protected String getId(){
      return id;
   }

   protected Point getPosition(){
      return position;
   }

   protected List<PImage> getImages(){
      return images;
   }

   protected int getImageIndex(){
      return  imageIndex;
   }



   public PImage getCurrentImage()
   {
         return(this.getImages().get(this.getImageIndex()));
   }

   protected void setPosition(Point position){
      this.position = position;
   }



   public Entity(String id, Point position,
                 List<PImage> images) {
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
   }


   public void nextImage()
   {
      this.imageIndex = (this.imageIndex + 1) % this.images.size();
   }



}


