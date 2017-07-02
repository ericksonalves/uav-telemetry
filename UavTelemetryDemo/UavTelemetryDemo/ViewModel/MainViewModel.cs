using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;

namespace UavTelemetryDemo.ViewModel
{
    public class MainViewModel : ViewModelBase
    {
        private bool sendToLocalhost;
        public bool SendToLocalhost
        {
            get { return sendToLocalhost; }
            set { Set(() => SendToLocalhost, ref sendToLocalhost, value); }
        }

        private string altitude;
        public string Altitude
        {
            get { return altitude; }
            set { Set(() => Altitude, ref altitude, value); }
        }

        private string battery;
        public string Battery
        {
            get { return battery; }
            set { Set(() => Battery, ref battery, value); }
        }

        private string ip;
        public string Ip
        {
            get { return ip; }
            set { Set(() => Ip, ref ip, value); }
        }

        private string latitude;
        public string Latitude
        {
            get { return latitude; }
            set { Set(() => Latitude, ref latitude, value); }
        }

        private string longitude;
        public string Longitude
        {
            get { return longitude; }
            set { Set(() => Longitude, ref longitude, value); }
        }

        private string speed;
        public string Speed
        {
            get { return speed; }
            set { Set(() => Speed, ref speed, value); }
        }

        public RelayCommand UpdateCommand { get; set; }

        public MainViewModel()
        {
            UpdateCommand = new RelayCommand(UpdateCommandAction);
        }

        private void UpdateCommandAction()
        {
        }
    }
}