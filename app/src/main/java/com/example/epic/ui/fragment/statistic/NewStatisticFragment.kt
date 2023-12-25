package com.example.epic.ui.fragment.statistic

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.data.adapter.MonthAdapter
import com.example.epic.data.model.month.Month
import com.example.epic.databinding.FragmentNewStatisticBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.ProductViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.getMonth
import com.example.epic.util.getYear
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewStatisticFragment :
    BaseFragment<FragmentNewStatisticBinding>(FragmentNewStatisticBinding::inflate),
    MonthAdapter.ItemListener {

    @Inject
    lateinit var monthAdapter: MonthAdapter

    private val viewModel: ProductViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentMonth = getMonth()
        setUpData(currentMonth)
        setUpToolbar()
        setUpMonth()
    }

    private fun setUpMonth() {
        binding.apply {
            val currentMonth = getMonth()
            val months = listOf(
                Month(1, "Januari"),
                Month(2, "Februari"),
                Month(3, "Maret"),
                Month(4, "April"),
                Month(5, "Mei"),
                Month(6, "Juni"),
                Month(7, "Juli"),
                Month(8, "Agustus"),
                Month(9, "September"),
                Month(10, "Oktober"),
                Month(11, "November"),
                Month(12, "Desember")
            )

            for (month in months) {
                if (month.monthNumber == currentMonth) {
                    month.isSelected = true
                }
            }

            monthAdapter.listener = this@NewStatisticFragment
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

    override fun onClickItem(data: Month) {
        setUpData(data.monthNumber)
    }

    private fun setUpData(monthNumber: Int) {
        val currentYear = getYear()
        viewModel.requestStatisticSeller(monthNumber, currentYear)
        viewModel.readStatisticSeller.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val data = transformApiDataToEntries(response.data)
                    setupLineChart(binding.lineChart, data)
//                    setUpData2(response.data)
                    viewModel.readStatisticSeller.removeObservers(viewLifecycleOwner)
                    binding.lineChart.visibility = View.VISIBLE

                }

                is NetworkResult.Loading -> {
                    showLoading()
                }

                is NetworkResult.Error -> {
                    hideLoading()
                    showErrorMessage(it.message!!)
                }

            }
        }
    }

    private fun transformApiDataToEntries(apiData: List<Int>): List<Entry> {
        return apiData.mapIndexed { index, chartData ->
            Entry((index + 1).toFloat(), chartData.toFloat())
        }
    }

    private fun setupLineChart(lineChart: LineChart, entries: List<Entry>) {
        val dataSet = LineDataSet(entries, "Label Data")
        dataSet.label = "Grafik Penjualan"
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK
        dataSet.color = Color.parseColor("#00A4FF")
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
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(dataSet)

        val lineData = LineData(dataSets)
        lineChart.data = lineData

        // Konfigurasi chart lainnya seperti label, sumbu, dsb.
//        lineChart.xAxis.labelRotationAngle = 45f
        lineChart.xAxis.granularity = 1f


        // Konfigurasi LineChart
        lineChart.data = lineData
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.description.isEnabled = false


        // Konfigurasi sumbu X
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = Color.BLACK
        xAxis.setLabelCount(entries.size, true)

        // Konfigurasi sumbu Y
        val leftYAxis = lineChart.axisLeft
        leftYAxis.textColor = Color.WHITE
        leftYAxis.axisMinimum = 0f
        leftYAxis.labelCount = 4
        leftYAxis.yOffset = 10f

        leftYAxis.setDrawGridLines(false)
        leftYAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        val rightYAxis = lineChart.axisRight
        rightYAxis.setDrawGridLines(false)
        rightYAxis.axisMinimum = 0f
        rightYAxis.labelCount = 4
        rightYAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }
        
//        Setup legend
        val legend = lineChart.legend
        legend.isEnabled = true
        legend.setDrawInside(true)
        legend.textColor = Color.BLACK
        legend.textSize = 16f
        legend.typeface = Typeface.create("Poppins", Typeface.NORMAL)
        legend.typeface = Typeface.DEFAULT_BOLD
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP



        // Jika nilai sumbu y berupa integer, gunakan formatter ini
//        lineChart.axisLeft.valueFormatter = object : ValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                return value.toInt().toString()
//            }
//        }

        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.invalidate()
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