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
	public Timer timer;//ʱ���������
    MainActivity activity;
	ArrayList<SingleFish> afish=new ArrayList<SingleFish>();//����������
	ArrayList<SingleFish> del=new ArrayList<SingleFish>();//ɾ������б�
	Bitmap f1;//����ͼƬ
	Bitmap f2,good,back;//����ͼƬ
	Bitmap f[],grass,stone,sea,wall;
	Bitmap bmpMultiply;		//�˺�ͼƬ��������ʾ����
	Bitmap bmpBall;			//С��ͼƬ
	Bitmap bmpNumber[];//�����֡�ͼƬ
	Bitmap breakMarkBitmap;//ʱ��ָ��
	static KeyThread keythread;//ˢ֡�߳�
	static FishGoThread fishgothread;//�������ߵ��߳�
	static FishGeneratorThread fishgeneratorthread;//��������߳�
	static TimeRunningThread timerunningthread;
	/**�����ƶ�����**/
    public final static int ANIM_DOWN = 0;
    /**�����ƶ�����**/
    public final static int ANIM_LEFT = 1;
    /**�����ƶ�����**/
    public final static int ANIM_RIGHT = 2;
    /**�����ƶ�����**/
    public final static int ANIM_UP = 3;
    /**������������**/
    public final static int ANIM_COUNT = 4;
    public static final int MAX_LIFE = 3;			//���������
    public static int life = MAX_LIFE;							//��ʼ״̬���������	
    Animation mHeroAnim [] = new Animation[ANIM_COUNT];
    Paint mPaint = null;

/**�����������**/
public static boolean mAllkeyDown = false;
/**������**/
private static boolean mIskeyDown = false;
/**������**/
private static boolean mIskeyLeft = false;
/**������**/
private static boolean mIskeyRight = false;
/**������**/
private static boolean mIskeyUp = false;
public static boolean islife=true;

//��ǰ���ƶ���״̬ID
int mAnimationState = 0;
    //tile��Ŀ��
public final static int TILE_WIDTH =32;
public final static int TILE_HEIGHT = 32;
public int [][]mCollision;
//��Ϸ��ͼ��Դ
Bitmap mBitmap = null;
//��Դ�ļ�
Resources mResources = null;

//Ӣ�������߷�Χ�л�������
int mHeroScreenX = 0;
int mHeroScreenY = 0;
//Ӣ���ڵ�ͼ�е�������Ӣ�۽ŵ�����Ϊԭ��
public static int mHeroPosX = 0;
public static int mHeroPosY= 0;


//����Ӣ�۷�����ײ��ǰ�������
int mBackHeroPosX = 0;
int mBackHeroPosY= 0;

//Ӣ���ڵ�ͼ�л�������
 int mHeroImageX = 32;
 int mHeroImageY= 32;

//Ӣ���ڵ�ͼ��λ�����е�����
int mHeroIndexX = 0;
int mHeroIndexY= 0;

//��Ļ��߲ųߴ�
int mScreenWidth = 0;
int mScreenHeight = 0;
/**����ͼƬ��Դ��ʵ��Ӣ�۽ŵװ������ƫ��**/
public final static int OFF_HERO_X = 16;
public final static int OFF_HERO_Y = 40;
/**�������߲���**/
public  static int HERO_STEP = 8;
/**��Ϸ���߳�**/
private Thread mThread = null;
/**�߳�ѭ����־**/
public static  boolean mIsRunning = false;
private SurfaceHolder mSurfaceHolder = null;
private Canvas mCanvas = null;
public GameView(Context context,int screenWidth, int screenHeight,MainActivity activity) {
    super(context);
    this.activity=activity;

    this.getHolder().addCallback(this);
	setFocusableInTouchMode(true);
	//���ñ�������
	this.setKeepScreenOn(true);
    mPaint = new Paint();
    mPaint.setAntiAlias(true);//�򿪿����
    mScreenWidth = screenWidth;
    mScreenHeight = screenHeight;
    initAnimation(context);
    initHero();
    intBack();
    Constant.changeRadio();//���ó������г�����ֵ�ķ���
    /**��ȡmSurfaceHolder����ͼ**/
    mSurfaceHolder = getHolder();
    mSurfaceHolder.addCallback(this);//ע��ص��ӿ�
    setFocusable(true);
}

private void initHero() {
//	//Ӣ���ڵ�ͼ�л�������

    /**����ͼƬ��ʾ���������Ӣ�۽ŵ׵����� **/
    /**X��+ͼƬ��ȵ�һ�� Y���ͼƬ�ĸ߶� **/
    mHeroPosX = mHeroImageX + OFF_HERO_X; //����
    mHeroPosY = mHeroImageY + OFF_HERO_Y;
    mHeroIndexX = mHeroPosX / 32;//��ͼ�е�����
    mHeroIndexY = mHeroPosY / 32;
}

