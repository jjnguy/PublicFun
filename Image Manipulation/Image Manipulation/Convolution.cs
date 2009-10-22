using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Runtime.InteropServices;
using System.Drawing.Imaging;
using System.Threading;

namespace Image_Manipulation
{
    /// <summary>
    ///   Implementation of ITransformation that does a convolution.
    /// </summary>
    public class Convolution : ITransformation
    {
        #region ITransformation Members

        /// <summary>
        ///   Convolves the bitmap with the kernel supplied in the constructor, putting the results into result.
        /// </summary>
        /// <param name="bitmap">The bitmap to convolve.</param>
        /// <param name="result">The object in which to place the results.</param>
        public void DoTransform(ManipulatableBitmap bitmap, Result result)
        {
            new ConvolutionComputationContext(bitmap, result, this).Execute();
        }

        #endregion

        /// <summary>
        ///   Enumeration of the normalization methods that ManipulatableBitmap supports.
        /// </summary>
        public enum NormalizationMethod
        {
            /// <summary>
            ///   Values outside the valid range of pixel values are clamped into the range.
            /// </summary>
            Clamp,

            /// <summary>
            ///   Values have their absolute value taken, then are divided by the sum of the absolute values of the kernel elements.
            /// </summary>
            Scale,

            /// <summary>
            ///   Positive values are divided by the sum of the positive elements of the kernel.  Negative
            ///   values are divided by the sum of the negative elements of the kernel (this makes them positive).
            /// </summary>
            SignedScale
        }

        /// <summary>
        ///   Delegate that normalizes pixel values.
        /// </summary>
        /// <param name="data">The pixel value to normalize</param>
        /// <returns>The normalized pixel value.  This should be in the range [0, 255]</returns>
        private delegate byte Normalizer(double data);

        private Kernel _Kernel;
        private Normalizer _Normalize;

        /// <summary>
        ///   The name of this convolution.  Value is retrieved from the kernel used to create this convolution.
        /// </summary>
        public string Name
        {
            get
            {
                return _Kernel.Name;
            }
        }

        /// <summary>
        ///   Create a new convolution object.
        /// </summary>
        /// <param name="normMethod">The method to use to normalize pixel values.</param>
        /// <param name="kernel">The kernel to use in the convolution.</param>
        public Convolution(NormalizationMethod normMethod, Kernel kernel)
        {
            _Normalize = MakeNormalizer(normMethod, kernel.Matrix);
            _Kernel = kernel;
        }

        private static Normalizer MakeNormalizer(NormalizationMethod normMethod, double[,] kernel)
        {
            switch (normMethod)
            {
            case NormalizationMethod.Clamp:
                return x => (byte)Math.Max(0, Math.Min(255, Math.Abs(x)));

            case NormalizationMethod.Scale:
                double normFactor = calcPositiveNormFactor(kernel) - calcNegativeNormFactor(kernel);
                return x => (byte)(Math.Abs(x) / normFactor);

            case NormalizationMethod.SignedScale:
                double posNormFactor = calcPositiveNormFactor(kernel);
                double negNormFactor = calcNegativeNormFactor(kernel);
                return x => (byte)(x > 0 ? x / posNormFactor : x / negNormFactor);
            default:
                // This shouldn't ever happen, we'll just use the identity function
                return x => (byte)x;
            }
        }

        private static double calcPositiveNormFactor(double[,] kernel)
        {
            double result = 0;
            foreach (double d in kernel)
                if (d > 0)
                    result += d;
            return result;
        }

        private static double calcNegativeNormFactor(double[,] kernel)
        {
            double result = 0;
            foreach (double d in kernel)
                if (d < 0)
                    result += d;
            return result;
        }

        /// <summary>
        ///   Contains all the information needed for a single image convolution.
        /// </summary>
        class ConvolutionComputationContext
        {
            /// <summary>
            ///   The result to store information in.
            /// </summary>
            private Result _Result;

            /// <summary>
            ///   The bitmap we are convolving.
            /// </summary>
            private ManipulatableBitmap _Bitmap;

            /// <summary>
            ///   The percentage of the overall computation time spent in grayscale conversion.
            /// </summary>
            private double _GrayscaleConvertFraction = 0;

            /// <summary>
            ///   The Convolution object that created this ConvolutionComputationContext.
            /// </summary>
            private Convolution _Outer;

