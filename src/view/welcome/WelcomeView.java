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
implements SurfaceHolder.Callback   //ʵ���������ڻص��ӿ�
{
	MainActivity activity;//activity������
	Paint paint;      //����
	int currentAlpha=0;  //��ǰ�Ĳ�͸��ֵ
	int sleepSpan=60;      //������ʱ��ms
	Bitmap currentLogo,logos;  //��ǰlogoͼƬ����
	int currentX=40;      //ͼƬλ��
	int currentY=0;
	public WelcomeView(MainActivity activity)
	{
		super(activity);
		this.activity = activity;
		this.getHolder().addCallback(this);  //�����������ڻص��ӿڵ�ʵ����
		paint = new Paint();  //��������
		paint.setAntiAlias(true);  //�򿪿����
		logos=BitmapFactory.decodeResource(activity.getResources(), R.drawable.mainf);		
	}
	public void onDraw(Canvas canvas)
	{	
		//���ƺ��������屳��
		paint.setColor(Color.BLACK);//���û�����ɫ
		paint.setAlpha(255);//���ò�͸����Ϊ255
		canvas.drawRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, paint);
		//����ƽ����ͼ
		if(currentLogo==null)return;
		paint.setAlpha(currentAlpha);		
		canvas.drawBitmap(currentLogo, currentX, currentY, paint);	
	}
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{
	}
	public void surfaceCreated(SurfaceHolder holder) //����ʱ������	
	{	
		new Thread()
		{
			public void run()
			{
					currentLogo=logos;//��ǰͼƬ������
					for(int i=255;i>-10;i=i-10)
					{//��̬����ͼƬ��͸����ֵ�������ػ�	
						currentAlpha=i;
						if(currentAlpha<0)//�����ǰ��͸����С����
						{
							currentAlpha=0;//����͸������Ϊ��
						}
						SurfaceHolder myholder=WelcomeView.this.getHolder();//��ȡ�ص��ӿ�
						Canvas canvas = myholder.lockCanvas();//��ȡ����
						try{
							synchronized(myholder)//ͬ��
							{
								onDraw(canvas);//���л��ƻ���
							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							if(canvas!= null)//�����ǰ������Ϊ��
							{
								myholder.unlockCanvasAndPost(canvas);//��������
							}
						}
						try
						{
							if(i==255)//������ͼƬ����ȴ�һ��
							{
								Thread.sleep(10);
							}
							Thread.sleep(sleepSpan);
						}
						catch(Exception e)//�׳��쳣
						{
							e.printStackTrace();
						}
					
				}
				activity.hd.sendEmptyMessage(0);//������Ϣ�����뵽���˵�����
			}
		}.start();
	}
	public void surfaceDestroyed(SurfaceHolder arg0)
	{//����ʱ������
	}
}