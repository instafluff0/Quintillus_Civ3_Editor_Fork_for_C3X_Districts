
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Histograph.
 * @author Quintillus
 */
public class HIST {
    private String header;
    private int turnCount;
    private int dataLengthOrWhichLeadsArePresent;
    private List<HistTurn> turns;
    
    byte[] inputFour = new byte[4];
    
    public void readDataSection(LittleEndianDataInputStream in) throws IOException {
        in.read(inputFour, 0, 4);
        header = new String(inputFour, "Windows-1252");
        
        this.turnCount = in.readInt();
        this.dataLengthOrWhichLeadsArePresent = in.readInt();
        
        turns = new ArrayList<HistTurn>(turnCount);
        
        for (int i = 0; i < turnCount; i++) {
            HistTurn newTurn = new HistTurn();
            newTurn.setTurnNumber(in.readInt());
            newTurn.setDate(in.readInt());
            int remainingCivCount = in.readInt();
            newTurn.setRemainingCivs(remainingCivCount);
            
            List<Integer> civIDs = new ArrayList<Integer>();
            for (int c = 0; c < remainingCivCount; c++) {
                civIDs.add(in.readInt());
            }
            newTurn.setCivIds(civIDs);
            
            List<Integer> power = new ArrayList<Integer>();
            for (int c = 0; c < remainingCivCount; c++) {
                power.add(in.readInt());
            }
            newTurn.setPower(power);
            
            List<Integer> score = new ArrayList<Integer>();
            for (int c = 0; c < remainingCivCount; c++) {
                score.add(in.readInt());
            }
            newTurn.setScore(score);
            
            List<Integer> culture = new ArrayList<Integer>();
            for (int c = 0; c < remainingCivCount; c++) {
                culture.add(in.readInt());
            }
            newTurn.setCulture(culture);
            
            turns.add(newTurn);
        }
    }
    
    public List<List<Integer>> getScores() {
        return null;
    }
    
    public List<Integer> getPowerForPlayer(int playerID) {
        List<Integer> power = new ArrayList<Integer>(turns.size());
        for (HistTurn turn : turns) {
            int i = 0;
            boolean foundPlayer = false;
            for (i = 0; i < turn.getCivIds().size(); i++) {
                if (turn.getCivIds().get(i) == playerID) {
                    foundPlayer = true;
                    break;
                }
            }
            if (foundPlayer) {
                power.add(turn.getPower().get(i));
            }
            else {
                power.add(0);
            }
        }
        return power;
    }
    
}
