using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Threading;
using System.IO;

namespace Image_Manipulation
{
	class UI
	{
		static void Main(string[] args)
		{
			Convolution[] convolutions;
			try
			{
				convolutions = GetConvolutions("config.txt").ToArray();
			}
			catch (Exception e)
			{
				Console.WriteLine(e.Message);
				return;
			}

			string[] files;
			if (args.Length == 0)
			{
				Console.WriteLine("Please enter an input image file name: ");
				files = new string[] { Console.ReadLine() };
			}
			else
			{
				files = args;
			}

			foreach (string file in files)
				ProcessFile(Sanitize(file), convolutions);
		}

		// Removes quotes from a string
		private static string Sanitize(string file)
		{
			char[] chars = { '"', '\'' };
			return file.TrimStart(chars).TrimEnd(chars);
		}

		// Different threads outputting status interfere with each other's cursor position if the don't
		// synchronize, so the all take this lock before doing console stuff.
		static object ConsoleLock = new object();

		private static void ProcessFile(string file, Convolution[] transforms)
		{
			Console.WriteLine("Processing " + file);
			try
			{
				Console.WriteLine("Loading...");
				IManipulatableBitmap bitmap = new ManipulatableBitmap(new Bitmap(file));
				Console.WriteLine("Processing...");
				lock (ConsoleLock)
				{
					foreach (Convolution conv in transforms)
						ProcessImage(bitmap, conv, file);
				}
			}
			catch (ArgumentException e)
			{
				Console.WriteLine("Error: " + e.Message);
			}
		}

		/// <summary>
		///   Process bitmap with convolution, assuming the bitmap's input file name was file.
		/// </summary>
		/// <param name="bitmap">The bitmap to process.</param>
		/// <param name="conv">The convolution to perform.</param>
		/// <param name="file">The file the bitmap came from.</param>
		/// <returns></returns>
		private static IResult ProcessImage(IManipulatableBitmap bitmap, Convolution conv, string file)
		{
			int row = Console.CursorTop;
			Console.WriteLine(conv.Name + ":   0.0% complete");
			string outFile = GetOutputName(file, conv.Name);

			// This will do the transform on a separate thread.
			return bitmap.BeginTransform(
				conv,
				x => OutputProgress(x, row, conv.Name.Length + 2),
				r => HandleResult(r, conv.Name, outFile, row));
		}

		/// <summary>
		///   Updates the progress for a computation
		/// </summary>
		/// <param name="progress">A number in [0,1] representing the progress of the computation.</param>
		/// <param name="row">The row at which the percentage string should begin.</param>
		/// <param name="col">The column at which the percentage string should begin.</param>
		private static void OutputProgress(double progress, int row, int col)
		{
			lock (ConsoleLock)
			{
				Console.CursorVisible = false;
				Console.CursorLeft = col;
				Console.CursorTop = row;
				Console.Write("{0,6:0.0%}", progress);
				Console.CursorVisible = true;
			}
		}

		/// <summary>
		///   Handles the result of a computation.
		/// </summary>
		/// <param name="result">The result of the computation.</param>
		/// <param name="name">A description of the computation</param>
		/// <param name="outFile">The output file to save the image in (if necessary).</param>
		/// <param name="row">The row at which at output updated progress.</param>
		private static void HandleResult(IResult result, string name, string outFile, int row)
		{
			try
			{
				result.Get().Save(outFile);
				OutputResult("Finished", row, name.Length + 2);
			}
			catch (ExecutionException e)
			{
				OutputResult(e.InnerException.ToString(), row, name.Length + 2);
			}
			catch (CancellationException)
			{
				OutputResult("Cancelled", row, name.Length + 2);
			}
		}

		/// <summary>
		///   Outputs msg at position (row, col), along with some blank space after it.
		/// </summary>
		/// <param name="msg">The message to output.</param>
		/// <param name="row">The row at which to output.</param>
		/// <param name="col">The column at which to output.</param>
		private static void OutputResult(String msg, int row, int col)
		{
			lock (ConsoleLock)
			{
				Console.CursorVisible = false;
				Console.CursorLeft = col;
				Console.CursorTop = row;
				Console.Write(msg);
				for (int i = 0; i < 20 - msg.Length; i++)
					Console.Write(' ');
				Console.CursorVisible = true;
			}
		}

		private static string GetOutputName(string file, string description)
		{
			return file.Substring(0, file.LastIndexOf('.')) + " - " + description + ".png";
		}

		/// <summary>
		///   Gets all the kernels from a file and wraps them in Convolution objects.
		/// </summary>
		/// <param name="filename">The filename from which to read.</param>
		/// <returns>The convolutions to perform.</returns>
		private static IEnumerable<Convolution> GetConvolutions(string filename)
		{
			using (StreamReader file = File.OpenText(filename))
			{
				string line;
				while (!file.EndOfStream)
				{
					line = file.ReadLine();
					while (line == "")
						line = file.ReadLine();

					// The first blank line is the name, the next lines are matrix data until the next blank line.
					string name = line;

					// Retrieve and parse the lines of the matrix.
					double[][] matrix = (from l in ReadMatrixLines(file)
										 select l.Split(' ').Select(val => Double.Parse(val)).ToArray()).ToArray();

					// double[][] isn't the same as double[,], so do the conversion.

					double[,] matrixSq = new double[matrix.Length, matrix[0].Length];
					for (int i = 0; i < matrix.Length; i++)
					{
						if (matrix[i].Length != matrix[0].Length)
							throw new ArgumentException(String.Format("Config file was incorrectly formatted, kernel {0} was not rectangular", name));
						for (int j = 0; j < matrix[0].Length; j++)
						{
							matrixSq[i, j] = matrix[i][j];
						}
					}
					if (matrixSq.GetLength(0) % 2 != 1 || matrixSq.GetLength(1) % 2 != 1)
						throw new ArgumentException(String.Format("Config file was incorrectly formatted, kernel {0} must have odd dimensions", name));

					yield return new Convolution(Convolution.NormalizationMethod.Scale, new Kernel(name, matrixSq));
				}
			}
		}

		/// <summary>
		///   Reads until a blank line is encountered.  Used to read in a full matrix from a file.
		/// </summary>
		/// <param name="file">The file to read from.</param>
		/// <returns>The lines of the matrix.</returns>
		private static IEnumerable<string> ReadMatrixLines(StreamReader file)
		{
			string line = file.ReadLine();
			while (line != "" && line != null)
			{
				yield return line;
				line = file.ReadLine();
			}
		}
	}
}
