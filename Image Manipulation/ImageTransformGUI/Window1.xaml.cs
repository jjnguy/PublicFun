using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Windows;
using System.Xml.Linq;
using Image_Manipulation;
using Microsoft.Win32;

namespace ImageTransformGUI
{
	/// <summary>
	/// Interaction logic for Window1.xaml
	/// </summary>
	public partial class Window1 : Window
	{
		private Convolution[] convols;

		/// <summary>
		/// Default constructor.
		/// 
		/// Most of the heavy lifting is done in the XAML
		/// </summary>
		public Window1()
		{
			InitializeComponent();
		}

		/// <summary>
		/// Code called when a user chooses to open a Transform File
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		private void OpenFile_Click(object sender, RoutedEventArgs e)
		{
			OpenFileDialog f = new OpenFileDialog();
			f.Filter = "XML File|*.xml";
			bool? result = f.ShowDialog();
			if (result != true) return;
			convols = ReadTransformFile(f.FileName).ToArray();
			foreach (var convl in convols)
			{
				listOfConvls.Items.Add(convl.Name);
			}
		}

		/// <summary>
		/// Reads in an XML transform file.
		/// 
		/// Uses LINQ to XML very poorly.
		/// </summary>
		/// <param name="loc">The location of the XML file.</param>
		/// <returns>List of convolutions defined in the file.</returns>
		private static IEnumerable<Convolution> ReadTransformFile(string loc)
		{
			// Prepare yourself for a horriffic use of LINQ to XML
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
				if (name.Value == "Smooth")
					yield return new Convolution(Convolution.NormalizationMethod.SignedScale, new Kernel(name.Value, arr));
				else
					yield return new Convolution(Convolution.NormalizationMethod.Clamp, new Kernel(name.Value, arr));
			}
		}

		/// <summary>
		/// Code ran when a user chooses to run a Transformation on an image.
		/// 
		/// Passes off any heavy lifting to the currently selected tab.
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		private void runConvloButton_Click(object sender, RoutedEventArgs e)
		{
			if (convols == null || tabControl1.Items.Count == 0)
			{
				MessageBox.Show("Make sure you have loaded both at least one Kernel and an image to transform.",
					"Could Not Transform", MessageBoxButton.OK, MessageBoxImage.Error);
				return;
			}
			if (listOfConvls.SelectedIndex == -1)
			{
				MessageBox.Show("Make sure you have selected a transform to apply.",
					"Could Not Transform", MessageBoxButton.OK, MessageBoxImage.Error);
				return;
			}
			ImageTransformTab selectedTab = (ImageTransformTab) tabControl1.SelectedItem;
			foreach(Convolution c in convols)
			{
				if (c.Name == (string)listOfConvls.SelectedItem)
				{
					selectedTab.TransformTab(c);
					return;
				}
			}
		}

		/// <summary>
		/// Code ran when a user chooses to open a new image.
		/// 
		/// Adds a new tab to the interface.
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		private void OpenImageFile_Click(object sender, RoutedEventArgs e)
		{
			OpenFileDialog f = new OpenFileDialog();
			f.Filter = "Image File|*.JPG; *.JPEG; *.BMP; *.PNG; *.GIF";
			bool? result = f.ShowDialog();
			if (result != true) 
                return;
			ImageTransformTab newTab = new ImageTransformTab(new ManipulatableBitmap(new Bitmap(f.FileName)), f.FileName);
			newTab.Name = "tab" + tabControl1.Items.Count;
			tabControl1.Items.Add(newTab);
			tabControl1.SelectedItem = newTab;
		}

		private void Keep_Original_Size_Checked(object sender, RoutedEventArgs e)
		{
			// Not used.
		}
	}
}
