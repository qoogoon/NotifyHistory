package com.kumestudio.notify.constant

class Tag {
    companion object{
        val SERVICE get() = getTagName("SERVICE")
        val NotifyListAdapter get() = getTagName("NotifyListAdapter")

        private fun getTagName(tag : String) : String{
            return "[TAG_${tag}]"
        }
    }
}