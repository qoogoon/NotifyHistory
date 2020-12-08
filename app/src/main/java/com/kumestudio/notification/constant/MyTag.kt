package com.kumestudio.notification.constant

class MyTag {
    companion object{
        val SERVICE get() = getTagName("SERVICE")
        val NotifyListAdapter get() = getTagName("NotifyListAdapter")
        val MainActivity get() = getTagName("MainActivity")

        private fun getTagName(tag : String) : String{
            return "[TAG_${tag}]"
        }
    }
}