package net.coobird.thumbnailator.filters.textwriters;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class MotionTextWriter extends AbstractTextWriter {
	
	private static final int QUANTITY = 20;

	public void write(Graphics2D g2d, String text, Color color, Color secondaryColor, int x, int y) {
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int width = fontMetrics.stringWidth(text);
		Font font = fontMetrics.getFont();

		String fontFamily = font.getFamily();
		int size = font.getSize();
		int decrement = QUANTITY;
		
		AlphaComposite composite = (AlphaComposite) g2d.getComposite();
		int rule = composite.getRule();
		float alphaMax = composite.getAlpha();
		float alpha = 0.0f;
		float alphaIncrement = 0.1f;
		
		for (int i = 0; i < QUANTITY; i++) {
			
			g2d.setComposite(AlphaComposite.getInstance(rule, getAlpha(alpha, alphaMax)));
			g2d.setFont(new Font(fontFamily, Font.PLAIN, (size - decrement)));
			int w = fontMetrics.stringWidth(text);
			int h = fontMetrics.getHeight();
			
//			int r = color.getRed() - decrement * 10;
//			int g = color.getGreen() - decrement * 10;
//			int b = color.getBlue() - decrement * 10;
			
//			System.out.println("RGB: " + r + ", " + g + ", " + b);
			
			doWrite(g2d, text, color, x + (width - w) / 2, shiftSouth(y - h, 5 * i));
			decrement--;
			alpha = alpha + alphaIncrement;
		}
	}
	
	private float getAlpha(float alpha, float alphaMax) {
		return alpha > alphaMax ? alphaMax : alpha;
	}

}