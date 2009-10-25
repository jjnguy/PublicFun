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
		private ManipulatableBitmap originalImage;
		private BitmapSource displayImage;
		private Bitmap saveImage;

		private StackPanel thePane;
		private System.Windows.Controls.Image display;
		private ProgressBar progress;

		static ImageTransformTab()
		{
			DefaultStyleKeyProperty.OverrideMetadata(typeof(ImageTransformTab), new FrameworkPropertyMetadata(typeof(TabItem)));
		}

		public ImageTransformTab(IManipulatableBitmap image, string fileName)
			: base()
		{
			thePane = new StackPanel();

			this.originalImage = (ManipulatableBitmap)image;
			displayImage = Imaging.CreateBitmapSourceFromHBitmap(originalImage.InnerBitmap.GetHbitmap(),
				IntPtr.Zero, Int32Rect.Empty, System.Windows.Media.Imaging.BitmapSizeOptions.FromEmptyOptions());
			Header = Path.GetFileName(fileName);
			display = new System.Windows.Controls.Image();
			display.Source = displayImage;
			display.Stretch = Stretch.None;
			progress = new ProgressBar();
			progress.Visibility = Visibility.Collapsed;
			progress.Height = 50;
			progress.Maximum = 100;
			progress.Minimum = 0;

			thePane.Children.Add(progress);
			thePane.Children.Add(display);
			Content = thePane;
			ContextMenu picMenu = new ContextMenu();
			MenuItem item = new MenuItem();
			item.Click += Save_Clicked;
			item.Name = "Save";
			item.Header = "Save";
			picMenu.Items.Add(item);
			display.ContextMenu = picMenu;
		}

		private void Save_Clicked(object sender, RoutedEventArgs s)
		{
			SaveFileDialog saveDia = new SaveFileDialog();
			bool? result = saveDia.ShowDialog();
			if (result != true)
				return;
			saveImage.Save(saveDia.FileName);
		}

		public void TransformTab(ITransformation t)
		{
			progress.Visibility = Visibility.Visible;
			originalImage.BeginTransform(t, ProgressCallback, DoneCallback);
		}

		private void ProgressCallback(double progress)
		{
			#region Code found on internet: http://thispointer.spaces.live.com/blog/cns!74930F9313F0A720!252.entry?_c11_blogpart_blogpart=blogview&_c=blogpart#permalink
			this.Dispatcher.Invoke(DispatcherPriority.Normal, (Action)(() =>
			{
				this.progress.Value = progress * 100; //this is my code
			}));
			#endregion
		}

		private void DoneCallback(IResult result)
		{
			saveImage = result.Get();
			#region Code found on internet: http://thispointer.spaces.live.com/blog/cns!74930F9313F0A720!252.entry?_c11_blogpart_blogpart=blogview&_c=blogpart#permalink
			this.Dispatcher.Invoke(DispatcherPriority.Normal, (Action)(() =>
			{
				BitmapSource bSrc = Imaging.CreateBitmapSourceFromHBitmap(
						saveImage.GetHbitmap(),
						IntPtr.Zero,
						Int32Rect.Empty,
						System.Windows.Media.Imaging.BitmapSizeOptions.FromEmptyOptions());
				displayImage = bSrc;

				display.Source = bSrc; //This is my code
				progress.Visibility = Visibility.Collapsed;
			}));
			#endregion
		}
	}
}
