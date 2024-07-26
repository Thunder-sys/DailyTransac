package com.example.dailytransac.khush

import android.os.Bundle
import java.util.ArrayList
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailytransac.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class  Budgetscreen : AppCompatActivity() {
    lateinit var linechart:LineChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budgetscreen)
        linechart = findViewById(R.id.Linechart)

        fun dataValues1(): ArrayList<Entry> {
            val dataVals = ArrayList<Entry>()
            dataVals.add(Entry(0f, 20f))
            dataVals.add(Entry(1f, 24f))
            dataVals.add(Entry(2f, 28f))
            dataVals.add(Entry(3f, 10f))
            dataVals.add(Entry(4f, 20f))
            dataVals.add(Entry(5f, 16f))
            dataVals.add(Entry(6f, 19f))
            dataVals.add(Entry(7f, 9f))
            dataVals.add(Entry(8f, 25f))
            return dataVals
        }

        var lineDataSet:LineDataSet = LineDataSet(dataValues1(),"Data Set 1")

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(lineDataSet)

        val data = LineData(dataSets)
        linechart.data = data
        linechart.invalidate()
    }
}