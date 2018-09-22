package valery.pankov.aamsk_exercise2;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageAdapter extends PagerAdapter {

    Context context;


    public int[] Images = new int[]{
            R.drawable.portrait,
            R.drawable.witch_car,
            R.drawable.stewart_1969_england,
            R.drawable.tyrrellsilverstone,
            R.drawable.pic1,
            R.drawable.pic2
    };

    ImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Images.length;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(Images[position]);
        container.addView(imageView, 0);
        return imageView;
    }





    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

}
