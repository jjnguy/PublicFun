using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Image_Manipulation
{
    /// <summary>
    ///   Encapsulates a kernel and a description of the kernel
    /// </summary>
    public class Kernel
    {
        /// <summary>
        ///   The matrix of this kernel.  Modifying this array results in undefined behavior.
        /// </summary>
        public double[,] Matrix
        {
            get;
            protected set;
        }

        /// <summary>
        ///   A user-friendly name for this kernel.
        /// </summary>
        public string Name
        {
            get;
            protected set;
        }

        /// <summary>
        ///   Creates a kernel.
        /// </summary>
        /// <param name="name">A user-friendly name for this matrix</param>
        /// <param name="matrix">The matrix for this kernel</param>
        /// <exception cref="ArgumentException">If matrix does not have odd dimensions</exception>
        public Kernel(string name, double[,] matrix)
        {
            if (matrix.GetLength(0) % 2 != 1 || matrix.GetLength(1) % 2 != 1)
                throw new ArgumentException("Kernel matrix dimensions must be odd");
            Name = name;
            Matrix = matrix;
        }
    }
}
