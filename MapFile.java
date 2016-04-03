package itdelatrisu.windsong.converter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Structure storing data parsed from map files.
 */
public class MapFile {
	/** The File object associated with this map. */
	@SuppressWarnings("unused")
	private File file;

	/** Audio file object. */
	public File audioFilename;

	/** Song title. */
	public String title = "";

	/** Song artist. */
	public String artist = "";

	/** Mmap creator. */
	public String creator = "";

	/** Map difficulty. */
	public int difficulty = 1;

	/** All hit objects. */
	public HitObject[] objects;

	/**
	 * Constructor.
	 * @param file the file associated with this map
	 */
	public MapFile(File file) { this.file = file; }

	/**
	 * Writes the map to a file.
	 * @param f the file
	 */
	public void write(File f) {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(f), "utf-8"))) {
			writer.write("[Metadata]");
			writer.newLine();
			writer.write("AudioFilename: ");
			writer.write(audioFilename.getName());
			writer.newLine();
			writer.write("Title: ");
			writer.write(title);
			writer.newLine();
			writer.write("Artist: ");
			writer.write(artist);
			writer.newLine();
			writer.write("Creator: ");
			writer.write(creator);
			writer.newLine();
			writer.write("Difficulty: ");
			writer.write(Integer.toString(difficulty));
			writer.newLine();
			writer.newLine();
			writer.write("[HitObjects]");
			writer.newLine();
			for (int i = 0; i < objects.length; i++) {
				writer.write(objects[i].toString());
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}