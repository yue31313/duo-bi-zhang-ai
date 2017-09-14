package view.adv;
import game.time.TimeRunningThread;
import game.time.Timer;

import java.util.ArrayList;
import view.map.Animation;
import view.map.MapView;

import com.adventures.Constant;
import com.adventures.MainActivity;
import com.example.adventures.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable

{	
	public Timer timer;//时间类的引用
    MainActivity activity;
	ArrayList<SingleFish> afish=new ArrayList<SingleFish>();//生成鱼的类表
	ArrayList<SingleFish> del=new ArrayList<SingleFish>();//删除鱼的列表
	Bitmap f1;//怪物图片
	Bitmap f2,good,back;//生命图片
	Bitmap f[],grass,stone,sea,wall;
	Bitmap bmpMultiply;		//乘号图片，用以显示命数
	Bitmap bmpBall;			//小球图片
	Bitmap bmpNumber[];//“数字”图片
	Bitmap breakMarkBitmap;//时间分割符
	static KeyThread keythread;//刷帧线程
	static FishGoThread fishgothread;//控制鱼走的线程
	static FishGeneratorThread fishgeneratorthread;//产生鱼的线程
	static TimeRunningThread timerunningthread;
	/**向下移动动画**/
    public final static int ANIM_DOWN = 0;
    /**向左移动动画**/
    public final static int ANIM_LEFT = 1;
    /**向右移动动画**/
    public final static int ANIM_RIGHT = 2;
    /**向上移动动画**/
    public final static int ANIM_UP = 3;
    /**动画的总数量**/
    public final static int ANIM_COUNT = 4;
    public static final int MAX_LIFE = 3;			//最大生命数
    public static int life = MAX_LIFE;							//初始状态生命数最大	
    Animation mHeroAnim [] = new Animation[ANIM_COUNT];
    Paint mPaint = null;

/**任意键被按下**/
public static boolean mAllkeyDown = false;
/**按键下**/
private static boolean mIskeyDown = false;
/**按键左**/
private static boolean mIskeyLeft = false;
/**按键右**/
private static boolean mIskeyRight = false;
/**按键上**/
private static boolean mIskeyUp = false;
public static boolean islife=true;

//当前绘制动画状态ID
int mAnimationState = 0;
    //tile块的宽高
public final static int TILE_WIDTH =32;
public final static int TILE_HEIGHT = 32;
public int [][]mCollision;
//游戏地图资源
Bitmap mBitmap = null;
//资源文件
Resources mResources = null;

//英雄在行走范围中绘制坐标
int mHeroScreenX = 0;
int mHeroScreenY = 0;
//英雄在地图中的坐标以英雄脚底中心为原点
public static int mHeroPosX = 0;
public static int mHeroPosY= 0;


//备份英雄发生碰撞以前的坐标点
int mBackHeroPosX = 0;
int mBackHeroPosY= 0;

//英雄在地图中绘制坐标
 int mHeroImageX = 32;
 int mHeroImageY= 32;

//英雄在地图二位数组中的索引
int mHeroIndexX = 0;
int mHeroIndexY= 0;

//屏幕宽高才尺寸
int mScreenWidth = 0;
int mScreenHeight = 0;
/**人物图片资源与实际英雄脚底板坐标的偏移**/
public final static int OFF_HERO_X = 16;
public final static int OFF_HERO_Y = 40;
/**主角行走步长**/
public  static int HERO_STEP = 8;
/**游戏主线程**/
private Thread mThread = null;
/**线程循环标志**/
public static  boolean mIsRunning = false;
private SurfaceHolder mSurfaceHolder = null;
private Canvas mCanvas = null;
public GameView(Context context,int screenWidth, int screenHeight,MainActivity activity) {
    super(context);
    this.activity=activity;

    this.getHolder().addCallback(this);
	setFocusableInTouchMode(true);
	//设置背景常亮
	this.setKeepScreenOn(true);
    mPaint = new Paint();
    mPaint.setAntiAlias(true);//打开抗锯齿
    mScreenWidth = screenWidth;
    mScreenHeight = screenHeight;
    initAnimation(context);
    initHero();
    intBack();
    Constant.changeRadio();//调用常量类中常量赋值的方法
    /**获取mSurfaceHolder画地图**/
    mSurfaceHolder = getHolder();
    mSurfaceHolder.addCallback(this);//注册回调接口
    setFocusable(true);
}

private void initHero() {
//	//英雄在地图中绘制坐标

    /**根据图片显示的坐标算出英雄脚底的坐标 **/
    /**X轴+图片宽度的一半 Y轴加图片的高度 **/
    mHeroPosX = mHeroImageX + OFF_HERO_X; //坐标
    mHeroPosY = mHeroImageY + OFF_HERO_Y;
    mHeroIndexX = mHeroPosX / 32;//地图中的索引
    mHeroIndexY = mHeroPosY / 32;
}

//初始化图片资源
public void intBack(){
	 back= BitmapFactory.decodeResource(this.getResources(), R.drawable.backk);
	
}
public void initBitmap(){
		f1=PicLoadUtil.scaleToFit(BitmapFactory.decodeResource(this.getResources(), R.drawable.f1),
				Constant.BIG_FISH_WIDTH, Constant.BIG_FISH_HEIGHT);
		f2=PicLoadUtil.scaleToFit(BitmapFactory.decodeResource(this.getResources(), R.drawable.f2),
				Constant.BIG_FISH_WIDTH, Constant.BIG_FISH_HEIGHT);
		good=PicLoadUtil.scaleToFit(BitmapFactory.decodeResource(this.getResources(), R.drawable.good),
				Constant.BIG_FISH_WIDTH, Constant.BIG_FISH_HEIGHT);
		grass=PicLoadUtil.scaleToFit(BitmapFactory.decodeResource(this.getResources(), R.drawable.grass),
				Constant.BIG_FISH_WIDTH, Constant.BIG_FISH_HEIGHT);
		stone=PicLoadUtil.scaleToFit(BitmapFactory.decodeResource(this.getResources(), R.drawable.stone),
				Constant.BIG_FISH_WIDTH, Constant.BIG_FISH_HEIGHT);
		wall=PicLoadUtil.scaleToFit(BitmapFactory.decodeResource(this.getResources(), R.drawable.brick),
				Constant.BIG_FISH_WIDTH, Constant.BIG_FISH_HEIGHT);
		sea=PicLoadUtil.scaleToFit(BitmapFactory.decodeResource(this.getResources(), R.drawable.sea),
				Constant.BIG_FISH_WIDTH, Constant.BIG_FISH_HEIGHT);
		
	    bmpNumber = new Bitmap[10];			//初始化数字图片资源
		bmpNumber[0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.number0);
		bmpNumber[1] = BitmapFactory.decodeResource(this.getResources(), R.drawable.number1);
		bmpNumber[2] = BitmapFactory.decodeResource(this.getResources(), R.drawable.number2);
		bmpNumber[3] = BitmapFactory.decodeResource(this.getResources(), R.drawable.number3);
		bmpNumber[4] = BitmapFactory.decodeResource(this.getResources(), R.drawable.number4);
		bmpNumber[5] = BitmapFactory.decodeResource(this.getResources(), R.drawable.number5);
		bmpNumber[6] = BitmapFactory.decodeResource(this.getResources(), R.drawable.number6);
		bmpNumber[7] = BitmapFactory.decodeResource(this.getResources(), R.drawable.number7);
		bmpNumber[8] = BitmapFactory.decodeResource(this.getResources(), R.drawable.number8);
		bmpNumber[9] = BitmapFactory.decodeResource(this.getResources(), R.drawable.number9);
		bmpBall = BitmapFactory.decodeResource(this.getResources(), R.drawable.ball); 
		bmpMultiply = BitmapFactory.decodeResource(this.getResources(), R.drawable.multiply);
		//时间分隔符
		breakMarkBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.breakmark);
	
}

