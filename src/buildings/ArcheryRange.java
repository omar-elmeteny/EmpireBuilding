package buildings;

import exceptions.BuildingInCoolDownException;
import exceptions.MaxLevelException;
import exceptions.MaxRecruitedException;
import units.Archer;
import units.Unit;

public class ArcheryRange extends MilitaryBuilding {

	public ArcheryRange() {
		super(1500, 800, 400);

	}

	@Override
	public Unit recruit() throws BuildingInCoolDownException, MaxRecruitedException {
		checkCanRecruit();
		setCurrentRecruit(getCurrentRecruit() + 1);
		if (getLevel() == 1)
			return new Archer(1, 60, 0.4, 0.5, 0.6);

		else if (getLevel() == 2)
			return new Archer(2, 60, 0.4, 0.5, 0.6);
		else
			return new Archer(3, 70, 0.5, 0.6, 0.7);
	}

	@Override
	public void upgrade() throws BuildingInCoolDownException, MaxLevelException {
		super.upgrade();
		if (getLevel() == 1) {
			setLevel(2);
			setUpgradeCost(700);
			setRecruitmentCost(450);
		} else if (getLevel() == 2) {
			setLevel(3);
			setRecruitmentCost(500);
		}

	}

	@Override
	public Class<?> getUnitType() {
		return Archer.class;
	}

	@Override
	public String getName() {
		return "Archery Range";
	}
}
