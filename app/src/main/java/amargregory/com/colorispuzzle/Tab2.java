package amargregory.com.colorispuzzle;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by AMAR on 11/11/2017.
 */

public class Tab2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View myView= inflater.inflate(R.layout.tab2_fragment, container, false);
        return myView;
    }

}
