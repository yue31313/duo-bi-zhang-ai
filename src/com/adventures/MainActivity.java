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
	public static WhichView curr;//当前枚举值
	WelcomeView welcomeview;//进入欢迎界面
	GameView gameView;
	Timer time;
	RankView rankview;	//排行榜界面
	boolean collision_soundflag=true;//是否开启碰撞声音
	public static boolean easy=true;
	public static boolean normal=false;
	public static boolean hard=false;
	public static int level=0;//当前所选关卡
	int map_level_index=1;//排行榜中所选关数
	public int curr_grade;//当前游戏的得分curr_grade=totalSecond-leftSecond;
	SoundPool soundPool;//声音池
	HashMap<Integer, Integer> soundPoolMap; //声音池中声音ID与自定义声音ID的Map
    public Handler hd=new Handler(){
			public void handleMessage(Message msg){
        		switch(msg.what){
	        		case 0://切换主菜单界面
	        			goToMainView();
	        		break;
	        		case 1://切换到赢的界面
	        			goToWinView();
	                    break;
	        		case 2://切换到输的界面
	        			goToFailView();
	        			break;
	        		case 3://切换到游戏的界面
	        			goToGameView();
	        			break;
	        		case 5://切换到设置界面
	        			goToSet();
	        			break;
	        		case 6://切换到排行榜界面
	        			goToRankView();
	        			break;
        		}}};
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);        
        //设置全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
        //强制为横屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_HEIGHT=dm.heightPixels;
        SCREEN_WIDTH=dm.widthPixels;  
        initSound();
        initDatabase();  
        goToWelcomeView();//进入欢迎界面
    }
    //创建数据库
    public  void initDatabase(){
    	//创建表
    	String sql="create table if not exists rank(id int(2) primary key not" +
    			" null,level int(2),grade int(4),time char(20));";
    	SQLiteUtil.createTable(sql);
    }
    //插入时间的方法
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
  //进入欢迎界面
    public void goToWelcomeView(){
    	if(welcomeview==null){
    		welcomeview=new WelcomeView(this);
    	}
    	setContentView(welcomeview);
    	curr=WhichView.welcome_view;
    }
    //进入主菜单
    public void goToMainView(){
    	setContentView(R.layout.main);
    	curr=WhichView.main_menu;
    	GameView.mHeroPosX=GameView.mHeroPosY=32;
    	ImageButton ib_start=(ImageButton)findViewById(R.id.ImageButton_Start);
    	ImageButton ib_rank=(ImageButton)findViewById(R.id.ImageButton_Rank);
    	ImageButton ib_set=(ImageButton)findViewById(R.id.ImageButton_Set);
    	ib_start.setOnClickListener(//进入到选关界面
              new OnClickListener(){
				public void onClick(View v){
					GameView.mHeroPosX=48;GameView.mHeroPosY=72;
		    		GameView.life=3;
					hd.sendEmptyMessage(3);
				}});
    	ib_rank.setOnClickListener(//切换到排行榜界面
              new OnClickListener(){
				public void onClick(View v){
					hd.sendEmptyMessage(6);
				}});
    	ib_set.setOnClickListener(//切换到设置界面
              new OnClickListener(){
				public void onClick(View v){
					hd.sendEmptyMessage(5);
				}});}
    //进入设置界面
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
					//前往主菜单
					hd.sendEmptyMessage(0);
				}
			}
    	);
    }
  //进入游戏界面
    public void goToGameView()
    {
    	if(gameView==null){
			gameView=new GameView(this,SCREEN_HEIGHT,SCREEN_WIDTH,this);
    	}
    	setContentView(gameView);
    	gameView.requestFocus();//获取焦点
    	gameView.setFocusableInTouchMode(true);//设为可触控
    	curr=WhichView.game_view;
    }
    //进入排行榜
    public void goToRankView()
    {
    	if(rankview==null)
    	{
    		rankview = new RankView(this);
    	}    	   	
         setContentView(rankview);         
    	curr=WhichView.rank_view;
    }
    //如果闯关成功
    public void goToWinView()
    {
    	setContentView(R.layout.win);
    	curr=WhichView.win_view;
    	TextView tv_score=(TextView)findViewById(R.id.TextView_score);//当前得分
        TextView tv_flag=(TextView)findViewById(R.id.TextView_flag);//是否刷新纪录
        ImageButton ib_replay=(ImageButton)findViewById(R.id.ImageButton_Replay);//重玩按钮
        ImageButton ib_next=(ImageButton)findViewById(R.id.ImageButton_Next);//下一关按钮
        ImageButton ib_back=(ImageButton)findViewById(R.id.ImageButton_Back);//返回按钮
        tv_score.setText("本关得分为:"+curr_grade);
        //查询本关最大的分数记录
        String sql_maxScore="select min(grade) from rank where level="+(level+1);
        System.out.println(sql_maxScore);
    	Vector<Vector<String>> vector=SQLiteUtil.query(sql_maxScore);
    	//如果当前分数大于历史记录,则刷新记录
    	
    	if(vector.get(0).get(0)==null||curr_grade<Integer.parseInt(vector.get(0).get(0)))
    	{
    		tv_flag.setText("刷新纪录!");
    	}
    	else
    	{
    		tv_flag.setText("没有刷新纪录!");
    	}
    	insertTime(level+1,curr_grade);
    	//如果当前已到达关底 则下一关按钮不可用
    	if(level==4)
    	{
    		ib_next.setEnabled(false);
    		ib_next.setVisibility(INVISIBLE);
    	}
        ib_replay.setOnClickListener//重玩按钮监听   
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
        ib_next.setOnClickListener//下一关按钮监听
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
        ib_back.setOnClickListener//返回按钮监听   返回到选关界面
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
    //如果闯关失败
    public void goToFailView()
    {
    	setContentView(R.layout.fail);
    	curr=WhichView.fail_view;
        ImageButton ib_replay=(ImageButton)findViewById(R.id.Fail_ImageButton_Replay);
        ImageButton ib_back=(ImageButton)findViewById(R.id.Fail_ImageButton_Back);
        ib_replay.setOnClickListener//重玩按钮监听
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
        ib_back.setOnClickListener//返回按钮监听   返回到选关界面
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
	protected void onResume() //重写onResume方法
    {		
    	super.onResume();

	}
	@Override
	protected void onPause() //重写onPause方法
	{		
		super.onPause();
	}
	public void initSound()
    {
			//声音池
			soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		    soundPoolMap = new HashMap<Integer, Integer>();   
		    //吃东西音乐
		    soundPoolMap.put(1, soundPool.load(this, R.raw.dong, 1)); 
    }
    //播放声音
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
    			curr==WhichView.rank_view))//返回选关界面
    	{
    		

    		goToMainView();
    		GameView.mHeroPosX=GameView.mHeroPosY=32;
    		GameView.life=5;
    		GameView.mIsRunning=false;

    		return true;
    	}
    	if(keyCode==4&&(curr==WhichView.win_view||curr==WhichView.fail_view))//如果当前在赢输界面
    	{
    		reset();
    		goToMainView();
    		return true;
    	}
    	if(keyCode==4&&curr==WhichView.main_menu)//如果当前在主菜单界面
    	{
    		System.exit(0);
    		return true;
    	}
    	if(keyCode==4&&curr==WhichView.game_view)//如果当前在游戏界面
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