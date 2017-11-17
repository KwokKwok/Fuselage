# WPF 路由

``` XAML
<Window x:Class="Hola.MainWindow"
        xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
        xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml"
        xmlns:d="http://schemas.microsoft.com/expression/blend/2008"
        xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
        xmlns:local="clr-namespace:Hola"
        mc:Ignorable="d"
        Title="MainWindow" Height="350" Width="525"
        local:TimeButton.ReportTime="ReportTimeHandler">
    <Grid x:Name="grid_1" local:TimeButton.ReportTime="ReportTimeHandler">
        <Grid x:Name="grid_2" local:TimeButton.ReportTime="ReportTimeHandler">
            <Grid x:Name="grid_3" local:TimeButton.ReportTime="ReportTimeHandler">
                <StackPanel x:Name="stack_panel_1" local:TimeButton.ReportTime="ReportTimeHandler">
                    <ListBox x:Name="listbox"/>
                    <local:TimeButton x:Name="time_button" Width="80" Height="80" Content="报时" local:TimeButton.ReportTime="ReportTimeHandler"/>
                </StackPanel>
            </Grid>
            <Grid x:Name="grid_4" local:TimeButton.ReportTime="ReportTimeHandler"/>
        </Grid>
        <Grid x:Name="grid_5" local:TimeButton.ReportTime="ReportTimeHandler"/>
    </Grid>
</Window>

```

``` C#
using System;
using System.Windows;
using System.Windows.Controls;

namespace Hola
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        private void ReportTimeHandler(object sender, ReportTimeEventArgs e)
        {
            var element = sender as FrameworkElement;
            string time = e.ClickTime.ToLongTimeString();
            string content = string.Format("{0} 到达 {1}", time, element.Name);
            this.listbox.Items.Add(content);

            if (element == this.grid_3)
            {
                e.Handled = true;
            }
        }
    }

    class ReportTimeEventArgs : RoutedEventArgs
    {
        public ReportTimeEventArgs(RoutedEvent routedEvent,object source) : base(routedEvent, source) { }

        public DateTime ClickTime { get; set; }
    }


   class TimeButton : Button
    {
        public static readonly RoutedEvent ReportTimeEvent = EventManager.RegisterRoutedEvent("ReportTime", RoutingStrategy.Tunnel, typeof(EventHandler<ReportTimeEventArgs>), typeof(TimeButton));

        public event RoutedEventHandler ReportTime
        {
            add { this.AddHandler(ReportTimeEvent, value); }
            remove { this.RemoveHandler(ReportTimeEvent,value); }
        }

        protected override void OnClick()
        {
            base.OnClick();

            var args = new ReportTimeEventArgs(ReportTimeEvent, this);
            args.ClickTime = DateTime.Now;
            this.RaiseEvent(args);
        }
    }
}

```

注意：

1. 路由的方向
1. 事件的消息是沿着Visual Tree传递的，比如UserControl中的一个Button激发了事件，通过e.Souce可以定位到UserControl，而通过e.OriginalSource可以定位到UserControl内部的Button。

## 附加事件

附加事件是为了让非UIElement类的对象的路由事件，其激发必须依赖于一个具有RaiseEvent的对象(UIElement)。其激发事件的路由的第一站是激发它的元素。一般定义在Keyboard，Binding，Mouse类中。平时也不会用到，知道就好。可以通过e.OriginalSource找到激发事件的对象，比如Binding对象。

只有一个按钮。

``` C#
namespace Hola
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            this.grid.AddHandler(Student.NameChangedEvent, new RoutedEventHandler(this.StudentNameChangedHandler));
        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {
            var stu = new Student() { Id = 101, Name = "Tim" };
            stu.Name = "Tom";
            RoutedEventArgs arg = new RoutedEventArgs(Student.NameChangedEvent, stu);
            this.button1.RaiseEvent(arg);
        }

        private void StudentNameChangedHandler(object senter,RoutedEventArgs e)
        {
            MessageBox.Show((e.OriginalSource as Student).Id.ToString());
        }
    }

    public class Student
    {
        public static readonly RoutedEvent NameChangedEvent = EventManager.RegisterRoutedEvent("Name", RoutingStrategy.Bubble, typeof(RoutedEventHandler), typeof(Student));

        public int Id { get; set; }
        public string Name { get; set; }

    }
}

```
