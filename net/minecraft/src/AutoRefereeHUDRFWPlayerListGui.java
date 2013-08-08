package net.minecraft.src;

import static net.minecraft.src.AutoRefereeHUD.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class AutoRefereeHUDRFWPlayerListGui extends GuiScreen{
	private AutoReferee autoReferee;
	private ScaledResolution scaledResolution;
	private float scale;
	private float height;
	
	private void addPlayerButtons(ArrayList<AutoRefereePlayer> players, float xOffset){
		float yPos = height + PLAYER_LIST_PLAYERS_Y_OFFSET*scale;
		
		for(AutoRefereePlayer player : players){
			this.buttonList.add(new AutoRefereeButton(xOffset + PLAYER_LIST_HEAD_X_OFFSET*scale, yPos + PLAYER_LIST_HEAD_Y_OFFSET*scale, scale, player, "TP"));
			
			this.buttonList.add(new AutoRefereeButton(xOffset + PLAYER_LIST_BUTTONS_OFFSET*scale, yPos, scale, player, "TPDeath"));
			this.buttonList.add(new AutoRefereeButton(xOffset + PLAYER_LIST_BUTTONS_OFFSET*scale, yPos + PLAYER_LIST_BED_BUTTON_OFFSET*scale, scale, player, "TPBed"));
			this.buttonList.add(new AutoRefereeButton(xOffset + PLAYER_LIST_BUTTONS_OFFSET*scale, yPos + PLAYER_LIST_DOMINATION_Y_OFFSET*scale, scale, player, "VI"));
			this.buttonList.add(new AutoRefereeButton(xOffset + PLAYER_LIST_BUTTONS_OFFSET*scale, yPos + PLAYER_LIST_DEATH_INV_BUTTON_OFFSET*scale, scale, player, "VIDeath"));
			
			yPos += PLAYER_LIST_PLAYER_HEIGHT*scale;
		}
	}
	
	public void initGui(){
		autoReferee = AutoReferee.get();
		scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);

		scale = (float) (scaledResolution.getScaledHeight() - 45) / (float) PLAYER_LIST_BOX_HEIGHT;
		scale = Math.min(scale, (float) (scaledResolution.getScaledWidth()) / (float) (PLAYER_LIST_BOX_WIDTH * 2 + 20));
		height = (scaledResolution.getScaledHeight() - 35) / 2 - PLAYER_LIST_BOX_HEIGHT / 2 * scale;
		height = Math.max(10, height);
		
		AutoRefereeTeam at1 = autoReferee.getLeftTeam(mc.ingameGUI.updateCounter);
		AutoRefereeTeam at2 = autoReferee.getRightTeam(mc.ingameGUI.updateCounter);
		if(at1 != null)
			addPlayerButtons(autoReferee.getPlayersOfTeam(at1),scaledResolution.getScaledWidth() / 2 - PLAYER_LIST_BOX_WIDTH * scale);
		if(at2 != null)
			addPlayerButtons(autoReferee.getPlayersOfTeam(at2),scaledResolution.getScaledWidth() / 2);
	}
	
	public void drawScreen(int par1, int par2, float par3){
		if(!Keyboard.isKeyDown(mc.gameSettings.keyBindPlayerList.keyCode))
			mc.displayGuiScreen((GuiScreen) null);
		
		handleMovements();
		
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
		if (widthName > (PLAYER_LIST_BOX_WIDTH - PLAYER_LIST_BOX_PADDING * 2))
			scaleName = (PLAYER_LIST_BOX_WIDTH - PLAYER_LIST_BOX_PADDING * 2) / widthName * scaleName;

		if (at1 != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(scaledResolution.getScaledWidth() / 2 - PLAYER_LIST_BOX_WIDTH * scale, height, 0);
			GL11.glScalef(scale, scale, scale);
			AutoRefereeHUDRFW.renderTeamInPlayerList(at1, scaleName, mc);
			GL11.glPopMatrix();
		}
		if (at2 != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(scaledResolution.getScaledWidth() / 2, height, 0);
			GL11.glScalef(scale, scale, scale);
			AutoRefereeHUDRFW.renderTeamInPlayerList(at2, scaleName, mc);
			GL11.glPopMatrix();
		}
		
		super.drawScreen(par1, par2, par3);
	}
	
	private void handleMovements(){
		this.mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.keyCode);
		this.mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.keyCode);
		this.mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.keyCode);
		this.mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindRight.keyCode);
		this.mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.keyCode);
		this.mc.gameSettings.keyBindSneak.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.keyCode);
	}

	protected void actionPerformed(GuiButton guiButton)
    {
		if (guiButton.getClass() != AutoRefereeButton.class)
			return;
		AutoRefereeButton button = (AutoRefereeButton) guiButton;
		autoReferee.messageServer(button.message);
		this.mc.displayGuiScreen((GuiScreen)null);
    }
}
