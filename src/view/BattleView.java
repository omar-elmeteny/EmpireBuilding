package view;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
        battleView.unitClicked(unit, (JButton) e.getComponent());
    }
}

public class BattleView extends LimitedHeightPanel implements GameInformationView {
    private final GameView gameView;
    private final Army attackingArmy;
    private final Army defendingArmy;

    private JPanel armiesContainer;
    private JLabel attackingArmyLabel;
    private JPanel attackingArmyPanel;
    private JLabel defendingArmyLabel;
    private JPanel defendingArmyPanel;
    private JPanel bottomPanel;
    private JTextArea battleLog;
    private MaxWidthButton closeBattleViewButton;
    private JScrollPane battleLogScroller;
    private Unit attackingUnit;

    public BattleView(GameView gameView, Army attackingArmy, Army defendingArmy) {
        super();
        this.gameView = gameView;
        this.attackingArmy = attackingArmy;
        this.defendingArmy = defendingArmy;

        this.setOpaque(false);
        this.setLayout(new BorderLayout());

        GridBagLayout gridbag = new GridBagLayout();

        this.armiesContainer = new JPanel();
        this.armiesContainer.setOpaque(false);

        this.armiesContainer.setLayout(gridbag);
        this.add(armiesContainer, BorderLayout.CENTER);

        attackingArmyLabel = new JLabel();
        attackingArmyLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        attackingArmyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        attackingArmyLabel.setText("Player's army");
        attackingArmyLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.weighty = 1;
        armiesContainer.add(attackingArmyLabel, c);

        this.attackingArmyPanel = new JPanel();
        this.attackingArmyPanel.setOpaque(false);
        this.attackingArmyPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.attackingArmyPanel.setLayout(new GridLayout(6, 6, 3, 3));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.weighty = 10;
        armiesContainer.add(attackingArmyPanel, c);

        defendingArmyLabel = new JLabel();
        defendingArmyLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        defendingArmyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        defendingArmyLabel.setText(this.defendingArmy.getCurrentLocation() + "'s army");
        defendingArmyLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.weighty = 1;
        armiesContainer.add(defendingArmyLabel, c);

        this.defendingArmyPanel = new JPanel();
        this.defendingArmyPanel.setOpaque(false);
        this.defendingArmyPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.defendingArmyPanel.setLayout(new GridLayout(6, 6, 3, 3));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.weighty = 10;
        armiesContainer.add(defendingArmyPanel, c);

        this.bottomPanel = new JPanel();
        this.bottomPanel.setLayout(new BoxLayout(this.bottomPanel, BoxLayout.Y_AXIS));
        this.bottomPanel.setOpaque(false);
        this.bottomPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 150));
        this.add(bottomPanel, BorderLayout.SOUTH);

        this.battleLog = new JTextArea();
        this.battleLog.setEditable(false);

        this.battleLogScroller = new JScrollPane(this.battleLog);
        this.battleLogScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.battleLogScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.battleLogScroller.setOpaque(false);
        this.battleLogScroller.getViewport().setOpaque(false);
        this.bottomPanel.add(this.battleLogScroller);

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
            if (unit.getParentArmy() == defendingArmy) {
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

        for (Unit unit : army.getUnits()) {
            MaxWidthButton button = new MaxWidthButton();
            String text = unit.getCurrentSoldierCount() + " level " + unit.getLevel() + " "
                    + unit.getClass().getSimpleName();
            if (unit.getCurrentSoldierCount() > 1) {
                if (text.endsWith("y")) {
                    text = text.substring(0, text.length() - 1) + "ies";
                } else {
                    text += "s";
                }
            }

            try {
                BufferedImage image = ImageIO.read(new File(unit.getClass().getSimpleName().toLowerCase() + ".png"));
                Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImage);
                button.setIcon(icon);
                button.setVerticalTextPosition(SwingConstants.BOTTOM);
                button.setHorizontalTextPosition(SwingConstants.CENTER);
            } catch (IOException e) {
                e.printStackTrace();
            }

            button.setText(text);
            button.setBackground(panel == attackingArmyPanel ? new Color(40, 167, 69) : new Color(220, 53, 69));
            button.setForeground(Color.WHITE);
            button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
            button.addMouseListener(new UnitButtonListener(this, unit));
            panel.add(button);
        }
    }

    @Override
    public void updateGameInformation() {
        this.fillArmyPanel(attackingArmyPanel, attackingArmy);
        this.fillArmyPanel(defendingArmyPanel, defendingArmy);
        armiesContainer.validate();
        armiesContainer.repaint();
        this.closeBattleViewButton
                .setVisible(attackingArmy.getUnits().size() == 0 || defendingArmy.getUnits().size() == 0);
    }

}
