using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Drawing.Imaging;
using System.Threading;

namespace Image_Manipulation
{
    /// <summary>
    ///   Implementatio nof IManipulatableBitmap.
    /// </summary>
    public class ManipulatableBitmap : IManipulatableBitmap
    {
        #region IManipulatableBitmap Members

        /// <summary>
        ///   Perform a transformation.  The computation will be done on the calling thread
        /// </summary>
        /// <param name="transform">The transformation to perform</param>
        /// <returns>The result of the transform</returns>
        public Bitmap Transform(ITransformation transform)
        {
            return BeginTransform(transform, null, null).Get();
        }

        /// <summary>
        ///   Begin a transformation.  The operation will be done on a separate thread.
        /// </summary>
        /// <param name="transform">The transformation to perform</param>
        /// <param name="progressListener">A delegate that receives progress updates, optional</param>
        /// <param name="completionListener">A delegate the receives a completion notification, optional</param>
        /// <returns>An IManipulationResult that can be used to access the result of the convolution</returns>
        public IResult BeginTransform(ITransformation transform, ProgressListener progressListener, CompletionListener completionListener)
        {
            Result future = new Result();
            if (progressListener != null)
                future.ProgressUpdate += progressListener;
            if (completionListener != null)
                future.CompletionNotification += completionListener;
            // Start the computation up.
            new Thread(new ThreadStart(() => transform.DoTransform(this, future))).Start();
            return future;
        }

        #endregion

        /// <summary>
        ///   The bitmap "wrapped" by this ManipulatableBitmap.  Modification of this bitmap results in undefined behavior.
        /// </summary>
        public Bitmap InnerBitmap
        {
            get;
            private set;
        }

        /// <summary>
        ///   Creates a ManipulatableBitmap that wraps a copy of bitmap and normalizes using normMethod.
        /// </summary>
        /// <param name="bitmap">The bitmap to manipulate.  This argument is copied prior to use.</param>
        public ManipulatableBitmap(Bitmap bitmap) :
            this(bitmap, true)
        {
        }

        /// <summary>
        ///   Creates a ManipulatableBitmap that wraps bitmap and normalizes using normMethod.
        /// </summary>
        /// <param name="bitmap">The bitmap to manipulate.  This argument might (dependent on makeCopy) be copied prior to use.</param>
        /// <param name="makeCopy">Whether to make a copy of the bitmap.</param>
        public ManipulatableBitmap(Bitmap bitmap, bool makeCopy)
        {
            InnerBitmap = makeCopy ? new Bitmap(bitmap) : bitmap;
        }

        /// <summary>
        ///     Used to synchronized access to grayscaleImage and grayscaleImageData, since those are calculated on a separate thread
        /// </summary>
        internal object grayscaleImageLock = new object();

        /// <summary>
        ///     When this is non-null, it is locked with PixelFormat = Format32bppRgb, and grayscaleImageData is the data.
        ///     This field is lazy initialized by the first ManipulationResult.  Use grayscaleImageLock to synchronize access
        ///     to this field.
        /// </summary>
        internal Bitmap grayscaleImage;

        /// <summary>
        ///     When this is non-null, it is the data of grayscaleImage, and has PixelFormat = Format32bppRgb
        /// </summary>
        internal BitmapData grayscaleImageData;

        /// <summary>
        ///   This is used so that if multiple convolutions start at once, and one of them needs to do the
        ///   grayscale conversion, the others can get progress updates.  The progress values are in the
        ///   range [0,1] with 0 indicating that grayscale conversion has just begun, and 1 indicating that
        ///   grayscale conversion (but not the remaining image processing) has finished.
        /// </summary>
        internal event ProgressListener GrayscaleConvertProgress;

        /// <summary>
        ///   Invokes the GrayscaleConvertProgress event with value progress.
        /// </summary>
        /// <param name="progress">The progress of the grayscale conversion, in the range [0,1]</param>
        internal void UpdateGrayscaleConvertProgress(double progress)
        {
            try
            {
                if (GrayscaleConvertProgress != null)
                    GrayscaleConvertProgress(progress);
            }
            catch (NullReferenceException)
            {
                // Can still happen if we got pre-empted after the if check and before the call
            }
        }
    }
}
