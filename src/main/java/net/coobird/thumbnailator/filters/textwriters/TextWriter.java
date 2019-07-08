package net.coobird.thumbnailator.filters.textwriters;

import java.awt.Color;
import java.awt.Graphics2D;

public interface TextWriter {

	int getSpacing();
	
	void setImageWidth(int width);

	void setImageHeight(int height);
	
	void write(Graphics2D g, String text, Color color, Color secondaryColor, int x, int y);

}