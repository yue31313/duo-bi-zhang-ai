package view.adv;

public class KeyThread extends Thread {
	GameView gameView;
	private boolean keyFlag=false;
	
	public KeyThread(GameView gameView)
	{
		this.gameView=gameView;
	}
	@Override
	public void run()
	{
		while(isKeyFlag())
		{
			try
			{
				Thread.sleep(20);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
//			gameView.repaint();
		}
	}
	
	public void setKeyFlag(boolean keyFlag) {
		this.keyFlag = keyFlag;
	}
	public boolean isKeyFlag() {
		return keyFlag;
	}
	
}
