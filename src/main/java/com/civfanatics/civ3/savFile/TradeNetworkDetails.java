
package com.civfanatics.civ3.savFile;

/**
 *
 * @author Andrew
 */
public class TradeNetworkDetails {
    private byte hasResource;
    private byte importExport;
    private byte tradeable;

    public byte getHasResource() {
        return hasResource;
    }

    public void setHasResource(byte hasResource) {
        this.hasResource = hasResource;
    }

    public byte getImportExport() {
        return importExport;
    }

    public void setImportExport(byte importExport) {
        this.importExport = importExport;
    }

    public byte getTradeable() {
        return tradeable;
    }

    public void setTradeable(byte tradeable) {
        this.tradeable = tradeable;
    }
}
