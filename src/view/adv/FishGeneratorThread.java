package view.adv;

import com.adventures.Constant;
import com.adventures.MainActivity;

public class FishGeneratorThread extends Thread
{
	public static int fishon=800;
	
	SingleFish sfish;
	GameView gameview;
	MainActivity activity;
	private boolean fishgeneratorflag=false;//标志位
	
	public FishGeneratorThread(GameView gameview)
	{
		this.gameview=gameview;
	}
	 
	public void run()
	{
		while(isFishgeneratorflag())
		{
			try
			{
				synchronized (gameview.afish) {
					//生成鱼
					float probability=(float) Math.random();
					if(probability<0.5)//出小鱼的几率大
					{
						SingleFish smallfish=new SingleFish(gameview.f1,Constant.SMALL_FISH_SPEED,1);
						gameview.afish.add(smallfish);//将鱼放入ArrayList中
					}
					//删除指定区域的鱼
					for(SingleFish delfish:gameview.afish)
					{
						if(delfish.x>Constant.SCREEN_WIDTH||delfish.y>Constant.SCREEN_HEIGHT||
								delfish.x<0||delfish.y<0)//删除游出屏幕的鱼
						{
							gameview.del.add(delfish);
						}
						//与英雄发生碰闯删除该怪物
						if(this.contain(delfish)){
							GameView.life--;
							this.gameview.activity.playSound(1, 0);
							gameview.del.add(delfish);
						}
					}
					gameview.afish.removeAll(gameview.del);
					gameview.del.clear();

				}
				Thread.sleep(fishon);//每隔0.5秒钟生成一条新的鱼
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public  boolean isContain(float otherX, float otherY, float otherWidth, float otherHeight){
		//判断两个矩形是否碰撞
		
		if(otherX+otherWidth+10>GameView.mHeroPosX+2&&otherX<GameView.mHeroPosX+30
				&&otherY<GameView.mHeroPosY+45&&otherY+otherHeight+10>GameView.mHeroPosY+2){
			return true;
		}

		return false;
	}
	public  boolean contain(SingleFish ep){
		if(isContain(ep.x, ep.y, ep.f.getWidth(), ep.f.getHeight())){//检测成功
			return true;
			}
		return false;
		}

	public void setFishgeneratorflag(boolean fishgeneratorflag) {
		this.fishgeneratorflag = fishgeneratorflag;
	}

	public boolean isFishgeneratorflag() {
		return fishgeneratorflag;
	}
}
