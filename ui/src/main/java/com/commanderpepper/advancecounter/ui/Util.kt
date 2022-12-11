package com.commanderpepper.advancecounter.ui

import androidx.core.text.isDigitsOnly

fun String.isNumber(): Boolean {
    return if(this.first() == '-'){
        this.drop(1).toLongOrNull() != null
    }
    else{
        this.isDigitsOnly()
    }
}