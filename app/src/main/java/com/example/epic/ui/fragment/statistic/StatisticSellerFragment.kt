package com.example.epic.ui.fragment.statistic

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.R
import com.example.epic.data.adapter.MonthAdapter
import com.example.epic.data.model.month.Month
import com.example.epic.databinding.FragmentStatisticSellerBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.ProductViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.getMonth
import com.example.epic.util.getYear
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatisticSellerFragment :
    BaseFragment<FragmentStatisticSellerBinding>(FragmentStatisticSellerBinding::inflate),
    MonthAdapter.ItemListener {

    @Inject
    lateinit var monthAdapter: MonthAdapter

    private val viewModel: ProductViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpData()
        setUpToolbar()
//        setUpLineChart()
        setUpMonth()


    }

    private fun setUpMonth() {
        binding.apply {
            val monthArray = resources.getStringArray(R.array.month)
//            val months = monthArray.map {
//                Month(it, false)
//            }

            val months = ArrayList<Month>()
            val currentMonth = getMonth()
            for (i in monthArray.indices) {
                val isSelected = (i == currentMonth - 1)
                months.add(Month(i + 1, monthArray[i], isSelected))
            }

            monthAdapter.differ.submitList(months)
            rvMonth.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = monthAdapter
            }
        }
    }

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            view?.let {
                configureToolbarBackPress(
                    toolbar.myToolbar,
                    it,
                    requireActivity(),
                    "Grafik Penjualan"
                )
            }
        }
    }

    private fun setUpData2(data: List<Int>) {
        val dataValues = arrayOf(4, 2, 5, 3, 5)
        val lineChart = binding.lineChart
        val entries = ArrayList<Entry>()
        for (i in dataValues.indices) {
            entries.add(Entry(i.toFloat() + 1, dataValues[i].toFloat()))
        }

        // Konfigurasi dataset
        val dataSet = LineDataSet(entries, "Data Set 1")
        dataSet.valueTextColor = Color.RED
        dataSet.color = Color.parseColor("#00A4FF")
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 12f
        dataSet.circleHoleColor = Color.parseColor("#00FF85")
        dataSet.circleRadius = 8f
        dataSet.circleHoleRadius = 4f
        dataSet.setCircleColor(Color.WHITE)
        dataSet.lineWidth = 3f
        dataSet.setDrawValues(false)

        // Membuat gradien
        dataSet.setDrawFilled(true)
        dataSet.fillDrawable = getGradientDrawable()

        // Konfigurasi LineData
        val lineData = LineData(dataSet)

        // Menggunakan mode CUBIC_BEZIER untuk membuat garis cekung
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        // Konfigurasi LineChart
        lineChart.data = lineData
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.description.isEnabled = false

        // Konfigurasi sumbu X
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = Color.BLACK
        xAxis.setLabelCount(dataValues.size, true)

        // Konfigurasi sumbu Y
        val leftYAxis = lineChart.axisLeft
        leftYAxis.textColor = Color.WHITE
        leftYAxis.setDrawGridLines(false)
        val rightYAxis = lineChart.axisRight
        rightYAxis.setDrawGridLines(false)

        // Konfigurasi legenda
        val legend = lineChart.legend
        legend.textColor = Color.WHITE

        // Menambahkan listener untuk menangani klik pada titik data
        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {

            }

            override fun onNothingSelected() {

            }
        })
    }

    private fun setUpData() {
        val currentMonth = getMonth()
        val currentYear = getYear()
        viewModel.requestStatisticSeller(currentMonth, currentYear)
        viewModel.readStatisticSeller.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    setUpData2(response.data)

                }

                is NetworkResult.Loading -> {
                    showLoading()
                }

                is NetworkResult.Error -> {
                    hideLoading()
                    showErrorMessage(it.message!!)
                }

                else -> {}
            }
        }
    }

    override fun onClickItem(data: Month) {

    }

    private fun getGradientDrawable(): Drawable {
        val colors = intArrayOf(
            Color.parseColor("#66C8FE"),
            Color.parseColor("#73CDFF"),
            Color.parseColor("#008AD5FF"),
        )
        return GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
    }

}