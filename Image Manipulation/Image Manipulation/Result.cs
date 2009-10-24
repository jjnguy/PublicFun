using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Drawing;
using System.Drawing.Imaging;

namespace Image_Manipulation
{
    /// <summary>
    ///   Implements IResult
    /// </summary>
    public class Result : IResult
    {
        /// <summary>
        ///   Occurs when an updated progress value is available.
        /// </summary>
        public event ProgressListener ProgressUpdate;

        /// <summary>
        ///   Occurs when the computation is complete.
        /// </summary>
        public event CompletionListener CompletionNotification;

        /// <summary>
        ///   The current progress of this manipulation, in the range [0,1].
        /// </summary>
        public double Progress
        {
            // Doubles are 64-bit, so locking is required to get atomic reads and writes (on 32 bit platforms anyway), and
            // they also create memory barriers.
            get
            {
                double temp;
                lock (this)
                {
                    if (_Exception is CancellationException)
                        throw _Exception;
                    temp = _Progress;
                }
                return temp;
            }

            set
            {
                lock (this)
                {
                    // Trying to update the progress of a cancelled computation doesn't make any sense, so throw an exception
                    if (_Exception is CancellationException)
                        throw _Exception;
                    _Progress = value;
					if (ProgressUpdate != null)
						ProgressUpdate(value);
                }
            }
        }

        private Bitmap _Bitmap;
        private bool _Complete;
        private Exception _Exception;
        private double _Progress;

        /// <summary>
        ///   Gets the result of the computation, blocking if necessary to wait for the computation to complete.
        /// </summary>
        /// <returns>The result of the computation.</returns>
        /// <exception cref="CancellationException">If the computation is cancelled.</exception>
        public Bitmap Get()
        {
            lock (this)
            {
                while (!_Complete)
                {
                    Monitor.Wait(this);
                }
                if (_Exception != null)
                {
                    throw _Exception;
                }
                return _Bitmap;
            }
        }

        /// <summary>
        ///   Cancels the computation.
        /// </summary>
        public void Cancel()
        {
            SetCancelled();
        }

        /// <summary>
        ///   Indicates whether the computation is complete.  If this is true, then calling Get() will not
        ///   block.  If the computation is cancelled, IsComplete is true.
        /// </summary>
        public bool IsComplete
        {
            get
            {
                lock (this)
                {
                    return _Complete;
                }
            }
            private set
            {
                lock (this)
                {
                    _Complete = value;
                }
            }
        }

        internal void SetResult(Bitmap bitmap)
        {
            lock (this)
            {
                _Bitmap = bitmap;
                _Complete = true;
                Monitor.PulseAll(this);
				if (CompletionNotification != null)
					CompletionNotification(this);
            }
        }

        internal void SetException(Exception e)
        {
            lock (this)
            {
                this._Exception = new ExecutionException(e);
                _Complete = true;
                Monitor.PulseAll(this);
                CompletionNotification(this);
            }
        }

        internal void SetCancelled()
        {
            lock (this)
            {
                this._Exception = new CancellationException();
                _Complete = true;
                Monitor.PulseAll(this);
                CompletionNotification(this);
            }
        }
    }
}
