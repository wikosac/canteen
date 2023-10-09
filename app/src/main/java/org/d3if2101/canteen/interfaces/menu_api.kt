package org.d3if2101.canteen.interfaces

import org.d3if2101.canteen.datamodels.MenuItem

enum class RequestType {
    READ, OFFLINE_UPDATE
}

interface MenuApi {
    fun onFetchSuccessListener(list: ArrayList<MenuItem>, requestType: RequestType)
}