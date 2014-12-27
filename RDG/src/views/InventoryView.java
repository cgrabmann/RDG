package views;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import elements.Armament;
import elements.Element;
import elements.Equipment;
import elements.Potion;
import elements.Weapon;
import gameEssentials.Map;
import general.Enums.Armor;
import general.Enums.ImageSize;
import general.Enums.ItemClasses;
import general.Enums.UsedClasses;
import general.Enums.WeaponTypes;
import general.ResourceManager;

/**
 * InventoryView extends a View in the Inventory context.
 * 
 * @see View
 */
public class InventoryView extends View {
	
	private static InventoryView INSTANCE = null;

	/* Some Values for positioning this View */
	public int ORIGIN_X;
	public int ORIGIN_Y;
	private final int border = 5;

	/* Collection for all Items, saved in Inventory */
	/*private LinkedList<Weapon> weapons;
	private LinkedList<Armament> armaments;
	private LinkedList<Potion> potions;
	private LinkedList<Equipment> armor;*/
	private LinkedList<Element> items;

	/* ResourceManager */
	private ResourceManager resources;
	
	/* max items in inventory */
	private final int maxItems = 24;
	private int storedItemsCtr = 0;
	
	/* player can also aquire one key */
	private boolean hasKey = false;

	/**
	 * Constructs an InventoryView passing its origin as single x and y
	 * coordinates in tile numbers. Dimension will be set to default values in
	 * pixels.<br>
	 * <br>
	 * InventoryView extends View.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @throws SlickException
	 * @see InventoryView
	 */
	private InventoryView(String contextName, int originX, int originY)
			throws SlickException {
		this(contextName, new Point(originX, originY));
	}

	/**
	 * Constructs an InventoryView passing its origin as a Point in tile
	 * numbers. Dimension will be set to default values in pixels.<br>
	 * <br>
	 * InventoryView extends View.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @throws SlickException
	 * @param contextName
	 * @param origin
	 * @throws SlickException
	 * @see InventoryView
	 */
	private InventoryView(String contextName, Point origin)
			throws SlickException {
		this(contextName, origin, new Dimension(640, 480));
	}

	/**
	 * Constructs an InventoryView passing its origin as single x and y
	 * coordinates in tile numbers and its Dimension as single x and y
	 * coordinates in pixels.<br>
	 * <br>
	 * InventoryView extends View.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @param width
	 * @param height
	 * @throws SlickException
	 * @see InventoryView
	 */
	private InventoryView(String contextName, int originX, int originY,
			int width, int height) throws SlickException {
		this(contextName, new Point(originX, originY), new Dimension(width,
				height));
	}

	/**
	 * Constructs an InventoryView passing its origin as a Point in tile numbers
	 * and its Dimension as a Dimension in pixels.<br>
	 * <br>
	 * InventoryView extends View.
	 * 
	 * @param contextName
	 * @param origin
	 * @param size
	 * @throws SlickException
	 * @see InventoryView
	 */
	private InventoryView(String contextName, Point origin, Dimension size)
			throws SlickException {
		super(contextName, origin, size);

		ORIGIN_X = origin.x * GameEnvironment.BLOCK_SIZE;
		ORIGIN_Y = 240;

		resources = new ResourceManager().getInstance();

		items = new LinkedList<Element>();
		
		/*weapons = new LinkedList<Weapon>();
		armaments = new LinkedList<Armament>();
		armor = new LinkedList<Equipment>();
		potions = new LinkedList<Potion>();*/

		/* for testing */
		/*items.add(new Weapon("Dolch", resources.IMAGES.get("M_Weapon"), 0f, 0f,
				0f, 0f, ItemClasses.MEDIUM, WeaponTypes.SINGLEHAND, 0));
		items.add(new Weapon("Schwert", resources.IMAGES.get("S_Weapon"), 0f,
				0f, 0f, 0f, ItemClasses.MEDIUM, WeaponTypes.SINGLEHAND, 0));

		items.add(new Armament("Helmet", resources.IMAGES.get("Helmet"),
				"dont know what type is for", ItemClasses.MEDIUM, 0f, 0f, 0f,
				Armor.HEAD));
		items.add(new Armament("Chest", resources.IMAGES.get("Cuirass"),
				"dont know what type is for", ItemClasses.MEDIUM, 0f, 0f, 0f,
				Armor.CHEST));
		items.add(new Armament("Arms", resources.IMAGES.get("Arms"),
				"dont know what type is for", ItemClasses.MEDIUM, 0f, 0f, 0f,
				Armor.ARMS));
		items.add(new Armament("Shoes", resources.IMAGES.get("Shoes"),
				"dont know what type is for", ItemClasses.MEDIUM, 0f, 0f, 0f,
				Armor.FEET));
		items.add(new Armament("Legs", resources.IMAGES.get("Legs"),
				"dont know what type is for", ItemClasses.MEDIUM, 0f, 0f, 0f,
				Armor.LEGS));*/
		/* testing */
	}
	
