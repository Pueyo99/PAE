package com.example.covidsafespaces;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Results extends AppCompatActivity {

    private CheckBox w_open, w_hafopen, w_closed,mask;
    private String username;
    private int capacity, ventilationCapacity, people;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Bundle extras = getIntent().getExtras();
        capacity = extras.getInt("capacity");
        ventilationCapacity = extras.getInt("ventilationCapacity");
        people = extras.containsKey("people")?extras.getInt("people"):0;
        username = extras.getString("username");

        BarChart results = findViewById(R.id.Results);
        w_open=findViewById(R.id.w_open_check);
        w_hafopen=findViewById(R.id.w_halfopen_check);
        w_closed=findViewById(R.id.w_closed_check);
        mask=findViewById(R.id.mask_check);
        w_open.setChecked(true);
        mask.setChecked(true);

        ArrayList<BarEntry> capacidades = new ArrayList<>();
        capacidades.add(new BarEntry(0, people));
        capacidades.add(new BarEntry(1, capacity));
        capacidades.add(new BarEntry(2, ventilationCapacity));

        BarDataSet barDataSet = new BarDataSet(capacidades, "People, Capacity, Ventilation");
        //Porque no cambia el colo
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(android.R.color.black);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);
        //El texto del eje X
        ArrayList<String> labelsNames = new ArrayList<>();
        labelsNames.add("People");
        labelsNames.add("Capacity");
        labelsNames.add("Ventilation");

        results.setFitBars(true);
        results.setData(barData);
        results.getDescription().setText(" ");
        results.animateY(2000);
        Legend legend = results.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setForm(Legend.LegendForm.CIRCLE);
        //Eje X
        XAxis xAxis = results.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsNames));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelsNames.size());
        xAxis.setLabelRotationAngle(0);

        //Eje y
        YAxis yAxis = results.getAxisLeft();
        YAxis yAxis1 = results.getAxisRight();
        yAxis1.setStartAtZero(true);
        yAxis.setStartAtZero(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.results_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                Intent i = new Intent(Results.this, MainActivity.class);
                i.putExtra("username",username);
                startActivity(i);
                break;
        }

        return true;
    }
}