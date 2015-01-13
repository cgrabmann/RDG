package views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import views.font.TrueTypeFont;
import elements.Armament;
import elements.Element;
import elements.Equipment;
import elements.Potion;
import elements.Weapon;
import general.Enums.ArmorStatsAttributes;
import general.Enums.ArmorStatsMode;
import general.Enums.ArmorStatsTypes;
import general.Enums.ImageSize;
import general.Enums.Potions;
import general.Enums.WeaponTypes;
import general.ItemFactory;
import general.ResourceManager;
import general.Enums.Armor;

/**
 * ArmorView extends a View in the Armor context.
 * 
 * @see View
 */
/**
 * @author Flo
 *
 */
public class ArmorView extends View {

	/* Values for Positioning of the View */
	public int ORIGIN_X;
	public int ORIGIN_Y;
	private int space = 2;
	private int tabWidth = 30;
	private int tabHeight = 25;

	/* Values for positioning the Tabs */
	private int tab1X;
	private int tab1Y;
	private int tab2X;
	private int tab2Y;
	private int textPositionX;
	private int textPositionY;

	/* positions of items */
	private int headX;
	private int headY;
	private int main_wX;
	private int main_wY;
	private int sub_wX;
	private int sub_wY;
	private int chestX;
	private int chestY;
	private int armX;
	private int armY;
	private int legX;
	private int legY;
	private int feetX;
	private int feetY;
	private int pot1X;
	private int pot1Y;
	private int pot2X;
	private int pot2Y;
	private int pot3X;
	private int pot3Y;

	/* Mouse Position */
	private int mousePositionX;
	private int mousePositionY;
	
	/* width/height of ArmorImages */
	private final int IMAGE_SIZE = 20;

	/* Represents Set1 or Set2 */
	private boolean set = true;

	/* if the mouse is over an element and the description shall be shown */
	private String description;
	private boolean showDescription = false;
	private int descriptionWidth = 0;
	private int descriptionHeight = 0;
	
	/* Values of Player (Attack, Speed, Accuracy, Defense) */
	public final static int ATTACK = 0;
	public final static int SPEED = 1;
	public final static int ACCURACY = 2;
	public final static int DEFENSE = 3;
	private static float[] values = new float[4];

	/* HashMap for all equipped Armor */
	HashMap<Armor, Equipment> armor1;
	HashMap<Armor, Equipment> armor2;
	HashMap<Potions, Potion> potion1_types;
	HashMap<Potions, Potion> potion2_types;

	/* Collection for the armor, is used for iterating through all armor */
	Collection<Equipment> equipment1;
	Collection<Equipment> equipment2;
	Collection<Potion> potions1;
	Collection<Potion> potions2;

	/*
	 * stores previously equipped weapons to determine if weapon is dragged from
	 * armory or from inventory
	 */
	Weapon prevWeaponMainSet1 = null;
	Weapon prevWeaponSubSet1 = null;
	Weapon prevWeaponMainSet2 = null;
	Weapon prevWeaponSubSet2 = null;

	/*
	 * stores previously equipped potions to determine if potion is dragged from
	 * armory or from inventory
	 */
	Potion prevPotion1Set1 = null;
	Potion prevPotion2Set1 = null;
	Potion prevPotion3Set1 = null;
	Potion prevPotion1Set2 = null;
	Potion prevPotion2Set2 = null;
	Potion prevPotion3Set2 = null;

	/* Factory and Resource Classes */
	private ResourceManager resources;

	/* a potion drunk by a player and set in armorView.drinkPotion() */
	Potion selectedPotion = null;

	/* fallback weapon: fists */
	Weapon fists1 = null;
	Weapon fists2 = null;

	/* Colors */
	private final Color BLACK = new Color(0f, 0f, 0f);
	private final Color GREY = new Color(0.5f, 0.5f, 0.5f);
	private final Color DARK_GREY = new Color(0.25f, 0.25f, 0.25f);
	private final Color RED = new Color(1f, 0f, 0f);
	private final Color WHITE = new Color(1f, 1f, 1f);
	
	/**
	 * Constructs an ArmorView passing its origin as single x and y coordinates
	 * in tile numbers.<br>
	 * Dimension will be set to default values in pixels.<br>
	 * <br>
	 * ArmorView extends View.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @throws SlickException
	 * @see ArmorView
	 */
	public ArmorView(String contextName, int originX, int originY)
			throws SlickException {
		this(contextName, new Point(originX, originY));
	}

	/**
	 * Constructs an ArmorView passing its origin as a Point in tile numbers.<br>
	 * Dimension will be set to default values in pixels.<br>
	 * <br>
	 * ArmorView extends View.
	 * 
	 * @param contextName
	 * @param origin
	 * @throws SlickException
	 * @see ArmorView
	 */
	public ArmorView(String contextName, Point origin) throws SlickException {
		this(contextName, origin, new Dimension(640, 480));
	}

	/**
	 * Constructs an ArmorView passing its origin as single x and y coordinates
	 * in tile numbers and its Dimension as single x and y coordinates in
	 * pixels.<br>
	 * Dimension will be set to default values in pixels.<br>
	 * <br>
	 * ArmorView extends View.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @param width
	 * @param height
	 * @throws SlickException
	 * @see ArmorView
	 */
	public ArmorView(String contextName, int originX, int originY, int width,
			int height) throws SlickException {
		this(contextName, new Point(originX, originY), new Dimension(width,
				height));
	}

