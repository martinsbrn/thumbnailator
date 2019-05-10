package net.coobird.thumbnailator.filters.textwriters;

import java.awt.Color;
import java.awt.Graphics2D;

public class DefaultTextWriter extends AbstractTextWriter {

	public void write(Graphics2D g, String text, Color color, Color secondaryColor, int x, int y) {
		doWrite(g, text, color, x, y);
	}

}