package com.example.ecommerceapp.view.fragment.subPages;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.example.ecommerceapp.R;
import com.example.ecommerceapp.databinding.FragmentPertChartBinding;
import com.example.ecommerceapp.view.fragment.BaseFragment;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;


public class PertChartFragment extends BaseFragment {
    NavController navController;

    List pieData = new ArrayList<>();

    LineChartView lineChartView;
    String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
            "Oct", "Nov", "Dec"};
    int[] yAxisData = {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18};

    private View root;
    private FragmentPertChartBinding binding;


    public PertChartFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_pert_chart, container, false);
        binding = FragmentPertChartBinding.inflate(inflater, container, false);
        root=binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(getActivity(), R.id.home_activity_fragment_normal);
        ArrayList<String> list = new ArrayList<String>();
        boolean x = Arrays.asList(list).contains("yourObject");
        for(int i=0;i<=list.size();i++){
            if (list.get(i).contains("")){

            }
        }
        setUpActivity();
        initPertChart();
        initCupeChart();
    }

    private void initCupeChart() {
        ValueLineSeries valueLineSeries=new ValueLineSeries();
        valueLineSeries.setColor(getActivity().getResources().getColor(R.color.red_E55151));
        valueLineSeries.addPoint(new ValueLinePoint("Mon",190f));
        valueLineSeries.addPoint(new ValueLinePoint("Tus",500f));
        valueLineSeries.addPoint(new ValueLinePoint("Wed",850f));
        valueLineSeries.addPoint(new ValueLinePoint("Thus",1000f));
        valueLineSeries.addPoint(new ValueLinePoint("Fri",700f));
        valueLineSeries.addPoint(new ValueLinePoint("Mon",2.0f));
        valueLineSeries.addPoint(new ValueLinePoint("Mon",2.0f));
        binding.curveChart.addSeries(valueLineSeries);
    }

    private void initPertChart() {
        pieData.add(new SliceValue(30, getActivity().getResources().getColor(R.color.grean_568E4C)).setLabel("94"));
        pieData.add(new SliceValue(20, getActivity().getResources().getColor(R.color.brown_9A6223)).setLabel("234"));
        pieData.add(new SliceValue(20, getActivity().getResources().getColor(R.color.blue_116BAC)).setLabel("301"));
        pieData.add(new SliceValue(10, getActivity().getResources().getColor(R.color.red_E55151)).setLabel("301"));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(10);
        pieChartData.setHasCenterCircle(true).setCenterText1("").setCenterText1FontSize(10).setCenterText1Color(Color.parseColor("#0097A7"));
        binding.chart.setPieChartData(pieChartData);
        binding.chart.setChartRotationEnabled(false);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 190),
                new DataPoint(1, 500),
                new DataPoint(2, 400),
                new DataPoint(3, 850),
                new DataPoint(4, 1000),
                new DataPoint(5, 700),
                new DataPoint(6, 550),
        });

        // after adding data to our line graph series.
        // on below line we are setting
        // title for our graph view.
        binding.idGraphView.setTitle("My Graph View");

        // on below line we are setting
        // text color to our graph view.
        binding.idGraphView.setTitleColor(R.color.purple_200);

        // on below line we are setting
        // our title text size.
        binding.idGraphView.setTitleTextSize(18);

        // on below line we are adding
        // data series to our graph view.
        binding.idGraphView.addSeries(series);


        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();


        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("");
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        binding.chartLine.setLineChartData(data);

        Viewport viewport = new Viewport(binding.chartLine.getMaximumViewport());
        viewport.top = 110;
        binding.chartLine.setMaximumViewport(viewport);
        binding.chartLine.setCurrentViewport(viewport);
    }

    public class MyView extends View {

        public MyView(Context context) {
            super(context);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int x = getWidth();
            int y = getHeight();
            int radias;
            Paint paint = new Paint();

            canvas.drawPaint(paint);
            paint.setColor(Color.parseColor("\n" +
                    "#D9D9D9"));
            canvas.drawArc(50f, 50f, 50f, 50, 50, 5, true, paint);
        }
    }
}