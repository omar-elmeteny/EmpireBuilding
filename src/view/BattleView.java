package view;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import units.Army;
import units.AttackResult;
import units.Unit;

class CloseBattleViewButtonListener extends MouseInputAdapter {
    private GameView gameView;

    public CloseBattleViewButtonListener(GameView gameView) {
        this.gameView = gameView;

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gameView.closeBattleView();
    }
}

class UnitButtonListener extends MouseInputAdapter {
    private BattleView battleView;
    private Unit unit;

    public UnitButtonListener(BattleView battleView, Unit unit) {
        this.battleView = battleView;
        this.unit = unit;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        battleView.unitClicked(unit, (JButton)e.getComponent());
    }
}

public class BattleView extends LimitedHeightPanel implements GameInformationView {
    private final GameView gameView;
    private final Army attackingArmy;
    private final Army defendingArmy;


    private LimitedHeightPanel armiesContainer;
    private LimitedHeightPanel attackingArmyPanel;
    private LimitedHeightPanel defendingArmyPanel;
    private LimitedHeightPanel bottomPanel;
    private JTextArea battleLog;
    private MaxWidthButton closeBattleViewButton;
    private JScrollPane battleLogContainer;
    private Unit attackingUnit;

    public BattleView(GameView gameView, Army attackingArmy, Army defendingArmy) {
        super();
        this.gameView = gameView;
        this.attackingArmy = attackingArmy;
        this.defendingArmy = defendingArmy;

        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        
        this.armiesContainer = new LimitedHeightPanel();
        this.armiesContainer.setOpaque(false);
        this.armiesContainer.setLayout(new BorderLayout());
        this.add(armiesContainer, BorderLayout.CENTER);

        this.attackingArmyPanel = new LimitedHeightPanel();
        this.attackingArmyPanel.setOpaque(false);
        this.attackingArmyPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.attackingArmyPanel.setLayout(new BoxLayout(attackingArmyPanel, BoxLayout.Y_AXIS));
        armiesContainer.add(attackingArmyPanel, BorderLayout.WEST);

        this.defendingArmyPanel = new LimitedHeightPanel();
        this.defendingArmyPanel.setOpaque(false);
        this.defendingArmyPanel.setLayout(new BoxLayout(defendingArmyPanel, BoxLayout.Y_AXIS));
        armiesContainer.add(defendingArmyPanel, BorderLayout.EAST);

        this.bottomPanel = new LimitedHeightPanel();
        this.bottomPanel.setLayout(new BoxLayout(this.bottomPanel, BoxLayout.Y_AXIS));
        this.bottomPanel.setOpaque(false);
        this.add(bottomPanel, BorderLayout.SOUTH);

        this.battleLog = new JTextArea();
        this.battleLog.setEditable(false);
        
        this.battleLogContainer = new JScrollPane(this.battleLog);
        this.battleLogContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        this.battleLogContainer.setMinimumSize(new Dimension(1, 300));
        this.battleLogContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.battleLogContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.battleLogContainer.setOpaque(false);
        this.battleLogContainer.getViewport().setOpaque(false);
        this.bottomPanel.add(this.battleLogContainer);
        
        this.closeBattleViewButton = new MaxWidthButton();
        this.closeBattleViewButton.setBackground(new Color(86, 94, 100));
        this.closeBattleViewButton.setForeground(Color.WHITE);
        this.closeBattleViewButton.setVisible(false);
        this.closeBattleViewButton.setFont(new Font(Font.SERIF, Font.BOLD, 18));
        this.closeBattleViewButton.addMouseListener(new CloseBattleViewButtonListener(gameView));
        this.closeBattleViewButton.setText("Close Battle View");
        this.bottomPanel.add(this.closeBattleViewButton);

        updateGameInformation();
    }

    public Army getAttackingArmy() {
        return attackingArmy;
    }

    public Army getDefenArmy() {
        return defendingArmy;
    }

    public void unitClicked(Unit unit, JButton component) {
        if (attackingUnit == null) {
            if(unit.getParentArmy() == defendingArmy) {
                return;
            }
            attackingUnit = unit;
        } else {
            ArrayList<AttackResult> results = gameView.attackUnit(attackingUnit, unit);
            attackingUnit = null;
            for (AttackResult result : results) {
                this.battleLog.setText(this.battleLog.getText() + result.toString() + "\n");
            }
        }
    }


    private void fillArmyPanel(JPanel panel, Army army) {
        panel.removeAll();
        
        MaxWidthLabel label = new MaxWidthLabel();
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        label.setHorizontalAlignment(army.isEnemy() ? SwingConstants.RIGHT : SwingConstants.LEFT);
        label.setText(army.isEnemy() ? army.getCurrentLocation() + "'s Army" : "Player's army");
        label.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        panel.add(label);

        for (Unit unit : army.getUnits()) {
            MaxWidthButton button = new MaxWidthButton();
            String text = unit.getCurrentSoldierCount() + " level " + unit.getLevel() + " " + unit.getClass().getSimpleName();
            if (unit.getCurrentSoldierCount() > 1) {
                if (text.endsWith("y")) {
                    text = text.substring(0, text.length() - 1) + "ies";
                } else {
                    text += "s";
                }
            }
            button.setText(text);
            button.setBackground(panel == attackingArmyPanel ? 
                new Color(40, 167, 69) : new Color(220, 53, 69));
            button.setForeground(Color.WHITE);
            button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            button.addMouseListener(new UnitButtonListener(this, unit));
            panel.add(button);
        }
    }

    @Override
    public void updateGameInformation() {
        this.fillArmyPanel(attackingArmyPanel, attackingArmy);
        this.fillArmyPanel(defendingArmyPanel, defendingArmy);
        this.closeBattleViewButton.setVisible(attackingArmy.getUnits().size() == 0 || defendingArmy.getUnits().size() == 0); 
    }

}