	/**
	 * Constructs an ArmorView passing its origin as a Point in tile numbers and
	 * its Dimension as a Dimension.<br>
	 * Dimension will be set to default values in pixels.<br>
	 * <br>
	 * ArmorView extends View.
	 * 
	 * @param contextName
	 * @param origin
	 * @param size
	 * @throws SlickException
	 * @see ArmorView
	 */
	public ArmorView(String contextName, Point origin, Dimension size)
			throws SlickException {
		super(contextName, origin, size);
		
		resources = new ResourceManager().getInstance();

		ORIGIN_X = origin.x * GameEnvironment.BLOCK_SIZE;
		ORIGIN_Y = origin.y * GameEnvironment.BLOCK_SIZE;

		tab1X = ORIGIN_X + space;
		tab1Y = ORIGIN_Y + 5;
		tab2X = ORIGIN_X + 2 * space + tabWidth;
		tab2Y = ORIGIN_Y + 5;
		textPositionX = ORIGIN_X + size.width - 50;
		textPositionY = ORIGIN_Y + 10;

		resources = new ResourceManager().getInstance();

		armor1 = new HashMap<Armor, Equipment>(7);
		equipment1 = armor1.values();
		armor2 = new HashMap<Armor, Equipment>(7);
		equipment2 = armor2.values();
		potion1_types = new HashMap<Potions, Potion>(3);
		potions1 = potion1_types.values();
		potion2_types = new HashMap<Potions, Potion>(3);
		potions2 = potion2_types.values();

		/* create fists as fallback weapons */
		fists1 = ItemFactory.createWeapon("Fists", 1);
		fists2 = ItemFactory.createWeapon("Fists", 1);

		fists2.setAsSubWeapon();

		armor1.put(Armor.MAIN_WEAPON, fists1);
		armor1.put(Armor.SUB_WEAPON, fists2);
		armor2.put(Armor.MAIN_WEAPON, fists1);
		armor2.put(Armor.SUB_WEAPON, fists2);

		headX = ORIGIN_X + 100;
		headY = ORIGIN_Y + 5 + tabHeight + 10;
		main_wX = ORIGIN_X + 20;
		main_wY = ORIGIN_Y + 75 + tabHeight + 10;
		sub_wX = ORIGIN_X + 125;
		sub_wY = ORIGIN_Y + 60 + tabHeight + 10;
		chestX = ORIGIN_X + 120;
		chestY = ORIGIN_Y + 30 + tabHeight + 10;
		armX = ORIGIN_X + 30;
		armY = ORIGIN_Y + 45 + tabHeight + 10;
		legX = ORIGIN_X + 125;
		legY = ORIGIN_Y + 100 + tabHeight + 10;
		feetX = ORIGIN_X + 35;
		feetY = ORIGIN_Y + 115 + tabHeight + 10;
		pot1X = ORIGIN_X + size.width / 2 - tabWidth - 2 - tabWidth / 2 + 6;
		pot1Y = ORIGIN_Y + size.height - tabHeight - 5 + 5;
		pot2X = ORIGIN_X + size.width / 2 - tabWidth / 2 + 6;
		pot2Y = ORIGIN_Y + size.height - tabHeight - 5 + 5;
		pot3X = ORIGIN_X + size.width / 2 + tabWidth / 2 + 2 + 6;
		pot3Y = ORIGIN_Y + size.height - tabHeight - 5 + 5;
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {

		/* grey background */
		graphics.setColor(GREY);
		graphics.fillRect(ORIGIN_X, ORIGIN_Y, size.width, tabHeight + 5);

		/* draw sets */
		graphics.setColor(RED);
		if (set) {
			graphics.fillRect(tab1X, tab1Y, tabWidth, tabHeight);
			graphics.fillRect(tab2X, tab2Y, tabWidth, tabHeight - 2);
			graphics.setColor(BLACK);
			graphics.setFont(resources.DEFAULT_FONTS.get("set"));
			graphics.drawString("SET 1", textPositionX, textPositionY-3);
		} else {
			graphics.fillRect(tab1X, tab1Y, tabWidth, tabHeight - 2);
			graphics.fillRect(tab2X, tab2Y, tabWidth, tabHeight);
			graphics.setColor(BLACK);
			graphics.setFont(resources.DEFAULT_FONTS.get("set"));
			graphics.drawString("SET 2", textPositionX, textPositionY-3);
		}

		/* red field */
		graphics.setColor(RED);
		graphics.fillRect(ORIGIN_X, ORIGIN_Y + 30, size.width, size.height - 2
				* (tabHeight - 5));

		/* draw warrior image */
		graphics.drawImage(resources.IMAGES.get("Armor_Background"), ORIGIN_X,
				ORIGIN_Y + tabHeight + 10);

		/* potion section */
		graphics.setColor(GREY);
		graphics.fillRect(ORIGIN_X, ORIGIN_Y + size.height - tabHeight - 5,
				size.width, tabHeight + 5);

		/* test potion section */
		graphics.setColor(DARK_GREY);
		graphics.fillRect(ORIGIN_X + size.width / 2 - tabWidth / 2, ORIGIN_Y
				+ size.height - tabHeight - 3, tabWidth, tabHeight);

		graphics.setColor(DARK_GREY);
		graphics.fillRect(ORIGIN_X + size.width / 2 - tabWidth / 2 - tabWidth
				- 2, ORIGIN_Y + size.height - tabHeight - 3, tabWidth,
				tabHeight);

		graphics.setColor(DARK_GREY);
		graphics.fillRect(ORIGIN_X + size.width / 2 + tabWidth / 2 + 2,
				ORIGIN_Y + size.height - tabHeight - 3, tabWidth, tabHeight);

		/* draw equipment and potions */
		if (set) {
			for (Equipment e : equipment1) {
				switch (e.getArmorType()) {
				case MAIN_WEAPON:
					graphics.drawImage(e.getImage(ImageSize.d20x20), main_wX,
							main_wY);
					break;
				case SUB_WEAPON:
					graphics.drawImage(e.getImage(ImageSize.d20x20), sub_wX,
							sub_wY);
					break;
				case HEAD:
					graphics.drawImage(e.getImage(ImageSize.d20x20), headX,
							headY);
					break;
				case CHEST:
					graphics.drawImage(e.getImage(ImageSize.d20x20), chestX,
							chestY);
					break;
				case ARMS:
					graphics.drawImage(e.getImage(ImageSize.d20x20), armX, armY);
					break;
				case LEGS:
					graphics.drawImage(e.getImage(ImageSize.d20x20), legX, legY);
					break;
				case FEET:
					graphics.drawImage(e.getImage(ImageSize.d20x20), feetX,
							feetY);
					break;
				}
			}
			for (Potion p : potions1) {
				switch (p.POTION_TYPE) {

				case POTION1:
					graphics.drawImage(p.getImage(ImageSize.d20x20), pot1X,
							pot1Y);
					break;
				case POTION2:
					graphics.drawImage(p.getImage(ImageSize.d20x20), pot2X,
							pot2Y);
					break;
				case POTION3:
					graphics.drawImage(p.getImage(ImageSize.d20x20), pot3X,
							pot3Y);
					break;
				}
			}
		} else {
			for (Equipment e : equipment2) {
				switch (e.getArmorType()) {
				case MAIN_WEAPON:
					graphics.drawImage(e.getImage(ImageSize.d20x20), main_wX,
							main_wY);
					break;
				case SUB_WEAPON:
					graphics.drawImage(e.getImage(ImageSize.d20x20), sub_wX,
							sub_wY);
					break;
				case HEAD:
					graphics.drawImage(e.getImage(ImageSize.d20x20), headX,
							headY);
					break;
				case CHEST:
					graphics.drawImage(e.getImage(ImageSize.d20x20), chestX,
							chestY);
					break;
				case ARMS:
					graphics.drawImage(e.getImage(ImageSize.d20x20), armX, armY);
					break;
				case LEGS:
					graphics.drawImage(e.getImage(ImageSize.d20x20), legX, legY);
					break;
				case FEET:
					graphics.drawImage(e.getImage(ImageSize.d20x20), feetX,
							feetY);
					break;
				}
			}
			for (Potion p : potions2) {
				switch (p.POTION_TYPE) {

				case POTION1:
					graphics.drawImage(p.getImage(ImageSize.d20x20), pot1X,
							pot1Y);
					break;
				case POTION2:
					graphics.drawImage(p.getImage(ImageSize.d20x20), pot2X,
							pot2Y);
					break;
				case POTION3:
					graphics.drawImage(p.getImage(ImageSize.d20x20), pot3X,
							pot3Y);
					break;
				}
			}
		}

		if (showDescription && description != null) {
			float addSpace = 0;
			if (descriptionWidth >= 130) {
				addSpace = 5;
			}
			graphics.setColor(WHITE);
			
			graphics.fillRect(mousePositionX - descriptionWidth,
					mousePositionY - descriptionHeight - 5,
					descriptionWidth, descriptionHeight + 10);
			((TrueTypeFont) resources.DEFAULT_FONTS.get("description")).drawString(mousePositionX - descriptionWidth + descriptionWidth/2, mousePositionY - descriptionHeight,
					description, BLACK, TrueTypeFont.ALIGN_CENTER);
			
			
			/*graphics.fillRect(mousePositionX - descriptionWidth - 50,
					mousePositionY - descriptionHeight - 5,
					descriptionWidth + 70, descriptionHeight + 10);
			((TrueTypeFont) resources.DEFAULT_FONTS.get("description")).drawString(mousePositionX - descriptionWidth
					+ descriptionWidth / 3 + addSpace, mousePositionY - descriptionHeight,
					description, BLACK, TrueTypeFont.ALIGN_CENTER);*/
		}
	}

	@Override
	public void update() {
		values[ATTACK] = 0;
		values[ACCURACY] = 0;
		values[DEFENSE] = 0;
		values[SPEED] = 0;
		
		if(set) {
			if(armor1.get(Armor.MAIN_WEAPON) != null) {
				values[ATTACK] += ((Weapon) armor1.get(Armor.MAIN_WEAPON)).ATTACK;
				values[ACCURACY] += ((Weapon) armor1.get(Armor.MAIN_WEAPON)).ACCURACY;
				values[SPEED] += ((Weapon) armor1.get(Armor.MAIN_WEAPON)).SPEED;
				values[DEFENSE] += ((Weapon) armor1.get(Armor.MAIN_WEAPON)).DEFENSE;
			}
			
			if(armor1.get(Armor.SUB_WEAPON) != null) {
				values[ATTACK] += ((Weapon) armor1.get(Armor.SUB_WEAPON)).ATTACK;
				values[ACCURACY] += ((Weapon) armor1.get(Armor.SUB_WEAPON)).ACCURACY;
				values[SPEED] += ((Weapon) armor1.get(Armor.SUB_WEAPON)).SPEED;
				values[DEFENSE] += ((Weapon) armor1.get(Armor.SUB_WEAPON)).DEFENSE;
			}
			
			if(armor1.get(Armor.HEAD) != null) {
				values[DEFENSE] += ((Armament) armor1.get(Armor.HEAD)).ARMOR;
				values[SPEED] -= ((Armament) armor1.get(Armor.HEAD)).SPEED;
			}
			
			if(armor1.get(Armor.CHEST) != null) {
				values[DEFENSE] += ((Armament) armor1.get(Armor.CHEST)).ARMOR;
				values[SPEED] -= ((Armament) armor1.get(Armor.CHEST)).SPEED;
			}
			
			if(armor1.get(Armor.ARMS) != null) {
				values[DEFENSE] += ((Armament) armor1.get(Armor.ARMS)).ARMOR;
				values[SPEED] -= ((Armament) armor1.get(Armor.ARMS)).SPEED;
			}
			
			if(armor1.get(Armor.LEGS) != null) {
				values[DEFENSE] += ((Armament) armor1.get(Armor.LEGS)).ARMOR;
				values[SPEED] -= ((Armament) armor1.get(Armor.LEGS)).SPEED;
			}
			
			if(armor1.get(Armor.FEET) != null) {
				values[DEFENSE] += ((Armament) armor1.get(Armor.FEET)).ARMOR;
				values[SPEED] -= ((Armament) armor1.get(Armor.FEET)).SPEED;
			}
		}else {
			if(armor2.get(Armor.MAIN_WEAPON) != null) {
				values[ATTACK] += ((Weapon) armor2.get(Armor.MAIN_WEAPON)).ATTACK;
				values[ACCURACY] += ((Weapon) armor2.get(Armor.MAIN_WEAPON)).ACCURACY;
				values[SPEED] += ((Weapon) armor2.get(Armor.MAIN_WEAPON)).SPEED;
				values[DEFENSE] += ((Weapon) armor2.get(Armor.MAIN_WEAPON)).DEFENSE;
			}
			
			if(armor2.get(Armor.SUB_WEAPON) != null) {
				values[ATTACK] += ((Weapon) armor2.get(Armor.SUB_WEAPON)).ATTACK;
				values[ACCURACY] += ((Weapon) armor2.get(Armor.SUB_WEAPON)).ACCURACY;
				values[SPEED] += ((Weapon) armor2.get(Armor.SUB_WEAPON)).SPEED;
				values[DEFENSE] += ((Weapon) armor2.get(Armor.SUB_WEAPON)).DEFENSE;
			}
			
			if(armor2.get(Armor.HEAD) != null) {
				values[DEFENSE] += ((Armament) armor2.get(Armor.HEAD)).ARMOR;
				values[SPEED] -= ((Armament) armor2.get(Armor.HEAD)).SPEED;
			}
			
			if(armor2.get(Armor.CHEST) != null) {
				values[DEFENSE] += ((Armament) armor2.get(Armor.CHEST)).ARMOR;
				values[SPEED] -= ((Armament) armor2.get(Armor.CHEST)).SPEED;
			}
			
			if(armor2.get(Armor.ARMS) != null) {
				values[DEFENSE] += ((Armament) armor2.get(Armor.ARMS)).ARMOR;
				values[SPEED] -= ((Armament) armor2.get(Armor.ARMS)).SPEED;
			}
			
			if(armor2.get(Armor.LEGS) != null) {
				values[DEFENSE] += ((Armament) armor2.get(Armor.LEGS)).ARMOR;
				values[SPEED] -= ((Armament) armor2.get(Armor.LEGS)).SPEED;
			}
			
			if(armor2.get(Armor.FEET) != null) {
				values[DEFENSE] += ((Armament) armor2.get(Armor.FEET)).ARMOR;
				values[SPEED] -= ((Armament) armor2.get(Armor.FEET)).SPEED;
			}
		}
		
		int mem = 0;
		mem = (int) (values[ATTACK] * 100);
		values[ATTACK] = ((float) mem)/100f;
		mem = (int) (values[SPEED] * 100);
		values[SPEED] = ((float) mem)/100f;
		mem = (int) (values[ACCURACY] * 100);
		values[ACCURACY] = ((float) mem)/100f;
		mem = (int) (values[DEFENSE] * 100);
		values[DEFENSE] = ((float) mem)/100f;
	}

	/**
	 * Switches between sets 1 and 2.
	 * 
	 * @param set
	 */
	public void switchSet() {
		if (set) {
			this.set = false;
		} else {
			this.set = true;
		}
	}

	/**
	 * Switches between Equipment tabs 1 and 2.
	 * 
	 * @param x
	 * @param y
	 */
	public void changeTab(int x, int y) {
		if (x > tab1X && x < tab1X + tabWidth && y > tab1Y
				&& y < tab1Y + tabHeight) {
			this.set = true;
		} else if (x > tab2X && x < tab2X + tabWidth && y > tab2Y
				&& y < tab2Y + tabHeight) {
			this.set = false;
		}
	}

	/**
	 * Player drinks a potion.
	 * 
	 * @param potion
	 *            - Element
	 * @param x
	 *            - MousePosition
	 * @param y
	 *            - MousePosition
	 * @return null or the Potion that has to be stored in the Inventory (if max
	 *         number of potions already reached during a fight)
	 */
	public Potion drinkPotion(Potion potion, int x, int y,
			InventoryView inventory) {
		Potion p = potion;
		if (potion == null) {
			return p;
		}
		if (x > ORIGIN_X && x < ORIGIN_X + size.width
				&& y > ORIGIN_Y + tabHeight + 5
				&& y < ORIGIN_Y + size.height - tabHeight - 5) {

			// Send info about taken potion to enemy ? -> only if potion effects
			// enemy?

			// set taken potion to be later obtained by fight.java
			this.selectedPotion = p;

			p = null; // only for testing purposes
		}

		return p;
	}

	/**
	 * Used by Fight.java to obtain the latest taken potion by the player.
	 * 
	 * @return the potion taken by a player
	 */
	public Potion getSelectedPotion() {

		Potion tempPotion = selectedPotion;
		selectedPotion = null;

		return tempPotion;
	}

	/**
	 * Equips the Item in a Set.
	 * 
	 * @param element
	 *            - Element
	 * @param x
	 *            - MousePosition
	 * @param y
	 *            - MousePosition
	 * @return null or the Equipment that has to be stored in the Inventory
	 */
	public Element equipItem(Element element, int x, int y,
			InventoryView inventory) {
		Element e = null;
		if (element == null) {
			return e;
		}

		/* check if dragged item is dragged to armor section */
		if (x > ORIGIN_X && x < ORIGIN_X + size.width
				&& y > ORIGIN_Y + tabHeight + 5
				&& y < ORIGIN_Y + size.height - tabHeight - 5
				&& element instanceof Equipment) {

			/* in case of weapons, detect which weapon shall be replaced */
			if (element instanceof Weapon) {

				Weapon thisweapon = (Weapon) element;

				Weapon weapon1 = null;
				Weapon weapon2 = null;

				boolean returnImmediatlyNull = false;
				boolean returnImmediatly = false;
				boolean fromInsideArmor = false;

				/* decide on which side to drop the weapon -> main or sub */
				if ((x - ORIGIN_X) > size.width / 2) {
					((Equipment) element).setAsSubWeapon();
					if (thisweapon == fists1) {
						thisweapon = fists2;
					}
				} else {
					((Equipment) element).setAsMainWeapon();
					if (thisweapon == fists2) {
						thisweapon = fists1;
					}
				}

				/* check if weapon is dragged from inside Armor */
				if (set) {

					/* check if dragged weapon is stored inside armor */

					if (thisweapon == prevWeaponMainSet1
							|| thisweapon == prevWeaponSubSet1) {
						fromInsideArmor = true;
					}
					if (thisweapon.NAME.equals("Fists")) {
						fromInsideArmor = true;
					}
					weapon1 = (Weapon) armor1.get(Armor.MAIN_WEAPON);
					weapon2 = (Weapon) armor1.get(Armor.SUB_WEAPON);
				} else {
					if (thisweapon == prevWeaponMainSet2
							|| thisweapon == prevWeaponSubSet2) {
						fromInsideArmor = true;
					}
					if (thisweapon.NAME.equals("Fists")) {
						fromInsideArmor = true;
					}
					weapon1 = (Weapon) armor2.get(Armor.MAIN_WEAPON);
					weapon2 = (Weapon) armor2.get(Armor.SUB_WEAPON);
				}

				/* return both weapons to inventory if a twohand is added */
				if (thisweapon.TYPE == WeaponTypes.TWOHAND) {

					if (set) {
						armor1.remove(Armor.MAIN_WEAPON);
						armor1.remove(Armor.SUB_WEAPON);
					} else {
						armor2.remove(Armor.MAIN_WEAPON);
						armor2.remove(Armor.SUB_WEAPON);
					}

					if (weapon1 != null) {
						inventory.storeItem(weapon1, this);
					}
					if (weapon2 != null) {
						inventory.storeItem(weapon2, this);
					}

				} else {

					/*
					 * exchange two single hand weapons, handle twohands, handle
					 * max 1 weapons
					 */
					if (weapon1 != null) {

						/*
						 * check for two-hand weapons or weapons with maximum of
						 * 1
						 */
						if (weapon1.TYPE == WeaponTypes.TWOHAND
								|| (weapon1.MAX == 1 && weapon1.NAME == thisweapon.NAME)) {
							if (((Equipment) element).getArmorType() == Armor.SUB_WEAPON) {
								returnImmediatly = true;
								e = ((Equipment) element);
							}
						}

						/* exchange weapons inside of armory */
						else if (((Equipment) element).getArmorType() == Armor.MAIN_WEAPON
								&& fromInsideArmor == true) {

							Equipment tempWeapon;

							if (set) {
								tempWeapon = armor1.get(Armor.MAIN_WEAPON);
								tempWeapon.setAsSubWeapon();
								armor1.put(Armor.SUB_WEAPON, tempWeapon);
								armor1.put(Armor.MAIN_WEAPON,
										((Equipment) element));
							} else {
								tempWeapon = armor2.get(Armor.MAIN_WEAPON);
								tempWeapon.setAsSubWeapon();
								armor2.put(Armor.SUB_WEAPON, tempWeapon);
								armor2.put(Armor.MAIN_WEAPON,
										((Equipment) element));
							}
							if (weapon2 == null) {
								returnImmediatlyNull = true;
							}
						}
					}
					if (weapon2 != null) {

						/*
						 * check for two-hand weapons or weapons with maximum of
						 * 1
						 */
						if (weapon2.TYPE == WeaponTypes.TWOHAND
								|| (weapon2.MAX == 1 && weapon2.NAME == thisweapon.NAME)) {
							if (((Equipment) element).getArmorType() == Armor.MAIN_WEAPON) {
								returnImmediatly = true;
								e = ((Equipment) element);
							}
						}

						/* exchange weapons inside of armory */
						else if (((Equipment) element).getArmorType() == Armor.SUB_WEAPON
								&& fromInsideArmor == true) {

							Equipment tempWeapon;

							if (set) {
								tempWeapon = armor1.get(Armor.SUB_WEAPON);
								tempWeapon.setAsMainWeapon();
								armor1.put(Armor.MAIN_WEAPON, tempWeapon);
								armor1.put(Armor.SUB_WEAPON,
										((Equipment) element));
							} else {
								tempWeapon = armor2.get(Armor.SUB_WEAPON);
								tempWeapon.setAsMainWeapon();
								armor2.put(Armor.MAIN_WEAPON, tempWeapon);
								armor2.put(Armor.SUB_WEAPON,
										((Equipment) element));
							}
							if (weapon1 == null) {
								returnImmediatlyNull = true;
							}
						}
					}
				}

				/*
				 * store current weapons to check for future weapon drop -> is
				 * new weapon from inventory or from armorview?
				 */
				prevWeaponMainSet1 = (Weapon) armor1.get(Armor.MAIN_WEAPON);
				prevWeaponSubSet1 = (Weapon) armor1.get(Armor.SUB_WEAPON);
				prevWeaponMainSet2 = (Weapon) armor2.get(Armor.MAIN_WEAPON);
				prevWeaponSubSet2 = (Weapon) armor2.get(Armor.SUB_WEAPON);

				/*
				 * do not allow adding another weapon when a twohand is equipped
				 * or max of weapon is 1
				 */
				if (returnImmediatly) {
					// addFists();
					return e;
				}
				/*
				 * do not allow adding another weapon when a twohand is equipped
				 * or max of weapon is 1
				 */
				if (returnImmediatlyNull) {
					// addFists();
					return null;
				}
			}

			if (set) {
				if (armor1.containsKey(((Equipment) element).getArmorType())) {
					e = armor1.get(((Equipment) element).getArmorType());
				}
				armor1.put(((Equipment) element).getArmorType(),
						((Equipment) element));
			} else {
				if (armor2.containsKey(((Equipment) element).getArmorType())) {
					e = armor2.get(((Equipment) element).getArmorType());
				}
				armor2.put(((Equipment) element).getArmorType(),
						((Equipment) element));
			}

			/*
			 * store current weapons to check for future weapon drop -> is new
			 * weapon from inventory or from armorview?
			 */
			prevWeaponMainSet1 = (Weapon) armor1.get(Armor.MAIN_WEAPON);
			prevWeaponSubSet1 = (Weapon) armor1.get(Armor.SUB_WEAPON);
			prevWeaponMainSet2 = (Weapon) armor2.get(Armor.MAIN_WEAPON);
			prevWeaponSubSet2 = (Weapon) armor2.get(Armor.SUB_WEAPON);

			// addFists();
			return e;

			/* check if item is dragged to potion section */
		} else if (x > ORIGIN_X && x < ORIGIN_X + size.width
				&& y > ORIGIN_Y + size.height - tabHeight - 5
				&& y < ORIGIN_Y + size.height && element instanceof Potion) {

			Potion thispotion = (Potion) element;

			/* decide if potion is dragged to potion 1,2,3 */
			if (x > ORIGIN_X + size.width / 2 - tabWidth / 2 - 2
					&& x < ORIGIN_X + size.width / 2 + tabWidth / 2 + 2) {
				((Potion) element).POTION_TYPE = Potions.POTION2;
			} else if (x > ORIGIN_X + size.width / 2 - tabWidth - 2 - tabWidth
					/ 2 - 2
					&& x < ORIGIN_X + size.width / 2 - tabWidth / 2 - 2) {
				((Potion) element).POTION_TYPE = Potions.POTION1;
			} else if (x > ORIGIN_X + size.width / 2 + tabWidth / 2 + 2
					&& x < ORIGIN_X + size.width / 2 + tabWidth / 2 + 2
							+ tabWidth + 2) {
				((Potion) element).POTION_TYPE = Potions.POTION3;
			}

			Potion potion1 = null;
			Potion potion2 = null;
			Potion potion3 = null;

			boolean fromInsideArmor = false;

			/* check if potion is dragged from inside Armor */
			if (set) {
				if (thispotion == prevPotion1Set1
						|| thispotion == prevPotion2Set1
						|| thispotion == prevPotion3Set1) {
					fromInsideArmor = true;

					if (thispotion == prevPotion1Set1) {
						if (thispotion.POTION_TYPE == Potions.POTION2
								&& prevPotion2Set1 != null) {
							prevPotion2Set1.POTION_TYPE = Potions.POTION1;
							potion1_types.put(Potions.POTION1, prevPotion2Set1);
						}
						if (thispotion.POTION_TYPE == Potions.POTION3
								&& prevPotion3Set1 != null) {
							prevPotion3Set1.POTION_TYPE = Potions.POTION1;
							potion1_types.put(Potions.POTION1, prevPotion3Set1);
						}
					} else if (thispotion == prevPotion2Set1) {
						if (thispotion.POTION_TYPE == Potions.POTION1
								&& prevPotion1Set1 != null) {
							prevPotion1Set1.POTION_TYPE = Potions.POTION2;
							potion1_types.put(Potions.POTION2, prevPotion1Set1);
						}
						if (thispotion.POTION_TYPE == Potions.POTION3
								&& prevPotion3Set1 != null) {
							prevPotion3Set1.POTION_TYPE = Potions.POTION2;
							potion1_types.put(Potions.POTION2, prevPotion3Set1);
						}
					} else if (thispotion == prevPotion3Set1) {
						if (thispotion.POTION_TYPE == Potions.POTION1
								&& prevPotion1Set1 != null) {
							prevPotion1Set1.POTION_TYPE = Potions.POTION3;
							potion1_types.put(Potions.POTION3, prevPotion1Set1);
						}
						if (thispotion.POTION_TYPE == Potions.POTION2
								&& prevPotion2Set1 != null) {
							prevPotion2Set1.POTION_TYPE = Potions.POTION3;
							potion1_types.put(Potions.POTION3, prevPotion2Set1);
						}
					}
				}
			} else {
				if (thispotion == prevPotion1Set2
						|| thispotion == prevPotion2Set2
						|| thispotion == prevPotion3Set2) {
					fromInsideArmor = true;

					if (thispotion == prevPotion1Set2) {
						if (thispotion.POTION_TYPE == Potions.POTION2
								&& prevPotion2Set2 != null) {
							prevPotion2Set2.POTION_TYPE = Potions.POTION1;
							potion2_types.put(Potions.POTION1, prevPotion2Set2);
						}
						if (thispotion.POTION_TYPE == Potions.POTION3
								&& prevPotion3Set2 != null) {
							prevPotion3Set2.POTION_TYPE = Potions.POTION1;
							potion2_types.put(Potions.POTION1, prevPotion3Set2);
						}
					} else if (thispotion == prevPotion2Set2) {
						if (thispotion.POTION_TYPE == Potions.POTION1
								&& prevPotion1Set2 != null) {
							prevPotion1Set2.POTION_TYPE = Potions.POTION2;
							potion2_types.put(Potions.POTION2, prevPotion1Set2);
						}
						if (thispotion.POTION_TYPE == Potions.POTION3
								&& prevPotion3Set2 != null) {
							prevPotion3Set2.POTION_TYPE = Potions.POTION2;
							potion2_types.put(Potions.POTION2, prevPotion3Set2);
						}
					} else if (thispotion == prevPotion3Set2) {
						if (thispotion.POTION_TYPE == Potions.POTION1
								&& prevPotion1Set2 != null) {
							prevPotion1Set2.POTION_TYPE = Potions.POTION3;
							potion2_types.put(Potions.POTION3, prevPotion1Set2);
						}
						if (thispotion.POTION_TYPE == Potions.POTION2
								&& prevPotion2Set2 != null) {
							prevPotion2Set2.POTION_TYPE = Potions.POTION3;
							potion2_types.put(Potions.POTION3, prevPotion2Set2);
						}
					}
				}
			}

			if (set) {
				if (potion1_types.containsKey(((Potion) element).POTION_TYPE)) {
					e = potion1_types.get(((Potion) element).POTION_TYPE);
				}
				potion1_types.put(((Potion) element).POTION_TYPE,
						((Potion) element));
			} else {
				if (potion2_types.containsKey(((Potion) element).POTION_TYPE)) {
					e = potion2_types.get(((Potion) element).POTION_TYPE);
				}
				potion2_types.put(((Potion) element).POTION_TYPE,
						((Potion) element));
			}

			if (fromInsideArmor) {
				e = null;
			}

			/*
			 * store current potions to check for future potion drop -> is new
			 * potion from inventory or from armorview?
			 */
			prevPotion1Set1 = (Potion) potion1_types.get(Potions.POTION1);
			prevPotion2Set1 = (Potion) potion1_types.get(Potions.POTION2);
			prevPotion3Set1 = (Potion) potion1_types.get(Potions.POTION3);
			prevPotion1Set2 = (Potion) potion2_types.get(Potions.POTION1);
			prevPotion2Set2 = (Potion) potion2_types.get(Potions.POTION2);
			prevPotion3Set2 = (Potion) potion2_types.get(Potions.POTION3);

		} else {
			e = element;
		}
		return e;
	}

	/**
	 * Always add Fist as fallback weapon when there are still free slots.
	 * 
	 */
	public void addFists() {

		Weapon equippedWeaponMain = null;
		Weapon equippedWeaponSub = null;
		int slotSum = 0;

		/* get the equipped weapons */
		if (set) {
			equippedWeaponMain = (Weapon) armor1.get(Armor.MAIN_WEAPON);
			equippedWeaponSub = (Weapon) armor1.get(Armor.SUB_WEAPON);
		} else {
			equippedWeaponMain = (Weapon) armor2.get(Armor.MAIN_WEAPON);
			equippedWeaponSub = (Weapon) armor2.get(Armor.SUB_WEAPON);
		}

		/* reset fists */
		if (equippedWeaponMain != null) {
			/* delete fists if equipping two-hand */
			if (set) {
				if (equippedWeaponMain.NAME.equals(("Fists"))) {
					armor1.remove(Armor.MAIN_WEAPON);
				}
			} else {
				if (equippedWeaponMain.NAME.equals(("Fists"))) {
					armor2.remove(Armor.MAIN_WEAPON);
				}
			}
			if (equippedWeaponMain.NAME.equals("Fists")) {
				equippedWeaponMain = null;
			}
		}
		if (equippedWeaponSub != null) {
			/* delete fists if equipping two-hand */
			if (set) {
				if (equippedWeaponSub.NAME.equals(("Fists"))) {
					armor1.remove(Armor.SUB_WEAPON);
				}
			} else {
				if (equippedWeaponSub.NAME.equals(("Fists"))) {
					armor2.remove(Armor.SUB_WEAPON);
				}
			}
			if (equippedWeaponSub.NAME.equals("Fists")) {
				equippedWeaponSub = null;
			}
		}
		fists1.setAsMainWeapon();
		fists2.setAsSubWeapon();

		/* calculate how many slots they need */
		if (equippedWeaponMain != null) {
			if (equippedWeaponMain.TYPE == WeaponTypes.SINGLEHAND) {
				slotSum += 1;
			} else if (equippedWeaponMain.TYPE == WeaponTypes.TWOHAND) {
				slotSum += 2;
			}
		}
		if (equippedWeaponSub != null) {
			if (equippedWeaponSub.TYPE == WeaponTypes.SINGLEHAND) {
				slotSum += 1;
			} else if (equippedWeaponSub.TYPE == WeaponTypes.TWOHAND) {
				slotSum += 2;
			}
		}

		/* if not 2 slots are used, fill up with fists */
		if (slotSum == 0) {
			fists1.setAsMainWeapon();
			fists2.setAsSubWeapon();
			if (set) {
				armor1.put(Armor.MAIN_WEAPON, fists1);
				prevWeaponMainSet1 = null;
				armor1.put(Armor.SUB_WEAPON, fists2);
				prevWeaponSubSet1 = null;
			} else {
				armor2.put(Armor.MAIN_WEAPON, fists1);
				prevWeaponMainSet2 = null;
				armor2.put(Armor.SUB_WEAPON, fists2);
				prevWeaponSubSet2 = null;
			}
		} else if (slotSum == 1) {
			if (equippedWeaponMain == null) {
				if (set) {
					fists1.setAsMainWeapon();
					armor1.put(Armor.MAIN_WEAPON, fists1);
					prevWeaponMainSet1 = null;
				} else {
					fists2.setAsMainWeapon();
					armor2.put(Armor.MAIN_WEAPON, fists1);
					prevWeaponMainSet2 = null;
				}
			} else if (equippedWeaponSub == null) {
				if (set) {
					fists1.setAsSubWeapon();
					armor1.put(Armor.SUB_WEAPON, fists2);
					prevWeaponSubSet1 = null;
				} else {
					fists2.setAsSubWeapon();
					armor2.put(Armor.SUB_WEAPON, fists2);
					prevWeaponSubSet2 = null;
				}
			}
		}
	}

	/**
	 * Returns an equipped item.
	 * 
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public Element getItem(int mouseX, int mouseY) {

		if (mouseX > ORIGIN_X && mouseX < ORIGIN_X + size.width
				&& mouseY > ORIGIN_Y
				&& mouseY < ORIGIN_Y + size.height - tabHeight - 5) {

			Equipment e = null;

			/* HEAD */
			if (mouseX > ORIGIN_X + 100 && mouseX < ORIGIN_X + 100 + IMAGE_SIZE
					&& mouseY > ORIGIN_Y + 5 + tabHeight + 10
					&& mouseY < ORIGIN_Y + 5 + tabHeight + 10 + IMAGE_SIZE) {
				if (set) {
					e = armor1.get(Armor.HEAD);
					armor1.remove(Armor.HEAD);
				} else {
					e = armor2.get(Armor.HEAD);
					armor2.remove(Armor.HEAD);
				}
			}
			/* CHEST */
			else if (mouseX > ORIGIN_X + 120
					&& mouseX < ORIGIN_X + 120 + IMAGE_SIZE
					&& mouseY > ORIGIN_Y + 30 + tabHeight + 10
					&& mouseY < ORIGIN_Y + 30 + tabHeight + 10 + IMAGE_SIZE) {
				if (set) {
					e = armor1.get(Armor.CHEST);
					armor1.remove(Armor.CHEST);
				} else {
					e = armor2.get(Armor.CHEST);
					armor2.remove(Armor.CHEST);
				}
			}
			/* ARMS */
			else if (mouseX > ORIGIN_X + 30
					&& mouseX < ORIGIN_X + 30 + IMAGE_SIZE
					&& mouseY > ORIGIN_Y + 45 + tabHeight + 10
					&& mouseY < ORIGIN_Y + 45 + tabHeight + 10 + IMAGE_SIZE) {
				if (set) {
					e = armor1.get(Armor.ARMS);
					armor1.remove(Armor.ARMS);
				} else {
					e = armor2.get(Armor.ARMS);
					armor2.remove(Armor.ARMS);
				}
			}
			/* LEGS */
			else if (mouseX > ORIGIN_X + 125
					&& mouseX < ORIGIN_X + 125 + IMAGE_SIZE
					&& mouseY > ORIGIN_Y + 100 + tabHeight + 10
					&& mouseY < ORIGIN_Y + 100 + tabHeight + 10 + IMAGE_SIZE) {
				if (set) {
					e = armor1.get(Armor.LEGS);
					armor1.remove(Armor.LEGS);
				} else {
					e = armor2.get(Armor.LEGS);
					armor2.remove(Armor.LEGS);
				}
			}
			/* FEET */
			else if (mouseX > ORIGIN_X + 35
					&& mouseX < ORIGIN_X + 35 + IMAGE_SIZE
					&& mouseY > ORIGIN_Y + 115 + tabHeight + 10
					&& mouseY < ORIGIN_Y + 115 + tabHeight + 10 + IMAGE_SIZE) {
				if (set) {
					e = armor1.get(Armor.FEET);
					armor1.remove(Armor.FEET);
				} else {
					e = armor2.get(Armor.FEET);
					armor2.remove(Armor.FEET);
				}
			}
			/* MAIN WEAPON */
			else if (mouseX > ORIGIN_X + 20
					&& mouseX < ORIGIN_X + 20 + IMAGE_SIZE
					&& mouseY > ORIGIN_Y + 75 + tabHeight + 10
					&& mouseY < ORIGIN_Y + 75 + tabHeight + 10 + IMAGE_SIZE) {
				if (set) {
					e = armor1.get(Armor.MAIN_WEAPON);
					armor1.remove(Armor.MAIN_WEAPON);
				} else {
					e = armor2.get(Armor.MAIN_WEAPON);
					armor2.remove(Armor.MAIN_WEAPON);
				}
			}
			/* SUB WEAPON */
			else if (mouseX > ORIGIN_X + 125
					&& mouseX < ORIGIN_X + 125 + IMAGE_SIZE
					&& mouseY > ORIGIN_Y + 60 + tabHeight + 10
					&& mouseY < ORIGIN_Y + 60 + tabHeight + 10 + IMAGE_SIZE) {
				if (set) {
					e = armor1.get(Armor.SUB_WEAPON);
					armor1.remove(Armor.SUB_WEAPON);
				} else {
					e = armor2.get(Armor.SUB_WEAPON);
					armor2.remove(Armor.SUB_WEAPON);
				}
			}

			return e;

		} else if (mouseX > ORIGIN_X && mouseX < ORIGIN_X + size.width
				&& mouseY > ORIGIN_Y + size.height - tabHeight - 5
				&& mouseY < ORIGIN_Y + size.height) {

			Potion e = null;

			/* decide if potion is dragged from potion 1,2,3 */
			if (mouseX > ORIGIN_X + size.width / 2 - tabWidth / 2 - 2
					&& mouseX < ORIGIN_X + size.width / 2 + tabWidth / 2 + 2) {
				if (set) {
					e = potion1_types.get(Potions.POTION2);
					potion1_types.remove(Potions.POTION2);
				} else {
					e = potion2_types.get(Potions.POTION2);
					potion2_types.remove(Potions.POTION2);
				}
			} else if (mouseX > ORIGIN_X + size.width / 2 - tabWidth - 2
					- tabWidth / 2 - 2
					&& mouseX < ORIGIN_X + size.width / 2 - tabWidth / 2 - 2) {
				if (set) {
					e = potion1_types.get(Potions.POTION1);
					potion1_types.remove(Potions.POTION1);
				} else {
					e = potion2_types.get(Potions.POTION1);
					potion2_types.remove(Potions.POTION1);
				}
			} else if (mouseX > ORIGIN_X + size.width / 2 + tabWidth / 2 + 2
					&& mouseX < ORIGIN_X + size.width / 2 + tabWidth / 2 + 2
							+ tabWidth + 2) {
				if (set) {
					e = potion1_types.get(Potions.POTION3);
					potion1_types.remove(Potions.POTION3);
				} else {
					e = potion2_types.get(Potions.POTION3);
					potion2_types.remove(Potions.POTION3);
				}
			}

			return e;
		}

