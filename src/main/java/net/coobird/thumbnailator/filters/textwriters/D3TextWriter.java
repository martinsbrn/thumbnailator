package net.coobird.thumbnailator.filters.textwriters;

import java.awt.Color;
import java.awt.Graphics2D;

public class D3TextWriter extends AbstractTextWriter {

	public void write(Graphics2D g, String text, Color color, Color secondaryColor, int x, int y) {
		for (int i = 0; i < 5; i++) {
			doWrite(g, text, color, shiftEast(x, i), shiftNorth(shiftSouth(y, i), 1));
			doWrite(g, text, Color.BLACK, shiftWest(shiftEast(x, i), 1), shiftSouth(y, i));
		}
		doWrite(g, text, color, shiftEast(x, 5), shiftSouth(y, 5));
	}

}
