package levi.efi.com.sharedshoppinglist;

/**
 * Created by P0020077 on 19/07/2017.
 */

public interface Saveable {

    void SaveData();
    boolean getAddState();
    void setAddState(boolean state);
    void goToNextItem(int currPosition);
}
