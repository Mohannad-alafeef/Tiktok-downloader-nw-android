package com.example.tiktokdownloaderdemo.extention

import android.view.View


fun View.toggle(){
    if (this.visibility == View.VISIBLE) this.visibility = View.GONE
    else this.visibility = View.VISIBLE
}