		return null;
	}

	/**
	 * Return potions to armorView if dropped at wrong location.
	 * 
	 * @param potion
	 */
	public void backPotion(Potion potion) {

		if (set) {
			potion1_types.put(potion.POTION_TYPE, potion);
		} else {
			potion2_types.put(potion.POTION_TYPE, potion);
		}
	}

	/**
	 * Bonus is added when wearing a full set of armor.
	 * 
	 * @return the bonus for a full set of armor or 1
	 */
	private float armamentBonus() {

		float bonus = 1f;

		/* get one armament material */
		Equipment exampleItem = null;
		String armamentMaterial = null;

		if (set) {
			exampleItem = armor1.get(Armor.CHEST);
		} else {
			exampleItem = armor2.get(Armor.CHEST);
		}

		/* if one part in set is missing, bonus will not be applied */
		if (exampleItem == null) {
			return 1.0f;
		} else {

			armamentMaterial = ((Armament) exampleItem).TYPE;

			int sameCtr = 0; // must be five for a set bonus

			if (set) {
				// loop through all equipment in set and increase sameCtr if the
				// types match
				for (Equipment e : equipment1) {
					if (e instanceof Armament) {
						if (((Armament) e).TYPE.equals(armamentMaterial)) {
							sameCtr++;
						}
					}
				}
			} else {
				for (Equipment e : equipment2) {
					if (e instanceof Armament) {
						if (((Armament) e).TYPE.equals(armamentMaterial)) {
							sameCtr++;
						}
					}
				}
			}

			if (sameCtr == 5) {
				bonus = ((Armament) exampleItem).BONUS;
			}
		}

		return bonus;
	}

	/**
	 * Returns sum of all values for a specific armament attribute of all
	 * equipped armaments.<br>
	 * 
	 * @param speed
	 * @return
	 */
	public float getStats(ArmorStatsTypes type, ArmorStatsMode mode,
			ArmorStatsAttributes att) {

		float value = 0f;
		float subvalue = 0f;
		int itemCtr = 0; // needed for average calculation
		Collection<Equipment> myEquipment = null;

		if (set) {
			myEquipment = equipment1;
		} else {
			myEquipment = equipment2;
		}

		for (Equipment e : myEquipment) {
			if (type == ArmorStatsTypes.ARMAMENT) {
				if (e instanceof Armament) {
					if (mode == ArmorStatsMode.SUM) {
						if (att == ArmorStatsAttributes.SPEED) {
							value += ((Armament) e).SPEED;
						}
						if (att == ArmorStatsAttributes.ARMOR) {
							value += ((Armament) e).ARMOR;
						}
					}
				}
				if (e instanceof Weapon) {
					if (mode == ArmorStatsMode.SUM) {
						if (att == ArmorStatsAttributes.ARMOR) {
							subvalue += ((Weapon) e).DEFENSE;
						}
					}
				}
			}
			if (type == ArmorStatsTypes.WEAPONS) {
				if (e instanceof Weapon) {
					if (mode == ArmorStatsMode.MAX) {
						if (att == ArmorStatsAttributes.SPEED) {
							if ((100 - ((Weapon) e).SPEED) > value) {
								value = ((Weapon) e).SPEED;
							}
						}
					}
					if (mode == ArmorStatsMode.AVERAGE) {
						if (att == ArmorStatsAttributes.ACCURACY) {
							value = value + ((Weapon) e).ACCURACY;
							itemCtr++;
						}
					}
					if (mode == ArmorStatsMode.SUM) {
						if (att == ArmorStatsAttributes.ATTACK) {
							value += (value + ((Weapon) e).ATTACK);
						}
					}
				}
			}
		}

		/* add armament bonus for a full set of armor */
		if (type == ArmorStatsTypes.ARMAMENT) {
			if (mode == ArmorStatsMode.SUM) {
				if (att == ArmorStatsAttributes.ARMOR) {
					value = value * armamentBonus();
				}
			}
		}

		/* calculate average */
		if (itemCtr > 0 && mode == ArmorStatsMode.AVERAGE) {
			value = value / itemCtr;
		}

		value = value + subvalue;

		return value;
	}

	public void showDescription(int mouseX, int mouseY) {

		this.mousePositionX = mouseX;
		this.mousePositionY = mouseY;

		if (mouseX > main_wX && mouseX < main_wX + 20 && mouseY > main_wY
				&& mouseY < main_wY + 20) {
			showDescription = true;
			if (set) {
				if (armor1.get(Armor.MAIN_WEAPON) != null)
					description = armor1.get(Armor.MAIN_WEAPON)
							.getDescription();
			} else {
				if (armor2.get(Armor.MAIN_WEAPON) != null)
					description = armor2.get(Armor.MAIN_WEAPON)
							.getDescription();
			}
		} else if (mouseX > sub_wX && mouseX < sub_wX + 20 && mouseY > sub_wY
				&& mouseY < sub_wY + 20) {
			showDescription = true;
			if (set) {
				if (armor1.get(Armor.SUB_WEAPON) != null)
					description = armor1.get(Armor.SUB_WEAPON).getDescription();
			} else {
				if (armor2.get(Armor.SUB_WEAPON) != null)
					description = armor2.get(Armor.SUB_WEAPON).getDescription();
			}
		} else if (mouseX > chestX && mouseX < chestX + 20 && mouseY > chestY
				&& mouseY < chestY + 20) {
			showDescription = true;
			if (set) {
				if (armor1.get(Armor.CHEST) != null)
					description = armor1.get(Armor.CHEST).getDescription();
			} else {
				if (armor2.get(Armor.CHEST) != null)
					description = armor2.get(Armor.CHEST).getDescription();
			}
		} else if (mouseX > headX && mouseX < headX + 20 && mouseY > headY
				&& mouseY < headY + 20) {
			showDescription = true;
			if (set) {
				if (armor1.get(Armor.HEAD) != null)
					description = armor1.get(Armor.HEAD).getDescription();
			} else {
				if (armor2.get(Armor.HEAD) != null)
					description = armor2.get(Armor.HEAD).getDescription();
			}
		} else if (mouseX > armX && mouseX < armX + 20 && mouseY > armY
				&& mouseY < armY + 20) {
			showDescription = true;
			if (set) {
				if (armor1.get(Armor.ARMS) != null)
					description = armor1.get(Armor.ARMS).getDescription();
			} else {
				if (armor2.get(Armor.ARMS) != null)
					description = armor2.get(Armor.ARMS).getDescription();
			}
		} else if (mouseX > legX && mouseX < legX + 20 && mouseY > legY
				&& mouseY < legY + 20) {
			showDescription = true;
			if (set) {
				if (armor1.get(Armor.LEGS) != null)
					description = armor1.get(Armor.LEGS).getDescription();
			} else {
				if (armor2.get(Armor.LEGS) != null)
					description = armor2.get(Armor.LEGS).getDescription();
			}
		} else if (mouseX > feetX && mouseX < feetX + 20 && mouseY > feetY
				&& mouseY < feetY + 20) {
			showDescription = true;
			if (set) {
				if (armor1.get(Armor.FEET) != null)
					description = armor1.get(Armor.FEET).getDescription();
			} else {
				if (armor2.get(Armor.FEET) != null)
					description = armor2.get(Armor.FEET).getDescription();
			}
		} else if (mouseX > pot1X && mouseX < pot1X + 20 && mouseY > pot1Y
				&& mouseY < pot1Y + 20) {
			showDescription = true;
			if (set) {
				if (potion1_types.get(Potions.POTION1) != null)
					description = potion1_types.get(Potions.POTION1)
							.getDescription();
			} else {
				if (potion2_types.get(Potions.POTION1) != null)
					description = potion2_types.get(Potions.POTION1)
							.getDescription();
			}
		} else if (mouseX > pot2X && mouseX < pot2X + 20 && mouseY > pot2Y
				&& mouseY < pot2Y + 20) {
			showDescription = true;
			if (set) {
				if (potion1_types.get(Potions.POTION2) != null)
					description = potion1_types.get(Potions.POTION2)
							.getDescription();
			} else {
				if (potion2_types.get(Potions.POTION2) != null)
					description = potion2_types.get(Potions.POTION2)
							.getDescription();
			}
		} else if (mouseX > pot3X && mouseX < pot3X + 20 && mouseY > pot3Y
				&& mouseY < pot3Y + 20) {
			showDescription = true;
			if (set) {
				if (potion1_types.get(Potions.POTION3) != null)
					description = potion1_types.get(Potions.POTION3)
							.getDescription();
			} else {
				if (potion2_types.get(Potions.POTION3) != null)
					description = potion2_types.get(Potions.POTION3)
							.getDescription();
			}
		} else {
			showDescription = false;
		}

		if (showDescription && description != null && description != "") {
			this.descriptionWidth = 0;
			this.descriptionHeight = 0;
			
			int length = 0;
			String longest = "";
			String s[] = description.split("\n");
			int height = s.length;
			for (String st : s) {
				if (resources.DEFAULT_FONTS.get("description").getWidth(st) > length) {
					length = resources.DEFAULT_FONTS.get("description").getWidth(st);
				}
			}
			
			this.descriptionWidth = (int) (20 + length);
			this.descriptionHeight = resources.DEFAULT_FONTS.get("description").getLineHeight() * height;
		}
	}

	public void endShowingDescription() {
		showDescription = false;
		this.descriptionWidth = 0;
		this.descriptionHeight = 0;
	}
	
	public static float[] getValues() {
		return values;
	}

}
