package itdelatrisu.windsong.converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for beatmaps.
 */
public class BeatmapParser {
	/**
	 * Invokes parser for each OSU file in a root directory.
	 * @param root the root directory (search has depth 1)
	 */
	public static void parseAllFiles(File root) {
		parseDirectories(root.listFiles());
	}

	/**
	 * Invokes parser for each directory in the given array.
	 * @param dirs the array of directories to parse
	 */
	public static void parseDirectories(File[] dirs) {
		if (dirs == null)
			return;

		// parse directories
		for (File dir : dirs) {
			if (!dir.isDirectory())
				continue;

			// find all OSU files
			File[] files = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".osu");
				}
			});
			if (files == null || files.length < 1)
				continue;

			// parse beatmap files and write converted Windsong files
			for (File file : files) {
				MapFile map = parseFile(file, dir);
				if (map != null) {
					String filename = file.getName();
					String newFilename = filename.substring(0, filename.length() - 4) + ".wind";
					map.write(new File(dir, newFilename));
				}
			}
		}
	}

	/**
	 * Parses a beatmap.
	 * @param file the file to parse
	 * @param dir the directory containing the beatmap
	 * @return the new Windsong map file
	 */
	private static MapFile parseFile(File file, File dir) {
		MapFile map = new MapFile(file);
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			String line = in.readLine();
			String tokens[] = null;
			while (line != null) {
				line = line.trim();
				if (!isValidLine(line)) {
					line = in.readLine();
					continue;
				}
				switch (line) {
				case "[General]":
					while ((line = in.readLine()) != null) {
						line = line.trim();
						if (!isValidLine(line))
							continue;
						if (line.charAt(0) == '[')
							break;
						if ((tokens = tokenize(line)) == null)
							continue;
						try {
							switch (tokens[0]) {
							case "AudioFilename":
								File audioFileName = new File(dir, tokens[1]);
								if (!audioFileName.isFile()) {
									// try to find the file with a case-insensitive match
									boolean match = false;
									for (String s : dir.list()) {
										if (s.equalsIgnoreCase(tokens[1])) {
											audioFileName = new File(dir, s);
											match = true;
											break;
										}
									}
									if (!match) {
										System.out.printf("Audio file '%s' not found in directory '%s'.", tokens[1], dir.getName());
										return null;
									}
								}
								map.audioFilename = audioFileName;
								break;
							case "Mode":
								/* Non-osu! standard files not implemented. */
								if (Byte.parseByte(tokens[1]) != 0) return null;
								break;
							default:
								break;
							}
						} catch (Exception e) { e.printStackTrace(); }
					}
					break;
				case "[Metadata]":
					while ((line = in.readLine()) != null) {
						line = line.trim();
						if (!isValidLine(line))
							continue;
						if (line.charAt(0) == '[')
							break;
						if ((tokens = tokenize(line)) == null)
							continue;
						try {
							switch (tokens[0]) {
							case "Title":
								map.title = tokens[1];
								break;
							case "Artist":
								map.artist = tokens[1];
								break;
							case "Creator":
								map.creator = tokens[1];
								break;
							}
						} catch (Exception e) { e.printStackTrace(); }
					}
					break;
				case "[Difficulty]":
					while ((line = in.readLine()) != null) {
						line = line.trim();
						if (!isValidLine(line))
							continue;
						if (line.charAt(0) == '[')
							break;
						if ((tokens = tokenize(line)) == null)
							continue;
						try {
							switch (tokens[0]) {
							case "OverallDifficulty":
								map.difficulty = Math.round(Float.parseFloat(tokens[1]));
								break;
							}
						} catch (Exception e) { e.printStackTrace(); }
					}
					break;
				case "[HitObjects]":
					List<HitObject> hitObjects = new ArrayList<HitObject>();
					while ((line = in.readLine()) != null) {
						line = line.trim();
						if (!isValidLine(line))
							continue;
						if (line.charAt(0) == '[')
							break;
						try {
							hitObjects.add(new HitObject(line));
						} catch (Exception e) { e.printStackTrace(); }
					}
					map.objects = hitObjects.toArray(new HitObject[hitObjects.size()]);
					break;
				default:
					line = in.readLine();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// no associated audio file?
		if (map.audioFilename == null)
			return null;

		return map;
	}

	/**
	 * Returns false if the line is too short or commented.
	 */
	private static boolean isValidLine(String line) {
		return (line.length() > 1 && !line.startsWith("//"));
	}

	/**
	 * Splits line into two strings: tag, value.
	 * If no ':' character is present, null will be returned.
	 */
	private static String[] tokenize(String line) {
		int index = line.indexOf(':');
		if (index == -1) {
			System.out.printf("Failed to tokenize line: '%s'.", line);
			return null;
		}

		String[] tokens = new String[2];
		tokens[0] = line.substring(0, index).trim();
		tokens[1] = line.substring(index + 1).trim();
		return tokens;
	}
}