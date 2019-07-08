package net.coobird.thumbnailator.filters.textwriters;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class AbstractTextWriter implements TextWriter {

	private int imageWidth;
	private int imageHeight;

	public int getSpacing() {
		return 0;
	}

	protected void doWrite(Graphics2D g, String text, int x, int y) {
		g.drawString(text, x, y);
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

	public void setImageWidth(int width) {
		this.imageWidth = width;
	}

	public void setImageHeight(int height) {
		this.imageHeight = height;
	}

	protected int getImageWidth() {
		return imageWidth;
	}

	protected int getImageHeight() {
		return imageHeight;
	}

}