private void initAnimation(Context context) {
    //这里可以用循环来处理总之我们需要把动画的ID传进去
    mHeroAnim[ANIM_DOWN] = new Animation(context,new int []{R.drawable.hero_down_a,
    		R.drawable.hero_down_b,R.drawable.hero_down_c,R.drawable.hero_down_d},true);
    mHeroAnim[ANIM_LEFT] = new Animation(context,new int []{R.drawable.hero_left_a,
    		R.drawable.hero_left_b,R.drawable.hero_left_c,R.drawable.hero_left_d},true);
    mHeroAnim[ANIM_RIGHT]= new Animation(context,new int []{R.drawable.hero_right_a,
    		R.drawable.hero_right_b,R.drawable.hero_right_c,R.drawable.hero_right_d},true);
    mHeroAnim[ANIM_UP]   = new Animation(context,new int []{R.drawable.hero_up_a,
    		R.drawable.hero_up_b,R.drawable.hero_up_c,R.drawable.hero_up_d},true);

}

protected void Draw() { 
    mCollision  = MapView.MapC[MainActivity.level];
    if(MainActivity.easy)
    	HERO_STEP=8;
    else if(MainActivity.normal)
    	HERO_STEP=14;
    else if(MainActivity.hard)
    	HERO_STEP=20;
    	
   /**绘制地图**/
    mCanvas.drawBitmap(back,0,0,null);
    DrawLife(mCanvas);
	mCanvas.drawBitmap(bmpBall, 220, 2, null);
	mCanvas.drawBitmap(bmpMultiply, 260, 1, null);
	drawNumber(mCanvas,life);//话生命
	timer.drawSelf(mCanvas, mPaint,0,0);//绘制时间(后边的0,0为偏移量)
	synchronized (afish) {
	for(SingleFish fish:afish)//绘制ArrayList中的每一条鱼
		{
		fish.drawSelf(mCanvas, mPaint,0,0);//后边的0,0为偏移量
		}
	}	    /**更新动画**/
    UpdateAnimation();
	/**绘制动画**/
    RenderAnimation(mCanvas);
}

