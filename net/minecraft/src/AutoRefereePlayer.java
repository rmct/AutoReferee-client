//START CODE
package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class AutoRefereePlayer {
	private String username;
	private int health;
	private int armor;
	private int kills;
	private int deaths;
	private ArrayList<AutoRefereeObjective> objectives;
	private ArrayList<String> domination;
	private int killstreak;
	private int accuracy;
	private AutoRefereeTeam team;
	private boolean loggedIn;
	private HashMap<String, Integer> itemAmounts = new HashMap<String, Integer>();
	private String dimension;
	private ResourceLocation skin;
	private ResourceLocation cape;

	public AutoRefereePlayer(String username, AutoRefereeTeam team) {
		this(username);
		this.team = team;
	}

	public AutoRefereePlayer(String username) {
		this.username = username;
		this.team = null;
		this.health = 20;
		this.armor = 0;
		this.kills = 0;
		this.deaths = 0;
		this.objectives = new ArrayList<AutoRefereeObjective>();
		this.killstreak = 0;
		this.accuracy = 0;
		this.domination = new ArrayList<String>();
		this.loggedIn = true;
		this.dimension = "overworld";
		//find skin resource (and bind if not yet loaded)
		this.skin = AbstractClientPlayer.func_110311_f(username);
		AbstractClientPlayer.func_110304_a(this.skin, this.username);
		this.cape = null;
	}

	public int getHealth() {
		return this.health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getArmor() {
		return this.armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public AutoRefereeTeam getTeam() {
		return this.team;
	}

	public void setTeam(AutoRefereeTeam team) {
		this.team = team;
	}

	public void addObjective(AutoRefereeObjective obj) {
		this.objectives.add(obj);
	}

	public void removeObjective(AutoRefereeObjective obj) {
		this.objectives.remove(obj);
	}

	public ArrayList<AutoRefereeObjective> getObjectives() {
		return this.objectives;
	}

	public String getColoredName() {
		if (this.team != null)
			return team.getColorString() + this.username;
		return this.username;
	}

	public String getName() {
		return this.username;
	}

	public int getKills() {
		return this.kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return this.deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public void setAccuracy(int acc) {
		this.accuracy = acc;
	}

	public int getAccuracy() {
		return this.accuracy;
	}

	public String getAccuracyString() {
		if (this.accuracy < 10)
			return "0" + this.accuracy + "%";
		return this.accuracy + "%";
	}

	public void setKillStreak(int streak) {
		this.killstreak = streak;
	}

	public int getKillStreak() {
		return this.killstreak;
	}

	public void addDomination(String name) {
		if (!this.domination.contains(name))
			this.domination.add(name);
	}

	public void removeDomination(String name) {
		if (this.domination.contains(name))
			this.domination.remove(name);
	}

	public ArrayList<String> getDomination() {
		return this.domination;
	}

	public int getDominationAmount() {
		return this.domination.size();
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean getLoggedIn() {
		return this.loggedIn;
	}

	public int getItemAmount(int id, int damage) {
		if (itemAmounts.containsKey(id + "-" + damage))
			return itemAmounts.get(id + "-" + damage);
		return 0;
	}

	public void setItemAmount(int id, int damage, int amount) {
		itemAmounts.put(id + "-" + damage, amount);
	}
	
	public void setDimension(String dimension){
		this.dimension = dimension.toLowerCase();
	}
	
	public String getDimension(){
		return this.dimension;
	}
	
	public ResourceLocation getSkin(){
		return this.skin;
	}
	
	public ResourceLocation getCape(){
		return this.cape;
	}
	
	public void setCapeUrl(String url){
		if(url == ""){
			cape = null;
			return;
		}
		TextureManager tm = Minecraft.getMinecraft().func_110434_K();
        ThreadDownloadImageData image = new ThreadDownloadImageData(url, (ResourceLocation)null, (IImageBuffer)null);
        this.cape = AbstractClientPlayer.func_110299_g(this.username);
        tm.func_110579_a(cape, (TextureObject)image);
        Logger.getLogger("minecraft").info(url + " - " + (image == null));
	}
}
// END CODE