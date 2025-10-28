package com.civfanatics.civ3.xplatformeditor;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
class PCXRunner {

    static void main(String[]args)
    {
        OldPCXFilter pcx = new OldPCXFilter("C:\\Program Files\\Infogrames Interactive\\Civilization III\\Art\\units\\Palettes\\ntp00.pcx");
        pcx.readFile();
        pcx.parse();
        System.out.println(pcx);
    }
}
