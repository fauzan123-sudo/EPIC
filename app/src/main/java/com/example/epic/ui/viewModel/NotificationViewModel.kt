package com.example.epic.ui.viewModel

//@HiltViewModel
//class NotificationViewModel @Inject constructor(private val repository: NotificationRepository) :
//    ViewModel() {

//    private val _notifications: MutableStateFlow<Resource<List<Notification>>> =
//        MutableStateFlow(Resource.Loading())
//    val notifications: StateFlow<Resource<List<Notification>>> = _notifications
//
//    fun getNotificationsByNip(nip: String) {
//        viewModelScope.launch {
//            repository.getNotificationsByNip(nip).collect { result ->
//                _notifications.value = result
//            }
//        }
//    }

//}