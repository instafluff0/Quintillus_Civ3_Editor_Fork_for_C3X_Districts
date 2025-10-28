package com.civfanatics.civ3.xplatformeditor.tabs.biqc;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
import javax.swing.JCheckBox;

/**
 * This class is used to store a user's preferences for which check boxes are
 * selected on the BIQC tab.
 * 
 * @author Andrew
 */
public class checkBoxSettings
{
    boolean buildings;
    boolean citizens;
    boolean civilizations;
    boolean culture;
    boolean difficulties;
    boolean eras;
    boolean espionage;
    boolean experience;
    boolean flavors;
    boolean generalSettings;
    boolean governments;
    boolean resources;
    boolean technologies;
    boolean terrains;
    boolean units;
    boolean workerJobs;
    boolean worldSizes;
    boolean cities;
    boolean colonies;
    boolean continents;
    boolean startingLocations;
    boolean tiles;
    boolean mapUnits;
    boolean worldCharacteristics;
    boolean worldMap;
    boolean additionalProperties;
    boolean playerData;
    int numSelected;

    /**
     * The constructor.  Defaults all check boxes to not selected.
     */
    public checkBoxSettings()
    {
        buildings = false;
        citizens = false;
        civilizations = false;
        culture = false;
        difficulties = false;
        eras = false;
        espionage = false;
        experience = false;
        flavors = false;
        generalSettings = false;
        governments = false;
        resources = false;
        technologies = false;
        terrains = false;
        units = false;
        workerJobs = false;
        worldSizes = false;
        cities = false;
        colonies = false;
        continents = false;
        startingLocations = false;
        tiles = false;
        mapUnits = false;
        worldCharacteristics = false;
        worldMap = false;
        additionalProperties = false;
        playerData = false;
        numSelected = 0;
    }

    public int getNumSelected()
    {
        return numSelected;
    }

    /**
     * @param numSelected - The number of check boxes selected.
     */
    public void storeNumSelected(int numSelected)
    {
        this.numSelected = numSelected;
    }