	/**This shall only be called for constructing the first inventory view.
	 * @return the one and only instance of inventory view
	 * @throws SlickException
	 */
	public static InventoryView getInstance(String contextName, Point origin, Dimension size) throws SlickException {
		if (INSTANCE == null) {
			INSTANCE = new InventoryView(contextName, origin, size);
		}
		return INSTANCE;
	}
	
	/**
	 * @return the one and only instance of inventory view
	 * @throws SlickException
	 */
	public static InventoryView getInstance() throws SlickException {
		return INSTANCE;
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {
		graphics.setColor(new Color(0.2f, 0.2f, 0.2f));
		graphics.fillRect(ORIGIN_X, ORIGIN_Y, size.width, size.height);
		graphics.setColor(new Color(0f, 0f, 1f));
		graphics.fillRect(ORIGIN_X + border, ORIGIN_Y + border, size.width - 2
				* border, size.height - 2 * border);

		int x = 0, y = 0;

		for (Element e : items) {
			graphics.drawImage(e.getImage(ImageSize.d20x20), 10 + ORIGIN_X + x * 40, 10
					+ ORIGIN_Y + y * 40);
			if (x == 3) {
				x = 0;
				y++;
			} else {
				x++;
			}
		}

	}

	@Override
	public void update() {

	}

	/* 160:240 */
	/**Returns the selected Item from Inventory screen.
	 * 
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public Element getItem(int mouseX, int mouseY, UsedClasses classname) {		
		
		if (mouseX > ORIGIN_X && mouseX < ORIGIN_X + size.width
				&& mouseY > ORIGIN_Y && mouseY < ORIGIN_Y + size.height) {
						
			Class<?> tempClass = null;
			try {
				tempClass = Class.forName( "elements." + classname);
			} catch (ClassNotFoundException e1) {
				System.out.println("Only element classes are allowed in inventory interaction!");
			}
									
			int x = 0, y = 0;
			for (int i = 0; i < items.size(); i++) {

				if (mouseX > ORIGIN_X + x * 40
						&& mouseX < ORIGIN_X + x * 40 + 40
						&& mouseY > ORIGIN_Y + y * 40
						&& mouseY < ORIGIN_Y + y * 40 + 40) {
					break;
				}

				if (i % 4 == 0 && i != 0) {
					y++;
					x = 0;
				} else {
					x++;
				}
			}
			
			if ((x + y*4) < items.size()) {
				Element e = items.get(x + y*4);
				
				if (!(e.NAME.equals("Key"))) {
					
					/* only allow operation for specified class */
					if (!(tempClass.isInstance(e))) {
						return null;
					} 
					
					items.remove(x + y*4);
										
					if(e != null) {
						this.storedItemsCtr--;
					}
				} else {
					e = null;
				}
				
				return e;
			}
		}
		return null;
	}
	
	public void showDescription(int mouseX, int mouseY) {
		int x = 0, y = 0;
		for (int i = 0; i < items.size(); i++) {

			if (mouseX > ORIGIN_X + x * 40
					&& mouseX < ORIGIN_X + x * 40 + 40
					&& mouseY > ORIGIN_Y + y * 40
					&& mouseY < ORIGIN_Y + y * 40 + 40) {
				break;
			}

			if (i % 4 == 0 && i != 0) {
				y++;
				x = 0;
			} else {
				x++;
			}
		}
		
		String description = items.get(x + y*4).getDescription();
	}
	
	/*
	public void storeEquipment(Equipment equipment) {
		armor.add(equipment);
	} */ //Flo: deprecated -> replaced by storeItem
	
	
	/**Add items to lists of items, weapons, armaments, potions, armor.
	 * @param item
	 */
	public Element storeItem(Element item, ArmorView armorView) {
		
		if (item == null) {
			return null;
		}
		
		if (storedItemsCtr >= maxItems) {
			return item;
		} else {
			if (item instanceof Weapon) {
				/*weapons.add((Weapon) item);
				armor.add((Equipment) item);*/
				if (!(item.NAME.equals("Fists"))) {
					items.add(item);
					storedItemsCtr++;
				}
				armorView.addFists();
			}
			else if (item instanceof Armament) {
				/*armaments.add((Armament) item);
				armor.add((Equipment) item);*/
				items.add(item);
				storedItemsCtr++;
			}
			else if (item instanceof Potion) {
				/*potions.add((Potion) item);*/
				items.add(item);
				storedItemsCtr++;
			}
			else if (item.NAME.equals("Key") && this.hasKey == false) {
				items.add(item);
				this.hasKey = true;
				storedItemsCtr++;
			}
			return null;
		}
	}
	
	/**
	 * @return true if there is still more room for items to be stored
	 */
	public boolean hasMoreRoom() {
		if (this.storedItemsCtr < this.maxItems) {
			return true;
		}
		return false;
	}
	
	/**
	 * @return true if player has already found the key for treasure chamber
	 */
	public boolean hasKey() {
		return this.hasKey;
	}
}
