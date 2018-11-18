package com.example.administrator.glasshouse

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.SupportClass.Suggestion
import kotlinx.android.synthetic.main.activity_share_gate.*

class ShareGateActivity : AppCompatActivity() {

    val allNameList: ArrayList<String> = ArrayList()
    val mSuggestions: ArrayList<Suggestion> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_gate)

        getAllUser()

        // Custom từng dòng của Suggesttion
        search_user.setOnQueryChangeListener { oldQuery, newQuery ->
            run {
                if (oldQuery == "" && newQuery == "") {
                    search_user.clearSuggestions()
                } else {
                    search_user.showProgress()
                    search_user.swapSuggestions(getSuggestion(newQuery))
                    search_user.hideProgress()
                }
            }
        }

        search_user.setOnFocusChangeListener(object : FloatingSearchView.OnFocusChangeListener{
            override fun onFocusCleared() {

            }

            override fun onFocus() {
                search_user.showProgress()
                search_user.swapSuggestions(getSuggestion(search_user.query))
                search_user.hideProgress()
            }

        })

        search_user.setOnSearchListener(object : FloatingSearchView.OnSearchListener{
            override fun onSearchAction(currentQuery: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
                val suggestion : Suggestion = searchSuggestion as Suggestion
                Toast.makeText(this@ShareGateActivity,"Đã chọn" + suggestion.mName,Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun getSuggestion(newQuery: String?): ArrayList<Suggestion> {
        val suggestions: ArrayList<Suggestion> = ArrayList()
        for (suggestion in mSuggestions) {
            if (suggestion.mName.toLowerCase().contains(newQuery!!.toLowerCase())) {
                suggestions.add(suggestion)
            }
        }
        return suggestions
    }

    private fun getAllUser() {
        MyApolloClient.getApolloClient().query(
                GetAllUserQuery.builder().build()
        ).enqueue(object : ApolloCall.Callback<GetAllUserQuery.Data>() {
            override fun onResponse(response: Response<GetAllUserQuery.Data>) {
                val users = response.data()!!.allUsers!!
                for (user in users) {
                    val name = user.name()
                    val id = user.id()
                    val email = user.email()
                    val listPermission = user.gatePermission!!
                    val listServiceTag : ArrayList<String> = ArrayList()
                    for (item in listPermission){
                        listServiceTag.add(item.serviceTag!!)
                    }
                    Log.d("!getUser", name)
                    if (name != null) {
                        allNameList.add(name)
                        mSuggestions.add(Suggestion(name))
                    }
                }
            }

            override fun onFailure(e: ApolloException) {
                Log.d("!getUser", e.message)
            }
        })
    }
}
