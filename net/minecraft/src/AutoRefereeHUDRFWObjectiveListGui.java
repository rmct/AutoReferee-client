package net.minecraft.src;

import static net.minecraft.src.AutoRefereeHUD.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class AutoRefereeHUDRFWObjectiveListGui extends GuiScreen {
	private AutoReferee autoReferee;
	private ScaledResolution scaledResolution;
	private float scale;
	private float height;
	
	private void addTeamButtons(AutoRefereeTeam team, float xOffset){
		float yPos = height + TEAM_LIST_OBJECTIVES_Y_OFFSET * scale;
		
		for(AutoRefereeObjective obj : team.getObjectives()){
			this.buttonList.add(new AutoRefereeButton(xOffset + TEAM_LIST_OBJ_X_OFFSET*scale+1, yPos +TEAM_LIST_OBJ_Y_OFFSET*scale+1, scale, obj, "OBJ"));
			yPos += TEAM_LIST_OBJECTIVES_HEIGHT*scale;
		}
		this.buttonList.add(new AutoRefereeButton(xOffset + TEAM_LIST_OBJ_X_OFFSET*scale+2, yPos +TEAM_LIST_OBJ_Y_OFFSET*scale+2, scale, team, "VM"));
		yPos += TEAM_LIST_OBJECTIVES_HEIGHT*scale;
		
		this.buttonList.add(new AutoRefereeButton(xOffset + TEAM_LIST_OBJ_X_OFFSET*scale+2, yPos +TEAM_LIST_OBJ_Y_OFFSET*scale+2, scale, team, "SPAWN"));
		yPos += TEAM_LIST_OBJECTIVES_HEIGHT*scale;
	}
	
	public void initGui(){
		autoReferee = AutoReferee.get();
		scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		
		scale = (float) (scaledResolution.getScaledHeight() - 45) / (float) TEAM_LIST_BOX_HEIGHT;
		scale = Math.min(scale, (float) (scaledResolution.getScaledWidth()) / (float) (TEAM_LIST_BOX_WIDTH * 2 + 20));
		height = (scaledResolution.getScaledHeight() - 35) / 2 - TEAM_LIST_BOX_HEIGHT / 2 * scale;
		height = Math.max(10, height);
		
		AutoRefereeTeam at1 = autoReferee.getLeftTeam(mc.ingameGUI.updateCounter);
		AutoRefereeTeam at2 = autoReferee.getRightTeam(mc.ingameGUI.updateCounter);
		if(at1 != null)
			addTeamButtons(at1,scaledResolution.getScaledWidth() / 2 - TEAM_LIST_BOX_WIDTH * scale);
		if(at2 != null)
			addTeamButtons(at2,scaledResolution.getScaledWidth() / 2);
	}
	
	public void drawScreen(int par1, int par2, float par3){
		if(!Keyboard.isKeyDown(mc.gameSettings.keyBindDrop.keyCode))
			mc.displayGuiScreen((GuiScreen) null);
		
		GL11.glEnable(GL11.GL_ALPHA_TEST);
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
		if (widthName > (TEAM_LIST_BOX_WIDTH - TEAM_LIST_BOX_PADDING * 2))
			scaleName = (TEAM_LIST_BOX_WIDTH - TEAM_LIST_BOX_PADDING * 2) / widthName * scaleName;
		if (at1 != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(scaledResolution.getScaledWidth() / 2 - TEAM_LIST_BOX_WIDTH * scale, height, 0);
			GL11.glScalef(scale, scale, scale);
			AutoRefereeHUDRFW.renderTeamInTeamList(at1, scaleName, mc);
			GL11.glPopMatrix();
		}
		if (at2 != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(scaledResolution.getScaledWidth() / 2, height, 0);
			GL11.glScalef(scale, scale, scale);
			AutoRefereeHUDRFW.renderTeamInTeamList(at2, scaleName, mc);
			GL11.glPopMatrix();
		}
		
		super.drawScreen(par1, par2, par3);
	}

	protected void actionPerformed(GuiButton guiButton){
		if (guiButton.getClass() != AutoRefereeButton.class)
			return;
		AutoRefereeButton button = (AutoRefereeButton) guiButton;
		autoReferee.messageServer(button.message);
		this.mc.displayGuiScreen((GuiScreen)null);
	}
}