    /**
     * Sets the buildings check box based on the stored value.
     *
     * @param box - The JCheckBox that represents buildings.
     */
    public void setBuildings(JCheckBox box)
    {
        if (buildings)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the citizens check box based on the stored value.
     *
     * @param box - The JCheckBox that represents citizens.
     */
    public void setCitizens(JCheckBox box)
    {
        if (citizens)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the civilizations check box based on the stored value.
     *
     * @param box - The JCheckBox that represents civilizations.
     */
    public void setCivilizations(JCheckBox box)
    {
        if (civilizations)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the culture check box based on the stored value.
     *
     * @param box - The JCheckBox that represents cultures.
     */
    public void setCulture(JCheckBox box)
    {
        if (culture)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the difficulties check box based on the stored value.
     *
     * @param box - The JCheckBox that represents difficulties.
     */
    public void setDifficulties(JCheckBox box)
    {
        if (difficulties)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the eras check box based on the stored value.
     *
     * @param box - The JCheckBox that represents eras.
     */
    public void setEras(JCheckBox box)
    {
        if (eras)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the espionage check box based on the stored value.
     *
     * @param box - The JCheckBox that represents espionage missions.
     */
    public void setEspionage(JCheckBox box)
    {
        if (espionage)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the experience check box based on the stored value.
     *
     * @param box - The JCheckBox that represents experience levels.
     */
    public void setExperience(JCheckBox box)
    {
        if (experience)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the flavors check box based on the stored value.
     *
     * @param box - The JCheckBox that represents flavors.
     */
    public void setFlavors(JCheckBox box)
    {
        if (flavors)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the general settings check box based on the stored value.
     *
     * @param box - The JCheckBox that represents general settings.
     */
    public void setGeneralSettings(JCheckBox box)
    {
        if (generalSettings)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the governments check box based on the stored value.
     *
     * @param box - The JCheckBox that represents governments.
     */
    public void setGovernments(JCheckBox box)
    {
        if (governments)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the resources check box based on the stored value.
     *
     * @param box - The JCheckBox that represents resources.
     */
    public void setResources(JCheckBox box)
    {
        if (resources)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the technologies check box based on the stored value.
     *
     * @param box - The JCheckBox that represents technologies.
     */
    public void setTechnologies(JCheckBox box)
    {
        if (technologies)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the terrains check box based on the stored value.
     *
     * @param box - The JCheckBox that represents terrains.
     */
    public void setTerrains(JCheckBox box)
    {
        if (terrains)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the units check box based on the stored value.
     *
     * @param box - The JCheckBox that represents units.
     */
    public void setUnits(JCheckBox box)
    {
        if (units)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the worker jobs check box based on the stored value.
     *
     * @param box - The JCheckBox that represents worker jobs.
     */
    public void setWorkerJobs(JCheckBox box)
    {
        if (workerJobs)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the world sizes check box based on the stored value.
     *
     * @param box - The JCheckBox that represents world sizes.
     */
    public void setWorldSizes(JCheckBox box)
    {
        if (worldSizes)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the cities check box based on the stored value.
     *
     * @param box - The JCheckBox that represents cities.
     */
    public void setCities(JCheckBox box)
    {
        if (cities)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the colonies check box based on the stored value.
     *
     * @param box - The JCheckBox that represents colonies.
     */
    public void setColonies(JCheckBox box)
    {
        if (colonies)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the continents check box based on the stored value.
     *
     * @param box - The JCheckBox that represents continents.
     */
    public void setContinents(JCheckBox box)
    {
        if (continents)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the starting locations check box based on the stored value.
     *
     * @param box - The JCheckBox that represents starting locations.
     */
    public void setStartingLocations(JCheckBox box)
    {
        if (startingLocations)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the tiles check box based on the stored value.
     *
     * @param box - The JCheckBox that represents tiles.
     */
    public void setTiles(JCheckBox box)
    {
        if (tiles)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the map units check box based on the stored value.
     *
     * @param box - The JCheckBox that represents units on the map.
     */
    public void setMapUnits(JCheckBox box)
    {
        if (mapUnits)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the world characteristics check box based on the stored value.
     *
     * @param box - The JCheckBox that represents world characteristics.
     */
    public void setWorldCharacteristics(JCheckBox box)
    {
        if (worldCharacteristics)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the world map check box based on the stored value.
     *
     * @param box - The JCheckBox that represents the world map.
     */
    public void setWorldMap(JCheckBox box)
    {
        if (worldMap)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the additional properties check box based on the stored value.
     *
     * @param box - The JCheckBox that represents additional properties.
     */
    public void setAdditionalProperties(JCheckBox box)
    {
        if (additionalProperties)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the player data check box based on the stored value.
     *
     * @param box - The JCheckBox that represents player data.
     */
    public void setPlayerData(JCheckBox box)
    {
        if (playerData)
        {
            box.setSelected(true);
        }
        else
        {
            box.setSelected(false);
        }
    }

    /**
     * Sets the internal buildings toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents buildings.
     */
    public void storeBuildings(JCheckBox box)
    {
        if (box.isSelected())
        {
            buildings = true;
        }
        else
        {
            buildings = false;
        }
    }

    /**
     * Sets the internal citizens toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents citizens.
     */
    public void storeCitizens(JCheckBox box)
    {
        if (box.isSelected())
        {
            citizens = true;
        }
        else
        {
            citizens = false;
        }
    }

    /**
     * Sets the internal civilizations toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents civilizations.
     */
    public void storeCivilizations(JCheckBox box)
    {
        if (box.isSelected())
        {
            civilizations = true;
        }
        else
        {
            civilizations = false;
        }
    }

    /**
     * Sets the internal culture toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents cultures.
     */
    public void storeCulture(JCheckBox box)
    {
        if (box.isSelected())
        {
            culture = true;
        }
        else
        {
            culture = false;
        }
    }

    /**
     * Sets the internal difficulties toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents difficulties.
     */
    public void storeDifficulties(JCheckBox box)
    {
        if (box.isSelected())
        {
            difficulties = true;
        }
        else
        {
            difficulties = false;
        }
    }

    /**
     * Sets the internal eras toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents eras.
     */
    public void storeEras(JCheckBox box)
    {
        if (box.isSelected())
        {
            eras = true;
        }
        else
        {
            eras = false;
        }
    }

    /**
     * Sets the internal espionage toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents espionage missions.
     */
    public void storeEspionage(JCheckBox box)
    {
        if (box.isSelected())
        {
            espionage = true;
        }
        else
        {
            espionage = false;
        }
    }

    /**
     * Sets the internal experience toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents experience levels.
     */
    public void storeExperience(JCheckBox box)
    {
        if (box.isSelected())
        {
            experience = true;
        }
        else
        {
            experience = false;
        }
    }

    /**
     * Sets the internal flavors toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents flavors.
     */
    public void storeFlavors(JCheckBox box)
    {
        if (box.isSelected())
        {
            flavors = true;
        }
        else
        {
            flavors = false;
        }
    }

    /**
     * Sets the internal general settings toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents general settings.
     */
    public void storeGeneralSettings(JCheckBox box)
    {
        if (box.isSelected())
        {
            generalSettings = true;
        }
        else
        {
            generalSettings = false;
        }
    }

    /**
     * Sets the internal governments toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents governments.
     */
    public void storeGovernments(JCheckBox box)
    {
        if (box.isSelected())
        {
            governments = true;
        }
        else
        {
            governments = false;
        }
    }

    /**
     * Sets the internal resources toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents resources.
     */
    public void storeResources(JCheckBox box)
    {
        if (box.isSelected())
        {
            resources = true;
        }
        else
        {
            resources = false;
        }
    }

    /**
     * Sets the internal technologies toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents technologies.
     */
    public void storeTechnologies(JCheckBox box)
    {
        if (box.isSelected())
        {
            technologies = true;
        }
        else
        {
            technologies = false;
        }
    }

    /**
     * Sets the internal terrains toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents terrains.
     */
    public void storeTerrains(JCheckBox box)
    {
        if (box.isSelected())
        {
            terrains = true;
        }
        else
        {
            terrains = false;
        }
    }

    /**
     * Sets the internal units toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents units.
     */
    public void storeUnits(JCheckBox box)
    {
        if (box.isSelected())
        {
            units = true;
        }
        else
        {
            units = false;
        }
    }

    /**
     * Sets the internal worker jobs toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents worker jobs.
     */
    public void storeWorkerJobs(JCheckBox box)
    {
        if (box.isSelected())
        {
            workerJobs = true;
        }
        else
        {
            workerJobs = false;
        }
    }

    /**
     * Sets the internal world sizes toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents world sizes.
     */
    public void storeWorldSizes(JCheckBox box)
    {
        if (box.isSelected())
        {
            worldSizes = true;
        }
        else
        {
            worldSizes = false;
        }
    }

    /**
     * Sets the internal cities toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents cities.
     */
    public void storeCities(JCheckBox box)
    {
        if (box.isSelected())
        {
            cities = true;
        }
        else
        {
            cities = false;
        }
    }

    /**
     * Sets the internal colonies toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents colonies.
     */
    public void storeColonies(JCheckBox box)
    {
        if (box.isSelected())
        {
            colonies = true;
        }
        else
        {
            colonies = false;
        }
    }

    /**
     * Sets the internal continents toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents continents.
     */
    public void storeContinents(JCheckBox box)
    {
        if (box.isSelected())
        {
            continents = true;
        }
        else
        {
            continents = false;
        }
    }

    /**
     * Sets the internal starting locations toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents starting locations.
     */
    public void storeStartingLocations(JCheckBox box)
    {
        if (box.isSelected())
        {
            startingLocations = true;
        }
        else
        {
            startingLocations = false;
        }
    }

    /**
     * Sets the internal tiles toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents tiles.
     */
    public void storeTiles(JCheckBox box)
    {
        if (box.isSelected())
        {
            tiles = true;
        }
        else
        {
            tiles = false;
        }
    }

    /**
     * Sets the internal map units toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents units on the map.
     */
    public void storeMapUnits(JCheckBox box)
    {
        if (box.isSelected())
        {
            mapUnits = true;
        }
        else
        {
            mapUnits = false;
        }
    }

    /**
     * Sets the internal world characteristics toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents world characteristics.
     */
    public void storeWorldCharacteristics(JCheckBox box)
    {
        if (box.isSelected())
        {
            worldCharacteristics = true;
        }
        else
        {
            worldCharacteristics = false;
        }
    }

    /**
     * Sets the internal world map toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents the world map.
     */
    public void storeWorldMap(JCheckBox box)
    {
        if (box.isSelected())
        {
            worldMap = true;
        }
        else
        {
            worldMap = false;
        }
    }

    /**
     * Sets the internal additional properties toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents additional properties.
     */
    public void storeAdditionalProperties(JCheckBox box)
    {
        if (box.isSelected())
        {
            additionalProperties = true;
        }
        else
        {
            additionalProperties = false;
        }
    }

    /**
     * Sets the internal player data toggle based on the value of the check box.
     *
     * @param box - The JCheckBox that represents player data.
     */
    public void storePlayerData(JCheckBox box)
    {
        if (box.isSelected())
        {
            playerData = true;
        }
        else
        {
            playerData = false;
        }
    }
}
