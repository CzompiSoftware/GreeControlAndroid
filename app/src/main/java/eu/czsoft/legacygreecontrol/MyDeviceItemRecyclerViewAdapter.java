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
        holder.item = values.get(position);
        holder.nameView.setText(holder.item.name);
        holder.temperatureView.setText(String.format("%d °C", holder.item.temperature));

        switch (holder.item.mode)
        {
            case AUTO:
                holder.modeView.setText(R.string.mode_auto);
                break;

            case COOL:
                holder.modeView.setText(R.string.mode_cool);
                break;

            case DRY:
                holder.modeView.setText(R.string.mode_dry);
                break;

            case HEAT:
                holder.modeView.setText(R.string.mode_heat);
                break;

            case FAN:
                holder.modeView.setText(R.string.mode_fan);
                break;
        }

        switch (holder.item.roomType)
        {
            case BEDROOM:
                holder.iconView.setImageResource(R.mipmap.ic_bedroom);
                break;

            case LIVING_ROOM:
                holder.iconView.setImageResource(R.mipmap.ic_livingroom);
                break;

            case KITCHEN:
                holder.iconView.setImageResource(R.mipmap.ic_kitchen);
                break;

            case DINING_ROOM:
                holder.iconView.setImageResource(R.mipmap.ic_diningroom);
                break;

            case BATHROOM:
                holder.iconView.setImageResource(R.mipmap.ic_bathroom);
                break;

            case OFFICE:
                holder.iconView.setImageResource(R.mipmap.ic_office);
                break;

            default:
                holder.iconView.setImageResource(R.mipmap.ic_air_conditioner);
                break;
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onListFragmentInteraction(holder.item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView nameView;
        public final ImageView iconView;
        public final TextView modeView;
        public final TextView temperatureView;
        public DeviceItem item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.nameView = view.findViewById(R.id.nameTextView);
            this.iconView = view.findViewById(R.id.icon);
            this.modeView = view.findViewById(R.id.modeTextView);
            this.temperatureView = view.findViewById(R.id.temperatureTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }
    }
}
