
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

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
    
    public List<Integer> getHistographScoresForPlayer(int playerID, BiFunction<HistTurn, Integer, Integer> scoreComponentFunction) {
        
        List<Integer> power = new ArrayList<Integer>(turns.size());
        for (HistTurn turn : turns) {
            int playerIdInTurn = 0;
            boolean foundPlayer = false;
            for (playerIdInTurn = 0; playerIdInTurn < turn.getCivIds().size(); playerIdInTurn++) {
                if (turn.getCivIds().get(playerIdInTurn) == playerID) {
                    foundPlayer = true;
                    break;
                }
            }
            if (foundPlayer) {
                power.add(scoreComponentFunction.apply(turn, playerIdInTurn));
            }
            else {
                power.add(0);
            }
        }
        return power;
    }
    
    public int getNumTurns() {
        return turns.size();
    }
    
}
