package net.coobird.thumbnailator.filters.textwriters;

import java.awt.Color;
import java.awt.Graphics2D;

public class SegmentTextWriter extends AbstractTextWriter {

	public void write(Graphics2D g, String text, Color color, Color secondaryColor, int x, int y) {
		int w = (g.getFontMetrics()).stringWidth(text);
		int h = (g.getFontMetrics()).getHeight();
		int d = (g.getFontMetrics()).getDescent();

		doWrite(g, text, color, x, y);

		g.setColor(secondaryColor);
		for (int i = 0; i < h; i += 3) {
			g.drawLine(x, y + d - i, x + w, y + d - i);
		}
	}

}