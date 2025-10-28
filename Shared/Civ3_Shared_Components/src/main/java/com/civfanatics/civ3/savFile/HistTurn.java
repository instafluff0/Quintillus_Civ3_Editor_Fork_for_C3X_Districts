
package com.civfanatics.civ3.savFile;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class HistTurn {
    int turnNumber;
    int date;
    int remainingCivs;
    
    List<Integer> civIds = new ArrayList<Integer>();
    List<Integer> power = new ArrayList<Integer>();
    List<Integer> score = new ArrayList<Integer>();
    List<Integer> culture = new ArrayList<Integer>();

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getRemainingCivs() {
        return remainingCivs;
    }

    public void setRemainingCivs(int remainingCivs) {
        this.remainingCivs = remainingCivs;
    }

    public List<Integer> getCivIds() {
        return civIds;
    }

    public void setCivIds(List<Integer> civIds) {
        this.civIds = civIds;
    }

    public Integer getPower(int player) {
        return power.get(player);
    }

    public void setPower(List<Integer> power) {
        this.power = power;
    }

    public Integer getScore(int player) {
        return score.get(player);
    }

    public void setScore(List<Integer> score) {
        this.score = score;
    }

    public Integer getCulture(int player) {
        return culture.get(player);
    }

    public void setCulture(List<Integer> culture) {
        this.culture = culture;
    }
}
