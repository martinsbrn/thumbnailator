package net.coobird.thumbnailator.filters;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.coobird.thumbnailator.util.BufferedImages;

/**
 * An {@link ImageFilter} which will apply multiple {@link ImageFilter}s in a
 * specific order.
 * 
 * @author coobird
 *
 */
public final class Pipeline implements ImageFilter
{
	/**
	 * A list of image filters to apply.
	 */
	private final List<ImageFilter> filtersToApply;
	
	private final List<BufferedImageOp> effectsToApply;
	
	/**
	 * An unmodifiable list of image filters to apply.
	 * Used by the {@link #getFilters()} method.
	 * 
	 * This object is created by Collections.unmodifiableList which provides
	 * an unmodifiable view of the original list.
	 * 
	 * Therefore, any changes to the original list will also be "visible" from
	 * this list as well.
	 */
	private final List<ImageFilter> unmodifiableFiltersToApply;
	private final List<BufferedImageOp> unmodifiableEffectsToApply;
	
	/**
	 * Instantiates a new {@link Pipeline} with no image filters to apply.
	 */
	public Pipeline()
	{
		this(Collections.<ImageFilter>emptyList());
	}
	
	/**
	 * Instantiates a new {@link Pipeline} with an array of {@link ImageFilter}s
	 * to apply.
	 * 
	 * @param filters		An array of {@link ImageFilter}s to apply.
	 */
	public Pipeline(ImageFilter... filters)
	{
		this(Arrays.asList(filters));
	}
	
	/**
	 * Instantiates a new {@link Pipeline} with a list of {@link ImageFilter}s
	 * to apply.
	 * 
	 * @param filters		A list of {@link ImageFilter}s to apply.
	 */
	public Pipeline(List<ImageFilter> filters)
	{
		if (filters == null)
		{
			throw new NullPointerException("Cannot instantiate with a null" +
			"list of image filters.");
		}
		
		filtersToApply = new ArrayList<ImageFilter>(filters);
		effectsToApply = new ArrayList<BufferedImageOp>();
		unmodifiableFiltersToApply =
			Collections.unmodifiableList(filtersToApply);
		unmodifiableEffectsToApply =
				Collections.unmodifiableList(effectsToApply);
	}
	
	/**
	 * Adds an {@code ImageFilter} to the pipeline.
	 */
	public void add(ImageFilter filter)
	{
		if (filter == null)
		{
			throw new NullPointerException("An image filter must not be null.");
		}
		
		filtersToApply.add(filter);
	}
	
	/**
	 * Adds an {@code ImageFilter} to the beginning of the pipeline.
	 */
	public void addFirst(ImageFilter filter)
	{
		if (filter == null)
		{
			throw new NullPointerException("An image filter must not be null.");
		}
		
		filtersToApply.add(0, filter);
	}
	
	/**
	 * Adds a {@code List} of {@code ImageFilter}s to the pipeline.
	 * 
	 * @param filters			A list of filters to add to the pipeline.
	 */
	public void addAll(List<ImageFilter> filters)
	{
		if (filters == null)
		{
			throw new NullPointerException("A list of image filters must not be null.");
		}
		
		filtersToApply.addAll(filters);
	}
	
	public void add(BufferedImageOp effect) {
		if (effect == null) {
			throw new NullPointerException("An image effect must not be null.");
		}
		effectsToApply.add(effect);
	}

	public void addFirst(BufferedImageOp effect) {
		if (effect == null) {
			throw new NullPointerException("An image effect must not be null.");
		}
		effectsToApply.add(0, effect);
	}

	/**
	 * Returns a list of {@link ImageFilter}s which will be applied by this
	 * {@link Pipeline}.
	 * 
	 * @return					A list of filters which are applied by this
	 * 							pipeline.
	 */
	public List<ImageFilter> getFilters()
	{
		return unmodifiableFiltersToApply;
	}
	
	public List<BufferedImageOp> getEffects() {
		return unmodifiableEffectsToApply;
	}
	
	public BufferedImage apply(BufferedImage img)
	{
		if (filtersToApply.isEmpty() && effectsToApply.isEmpty())
		{
			return img;
		}
		
		BufferedImage image = BufferedImages.copy(img);
		
		for(BufferedImageOp effect : effectsToApply) {
			image = effect.filter(image, null);
		}
		
		for (ImageFilter filter : filtersToApply)
		{
			image = filter.apply(image);
		}
		
		return image;
	}
}
