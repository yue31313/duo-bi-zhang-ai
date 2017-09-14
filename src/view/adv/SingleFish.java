package view.adv;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class SingleFish {
	
	float x;//��ͼƬ��x����
	float y;//��ͼƬ��y���� 
	Bitmap f;//���λͼ
	int i;//������β�ڶ���3��ͼƬ
	float degrees;//�������ͷ������ת�ĽǶ�
	int count;//���ߵĲ���
	int space;//��Ĳ�����С
	int forwardstep;//�㴦��ǰ��״̬�Ĳ���
	public SingleFish(Bitmap f,int space,int fishsign)//����任�Ƕ�ʱ�ٸ�
	{

		int setx=(int) (64*Math.random()*10);  //��������ֵ�λ��
		int sety=(int) (32*Math.random()*10);
		{
			this.x=setx;
			this.y=sety;
		}
		this.f=f;
		this.count=0;
		this.space=space;
//		 �Ƕ�ת���� math.rad(180) 3.1415926535898 
//		deg ����ת�Ƕ� math.deg(math.pi) 180 
		this.degrees=(float) (Math.PI/2*(int)(Math.random()*10));
	
//		this.forwardstep=(int) (Constant.FORWARD_STEP+(Constant.FORWARD_MAX_STEP-Constant.FORWARD_STEP)*Math.random());
	}

	//������ķ���
	public void drawSelf(Canvas canvas,Paint paint,float offsetX,float offsetY)//������ķ������̳и���ķ�����ͬ
	{
		Matrix m1=new Matrix();//ͼƬ��תһ���ĽǶ�
//		toDegrees�����ǰѻ�����ֵ��double��ת��Ϊ�Ƕȡ�
		m1.setRotate((float) Math.toDegrees(degrees), f.getWidth()/2, f.getHeight()/2);//Χ��������תf.getWidth()/2, f.getHeight()/2
		Matrix m2=new Matrix();//ͼƬ�ƶ�һ���ľ���
		m2.setTranslate(x+offsetX, y+offsetY);
		Matrix m3=new Matrix();//���ƶ�ͼƬ����תͼƬ
		m3.setConcat(m2, m1);//��m���кϲ�
		canvas.drawBitmap(f, m3, paint);//������ת���ͼƬ
	}
	public void fishGo()//�������ߵķ���,��Ĳ�����ͬ
	{
		x=(float) (x-space*Math.cos(Math.PI+degrees));
		y=(float) (y+space*Math.sin(Math.PI-degrees));
	}

	

	}
