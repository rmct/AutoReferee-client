//START CODE
package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class AutoRefereeHUDUHC extends AutoRefereeHUD {

	public static final int TEAM_LIST_PLAYER_BOX_HEIGHT = 10;
	public static final int TEAM_LIST_PLAYER_BOX_WIDTH = 95 + 85;
	public static final int PLAYER_LIST_TEAM_NAME_OFFSET = PLAYER_LIST_ARMOR_Y_OFFSET + 10;
	public static final int PLAYER_LIST_HEAD_Y_OFFSET = AutoRefereeHUD.PLAYER_LIST_HEAD_Y_OFFSET - 8;

	public static void renderPlayerList(Minecraft mc) {
		ScaledResolution scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		AutoReferee autoReferee = AutoReferee.get();
		float scale = (float) (scaledResolution.getScaledHeight() - 45) / (float) PLAYER_LIST_BOX_HEIGHT;
		scale = Math.min(scale, (float) (scaledResolution.getScaledWidth()) / (float) (PLAYER_LIST_BOX_WIDTH * 2 + 20));
		float height = (scaledResolution.getScaledHeight() - 35) / 2 - PLAYER_LIST_BOX_HEIGHT / 2 * scale;
		height = Math.max(10, height);
		
		List<AutoRefereePlayer> leftPlayers = autoReferee.getClosestPlayers(mc.ingameGUI.updateCounter);
		List<AutoRefereePlayer> rightPlayers = autoReferee.getCyclingPlayers(mc.ingameGUI.updateCounter,true);

		float scaleName = 1.5F;
		
		if (leftPlayers != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(scaledResolution.getScaledWidth() / 2 - PLAYER_LIST_BOX_WIDTH * scale, height, 0);
			GL11.glScalef(scale, scale, scale);
			if(leftPlayers.size() != 0)
				renderTeamInPlayerList(leftPlayers, "Closest Players", scaleName, mc);
			else
				renderTeamInPlayerList(rightPlayers, "All Players", scaleName, mc);
			GL11.glPopMatrix();
		}
		if (rightPlayers != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(scaledResolution.getScaledWidth() / 2, height, 0);
			GL11.glScalef(scale, scale, scale);
			if(leftPlayers.size() != 0)
				renderTeamInPlayerList(rightPlayers, "All Players", scaleName, mc);
			else{
				rightPlayers = autoReferee.getCyclingPlayers(mc.ingameGUI.updateCounter+AutoReferee.TEAM_LIST_CYCLE,false);
				renderTeamInPlayerList(rightPlayers, "All Players", scaleName, mc);
			}
			GL11.glPopMatrix();
		}
	}

	private static void renderTeamInPlayerList(List<AutoRefereePlayer> players, String name, float scaleName, Minecraft mc) {
		AutoReferee autoReferee = AutoReferee.get();
		if(players.size() > 0 && name != ""){
			mc.ingameGUI.drawRect(0, 0, PLAYER_LIST_BOX_WIDTH, PLAYER_LIST_PLAYERS_Y_OFFSET, Long.valueOf("EF3F3F3F", 16).intValue());
			autoReferee.renderCenteredString(name, PLAYER_LIST_BOX_WIDTH / 2, PLAYER_LIST_TEAM_OFFSET, scaleName, 16777215, true);
		}
		int i = 0, j;
		int health, armor, height;
		for (AutoRefereePlayer apl : players) {
			GL11.glPushMatrix();
			int textcolor = 16777215;
			if (!apl.getLoggedIn() || apl.getHealth() == 0) {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
				textcolor = (256 << 24) + (128 << 16) + (128 << 8) + 128;
			}
			GL11.glTranslatef(0, PLAYER_LIST_PLAYERS_Y_OFFSET + i * PLAYER_LIST_PLAYER_HEIGHT, 0);
			mc.ingameGUI.drawRect(0, 0, PLAYER_LIST_BOX_WIDTH, PLAYER_LIST_PLAYER_HEIGHT, apl.getTeam().getBoxColor());
			GL11.glTranslatef(0, 3, 0);
			autoReferee.renderString(apl.getName(), PLAYER_LIST_NAME_OFFSET, 0, 1F, textcolor, true);
			autoReferee.renderString(apl.getAccuracyString(), PLAYER_LIST_KILLS_OFFSET, 0, 1F, textcolor, true);
			autoReferee.renderString(apl.getKills() + "", PLAYER_LIST_KILLS_OFFSET, PLAYER_LIST_HEALTH_Y_OFFSET, 1F, textcolor, true);
			health = apl.getHealth();
			armor = apl.getArmor();
			autoReferee.renderHearts(health, PLAYER_LIST_HEALTH_X_OFFSET, PLAYER_LIST_HEALTH_Y_OFFSET, 1F, true);
			autoReferee.renderArmor(armor, PLAYER_LIST_ARMOR_X_OFFSET, PLAYER_LIST_ARMOR_Y_OFFSET, 1F);
			autoReferee.renderSkinHead(apl.getName(), PLAYER_LIST_HEAD_X_OFFSET, PLAYER_LIST_HEAD_Y_OFFSET, 1F);
			if(!autoReferee.isFFA()){
				int teamNameYOffset = PLAYER_LIST_TEAM_NAME_OFFSET;
				if(armor == 0)
					teamNameYOffset = PLAYER_LIST_ARMOR_Y_OFFSET;
				String teamName = apl.getTeam().getName();
				if (teamName.length() > 16)
					teamName = teamName.substring(0, 16);
				autoReferee.renderString("Team: " + teamName, PLAYER_LIST_NAME_OFFSET, teamNameYOffset + 2, 0.7F, textcolor, true);
			}

			// DIMENSION
			int dimensionId = 0;
			if ("nether".equalsIgnoreCase(apl.getDimension())) {dimensionId = 90;}
			else if ("end".equalsIgnoreCase(apl.getDimension())) {dimensionId = 121;}
			if (dimensionId != 0){
				float scale = 0.9F;
				mc.ingameGUI.drawRect(PLAYER_LIST_DOMINATION_X_OFFSET, 0, PLAYER_LIST_DOMINATION_X_OFFSET + (int) (16 * scale), (int) (16 * scale), 0x33FFFFFF);
				autoReferee.renderItem(dimensionId, 0, PLAYER_LIST_DOMINATION_X_OFFSET, 0, scale);
			}
			
			// GOLDEN BARS
			if (apl.getItemAmount(266, 0) != 0) {
				float scale = 0.9F;
				mc.ingameGUI.drawRect(PLAYER_LIST_KILL_STREAK_X_OFFSET, (int) (16 * scale), PLAYER_LIST_KILL_STREAK_X_OFFSET + (int) (16 * scale), 2 * (int) (16 * scale), 0x33FFFFFF);
				autoReferee.renderItem(266, 0, apl.getItemAmount(266, 0), PLAYER_LIST_KILL_STREAK_X_OFFSET, (int) (16 * scale), scale);
			}
			// ENCHANTED GOLDEN APPLES
			/*if (apl.getItemAmount(322, 1) != 0) {
				float scale = 0.9F;
				mc.ingameGUI.drawRect(PLAYER_LIST_DOMINATION_X_OFFSET, PLAYER_LIST_DOMINATION_Y_OFFSET, PLAYER_LIST_DOMINATION_X_OFFSET + (int) (16 * scale), PLAYER_LIST_DOMINATION_Y_OFFSET + (int) (16 * scale), 0x33FFFFFF);
				GL11.glEnable(GL11.GL_ALPHA);
				autoReferee.renderItem(322, 1, apl.getItemAmount(322, 1), PLAYER_LIST_DOMINATION_X_OFFSET, PLAYER_LIST_DOMINATION_Y_OFFSET, scale);
			// NORMAL GOLDEN APPLES
			} else*/ 
			//NORMAL GOLDEN APPLES. NOTCH APPLES DON'T RENDER VERY WELL SO ARE DISPLAYED AS NORMAL APPLE
			if (apl.getItemAmount(322, 0) + apl.getItemAmount(322, 1) != 0) {
				float scale = 0.9F;
				mc.ingameGUI.drawRect(PLAYER_LIST_DOMINATION_X_OFFSET, 2*(int) (16 * scale), PLAYER_LIST_DOMINATION_X_OFFSET + (int) (16 * scale), 3* (int) (16 * scale), 0x33FFFFFF);
				autoReferee.renderItem(322, 0, apl.getItemAmount(322, 0) + apl.getItemAmount(322, 1), PLAYER_LIST_DOMINATION_X_OFFSET, 2*(int) (16 * scale), scale);
			}

			j = 0;
			for (AutoRefereeObjective obj : apl.getObjectives()) {
				mc.ingameGUI.drawRect(PLAYER_LIST_OBJECTIVES_X_OFFSET + PLAYER_LIST_OBJECTIVE_OFFSET * j - PLAYER_LIST_OBJ_BORDER_WIDTH, PLAYER_LIST_OBJECTIVES_Y_OFFSET - PLAYER_LIST_OBJ_BORDER_WIDTH, PLAYER_LIST_OBJECTIVES_X_OFFSET + PLAYER_LIST_OBJECTIVE_OFFSET * j + PLAYER_LIST_OBJ_RECT_WIDTH + PLAYER_LIST_OBJ_BORDER_WIDTH, PLAYER_LIST_OBJECTIVES_Y_OFFSET + PLAYER_LIST_OBJ_RECT_WIDTH + PLAYER_LIST_OBJ_BORDER_WIDTH, 0x33FFFFFF);
				autoReferee.renderItem(obj.getId(), obj.getData(), PLAYER_LIST_OBJECTIVES_X_OFFSET + PLAYER_LIST_OBJECTIVE_OFFSET * j, PLAYER_LIST_OBJECTIVES_Y_OFFSET, 0.9F);
				++j;
			}
			GL11.glDisable(GL11.GL_LIGHTING);
			++i;
			GL11.glPopMatrix();
		}
	}

	public static void renderTeamList(Minecraft mc) {
		GL11.glEnable(GL11.GL_ALPHA_TEST);

		ScaledResolution scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		float scale = (float) (scaledResolution.getScaledHeight() - 45) / ((float) (TEAM_LIST_PLAYER_BOX_HEIGHT+2) * (AutoReferee.get().players.size()/2));
		scale = Math.min(scale, (float) (scaledResolution.getScaledWidth()) / (float) (TEAM_LIST_PLAYER_BOX_WIDTH * 2 + 20));
		scale = Math.min(scale, 1F);

		int counter = 0;
		for (AutoRefereeTeam at : AutoReferee.get().teams.values()) {
			for (AutoRefereePlayer apl : AutoReferee.get().getPlayersOfTeam(at)) {
				GL11.glPushMatrix();
				GL11.glTranslatef(scaledResolution.getScaledWidth() / 2 + (((counter % 2) - 1) * (TEAM_LIST_PLAYER_BOX_WIDTH + 2)) * scale, 10 + ((counter / 2) * (TEAM_LIST_PLAYER_BOX_HEIGHT + 2)) * scale, 0);
				GL11.glScalef(scale, scale, scale);
				renderPlayerInTeamList(mc, apl, 0);
				GL11.glPopMatrix();
				++counter;
			}
		}
	}

	public static void renderPlayerInTeamList(Minecraft mc, AutoRefereePlayer apl, int counter) {
		AutoReferee autoReferee = AutoReferee.get();

		int textcolor = 16777215;
		if (!apl.getLoggedIn()) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
			textcolor = (256 << 24) + (128 << 16) + (128 << 8) + 128;
		}
		GL11.glPushMatrix();
		mc.ingameGUI.drawRect(-1, -1, TEAM_LIST_PLAYER_BOX_WIDTH + 1, TEAM_LIST_PLAYER_BOX_HEIGHT + 1, apl.getTeam().getBoxColor());
		autoReferee.renderString(apl.getName(), 0, 0, 1F, textcolor, true);
		autoReferee.renderHearts(apl.getHealth(), 95, 0, 1F, true);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
	
	public static void renderGeneralHUD(Minecraft mc) {
		float scale = 1.0F;
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		AutoReferee autoReferee = AutoReferee.get();
		ScaledResolution scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		// update time
		if (autoReferee.timeUpdated)
			autoReferee.updateLastTick(mc.ingameGUI.updateCounter);
		if (autoReferee.gameRunning)
			autoReferee.updateTime(mc.ingameGUI.updateCounter);
		// update countdown
		if (autoReferee.countdownUpdated)
			autoReferee.updateLastCountDownTick(mc.ingameGUI.updateCounter);
		if (autoReferee.countingDown)
			autoReferee.updateCountdown(mc.ingameGUI.updateCounter);
		// calculating positions
		float bigggestWidthTeam = 0;
		float align = AUTOREFEREE_HUD_ALIGN * scale;
		int height = AUTOREFEREE_HUD_HEIGHT;
		float widthClock = 0;
		if (!autoReferee.countingDown) {
			widthClock += align;
			widthClock += mc.fontRenderer.getStringWidth(autoReferee.getTime()) * scale;
			widthClock += align;
		} else if (autoReferee.countingDown) {
			widthClock += align;
			widthClock += mc.fontRenderer.getStringWidth(autoReferee.getCountdown()) * scale;
			widthClock += align;
		}
		float xOffset = 0;
		float width = 0;
		String stringRemainingPlayers = autoReferee.getNumberOfPlayersRemainingString();
		float widthText = 2*align + mc.fontRenderer.getStringWidth(stringRemainingPlayers);

		// display time
		xOffset = scaledResolution.getScaledWidth() / 2 - widthClock / 2 - widthText/2;
		xOffset += align;
		String text = autoReferee.getTime();
		if(autoReferee.countingDown)
			text = autoReferee.getCountdown();
		width = mc.fontRenderer.getStringWidth(text) * scale;
		mc.ingameGUI.drawRect((int) (xOffset - align), 0, (int) (xOffset + width + align), height, Long.valueOf("EF3F3F3F", 16).intValue());
		autoReferee.renderString(text, xOffset, 1, scale, 16777215, true);
		xOffset += width + align;
		
		//Render remaining players
		width = mc.fontRenderer.getStringWidth(stringRemainingPlayers);
		xOffset += align;
		mc.ingameGUI.drawRect((int) (xOffset - align), 0, (int) (xOffset + width + align), height, Long.valueOf("EF888888", 16).intValue());
		autoReferee.renderCenteredString(stringRemainingPlayers, xOffset + width / 2, 1, scale, 16777215, false);
		
		
		//Render Closest Player
		List<AutoRefereePlayer> closestPlayers = autoReferee.getClosestPlayers(mc.ingameGUI.updateCounter);
		if (closestPlayers.size() >= 1){
			GL11.glPushMatrix();
			GL11.glTranslatef((scaledResolution.getScaledWidth() - PLAYER_LIST_BOX_WIDTH)/2, scaledResolution.getScaledHeight() - PLAYER_LIST_PLAYERS_Y_OFFSET - PLAYER_LIST_PLAYER_HEIGHT, 0);
			if(!AutoReferee.get().getMinecraft().gameSettings.hideGUI)
				GL11.glTranslatef(0, -22, 0);
			renderTeamInPlayerList(closestPlayers.subList(0, 1), "", 1.0F, mc);
			GL11.glPopMatrix();
		}
	}
}
//END CODE