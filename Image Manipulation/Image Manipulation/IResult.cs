using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Drawing.Imaging;

namespace Image_Manipulation
{
    /// <summary>
    ///   A delegate that receives progress updates.
    /// </summary>
    /// <param name="progress">A value in the range [0,1] that represents how the current progress of the computation.</param>
    public delegate void ProgressListener(double progress);

    /// <summary>
    ///   A delegate that receives a completion notification.
    /// </summary>
    /// <param name="result">The result of the computation.</param>
    public delegate void CompletionListener(IResult result);

    /// <summary>
    ///   Encapsulates the results of an operation that will complete asynchronously.
    /// </summary>
    public interface IResult
    {
        /// <summary>
        ///   Occurs when an updated progress value is available.
        /// </summary>
        event ProgressListener ProgressUpdate;

        /// <summary>
        ///   Occurs when the computation is complete.
        /// </summary>
        event CompletionListener CompletionNotification;

        /// <summary>
        ///   The current progress of this manipulation, in the range [0,1].
        /// </summary>
        double Progress
        {
            get;
            set;
        }

        /// <summary>
        ///   Gets the result of the computation, blocking if necessary to wait for the computation to complete.
        /// </summary>
        /// <returns>The result of the computation.</returns>
        /// <exception cref="CancellationException">If the computation is cancelled.</exception>
        Bitmap Get();

        /// <summary>
        ///   Cancels the computation.
        /// </summary>
        void Cancel();

        /// <summary>
        ///   Indicates whether the computation is complete and the result is available.  If this is true, then calling Get() will not block.
        /// </summary>
        bool IsComplete
        {
            get;
        }
    }

    /// <summary>
    ///   Thrown by IResult if the result of a cancelled computation is requested.
    /// </summary>
    public class CancellationException : Exception
    {
    }

    /// <summary>
    ///   Thrown by IResult if the result of a computation that threw an exception is requested. 
    /// </summary>
    public class ExecutionException : Exception
    {
        /// <summary>
        ///   Create a new ExecutionException.
        /// </summary>
        /// <param name="e">The exception that this exception "wraps".</param>
        public ExecutionException(Exception e)
            : base("", e)
        {
        }
    }
}
