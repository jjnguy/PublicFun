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
	///     <MyNamespace:SingleTransformTab/>
	///
	/// </summary>
	public class SingleTransformTab : TabItem
	{
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

		public SingleTransformTab(ManipulatableBitmap pic, ITransformation transform)
			: base()
		{
			this.originalImage = pic;
			this.convol = transform;
			thePane = new StackPanel();
			theProgressBar = new ProgressBar();
			theProgressBar.Minimum = 0;
			theProgressBar.Maximum = 100;
			theProgressBar.Height = 25;
			theProgressBar.Visibility = Visibility.Collapsed;
			saveImage = pic.InnerBitmap;
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
			
			thePane.Children.Add(theProgressBar);
			thePane.Children.Add(theImage);
			scroll = new ScrollViewer();
			scroll.HorizontalScrollBarVisibility = ScrollBarVisibility.Auto;
			scroll.Content = thePane;
			// needs to be fixed.  The scroll bar should be moved out of the scroll area.
			this.Content = scroll;
		}

		public void TransformTab()
		{
			if (this.convol != null)
			{
				theProgressBar.Visibility = Visibility.Visible;
				originalImage.BeginTransform(convol, ProgressCallback, DoneCallback);
			}
		}

		private void ProgressCallback(double progress)
		{
			#region Code found on internet: http://thispointer.spaces.live.com/blog/cns!74930F9313F0A720!252.entry?_c11_blogpart_blogpart=blogview&_c=blogpart#permalink
			this.Dispatcher.Invoke(DispatcherPriority.Normal, (Action)(() =>
			{
				this.theProgressBar.Value = progress * 100; //this is my code
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
				theImage.Source = bSrc;
				theProgressBar.Visibility = Visibility.Collapsed;
			}));
			#endregion
		}

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
