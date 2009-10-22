using System;
using System.Collections.Generic;
using System.Linq;
using System.Text; 
using System.Drawing;

namespace Image_Manipulation
{
    /// <summary>
    ///   Interface for a bitmap that can be transformed.
    /// </summary>
    public interface IManipulatableBitmap
    {
        /// <summary>
        ///   Perform a transformation.  The computation will be done on the calling thread
        /// </summary>
        /// <param name="transform">The transformation to perform</param>
        /// <returns>The result of the transform</returns>
        Bitmap Transform(ITransformation transform);

        /// <summary>
        ///   Begin a transformation.  The operation will be done on a separate thread.
        /// </summary>
        /// <param name="transform">The transformation to perform</param>
        /// <param name="progressListener">A delegate that receives progress updates, optional</param>
        /// <param name="completionListener">A delegate the receives a completion notification, optional</param>
        /// <returns>An IManipulationResult that can be used to access the result of the convolution</returns>
        IResult BeginTransform(ITransformation transform, ProgressListener progressListener, CompletionListener completionListener);
    }
}
