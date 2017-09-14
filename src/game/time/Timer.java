package game.time;

import com.adventures.Constant;

import view.adv.GameView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * 
 *��ʱ����
 *
 */
public class Timer {
	GameView gameView;
	public final int totalSecond=60;//������
	private Bitmap breakMarkBitmap;//ʱ��ָ�����λͼ
	private Bitmap[] numberBitmaps;	//����λͼ
	public int leftSecond=totalSecond;//����ʱ�䱣������
	int endX=Constant.SCREEN_WIDTH*3/5;//���ֵ����ϵ�����
	int endY=(int) Constant.BAR_Y;
	int numberWidth;//����ͼƬ�Ŀ��
	int numberHeight;//����ͼƬ�ĸ߶�
	int breakMarkWidth;//ʱ��ָ���Ŀ��
	int breakMarkHeight;//ʱ��ָ����ĸ߶�
	public Timer(GameView gameView,Bitmap breakMarkBitmap,Bitmap[] numberBitmaps)
	{
		this.gameView=gameView;
		this.breakMarkBitmap=breakMarkBitmap;
		this.numberBitmaps=numberBitmaps;
		numberWidth=numberBitmaps[0].getWidth();
		numberHeight=numberBitmaps[0].getHeight();		
		breakMarkWidth=breakMarkBitmap.getWidth();
		breakMarkHeight=breakMarkBitmap.getHeight();
	}
	//����ʱ��ķ���
	public void drawSelf(Canvas canvas,Paint paint,float offsetX,float offsetY)
	{
		int second=this.leftSecond%60;
		int minute=this.leftSecond/60;		
		//��������
		drawNumberBitmap(second,numberBitmaps,endX+offsetX,endY+offsetX,canvas, paint);
		//���Ʒָ���
		int secondLength=(second+"").length()<=1 ? (second+"").length()+1 : (second+"").length();
		int breakMarkX=endX-secondLength*numberWidth-breakMarkWidth;
		canvas.drawBitmap(breakMarkBitmap, breakMarkX, 0,paint);//����ʱ��ָ���ͼƬ
		//���Ʒ���
		int miniteEndX=breakMarkX;
		int miniteEndY=endY;
		drawNumberBitmap(minute,numberBitmaps,miniteEndX+offsetX,miniteEndY+offsetX,canvas, paint);
	}
	//����ʱ��ķ���
	public void subtractTime(int second)
	{
		if(this.leftSecond>0)
		{
			this.leftSecond-=second;
		}
		else//���ʱ��Ϊ0��������Ϸ
		{
			gameView.overGame();
		}
	}
	//������ͼƬ�ķ���
	public void drawNumberBitmap(int number,Bitmap[] numberBitmaps,float endX,float endY,Canvas canvas,Paint paint)
	{
		String numberStr=number+"";
		if(number<10){//��֤��������λ
			numberStr="0"+numberStr;
		}
		for(int i=0;i<numberStr.length();i++)
		{
			char c=numberStr.charAt(i);
			canvas.drawBitmap
			(
					numberBitmaps[c-'0'], 
					endX-numberWidth*(numberStr.length()-i), 
					endY, 
					paint
			);
		}
	}
}
