using System;
using System.Drawing;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Interop;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Threading;
using Image_Manipulation;
using Microsoft.Win32;
using System.IO;

namespace ImageTransformGUI
{
	/// <summary>
	/// Follow steps 1a or 1b and then 2 to use this custom control in a XAML file.
	///
	/// Step 1a) Using this custom control in a XAML file that exists in the current project.
	/// Add this XmlNamespace attribute to the root element of the markup file where it is 
	/// to be used:
	///
	///     xmlns:MyNamespace="clr-namespace:ImageTransformGUI"
	///
	///
	/// Step 1b) Using this custom control in a XAML file that exists in a different project.
	/// Add this XmlNamespace attribute to the root element of the markup file where it is 
	/// to be used:
	///
	///     xmlns:MyNamespace="clr-namespace:ImageTransformGUI;assembly=ImageTransformGUI"
	///
	/// You will also need to add a project reference from the project where the XAML file lives
	/// to this project and Rebuild to avoid compilation errors:
	///
	///     Right click on the target project in the Solution Explorer and
	///     "Add Reference"->"Projects"->[Browse to and select this project]
	///
	///
	/// Step 2)
	/// Go ahead and use your control in the XAML file.
	///
	///     <MyNamespace:ImageTransformTab/>
	///
	/// </summary>
	public class ImageTransformTab : TabItem
	{
		static ImageTransformTab()
		{
			DefaultStyleKeyProperty.OverrideMetadata(typeof(ImageTransformTab), new FrameworkPropertyMetadata(typeof(TabItem)));
		}

		private TabControl transformations;

		private ManipulatableBitmap image;

		public ImageTransformTab(IManipulatableBitmap image, string fileName)
			: base()
		{
			this.image = (ManipulatableBitmap) image;
			transformations = new TabControl();
			transformations.TabStripPlacement = Dock.Left;
			Header = Path.GetFileName(fileName);
			this.Content = transformations;
			// Add a tab that is un-transformed
			SingleTransformTab newTab = new SingleTransformTab(this.image, null);
			transformations.Items.Add(newTab);
			transformations.SelectedItem = newTab;
		}

		internal void TransformTab(Convolution c)
		{
			// if the tab pane already contains the transformation, we will just show that
			foreach (Control tabC in transformations.Items)
			{
				SingleTransformTab tab = (SingleTransformTab)tabC;
				if (tab.Header.Equals(c.Name))
				{
					transformations.SelectedItem = tab;
					return;
				}
			}

			// Otherwise we just add new tab
			SingleTransformTab newTab = new SingleTransformTab(image, c);
			newTab.TransformTab();
			transformations.Items.Add(newTab);
			transformations.SelectedItem = newTab;
		}
	}
}
