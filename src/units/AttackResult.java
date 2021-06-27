package units;

public class AttackResult {
    private Unit attacker;
    private Unit defender;
    private boolean attackerIsPlayer;
    private int soldiersKilled;

    public AttackResult(Unit attacker, Unit defender, int soldiersKilled, boolean attackerIsPlayer) {
        super();
        this.setSoldiersKilled(soldiersKilled);
        this.setAttacker(attacker);
        this.setDefender(defender);
        this.setAttackerIsPlayer(attackerIsPlayer);
    }

    public int getSoldiersKilled() {
        return soldiersKilled;
    }

    public void setSoldiersKilled(int soldiersKilled) {
        this.soldiersKilled = soldiersKilled;
    }

    public boolean isAttackerIsPlayer() {
        return attackerIsPlayer;
    }

    public void setAttackerIsPlayer(boolean attackerIsPlayer) {
        this.attackerIsPlayer = attackerIsPlayer;
    }

    public Unit getDefender() {
        return defender;
    }

    public void setDefender(Unit defender) {
        this.defender = defender;
    }

    public Unit getAttacker() {
        return attacker;
    }

    public void setAttacker(Unit attacker) {
        this.attacker = attacker;
    }

    public String getAttackerName() {
        return attackerIsPlayer ? "Player" : attacker.getParentArmy().getCurrentLocation();
    }

    public String getDefenderName() {
        return attackerIsPlayer ? defender.getParentArmy().getCurrentLocation() : "player";
    }

    @Override
    public String toString() {
        return getAttackerName() + "'s " + attacker.getClass().getSimpleName() + " attacked " + getDefenderName()
                + "'s " + defender.getClass().getSimpleName() + " and killed " + soldiersKilled
                + (soldiersKilled == 1 ? " soldier." : " soldiers.");
    }
}
