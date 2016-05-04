package ducere.lechal.pod;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.poliveira.apps.parallaxlistview.ParallaxScrollView;

/**
 * Created by VR Naresh on 03-05-2016.
 */
public class Fitness extends Fragment implements View.OnClickListener {
    private View view;
    ImageView imgMenupop;

    public Fitness() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fitness, container, false);
        ParallaxScrollView mScrollView = (ParallaxScrollView) view.findViewById(R.id.view1);
        mScrollView.setParallaxView(getActivity().getLayoutInflater().inflate(R.layout.fitnes_header, mScrollView, false));
        imgMenupop = (ImageView) view.findViewById(R.id.imgMenupop);
        imgMenupop.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgMenupop:
                popMenuDialog();
                break;
        }
    }

    private void popMenuDialog() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(getContext(), imgMenupop);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getContext(), "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();//showing popup menu
    }


}