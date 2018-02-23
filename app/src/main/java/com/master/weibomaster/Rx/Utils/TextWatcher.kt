package coms.pacs.pacs.Rx.Utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.lang.ref.SoftReference

/**
 * Created by 不听话的好孩子 on 2018/1/17.
 */
class TextWatcher :ObservableOnSubscribe<String> {
    private val etz: SoftReference<EditText>
    constructor(et: EditText){
        etz= SoftReference(et)
    }
    override fun subscribe(e: ObservableEmitter<String>) {
        if(etz.get()!=null){
            etz.get()!!.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!e.isDisposed){
                        e.onNext(s.toString())
                    }
                }
            })
        }
    }

}