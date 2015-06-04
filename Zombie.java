import processing.core.PImage;

import java.util.List;

public class Zombie
   extends MobileAnimatedActor
{
	private int imageCount;
	public int zombieCount;
   public Zombie(String name, Point position, int rate, int animation_rate, List<PImage> imgs, int zombieCount)
   {
      super(name, position, rate, animation_rate, imgs);
      imageCount = 0;
      this.zombieCount = zombieCount;
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

         WorldEntity target = world.findNearest(getPosition(), Miner.class);
         
         Actor newEntity = this;
         
         if (move(world, target))
         {
        	 Point temp = target.getPosition();
        	 target.remove(world);
        	 Zombie newZombie = new Zombie(this.getName() + zombieCount + 1, temp, 2300, 300, this.getImages(), zombieCount);
        	 world.addEntity(newZombie);
        	 newZombie.schedule(world, newZombie.getRate(), imageStore);
         }

         scheduleAction(world, newEntity,
            newEntity.createAction(world, imageStore),
            ticks + newEntity.getRate());
      };
      return action[0];
   }
   
   
   
   public void nextImage(){
	   imageCount++;
   }
   public PImage getImage(){
      if (!(imageCount < this.getImages().size())){
    	  imageCount = 0;
      }
      return this.getImages().get(imageCount);
   }
   
   
   protected Point nextPosition(WorldModel world, Point dest_pt){
	  int horiz = Integer.signum(dest_pt.x - getPosition().x);
      Point new_pt = new Point(getPosition().x + horiz, getPosition().y);

      if (horiz == 0 || !canPassThrough(world, new_pt))
      {
         int vert = Integer.signum(dest_pt.y - getPosition().y);
         new_pt = new Point(getPosition().x, getPosition().y + vert);

         if (vert == 0 || !canPassThrough(world, new_pt))
         {
            new_pt = getPosition();
         }
      }

      return new_pt;
   }
}
