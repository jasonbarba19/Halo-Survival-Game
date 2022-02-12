import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import processing.core.PImage;
import processing.core.PApplet;

/*
Functions - everything our virtual world is doing right now - is this a good design?
 */

final class Functions
{
   public static final Random rand = new Random();

   public static final String MAIN_KEY = "main";

   public static final String CHIEF_KEY = "chief";
   public static final int CHIEF_NUM_PROPERTIES = 7;
   public static final int CHIEF_ID = 1;
   public static final int CHIEF_COL = 2;
   public static final int CHIEF_ROW = 3;
   public static final int CHIEF_LIMIT = 4;
   public static final int CHIEF_ACTION_PERIOD = 5;
   public static final int CHIEF_ANIMATION_PERIOD = 6;

   public static final String OBSTACLE_KEY = "obstacle";
   public static final int OBSTACLE_NUM_PROPERTIES = 2;
   public static final int OBSTACLE_ID = 1;

   public static final String GRUNT_KEY = "grunt";
   public static final int GRUNT_NUM_PROPERTIES = 5;
   public static final int GRUNT_ID = 1;
   public static final int GRUNT_COL = 2;
   public static final int GRUNT_ROW = 3;
   public static final int GRUNT_ACTION_PERIOD = 4;

   public static final String WASHER_KEY = "washer";
   public static final int WASHER_NUM_PROPERTIES = 4;
   public static final int WASHER_ID = 1;
   public static final int WASHER_COL = 2;
   public static final int WASHER_ROW = 3;

   public static final String CRYSTAL_KEY = "crystal";
   public static final int CRYSTAL_NUM_PROPERTIES = 3;
   public static final int CRYSTAL_ID = 1;
   public static final int CRYSTAL_ACTION_PERIOD = 2;

   public static final String ELITE_KEY = "elite";
   public static final String ELITE_ID_SUFFIX = " -- elite";
   public static final int ELITE_PERIOD_SCALE = 4;
   public static final int ELITE_ANIMATION_MIN = 50;
   public static final int ELITE_ANIMATION_MAX = 75;
   public static final int ELITE_ACTION_MIN = 1600;
   public static final int ELITE_ACTION_MAX = 2400;

   public static final String BOOM_KEY = "boom";
   public static final String BOOM_ID = "boom";
   public static final int BOOM_ACTION_PERIOD = 1100;
   public static final int BOOM_ANIMATION_PERIOD = 100;
   public static final int BOOM_ANIMATION_REPEAT_COUNT = 10;


   public static final String GRUNT_ID_PREFIX = "grunt -- ";
   public static final int GRUNT_CORRUPT_MIN = 20000;
   public static final int GRUNT_CORRUPT_MAX = 30000;
   public static final int GRUNT_REACH = 1;

   public static final String BGND_KEY = "background";
   public static final int BGND_NUM_PROPERTIES = 4;
   public static final int BGND_ID = 1;
   public static final int BGND_COL = 2;
   public static final int BGND_ROW = 3;

   public static final int COLOR_MASK = 0xffffff;
   public static final int KEYED_IMAGE_MIN = 5;
   private static final int KEYED_RED_IDX = 2;
   private static final int KEYED_GREEN_IDX = 3;
   private static final int KEYED_BLUE_IDX = 4;

   public static final int PROPERTY_KEY = 0;


   public static void processImageLine(Map<String, List<PImage>> images,
                                       String line, PApplet screen)
   {
      String[] attrs = line.split("\\s");
      if (attrs.length >= 2)
      {
         String key = attrs[0];
         PImage img = screen.loadImage(attrs[1]);
         if (img != null && img.width != -1)
         {
            List<PImage> imgs = getImages(images, key);
            imgs.add(img);

            if (attrs.length >= KEYED_IMAGE_MIN)
            {
               int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
               int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
               int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
               setAlpha(img, screen.color(r, g, b), 0);
            }
         }
      }
   }

   public static List<PImage> getImages(Map<String, List<PImage>> images,
                                        String key)
   {
      List<PImage> imgs = images.get(key);
      if (imgs == null)
      {
         imgs = new LinkedList<>();
         images.put(key, imgs);
      }
      return imgs;
   }

   /*
     Called with color for which alpha should be set and alpha value.
     setAlpha(img, color(255, 255, 255), 0));
   */
   public static void setAlpha(PImage img, int maskColor, int alpha)
   {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha)
         {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }


   public static boolean processLine(String line, WorldModel world,
                                     ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return parseBackground(properties, world, imageStore);
            case CHIEF_KEY:
               return parseChief(properties, world, imageStore);
            case OBSTACLE_KEY:
               return parseObstacle(properties, world, imageStore);
            case GRUNT_KEY:
               return parseFish(properties, world, imageStore);
            case WASHER_KEY:
               return parseWashingMachine(properties, world, imageStore);
            case CRYSTAL_KEY:
               return parseCrystal(properties, world, imageStore);
         }
      }

      return false;
   }

   public static boolean parseBackground(String [] properties,
                                         WorldModel world, ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         world.setBackground(pt,
                 new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   public static boolean parseChief(String [] properties, WorldModel world,
                                   ImageStore imageStore)
   {
      if (properties.length == CHIEF_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[CHIEF_COL]),
                 Integer.parseInt(properties[CHIEF_ROW]));
         Entity entity = new ChiefNotFull(properties[CHIEF_ID],
                 Integer.parseInt(properties[CHIEF_LIMIT]),
                 pt,
                 Integer.parseInt(properties[CHIEF_ACTION_PERIOD]),
                 Integer.parseInt(properties[CHIEF_ANIMATION_PERIOD]),
                 imageStore.getImageList(CHIEF_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == CHIEF_NUM_PROPERTIES;
   }

   public static boolean parseObstacle(String [] properties, WorldModel world,
                                       ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {

         Point pt = new Point(rand.nextInt(10, 64),
                 rand.nextInt(30));
         Entity entity = new Obstacle(properties[OBSTACLE_ID],
                 pt, imageStore.getImageList(OBSTACLE_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }



   public static boolean parseFish(String [] properties, WorldModel world,
                                   ImageStore imageStore)
   {
      if (properties.length == GRUNT_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[GRUNT_COL]),
                 Integer.parseInt(properties[GRUNT_ROW]));
         Entity entity = new Grunt(properties[GRUNT_ID],
                 pt, Integer.parseInt(properties[GRUNT_ACTION_PERIOD]),
                 imageStore.getImageList(GRUNT_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == GRUNT_NUM_PROPERTIES;
   }

   public static boolean parseWashingMachine(String [] properties, WorldModel world,
                                       ImageStore imageStore)
   {
      if (properties.length == WASHER_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[WASHER_COL]),
                 Integer.parseInt(properties[WASHER_ROW]));
         Entity entity = new WashingMachine(properties[WASHER_ID],
                 pt, imageStore.getImageList(WASHER_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == WASHER_NUM_PROPERTIES;
   }

   public static boolean parseCrystal(String [] properties, WorldModel world,
                                     ImageStore imageStore)
   {
      if (properties.length == CRYSTAL_NUM_PROPERTIES)
      {
         Point pt = new Point(rand.nextInt(15, 50),
                 rand.nextInt(30));
         Entity entity = new Crystal(properties[CRYSTAL_ID],
                 pt,
                 Integer.parseInt(properties[CRYSTAL_ACTION_PERIOD]),
                 imageStore.getImageList(CRYSTAL_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == CRYSTAL_NUM_PROPERTIES;
   }




}
