package net.coobird.thumbnailator.filters.textwriters;

import java.awt.Color;
import java.awt.Graphics2D;

public class TextShadowWriter extends AbstractTextWriter {

	public void write(Graphics2D g, String text, Color color, Color secondaryColor, int x, int y) {
		doWrite(g, text, secondaryColor == null ? Color.DARK_GRAY : secondaryColor, shiftEast(x, 5), shiftSouth(y, 5));
		doWrite(g, text, color, x, y);
	}

}
