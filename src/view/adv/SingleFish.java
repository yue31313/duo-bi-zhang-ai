package view.adv;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class SingleFish {
	
	float x;//鱼图片的x坐标
	float y;//鱼图片的y坐标 
	Bitmap f;//鱼的位图
	int i;//控制鱼尾摆动的3张图片
	float degrees;//相对于鱼头向左，旋转的角度
	int count;//鱼走的步数
	int space;//鱼的步进大小
	int forwardstep;//鱼处于前进状态的步数
	public SingleFish(Bitmap f,int space,int fishsign)//随机变换角度时再改
	{

		int setx=(int) (64*Math.random()*10);  //鱼随机出现的位置
		int sety=(int) (32*Math.random()*10);
		{
			this.x=setx;
			this.y=sety;
		}
		this.f=f;
		this.count=0;
		this.space=space;
//		 角度转弧度 math.rad(180) 3.1415926535898 
//		deg 弧度转角度 math.deg(math.pi) 180 
		this.degrees=(float) (Math.PI/2*(int)(Math.random()*10));
	
//		this.forwardstep=(int) (Constant.FORWARD_STEP+(Constant.FORWARD_MAX_STEP-Constant.FORWARD_STEP)*Math.random());
	}

	//绘制鱼的方法
	public void drawSelf(Canvas canvas,Paint paint,float offsetX,float offsetY)//绘制鱼的方法，继承该类的方法相同
	{
		Matrix m1=new Matrix();//图片旋转一定的角度
//		toDegrees方法是把弧度数值（double）转化为角度。
		m1.setRotate((float) Math.toDegrees(degrees), f.getWidth()/2, f.getHeight()/2);//围绕中心旋转f.getWidth()/2, f.getHeight()/2
		Matrix m2=new Matrix();//图片移动一定的距离
		m2.setTranslate(x+offsetX, y+offsetY);
		Matrix m3=new Matrix();//先移动图片再旋转图片
		m3.setConcat(m2, m1);//对m进行合并
		canvas.drawBitmap(f, m3, paint);//绘制旋转后的图片
	}
	public void fishGo()//吃人物走的方法,鱼的步进不同
	{
		x=(float) (x-space*Math.cos(Math.PI+degrees));
		y=(float) (y+space*Math.sin(Math.PI-degrees));
	}

	

	}
