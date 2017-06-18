using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using System.Windows;

namespace UavTelemetryDemo.ViewModel
{
    public class MainViewModel : ViewModelBase
    {
        public RelayCommand UpdateCommand { get; set; }

        public MainViewModel()
        {
            UpdateCommand = new RelayCommand(UpdateCommandAction);
        }

        private void UpdateCommandAction()
        {
            MessageBox.Show("Update Command Executed");
        }
    }
}