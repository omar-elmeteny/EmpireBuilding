package exceptions;

import engine.City;

public class PlayerMustAttackCityException extends EmpireException {
    private final City city;

    public PlayerMustAttackCityException(City city) {
        this.city = city;
		
	}

	public PlayerMustAttackCityException(City city, String s) {
		super(s);
        this.city = city;
	}

    public City getCity() {
        return city;
    }
}
