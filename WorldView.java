import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

/*
WorldView ideally mostly controls drawing the current part of the whole world
that we can see based on the viewport
*/

final class WorldView
{
   private final PApplet screen;
   private final WorldModel world;
   private final int tileWidth;
   private final int tileHeight;
   private final Viewport viewport;

   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
                    int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }

   public Viewport getViewport() {
      return viewport;
   }

   public void drawBackground()
   {
      for (int row = 0; row < this.viewport.getNumRows(); row++)
      {
         for (int col = 0; col < this.viewport.getNumCols(); col++)
         {
            Point worldPoint = this.viewport.viewportToWorld(col, row);
            Optional<PImage> image = this.world.getBackgroundImage(worldPoint);
            if (image.isPresent())
            {
               this.screen.image(image.get(), col * this.tileWidth,
                       row * this.tileHeight);
            }
         }
      }
   }

   public void shiftView(int colDelta, int rowDelta)
   {
      int newCol = clamp(this.viewport.getCol() + colDelta, 0,
              this.world.getNumCols() - this.viewport.getNumCols());
      int newRow = clamp(this.viewport.getRow() + rowDelta, 0,
              this.world.getNumRows() - this.viewport.getNumRows());

      this.viewport.shift(newCol, newRow);
   }

   public static int clamp(int value, int low, int high)
   {
      return Math.min(high, Math.max(value, low));
   }

   public void drawViewport(long game_time, int kill_count)
   {
      this.drawBackground();
      this.drawEntities();
      this.screen.textSize(20);
      this.screen.fill(255, 255, 255);
      this.screen.text("Score: " + game_time / 10, 510, 20);
      this.screen.text("Kill Count:" + kill_count,350 , 20);
   }

   public void drawGameOver(long score){
      this.screen.background(0, 0, 0);
      this.screen.textSize(40);
      this.screen.text("Game Over: No More Laundry", 10, 150);
      this.screen.text("Score: " + score / 10, 10, 210);

   }

   public void drawEntities()
   {
      for (Entity entity : this.world.getEntities())
      {
         Point pos = entity.getPosition();

         if (this.viewport.contains(pos))
         {
            Point viewPoint = this.viewport.worldToViewport(pos.getX(), pos.getY());
            this.screen.image(entity.getCurrentImage(),
                    viewPoint.getX() * this.tileWidth, viewPoint.getY() * this.tileHeight);
         }
      }
   }




}
