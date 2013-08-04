//START CODE
package net.minecraft.src;

public class AutoRefereeObjective {

	private int id;
	private int dataValue;
	private AutoRefereeObjectiveStatus status;
	private AutoRefereeTeam team;

	public AutoRefereeObjective(int id, AutoRefereeTeam team) {
		this(id, 0, team);
	}

	public AutoRefereeObjective(int id, int dataValue, AutoRefereeTeam team) {
		this(id, dataValue, AutoRefereeObjectiveStatus.FLEECY_BOX);
		this.team = team;
	}

	public AutoRefereeObjective(int id, int dataValue, AutoRefereeObjectiveStatus status) {
		this.id = id;
		this.dataValue = dataValue;
		this.status = status;
	}

	public int getId() {
		return this.id;
	}

	public int getData() {
		return this.dataValue;
	}

	public void setStatus(AutoRefereeObjectiveStatus status) {
		this.status = status;
	}

	public AutoRefereeObjectiveStatus getStatus() {
		return this.status;
	}
	
	public AutoRefereeTeam getTeam(){
		return this.team;
	}

	@Override
	public String toString(){
		if(dataValue == 0)
			return "" + id;
		return id + "," + dataValue;
	}
}
// END CODE