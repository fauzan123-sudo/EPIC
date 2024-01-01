package com.example.epic.ui.fragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.epic.R
import com.example.epic.data.adapter.MonthAdapter
import com.example.epic.data.model.home.RequestHome
import com.example.epic.data.model.month.Month
import com.example.epic.data.model.notification.update.RequestUpdateToken
import com.example.epic.data.model.user.management.read.Data
import com.example.epic.databinding.FragmentHomeBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.HomeViewModel
import com.example.epic.ui.viewModel.NotificationViewModel
import com.example.epic.ui.viewModel.ProfileViewModel
import com.example.epic.ui.viewModel.UserManagementViewModel
import com.example.epic.util.Constants.FirebaseToken
import com.example.epic.util.DataStatus
import com.example.epic.util.NetworkResult
import com.example.epic.util.getCurrentDate
import com.example.epic.util.getCurrentDay
import com.example.epic.util.getMonth
import com.example.epic.util.getMonthAndYear
import com.example.epic.util.getStoreName
import com.example.epic.util.getUserId
import com.example.epic.util.getYear
import com.example.epic.util.readLoginResponse
import com.example.epic.util.readStore
import com.example.epic.util.saveStore
import com.example.epic.util.saveStoreName
import com.example.epic.util.saveUserId
import com.example.epic.util.updateStore
import com.example.epic.util.updateStoreName
import com.example.epic.util.updateUserId
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import java.util.concurrent.CompletableFuture
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    MonthAdapter.ItemListener {

    @Inject
    lateinit var monthAdapter: MonthAdapter

    private val viewModel: ProfileViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val userManagementViewModel: UserManagementViewModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()

    private val listIdStore = ArrayList<Int>()
    private val listNameStore = ArrayList<String>()

    private var defaultSpinner: Data? = null
    private var stores: List<Data>? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        loadApi(getMonth())
        loadCalendar()
        pickStore()
        loadStore()
        backPress()
        checkFcm()
        getNameStore()



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

            monthAdapter.differ.submitList(months)
            rvMonth.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = monthAdapter

                monthAdapter.listener = this@HomeFragment
            }
            imgProfile.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            }
        }

    }

    private fun getNameStore() {
        homeViewModel.getAllStore()
        homeViewModel.getAllStore.observe(viewLifecycleOwner) {
            when (it.status) {
                DataStatus.Status.SUCCESS -> {
                    hideLoading()
                    val response = it.data
//                    defaultSpinner = response
                    Log.d("TAG", "getNameStore is : $response and $defaultSpinner")
                }

                DataStatus.Status.LOADING -> {
                    showLoading()
                }

                DataStatus.Status.ERROR -> {
                    hideLoading()
                    showErrorMessage(it.message!!)
                }

            }
        }
    }

    private fun tokenFirebase(): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d(FirebaseToken, "$FirebaseToken $token")
                    Log.d("fcm founded", "")
                    completableFuture.complete(token)
                } else {
                    Log.d("no fcm founded", "")
                    completableFuture.completeExceptionally(task.exception!!)
                }
            }

        return completableFuture
    }

    private fun checkFcm() {
        tokenFirebase().thenApply { token ->
            notificationViewModel.requestCheckFcm(token)
            notificationViewModel.checkFcm.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        hideLoading()
                        val response = it.data!!
                        if (!response.status) {
                            updateToken()
                        }
                    }

                    is NetworkResult.Loading -> {
                        showLoading()
                    }

                    is NetworkResult.Error -> {
                        hideLoading()
                        Log.e("TAG", "error fcm: ${it.message}")
                        showErrorMessage(it.message!!)
                    }
                }
            }
        }.exceptionally { exception ->
            exception.message?.let { Log.e("Token Retrieval Error", it) }
            null
        }
    }

    private fun updateToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                notificationViewModel.requestUpdateFcm(RequestUpdateToken(token))
                notificationViewModel.updateFcmResponse.observe(viewLifecycleOwner) {
                    when (it) {
                        is NetworkResult.Success -> {
                            hideLoading()
                            val response = it.data!!
                            if (response.status) {
                                Log.d("TAG", "updateToken: ${response.message}")
                            } else {
                                Log.e("TAG", "updateToken: ${response.message}")
                            }
                        }

                        is NetworkResult.Loading -> {
                            showLoading()
                        }

                        is NetworkResult.Error -> {
                            hideLoading()
                            Log.e("TAG", "error update fcm: ${it.message}")
                            showErrorMessage(it.message!!)
                        }
                    }
                }
            } else {
                Log.d("no fcm founded", "")
            }
        }

    }

    private fun loadStore() {
        binding.apply {
            if (savedUser?.role == "1") {
                tilStore.visibility = View.VISIBLE
                tvStoreName.visibility = View.GONE

                hIncomingMenu.setOnClickListener {
                    if (getUserId() == null) {
                        showErrorMessage("harap pilih toko dulu")
                    } else {
                        findNavController().navigate(R.id.action_homeFragment_to_listSalesFragment)
                    }
                }

                tvLookAll.setOnClickListener {
                    if (getUserId() == null) {
                        showErrorMessage("harap pilih toko dulu")
                    } else {
                        findNavController().navigate(R.id.action_homeFragment_to_allMenuFragment)
                    }
                }

                imgLookAll.setOnClickListener {
                    if (getUserId() == null) {
                        showErrorMessage("harap pilih toko dulu")
                    } else {
                        findNavController().navigate(R.id.action_homeFragment_to_allMenuFragment)
                    }
                }

                hStockMenu.setOnClickListener {
                    if (getUserId() == null) {
                        showErrorMessage("harap pilih toko dulu")
                    } else {
                        findNavController().navigate(R.id.action_homeFragment_to_listStockFragment)
                    }
                }

                mcSeller.setOnClickListener {
                    if (getUserId() == null) {
                        showErrorMessage("harap pilih toko dulu")
                    } else {
                        findNavController().navigate(R.id.action_homeFragment_to_listSellerFragment)
                    }
                }
            } else {
                tilStore.visibility = View.GONE
                tvStoreName.visibility = View.VISIBLE

                tvStoreName.text = savedUser?.nama_toko

                hIncomingMenu.setOnClickListener {
                    findNavController().navigate(R.id.action_homeFragment_to_listSalesFragment)
                }

                tvLookAll.setOnClickListener {
                    findNavController().navigate(R.id.action_homeFragment_to_allMenuFragment)
                }

                imgLookAll.setOnClickListener {
                    findNavController().navigate(R.id.action_homeFragment_to_allMenuFragment)
                }

                hStockMenu.setOnClickListener {
                    findNavController().navigate(R.id.action_homeFragment_to_listStockFragment)
                }

                mcSeller.setOnClickListener {
                    findNavController().navigate(R.id.action_homeFragment_to_listSellerFragment)
                }
            }
        }
    }

    private fun disableSpinnerCategory() {
        val spinnerCategory = binding.spPickStore
        listIdStore.clear()
        listNameStore.clear()
        spinnerCategory.isEnabled = false
        spinnerCategory.text.clear()
        binding.tilStore.hint = "data kosong"
        spinnerCategory.setAdapter(null)
    }

    private fun pickStore() {
        userManagementViewModel.requestUserList()
        userManagementViewModel.listUserResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val stores = response.data
                    if (stores.isEmpty()) {
                        disableSpinnerCategory()
                    } else {
                        binding.spPickStore.isEnabled = true
                        val spinnerCategory = binding.spPickStore
                        listIdStore.clear()
                        listNameStore.clear()

                        val filteredStores = stores.filter { store ->
                            store.role != "1"
                        }

                        filteredStores.forEach { store ->
                            listIdStore.add(store.id_user)
                            listNameStore.add(store.nama_toko ?: "-")
                        }

                        spinnerCategory.setAdapter(
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                listNameStore
                            )
                        )

                        if (readStore()?.nama_toko == null) {
                            binding.spPickStore.hint = "pilih toko"
                        } else {
                            binding.spPickStore.hint = "${readStore()?.nama_toko}"
                        }


                        var lastSelectedPosition = -1
                        spinnerCategory.setOnItemClickListener { _, _, position, _ ->
                            if (position != lastSelectedPosition) {
                                val selectedStore = filteredStores[position]

                                val getUserId = getUserId()
                                val thisMonth = getMonth()
                                val getNameStore = getStoreName()
                                loadApi(thisMonth)
                                if (getNameStore == null && getUserId == null && readStore() == null) {
                                    saveUserId(selectedStore.id_user.toString())
                                    saveStoreName(selectedStore.nama_toko ?: "-")
                                    saveStore(selectedStore)
                                    homeViewModel.saveStoreName(selectedStore)
                                } else {
                                    updateStore(selectedStore)
                                    updateUserId(selectedStore.id_user.toString())
                                    updateStoreName(selectedStore.nama_toko ?: "-")
                                }
                                lastSelectedPosition = position
                            }
                        }
                    }
                }

                is NetworkResult.Loading -> {
                    showLoading()
                }

                is NetworkResult.Error -> {
                    hideLoading()
                    Log.e("TAG", "error : ${it.message}")
                    showErrorMessage(it.message!!)
                }
            }
        }
    }

    private fun loadCalendar() {
        val currentDay = Date().getCurrentDay()
        val currentDate = Date().getCurrentDate()
        val monthAndYear = Date().getMonthAndYear()
        binding.hDay.text = currentDay
        binding.tvDate.text = currentDate
        binding.hDayYear.text = monthAndYear
    }

    private fun backPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                if (navController.currentDestination?.id == R.id.homeFragment) {
                    showExitConfirmationDialog()
                } else {
                    navController.navigateUp()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    @SuppressLint("SetTextI18n")
    private fun loadApi(thisMonth: Int) {
        val thisYear = getYear()
        homeViewModel.requestHome(
            RequestHome(
                "$thisYear-$thisMonth",
                getUserId()?.toInt() ?: savedUser!!.id_user
            )
        )
        homeViewModel.historyTransaction.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    with(binding) {
                        val response = it.data!!
                        val data = response.data
                        hIncomingGoods.text = "${data.sales} Barang"
                        tvTotalSeller.text = "${data.penjualan} Barang"
                        txtReturnProduct.text = "${data.pengembalian} Barang"
                        txtTotalStock.text = "${data.persediaan} Barang"
                    }
                }

                is NetworkResult.Loading -> {
                    showLoading()
                }

                is NetworkResult.Error -> {
                    hideLoading()
                    showErrorMessage(it.message!!)
                    Log.e("TAG", "loadApi: ${it.message}")
                }
            }
        }
    }

    private fun loadData() {
        val userSaved = readLoginResponse()
        viewModel.requestProfile(userSaved!!.user.id_user)
        viewModel.profileResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    Glide.with(requireContext())
                        .load(data.foto)
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.ic_no_image)
                        .into(binding.imgProfile)
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

    private fun showExitConfirmationDialog() {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Apakah Anda yakin?")
            .setContentText("")
            .setConfirmText("Ya, Keluar")
            .setCancelText("Batal")
            .setConfirmClickListener {
                it.dismissWithAnimation()
                requireActivity().finish()
//                        exitProcess(0)
            }
            .setCancelClickListener {
                it.dismissWithAnimation()
            }
            .show()
    }

    override fun onClickItem(data: Month) {
        loadApi(data.monthNumber)
    }


}