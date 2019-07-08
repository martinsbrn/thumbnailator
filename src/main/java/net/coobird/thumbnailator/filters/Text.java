package net.coobird.thumbnailator.filters;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.filters.textwriters.DefaultTextWriter;
import net.coobird.thumbnailator.filters.textwriters.TextWriter;
import net.coobird.thumbnailator.geometry.Position;

public class Text implements ImageFilter {

	private final String text;

	private final Font font;

	private final Color color, backgroundColor, secondaryColor;

	private final Position position;

	private final float opacity, backgroundOpacity;

	private final int insetLeft, insetRight, insetTop, insetBottom;
	
	private TextWriter textWriter;

	public Text(String text, Font font, Color color, Position position, float opacity) {
		this(text, font, color, position, opacity, null, 0, null, null, 0, 0, 0, 0);
	}

	public Text(String text, Font font, Color color, Position position, float opacity, Color backgroundColor, float backgroundOpacity) {
		this(text, font, color, position, opacity, backgroundColor, backgroundOpacity, null, null, 0, 0, 0, 0);
	}
	
	public Text(String text, Font font, Color color, Position position, float opacity, Color backgroundColor, float backgroundOpacity, int insetLeft, int insetRight, int insetTop, int insetBottom) {
		this(text, font, color, position, opacity, backgroundColor, backgroundOpacity, null, null, insetLeft, insetRight, insetTop, insetBottom);
	}

	public Text(String text, Font font, Color color, Position position, float opacity, Color backgroundColor, float backgroundOpacity, 
			TextWriter textWriter, Color secondaryColor, int insetLeft, int insetRight, int insetTop, int insetBottom) {

		if (text == null || text.length() < 1) {
			throw new NullPointerException("Text is null.");
		}

		if (font == null) {
			throw new NullPointerException("Font is null.");
		}

		if (color == null) {
			throw new NullPointerException("Color is null.");
		}

		if (position == null) {
			throw new NullPointerException("Position is null.");
		}
		if (opacity > 1.0f || opacity < 0.0f) {
			throw new IllegalArgumentException("Opacity is out of range of between 0.0f and 1.0f.");
		}

		if (backgroundOpacity > 1.0f || backgroundOpacity < 0.0f) {
			throw new IllegalArgumentException("Background opacity is out of range of between 0.0f and 1.0f.");
		}

		this.text = text;
		this.font = font;
		this.color = color;
		this.position = position;
		this.opacity = opacity;
		this.backgroundColor = backgroundColor;
		this.backgroundOpacity = backgroundOpacity;
		this.textWriter = textWriter;
		this.secondaryColor = secondaryColor;
		this.insetLeft = insetLeft;
		this.insetRight = insetRight;
		this.insetTop = insetTop;
		this.insetBottom = insetBottom;
	}

	public BufferedImage apply(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		int type = img.getType();
		
		// workaround for BufferedImages created by jhlabs filters
		if(type == 0) {
			type = BufferedImage.TYPE_INT_ARGB;
		}

		BufferedImage imgWithText = new BufferedImageBuilder(width, height, type).build();
		Graphics2D g = imgWithText.createGraphics();

		// Draw the actual image.
		g.drawImage(img, 0, 0, null);

		g.setFont(font);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		FontMetrics fontMetrics = g.getFontMetrics();
		Rectangle2D rect = fontMetrics.getStringBounds(text, g);

		Point p = position.calculate(width, height, (int) rect.getWidth(), (int) rect.getHeight(), insetLeft, insetRight, insetTop, insetBottom);
		
		if(textWriter == null) {
			textWriter = new DefaultTextWriter();
		}

		// Draw a background with transparency
		if (backgroundColor != null) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, backgroundOpacity));
			g.setColor(backgroundColor);
			g.fillRect(p.x - textWriter.getSpacing(), p.y - textWriter.getSpacing(), (int) rect.getWidth(), (int) rect.getHeight());
		}
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		
		textWriter.setImageHeight(height);
		textWriter.setImageWidth(width);
		textWriter.write(g, text, color, secondaryColor, p.x, (p.y + fontMetrics.getAscent()));
		
		g.dispose();

		return imgWithText;
	}
	
}