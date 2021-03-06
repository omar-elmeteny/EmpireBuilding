package buildings;

import exceptions.BuildingInCoolDownException;
import exceptions.MaxLevelException;

public abstract class Building {

	private int cost;
	private int level;
	private int upgradeCost;
	private boolean coolDown;

	public Building(int cost, int upgradeCost) {
		this.cost = cost;
		this.upgradeCost = upgradeCost;
		this.level = 1;
		coolDown = true;
	}

	public void upgrade() throws BuildingInCoolDownException, MaxLevelException {
		if (coolDown)
			throw new BuildingInCoolDownException(this.getName() + " is cooling down, please wait till next turn.");
		if (level == 3)
			throw new MaxLevelException(this.getName() + " is already at maximum level and cannot be ugpraded!");
		coolDown = true;
	}

	public int getCost() {
		return cost;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getUpgradeCost() {
		return upgradeCost;
	}

	public void setUpgradeCost(int upgradeCost) {
		this.upgradeCost = upgradeCost;
	}

	public boolean isCoolDown() {
		return coolDown;
	}

	public void setCoolDown(boolean inCooldown) {
		this.coolDown = inCooldown;
	}

	public String getName() {
		return this.getTypeName();
	}

	public String getTypeName() {
		return this.getClass().getSimpleName();
	}
}
