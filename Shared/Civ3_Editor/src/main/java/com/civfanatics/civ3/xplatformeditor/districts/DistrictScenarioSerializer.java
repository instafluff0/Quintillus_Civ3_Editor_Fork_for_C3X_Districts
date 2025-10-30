package com.civfanatics.civ3.xplatformeditor.districts;

import com.civfanatics.civ3.biqFile.CITY;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.TILE;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

/**
 * Handles loading and saving district scenario companion files ("*.c3x.txt").
 */
public final class DistrictScenarioSerializer {

	private static final Logger logger = Logger.getLogger(DistrictScenarioSerializer.class);

	private DistrictScenarioSerializer() {
	}

	public static void loadDistrictScenario(IO biq, File scenarioFile, File civInstallDir) {
		if ((biq == null) || (scenarioFile == null))
			return;

		File districtFile = resolveCompanionFile(scenarioFile);
		if ((districtFile == null) || (! districtFile.isFile()))
			return;

		DistrictDefinitions definitions = DistrictDefinitions.load(civInstallDir);
		if (definitions == null) {
			logger.warn("Unable to load district definitions. Skipping district scenario load.");
			return;
		}

		clearExistingDistricts(biq);

		List<String> parseErrors = new ArrayList<String>();
		List<String> unrecognized = new ArrayList<String>();

		Section current = null;
		boolean headerSeen = false;
		int lineNumber = 0;

		try (BufferedReader reader = new BufferedReader(new FileReader(districtFile))) {
			String raw;
			while ((raw = reader.readLine()) != null) {
				lineNumber++;
				String trimmed = raw.trim();
				if (trimmed.isEmpty() || trimmed.startsWith(";"))
					continue;

				if (! headerSeen) {
					if ("DISTRICTS".equalsIgnoreCase(trimmed)) {
						headerSeen = true;
						continue;
					}
					parseErrors.add(formatError(lineNumber, "Expected \"DISTRICTS\" header"));
					headerSeen = true;
					if (! trimmed.startsWith("#"))
						continue;
				}

				if (trimmed.startsWith("#")) {
					if (current != null)
						applySection(current, biq, definitions, parseErrors);

					String directive = trimmed.substring(1).trim().toLowerCase(Locale.ENGLISH);
					if (! "district".equals(directive)) {
						unrecognized.add(formatError(lineNumber, "Unrecognized directive " + trimmed));
						current = null;
						continue;
					}

					current = new Section();
					current.sectionStartLine = lineNumber;
					continue;
				}

				if (current == null)
					continue;

				int eqIndex = trimmed.indexOf('=');
				if (eqIndex < 0) {
					parseErrors.add(formatError(lineNumber, "Expected '='"));
					continue;
				}

				String key = trimmed.substring(0, eqIndex).trim().toLowerCase(Locale.ENGLISH);
				String value = trimmed.substring(eqIndex + 1).trim();
				handleKey(current, key, value, lineNumber, parseErrors, unrecognized);
			}

			if (current != null)
				applySection(current, biq, definitions, parseErrors);
		}
		catch (IOException e) {
			logger.warn("Failed reading district scenario file: " + districtFile.getAbsolutePath(), e);
		}

		if (! parseErrors.isEmpty() || ! unrecognized.isEmpty())
			showParseWarnings(districtFile, parseErrors, unrecognized);
	}

