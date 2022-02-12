final class Point
{
   private final int x;
   private final int y;
   private Point previous = null;
   private int g;
   private int h;
   private int f;

   public int getX(){
      return x;
   }

   public int getY(){
      return y;
   }

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }
   public Point getPrevious(){
      return previous;
   }

   public void setPrevious(Point previous){
      this.previous = previous;
   }

   public int getG(){
      return g;
   }

   public void setG(int g){
      this.g = g;
   }

   public int getH(){
      return h;
   }

   public void setH(int h){
      this.h = h;
   }

   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

   public int distanceSquared(Point p2)
   {
      int deltaX = this.x - p2.x;
      int deltaY = this.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }
}
