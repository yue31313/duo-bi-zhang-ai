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
				//�������ߵķ���
				synchronized (gameview.afish) {
					for(SingleFish singlefish:gameview.afish)//ÿһ��������궼�����仯
					{					
						singlefish.fishGo();//�������ߵķ���
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
