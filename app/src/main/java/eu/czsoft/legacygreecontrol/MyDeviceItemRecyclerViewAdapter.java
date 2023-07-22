package eu.czsoft.legacygreecontrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DeviceItem} and makes a call to the
 * specified {@link DeviceItemFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDeviceItemRecyclerViewAdapter extends RecyclerView.Adapter<MyDeviceItemRecyclerViewAdapter.ViewHolder> {

    private final List<DeviceItem> values;
    private final DeviceItemFragment.OnListFragmentInteractionListener listener;

    public MyDeviceItemRecyclerViewAdapter(List<DeviceItem> items, DeviceItemFragment.OnListFragmentInteractionListener listener) {
        values = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_deviceitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = values.get(position);
        holder.mNameView.setText(holder.mItem.name);
        holder.mTemperatureView.setText(String.format("%d °C", holder.mItem.temperature));

        switch (holder.mItem.mode)
        {
            case AUTO:
                holder.mModeView.setText(R.string.mode_auto);
                break;

            case COOL:
                holder.mModeView.setText(R.string.mode_cool);
                break;

            case DRY:
                holder.mModeView.setText(R.string.mode_dry);
                break;

            case HEAT:
                holder.mModeView.setText(R.string.mode_heat);
                break;

            case FAN:
                holder.mModeView.setText(R.string.mode_fan);
                break;
        }

        switch (holder.mItem.roomType)
        {
            case BEDROOM:
                holder.mIconView.setImageResource(R.mipmap.ic_bedroom);
                break;

            case LIVING_ROOM:
                holder.mIconView.setImageResource(R.mipmap.ic_livingroom);
                break;

            case KITCHEN:
                holder.mIconView.setImageResource(R.mipmap.ic_kitchen);
                break;

            case DINING_ROOM:
                holder.mIconView.setImageResource(R.mipmap.ic_diningroom);
                break;

            case BATHROOM:
                holder.mIconView.setImageResource(R.mipmap.ic_bathroom);
                break;

            case OFFICE:
                holder.mIconView.setImageResource(R.mipmap.ic_office);
                break;

            default:
                holder.mIconView.setImageResource(R.mipmap.ic_air_conditioner);
                break;
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final ImageView mIconView;
        public final TextView mModeView;
        public final TextView mTemperatureView;
        public DeviceItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.nameTextView);
            mIconView = view.findViewById(R.id.icon);
            mModeView = view.findViewById(R.id.modeTextView);
            mTemperatureView = view.findViewById(R.id.temperatureTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
