//START CODE
package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class AutoRefereeHUDUHC extends AutoRefereeHUD {

	public static final int TEAM_LIST_PLAYER_BOX_HEIGHT = 10;
	public static final int TEAM_LIST_PLAYER_BOX_WIDTH = 95 + 85;

	public static void renderPlayerList(Minecraft mc) {
		ScaledResolution scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		AutoReferee autoReferee = AutoReferee.get();
		float scale = (float) (scaledResolution.getScaledHeight() - 45) / (float) PLAYER_LIST_BOX_HEIGHT;
		scale = Math.min(scale, (float) (scaledResolution.getScaledWidth()) / (float) (PLAYER_LIST_BOX_WIDTH * 2 + 20));
		float height = (scaledResolution.getScaledHeight() - 35) / 2 - PLAYER_LIST_BOX_HEIGHT / 2 * scale;
		height = Math.max(10, height);
		AutoRefereeTeam at1 = autoReferee.getLeftTeam(mc.ingameGUI.updateCounter);
		AutoRefereeTeam at2 = autoReferee.getRightTeam(mc.ingameGUI.updateCounter);
		// scale down if too large name
		float widthName = 0, widthName2 = 0;
		float scaleName = 1.7F;
		if (at1 != null)
			widthName = mc.fontRenderer.getStringWidth(at1.getName()) * scaleName;
		if (at2 != null)
			widthName2 = mc.fontRenderer.getStringWidth(at2.getName()) * scaleName;
		if (widthName2 > widthName)
			widthName = widthName2;
		if (widthName > (PLAYER_LIST_BOX_WIDTH - PLAYER_LIST_BOX_PADDING * 2))
			scaleName = (PLAYER_LIST_BOX_WIDTH - PLAYER_LIST_BOX_PADDING * 2) / widthName * scaleName;

		if (at1 != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(scaledResolution.getScaledWidth() / 2 - PLAYER_LIST_BOX_WIDTH * scale, height, 0);
			GL11.glScalef(scale, scale, scale);
			renderTeamInPlayerList(at1, scaleName, mc);
			GL11.glPopMatrix();
		}
		if (at2 != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(scaledResolution.getScaledWidth() / 2, height, 0);
			GL11.glScalef(scale, scale, scale);
			renderTeamInPlayerList(at2, scaleName, mc);
			GL11.glPopMatrix();
		}
	}

	private static void renderTeamInPlayerList(AutoRefereeTeam at, float scaleName, Minecraft mc) {
		AutoReferee autoReferee = AutoReferee.get();
		String name = at.getName();
		mc.ingameGUI.drawRect(0, 0, PLAYER_LIST_BOX_WIDTH, PLAYER_LIST_BOX_HEIGHT, at.getBoxColor());
		autoReferee.renderCenteredString(name, PLAYER_LIST_BOX_WIDTH / 2, PLAYER_LIST_TEAM_OFFSET, scaleName, 16777215, true);
		int i = 0, j;
		int health, armor, height;
		for (AutoRefereePlayer apl : autoReferee.getPlayersOfTeam(at)) {
			GL11.glPushMatrix();
			int textcolor = 16777215;
			if (!apl.getLoggedIn()) {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
				textcolor = (256 << 24) + (128 << 16) + (128 << 8) + 128;
			}
			GL11.glTranslatef(0, PLAYER_LIST_PLAYERS_Y_OFFSET + i * PLAYER_LIST_PLAYER_HEIGHT, 0);
			autoReferee.renderString(apl.getName(), PLAYER_LIST_NAME_OFFSET, 0, 1F, textcolor, true);
			autoReferee.renderString(apl.getKills() + "", PLAYER_LIST_KILLS_OFFSET, PLAYER_LIST_HEALTH_Y_OFFSET, 1F, textcolor, true);
			health = apl.getHealth();
			armor = apl.getArmor();
			autoReferee.renderHearts(health, PLAYER_LIST_HEALTH_X_OFFSET, PLAYER_LIST_HEALTH_Y_OFFSET, 1F, true);
			autoReferee.renderArmor(armor, PLAYER_LIST_ARMOR_X_OFFSET, PLAYER_LIST_ARMOR_Y_OFFSET, 1F);
			autoReferee.renderSkinHead(apl.getName(), PLAYER_LIST_HEAD_X_OFFSET, PLAYER_LIST_HEAD_Y_OFFSET, 1F);

			// GOLDEN BARS
			if (apl.getItemAmount(266, 0) != 0) {
				float scale = 0.9F;
				mc.ingameGUI.drawRect(PLAYER_LIST_KILL_STREAK_X_OFFSET, PLAYER_LIST_KILL_STREAK_Y_OFFSET, PLAYER_LIST_KILL_STREAK_X_OFFSET + (int) (16 * scale), PLAYER_LIST_KILL_STREAK_Y_OFFSET + (int) (16 * scale), 0x33FFFFFF);
				autoReferee.renderItem(266, 0, apl.getItemAmount(266, 0), PLAYER_LIST_KILL_STREAK_X_OFFSET, PLAYER_LIST_KILL_STREAK_Y_OFFSET, scale);
			}
			// ENCHANTED GOLDEN APPLES
			if (apl.getItemAmount(322, 1) != 0) {
				float scale = 0.9F;
				mc.ingameGUI.drawRect(PLAYER_LIST_DOMINATION_X_OFFSET, PLAYER_LIST_DOMINATION_Y_OFFSET, PLAYER_LIST_DOMINATION_X_OFFSET + (int) (16 * scale), PLAYER_LIST_DOMINATION_Y_OFFSET + (int) (16 * scale), 0x33FFFFFF);
				autoReferee.renderItem(322, 1, apl.getItemAmount(322, 1), PLAYER_LIST_DOMINATION_X_OFFSET, PLAYER_LIST_DOMINATION_Y_OFFSET, scale);
				// NORMAL GOLDEN APPLES
			} else if (apl.getItemAmount(322, 0) != 0) {
				float scale = 0.9F;
				mc.ingameGUI.drawRect(PLAYER_LIST_DOMINATION_X_OFFSET, PLAYER_LIST_DOMINATION_Y_OFFSET, PLAYER_LIST_DOMINATION_X_OFFSET + (int) (16 * scale), PLAYER_LIST_DOMINATION_Y_OFFSET + (int) (16 * scale), 0x33FFFFFF);
				autoReferee.renderItem(322, 0, apl.getItemAmount(322, 0), PLAYER_LIST_DOMINATION_X_OFFSET, PLAYER_LIST_DOMINATION_Y_OFFSET, scale);
			}

			// DIMENSION
			if ("nether".equalsIgnoreCase(apl.getDimension())) {
				float scale = 0.9F;
				mc.ingameGUI.drawRect(PLAYER_LIST_DOMINATION_X_OFFSET, PLAYER_LIST_HEALTH_Y_OFFSET, PLAYER_LIST_DOMINATION_X_OFFSET + (int) (16 * scale), PLAYER_LIST_HEALTH_Y_OFFSET + (int) (16 * scale), 0x33FFFFFF);
				autoReferee.renderItem(90, 0, PLAYER_LIST_DOMINATION_X_OFFSET, PLAYER_LIST_HEALTH_Y_OFFSET, scale);
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
}
//END CODE