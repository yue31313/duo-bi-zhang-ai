package view.welcome;
import com.adventures.MainActivity;
import com.example.adventures.R;
//import com.adventures.R;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static com.adventures.Constant.*;

@SuppressLint({ "WrongCall", "ViewConstructor" })
public class WelcomeView extends SurfaceView 
implements SurfaceHolder.Callback   //实现生命周期回调接口
{
	MainActivity activity;//activity的引用
	Paint paint;      //画笔
	int currentAlpha=0;  //当前的不透明值
	int sleepSpan=60;      //动画的时延ms
	Bitmap currentLogo,logos;  //当前logo图片引用
	int currentX=40;      //图片位置
	int currentY=0;
	public WelcomeView(MainActivity activity)
	{
		super(activity);
		this.activity = activity;
		this.getHolder().addCallback(this);  //设置生命周期回调接口的实现者
		paint = new Paint();  //创建画笔
		paint.setAntiAlias(true);  //打开抗锯齿
		logos=BitmapFactory.decodeResource(activity.getResources(), R.drawable.mainf);		
	}
	public void onDraw(Canvas canvas)
	{	
		//绘制黑填充矩形清背景
		paint.setColor(Color.BLACK);//设置画笔颜色
		paint.setAlpha(255);//设置不透明度为255
		canvas.drawRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, paint);
		//进行平面贴图
		if(currentLogo==null)return;
		paint.setAlpha(currentAlpha);		
		canvas.drawBitmap(currentLogo, currentX, currentY, paint);	
	}
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{
	}
	public void surfaceCreated(SurfaceHolder holder) //创建时被调用	
	{	
		new Thread()
		{
			public void run()
			{
					currentLogo=logos;//当前图片的引用
					for(int i=255;i>-10;i=i-10)
					{//动态更改图片的透明度值并不断重绘	
						currentAlpha=i;
						if(currentAlpha<0)//如果当前不透明度小于零
						{
							currentAlpha=0;//将不透明度置为零
						}
						SurfaceHolder myholder=WelcomeView.this.getHolder();//获取回调接口
						Canvas canvas = myholder.lockCanvas();//获取画布
						try{
							synchronized(myholder)//同步
							{
								onDraw(canvas);//进行绘制绘制
							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							if(canvas!= null)//如果当前画布不为空
							{
								myholder.unlockCanvasAndPost(canvas);//解锁画布
							}
						}
						try
						{
							if(i==255)//若是新图片，多等待一会
							{
								Thread.sleep(10);
							}
							Thread.sleep(sleepSpan);
						}
						catch(Exception e)//抛出异常
						{
							e.printStackTrace();
						}
					
				}
				activity.hd.sendEmptyMessage(0);//发送消息，进入到主菜单界面
			}
		}.start();
	}
	public void surfaceDestroyed(SurfaceHolder arg0)
	{//销毁时被调用
	}
}