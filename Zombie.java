import processing.core.PImage;

import java.util.List;

public class Zombie
   extends MobileAnimatedActor
{
	private int imageCount;
   public Zombie(String name, Point position, int rate, int animation_rate, List<PImage> imgs)
   {
      super(name, position, rate, animation_rate, imgs);
      imageCount = 0;
   }

   protected boolean canPassThrough(WorldModel world, Point pt)
   {
      return !world.isOccupied(pt);
   }

   protected boolean move(WorldModel world, WorldEntity miner)
   {
      if (miner == null)
      {
         return false;
      }

      if (adjacent(getPosition(), miner.getPosition()))
      {
    	  Point temp = miner.getPosition();
    	 miner.remove(world);
    	 world.addEntity(new Zombie(this.getName(), temp, this.getRate(),
    			 this.getAnimationRate(), this.getImages()));
         return true;
      }
      else
      {
         world.moveEntity(this, nextPosition(world, miner.getPosition()));
         return false;
      }
   }
   public Action createAction(WorldModel world, ImageStore imageStore)
   {
      Action[] action = { null };
      action[0] = ticks -> {
         removePendingAction(action[0]);


         Actor newEntity = this;

         scheduleAction(world, newEntity,
            newEntity.createAction(world, imageStore),
            ticks + newEntity.getRate());
      };
      return action[0];
   }
   public void nextImage(){
	   System.out.println("In next image");
	   imageCount++;
   }
   public PImage getImage(){
      if (!(imageCount < this.getImages().size())){
    	  imageCount = 0;
      }
      return this.getImages().get(imageCount);
   }
}
