import java.util.List;
import java.util.Optional;
import processing.core.PImage;

final class Background
{
   private final String id;
   private final List<PImage> images;
   private int imageIndex;

   public int getImageIndex(){
      return imageIndex;
   }

   public List<PImage> getImages(){
      return images;
   }

   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }

   public PImage getCurrentImage()
   {
      return this.getImages().get((this).getImageIndex());
   }


}
