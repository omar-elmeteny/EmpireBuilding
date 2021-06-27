package units;

import java.util.ArrayList;

import exceptions.MaxCapacityException;
import exceptions.RelocateNotAllowedException;

public class Army {
	private Status currentStatus;
	private ArrayList<Unit> units;
	private int distancetoTarget;
	private String target;
	private String currentLocation;
	private final int maxToHold = 10;
	private String startingCity;
	private boolean enemy;

	public Army(String currentLocation) {
		this.currentLocation = currentLocation;
		currentStatus = Status.IDLE;
		units = new ArrayList<Unit>();
		distancetoTarget = -1;
		target = "";

	}

	public boolean isEnemy() {
		return enemy;
	}

	public void setEnemy(boolean enemy) {
		this.enemy = enemy;
	}

	public void relocateUnit(Unit unit) throws MaxCapacityException, RelocateNotAllowedException {
		if (unit.getParentArmy() == this) {
			return;
		}
		if (units.size() == maxToHold)
			throw new MaxCapacityException("Cannot relocate unit because maximum capacity for target army reached.");
		if (unit.getParentArmy().getCurrentStatus() != Status.IDLE || getCurrentStatus() != Status.IDLE
				|| !unit.getParentArmy().getCurrentLocation().equals(getCurrentLocation())) {
			throw new RelocateNotAllowedException(
					"To relocate a unit from one army to another, both armies must be idle and at the same location.");
		}
		units.add(unit);
		unit.getParentArmy().units.remove(unit);
		unit.setParentArmy(this);
	}

	public Status getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Status currentStatus) {
		this.currentStatus = currentStatus;
	}

	public ArrayList<Unit> getUnits() {
		return units;
	}

	public void setUnits(ArrayList<Unit> units) {
		this.units = units;
	}

	public int getDistancetoTarget() {
		return distancetoTarget;
	}

	public void setDistancetoTarget(int distancetoTarget) {
		this.distancetoTarget = distancetoTarget;
	}

	public String getStartingCity() {
		return startingCity;
	}

	public void setStartingCity(String startingCity) {
		this.startingCity = startingCity;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public int getMaxToHold() {
		return maxToHold;
	}

	public double foodNeeded() {
		double sum = 0;
		for (Unit u : units) {
			if (currentStatus == Status.IDLE)
				sum += (u.getIdleUpkeep() * u.getCurrentSoldierCount());
			else if (currentStatus == Status.MARCHING)
				sum += (u.getMarchingUpkeep() * u.getCurrentSoldierCount());
			else
				sum += (u.getSiegeUpkeep() * u.getCurrentSoldierCount());

		}
		return sum;

	}

	public void handleAttackedUnit(Unit u) {
		if (u.getCurrentSoldierCount() <= 0) {
			u.setCurrentSoldierCount(0);
			units.remove(u);
		}
	}

}