	public static void saveDistrictScenario(IO biq, File scenarioFile, File civInstallDir) {
		if ((biq == null) || (scenarioFile == null))
			return;

		File districtFile = resolveCompanionFile(scenarioFile);
		if (districtFile == null)
			return;

		DistrictDefinitions definitions = DistrictDefinitions.load(civInstallDir);
		if (definitions == null) {
			logger.warn("Unable to load district definitions. Skipping district scenario save.");
			return;
		}

		List<TILE> tiles = biq.tile;
		if (tiles == null) {
			logger.warn("No tiles available when saving district scenario file.");
			return;
		}

		List<OutputEntry> entries = new ArrayList<OutputEntry>();
		for (TILE tile : tiles) {
			if (tile == null)
				continue;
			TILE.DistrictData data = tile.getDistrictData();
			if ((data == null) || (data.districtId < 0))
				continue;

			int districtType = (data.districtType >= 0) ? data.districtType : data.districtId;
			DistrictDefinitions.DistrictDefinition def = definitions.getDistrict(districtType);
			if (def == null) {
				logger.warn("Skipping district at (" + tile.xPos + "," + tile.yPos + ") with unknown type " + districtType);
				continue;
			}

			OutputEntry entry = new OutputEntry();
			entry.x = tile.xPos;
			entry.y = tile.yPos;
			entry.districtName = def.getName();

			if (districtType == DistrictDefinitions.WONDER_DISTRICT_ID) {
				TILE.WonderDistrictInfo info = data.wonderInfo;
				if ((info != null) && (info.state == TILE.WDS_COMPLETED) && (info.wonderIndex >= 0)) {
					DistrictDefinitions.WonderDefinition wonder = definitions.getWonder(info.wonderIndex);
					CITY wonderCity = getCityById(biq.city, info.cityId);
					if ((wonder != null) && (wonderCity != null) && isNonEmpty(wonderCity.getName())) {
						entry.wonderName = wonder.getName();
						entry.wonderCity = wonderCity.getName();
					}
				}
			}

			entries.add(entry);
		}

		Collections.sort(entries);

		if ((districtFile.getParentFile() != null) && (! districtFile.getParentFile().exists()))
			districtFile.getParentFile().mkdirs();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(districtFile))) {
			writer.write("DISTRICTS");
			writer.newLine();
			writer.newLine();

		boolean firstEntry = true;
		for (OutputEntry entry : entries) {
			if (! firstEntry)
				writer.newLine();
			firstEntry = false;
			writer.write("#District");
			writer.newLine();
			writeKeyValue(writer, "coordinates", entry.x + "," + entry.y);
			writeKeyValue(writer, "district", entry.districtName);
			if (isNonEmpty(entry.wonderCity) && isNonEmpty(entry.wonderName)) {
				writeKeyValue(writer, "wonder_city", entry.wonderCity);
				writeKeyValue(writer, "wonder_name", entry.wonderName);
			}
			writer.newLine();
		}
		}
		catch (IOException e) {
			logger.error("Failed writing district scenario file: " + districtFile.getAbsolutePath(), e);
			showSaveError(districtFile, e.getMessage());
		}
	}

	private static void clearExistingDistricts(IO biq) {
		List<TILE> tiles = biq.tile;
		if (tiles == null)
			return;
		for (TILE tile : tiles)
			if (tile != null)
				tile.clearDistrict();
	}

	private static void applySection(Section section,
					     IO biq,
					     DistrictDefinitions definitions,
					     List<String> parseErrors) {
		if (section == null)
			return;

		boolean success = true;
		if (section.x == null || section.y == null) {
			parseErrors.add(formatError(section.sectionStartLine, "coordinates (value is required)"));
			success = false;
		}
		if (! isNonEmpty(section.districtName)) {
			parseErrors.add(formatError(section.sectionStartLine, "district (value is required)"));
			success = false;
		}
		if (! success)
			return;

		TILE tile = biq.getTileAt(section.x.intValue(), section.y.intValue());
		if (tile == null) {
			parseErrors.add(formatError(section.sectionStartLine, "Invalid coordinates (tile not found)"));
			return;
		}

		int districtId = findDistrictId(definitions, section.districtName);
		if (districtId < 0) {
			parseErrors.add(formatError(section.sectionStartLine, "district (unrecognized name)"));
			return;
		}

		int wonderIndex = -1;
		int wonderCityId = -1;
		if (districtId == DistrictDefinitions.WONDER_DISTRICT_ID) {
			if (! isNonEmpty(section.wonderName) || (! isNonEmpty(section.wonderCity))) {
				parseErrors.add(formatError(section.sectionStartLine, "Wonder district requires wonder_name and wonder_city"));
				return;
			}
			wonderIndex = findWonderIndex(definitions, section.wonderName);
			if (wonderIndex < 0) {
				parseErrors.add(formatError(section.sectionStartLine, "wonder_name (unrecognized)"));
				return;
			}
			wonderCityId = findCityIdByName(biq.city, section.wonderCity);
			if (wonderCityId < 0) {
				parseErrors.add(formatError(section.sectionStartLine, "wonder_city (unrecognized)"));
				return;
			}
		} else {
			if (isNonEmpty(section.wonderName) || isNonEmpty(section.wonderCity))
				parseErrors.add(formatError(section.sectionStartLine, "wonder_* fields ignored for non-wonder districts"));
		}

		tile.setDistrict(districtId, TILE.DISTRICT_STATE_COMPLETED);
		TILE.DistrictData data = tile.ensureDistrictData();
		data.districtId = districtId;
		data.districtType = districtId;
		data.state = TILE.DISTRICT_STATE_COMPLETED;

		TILE.WonderDistrictInfo info = new TILE.WonderDistrictInfo();
		info.wonderIndex = wonderIndex;
		info.wonderId = wonderIndex;
		info.cityId = wonderCityId;
		info.state = (districtId == DistrictDefinitions.WONDER_DISTRICT_ID && wonderIndex >= 0 && wonderCityId >= 0)
			? TILE.WDS_COMPLETED : 0;
		data.wonderInfo = info;
	}

	private static void handleKey(Section section,
				    String key,
				    String value,
				    int lineNumber,
				    List<String> parseErrors,
				    List<String> unrecognized) {
		if ("coordinates".equals(key)) {
			String text = stripQuotes(value);
			String[] parts = text.split(",");
			if (parts.length != 2) {
				parseErrors.add(formatError(lineNumber, "coordinates (expected format x,y)"));
				return;
			}
			try {
				section.x = Integer.valueOf(Integer.parseInt(parts[0].trim()));
				section.y = Integer.valueOf(Integer.parseInt(parts[1].trim()));
			}
			catch (NumberFormatException ex) {
				parseErrors.add(formatError(lineNumber, "coordinates (invalid integers)"));
			}
		}
		else if ("district".equals(key)) {
			section.districtName = stripQuotes(value);
		}
		else if ("wonder_city".equals(key)) {
			section.wonderCity = stripQuotes(value);
		}
		else if ("wonder_name".equals(key)) {
			section.wonderName = stripQuotes(value);
		}
		else {
			unrecognized.add(formatError(lineNumber, key + " (unrecognized key)"));
		}
	}

	private static int findDistrictId(DistrictDefinitions definitions, String name) {
		if (! isNonEmpty(name))
			return -1;
		String needle = name.trim().toLowerCase(Locale.ENGLISH);
		for (DistrictDefinitions.DistrictDefinition def : definitions.getDistricts()) {
			if ((def != null) && isNonEmpty(def.getName()) && def.getName().trim().toLowerCase(Locale.ENGLISH).equals(needle))
				return def.getId();
		}
		return -1;
	}

	private static int findWonderIndex(DistrictDefinitions definitions, String name) {
		if (! isNonEmpty(name))
			return -1;
		String needle = name.trim().toLowerCase(Locale.ENGLISH);
		List<DistrictDefinitions.WonderDefinition> wonders = definitions.getWonders();
		for (int i = 0; i < wonders.size(); i++) {
			DistrictDefinitions.WonderDefinition wd = wonders.get(i);
			if ((wd != null) && isNonEmpty(wd.getName()) && wd.getName().trim().toLowerCase(Locale.ENGLISH).equals(needle))
				return i;
		}
		return -1;
	}

	private static int findCityIdByName(List<CITY> cities, String name) {
		if (! isNonEmpty(name) || cities == null)
			return -1;
		String needle = name.trim().toLowerCase(Locale.ENGLISH);
		for (int i = 0; i < cities.size(); i++) {
			CITY city = cities.get(i);
			if ((city != null) && isNonEmpty(city.getName()) && city.getName().trim().toLowerCase(Locale.ENGLISH).equals(needle))
				return i;
		}
		return -1;
	}

	private static CITY getCityById(List<CITY> cities, int cityId) {
		if ((cities == null) || (cityId < 0) || (cityId >= cities.size()))
			return null;
		return cities.get(cityId);
	}

	private static void writeKeyValue(BufferedWriter writer, String key, String value) throws IOException {
		writer.write(String.format(Locale.ENGLISH, "%-12s = %s", key, value));
		writer.newLine();
	}

	private static void showParseWarnings(final File file, final List<String> parseErrors, final List<String> unrecognized) {
		if ((parseErrors == null || parseErrors.isEmpty()) && (unrecognized == null || unrecognized.isEmpty()))
			return;
		final StringBuilder sb = new StringBuilder();
		sb.append("Issues while reading ").append(file.getName()).append(':');
		sb.append('\n');
		for (String line : parseErrors)
			sb.append(line).append('\n');
		if (! unrecognized.isEmpty()) {
			sb.append('\n');
			sb.append("Unrecognized entries:").append('\n');
			for (String line : unrecognized)
				sb.append(line).append('\n');
		}

		Runnable dialog = new Runnable() {
			@Override
			public void run() {
				JOptionPane.showMessageDialog(null, sb.toString(), "District Scenario", JOptionPane.WARNING_MESSAGE);
			}
		};
		if (SwingUtilities.isEventDispatchThread())
			dialog.run();
		else
			SwingUtilities.invokeLater(dialog);
	}

	private static void showSaveError(final File file, final String message) {
		Runnable dialog = new Runnable() {
			@Override
			public void run() {
				String text = (message != null && message.length() > 0)
					? ("Failed to save " + file.getName() + ": " + message)
					: ("Failed to save " + file.getName());
				JOptionPane.showMessageDialog(null, text, "District Scenario", JOptionPane.ERROR_MESSAGE);
			}
		};
		if (SwingUtilities.isEventDispatchThread())
			dialog.run();
		else
			SwingUtilities.invokeLater(dialog);
	}

	private static File resolveCompanionFile(File scenarioFile) {
		if (scenarioFile == null)
			return null;
		String name = scenarioFile.getName();
		int dot = name.lastIndexOf('.');
		String base = (dot >= 0) ? name.substring(0, dot) : name;
		File parent = scenarioFile.getParentFile();
		return new File(parent, base + ".c3x.txt");
	}

	private static boolean isNonEmpty(String text) {
		return (text != null) && (text.trim().length() > 0);
	}

	private static String stripQuotes(String text) {
		if (text == null)
			return null;
		String trimmed = text.trim();
		if (trimmed.length() >= 2) {
			char first = trimmed.charAt(0);
			char last = trimmed.charAt(trimmed.length() - 1);
			if ((first == '"' && last == '"') || (first == '\'' && last == '\''))
				return trimmed.substring(1, trimmed.length() - 1);
		}
		return trimmed;
	}

	private static String formatError(int lineNumber, String message) {
		return "Line " + lineNumber + ": " + message;
	}

	private static class Section {
		int sectionStartLine;
		Integer x;
		Integer y;
		String districtName;
		String wonderCity;
		String wonderName;
	}

	private static class OutputEntry implements Comparable<OutputEntry> {
		int x;
		int y;
		String districtName;
		String wonderCity;
		String wonderName;

		@Override
		public int compareTo(OutputEntry other) {
			if (other == null)
				return 1;
			if (y != other.y)
				return Integer.compare(y, other.y);
			return Integer.compare(x, other.x);
		}
	}
}
