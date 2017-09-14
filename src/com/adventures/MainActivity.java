package com.adventures;
import view.adv.*;
import game.time.Timer;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import com.example.adventures.R;
import view.rank.RankView;
import view.welcome.WelcomeView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import static android.view.View.INVISIBLE;
//import static com.bn.map.GameSurfaceView.*;
import static com.adventures.Constant.*;
 enum  WhichView {welcome_view,main_menu,setting_view,
	mapchoice_view,game_view,rank_view,win_view,fail_view}
@SuppressLint({ "UseSparseArrays", "HandlerLeak" })
public class MainActivity extends Activity
{
	public static WhichView curr;//��ǰö��ֵ
	WelcomeView welcomeview;//���뻶ӭ����
	GameView gameView;
	Timer time;
	RankView rankview;	//���а����
	boolean collision_soundflag=true;//�Ƿ�����ײ����
	public static boolean easy=true;
	public static boolean normal=false;
	public static boolean hard=false;
	public static int level=0;//��ǰ��ѡ�ؿ�
	int map_level_index=1;//���а�����ѡ����
	public int curr_grade;//��ǰ��Ϸ�ĵ÷�curr_grade=totalSecond-leftSecond;
	SoundPool soundPool;//������
	HashMap<Integer, Integer> soundPoolMap; //������������ID���Զ�������ID��Map
    public Handler hd=new Handler(){
			public void handleMessage(Message msg){
        		switch(msg.what){
	        		case 0://�л����˵�����
	        			goToMainView();
	        		break;
	        		case 1://�л���Ӯ�Ľ���
	        			goToWinView();
	                    break;
	        		case 2://�л�����Ľ���
	        			goToFailView();
	        			break;
	        		case 3://�л�����Ϸ�Ľ���
	        			goToGameView();
	        			break;
	        		case 5://�л������ý���
	        			goToSet();
	        			break;
	        		case 6://�л������а����
	        			goToRankView();
	        			break;
        		}}};
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);        
        //����ȫ����ʾ
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
        //ǿ��Ϊ����
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_HEIGHT=dm.heightPixels;
        SCREEN_WIDTH=dm.widthPixels;  
        initSound();
        initDatabase();  
        goToWelcomeView();//���뻶ӭ����
    }
    //�������ݿ�
    public  void initDatabase(){
    	//������
    	String sql="create table if not exists rank(id int(2) primary key not" +
    			" null,level int(2),grade int(4),time char(20));";
    	SQLiteUtil.createTable(sql);
    }
    //����ʱ��ķ���
    public  void insertTime(int level,int grade)
    {
    	Date d=new Date();
    	int curr_Id;
        String curr_time=(d.getYear()+1900)+"-"+(d.getMonth()+1<10?"0"+
        		(d.getMonth()+1):(d.getMonth()+1))+"-"+d.getDate();
    	String sql_maxId="select max(id) from rank";
    	Vector<Vector<String>> vector=SQLiteUtil.query(sql_maxId);
    	if(vector.get(0).get(0)==null)
    	{
    		curr_Id=0;
    	}
    	else
    	{
    		curr_Id=Integer.parseInt(vector.get(0).get(0))+1;
    	}
    	String sql_insert="insert into rank values("+curr_Id+","+level+
    								","+grade+","+"'"+curr_time+"');";
    	SQLiteUtil.insert(sql_insert);
    }
  //���뻶ӭ����
    public void goToWelcomeView(){
    	if(welcomeview==null){
    		welcomeview=new WelcomeView(this);
    	}
    	setContentView(welcomeview);
    	curr=WhichView.welcome_view;
    }
    //�������˵�
    public void goToMainView(){
    	setContentView(R.layout.main);
    	curr=WhichView.main_menu;
    	GameView.mHeroPosX=GameView.mHeroPosY=32;
    	ImageButton ib_start=(ImageButton)findViewById(R.id.ImageButton_Start);
    	ImageButton ib_rank=(ImageButton)findViewById(R.id.ImageButton_Rank);
    	ImageButton ib_set=(ImageButton)findViewById(R.id.ImageButton_Set);
    	ib_start.setOnClickListener(//���뵽ѡ�ؽ���
              new OnClickListener(){
				public void onClick(View v){
					GameView.mHeroPosX=48;GameView.mHeroPosY=72;
		    		GameView.life=3;
					hd.sendEmptyMessage(3);
				}});
    	ib_rank.setOnClickListener(//�л������а����
              new OnClickListener(){
				public void onClick(View v){
					hd.sendEmptyMessage(6);
				}});
    	ib_set.setOnClickListener(//�л������ý���
              new OnClickListener(){
				public void onClick(View v){
					hd.sendEmptyMessage(5);
				}});}
    //�������ý���
    public void goToSet()
    {
    	setContentView(R.layout.setting);
    	curr=WhichView.setting_view;
    	final CheckBox cb_collision=(CheckBox)findViewById(R.id.CheckBox_collision);
    	final CheckBox easyC=(CheckBox)findViewById(R.id.checkBox1);
    	final CheckBox normalC=(CheckBox)findViewById(R.id.checkBox2);
    	final CheckBox hardC=(CheckBox)findViewById(R.id.checkBox3);
    	cb_collision.setChecked(collision_soundflag);
    	ImageButton ib_ok=(ImageButton)findViewById(R.id.ImageButton_ok);
    	ib_ok.setOnClickListener
    	( 
              new OnClickListener() 
              {
				public void onClick(View v) 
				{
					if(easyC.isChecked()){
						easy=true;
						normal=hard=false;
						normalC.setChecked(normal);
						hardC.setChecked(hard);
					}
					else if(normalC.isChecked()){
						normal=true;
						easy=hard=false;
						easyC.setChecked(false);
						hardC.setChecked(false);
					}
					else if(hardC.isChecked()){
						hard=true;
						easy=normal=false;
						easyC.setChecked(false);
						normalC.setChecked(false);
					}
					collision_soundflag=cb_collision.isChecked();
					//ǰ�����˵�
					hd.sendEmptyMessage(0);
				}
			}
    	);
    }
  //������Ϸ����
    public void goToGameView()
    {
    	if(gameView==null){
			gameView=new GameView(this,SCREEN_HEIGHT,SCREEN_WIDTH,this);
    	}
    	setContentView(gameView);
    	gameView.requestFocus();//��ȡ����
    	gameView.setFocusableInTouchMode(true);//��Ϊ�ɴ���
    	curr=WhichView.game_view;
    }
    //�������а�
    public void goToRankView()
    {
    	if(rankview==null)
    	{
    		rankview = new RankView(this);
    	}    	   	
         setContentView(rankview);         
    	curr=WhichView.rank_view;
    }
    //������سɹ�
    public void goToWinView()
    {
    	setContentView(R.layout.win);
    	curr=WhichView.win_view;
    	TextView tv_score=(TextView)findViewById(R.id.TextView_score);//��ǰ�÷�
        TextView tv_flag=(TextView)findViewById(R.id.TextView_flag);//�Ƿ�ˢ�¼�¼
        ImageButton ib_replay=(ImageButton)findViewById(R.id.ImageButton_Replay);//���水ť
        ImageButton ib_next=(ImageButton)findViewById(R.id.ImageButton_Next);//��һ�ذ�ť
        ImageButton ib_back=(ImageButton)findViewById(R.id.ImageButton_Back);//���ذ�ť
        tv_score.setText("���ص÷�Ϊ:"+curr_grade);
        //��ѯ�������ķ�����¼
        String sql_maxScore="select min(grade) from rank where level="+(level+1);
        System.out.println(sql_maxScore);
    	Vector<Vector<String>> vector=SQLiteUtil.query(sql_maxScore);
    	//�����ǰ����������ʷ��¼,��ˢ�¼�¼
    	
    	if(vector.get(0).get(0)==null||curr_grade<Integer.parseInt(vector.get(0).get(0)))
    	{
    		tv_flag.setText("ˢ�¼�¼!");
    	}
    	else
    	{
    		tv_flag.setText("û��ˢ�¼�¼!");
    	}
    	insertTime(level+1,curr_grade);
    	//�����ǰ�ѵ���ص� ����һ�ذ�ť������
    	if(level==4)
    	{
    		ib_next.setEnabled(false);
    		ib_next.setVisibility(INVISIBLE);
    	}
        ib_replay.setOnClickListener//���水ť����   
    	( 
              new OnClickListener() 
              {
				public void onClick(View v) 
				{
					reset();
					hd.sendEmptyMessage(3);
				}
			}
    	);
        ib_next.setOnClickListener//��һ�ذ�ť����
    	( 
              new OnClickListener() 
              {
				public void onClick(View v) 
				{
					if(level<4)
					{
						level++;
					}
					reset();
					gameView.repaint();
					hd.sendEmptyMessage(3);
				}
			}
    	);
        ib_back.setOnClickListener//���ذ�ť����   ���ص�ѡ�ؽ���
    	( 
              new OnClickListener() 
              {
				public void onClick(View v) 
				{
					reset();
					hd.sendEmptyMessage(0);
				}
			}
    	);
    }
    //�������ʧ��
    public void goToFailView()
    {
    	setContentView(R.layout.fail);
    	curr=WhichView.fail_view;
        ImageButton ib_replay=(ImageButton)findViewById(R.id.Fail_ImageButton_Replay);
        ImageButton ib_back=(ImageButton)findViewById(R.id.Fail_ImageButton_Back);
        ib_replay.setOnClickListener//���水ť����
    	( 
              new OnClickListener() 
              {
				public void onClick(View v) 
				{
					reset();
					hd.sendEmptyMessage(3);
				}
			}
    	);
        ib_back.setOnClickListener//���ذ�ť����   ���ص�ѡ�ؽ���
    	( 
              new OnClickListener() 
              {
				public void onClick(View v) 
				{
					hd.sendEmptyMessage(0);
				}
			}
    	);
    }

    @Override
	protected void onResume() //��дonResume����
    {		
    	super.onResume();

	}
	@Override
	protected void onPause() //��дonPause����
	{		
		super.onPause();
	}
	public void initSound()
    {
			//������
			soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		    soundPoolMap = new HashMap<Integer, Integer>();   
		    //�Զ�������
		    soundPoolMap.put(1, soundPool.load(this, R.raw.dong, 1)); 
    }
    //��������
	public void playSound(int sound, int loop) 
    {
	   if(collision_soundflag)
	   {
		   AudioManager mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);   
		    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   
		    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
		    float volume = streamVolumeCurrent / streamVolumeMax; 
		    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
	   }
	}

	public boolean onKeyDown(int keyCode, KeyEvent e)
    { 
    	if(keyCode==4&&(curr==WhichView.mapchoice_view||curr==WhichView.setting_view||
    			curr==WhichView.rank_view))//����ѡ�ؽ���
    	{
    		

    		goToMainView();
    		GameView.mHeroPosX=GameView.mHeroPosY=32;
    		GameView.life=5;
    		GameView.mIsRunning=false;

    		return true;
    	}
    	if(keyCode==4&&(curr==WhichView.win_view||curr==WhichView.fail_view))//�����ǰ��Ӯ�����
    	{
    		reset();
    		goToMainView();
    		return true;
    	}
    	if(keyCode==4&&curr==WhichView.main_menu)//�����ǰ�����˵�����
    	{
    		System.exit(0);
    		return true;
    	}
    	if(keyCode==4&&curr==WhichView.game_view)//�����ǰ����Ϸ����
    	{
    		reset();
    		GameView.stopAllThreads();
    		goToMainView();
    		return true;
    	}
    	return false;
    }
	public void reset(){
		GameView.mHeroPosX=48;GameView.mHeroPosY=72;
		GameView.life=3;
		if(GameView.mIsRunning==true)
		{GameView.mAllkeyDown=false;
		GameView.stopAllThreads();
		GameView.mIsRunning=false;
		}
	}
}