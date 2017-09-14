package com.adventures;

public class Constant {

	public static int SCREEN_WIDTH;//屏幕的宽
	public static int SCREEN_HEIGHT;//屏幕的高
	
	public static int BIG_FISH_WIDTH;
	public static int BIG_FISH_HEIGHT;
	
	public static int SMALL_FISH_SPEED=2;//物体的步进

	public static int BAR_Y;//得分条的Y坐标
	
	public static void changeRadio()
	{
		
		BIG_FISH_WIDTH=SCREEN_WIDTH/30;//物体的宽
		BIG_FISH_HEIGHT=SCREEN_WIDTH/28;//物体的高
	}
	

}
