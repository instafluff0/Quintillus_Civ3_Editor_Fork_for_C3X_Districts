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

    private static final Logger logger = Logger.getLogger(DistrictDefinitions.class);

    private static final String DEFAULT_DISTRICT_CONFIG = "default.districts_config.txt";
    private static final String CUSTOM_DISTRICT_CONFIG = "custom.districts_config.txt";
    private static final String DEFAULT_WONDER_CONFIG = "default.districts_wonders_config.txt";
    private static final String CUSTOM_WONDER_CONFIG = "custom.districts_wonders_config.txt";
    private static final int MAX_DEPENDENT_COLUMNS = 5;

    public static class DistrictDefinition {
        private final int id;
        private final String name;
        private final List<String> imgPaths;
        private final boolean varyByEra;
        private final boolean varyByCulture;
        private final int maxBuildingIndex;
        private final List<String> dependentImprovements;

        DistrictDefinition(int id,
                           String name,
                           List<String> imgPaths,
                           boolean varyByEra,
                           boolean varyByCulture,
                           int maxBuildingIndex,
                           List<String> dependentImprovements) {
            this.id = id;
            this.name = name;
            this.imgPaths = Collections.unmodifiableList(new ArrayList<String>(imgPaths));
            this.varyByEra = varyByEra;
            this.varyByCulture = varyByCulture;
            this.maxBuildingIndex = maxBuildingIndex;
            this.dependentImprovements = Collections.unmodifiableList(new ArrayList<String>(dependentImprovements));
        }

        DistrictDefinition merge(DistrictSection overrides) {
            List<String> newImgPaths = (overrides.imgPaths != null)
                    ? Collections.unmodifiableList(new ArrayList<String>(overrides.imgPaths))
                    : imgPaths;
            boolean newVaryByEra = (overrides.varyByEra != null) ? overrides.varyByEra.booleanValue() : varyByEra;
            boolean newVaryByCulture = (overrides.varyByCulture != null) ? overrides.varyByCulture.booleanValue() : varyByCulture;
            List<String> newDependentImprovements = (overrides.dependentImprovements != null)
                    ? Collections.unmodifiableList(new ArrayList<String>(overrides.dependentImprovements))
                    : dependentImprovements;
            int newMaxBuildingIndex = maxBuildingIndex;
            if (overrides.dependentImprovements != null) {
                int count = Math.min(overrides.dependentImprovements.size(), MAX_DEPENDENT_COLUMNS);
                newMaxBuildingIndex = Math.max(0, count);
            }
            return new DistrictDefinition(id, name, newImgPaths, newVaryByEra, newVaryByCulture, newMaxBuildingIndex, newDependentImprovements);
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<String> getImgPaths() {
            return imgPaths;
        }

        public boolean isVaryByEra() {
            return varyByEra;
        }

        public boolean isVaryByCulture() {
            return varyByCulture;
        }

        public int getMaxBuildingIndex() {
            return maxBuildingIndex;
        }

        public List<String> getDependentImprovementNames() {
            return dependentImprovements;
        }

        public int getDependentImprovementCount() {
            return dependentImprovements.size();
        }

        public int getVariantCount() {
            return Math.max(1, imgPaths.size());
        }

        public int getColumnCount() {
            return Math.max(1, maxBuildingIndex + 1);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class WonderDefinition {
        private final int index;
        private final String name;
        private final String imgPath;
        private final int imgRow;
        private final int imgColumn;
        private final int constructRow;
        private final int constructColumn;

        WonderDefinition(int index,
                         String name,
                         String imgPath,
                         int imgRow,
                         int imgColumn,
                         int constructRow,
                         int constructColumn) {
            this.index = index;
            this.name = name;
            this.imgPath = (imgPath != null && imgPath.length() > 0) ? imgPath : "Wonders.pcx";
            this.imgRow = Math.max(0, imgRow);
            this.imgColumn = Math.max(0, imgColumn);
            this.constructRow = Math.max(0, constructRow);
            this.constructColumn = Math.max(0, constructColumn);
        }

        WonderDefinition merge(WonderSection overrides) {
            String newImgPath = (overrides.imgPath != null) ? overrides.imgPath : imgPath;
            int newImgRow = (overrides.imgRow != null) ? overrides.imgRow.intValue() : imgRow;
            int newImgColumn = (overrides.imgColumn != null) ? overrides.imgColumn.intValue() : imgColumn;
            int newConstructRow = (overrides.constructRow != null) ? overrides.constructRow.intValue() : constructRow;
            int newConstructColumn = (overrides.constructColumn != null) ? overrides.constructColumn.intValue() : constructColumn;
            return new WonderDefinition(index, name, newImgPath, newImgRow, newImgColumn, newConstructRow, newConstructColumn);
        }

        public int getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }

        public String getImgPath() {
            return imgPath;
        }

        public int getImgRow() {
            return imgRow;
        }

        public int getImgColumn() {
            return imgColumn;
        }

        public int getConstructRow() {
            return constructRow;
        }

        public int getConstructColumn() {
            return constructColumn;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class DistrictSection {
        String name;
        List<String> imgPaths;
        Boolean varyByEra;
        Boolean varyByCulture;
        List<String> dependentImprovements;

        void apply(String key, String value) {
            if ("name".equals(key)) {
                name = unquote(value);
            } else if ("img_paths".equals(key)) {
                imgPaths = parseList(value);
            } else if ("vary_img_by_era".equals(key)) {
                varyByEra = parseBoolean(value);
            } else if ("vary_img_by_culture".equals(key)) {
                varyByCulture = parseBoolean(value);
            } else if ("dependent_improvs".equals(key)) {
                dependentImprovements = parseList(value);
            }
        }
    }

    private static class WonderSection {
        String name;
        String imgPath;
        Integer imgRow;
        Integer imgColumn;
        Integer constructRow;
        Integer constructColumn;

        void apply(String key, String value) {
            if ("name".equals(key)) {
                name = unquote(value);
            } else if ("img_path".equals(key)) {
                imgPath = unquote(value);
            } else if ("img_row".equals(key)) {
                imgRow = parseInteger(value);
            } else if ("img_column".equals(key)) {
                imgColumn = parseInteger(value);
            } else if ("img_construct_row".equals(key)) {
                constructRow = parseInteger(value);
            } else if ("img_construct_column".equals(key)) {
                constructColumn = parseInteger(value);
            }
        }
    }

    private final List<DistrictDefinition> districts = new ArrayList<DistrictDefinition>();
    private final Map<String, Integer> nameToId = new HashMap<String, Integer>();
    private final List<WonderDefinition> wonders = new ArrayList<WonderDefinition>();
    private final Map<String, Integer> wonderNameToIndex = new HashMap<String, Integer>();

    private DistrictDefinitions() {
        addSpecialDistrict(createNeighborhoodDefinition());
        addSpecialDistrict(createWonderDistrictDefinition());
        addSpecialDistrict(createDistributionHubDefinition());
        addSpecialDistrict(createAerodromeDefinition());
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

    public DistrictDefinition getDistrict(int id) {
        if (id < 0 || id >= districts.size()) {
            return null;
        }
        return districts.get(id);
    }

    public List<WonderDefinition> getWonders() {
        return Collections.unmodifiableList(wonders);
    }

    public WonderDefinition getWonder(int index) {
        if (index < 0 || index >= wonders.size()) {
            return null;
        }
        return wonders.get(index);
    }

    private void addSpecialDistrict(DistrictDefinition def) {
        ensureIndexCapacity(def.getId() + 1);
        districts.set(def.getId(), def);
        nameToId.put(def.getName().toLowerCase(Locale.ENGLISH), Integer.valueOf(def.getId()));
    }

    private void ensureIndexCapacity(int size) {
        while (districts.size() < size) {
            districts.add(null);
        }
    }

    private static DistrictDefinition createNeighborhoodDefinition() {
        List<String> imgPaths = new ArrayList<String>();
        imgPaths.add("Neighborhood_AMER.pcx");
        imgPaths.add("Neighborhood_EURO.pcx");
        imgPaths.add("Neighborhood_ROMAN.pcx");
        imgPaths.add("Neighborhood_MIDEAST.pcx");
        imgPaths.add("Neighborhood_ASIAN.pcx");
        imgPaths.add("Neighborhood_Abandoned.pcx");
        return new DistrictDefinition(
                NEIGHBORHOOD_DISTRICT_ID,
                "Neighborhood",
                imgPaths,
                true,
                true,
                3,
                Collections.<String>emptyList());
    }

    private static DistrictDefinition createWonderDistrictDefinition() {
        List<String> imgPaths = Collections.singletonList("WonderDistrict.pcx");
        return new DistrictDefinition(
                WONDER_DISTRICT_ID,
                "Wonder District",
                imgPaths,
                true,
                false,
                0,
                Collections.<String>emptyList());
    }

    private static DistrictDefinition createDistributionHubDefinition() {
        List<String> imgPaths = Collections.singletonList("DistributionHub.pcx");
        return new DistrictDefinition(
                DISTRIBUTION_HUB_DISTRICT_ID,
                "Distribution Hub",
                imgPaths,
                true,
                false,
                0,
                Collections.<String>emptyList());
    }

    private static DistrictDefinition createAerodromeDefinition() {
        List<String> imgPaths = Collections.singletonList("Aerodrome.pcx");
        return new DistrictDefinition(
                AERODROME_DISTRICT_ID,
                "Aerodrome",
                imgPaths,
                true,
                false,
                0,
                Collections.<String>emptyList());
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
        DistrictSection section = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith(";")) {
                    continue;
                }
                if (trimmed.startsWith("#")) {
                    applyDistrictSection(section);
                    section = new DistrictSection();
                    continue;
                }
                int eqIndex = trimmed.indexOf('=');
                if (eqIndex < 0) {
                    continue;
                }
                if (section == null) {
                    section = new DistrictSection();
                }
                String key = trimmed.substring(0, eqIndex).trim().toLowerCase(Locale.ENGLISH);
                String value = trimmed.substring(eqIndex + 1).trim();
                section.apply(key, value);
            }
            applyDistrictSection(section);
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

    private void applyDistrictSection(DistrictSection section) {
        if (section == null || section.name == null || section.name.isEmpty()) {
            return;
        }
        String key = section.name.toLowerCase(Locale.ENGLISH);
        Integer existingId = nameToId.get(key);
        if (existingId != null) {
            DistrictDefinition current = districts.get(existingId.intValue());
            DistrictDefinition updated = current.merge(section);
            districts.set(existingId.intValue(), updated);
        } else {
            int id = districts.size();
            ensureIndexCapacity(id + 1);
            List<String> imgPaths = (section.imgPaths != null) ? section.imgPaths : Collections.<String>emptyList();
            boolean varyByEra = (section.varyByEra != null) ? section.varyByEra.booleanValue() : false;
            boolean varyByCulture = (section.varyByCulture != null) ? section.varyByCulture.booleanValue() : false;
            List<String> dependent = (section.dependentImprovements != null) ? section.dependentImprovements : Collections.<String>emptyList();
            int maxBuildingIndex = dependent.isEmpty() ? 0 : Math.min(dependent.size(), MAX_DEPENDENT_COLUMNS);
            DistrictDefinition created = new DistrictDefinition(id, section.name, imgPaths, varyByEra, varyByCulture, maxBuildingIndex, dependent);
            districts.set(id, created);
            nameToId.put(key, Integer.valueOf(id));
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
        WonderSection section = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith(";")) {
                    continue;
                }
                if (trimmed.startsWith("#")) {
                    applyWonderSection(section);
                    section = new WonderSection();
                    continue;
                }
                int eqIndex = trimmed.indexOf('=');
                if (eqIndex < 0) {
                    continue;
                }
                if (section == null) {
                    section = new WonderSection();
                }
                String key = trimmed.substring(0, eqIndex).trim().toLowerCase(Locale.ENGLISH);
                String value = trimmed.substring(eqIndex + 1).trim();
                section.apply(key, value);
            }
            applyWonderSection(section);
        }
        catch (IOException e) {
            logger.warn("Failed reading district wonder config: " + file.getAbsolutePath(), e);
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

    private void applyWonderSection(WonderSection section) {
        if (section == null || section.name == null || section.name.isEmpty()) {
            return;
        }
        String key = section.name.toLowerCase(Locale.ENGLISH);
        Integer existingIndex = wonderNameToIndex.get(key);
        if (existingIndex != null) {
            WonderDefinition current = wonders.get(existingIndex.intValue());
            WonderDefinition updated = current.merge(section);
            wonders.set(existingIndex.intValue(), updated);
        } else {
            int index = wonders.size();
            String imgPath = (section.imgPath != null) ? section.imgPath : "Wonders.pcx";
            int imgRow = (section.imgRow != null) ? section.imgRow.intValue() : 0;
            int imgColumn = (section.imgColumn != null) ? section.imgColumn.intValue() : 0;
            int constructRow = (section.constructRow != null) ? section.constructRow.intValue() : 0;
            int constructColumn = (section.constructColumn != null) ? section.constructColumn.intValue() : 0;
            WonderDefinition created = new WonderDefinition(index, section.name, imgPath, imgRow, imgColumn, constructRow, constructColumn);
            wonders.add(created);
            wonderNameToIndex.put(key, Integer.valueOf(index));
        }
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

    private static List<String> parseList(String raw) {
        if (raw == null) {
            return Collections.emptyList();
        }
        String text = raw.trim();
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        String[] parts = text.split(",");
        List<String> result = new ArrayList<String>(parts.length);
        for (String part : parts) {
            String cleaned = unquote(part.trim());
            if (!cleaned.isEmpty()) {
                result.add(cleaned);
            }
        }
        return result;
    }

    private static Boolean parseBoolean(String value) {
        if (value == null) {
            return null;
        }
        String normalized = unquote(value).trim().toLowerCase(Locale.ENGLISH);
        if (normalized.isEmpty()) {
            return null;
        }
        if ("1".equals(normalized) || "true".equals(normalized) || "yes".equals(normalized)) {
            return Boolean.TRUE;
        }
        if ("0".equals(normalized) || "false".equals(normalized) || "no".equals(normalized)) {
            return Boolean.FALSE;
        }
        return null;
    }

    private static Integer parseInteger(String value) {
        if (value == null) {
            return null;
        }
        String normalized = unquote(value).trim();
        if (normalized.isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(Integer.parseInt(normalized));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private static String unquote(String text) {
        if (text == null) {
            return null;
        }
        String trimmed = text.trim();
        if (trimmed.length() >= 2) {
            char first = trimmed.charAt(0);
            char last = trimmed.charAt(trimmed.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                return trimmed.substring(1, trimmed.length() - 1);
            }
        }
        return trimmed;
    }
}
