
package com.civfanatics.civ3.biqFile;

public enum Section {
    BLDG,
    CTZN,
    CULT,
    DIFF,
    ERAS,
    ESPN,
    EXPR,
    GOOD,
    GOVT,
    RULE,
    PRTO,
    RACE,
    TECH,
    TFRM,
    TERR,
    WSIZ,
    FLAV,
    WCHR,
    WMAP,
    TILE,
    CONT,
    SLOC,
    CITY,
    UNIT,
    CLNY,
    GAME,
    LEAD;

    @Override
    public String toString()
    {
        String lowercase = this.name().toLowerCase();
        return lowercase.substring(0, 1).toUpperCase() + lowercase.substring(1);
    }
}
