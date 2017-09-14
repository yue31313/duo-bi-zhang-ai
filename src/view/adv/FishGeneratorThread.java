package view.adv;

import com.adventures.Constant;
import com.adventures.MainActivity;

public class FishGeneratorThread extends Thread
{
	public static int fishon=800;
	
	SingleFish sfish;
	GameView gameview;
	MainActivity activity;
	private boolean fishgeneratorflag=false;//��־λ
	
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
					//������
					float probability=(float) Math.random();
					if(probability<0.5)//��С��ļ��ʴ�
					{
						SingleFish smallfish=new SingleFish(gameview.f1,Constant.SMALL_FISH_SPEED,1);
						gameview.afish.add(smallfish);//�������ArrayList��
					}
					//ɾ��ָ���������
					for(SingleFish delfish:gameview.afish)
					{
						if(delfish.x>Constant.SCREEN_WIDTH||delfish.y>Constant.SCREEN_HEIGHT||
								delfish.x<0||delfish.y<0)//ɾ���γ���Ļ����
						{
							gameview.del.add(delfish);
						}
						//��Ӣ�۷�������ɾ���ù���
						if(this.contain(delfish)){
							GameView.life--;
							this.gameview.activity.playSound(1, 0);
							gameview.del.add(delfish);
						}
					}
					gameview.afish.removeAll(gameview.del);
					gameview.del.clear();

				}
				Thread.sleep(fishon);//ÿ��0.5��������һ���µ���
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public  boolean isContain(float otherX, float otherY, float otherWidth, float otherHeight){
		//�ж����������Ƿ���ײ
		
		if(otherX+otherWidth+10>GameView.mHeroPosX+2&&otherX<GameView.mHeroPosX+30
				&&otherY<GameView.mHeroPosY+45&&otherY+otherHeight+10>GameView.mHeroPosY+2){
			return true;
		}

		return false;
	}
	public  boolean contain(SingleFish ep){
		if(isContain(ep.x, ep.y, ep.f.getWidth(), ep.f.getHeight())){//���ɹ�
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
