package net.coobird.thumbnailator.filters.textwriters;

import java.awt.Color;
import java.awt.Graphics2D;

public class TextOverlapWriter extends AbstractTextWriter {

	@Override
	public void setSpacing(int value) {
		super.setSpacing(value == 0 ? 10 : value);
	}

	public void write(Graphics2D g, String text, Color color, Color secondaryColor, int x, int y) {
		doWrite(g, text, secondaryColor, shiftWest(x, getSpacing()), shiftSouth(y, getSpacing()));
		doWrite(g, text, color, x, y);
	}

}