//��ʼ��ͼƬ��Դ
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
		
	    bmpNumber = new Bitmap[10];			//��ʼ������ͼƬ��Դ
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
		//ʱ��ָ���
		breakMarkBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.breakmark);
	
}

private void initAnimation(Context context) {
    //���������ѭ����������֮������Ҫ�Ѷ�����ID����ȥ
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
    	
   /**���Ƶ�ͼ**/
    mCanvas.drawBitmap(back,0,0,null);
    DrawLife(mCanvas);
	mCanvas.drawBitmap(bmpBall, 220, 2, null);
	mCanvas.drawBitmap(bmpMultiply, 260, 1, null);
	drawNumber(mCanvas,life);//������
	timer.drawSelf(mCanvas, mPaint,0,0);//����ʱ��(��ߵ�0,0Ϊƫ����)
	synchronized (afish) {
	for(SingleFish fish:afish)//����ArrayList�е�ÿһ����
		{
		fish.drawSelf(mCanvas, mPaint,0,0);//��ߵ�0,0Ϊƫ����
		}
	}	    /**���¶���**/
    UpdateAnimation();
	/**���ƶ���**/
    RenderAnimation(mCanvas);
}

private void UpdateAnimation() {
    if (mAllkeyDown) {//������Ļ�����߳�
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
	if (mHeroPosX <= 0) {//����Ƿ񳬳�X�������
	    mHeroPosX = 10;
	    mIskeyDown=mIskeyUp=mIskeyLeft=mIskeyRight=false;
	} 
	else if (mHeroPosX >= Constant.SCREEN_WIDTH) {//����Ƿ񳬳�X�����ұ�
	    mHeroPosX = Constant.SCREEN_WIDTH-10;
	    mIskeyDown=mIskeyUp=mIskeyLeft=mIskeyRight=false;
	    }
	else if (mHeroPosY <= 0) {//����Ƿ񳬳�Y���ϱ�
	    mHeroPosY = 20;
	    mIskeyDown=mIskeyUp=mIskeyLeft=mIskeyRight=false;
	    } 
	else if (mHeroPosY >= Constant.SCREEN_HEIGHT) {//����Ƿ񳬳�Y���±�
	    mHeroPosY = Constant.SCREEN_HEIGHT-10;
	    mIskeyDown=mIskeyUp=mIskeyLeft=mIskeyRight=false;
	    }

	//��������Ƿ�Ե�����ֵ

	/** ���Ӣ���ƶ����ڵ�ͼ��λ�����е����� **/
	mHeroIndexX = mHeroPosX / 32;
	mHeroIndexY = mHeroPosY / 32;
	/** Խ���� **/
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
		life+= 1;				//С�����������1
	}
	/**С�����*/
	if(mCollision[mHeroIndexY][mHeroIndexX]==3)
	{
		stopAllThreads();//ֹͣ�����߳�
		this.activity.curr_grade=timer.totalSecond-timer.leftSecond;//ʣ���ʱ��
		this.activity.hd.sendEmptyMessage(1);
		mIsRunning=false;
		//С�����
	}
	//��ʯͷ�����߽���������ֵ��һ������λ�õ���10��
	if (mCollision[mHeroIndexY][mHeroIndexX] == 6||mCollision[mHeroIndexY][mHeroIndexX] == 4) {
	    mHeroPosX = mBackHeroPosX;
	    mHeroPosY = mBackHeroPosY;
	    if(life>=0){
	    life-= 1;                  //����ֵ��һ��Ӣ��λ�õ���һ��
	    this.activity.playSound(1, 0);//��������������
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
	/** ���������Ƶ�XY���� **/
	mHeroImageX = mHeroPosX - OFF_HERO_X;
	mHeroImageY = mHeroPosY - OFF_HERO_Y;
    }
}
//�жϷ���ʱ���ͼƬ����
private void RenderAnimation(Canvas canvas) {
    if (mAllkeyDown) {
	/**�������Ƕ���**/
	mHeroAnim[mAnimationState].DrawAnimation(canvas, mPaint, mHeroImageX, mHeroImageY);
    }else {
	/**����̧�������ֹͣ����**/
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
				canvas.drawBitmap(grass, j*TILE_WIDTH, i*TILE_WIDTH, null);//���ݵ�
				break;				
			case 2:
				canvas.drawBitmap(f2, j*TILE_WIDTH, i*TILE_WIDTH, null);//������ֵ
				break;
			case 3:
				canvas.drawBitmap(good, j*TILE_WIDTH, i*TILE_WIDTH, null);//�����ص�
				break;
			case 4:
				canvas.drawBitmap(sea, j*TILE_WIDTH, i*TILE_WIDTH, null);//����ˮ
				break;
			case 5:
				canvas.drawBitmap(wall, j*TILE_WIDTH, i*TILE_WIDTH, null);//��ǽ��
				break;
			case 6:
				canvas.drawBitmap(stone, j*TILE_WIDTH, i*TILE_WIDTH, null);//��ʯͷ
				break;
				}
		}
	}
}

