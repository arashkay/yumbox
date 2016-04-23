package geekhouse.ir.yumbox.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.norbsoft.typefacehelper.TypefaceHelper;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import geekhouse.ir.yumbox.R;
import geekhouse.ir.yumbox.data.api.models.Customer.orderHistory;
import geekhouse.ir.yumbox.ui.fragments.MainActivityFragment;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView sendDay;
        TextView orderedAt;
        TextView quantity;
        TextView price;
        CircleImageView mainDish;
        CircleImageView leftPic;
        CircleImageView middlePic;
        CircleImageView rightPic;
        FloatingActionButton fab;
        private final Context context;

        PersonViewHolder(View itemView) {
            super(itemView);
            sendDay = (TextView) itemView.findViewById(R.id.send_day);
            orderedAt = (TextView) itemView.findViewById(R.id.ordered_at);
            quantity = (TextView) itemView.findViewById(R.id.number_of_orders);
            price = (TextView) itemView.findViewById(R.id.price);
            mainDish = (CircleImageView) itemView.findViewById(R.id.main_course);
            leftPic = (CircleImageView) itemView.findViewById(R.id.left_little_pic);
            middlePic = (CircleImageView) itemView.findViewById(R.id.middle_little_pic);
            rightPic = (CircleImageView) itemView.findViewById(R.id.right_little_pic);
            fab = (FloatingActionButton) itemView.findViewById(R.id.call_fab);
            context = itemView.getContext();
        }
    }

    List<orderHistory> orders;
    Context context;

    public RVAdapter(List<orderHistory> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    private final int ORDERS = 0;
    private final int BUTTON = 1;

    @Override
    public int getItemViewType(int position) {
        if (position < orders.size()) {
            return ORDERS;
        } else {
            return BUTTON;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        if (i == BUTTON)
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_call_button, viewGroup, false);
        else
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_history, viewGroup, false);
        TypefaceHelper.typeface(v);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        if (i < orders.size()) {
            Picasso.with(context).load(MainActivityFragment.TEST_PIC).into(personViewHolder.mainDish);
            Picasso.with(context).load(MainActivityFragment.TEST_PIC).into(personViewHolder.leftPic);
            Picasso.with(context).load(MainActivityFragment.TEST_PIC).into(personViewHolder.middlePic);
            Picasso.with(context).load(MainActivityFragment.TEST_PIC).into(personViewHolder.rightPic);
            personViewHolder.quantity.setText(orders.get(i).quantity + " پرس ");
            personViewHolder.price.setText(orders.get(i).quantity * orders.get(i).daily_meal.main_dish.price
                    + " تومان ");
            if (orders.get(i).status.equals("pending"))
                personViewHolder.sendDay.setText("در حال ارسال");
            else
                personViewHolder.sendDay.setText("ارسال شده");
            personViewHolder.orderedAt.setText(orderedAt(i));
        } else {
            personViewHolder.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dial = new Intent();
                    dial.setAction("android.intent.action.DIAL");
                    dial.setData(Uri.parse("tel:09361551123"));
                    context.startActivity(dial);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orders.size() + 1;
    }

    private String orderedAt(int i) {
        String at = orders.get(i).at;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        at = (at.substring(0, Math.min(at.length(), 16)));
        String now = df.format(getNowAU());

        int minutesNow = Integer.parseInt(now.valueOf(now.charAt(14)) + now.valueOf(now.charAt(15)));
        int hoursNow = Integer.parseInt(now.valueOf(now.charAt(11)) + now.valueOf(now.charAt(12)));
        int daysNow = Integer.parseInt(now.valueOf(now.charAt(8)) + now.valueOf(now.charAt(9)));
        int monthsNow = Integer.parseInt(now.valueOf(now.charAt(5)) + now.valueOf(now.charAt(6)));
        int yearsNow = Integer.parseInt(now.valueOf(now.charAt(2)) + now.valueOf(now.charAt(3)));

        int minutesAt = Integer.parseInt(at.valueOf(at.charAt(14)) + at.valueOf(at.charAt(15)));
        int hoursAt = Integer.parseInt(at.valueOf(at.charAt(11)) + at.valueOf(at.charAt(12)));
        int daysAt = Integer.parseInt(at.valueOf(at.charAt(8)) + at.valueOf(at.charAt(9)));
        int monthsAt = Integer.parseInt(at.valueOf(at.charAt(5)) + at.valueOf(at.charAt(6)));
        int yearsAt = Integer.parseInt(at.valueOf(at.charAt(2)) + at.valueOf(at.charAt(3)));

        if (yearsNow - yearsAt != 0)
            return (yearsNow - yearsAt) + " سال پیش " ;
        if (monthsNow - monthsAt != 0)
            return (monthsNow - monthsAt) +" ماه پیش " ;
        if (daysNow - daysAt != 0)
            return (daysNow - daysAt) + " روز پیش ";
        if (hoursNow - hoursAt != 0 && hoursNow - hoursAt > 0)
            return (hoursNow - hoursAt) + " ساعت پیش ";
        else {
            if (minutesNow - minutesAt == 0)
                return "1 دقیقه پیش";
            else
                return (minutesNow - minutesAt) + " دقیقه پیش ";
        }
    }

    private Date getNowAU() {
        Calendar calendar = new GregorianCalendar();
        TimeZone timeZone = TimeZone.getTimeZone("Iran/Tehran");
        calendar.setTimeZone(timeZone);

        return calendar.getTime();
    }
}