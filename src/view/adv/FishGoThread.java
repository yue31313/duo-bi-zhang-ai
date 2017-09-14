package view.adv;

public class FishGoThread extends Thread {
	
	GameView gameview;
	
	private boolean fishGoFlag=false;
	private int sleepspan=40;
	
	public FishGoThread(GameView gameview)
	{
		this.gameview=gameview;
	}
	
	@Override
	public void run()
	{
		while(isFishGoFlag())
		{
			try
			{
				//正常鱼走的方法
				synchronized (gameview.afish) {
					for(SingleFish singlefish:gameview.afish)//每一条鱼的坐标都发生变化
					{					
						singlefish.fishGo();//调用鱼走的方法
					}
				}

				
				Thread.sleep(sleepspan);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}		
	}
	public void setFishGoFlag(boolean fishGoFlag) {
		this.fishGoFlag = fishGoFlag;
	}
	public boolean isFishGoFlag() {
		return fishGoFlag;
	}
}
