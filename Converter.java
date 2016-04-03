package itdelatrisu.windsong.converter;

import java.io.File;

/**
 * Main class.
 */
public class Converter {
	/**
	 * Invokes parser for each OSU file in a root directory.
	 * @param root the root directory (search has depth 1)
	 */
	public static void convert(File root) {
		BeatmapParser.parseAllFiles(root);
	}

	/** Runs the converter. */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("arguments: [root directory]");
			System.exit(1);
		}
		convert(new File(args[0]));
	}
}
