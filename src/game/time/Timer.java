package game.time;

import com.adventures.Constant;

import view.adv.GameView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * 
 *计时器类
 *
 */
public class Timer {
	GameView gameView;
	public final int totalSecond=60;//总秒数
	private Bitmap breakMarkBitmap;//时间分隔符的位图
	private Bitmap[] numberBitmaps;	//数字位图
	public int leftSecond=totalSecond;//将总时间保护起来
	int endX=Constant.SCREEN_WIDTH*3/5;//数字的右上点坐标
	int endY=(int) Constant.BAR_Y;
	int numberWidth;//数字图片的宽度
	int numberHeight;//数字图片的高度
	int breakMarkWidth;//时间分割符的宽度
	int breakMarkHeight;//时间分隔符的高度
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
	//绘制时间的方法
	public void drawSelf(Canvas canvas,Paint paint,float offsetX,float offsetY)
	{
		int second=this.leftSecond%60;
		int minute=this.leftSecond/60;		
		//绘制秒钟
		drawNumberBitmap(second,numberBitmaps,endX+offsetX,endY+offsetX,canvas, paint);
		//绘制分隔符
		int secondLength=(second+"").length()<=1 ? (second+"").length()+1 : (second+"").length();
		int breakMarkX=endX-secondLength*numberWidth-breakMarkWidth;
		canvas.drawBitmap(breakMarkBitmap, breakMarkX, 0,paint);//绘制时间分隔符图片
		//绘制分钟
		int miniteEndX=breakMarkX;
		int miniteEndY=endY;
		drawNumberBitmap(minute,numberBitmaps,miniteEndX+offsetX,miniteEndY+offsetX,canvas, paint);
	}
	//减少时间的方法
	public void subtractTime(int second)
	{
		if(this.leftSecond>0)
		{
			this.leftSecond-=second;
		}
		else//如果时间为0，结束游戏
		{
			gameView.overGame();
		}
	}
	//画数字图片的方法
	public void drawNumberBitmap(int number,Bitmap[] numberBitmaps,float endX,float endY,Canvas canvas,Paint paint)
	{
		String numberStr=number+"";
		if(number<10){//保证至少有两位
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
