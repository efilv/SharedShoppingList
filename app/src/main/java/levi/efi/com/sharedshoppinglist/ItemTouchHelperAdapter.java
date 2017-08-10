package levi.efi.com.sharedshoppinglist;

import android.support.v7.widget.RecyclerView;

/**
 * Created by P0020077 on 23/07/2017.
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void setFocus(RecyclerView.ViewHolder viewHolder);
}
