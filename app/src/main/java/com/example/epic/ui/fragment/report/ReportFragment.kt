package com.example.epic.ui.fragment.report

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.data.adapter.ReportAdapter
import com.example.epic.data.model.report.ItemPdf
import com.example.epic.databinding.FragmentReportBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.fragment.report.pdf.AppPermission
import com.example.epic.ui.fragment.report.pdf.FileHandler
import com.example.epic.ui.fragment.report.pdf.PdfService
import com.example.epic.ui.viewModel.ReportViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.getStoreName
import com.example.epic.util.getUserId
import com.example.epic.util.readStore
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class ReportFragment : BaseFragment<FragmentReportBinding>(FragmentReportBinding::inflate) {

    private val viewModel: ReportViewModel by viewModels()

    @Inject
    lateinit var adapter: ReportAdapter

    private val myItem = mutableListOf<ItemPdf>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpData()
        clickDownloadPdf()
        setUpToolbar()
        val store = readStore()
        Toast.makeText(requireContext(), store?.nama_toko ?: "-", Toast.LENGTH_SHORT).show()
    }

    private fun setUpToolbar() {
        binding.apply {
            view?.let {
                configureToolbarBackPress(
                    toolbar.myToolbar,
                    it,
                    requireActivity(),
                    "Laporan Penjualan"
                )
            }
        }
    }

    private fun clickDownloadPdf() {
        binding.btnDownloadPdf.setOnClickListener {
            if (AppPermission.permissionGranted(this)) {
                createPdf(myItem)
            } else {
                AppPermission.requestPermission(this)
            }
        }
    }

    private fun setUpData() {
        viewModel.requestReport(getUserId()?.toInt() ?: savedUser!!.id_user)
        viewModel.readReportResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    for (i in data.indices) {
                        val noItem = data[i]
                        val dataItem = ItemPdf(
                            no = (i + 1).toString(),
                            code = noItem.kode_barang,
                            productName = noItem.nama_barang,
                            sales = noItem.sales.toString(),
                            seller = noItem.penjualan.toString(),
                            productReturn = noItem.pengembalian.toString(),
                            stock = noItem.persediaan.toString()
                        )
                        myItem.add(dataItem)
                    }

                    adapter.also { newAdapter ->
                        newAdapter.differ.submitList(myItem)
//                        val newItem = ItemPdf(
//                            "No",
//                            "Kode",
//                            "Nama Barang",
//                            "Sales",
//                            "Penjualan",
//                            "Pengembalian Barang",
//                            "Persediaan"
//                        )
//                        it.addHeaderItem(newItem)
                        binding.rvReport.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = newAdapter
                        }
                    }
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

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AppPermission.REQUEST_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                AppPermission.requestPermission(this)
                showErrorMessage("Permission should be allowed")
            }
        }
    }

    private fun isFileExists(fileName: String): Boolean {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, fileName)
        return file.exists()
    }

    private fun createPdf(data: MutableList<ItemPdf>) {
        val onError: (Exception) -> Unit = { toastErrorMessage(it.message.toString()) }
        val onFinish: (File) -> Unit = { openFile(it) }
        val pdfService = PdfService()
        // Nama file awal
        val baseFileName = "Data Barang"
        var fileName = baseFileName
        var fileIndex = 1

        // Mengecek apakah file dengan nama yang sama sudah ada
        while (isFileExists(fileName)) {
            fileName = "$baseFileName.pdf"
            fileIndex++
        }

        pdfService.createUserTable(
            title = getStoreName() ?: "-",
            data = data,
            fileName = fileName,
            onFinish = onFinish,
            onError = onError
        )
    }

    private fun openFile(file: File) {
        val path = FileHandler().getPathFromUri(requireContext(), file.toUri())
        val pdfFile = path?.let { File(it) }
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
        val pdfIntent = Intent(Intent.ACTION_VIEW)
        pdfIntent.setDataAndType(pdfFile?.toUri(), "application/pdf")
        pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        try {
            startActivity(pdfIntent)
        } catch (e: ActivityNotFoundException) {
            toastErrorMessage("Can't read pdf file")
        }
    }

    private fun toastErrorMessage(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

}