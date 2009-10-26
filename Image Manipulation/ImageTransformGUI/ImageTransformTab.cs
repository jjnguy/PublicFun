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
	/// A tab that is responsible for all of hte transformations of a single image.
	/// 
	/// Contains sub tabs for each transformation, and one for the original image.
	/// </summary>
	public class ImageTransformTab : TabItem
	{
		static ImageTransformTab()
		{
			DefaultStyleKeyProperty.OverrideMetadata(typeof(ImageTransformTab), new FrameworkPropertyMetadata(typeof(TabItem)));
		}

		private TabControl transformations;

		private ManipulatableBitmap image;

		/// <summary>
		/// Constructs a tab responsible for the given Image.
		/// </summary>
		/// <param name="image">The image to be transformed</param>
		/// <param name="fileName">The filename of the image, used simple for naming the tab.</param>
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

		/// <summary>
		/// This method first checks to see if a tab has already been aded with the given transform.  If one does exist, is selects that.
		/// 
		/// Otherwise it creates a new tab with the image and then calls TransformTab on it to begin the transform.
		/// </summary>
		/// <param name="c">The transform to invoke</param>
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
