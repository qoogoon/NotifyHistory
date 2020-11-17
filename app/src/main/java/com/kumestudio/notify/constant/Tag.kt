package com.kumestudio.notify.constant

class Tag {
    companion object{
        val SERVICE get() = getTagName("SERVICE")

        private fun getTagName(tag : String) : String{
            return "[TAG_${tag}]"
        }
    }
}