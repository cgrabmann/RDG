package views;

import java.awt.Dimension;
import java.awt.Point;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import elements.Creature;
import elements.Element;
import elements.Monster;
import fighting.Fight;
import gameEssentials.Game;
import gameEssentials.Player;
import general.Enums.ImageSize;
import general.MonsterFactory;

/**
 * GameEnvironment extends a View in the GameEnvironment context.
 * 
 * @see View
 */
public class GameEnvironment extends View {

	/* Defines a Block Size in pixels! */
	public static final int BLOCK_SIZE = 32;

	/* Reference to the player, for which the GameEnvironment is shown */
	private Player player;
	
	/*
	 * Reference to the Shapes, which are shown in the GameEnvironment. Scope
	 * Elements are passed from Map Class.
	 */
	private Element[][] backgroundScope;
	private Element[][] overlayScope;
	
	/* ArmorView is needed to interact with armory items */
	ArmorView armorView = null;
	
	/* instance of a Fight - to be loaded into fight thread */
	private Fight fightInstance = null;
	
	/* the base Game class */
	Game game;

	/**
	 * Constructs a GameEnvironment passing its origin as single x and y
	 * coordinates in tile numbers.<br>
	 * Dimension will be set to default values in pixels.<br>
	 * <br>
	 * GameEnvironment extends View.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @param player
	 * @throws SlickException
	 * @see GameEnvironment
	 */
	public GameEnvironment(String contextName, int originX, int originY,
			Player player, ArmorView armorView, Game game) throws SlickException {
		this(contextName, new Point(originX, originY), player, armorView, game);
	}

	/**
	 * Constructs a GameEnvironment passing its origin as a Point in tile
	 * numbers.<br>
	 * Dimension will be set to default values in pixels.<br>
	 * <br>
	 * GameEnvironment extends View.
	 * 
	 * @param contextName
	 * @param origin
	 * @param player
	 * @throws SlickException
	 * @see GameEnvironment
	 */
	public GameEnvironment(String contextName, Point origin, Player player, ArmorView armorView, Game game)
			throws SlickException {
		this(contextName, origin, new Dimension(640, 480), player, armorView, game);
	}

	/**
	 * Constructs a GameEnvironment passing its origin in tiles and dimension in
	 * pixels as single x and y coordinates.<br>
	 * <br>
	 * GameEnvironment extends View.
	 * 
	 * @param contextName
	 * @param originX
	 * @param originY
	 * @param width
	 * @param height
	 * @param player
	 * @throws SlickException
	 * @see GameEnvironment
	 */
	public GameEnvironment(String contextName, int originX, int originY,
			int width, int height, Player player, ArmorView armorView, Game game) throws SlickException {
		this(contextName, new Point(originX, originY), new Dimension(width,
				height), player, armorView, game);
	}

	/**
	 * Constructs a GameEnvironment passing its origin as point in tile numbers
	 * and its dimension in pixels as Dimension.<br>
	 * <br>
	 * GameEnvironment extends View.
	 * 
	 * @param contextName
	 * @param origin
	 * @param size
	 * @param player
	 * @throws SlickException
	 * @see GameEnvironment
	 */
	public GameEnvironment(String contextName, Point origin, Dimension size,
			Player player, ArmorView armorView, Game game) throws SlickException {
		super(contextName, origin, size);
		
		this.game = game;
		this.player = player;
				
		if (size.width % BLOCK_SIZE != 0) {
			System.out.println("WATCH OUT! GameEnvironment only works well, "
					+ "if the width is a multiple of " + BLOCK_SIZE + "!");
		}

		if (size.height % BLOCK_SIZE != 0) {
			System.out.println("WATCH OUT! GameEnvironment only works well, "
					+ "if the height is a multiple of " + BLOCK_SIZE + "!");
		}

		/* Sets the last point down right in tile numbers (not pixels!) */
		downright.x = (int) size.width / BLOCK_SIZE;
		downright.y = (int) size.height / BLOCK_SIZE;

		this.armorView = armorView;
		this.fightInstance = new Fight(this.origin, this.size, this, player, armorView);
		
		update();
	}

	@Override
	public void draw(GameContainer container, Graphics graphics) {
		
		/* if the player is not in a current fight */
		if (!fightInstance.isInFight()) {
			
			/* downright is refered to in tile numbers */
			for (int i = 0; i < downright.x; i++) {
				for (int j = 0; j < downright.y; j++) {
					if ((1 + i) * BLOCK_SIZE <= size.width
							&& (1 + j) * BLOCK_SIZE <= size.height) {
						/*
						 * draw all image in GameEnvironment as 32x32, no matter
						 * if they are 32x32 or 64x64 sized
						 */
						if (backgroundScope[i][j] != null) {
							graphics.drawImage(
									backgroundScope[i][j].getImage(
											ImageSize.d32x32).getScaledCopy(32,
											32), origin.x * BLOCK_SIZE + i
											* BLOCK_SIZE, origin.y * BLOCK_SIZE
											+ j * BLOCK_SIZE);
						}
						if (overlayScope[i][j] != null) {
							graphics.drawImage(
									overlayScope[i][j].getImage(
											ImageSize.d32x32).getScaledCopy(32,
											32), origin.x * BLOCK_SIZE + i
											* BLOCK_SIZE, origin.y * BLOCK_SIZE
											+ j * BLOCK_SIZE);
						}
					} else {
						System.out.println("Image would extend the scope "
								+ "of the GameEnvironment!");
						System.out.println("Something wrong is happening - "
								+ "Fix that!");
					}
				}
			}

			player.draw(container, graphics);
			
		}else {	// show Fight
			fightInstance.draw(container, graphics);
		}
	}

	@Override
	public void update() {
		//map.update();	Warum???
		backgroundScope = map.getBackgroundScope();
		overlayScope = map.getOverlayScope();
	}
	
	
	/**
	 * Starts a Fight with the set creature as enemy.
	 * 
	 * @param creature - the enemy
	 * @throws SlickException 
	 * @throws InterruptedException 
	 */
	public void startFight(Creature enemy) throws SlickException {
		
		/* load fight instance into new thread for fight to be carried out */
		if (game.getFightThread() == null) {
			Thread thread = new Thread(fightInstance);
			game.setFightThread(thread);
		}
		
		/* start the fight and set the enemy */
		game.getFightThread().start();
		fightInstance.setEnemy(enemy);
	}
	
	/**Called by fight thread to signal that it has ended.
	 * @param looser
	 */
	public void fightEnds(Creature looser) {
		game.fightEnds(looser);
	}
	
	/**Returns the currently active Fight Instance or null if no fight is active.
	 * (thread that needs to be started more than once).
	 * @return fight Instance
	 */
	public Fight getFightInstance() {
		return fightInstance;
	}
}