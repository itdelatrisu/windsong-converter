package itdelatrisu.windsong.converter;

/**
 * Data type representing a parsed hit object.
 */
public class HitObject {
	/** Hit sound types. */
	private static final int SOUND_NORMAL = 0, SOUND_CLAP = 1;

	/** Position. */
	private int position;

	/** Start time (in ms). */
	private int time;

	/** Hit sound type. */
	private int sound;

	/**
	 * Constructor.
	 * @param line the line to be parsed
	 */
	public HitObject(String line) {
		String tokens[] = line.split(",");
		this.time = Integer.parseInt(tokens[2]);
		this.position = (int) (Math.random() * 9);
		this.sound = Integer.parseInt(tokens[4]) == 0 ? SOUND_NORMAL : SOUND_CLAP;
	}

	/**
	 * Returns the start time.
	 * @return the start time (in ms)
	 */
	public int getTime() { return time; }

	/**
	 * Returns the position.
	 */
	public int getPosition() { return position; }

	/**
	 * Returns the hit sound type.
	 * @return the sound type
	 */
	public int getSound() { return sound; }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(time); sb.append(',');
		sb.append(position); sb.append(',');
		sb.append(sound);
		return sb.toString();
	}
}
