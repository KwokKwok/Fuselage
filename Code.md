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
