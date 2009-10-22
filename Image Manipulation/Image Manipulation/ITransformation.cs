using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace Image_Manipulation
{
    /// <summary>
    ///   Represents an abstract transform that can be performed on a bitmap.
    /// </summary>
    public interface ITransformation
    {
        /// <summary>
        ///   Performs the transform on the given bitmap, updating the result object as the computation progresses.
        /// </summary>
        /// <param name="bitmap">The bitmap to transform.</param>
        /// <param name="result">The Result object to hold the results of the transformation.</param>
        void DoTransform(ManipulatableBitmap bitmap, Result result);
    }
}
