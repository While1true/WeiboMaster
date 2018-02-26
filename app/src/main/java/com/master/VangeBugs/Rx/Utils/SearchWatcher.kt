package coms.pacs.pacs.Rx.Utils

import android.support.v7.widget.SearchView
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.lang.ref.SoftReference

/**
 * Created by 不听话的好孩子 on 2018/1/17.
 */
class SearchWatcher :ObservableOnSubscribe<String> {
    private val etz: SoftReference<SearchView>
    constructor(et: SearchView){
        etz= SoftReference(et)
    }
    override fun subscribe(e: ObservableEmitter<String>) {
        if(etz.get()!=null){
            etz.get()!!.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String): Boolean {
                    if(!e.isDisposed){
                        e.onNext(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if(!e.isDisposed){
                        e.onNext(newText)
                    }
                    return true
                }

            })
        }
    }

}