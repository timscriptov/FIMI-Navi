package com.fimi.app.x8s.map.view.google;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.map.view.AbsMapCustomMarkerView;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.widget.X8MapPointMarkerViewGroup;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;


public class GglMapCustomMarkerView extends AbsMapCustomMarkerView {
    public static Bitmap loadBitmapFromView(View view, Context context) {
        if (view == null) {
            return null;
        }
        int width = View.MeasureSpec.makeMeasureSpec(0, 0);
        int height = View.MeasureSpec.makeMeasureSpec(0, 0);
        view.measure(width, height);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static int dipToPx(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dpValue * scale) + 0.5f);
    }

    public BitmapDescriptor createCustomMarkerViewForP2P(Context context, int res, float heightVale, int npos) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_custom_mark_view_for_point, null);
        TextView heightView = view.findViewById(R.id.point_heightValue);
        String s = X8NumberUtil.getDistanceNumberString(heightVale, 0, true);
        heightView.setText(s);
        ImageView imageView = view.findViewById(R.id.markerIcon);
        if (res != 0) {
            imageView.setBackgroundResource(res);
        }
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createCustomMarkerView(Context context, int res, float heightVale, int npos) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_custom_mark_view, null);
        TextView heightView = view.findViewById(R.id.point_heightValue);
        TextView tvPos = view.findViewById(R.id.tv_pos);
        tvPos.setText("" + npos);
        String s = X8NumberUtil.getDistanceNumberString(heightVale, 0, true);
        heightView.setText(s);
        ImageView imageView = view.findViewById(R.id.markerIcon);
        if (res != 0) {
            imageView.setBackgroundResource(res);
        }
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    @Override
    public Object createCustomMarkerView(Context context, int res, float heightVale) {
        return null;
    }

    @Override
    public BitmapDescriptor createCustomMarkerView2(Context context, int res, int nPos) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_custom_mark_view2, null);
        TextView heightView = view.findViewById(R.id.point_heightValue);
        heightView.setText("" + nPos);
        ImageView imageView = view.findViewById(R.id.markerIcon);
        if (res != 0) {
            imageView.setBackgroundResource(res);
        }
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    @Override
    public BitmapDescriptor createCustomMarkerView(Context context, int res) {
        return BitmapDescriptorFactory.fromResource(res);
    }

    @Override
    public BitmapDescriptor createInreterstMarkerView0(Context context, int res) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_custom_mark_view3, null);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    @Override
    public BitmapDescriptor createCurrentPointView(Context context, int res, int actionRes, int nPos) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_custom_mark_view4, null);
        ImageView imageView = view.findViewById(R.id.img_point);
        if (res != 0) {
            imageView.setBackgroundResource(res);
        }
        ImageView imageView2 = view.findViewById(R.id.img_action);
        if (res != 0) {
            imageView2.setBackgroundResource(actionRes);
        }
        TextView heightView = view.findViewById(R.id.tv_index);
        heightView.setText("" + nPos);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createMapPointNoAngleNoPioView(Context context, int res, float heightVale, int npos, boolean isSelect, boolean isRelation) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_marker_no_pio_view, null);
        X8MapPointMarkerViewGroup imageView = view.findViewById(R.id.myview);
        imageView.setValueNoPio(res, heightVale, npos, 0.0f, isSelect, isRelation);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createMapPioView(Context context, int res, float heightVale, int npos, boolean isSelect, boolean isRelation) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_marker_with_pio_view, null);
        X8MapPointMarkerViewGroup imageView = view.findViewById(R.id.myview);
        imageView.setPioValue(res, heightVale, npos, isSelect, isRelation);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createMapPointWithPioView(Context context, int res, float heightVale, int npos, int poi, float angle, boolean isSelect, boolean isRelation) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_marker_with_pio_view, null);
        X8MapPointMarkerViewGroup imageView = view.findViewById(R.id.myview);
        imageView.setValueWithPio(res, heightVale, npos, poi, angle, isSelect, isRelation);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createMapPointAngleNoPioView(Context context, int res, float heightVale, int npos, float angle, boolean isSelect, boolean isRelation) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_marker_no_pio_view, null);
        X8MapPointMarkerViewGroup imageView = view.findViewById(R.id.myview);
        imageView.setValueNoPio(res, heightVale, npos, angle, isSelect, isRelation);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createPointEventNoPioView(Context context, int actionRes, int angleRes, float heightVale, int npos, float angle, boolean isSelect, boolean isRelation) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_marker_event_no_pio_view, null);
        X8MapPointMarkerViewGroup imageView = view.findViewById(R.id.myview);
        imageView.setPointEventNoPioValue(actionRes, angleRes, heightVale, npos, angle, isSelect, isRelation);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createPointEventWithPioView(Context context, int actionRes, int angleRes, float heightVale, int npos, float angle, boolean isSelect, boolean isRelation) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_marker_event_with_pio_view, null);
        X8MapPointMarkerViewGroup imageView = view.findViewById(R.id.myview);
        imageView.setPointEventValue(actionRes, angleRes, heightVale, npos, angle, isSelect, isRelation);
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }

    public BitmapDescriptor createPointWithSmallArrow(Context context, int res, float angle, boolean isShow) {
        View view = LayoutInflater.from(context).inflate(R.layout.x8_map_marker_arrow_view, null);
        ImageView imageView = view.findViewById(R.id.markerIcon);
        if (res != 0) {
            imageView.setBackgroundResource(res);
            imageView.setRotation(angle);
        }
        if (!isShow) {
            imageView.setVisibility(View.INVISIBLE);
        }
        return BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view, context));
    }
}
