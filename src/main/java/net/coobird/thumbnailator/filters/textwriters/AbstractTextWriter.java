package net.coobird.thumbnailator.filters.textwriters;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class AbstractTextWriter implements TextWriter {
	
	private int spacing = 0;

	public int getSpacing() {
		return spacing;
	}
	
	public void setSpacing(int value) {
		this.spacing = value;
	}
	
	protected void doWrite(Graphics2D g, String text, Color color, int x, int y) {
		g.setColor(color);
		g.drawString(text, x, y);
	}

	protected int shiftNorth(int p, int distance) {
		return (p - distance);
	}

	protected int shiftSouth(int p, int distance) {
		return (p + distance);
	}

	protected int shiftEast(int p, int distance) {
		return (p + distance);
	}

	protected int shiftWest(int p, int distance) {
		return (p - distance);
	}
}