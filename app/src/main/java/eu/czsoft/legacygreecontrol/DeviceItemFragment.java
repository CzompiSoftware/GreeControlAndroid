package eu.czsoft.legacygreecontrol;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import eu.czsoft.greesdk.device.Device;
import eu.czsoft.greesdk.device.DeviceManager;
import eu.czsoft.greesdk.device.DeviceManagerEventListener;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DeviceItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int columnCount = 1;
    private OnListFragmentInteractionListener listener;
    private DeviceManagerEventListener deviceManagerEventListener;

    private View view;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeviceItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DeviceItemFragment newInstance(int columnCount) {
        DeviceItemFragment fragment = new DeviceItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deviceitem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            this.view = view;

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            if (columnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
            }

            DeviceManager dm = DeviceManager.getInstance();
            dm.unregisterEventListener(deviceManagerEventListener);

            deviceManagerEventListener = new DeviceManagerEventListener() {
                @Override
                public void onEvent(Event event) {
                if (event == Event.DEVICE_LIST_UPDATED || event == Event.DEVICE_STATUS_UPDATED)
                    updateDeviceList();
                }
            };

            dm.registerEventListener(deviceManagerEventListener);

            updateDeviceList();

            DividerItemDecoration dividerDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    LinearLayoutManager.VERTICAL);
            recyclerView.addItemDecoration(dividerDecoration);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;

        DeviceManager.getInstance().unregisterEventListener(deviceManagerEventListener);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DeviceItem item);
    }

    private void updateDeviceList() {
        if (view == null || !(view instanceof RecyclerView))
            return;

        final List<DeviceItem> items = new ArrayList<>();

        for (Device d : DeviceManager.getInstance().getDevices()) {
            items.add(new DeviceItem(d));
        }

        RecyclerView recyclerView = (RecyclerView) view;

        recyclerView.setAdapter(new MyDeviceItemRecyclerViewAdapter(items, listener));
    }
}
