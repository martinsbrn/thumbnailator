package net.coobird.thumbnailator.filters.textwriters;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

public class GradientTextWriter extends AbstractTextWriter {
	
	public void write(Graphics2D g, String text, Color color, Color secondaryColor, int x, int y) {
		FontMetrics fm = g.getFontMetrics();
		GradientPaint gp = new GradientPaint(0, 0, color, fm.stringWidth(text), fm.getHeight(), secondaryColor);
		g.setPaint(gp);
		doWrite(g, text, x, y);
	}

}