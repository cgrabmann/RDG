package elements;
import general.Enums.ImageSize;

import java.awt.Point;
import java.util.UUID;

import org.newdawn.slick.Image;

/**
 * This class represents a Shape. Because the engine doesn't remember any
 * positions and due to the fact, that we want to have uniqe Identifiers (NAME)
 * and the feature to set Shapes visible or not, we need a Wrapper Class, that
 * remembers different states for us.
 */

public class Element {
	
	/* At the beginning, one cannot know if it is player 1 or player 2 -> make variables protected so they can be changed later on */

	/* Here is the Image referenced */
	protected Image image;
	/* This variable saves the position of the Shape in upper left corner in tile numbers */
	protected Point position;
	/* This variable declares if the Object is visible or not */
	protected boolean visible = true;
	/* saves the Name of the element */
	public String NAME = null;
	/* an Element's unique ID (if needed) */
	public String ID = null;
	
	

	/**Constructs an Element. It will be positioned automatically at (0,0).<br>
	 * Element will be visible by default.
	 * 
	 * @param shapeName
	 * @param image
	 * @see Element
	 */
	public Element(String shapeName, Image image) {
		this(shapeName, image, 0, 0);
	}

	/**Constructs an Element passing its position as single x and y coordinates in tile numbers.<br>
	 * Element will be visible by default.
	 * 
	 * @param shapeName
	 * @param image
	 * @param x
	 * @param y
	 * @see Element
	 */
	public Element(String shapeName, Image image, int x, int y) {
		this(shapeName, image, new Point(x, y));
	}

	/**Constructs an Element passing its position as a Point in tile numbers.<br>
	 * Element will be visible by default.
	 * 
	 * @param shapeName
	 * @param image
	 * @param position
	 * @see Element
	 */
	public Element(String shapeName, Image image, Point position) {
		this(shapeName, image, position, true);
	}

	/**Constructs an Element passing its position as a Point and its visibility.
	 * 
	 * @param shapeName
	 * @param image
	 * @param position
	 * @param visible
	 * @see Element
	 */
	public Element(String shapeName, Image image, Point position,
			boolean visible) {
		this.NAME = shapeName;
		this.ID = shapeName + UUID.randomUUID();
		this.image = image.copy();
		this.position = position;
		this.visible = visible;
	}
	
	/**A copy constructor used for manipulating a copy of Element before transferring it via the network.
	 * @param copy
	 */
	public Element(Element copy) {
		this.NAME = copy.NAME;
		this.ID = copy.ID;
		this.image = copy.image.copy();
		this.position = copy.position;
		this.visible = copy.visible;
	}

	/**Changes visibility of an Element.
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return Visibility state of an Element
	 */
	public boolean isVisible() {
		return this.visible;
	}

	/**Sets the position of an Element passing its position as single x and y coordinates in tile numbers.<br>
	 * 
	 * @param positionX
	 * @param positionY
	 */
	public void setPosition(int positionX, int positionY) {
		this.position = new Point(positionX, positionY);
	}

	/**Sets the position of an Element passing its position as a Point in tile numbers.
	 * 
	 * @param position
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * @return Position of an Element in tile numbers
	 */
	public Point getPosition() {
		return this.position;
	}

	/**
	 * @return a scaled copy of an element's image
	 */
	public Image getImage(ImageSize is) {
		switch(is) {
		case d32x32:
			return this.image.getScaledCopy(32, 32);
		case d20x20:
			return this.image.getScaledCopy(20, 20);
		}
		return null;
	}
	
	/**
	 * @return the element's image
	 */
	public Image getImage() {
		return this.image;
	}

	/**
	 * Deletes an Element.
	 */
	public void delete() {
		// Maybe we have to delete Elements
	}
	
	public String getDescription() {
		return "Not Set";
	}
	
	/**Sets then image of this element. <br>
	 * To be used for replacing image with null when sending element via network.
	 * @param image
	 */
	public void setImage(Image image) {
		this.image = image;
	}

}
