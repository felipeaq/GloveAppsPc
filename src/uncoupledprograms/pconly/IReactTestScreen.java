package uncoupledprograms.pconly;

import uncoupledglovedatathings.MyColors;

import java.util.ArrayList;

public interface IReactTestScreen {
    void showNotRunningMessage();

    void showReadyMessage(String s);
    void changeColor(MyColors myColors);

    void showErrorAlert();

    void showSucessAlert();

    void showResult(int erros, int countShownColors, ArrayList<Long> timeInMileSeconds);

    void showResult(int timeInMileSeconds);
}
