using System;
using System.Drawing;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Interop;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using Microsoft.Win32;
using Image_Manipulation;
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
        private System.Windows.Controls.Image display;

        static ImageTransformTab()
        {
            DefaultStyleKeyProperty.OverrideMetadata(typeof(ImageTransformTab), new FrameworkPropertyMetadata(typeof(TabItem)));
        }

        public ImageTransformTab(IManipulatableBitmap image)
            : base()
        {
            this.originalImage = (ManipulatableBitmap)image;
            displayImage = Imaging.CreateBitmapSourceFromHBitmap(originalImage.InnerBitmap.GetHbitmap(),
                IntPtr.Zero, Int32Rect.Empty, System.Windows.Media.Imaging.BitmapSizeOptions.FromEmptyOptions());
            Header = "Header";
            display = new System.Windows.Controls.Image();
            display.Source = displayImage;
            display.Stretch = Stretch.None;
            Content = display;
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
            SaveToPng(display, saveDia.FileName);
        }

        #region code coppied from internet: http://msdnrss.thecoderblogs.com/2009/10/12/saving-images-bmp-png-etc-in-wpfsilverlight/
        void SaveToBmp(FrameworkElement visual, string fileName)
        {
            var encoder = new BmpBitmapEncoder();
            SaveUsingEncoder(fileName, encoder);
        }

        void SaveToPng(FrameworkElement visual, string fileName)
        {
            var encoder = new PngBitmapEncoder();
            SaveUsingEncoder(fileName, encoder);
        }

        void SaveUsingEncoder(string fileName, BitmapEncoder encoder)
        {
            RenderTargetBitmap bitmap = new RenderTargetBitmap(
                (int)display.ActualWidth,
                (int)display.ActualHeight,
                96,
                96,
                PixelFormats.Pbgra32);
            bitmap.Render(display);
            BitmapFrame frame = BitmapFrame.Create(bitmap);
            encoder.Frames.Add(frame);

            using (var stream = File.Create(fileName))
            {
                encoder.Save(stream);
            }
        }
        #endregion

        public void TransformTab(ITransformation t)
        {
            Bitmap bitmap = originalImage.Transform(t);
            BitmapSource bSrc = Imaging.CreateBitmapSourceFromHBitmap(
                    bitmap.GetHbitmap(),
                    IntPtr.Zero,
                    Int32Rect.Empty,
                    System.Windows.Media.Imaging.BitmapSizeOptions.FromEmptyOptions());
            displayImage = bSrc;
            display.Source = bSrc;
        }
    }
}