private void UpdateAnimation() {
    if (mAllkeyDown) {//触摸屏幕启动线程
	if (mIskeyDown) {
	    mAnimationState = ANIM_DOWN;
	    mHeroPosY += HERO_STEP;
	} else if (mIskeyLeft) {
	    mAnimationState = ANIM_LEFT;
	    mHeroPosX -= HERO_STEP;
	} else if (mIskeyRight) {
	    mAnimationState = ANIM_RIGHT;
	    mHeroPosX += HERO_STEP;
	} else if (mIskeyUp) {
	    mAnimationState = ANIM_UP;
	    mHeroPosY -= HERO_STEP;
	}
	if (mHeroPosX <= 0) {//检测是否超出X轴上左边
	    mHeroPosX = 10;
	    mIskeyDown=mIskeyUp=mIskeyLeft=mIskeyRight=false;
	} 
	else if (mHeroPosX >= Constant.SCREEN_WIDTH) {//检测是否超出X轴上右边
	    mHeroPosX = Constant.SCREEN_WIDTH-10;
	    mIskeyDown=mIskeyUp=mIskeyLeft=mIskeyRight=false;
	    }
	else if (mHeroPosY <= 0) {//检测是否超出Y轴上边
	    mHeroPosY = 20;
	    mIskeyDown=mIskeyUp=mIskeyLeft=mIskeyRight=false;
	    } 
	else if (mHeroPosY >= Constant.SCREEN_HEIGHT) {//检测是否超出Y轴下边
	    mHeroPosY = Constant.SCREEN_HEIGHT-10;
	    mIskeyDown=mIskeyUp=mIskeyLeft=mIskeyRight=false;
	    }

	//检测人物是否吃到生命值

	/** 算出英雄移动后在地图二位数组中的索引 **/
	mHeroIndexX = mHeroPosX / 32;
	mHeroIndexY = mHeroPosY / 32;
	/** 越界检测 **/
	int width = mCollision[0].length - 1;
	int height = mCollision.length - 1;
	if (mHeroIndexX <= 0) {
	    mHeroIndexX = 0;
	} else if (mHeroIndexX >= width) {
	    mHeroIndexX = width;
	}
	if (mHeroIndexY <= 0) {
	    mHeroIndexY = 0;
	} else if (mHeroIndexY >= height) {
	    mHeroIndexY = height;
	}
	if(mCollision[mHeroIndexY][mHeroIndexX]==2)
	{
		mCollision[mHeroIndexY][mHeroIndexX]=1;
		if(life<=5)
		life+= 1;				//小球的生命数加1
	}
	/**小球过关*/
	if(mCollision[mHeroIndexY][mHeroIndexX]==3)
	{
		stopAllThreads();//停止所有线程
		this.activity.curr_grade=timer.totalSecond-timer.leftSecond;//剩余的时间
		this.activity.hd.sendEmptyMessage(1);
		mIsRunning=false;
		//小球过关
	}
	//与石头或者走进河里生命值减一且人物位置倒退10点
	if (mCollision[mHeroIndexY][mHeroIndexX] == 6||mCollision[mHeroIndexY][mHeroIndexX] == 4) {
	    mHeroPosX = mBackHeroPosX;
	    mHeroPosY = mBackHeroPosY;
	    if(life>=0){
	    life-= 1;                  //生命值减一，英雄位置倒退一步
	    this.activity.playSound(1, 0);//播放碰闯的声音
	    if(mIskeyRight==true)
	    	mHeroPosX=mHeroPosX-10;
	    if(mIskeyLeft==true)
	    	mHeroPosX=mHeroPosX+10;
	    if(mIskeyDown==true)
	    	mHeroPosY=mHeroPosY-10;
	    if(mIskeyUp==true)
	    	mHeroPosY=mHeroPosY+10;
	    mIskeyDown=mIskeyUp=mIskeyLeft=mIskeyRight=false;
	    }
	}
	else if(mCollision[mHeroIndexY][mHeroIndexX]==5){
		 mHeroPosX = mBackHeroPosX;
	     mHeroPosY = mBackHeroPosY;
		 mIskeyDown=mIskeyUp=mIskeyLeft=mIskeyRight=false;
	}
	else {
	    mBackHeroPosX = mHeroPosX;
	    mBackHeroPosY = mHeroPosY;
	}
	/** 算出人物绘制的XY坐标 **/
	mHeroImageX = mHeroPosX - OFF_HERO_X;
	mHeroImageY = mHeroPosY - OFF_HERO_Y;
    }
}
//判断方向时候的图片动画
private void RenderAnimation(Canvas canvas) {
    if (mAllkeyDown) {
	/**绘制主角动画**/
	mHeroAnim[mAnimationState].DrawAnimation(canvas, mPaint, mHeroImageX, mHeroImageY);
    }else {
	/**按键抬起后人物停止动画**/
	mHeroAnim[mAnimationState].DrawFrame(canvas, mPaint, mHeroImageX, mHeroImageY, 0);
    }
}

