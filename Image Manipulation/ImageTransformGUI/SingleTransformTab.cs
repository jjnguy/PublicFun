using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Image_Manipulation;
using System.Windows.Threading;
using System.Windows.Interop;
using System.Windows.Controls;
using System.Windows.Media.Imaging;
using System.Windows.Media;
using Microsoft.Win32;

namespace ImageTransformGUI
{
	/// <summary>
	/// This tab controll contains one transformed Image.  When it is created it will display the original image until TransFormTab() is called.
	/// 
	/// It will then display a progress bar and perform the transformation in a background thread.  When the computation is complete, 
	/// the new image will be displayed, and the progressbar will disappear.
	/// </summary>
	public class SingleTransformTab : TabItem
	{
		/// <summary>
		/// Dunno exactly what this does besides teh fact that the second thing had to have 'TabItem' or else the tabs would not display.
		/// </summary>
		static SingleTransformTab()
		{
			DefaultStyleKeyProperty.OverrideMetadata(typeof(SingleTransformTab), new FrameworkPropertyMetadata(typeof(TabItem)));
		}

		private ManipulatableBitmap originalImage;
		private System.Drawing.Bitmap saveImage;
		private ITransformation convol;

		private ScrollViewer scroll;
		private ProgressBar theProgressBar;
		private StackPanel thePane;
		private System.Windows.Controls.Image theImage;

		/// <summary>
		/// Creates a new tab with the image to be transformed and the transform to perform on it.
		/// </summary>
		/// <param name="pic">The Image to transform</param>
		/// <param name="transform">The transform to perform</param>
		public SingleTransformTab(IManipulatableBitmap pic, ITransformation transform)
			: base()
		{
			this.originalImage = (ManipulatableBitmap) pic;
			this.convol = transform;
			thePane = new StackPanel();
			theProgressBar = new ProgressBar();
			theProgressBar.Minimum = 0;
			theProgressBar.Maximum = 100;
			theProgressBar.Height = 25;
			theProgressBar.Visibility = Visibility.Collapsed;
			saveImage = originalImage.InnerBitmap;
			theImage = new Image();
			theImage.Source = Imaging.CreateBitmapSourceFromHBitmap(originalImage.InnerBitmap.GetHbitmap(),
				IntPtr.Zero, Int32Rect.Empty, System.Windows.Media.Imaging.BitmapSizeOptions.FromEmptyOptions());

			theImage.Stretch = Stretch.None;
			if (transform == null)
				Header = "Original Image";
			else
				Header = ((Convolution)transform).Name;
			ContextMenu picMenu = new ContextMenu();
			MenuItem item = new MenuItem();
			item.Click += Save_Clicked;
			item.Name = "Save";
			item.Header = "Save";
			picMenu.Items.Add(item);
			theImage.ContextMenu = picMenu;
			scroll = new ScrollViewer();
			scroll.Content = theImage;
			thePane.Children.Add(theProgressBar);
			thePane.Children.Add(scroll);
			scroll.VerticalScrollBarVisibility = ScrollBarVisibility.Auto;
			scroll.HorizontalScrollBarVisibility = ScrollBarVisibility.Auto;
			
			this.Content = thePane;
		}

		/// <summary>
		/// Performs the transformation on the image in the tab.  The task is run on a different thread.
		/// </summary>
		public void TransformTab()
		{
			if (this.convol != null)
			{
				theProgressBar.Visibility = Visibility.Visible;
				originalImage.BeginTransform(convol, ProgressCallback, DoneCallback);
			}
		}

		/// <summary>
		/// The code that is called when the progress of a Transform is updated.
		/// </summary>
		/// <param name="progress">The progress percentage of the Transformation</param>
		private void ProgressCallback(double progress)
		{
			#region Code found on internet: http://thispointer.spaces.live.com/blog/cns!74930F9313F0A720!252.entry?_c11_blogpart_blogpart=blogview&_c=blogpart#permalink
			this.Dispatcher.Invoke(DispatcherPriority.Normal, (Action)(() =>
			{
				this.theProgressBar.Value = progress * 100; //this is my code
			}));
			#endregion
		}

		/// <summary>
		/// The code to be executed when an image is finished transforming.
		/// </summary>
		/// <param name="result">The result of the transformation.  Contains the resulting bitmap.</param>
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
				theImage.Source = bSrc;
				theProgressBar.Visibility = Visibility.Collapsed;
			}));
			#endregion
		}

		/// <summary>
		/// Method called when save is choosen from an images context menu.
		/// 
		/// Pops up a save dialog.
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="s"></param>
		private void Save_Clicked(object sender, RoutedEventArgs s)
		{
			SaveFileDialog saveDia = new SaveFileDialog();
			bool? result = saveDia.ShowDialog();
			if (result != true)
				return;
			saveImage.Save(saveDia.FileName);
		}
	}
}
