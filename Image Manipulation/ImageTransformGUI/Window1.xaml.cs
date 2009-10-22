using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Microsoft.Win32;
using Image_Manipulation;
using System.Xml.Linq;
using System.Drawing;

namespace ImageTransformGUI
{
	/// <summary>
	/// Interaction logic for Window1.xaml
	/// </summary>
	public partial class Window1 : Window
	{
		private Convolution[] convols;
		private IManipulatableBitmap image;

		public Window1()
		{
			InitializeComponent();
		}

		private void OpenFile_Click(object sender, RoutedEventArgs e)
		{
			OpenFileDialog f = new OpenFileDialog();
			bool? result = f.ShowDialog();
			if (result != true) return;
			convols = ReadTransformFile(f.FileName).ToArray();
			foreach (var convl in convols)
			{
				listOfConvls.Items.Add(convl.Name);
			}
		}

		private static IEnumerable<Convolution> ReadTransformFile(string loc)
		{
			System.Xml.Linq.XDocument doc = XDocument.Load(loc);
			var data = from Transformations in doc.Descendants("Convolution")
					   select new XElement(Transformations);
			foreach (var convol in data)
			{
				var name = convol.Attribute("Name");
				var rows = from Rows in convol.Descendants("Row")
							 select new XElement(Rows);
				double[,] arr = new double[rows.Count(), rows.Count()];
				int rowNum = 0;
				foreach (var row in rows)
				{
					var Item = from Items in row.Descendants("Item")
							   select new XElement(Items);
					int colNum = 0;
					foreach (var item in Item)
					{
						arr[rowNum, colNum] = Int32.Parse(item.Value);
						colNum++;
					}
					rowNum++;
				}
				yield return new Convolution(Convolution.NormalizationMethod.Clamp, new Kernel(name.Value, arr));
			}
		}

		private void runConvloButton_Click(object sender, RoutedEventArgs e)
		{
			if (convols == null || image == null)
			{
				MessageBox.Show("Make sure you have loaded both at least one Kernel and an image to transform.",
					"Could Not Transform", MessageBoxButton.OK, MessageBoxImage.Error);
				return;
			}

		}

		private void OpenImageFile_Click(object sender, RoutedEventArgs e)
		{
			OpenFileDialog f = new OpenFileDialog();
			bool? result = f.ShowDialog();
			if (result != true) return;
			image = new ManipulatableBitmap(new Bitmap(f.FileName));
		}
	}
}