public void DrawLife(Canvas canvas){
	for(int i=0;i<mCollision.length;i++){
		for(int j=0;j<mCollision[i].length;j++)
		{
			switch (mCollision[i][j])
			{
			case 1:
				canvas.drawBitmap(grass, j*TILE_WIDTH, i*TILE_WIDTH, null);//画草地
				break;				
			case 2:
				canvas.drawBitmap(f2, j*TILE_WIDTH, i*TILE_WIDTH, null);//画生命值
				break;
			case 3:
				canvas.drawBitmap(good, j*TILE_WIDTH, i*TILE_WIDTH, null);//画过关点
				break;
			case 4:
				canvas.drawBitmap(sea, j*TILE_WIDTH, i*TILE_WIDTH, null);//画海水
				break;
			case 5:
				canvas.drawBitmap(wall, j*TILE_WIDTH, i*TILE_WIDTH, null);//画墙壁
				break;
			case 6:
				canvas.drawBitmap(stone, j*TILE_WIDTH, i*TILE_WIDTH, null);//画石头
				break;
				}
		}
	}
}

public void drawNumber(Canvas canvas,int life) {//绘制数字
		canvas.drawBitmap(bmpNumber[life], 280+16,0, null);
}

public void run() {
    while (mIsRunning) {
	try {
		synchronized (mSurfaceHolder) {
		    /**拿到当前画布 然后锁定**/
		    mCanvas =mSurfaceHolder.lockCanvas();  
		    Draw();
		    /**绘制结束后解锁显示在屏幕上**/
		    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
		}
	    Thread.sleep(100);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	if(life==0){
		stopAllThreads();//停止所有线程
		mIsRunning=false;
		mHeroPosX=mHeroPosY=32;
		this.activity.hd.sendEmptyMessage(2);
	}
	//在这里加上线程安全锁

    }
}
public void overGame(){
	this.activity.hd.sendEmptyMessage(2);
}


public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	// TODO Auto-generated method stub
	
}

public void surfaceCreated(SurfaceHolder arg0) {
	// TODO Auto-generated method stub
	initBitmap();
	keythread=new KeyThread(this);//刷帧线程
	fishgeneratorthread=new FishGeneratorThread(this);//产生鱼的线程
	fishgothread=new FishGoThread(this);//控制鱼走的线程
	timerunningthread=new TimeRunningThread(this);
	timer=new Timer(this,breakMarkBitmap,bmpNumber);//时间类的对象
	//开启全部线程	
	startAllThreads();
	mIsRunning = true;
	mThread = new Thread(this);
	mThread.start();
}

