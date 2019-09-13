package net.coobird.thumbnailator.filters;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.geometry.Position;

/**
 * This class applies a watermark to an image.
 * 
 * @author coobird
 *
 */
public class Watermark implements ImageFilter
{
	/**
	 * The position of the watermark.
	 */
	private final Position position;
	
	/**
	 * The watermark image.
	 */
	private final BufferedImage watermarkImg;
	
	/**
	 * The opacity of the watermark.
	 */
	private final float opacity, backgroundOpacity;
	
	private final int insetLeft, insetRight, insetTop, insetBottom;
	
	private final Color backgroundColor;
	

	/**
	 * Instantiates a filter which applies a watermark to an image.
	 * 
	 * @param position			The position of the watermark.
	 * @param watermarkImg		The watermark image.
	 * @param opacity			The opacity of the watermark.
	 * 							<p>
	 * 							The value should be between {@code 0.0f} and
	 * 							{@code 1.0f}, where {@code 0.0f} is completely
	 * 							transparent, and {@code 1.0f} is completely
	 * 							opaque.
	 */
	public Watermark(Position position, BufferedImage watermarkImg,
			float opacity)
	{
		this(position, watermarkImg, opacity, null, 0, 0, 0, 0, 0);
	}
	
	public Watermark(Position position, BufferedImage watermarkImg, float opacity, Color backgroundColor, float backgroundOpacity) {
		this(position, watermarkImg, opacity, backgroundColor, backgroundOpacity, 0, 0, 0, 0);
	}
	
	public Watermark(Position position, BufferedImage watermarkImg, float opacity, int insetLeft, int insetRight, int insetTop, int insetBottom) {
		this(position, watermarkImg, opacity, null, 0, insetLeft, insetRight, insetTop, insetBottom);
	}
	
	public Watermark(Position position, BufferedImage watermarkImg, float opacity, Color backgroundColor, float backgroundOpacity, int insetLeft, int insetRight, int insetTop, int insetBottom)
	{
		if (position == null)
		{
			throw new NullPointerException("Position is null.");
		}
		if (watermarkImg == null)
		{
			throw new NullPointerException("Watermark image is null.");
		}
		if (opacity > 1.0f || opacity < 0.0f)
		{
			throw new IllegalArgumentException("Opacity is out of range of " +
					"between 0.0f and 1.0f.");
		}
		
		this.position = position;
		this.watermarkImg = watermarkImg;
		this.opacity = opacity;
		this.backgroundColor = backgroundColor;
		this.backgroundOpacity = backgroundOpacity;
		this.insetLeft = insetLeft;
		this.insetRight = insetRight;
		this.insetTop = insetTop;
		this.insetBottom = insetBottom;
	}

	public BufferedImage apply(BufferedImage img)
	{
		int width = img.getWidth();
		int height = img.getHeight();
		int type = img.getType();
		
		// workaround for BufferedImages created by jhlabs filters
		if(type == 0) {
			type = BufferedImage.TYPE_INT_ARGB;
		}

		BufferedImage imgWithWatermark =
			new BufferedImageBuilder(width, height, type).build();
		
		int watermarkWidth = watermarkImg.getWidth();
		int watermarkHeight = watermarkImg.getHeight();

		Point p = position.calculate(
				width, height, watermarkWidth, watermarkHeight,
				insetLeft, insetRight, insetTop, insetBottom
		);

		Graphics2D g = imgWithWatermark.createGraphics();
		
		// Draw the actual image.
		g.drawImage(img, 0, 0, null);
		
		// Draw a background with transparency
		if (backgroundColor != null) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, backgroundOpacity));
			g.setColor(backgroundColor);
			g.fillRect(p.x, p.y, watermarkWidth, watermarkHeight);
		}
		
		// Draw the watermark on top.
		g.setComposite(
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity)
		);
		
		g.drawImage(watermarkImg, p.x, p.y, null);
		
		g.dispose();

		return imgWithWatermark;
	}
}