public void drawNumber(Canvas canvas,int life) {//��������
		canvas.drawBitmap(bmpNumber[life], 280+16,0, null);
}

public void run() {
    while (mIsRunning) {
	try {
		synchronized (mSurfaceHolder) {
		    /**�õ���ǰ���� Ȼ������**/
		    mCanvas =mSurfaceHolder.lockCanvas();  
		    Draw();
		    /**���ƽ����������ʾ����Ļ��**/
		    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
		}
	    Thread.sleep(100);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	if(life==0){
		stopAllThreads();//ֹͣ�����߳�
		mIsRunning=false;
		mHeroPosX=mHeroPosY=32;
		this.activity.hd.sendEmptyMessage(2);
	}
	//����������̰߳�ȫ��

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
	keythread=new KeyThread(this);//ˢ֡�߳�
	fishgeneratorthread=new FishGeneratorThread(this);//��������߳�
	fishgothread=new FishGoThread(this);//�������ߵ��߳�
	timerunningthread=new TimeRunningThread(this);
	timer=new Timer(this,breakMarkBitmap,bmpNumber);//ʱ����Ķ���
	//����ȫ���߳�	
	startAllThreads();
	mIsRunning = true;
	mThread = new Thread(this);
	mThread.start();
}

public void surfaceDestroyed(SurfaceHolder arg0) {
	// TODO Auto-generated method stub
	boolean retry = true;
	 
	 stopAllThreads();//ֹͣ�����߳�
	 mIsRunning = false;
	   while (retry) {
	        try {
	        	timerunningthread.join();
	        	fishgeneratorthread.join();
	        	keythread.join();
	        	fishgothread.join();
	            retry = false;
	        } 
	        catch (InterruptedException e) {e.printStackTrace();}//���ϵ�ѭ����ֱ�������߳̽���
		}
	
}



	//���������߳�
public void startAllThreads()
	{
	    timerunningthread.setFlag(true);//����ʱ���̱߳�־λΪtrue
	    timerunningthread.start();
		fishgeneratorthread.setFishgeneratorflag(true);//��������̱߳�־λ��Ϊtrue
		keythread.setKeyFlag(true);//ˢ֡�̵߳ı�־λ��Ϊtrue
		fishgothread.setFishGoFlag(true);//�����̵߳ı�־λ��Ϊtrue		
		fishgeneratorthread.start();//������������߳�
		keythread.start();//����ˢ֡�߳�
		fishgothread.start();//�������ߵ��߳�
	}
//ֹͣ�����߳�
public static void stopAllThreads()
	{
		keythread.setKeyFlag(false);//�ر�ˢ֡�߳�
		fishgothread.setFishGoFlag(false);//�ر����ߵ��߳�
		fishgeneratorthread.setFishgeneratorflag(false);//�رղ�������߳�
		timerunningthread.setFlag(false);//�رյ���ʱ��
		
	}	




public boolean onTouchEvent(MotionEvent event) {

		int ievent = event.getAction();
		if (ievent == MotionEvent.ACTION_DOWN) {

			// �õ������λ��(��Ļ�ϵ�����)
			int x = (int) event.getX() / 32;
			int y = (int) event.getY() /32;
			if((x>mHeroIndexX)&&(Math.abs(x-mHeroIndexX)>Math.abs(y-mHeroIndexY))){//����
				Log.i("TAG", "TOUCH" +"����");
				mIskeyDown=false;mIskeyUp=false;mIskeyLeft=false;mIskeyRight=true;
				mAllkeyDown=mIskeyRight;
			}
			else if ((x<mHeroIndexX)&&(Math.abs(x-mHeroIndexX)>Math.abs(y-mHeroIndexY))){
				//��zuo
				mIskeyDown=false;mIskeyUp=false;mIskeyLeft=true;mIskeyRight=false;
				mAllkeyDown=mIskeyLeft;
				Log.i("TAG", "TOUCH" +"��zuo");
			}
			else if((y>mHeroIndexY)&&(Math.abs(x-mHeroIndexX)<Math.abs(y-mHeroIndexY))){
				//����
				
					mIskeyDown=true;mIskeyUp=false;mIskeyLeft=false;mIskeyRight=false;	
					mAllkeyDown=mIskeyDown;
					Log.i("TAG", "TOUCH" +"����");
				
			}
			else if ((y<mHeroIndexY)&&(Math.abs(x-mHeroIndexX)<Math.abs(y-mHeroIndexY))){
				//����
				mIskeyDown=false;mIskeyUp=true;mIskeyLeft=false;mIskeyRight=false;	
				mAllkeyDown=mIskeyUp;
				Log.i("TAG", "TOUCH" +"����");
			}

			Log.i("TAG", "TOUCH" + x + "---" + y);
			// �������ı䷽��
			return true;
		}
		return false;
	}
public void reTur(){//ײǽ������
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