public void surfaceDestroyed(SurfaceHolder arg0) {
	// TODO Auto-generated method stub
	boolean retry = true;
	 
	 stopAllThreads();//停止所有线程
	 mIsRunning = false;
	   while (retry) {
	        try {
	        	timerunningthread.join();
	        	fishgeneratorthread.join();
	        	keythread.join();
	        	fishgothread.join();
	            retry = false;
	        } 
	        catch (InterruptedException e) {e.printStackTrace();}//不断地循环，直到其它线程结束
		}
	
}



	//开启所有线程
public void startAllThreads()
	{
	    timerunningthread.setFlag(true);//倒计时的线程标志位为true
	    timerunningthread.start();
		fishgeneratorthread.setFishgeneratorflag(true);//产生鱼的线程标志位设为true
		keythread.setKeyFlag(true);//刷帧线程的标志位设为true
		fishgothread.setFishGoFlag(true);//鱼走线程的标志位设为true		
		fishgeneratorthread.start();//开启产生鱼的线程
		keythread.start();//开启刷帧线程
		fishgothread.start();//开启鱼走的线程
	}
//停止所有线程
public static void stopAllThreads()
	{
		keythread.setKeyFlag(false);//关闭刷帧线程
		fishgothread.setFishGoFlag(false);//关闭鱼走的线程
		fishgeneratorthread.setFishgeneratorflag(false);//关闭产生鱼的线程
		timerunningthread.setFlag(false);//关闭倒计时线
		
	}	




public boolean onTouchEvent(MotionEvent event) {

		int ievent = event.getAction();
		if (ievent == MotionEvent.ACTION_DOWN) {

			// 得到所点的位置(屏幕上的索引)
			int x = (int) event.getX() / 32;
			int y = (int) event.getY() /32;
			if((x>mHeroIndexX)&&(Math.abs(x-mHeroIndexX)>Math.abs(y-mHeroIndexY))){//向右
				Log.i("TAG", "TOUCH" +"向右");
				mIskeyDown=false;mIskeyUp=false;mIskeyLeft=false;mIskeyRight=true;
				mAllkeyDown=mIskeyRight;
			}
			else if ((x<mHeroIndexX)&&(Math.abs(x-mHeroIndexX)>Math.abs(y-mHeroIndexY))){
				//向zuo
				mIskeyDown=false;mIskeyUp=false;mIskeyLeft=true;mIskeyRight=false;
				mAllkeyDown=mIskeyLeft;
				Log.i("TAG", "TOUCH" +"向zuo");
			}
			else if((y>mHeroIndexY)&&(Math.abs(x-mHeroIndexX)<Math.abs(y-mHeroIndexY))){
				//向下
				
					mIskeyDown=true;mIskeyUp=false;mIskeyLeft=false;mIskeyRight=false;	
					mAllkeyDown=mIskeyDown;
					Log.i("TAG", "TOUCH" +"向下");
				
			}
			else if ((y<mHeroIndexY)&&(Math.abs(x-mHeroIndexX)<Math.abs(y-mHeroIndexY))){
				//向上
				mIskeyDown=false;mIskeyUp=true;mIskeyLeft=false;mIskeyRight=false;	
				mAllkeyDown=mIskeyUp;
				Log.i("TAG", "TOUCH" +"向上");
			}

			Log.i("TAG", "TOUCH" + x + "---" + y);
			// 控制器改变方向
			return true;
		}
		return false;
	}
public void reTur(){//撞墙后反向走
	if(mIskeyRight==true)
	{
		mIskeyRight=false;
		mIskeyLeft=true;		
		mHeroPosX=mHeroPosX-10;
		}
    if(mIskeyLeft==true)
    {
    	mIskeyLeft=false;
    	mIskeyRight=true;
    	mHeroPosX=mHeroPosX+10;
    }
    	
    if(mIskeyDown==true)
    {
    	mIskeyDown=false;
    	mIskeyUp=true;
    	mHeroPosY=mHeroPosY-10;
    }
    if(mIskeyUp==true)
    {
    	mIskeyUp=false;
    	mIskeyDown=true;
    	mHeroPosY=mHeroPosY+10;
    }
}


public void repaint()
{
	Canvas canvas=this.getHolder().lockCanvas();
	try
	{
		synchronized(mSurfaceHolder)
		{
			Draw();
		}
	}catch(Exception e)
	{
		e.printStackTrace();
	}finally
	{
		if(canvas!=null)
		{
			this.getHolder().unlockCanvasAndPost(canvas);
		}
	}
}
}