package bluetooth;

public interface IBlutoothInfoScreen {

    void connectionFailure(String message);
    void bluetoothConnected();
    void bluetoothDisconnected();
    void connecting();

}