            /// <summary>
            ///   The thread that is doing the convolution.
            /// </summary>
            private Thread _CompThread;

            /// <summary>
            ///   Create a new ConvolutionComputationContext that convolves Bitmap using the information
            ///   from the Convolution outer, storing the result in result.
            /// </summary>
            /// <param name="bitmap">The bitmap to convolve.</param>
            /// <param name="result">Where to store the result.</param>
            /// <param name="outer">The Convolution whose information to use.</param>
            public ConvolutionComputationContext(ManipulatableBitmap bitmap, Result result, Convolution outer)
            {
                _Bitmap = bitmap;
                _Result = result;
                _Outer = outer;
            }

            /// <summary>
            ///   Execute the convolution.
            /// </summary>
            public void Execute()
            {
                _CompThread = Thread.CurrentThread;

                object grayscaleLock = _Bitmap.grayscaleImageLock;
                Bitmap resultBitmap = null;
                BitmapData grayscaleBitmapData;
                BitmapData resultBitmapData = null;

                try
                {
                    // Add a listener in case we're in the middle of a grayscale conversion.
                    _Bitmap.GrayscaleConvertProgress += GrayscaleProgressUpdate;

                    // If someone else is doing the conversion, we'll block here, and when we get in we'll immediately
                    // fail the if check.  If we're cancelled while waiting for the lock, we'll get interrupted and
                    // hit the catch block below.
                    lock (grayscaleLock)
                    {
                        if (_Bitmap.grayscaleImage == null)
                            MakeGrayscaleBitmap();
                        grayscaleBitmapData = _Bitmap.grayscaleImageData;

                        // This is where the result pixels will be stored.
                        resultBitmap = new Bitmap(grayscaleBitmapData.Width, grayscaleBitmapData.Height);
                    }

                    _Bitmap.GrayscaleConvertProgress -= GrayscaleProgressUpdate;

                    // Lock the result bitmaps pixels before we do the convolution
                    Rectangle rect = new Rectangle(Point.Empty, resultBitmap.Size);
                    resultBitmapData = resultBitmap.LockBits(
                        rect,
                        System.Drawing.Imaging.ImageLockMode.ReadWrite,
                        System.Drawing.Imaging.PixelFormat.Format32bppRgb);

                    DoConvolve(grayscaleBitmapData, resultBitmapData);

                    resultBitmap.UnlockBits(resultBitmapData);
                    resultBitmapData = null;

                    _Result.SetResult(resultBitmap);
                }
                catch (ThreadInterruptedException)
                {
                }
                catch (CancellationException)
                {
                    // We got cancelled.  Clean up then return
                    if (resultBitmapData != null)
                        resultBitmap.UnlockBits(resultBitmapData);
                }
            }

            /// <summary>
            ///   First value received from the grayscale conversion progress update.  This is necessary to properly interpolate values.
            /// </summary>
            protected double _FirstGrayscaleProgressValue = 0;

            /// <summary>
            ///   Receives progress updates from the grayscale conversion, translates them into overall progress
            ///   values, and invokes the ProgressUpdate event.
            /// </summary>
            /// <param name="progress">The progress of the grayscale conversion</param>
            protected void GrayscaleProgressUpdate(double progress)
            {
                if (_GrayscaleConvertFraction == 0)
                {
                    // Basically makes the grayscale conversion take a proportional amount of time compared to the actual convolution.
                    _GrayscaleConvertFraction = 2 * (1 - progress) / (_Outer._Kernel.Matrix.Length + 2 * (1 - progress));

                    // Used below to do correct interpolation
                    _FirstGrayscaleProgressValue = progress;
                }
                try
                {
                    _Result.Progress = (progress - _FirstGrayscaleProgressValue) / (1 - _FirstGrayscaleProgressValue) * _GrayscaleConvertFraction;
                }
                catch (CancellationException)
                {
                    // Stop listening for updates, since we don't care anymore.
                    _Bitmap.GrayscaleConvertProgress -= GrayscaleProgressUpdate;

                    // Interrupt the computation thread.  If we're doing the grayscale conversion, we'll finish it
                    // in case someone else is waiting for it, otherwise we'll bail out immediately.
                    _CompThread.Interrupt();
                }
            }

