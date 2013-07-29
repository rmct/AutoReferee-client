//START CODE
package net.minecraft.src;

public class AutoRefereeHUD {
	
	public static int AUTOREFEREE_HUD_ALIGN = 4;
	public static int AUTOREFEREE_HUD_HEIGHT = 10;
	public static float AUTOREFEREE_HUD_SCALE_CUP_ICON = (float) AUTOREFEREE_HUD_HEIGHT / AutoReferee.AUTOREFEREE_ICON_SIZE;
	public static float AUTOREFEREE_HUD_WIDTH_CUP_ICON = AutoReferee.AUTOREFEREE_ICON_SIZE * AUTOREFEREE_HUD_SCALE_CUP_ICON;
	
	public static int PLAYER_LIST_BOX_WIDTH = 150;
	public static int PLAYER_LIST_BOX_HEIGHT = 230;
	public static int PLAYER_LIST_BOX_PADDING = 5;
	public static int PLAYER_LIST_TEAM_OFFSET = 8;
	public static int PLAYER_LIST_PLAYERS_Y_OFFSET = 30;
	public static int PLAYER_LIST_PLAYER_HEIGHT = 50;
	public static int PLAYER_LIST_NAME_OFFSET = 10;
	public static int PLAYER_LIST_ICON_OFFSET = 20;
	public static int PLAYER_LIST_DEATHS_OFFSET = 130;
	public static int PLAYER_LIST_KILLS_OFFSET = 105;
	public static int PLAYER_LIST_ACC_OFFSET = PLAYER_LIST_DEATHS_OFFSET;
	public static int PLAYER_LIST_HEALTH_X_OFFSET = 10;
	public static int PLAYER_LIST_HEALTH_Y_OFFSET = 8;
	public static int PLAYER_LIST_ARMOR_X_OFFSET = PLAYER_LIST_HEALTH_X_OFFSET;
	public static int PLAYER_LIST_ARMOR_Y_OFFSET = 16;
	public static int PLAYER_LIST_HEAD_X_OFFSET = 105;
	public static int PLAYER_LIST_HEAD_Y_OFFSET = 25;
	public static int PLAYER_LIST_OBJECTIVES_X_OFFSET = 10;
	public static int PLAYER_LIST_OBJECTIVES_Y_OFFSET = 27;
	public static int PLAYER_LIST_OBJECTIVE_OFFSET = 16;
	public static int PLAYER_LIST_OBJ_BORDER_WIDTH = 1;
	public static int PLAYER_LIST_OBJ_RECT_WIDTH = 14;
	public static int PLAYER_LIST_DOMINATION_X_OFFSET = 127;
	public static int PLAYER_LIST_DOMINATION_Y_OFFSET = 24;
	public static int PLAYER_LIST_KILL_STREAK_X_OFFSET = PLAYER_LIST_DOMINATION_X_OFFSET;
	public static int PLAYER_LIST_KILL_STREAK_Y_OFFSET = 38;
	
	public static int TEAM_LIST_BOX_WIDTH = 150;
	public static int TEAM_LIST_BOX_PADDING = 5;
	public static int TEAM_LIST_BOX_HEIGHT = 230;
	public static int TEAM_LIST_TEAM_OFFSET = PLAYER_LIST_TEAM_OFFSET;
	public static int TEAM_LIST_OBJECTIVES_Y_OFFSET = 40;
	public static int TEAM_LIST_OBJECTIVES_HEIGHT = 20;
	public static int TEAM_LIST_OBJ_X_OFFSET = 10;
	public static int TEAM_LIST_OBJ_Y_OFFSET = -5;
	public static int TEAM_LIST_OBJ_BORDER_WIDTH = 1;
	public static int TEAM_LIST_OBJ_RECT_WIDTH = 16;
	public static int TEAM_LIST_STATUS_NAME_OFFSET = 30;
	public static int TEAM_LIST_TEXT_OFFSET = 20;

	public static int EVENT_FEED_MARGIN = 5;
	public static int EVENT_FEED_Y_OFFSET = 10;
	public static int EVENT_FEED_HEIGHT = 10;

	public static void renderEventFeed(Minecraft mc) {
		float scale = 1.0F;
		// TODO scale down to not cover other things or so (or move team and player list 10 down... or don't show then)
		AutoReferee autoReferee = AutoReferee.get();
		ScaledResolution scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		String message = autoReferee.lastMessage.getTotalMessage();
		// TODO Long.valueOf("EF3F3F3F", 16).intValue() into constant!
		float scaleText = 0.75F;
		int width = (int) (mc.fontRenderer.getStringWidth(message) * scaleText);
		// TODO change color into team color if objective message
		mc.ingameGUI.drawRect(scaledResolution.getScaledWidth() / 2 - width / 2 - EVENT_FEED_MARGIN, EVENT_FEED_Y_OFFSET, scaledResolution.getScaledWidth() / 2 + width / 2 + EVENT_FEED_MARGIN, (int) (EVENT_FEED_Y_OFFSET + scaleText * EVENT_FEED_HEIGHT), Long.valueOf("EF3F3F3F", 16).intValue());
		// TODO scale text.
		// TODO 16777215 into AutoReferee constant
		// TODO make constants
		float y = EVENT_FEED_HEIGHT / 2 - 6;
		autoReferee.renderCenteredString(message, scaledResolution.getScaledWidth() / 2, EVENT_FEED_Y_OFFSET + 1, scaleText, 16777215, true);
		// TODO add background box if objective
		// TODO make AutoRefereeMessage class to distuinguish objectives from notifications
		// autoReferee.renderItem(35, 8, 8, 10, scaleText * 0.75F);
	}
}
//END CODE