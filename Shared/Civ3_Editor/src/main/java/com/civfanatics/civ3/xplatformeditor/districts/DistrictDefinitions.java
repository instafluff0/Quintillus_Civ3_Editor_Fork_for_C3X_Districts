package com.civfanatics.civ3.xplatformeditor.districts;

import com.civfanatics.civ3.xplatformeditor.Main;
import com.civfanatics.civ3.xplatformeditor.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;

public class DistrictDefinitions {

    public static final int NEIGHBORHOOD_DISTRICT_ID = 0;
    public static final int WONDER_DISTRICT_ID = 1;
    public static final int DISTRIBUTION_HUB_DISTRICT_ID = 2;
    public static final int AERODROME_DISTRICT_ID = 3;

    private static final String DEFAULT_DISTRICT_CONFIG = "default.districts_config.txt";
    private static final String CUSTOM_DISTRICT_CONFIG = "custom.districts_config.txt";
    private static final String DEFAULT_WONDER_CONFIG = "default.districts_wonders_config.txt";
    private static final String CUSTOM_WONDER_CONFIG = "custom.districts_wonders_config.txt";

    private static final Logger logger = Logger.getLogger(DistrictDefinitions.class);

    public static class DistrictDefinition {
        public final int id;
        public final String name;

        DistrictDefinition(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class WonderDefinition {
        public final int index;
        public final String name;

        WonderDefinition(int index, String name) {
            this.index = index;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private final List<DistrictDefinition> districts = new ArrayList<DistrictDefinition>();
    private final Map<String, Integer> nameToId = new HashMap<String, Integer>();
    private final List<WonderDefinition> wonders = new ArrayList<WonderDefinition>();

    private DistrictDefinitions() {
        addSpecialDistrict(NEIGHBORHOOD_DISTRICT_ID, "Neighborhood");
        addSpecialDistrict(WONDER_DISTRICT_ID, "Wonder District");
        addSpecialDistrict(DISTRIBUTION_HUB_DISTRICT_ID, "Distribution Hub");
        addSpecialDistrict(AERODROME_DISTRICT_ID, "Aerodrome");
    }

    public static DistrictDefinitions load(File civInstallDir) {
        DistrictDefinitions defs = new DistrictDefinitions();
        defs.loadDistrictConfigs(civInstallDir);
        defs.loadWonderConfigs(civInstallDir);
        return defs;
    }

    public List<DistrictDefinition> getDistricts() {
        return Collections.unmodifiableList(districts);
    }

    public List<WonderDefinition> getWonders() {
        return Collections.unmodifiableList(wonders);
    }

    private void addSpecialDistrict(int id, String name) {
        ensureIndexCapacity(id + 1);
        districts.set(id, new DistrictDefinition(id, name));
        nameToId.put(name.toLowerCase(Locale.ENGLISH), Integer.valueOf(id));
    }

    private void ensureIndexCapacity(int size) {
        while (districts.size() < size) {
            districts.add(null);
        }
    }

    private void addDynamicDistrict(String name) {
        if (name == null || name.isEmpty()) {
            return;
        }
        String key = name.toLowerCase(Locale.ENGLISH);
        if (nameToId.containsKey(key)) {
            // Overrides special district name; keep original ID
            int id = nameToId.get(key).intValue();
            districts.set(id, new DistrictDefinition(id, name));
            return;
        }
        int id = districts.size();
        districts.add(new DistrictDefinition(id, name));
        nameToId.put(key, Integer.valueOf(id));
    }

    private void loadDistrictConfigs(File civInstallDir) {
        List<File> sources = new ArrayList<File>();
        collectConfigCandidates(civInstallDir, DEFAULT_DISTRICT_CONFIG, sources);
        collectConfigCandidates(civInstallDir, CUSTOM_DISTRICT_CONFIG, sources);

        for (File config : sources) {
            if (config == null) {
                continue;
            }
            readDistrictConfig(config);
        }
    }

    private void readDistrictConfig(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            String pendingName = null;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith(";")) {
                    continue;
                }
                if (trimmed.startsWith("#")) {
                    if (pendingName != null) {
                        addDynamicDistrict(pendingName);
                        pendingName = null;
                    }
                    continue;
                }
                int eqIndex = trimmed.indexOf('=');
                if (eqIndex < 0) {
                    continue;
                }
                String key = trimmed.substring(0, eqIndex).trim().toLowerCase(Locale.ENGLISH);
                String value = trimmed.substring(eqIndex + 1).trim();
                if ("name".equals(key)) {
                    pendingName = unquote(value);
                }
            }
            if (pendingName != null) {
                addDynamicDistrict(pendingName);
            }
        }
        catch (IOException e) {
            logger.warn("Failed reading district config: " + file.getAbsolutePath(), e);
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ignored) {
                }
            }
        }
    }

    private void loadWonderConfigs(File civInstallDir) {
        List<File> sources = new ArrayList<File>();
        collectConfigCandidates(civInstallDir, DEFAULT_WONDER_CONFIG, sources);
        collectConfigCandidates(civInstallDir, CUSTOM_WONDER_CONFIG, sources);

        for (File config : sources) {
            if (config == null) {
                continue;
            }
            readWonderConfig(config);
        }
    }

    private void readWonderConfig(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            String pendingName = null;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith(";")) {
                    continue;
                }
                if (trimmed.startsWith("#")) {
                    if (pendingName != null) {
                        addWonder(pendingName);
                        pendingName = null;
                    }
                    continue;
                }
                int eqIndex = trimmed.indexOf('=');
                if (eqIndex < 0) {
                    continue;
                }
                String key = trimmed.substring(0, eqIndex).trim().toLowerCase(Locale.ENGLISH);
                String value = trimmed.substring(eqIndex + 1).trim();
                if ("name".equals(key)) {
                    pendingName = unquote(value);
                }
            }
            if (pendingName != null) {
                addWonder(pendingName);
            }
        }
        catch (IOException e) {
            logger.warn("Failed reading wonder config: " + file.getAbsolutePath(), e);
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ignored) {
                }
            }
        }
    }

    private void addWonder(String name) {
        if (name == null || name.isEmpty()) {
            return;
        }
        for (WonderDefinition def : wonders) {
            if (def.name.equalsIgnoreCase(name)) {
                return;
            }
        }
        wonders.add(new WonderDefinition(wonders.size(), name));
    }

    private void collectConfigCandidates(File civInstallDir, String fileName, List<File> out) {
        File config = resolveConfigFile(civInstallDir, fileName);
        if (config != null) {
            out.add(config);
        }
    }

    private File resolveConfigFile(File civInstallDir, String fileName) {
        List<File> candidates = new ArrayList<File>();
        if (civInstallDir != null) {
            try {
                String conquestsFolder = utils.getConquestsFolder(civInstallDir.getPath());
                if (conquestsFolder != null && conquestsFolder.length() > 0) {
                    File candidate = new File(conquestsFolder + "C3X" + Main.fileSlash + fileName);
                    candidates.add(candidate);
                }
            }
            catch (Exception e) {
                logger.debug("Unable to determine Conquests folder from civ install dir", e);
            }
        }
        candidates.add(new File(fileName));

        for (File candidate : candidates) {
            if (candidate != null && candidate.isFile()) {
                return candidate;
            }
        }
        logger.debug("District config not found: " + fileName);
        return null;
    }

    private String unquote(String text) {
        String trimmed = text.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"") && trimmed.length() >= 2) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        if (trimmed.startsWith("'") && trimmed.endsWith("'") && trimmed.length() >= 2) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }
}