            /// <summary>
            ///   The structure of a pixel value in PixelFormat.Format32bppRgb format.
            /// </summary>
            [StructLayout(LayoutKind.Sequential, Pack=1, Size=4)]
            protected struct PixelStruct
            {
                /// <summary>
                ///   Blue value.
                /// </summary>
                public byte b;

                /// <summary>
                ///   Green value.
                /// </summary>
                public byte g;

                /// <summary>
                ///   Red value.
                /// </summary>
                public byte r;

                /// <summary>
                ///   Unused padding data.
                /// </summary>
                public byte unused;
            }

            /// <summary>
            ///   Does the actual convolution.
            /// </summary>
            /// <param name="dataFrom">The origin bitmap data to convolve.  Should be locked as PixelFormat.Format32bppRgb.</param>
            /// <param name="dataTo">The destination for the convolved bitmap data.  Should be locked as PixelFormat.Format32bppRgb.</param>
            protected unsafe void DoConvolve(BitmapData dataFrom, BitmapData dataTo)
            {
                int matWidth = _Outer._Kernel.Matrix.GetLength(1);
                int matHeight = _Outer._Kernel.Matrix.GetLength(0);
                int xOff = -matWidth / 2;
                int yOff = -matHeight / 2;

                PixelStruct* ptrFrom = (PixelStruct*)dataFrom.Scan0;
                PixelStruct* ptrTo = (PixelStruct*)dataTo.Scan0;

                // For each output pixel...
                for (int y = 0; y < dataFrom.Height; y++)
                {
                    for (int x = 0; x < dataFrom.Width; x++)
                    {
                        // Calculate which parts of the matrix "fit" into the image bounds
                        double result = 0;
                        int iStart = Math.Max(0, -x - xOff);
                        int jStart = Math.Max(0, -y - yOff);
                        int iEnd = Math.Min(matWidth, dataFrom.Width - x - xOff);
                        int jEnd = Math.Min(matHeight, dataFrom.Height - y - yOff);

                        // Iterate over the valid parts of the matrix and add into result
                        for (int i = iStart; i < iEnd; i++)
                        {
                            for (int j = jStart; j < jEnd; j++)
                            {
                                PixelStruct pixel = ptrFrom[(x + i + xOff) +
                                                        (y + j + yOff) * dataFrom.Width];
                                result += _Outer._Kernel.Matrix[j, i] * pixel.r;
                            }
                        }

                        byte resultNormalized = _Outer._Normalize(result);

                        PixelStruct* resultPixel = ptrTo + x +
                                                           y * dataTo.Width;

                        // Make sure to set all the values...
                        resultPixel->r = resultPixel->b = resultPixel->g = resultNormalized;
                    }
                    // Update the progress at the end of every row  This can throw a CancellationException if the
                    // computation got cancelled.  The caller will clean up the locked bitmap.
                    _Result.Progress = _GrayscaleConvertFraction + (1 - _GrayscaleConvertFraction) * y / (double)dataFrom.Height;
                }
                _Result.Progress = 1;
            }

            /// <summary>
            ///   Convert the bitmap to grayscale.
            /// </summary>
            protected void MakeGrayscaleBitmap()
            {
                Bitmap bitmap = new Bitmap(_Bitmap.InnerBitmap);
                Rectangle rect = new Rectangle(Point.Empty, bitmap.Size);
                BitmapData data = bitmap.LockBits(
                    rect,
                    System.Drawing.Imaging.ImageLockMode.ReadWrite,
                    System.Drawing.Imaging.PixelFormat.Format32bppRgb);
                unsafe
                {
                    PixelStruct* ptr = (PixelStruct*)data.Scan0;
                    for (int i = 0; i < data.Width * data.Height; i++)
                    {
                        // The standard formula that everyone uses and no one knows the origin of.
                        byte result = (byte)(ptr[i].r * .3 + ptr[i].g * .59 + ptr[i].b * .11);
                        ptr[i].r = ptr[i].g = ptr[i].b = result;

                        // Update the results once per row.
                        if (i % data.Width == 0)
                            _Bitmap.UpdateGrayscaleConvertProgress(i / (double)(data.Width * data.Height));
                    }
                }

                _Bitmap.grayscaleImage = bitmap;
                // We leave the bitmapdata locked essentially forever so other convolutions can use it concurrently.
                _Bitmap.grayscaleImageData = data;
            }
        }
    }
}
