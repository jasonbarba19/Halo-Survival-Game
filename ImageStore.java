import java.util.*;

import processing.core.PApplet;
import processing.core.PImage;

/*
ImageStore: to ideally keep track of the images used in our virtual world
 */

final class ImageStore
{
   private final Map<String, List<PImage>> images;
   private final List<PImage> defaultImages;

   public Map<String, List<PImage>> getImages(){
      return images;
   }

   public ImageStore(PImage defaultImage)
   {
      this.images = new HashMap<>();
      defaultImages = new LinkedList<>();
      defaultImages.add(defaultImage);
   }

   public List<PImage> getImageList(String key)
   {
      return this.images.getOrDefault(key, this.defaultImages);
   }

   public void loadImages(Scanner in,PApplet screen)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            Functions.processImageLine(this.getImages(), in.nextLine(), screen);
         }
         catch (NumberFormatException e)
         {
            System.out.println(String.format("Image format error on line %d",
                    lineNumber));
         }
         lineNumber++;
      }
   }

}
