package hr.algebra.nasa.utils

import android.content.Context
import android.text.TextUtils
import android.widget.Toast

class ToastUtils {

    companion object{

        private var toast : Toast? = null
        fun show(context: Context?, message:String?){
            if(TextUtils.isEmpty(message)) return
            if (toast == null) toast = Toast.makeText(context,message,Toast.LENGTH_SHORT)
            else toast?.setText(message)
            toast?.show()
        }

        fun show(context: Context?, message:Int){
            if(message == 0) return
            if(toast == null) toast = Toast.makeText(context,message,Toast.LENGTH_SHORT)
            else toast?.setText(message)
            toast?.show()
        }

    }
}