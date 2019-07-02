package net.coobird.thumbnailator.util;

import java.awt.Dimension;
import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.ResizerFactory;

/**
 * Solução paliativa para o scaleUp.
 * 
 * Shrinker.resize(() -> Thumbnails.of("brad.jpg"), x -> x.size(1000,1000), x ->
 * x.toFile("b1"), true);
 *
 */
public final class Shrinker {

	private static Factory shrinkFactory = new Factory();
	private static DefaultResizerFactory drf = (DefaultResizerFactory) DefaultResizerFactory.getInstance();

	/**
	 * render a source image to a size that will never be larger than the source
	 * 
	 * @param          <TT> the builder type
	 * @param producer the config portion of the builder chain
	 * @param sizer    the sizing portion of the builder chain, eg size(100,100)
	 * @param saver    the termination of the builder chain, eg toFile
	 * @param always   if the source is smaller than the sizer size, scale by 1.0
	 *                 and save anyway
	 * @throws IOException
	 */
	public static <TT> void resize(Supplier<Builder<TT>> producer, Function<Builder<TT>, Builder<TT>> sizer, Saver<Builder<TT>> saver, boolean always) throws IOException {
		try {
			saver.exec(sizer.apply(producer.get()).resizerFactory(shrinkFactory));
		} catch (BiggerException ex) {
			if (always) {
				saver.exec(producer.get().scale(1.0));
			}
		}
	}

	private static class Factory implements ResizerFactory {
		public Resizer getResizer() {
			return null;
		}

		public Resizer getResizer(Dimension src, Dimension dst) {
			if (src.height < dst.height | src.width < dst.width) {
				throw new BiggerException();
			}
			return drf.getResizer(src, dst);
		}
	}

	public interface Saver<TT> {
		void exec(TT obj) throws IOException;
	}

	private static class BiggerException extends RuntimeException {
		private static final long serialVersionUID = -126432064818440429L;
	}

}