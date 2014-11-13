package worthy;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class ResourceManager {

	private static ResourceManager INSTANCE = null;

	public Image PLAYER;
	public Image ARMOR_BACKGROUND;
	public SpriteSheet TILES;
	
	public Image HELMET;
	public Image ARMS;
	public Image CUIRASS;
	public Image LEGS;
	public Image SHOES;
	public Image M_WEAPON;
	public Image S_WEAPON;

	/*
	 * public enum Direction { NORTH, SOUTH, EAST, WEST }
	 */

	public ResourceManager() throws SlickException {

	}

	private void loadResources() throws SlickException {
		PLAYER = new Image("./pictures/soldier_32x32.png");
		ARMOR_BACKGROUND = new Image("./pictures/warrior_160x160.png");
		TILES = new SpriteSheet("./pictures/tileset.png", 32, 32);
		HELMET = new Image("./pictures/Head.png");
		ARMS = new Image("./pictures/Arm.png");
		CUIRASS = new Image("./pictures/Chest.png");
		LEGS = new Image("./pictures/Legs.png");
		SHOES = new Image("./pictures/Feet.png");
		M_WEAPON = new Image("./pictures/Weapon.png");
		S_WEAPON = new Image("./pictures/Weapon2.png");
	}

	public ResourceManager getInstance() throws SlickException {
		if (INSTANCE == null) {
			INSTANCE = new ResourceManager();
			INSTANCE.loadResources();
		}

		return INSTANCE;
	}
}
