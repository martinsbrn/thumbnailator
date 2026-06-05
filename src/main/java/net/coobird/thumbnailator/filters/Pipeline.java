/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2020 Chris Kroells
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
public final class Pipeline implements ImageFilter {
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
	public Pipeline() {
		this(Collections.<ImageFilter>emptyList());
	}

	/**
	 * Instantiates a new {@link Pipeline} with an array of {@link ImageFilter}s
	 * to apply.
	 *
	 * @param filters		An array of {@link ImageFilter}s to apply.
	 */
	public Pipeline(ImageFilter... filters) {
		this(Arrays.asList(filters));
	}

	/**
	 * Instantiates a new {@link Pipeline} with a list of {@link ImageFilter}s
	 * to apply.
	 *
	 * @param filters		A list of {@link ImageFilter}s to apply.
	 */
	public Pipeline(List<ImageFilter> filters) {
		if (filters == null) {
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
	 *
	 * @param filter		An {@code ImageFilter}.
	 */
	public void add(ImageFilter filter) {
		if (filter == null) {
			throw new NullPointerException("An image filter must not be null.");
		}

		filtersToApply.add(filter);
	}

	/**
	 * Adds a {@code BufferedImageOp} effect to the pipeline.
	 * Effects are applied before filters.
	 *
	 * @param effect		A {@code BufferedImageOp} effect.
	 */
	public void add(BufferedImageOp effect) {
		if (effect == null) {
			throw new NullPointerException("An image effect must not be null.");
		}
		effectsToApply.add(effect);
	}

	/**
	 * Adds a {@code BufferedImageOp} effect to the beginning of the effects list.
	 *
	 * @param effect		A {@code BufferedImageOp} effect.
	 */
	public void addFirst(BufferedImageOp effect) {
		if (effect == null) {
			throw new NullPointerException("An image effect must not be null.");
		}
		effectsToApply.add(0, effect);
	}

	/**
	 * Adds an {@code ImageFilter} to the beginning of the pipeline.
	 *
	 * @param filter		An {@code ImageFilter}.
	 */
	public void addFirst(ImageFilter filter) {
		if (filter == null) {
			throw new NullPointerException("An image filter must not be null.");
		}

		filtersToApply.add(0, filter);
	}

	/**
	 * Adds a {@code List} of {@code ImageFilter}s to the pipeline.
	 *
	 * @param filters			A list of filters to add to the pipeline.
	 */
	public void addAll(List<ImageFilter> filters) {
		if (filters == null) {
			throw new NullPointerException("A list of image filters must not be null.");
		}

		filtersToApply.addAll(filters);
	}

	/**
	 * Returns a list of {@link ImageFilter}s which will be applied by this
	 * {@link Pipeline}.
	 *
	 * @return					A list of filters which are applied by this
	 * 							pipeline.
	 */
	public List<ImageFilter> getFilters() {
		return unmodifiableFiltersToApply;
	}

	/**
	 * Returns a list of {@link BufferedImageOp} effects which will be applied
	 * by this {@link Pipeline}.
	 *
	 * @return					A list of effects which are applied by this
	 * 							pipeline.
	 */
	public List<BufferedImageOp> getEffects() {
		return unmodifiableEffectsToApply;
	}

	public BufferedImage apply(BufferedImage img) {
		if (filtersToApply.isEmpty() && effectsToApply.isEmpty()) {
			return img;
		}

		BufferedImage image = BufferedImages.copy(img);

		for (BufferedImageOp effect : effectsToApply) {
			image = effect.filter(image, null);
		}

		for (ImageFilter filter : filtersToApply) {
			image = filter.apply(image);
		}

		return image;
	}
}
