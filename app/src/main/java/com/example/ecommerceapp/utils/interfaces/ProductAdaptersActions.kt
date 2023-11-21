package com.example.ecommerceapp.utils.interfaces

import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel

interface ProductAdaptersActions {
    fun makeActionSearch(generalDataSendedModel: GeneralDataSendedModel)
}