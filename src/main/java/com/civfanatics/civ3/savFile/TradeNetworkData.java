
package com.civfanatics.civ3.savFile;

import org.apache.log4j.Logger;

/**
 * Trade Network Data.  Sub-division of LEAD.
 * @author Andrew
 */
public class TradeNetworkData {
    
    Logger logger = Logger.getLogger(TradeNetworkData.class);
    
    //For each GOOD
        //For each LEAD
            //three bytes
    private TradeNetworkDetails[][] resourceInfo;
    
    public void initialize(int numGoods, int numPlayers) {
        resourceInfo = new TradeNetworkDetails[numGoods][];
        for (int i = 0; i < numGoods; i++) {
            resourceInfo[i] = new TradeNetworkDetails[numPlayers];
            for (int j = 0; j < numPlayers; j++) {
                resourceInfo[i][j] = new TradeNetworkDetails();
            }
        }
    }
    
    public void setInfo(int goodNumber, int playerNumber, byte hasResource, byte importExport, byte tradeable) {
        logger.trace("Good number: " + goodNumber + "; player number: " + playerNumber);
        resourceInfo[goodNumber][playerNumber].setHasResource(hasResource);
        resourceInfo[goodNumber][playerNumber].setImportExport(importExport);
        resourceInfo[goodNumber][playerNumber].setHasResource(hasResource);
    }
    
    public TradeNetworkDetails getResourceInfo(int goodNumber, int playerNumber) {
        return resourceInfo[goodNumber][playerNumber];
    }
}
